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
YAHOO.namespace("ur.sponsor");

// action to perform when submitting the personal sponsors.
var mySponsorAction = basePath + 'admin/getSponsors.action';

// actions for adding and removing folders
var updateSponsorAction = basePath + 'admin/updateSponsor.action';
var newSponsorAction = basePath + 'admin/createSponsor.action';
var deleteSponsorAction = basePath + 'admin/deleteSponsor.action';
var getSponsorAction = basePath + 'admin/getSponsor.action';

// object to hold the specified sponsor data.
var mySponsorTable = new YAHOO.ur.table.Table('mySponsors', 'newSponsors');


/**
 * sponsor namespace
 */
YAHOO.ur.sponsor = {

	/**
	 *  Function that retireves sponsor information
	 *  based on the given sponsor id.
	 *
	 *  The sponsor id used to get the folder.
	 */
	 
	getSponsors : function(rowStart, startPageNumber, currentPageNumber, order)
	{
		var callback =
		{
		    success: function(o) 
		    {
		        // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
		            var divToUpdate = document.getElementById('newSponsors');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get sponsor Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
			
			var transaction = YAHOO.util.Connect.asyncRequest('GET', 
		        mySponsorAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(),
		        callback, null);			
		
	},	
	
	 /** 
	  * clear out any form data messages or input
	  * in the new sponsor form
	  */
	clearSponsorForm : function()
	{
        // clear out any errors
        var div = document.getElementById('sponsorError');
        div.innerHTML = "";
		
		document.getElementById('newSponsorForm_name').value = "";
		document.getElementById('newSponsorForm_description').value = "";
		document.getElementById('newSponsorForm_id').value = "";
		document.newSponsorForm.newSponsor.value = "true";
	},
	
    /**
     * Set all sponsor id's form
     */
    setCheckboxes : function()
    {
        checked = document.mySponsors.checkAllSetting.checked;
        var sponsorIds = document.getElementsByName('sponsorIds');
        urUtil.setCheckboxes(sponsorIds, checked);
        
    }, 
 	
	/**
	 * Creates a YUI new sponsor modal dialog for when a user wants to create 
	 * a new sponsor
	 *
	 */
	createNewSponsorDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() {
			this.submit();
		};
			
		// handle a cancel of the adding sponsor dialog
		var handleCancel = function() {
		    YAHOO.ur.sponsor.newSponsorDialog.hide();
		    YAHOO.ur.sponsor.clearSponsorForm();
		};
		
		var handleSuccess = function(o) 
		{
			YAHOO.ur.util.wait.waitDialog.hide();
		    // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a sponsor
		        var response = o.responseText;
		        var sponsorForm = document.getElementById('newSponsorDialogFields');
		    
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        sponsorForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("newSponsorForm_success").value;
		  
		        //if the sponsor was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.sponsor.newSponsorDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the sponsor was added
		            YAHOO.ur.sponsor.newSponsorDialog.hide();
		            YAHOO.ur.sponsor.clearSponsorForm();
		        }
		        mySponsorTable.submitForm(mySponsorAction);
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
			YAHOO.ur.util.wait.waitDialog.hide();
		    alert('sponsor submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new sponsor button is clicked.
		YAHOO.ur.sponsor.newSponsorDialog = new YAHOO.widget.Dialog('newSponsorDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
	   	// show and center the sponsor dialog
        YAHOO.ur.sponsor.newSponsorDialog.showDialog = function()
        {
            YAHOO.ur.sponsor.newSponsorDialog.center();
            YAHOO.ur.sponsor.newSponsorDialog.show();
        }
        
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.sponsor.newSponsorDialog.validate = function() {
		    var name = document.getElementById('newSponsorForm_name').value;
			if (name == "" || name == null) {
			    alert('Sponsor name must be entered');
				return false;
			} else {
				return true;
			}
		};
		
		// Submit form
		YAHOO.ur.sponsor.newSponsorDialog.submit = function() 
		{   
			
		    YAHOO.util.Connect.setForm('newSponsorForm');
		     	    
		    if( YAHOO.ur.sponsor.newSponsorDialog.validate() )
		    {
		    	YAHOO.ur.util.wait.waitDialog.showDialog();
		        //based on what we need to do (update or create a 
		        // new sponsor) based on the action.
	            var action = newSponsorAction;
		        if( document.newSponsorForm.newSponsor.value != 'true')
		        {
		           action = updateSponsorAction;
		        }
	
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            action, callback);
	        }		
	    }
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.sponsor.newSponsorDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showSponsor", "click", 
		    YAHOO.ur.sponsor.newSponsorDialog.showDialog, 
		    YAHOO.ur.sponsor.newSponsorDialog, true);
	},   
	    
    
    /**
     * function to edit sponsor information
     */
    editSponsor : function(id)
    {	    
	    /*
         * This call back updates the html when a editing a sponsor
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('newSponsorDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
	                document.newSponsorForm.newSponsor.value = "false";
	                YAHOO.ur.sponsor.newSponsorDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('edit sponsor failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getSponsorAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
    },
    
	
	 /** 
	  * clear out any form data messages or input
	  * in the new sponsor form
	  */
	clearDeleteSponsorForm : function()
	{
	    var sponsorError = document.getElementById('newSponsorForm_nameError');
	    var div = document.getElementById('sponsorError');
		//clear out any error information
		if( sponsorError != null )
		{
		    if( sponsorError.innerHTML != null 
		        || sponsorError.innerHTML != "")
		    { 
		        div.removeChild(sponsorError);
		    }
		}
	},
	
	/**
	 * Creates a YUI new sponsor modal dialog for when a user wants to create 
	 * a new sponsor
	 *
	 */
	createDeleteSponsorDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
		    YAHOO.util.Connect.setForm('mySponsors');
		    
		    //delete the sponsor
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deleteSponsorAction, callback);
		};
		
			
		// handle a cancel of deleting sponsor dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.sponsor.deleteSponsorDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
		    // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a sponsor
		        var response = eval("("+o.responseText+")");
		    
		        //if the sponsor was not deleted then show the user the error message.
		        // received from the server
		        if( response.sponsorDeleted == "false" )
		        {
		            var deleteSponsorError = document.getElementById('form_deleteSponsorError');
	                deleteSponsorError.innerHTML = '<p id="newDeleteSponsorError">' 
	                + response.message + '</p>';
	                YAHOO.ur.sponsor.deleteSponsorDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the sponsors were deleted
		            YAHOO.ur.sponsor.deleteSponsorDialog.hide();
		            YAHOO.ur.sponsor.clearDeleteSponsorForm();
		        }
		        // reload the table
		        mySponsorTable.submitForm(mySponsorAction);
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('delete sponsor submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new sponsor button is clicked.
		YAHOO.ur.sponsor.deleteSponsorDialog = new YAHOO.widget.Dialog('deleteSponsorDialog', 
	        { width : "400px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
			
	    // show and center the department dialog
        YAHOO.ur.sponsor.deleteSponsorDialog.showDialog = function()
        {
            YAHOO.ur.sponsor.deleteSponsorDialog.center();
            YAHOO.ur.sponsor.deleteSponsorDialog.show();
        }
        	   
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,
											         failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.sponsor.deleteSponsorDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showDeleteSponsor", "click", 
		    YAHOO.ur.sponsor.deleteSponsorDialog.showDialog, 
		    YAHOO.ur.sponsor.deleteSponsorDialog, true);
	},
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() {
	    YAHOO.ur.sponsor.getSponsors(0, 1, 1, 'asc');
	    YAHOO.ur.sponsor.createNewSponsorDialog();
	    YAHOO.ur.sponsor.createDeleteSponsorDialog();
	}	
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.sponsor.init);