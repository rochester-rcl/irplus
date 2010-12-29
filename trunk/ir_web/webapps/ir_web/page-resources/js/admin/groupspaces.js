/*
   Copyright 2008-2010 University of Rochester

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
 * This code is for dealing with adding and removing group spaces
 */
YAHOO.namespace("ur.groupspace");

//actions for adding,updating and removing content types
var updateGroupSpaceAction = basePath + 'admin/updateGroupSpace.action';
var newGroupSpaceAction = basePath + 'admin/createGroupSpace.action';
var deleteGroupSpaceAction = basePath + 'admin/deleteGroupSpace.action';
var getGroupSpaceAction = basePath + 'admin/getGroupSpace.action';
/**
 * content type namespace
 */
YAHOO.ur.groupspace = {
	
	 /** 
	  * clear out any form data messages or input
	  * in the groupspace form
	  */
	clearGroupSpaceForm : function()
	{
	    
	    // clear out any errors
        var div = document.getElementById('groupspaceError');
        div.innerHTML = "";
	    
	    document.getElementById('groupSpaceName').value = "";
		document.getElementById('groupSpaceDescription').value = "";
		document.getElementById('groupSpaceId').value = "";
		document.addGroupSpace.newGroupSpace.value = "true";
	},
	
	
	/**
	 * Creates a YUI new content type modal dialog for when a user wants to create 
	 * a new groupspace
	 *
	 */
	createCreateGroupSpaceDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
			
		// handle a cancel of the adding content type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.groupspace.newGroupSpaceDialog.hide();
		    YAHOO.ur.groupspace.clearGroupSpaceForm();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a content type
		        var response = o.responseText;
		        var groupspaceForm = document.getElementById('groupSpaceDialogFields');
		    
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        groupspaceForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("success").value;
		    
		  
		        //if the content type was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.groupspace.newGroupSpaceDialog.showDialog();
		        }
		        else
		        {
		        	var id = document.getElementById('groupSpaceId').value;
		        	window.location = getGroupSpaceAction+ '?id=' + id;
		        }
		    }
	        
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
			YAHOO.ur.util.wait.waitDialog.hide();
		    alert('groupspace submission failed ' + o.status + ' status text ' + o.statusText );
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new content type button is clicked.
		YAHOO.ur.groupspace.newGroupSpaceDialog = new YAHOO.widget.Dialog('newGroupSpaceDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
		//submit form
		YAHOO.ur.groupspace.newGroupSpaceDialog.submit = function() 
		{
		    YAHOO.util.Connect.setForm('addGroupSpace');
		    if( YAHOO.ur.groupspace.newGroupSpaceDialog.validate() )
		    {
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            		newGroupSpaceAction, callback);
	        }
	     }		

	    
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.groupspace.newGroupSpaceDialog.validate = function() 
		{
		    var name = document.getElementById('groupSpaceName').value;
			if (name == "" || name == null) {
			    alert('A name must be entered');
				return false;
			} else {
				return true;
			}
		};			
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,   failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.groupspace.newGroupSpaceDialog.render();			
			
	    // show and center the dialog box
	    YAHOO.ur.groupspace.newGroupSpaceDialog.showDialog = function()
	    {
	        YAHOO.ur.groupspace.newGroupSpaceDialog.center();
	        YAHOO.ur.groupspace.newGroupSpaceDialog.show()
	    }

	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showGroupSpace", "click", 
		    YAHOO.ur.groupspace.newGroupSpaceDialog.showDialog, 
		    YAHOO.ur.groupspace.newGroupSpaceDialog, true);
		    
	},
	
	/**
	 * Creates a YUI new modal dialog for when a user wants to delete 
	 * a content type
	 *
	 */
	createDeleteGroupSpaceDialog :function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
		    YAHOO.util.Connect.setForm('deleteGroupSpaceForm');
		    
		    //delete the content type
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        		deleteGroupSpaceAction, callback);
		};
		
			
		// handle a cancel of deleting content type dialog
		var handleCancel = function() 
		{
			YAHOO.ur.groupspace.deleteGroupSpaceDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        var div = document.getElementById('groupSpaceTable');
		        div.innerHTML = o.responseText;
		        YAHOO.ur.groupspace.deleteGroupSpaceDialog.hide();
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
			YAHOO.ur.groupspace.deleteGroupSpaceDialog.hide();
		    alert('delete group space failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new content type button is clicked.
		YAHOO.ur.groupspace.deleteGroupSpaceDialog = new YAHOO.widget.Dialog('deleteGroupSpaceDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
			
	    // show and center the delete dialog
		YAHOO.ur.groupspace.deleteGroupSpaceDialog.showDialog = function()
	    {
			YAHOO.ur.groupspace.deleteGroupSpaceDialog.center();
			YAHOO.ur.groupspace.deleteGroupSpaceDialog.show();
	    }
	    
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
		// Render the Dialog
		YAHOO.ur.groupspace.deleteGroupSpaceDialog.render();

	},
	
	// delete the group space with the given id
	deleteGroupSpace : function(id)
	{
		document.getElementById('deleteId').value=id;
		YAHOO.ur.groupspace.deleteGroupSpaceDialog.showDialog();
	},
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	    YAHOO.ur.groupspace.createCreateGroupSpaceDialog();
	    YAHOO.ur.groupspace.createDeleteGroupSpaceDialog();
  	}
	
}	

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.groupspace.init);