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
YAHOO.namespace("ur.contributor.type");

// action to perform when submitting the personal contributor types.
var myContributorTypeAction = basePath + 'admin/getContributorTypes.action';

// actions for adding and removing folders
var updateContributorTypeAction = basePath + 'admin/updateContributorType.action';
var newContributorTypeAction = basePath + 'admin/createContributorType.action';
var deleteContributorTypeAction = basePath + 'admin/deleteContributorType.action';
var getContributorTypeAction = basePath + 'admin/getContributorType.action';

// object to hold the specified contributor type data.
var myContributorTypeTable = new  YAHOO.ur.table.Table('myContributorTypes', 'newContributorTypes');

/**
 * contributor namespace
 */
YAHOO.ur.contributor.type = {

	/**
	 *  Function that retireves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getContributorTypes : function(rowStart, startPageNumber, currentPageNumber, order)
	{
		 var callback =
         {
		    success: function(o) 
		    {
		    	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
		            var divToUpdate = document.getElementById('newContributorTypes');
		            divToUpdate.innerHTML = o.responseText;
		        } 
		    },
			
			failure: function(o) 
			{
			    alert('Get contributor type Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        myContributorTypeAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(), 
	        callback, null);		
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the new contributor type form
	  */
	clearContributorTypeForm : function()
	{
	    // clear out any errors
        var div = document.getElementById('contributorTypeError');
        div.innerHTML = "";
        
        document.getElementById('newContributorTypeForm_id').value="";
		document.getElementById('newContributorTypeForm_name').value="";
		document.getElementById('newContributorTypeForm_description').value="";
	
		document.newContributorType.newContributorType.value = "true";
	},
	
	/**
     * Selects or unselects all affilation checkboxes
     */
    setCheckboxes : function()
    {
        checked = document.myContributorTypes.checkAllSetting.checked;
        var contributorTypeIds = document.getElementsByName('contributorTypeIds');
        urUtil.setCheckboxes(contributorTypeIds, checked);
    },
   
	/**
	 * Creates a YUI new contributor type modal dialog for when a user wants to create 
	 * a new contributor type
	 *
	 */
	createNewContributorTypeDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() {
			this.submit();
		};
		
			
		// handle a cancel of the adding contributor type dialog
		var handleCancel = function() {
		    YAHOO.ur.contributor.type.contributorTypeDialog.hide();
		    YAHOO.ur.contributor.type.clearContributorTypeForm();
		};
		
		var handleSuccess = function(o) {
		
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a contributor type
		        var response = eval("("+o.responseText+")");
		    
		        //if the contributor type was not added then show the user the error message.
		        // received from the server
		        if( response.contributorTypeAdded == "false" )
		        {
		            var contributorTypeNameError = document.getElementById('contributorTypeError');
	                contributorTypeNameError.innerHTML = '<p id="newContributorTypeForm_nameError">' + response.message + '</p>';
	                YAHOO.ur.contributor.type.contributorTypeDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the contributor type was added
		            YAHOO.ur.contributor.type.contributorTypeDialog.hide();
		            YAHOO.ur.contributor.type.clearContributorTypeForm();
		        }
		        myContributorTypeTable.submitForm(myContributorTypeAction);
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('contributor type submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new contributor type button is clicked.
		YAHOO.ur.contributor.type.contributorTypeDialog = new YAHOO.widget.Dialog('newContributorTypeDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
		YAHOO.ur.contributor.type.contributorTypeDialog.submit = function()
		{  
		    YAHOO.util.Connect.setForm('newContributorType');
		    
		    if( YAHOO.ur.contributor.type.contributorTypeDialog.validate() )
		    {
			    //based on what we need to do (update or create a 
			    // new contributor type) based on the action.
		        var action = newContributorTypeAction;
			    if( document.newContributorType.newContributorType.value != 'true')
			    {
			       action = updateContributorTypeAction;
			    }
		
		        var cObj = YAHOO.util.Connect.asyncRequest('post',
		        action, callback);		
		    }
		};
			
	    //center and show the contributorTypeDialog
	    YAHOO.ur.contributor.type.contributorTypeDialog.showDialog = function()
	    {
	        YAHOO.ur.contributor.type.contributorTypeDialog.center();
	        YAHOO.ur.contributor.type.contributorTypeDialog.show();
	    };
	    
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.contributor.type.contributorTypeDialog.validate = function() 
		{
		
		    var name = document.getElementById('newContributorTypeForm_name').value;

			if (name == "" || name == null) {
			    alert('Contributor type name must be entered');
				return false;
			} else {
				return true;
			}
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.contributor.type.contributorTypeDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showContributorType", "click", 
		    YAHOO.ur.contributor.type.contributorTypeDialog.showDialog, 
		    YAHOO.ur.contributor.type.contributorTypeDialog, true);
	},
	
    /**
     * function to edit contributor type information
     */
    editContributorType2 : function(id)
    {	    
	    /*
         * This call back updates the html when a editing a content type
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('contributorTypeDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                   	document.newContributorType.newContributorType.value = "false";
	                YAHOO.ur.contributor.type.contributorTypeDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get contributor type failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getContributorTypeAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
     },
	
	 /** 
	  * clear out any form data messages or input
	  * in the new contributor type form
	  */
	clearDeleteContributorTypeForm : function()
	{
	    var contributorTypeError = document.getElementById('newContributorTypeForm_nameError');
	    var div = document.getElementById('contributorTypeError');
		//clear out any error information
		if( contributorTypeError != null )
		{
		    if( contributorTypeError.innerHTML != null 
		        || contributorTypeError.innerHTML != "")
		    { 
		        div.removeChild(contributorTypeError);
		    }
		}
	},
	
	/**
	 * Creates a YUI new contributor type modal dialog for when a user wants to create 
	 * a new contributor type
	 *
	 */
	createDeleteContributorTypeDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() {
		    YAHOO.util.Connect.setForm('myContributorTypes');
		    
		    //delete the contributor type
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deleteContributorTypeAction, callback);
		};
		
			
		// handle a cancel of deleting contributor type dialog
		var handleCancel = function() {
		    YAHOO.ur.contributor.type.deleteContributorTypeDialog.hide();
		};
		
		var handleSuccess = function(o) {
		
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a contributor type
		        var response = eval("("+o.responseText+")");
		    
		        //if the contributor type was not deleted then show the user the error message.
		        // received from the server
		        if( response.contributorTypeDeleted == "false" )
		        {
		            var deleteContributorTypeError = document.getElementById('form_deleteContributorTypeError');
	                deleteContributorTypeError.innerHTML = '<p id="newDeleteContributorTypeError">' 
	                + response.message + '</p>';
	                YAHOO.ur.contributor.type.deleteContributorTypeDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the contributor types were deleted
		            YAHOO.ur.contributor.type.deleteContributorTypeDialog.hide();
		            YAHOO.ur.contributor.type.clearDeleteContributorTypeForm();
		        }
		        // reload the table
		        myContributorTypeTable.submitForm(myContributorTypeAction);
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('contributor type submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new contributor type button is clicked.
		YAHOO.ur.contributor.type.deleteContributorTypeDialog = new YAHOO.widget.Dialog('deleteContributorTypeDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
	     
	    //center and show the delete dialog
	    YAHOO.ur.contributor.type.deleteContributorTypeDialog.showDialog = function()
	    {
	        YAHOO.ur.contributor.type.deleteContributorTypeDialog.center();
	        YAHOO.ur.contributor.type.deleteContributorTypeDialog.show();
	    }
	   
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.contributor.type.deleteContributorTypeDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showDeleteContributorType", "click", 
		    YAHOO.ur.contributor.type.deleteContributorTypeDialog.showDialog, 
		    YAHOO.ur.contributor.type.deleteContributorTypeDialog, true);
	},
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	    YAHOO.ur.contributor.type.getContributorTypes(0,1,1,'asc');
	    YAHOO.ur.contributor.type.createNewContributorTypeDialog();
	    YAHOO.ur.contributor.type.createDeleteContributorTypeDialog();
	}	
}	

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.contributor.type.init);