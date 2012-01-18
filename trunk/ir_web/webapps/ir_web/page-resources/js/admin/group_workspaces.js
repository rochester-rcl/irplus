/*
   Copyright 2008-2011 University of Rochester

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
var updateGroupWorkspaceAction = basePath + 'admin/updateGroupWorkspace.action';
var newGroupWorkspaceAction = basePath + 'admin/createGroupWorkspace.action';
var deleteGroupWorkspaceAction = basePath + 'admin/deleteGroupWorkspace.action';
var getGroupWorkspaceAction = basePath + 'admin/getGroupWorkspace.action';
/**
 * content type namespace
 */
YAHOO.ur.groupspace = {
	
	 /** 
	  * clear out any form data messages or input
	  * in the groupspace form
	  */
	clearGroupWorkspaceForm : function()
	{
	    
	    // clear out any errors
        var div = document.getElementById('groupWorkspaceError');
        div.innerHTML = "";
	    
	    document.getElementById('groupWorkspaceName').value = "";
		document.getElementById('groupWorkspaceDescription').value = "";
		document.getElementById('groupWorkspaceId').value = "";
		document.addGroupWorkspace.newGroupWorkspace.value = "true";
	},
	
	
	/**
	 * Creates a YUI new content type modal dialog for when a user wants to create 
	 * a new groupspace
	 *
	 */
	createCreateGroupWorkspaceDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
			
		// handle a cancel of the adding content type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.groupspace.newGroupWorkspaceDialog.hide();
		    YAHOO.ur.groupspace.clearGroupWorkspaceForm();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a content type
		        var response = o.responseText;
		        var groupspaceForm = document.getElementById('groupWorkspaceDialogFields');
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        groupspaceForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("newGroupWorkspaceFormSuccess").value;
		  
		        //if the content type was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.groupspace.newGroupWorkspaceDialog.showDialog();
		        }
		        else
		        {
		        	var id = document.getElementById('groupWorkspaceId').value;
		        	window.location = getGroupWorkspaceAction+ '?id=' + id;
		        }
		    }
	        
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
			YAHOO.ur.util.wait.waitDialog.hide();
		    alert('group workspace submission failed ' + o.status + ' status text ' + o.statusText );
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new content type button is clicked.
		YAHOO.ur.groupspace.newGroupWorkspaceDialog = new YAHOO.widget.Dialog('newGroupWorkspaceDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
		//submit form
		YAHOO.ur.groupspace.newGroupWorkspaceDialog.submit = function() 
		{
		    YAHOO.util.Connect.setForm('addGroupWorkspace');
		    if( YAHOO.ur.groupspace.newGroupWorkspaceDialog.validate() )
		    {
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            		newGroupWorkspaceAction, callback);
	        }
	     }		

	    
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.groupspace.newGroupWorkspaceDialog.validate = function() 
		{
		    var name = document.getElementById('groupWorkspaceName').value;
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
		YAHOO.ur.groupspace.newGroupWorkspaceDialog.render();			
			
	    // show and center the dialog box
	    YAHOO.ur.groupspace.newGroupWorkspaceDialog.showDialog = function()
	    {
	        YAHOO.ur.groupspace.newGroupWorkspaceDialog.center();
	        YAHOO.ur.groupspace.newGroupWorkspaceDialog.show()
	    }

	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showGroupWorkspace", "click", 
		    YAHOO.ur.groupspace.newGroupWorkspaceDialog.showDialog, 
		    YAHOO.ur.groupspace.newGroupWorkspaceDialog, true);
		    
	},
	
	/**
	 * Creates a YUI new modal dialog for when a user wants to delete 
	 * a content type
	 *
	 */
	createDeleteGroupWorkspaceDialog :function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
		    YAHOO.util.Connect.setForm('deleteGroupWorkspaceForm');
		    
		    //delete the content type
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        		deleteGroupWorkspaceAction, callback);
		};
		
			
		// handle a cancel of deleting content type dialog
		var handleCancel = function() 
		{
			YAHOO.ur.groupspace.deleteGroupWorkspaceDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        var div = document.getElementById('groupWorkspaceTable');
		        div.innerHTML = o.responseText;
		        YAHOO.ur.groupspace.deleteGroupWorkspaceDialog.hide();
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
			YAHOO.ur.groupspace.deleteGroupWorkspaceDialog.hide();
		    alert('delete group workspace failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new content type button is clicked.
		YAHOO.ur.groupspace.deleteGroupWorkspaceDialog = new YAHOO.widget.Dialog('deleteGroupWorkspaceDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
			
	    // show and center the delete dialog
		YAHOO.ur.groupspace.deleteGroupWorkspaceDialog.showDialog = function()
	    {
			YAHOO.ur.groupspace.deleteGroupWorkspaceDialog.center();
			YAHOO.ur.groupspace.deleteGroupWorkspaceDialog.show();
	    }
	    
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
		// Render the Dialog
		YAHOO.ur.groupspace.deleteGroupWorkspaceDialog.render();

	},
	
	// delete the group space with the given id
	deleteGroupWorkspace : function(id)
	{
		document.getElementById('deleteId').value=id;
		YAHOO.ur.groupspace.deleteGroupWorkspaceDialog.showDialog();
	},
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	    YAHOO.ur.groupspace.createCreateGroupWorkspaceDialog();
	    YAHOO.ur.groupspace.createDeleteGroupWorkspaceDialog();
	    var myTabs = new YAHOO.widget.TabView("group-workspace-tabs");
  	}
	
}	

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.groupspace.init);