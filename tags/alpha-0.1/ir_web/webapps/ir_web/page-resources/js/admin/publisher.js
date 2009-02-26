/*
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

/**
 * This code is for dealing with adding and removing folders 
 * in the workspace.
 */
YAHOO.namespace("ur.publisher");

// action to perform when submitting the personal publishers.
var myPublisherAction = basePath + 'admin/getPublishers.action';

// actions for adding and removing folders
var updatePublisherAction = basePath + 'admin/updatePublisher.action';
var newPublisherAction = basePath + 'admin/createPublisher.action';
var deletePublisherAction = basePath + 'admin/deletePublisher.action';
var getPublisherAction = basePath + 'admin/getPublisher.action';

// object to hold the specified publisher data.
var myPublisherTable = new YAHOO.ur.table.Table('myPublishers', 'newPublishers');


/**
 * sponsor namespace
 */
YAHOO.ur.publisher = {
	
	/**
	 *  Function that retireves publisher information
	 *  based on the given publisher id.
	 *
	 *  The publisher id used to get the folder.
	 */
	getPublishers : function(rowStart, startPageNumber, currentPageNumber, order)
	{
		var getPublisherCallback =
		{
		    success: function(o) 
		    {
		        // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
		            var divToUpdate = document.getElementById('newPublishers');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get publisher Failure ' + o.status + ' status text ' + o.statusText );
			}
		}

	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        myPublisherAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(), 
	        getPublisherCallback, null);	
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the new publisher form
	  */
	clearPublisherForm : function()
	{
        // clear out any errors
        var div = document.getElementById('publisherError');
        div.innerHTML = "";
		
		document.getElementById('newPublisherForm_name').value = "";
		document.getElementById('newPublisherForm_description').value = "";
		document.getElementById('newPublisherForm_id').value = "";
		document.newPublisherForm.newPublisher.value = "true";
	},

    /**
     * Set all publisher id's form
     */
    setCheckboxes : function()
    {
        checked = document.myPublishers.checkAllSetting.checked;
        var publisherIds = document.getElementsByName('publisherIds');
        urUtil.setCheckboxes(publisherIds, checked);
        
    }, 	

	/**
	 * Creates a YUI new publisher modal dialog for when a user wants to create 
	 * a new publisher
	 *
	 */
	createNewPublisherDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
			
		// handle a cancel of the adding publisher dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.publisher.newPublisherDialog.hide();
		    YAHOO.ur.publisher.clearPublisherForm();
		};
		
		var handleSuccess = function(o) 
		{
		    // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a publisher
		        var response = o.responseText;
		        var publisherForm = document.getElementById('newPublisherDialogFields');
		    
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        publisherForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("newPublisherForm_success").value;
		    
		        //if the publisher was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.publisher.newPublisherDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the publisher was added
		            YAHOO.ur.publisher.newPublisherDialog.hide();
		            YAHOO.ur.publisher.clearPublisherForm();
		        }
		        myPublisherTable.submitForm(myPublisherAction);
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('publisher submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new publisher button is clicked.
		YAHOO.ur.publisher.newPublisherDialog = new YAHOO.widget.Dialog('newPublisherDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );

	   	// show and center the sponsor dialog
        YAHOO.ur.publisher.newPublisherDialog.showDialog = function()
        {
            YAHOO.ur.publisher.newPublisherDialog.center();
            YAHOO.ur.publisher.newPublisherDialog.show();
        }		

		YAHOO.ur.publisher.newPublisherDialog.submit = function() 
		{   
		    YAHOO.util.Connect.setForm('newPublisherForm');
		    
		    	    
		    if( YAHOO.ur.publisher.newPublisherDialog.validate() )
		    {
		        //based on what we need to do (update or create a 
		        // new publisher) based on the action.
	            var action = newPublisherAction;
		        if( document.newPublisherForm.newPublisher.value != 'true')
		        {
		           action = updatePublisherAction;
		        }
	
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            action, callback);
	        }	
	    }		
	   
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.publisher.newPublisherDialog.validate = function() {
		    var name = document.getElementById('newPublisherForm_name').value;
			if (name == "" || name == null) {
			    alert('A Publisher name must be entered');
				return false;
			} else {
				return true;
			}
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.publisher.newPublisherDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showPublisher", "click", 
		    YAHOO.ur.publisher.newPublisherDialog.showDialog, 
		    YAHOO.ur.publisher.newPublisherDialog, true);
		    
	 },
    
    /**
     * function to edit publisher information
     */
    editPublisher : function(id)
    {	    
	    /*
         * This call back updates the html when a editing a publisher
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('newPublisherDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
	                document.newPublisherForm.newPublisher.value = "false";
	                YAHOO.ur.publisher.newPublisherDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get publisher failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getPublisherAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
    },
    
	
	 /** 
	  * clear out any form data messages or input
	  * in the new publisher form
	  */
	clearDeletePublisherForm : function()
	{
	    var publisherError = document.getElementById('newPublisherForm_nameError');
	    var div = document.getElementById('publisherError');
		//clear out any error information
		if( publisherError != null )
		{
		    if( publisherError.innerHTML != null 
		        || publisherError.innerHTML != "")
		    { 
		        div.removeChild(publisherError);
		    }
		}
	},
	
	/**
	 * Creates a YUI new publisher modal dialog for when a user wants to create 
	 * a new publisher
	 *
	 */
	createDeletePublisherDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
		    YAHOO.util.Connect.setForm('myPublishers');
		    
		    //delete the publisher
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deletePublisherAction, callback);
		};
		
			
		// handle a cancel of deleting publisher dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.publisher.deletePublisherDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
		    // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {		
		        //get the response from adding a publisher
		        var response = eval("("+o.responseText+")");
		    
		        //if the publisher was not deleted then show the user the error message.
		        // received from the server
		        if( response.publisherDeleted == "false" )
		        {
		            var deletePublisherError = document.getElementById('form_deletePublisherError');
	                deletePublisherError.innerHTML = '<p id="newDeletePublisherError">' 
	                + response.message + '</p>';
	                YAHOO.ur.publisher.deletePublisherDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the publishers were deleted
		            YAHOO.ur.publisher.clearDeletePublisherForm();
		            YAHOO.ur.publisher.deletePublisherDialog.hide();
		        }
		        // reload the table
		        myPublisherTable.submitForm(myPublisherAction);
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('publisher submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new publisher button is clicked.
		YAHOO.ur.publisher.deletePublisherDialog = new YAHOO.widget.Dialog('deletePublisherDialog', 
	        { width : "400px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
			
	   	// show and center the sponsor dialog
        YAHOO.ur.publisher.deletePublisherDialog.showDialog = function()
        {
            YAHOO.ur.publisher.deletePublisherDialog.center();
            YAHOO.ur.publisher.deletePublisherDialog.show();
        }	
        	   
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.publisher.deletePublisherDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showDeletePublisher", "click", 
		    YAHOO.ur.publisher.deletePublisherDialog.showDialog, 
		    YAHOO.ur.publisher.deletePublisherDialog, true);
	},
	
	
	/** initialize the page
	 *  this is called once the dom has been created
	 */ 
	init : function() 
	{
	    YAHOO.ur.publisher.getPublishers(0, 1, 1, 'asc');
	    YAHOO.ur.publisher.createNewPublisherDialog();
	    YAHOO.ur.publisher.createDeletePublisherDialog();
	}

}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.publisher.init);