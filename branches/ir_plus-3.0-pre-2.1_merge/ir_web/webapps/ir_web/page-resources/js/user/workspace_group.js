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
 * This code is for dealing with adding and removing folders 
 * in the workspace.
 */
YAHOO.namespace("ur.user.workspace_group");


//actions for adding,updating and removing content types
var updateGroupAction = basePath + 'user/updateWorkspaceGroup.action';
var newGroupAction = basePath + 'user/createWorkspaceGroup.action';
var deleteGroupAction = basePath + 'user/deleteWorkspaceGroup.action';
var getWorkspaceGroupsAction = basePath + 'user/viewWorkspaceGroups.action';

/**
 * content type namespace
 */
YAHOO.ur.user.workspace_group = {
	
	/**
	 * Creates a YUI new content type modal dialog for when a user wants to create 
	 * a new group for a workspace
	 *
	 */
	createCreateWorkspaceGroupDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
			
		// handle a cancel of the adding content type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.user.workspace_group.newWorkspaceGroupDialog.hide();
		    YAHOO.ur.user.workspace_group.clearForm();
		};
		
		var handleSuccess = function(o) 
		{	
			// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a content type
		        var response = o.responseText;
		        var groupspaceForm = document.getElementById('workspaceGroupDialogFields');
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
	                YAHOO.ur.user.workspace_group.newWorkspaceGroupDialog.showDialog();
		        }
		        else
		        {
		        	YAHOO.ur.user.workspace_group.clearForm();
		        	var workspaceId = document.getElementById("groupWorkspaceId").value;
		        	YAHOO.ur.user.workspace_group.getGroups(workspaceId);
		        	YAHOO.ur.user.workspace_group.newWorkspaceGroupDialog.hide();
		        }
		    }
	        
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('group workspace submission failed ' + o.status + ' status text ' + o.statusText );
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new content type button is clicked.
		YAHOO.ur.user.workspace_group.newWorkspaceGroupDialog = new YAHOO.widget.Dialog('newWorkspaceGroupDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
		//submit form
		YAHOO.ur.user.workspace_group.newWorkspaceGroupDialog.submit = function() 
		{
		    YAHOO.util.Connect.setForm('addWorkspaceGroup');
		    if( YAHOO.ur.user.workspace_group.newWorkspaceGroupDialog.validate() )
		    {
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            		newGroupAction, callback);
	        }
	     }		

	    
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.user.workspace_group.newWorkspaceGroupDialog.validate = function() 
		{
		    var name = document.getElementById('groupName').value;
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
		YAHOO.ur.user.workspace_group.newWorkspaceGroupDialog.render();			
			
	    // show and center the dialog box
	    YAHOO.ur.user.workspace_group.newWorkspaceGroupDialog.showDialog = function()
	    {
	        YAHOO.ur.user.workspace_group.newWorkspaceGroupDialog.center();
	        YAHOO.ur.user.workspace_group.newWorkspaceGroupDialog.show();
	    }
		    
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the groupspace form
	  */
	clearForm : function()
	{
	    
	    // clear out any errors
       var div = document.getElementById('workspaceGroupError');
       div.innerHTML = "";
	    
	    document.getElementById('groupName').value = "";
		document.getElementById('groupDescription').value = "";
		document.getElementById('workspace_group_id').value = "";
		
		document.getElementById('newWorkspaceGroup').value = "true";
		
	},
	
	/**
	 * Get all group workspaces for a user
	 */
    getGroups : function (groupWorkspaceId)
    {
	    // handle a successful return
        var handleSuccess = function(o) 
        {
    	    
		    // check for the timeout - forward user to login page if timeout
            // occurred
            if( !urUtil.checkTimeOut(o.responseText) )
            {       	    
                var response = o.responseText;
                document.getElementById('workspace_groups_table').innerHTML = response;
            }
        };

        // handle form submission failure
        var handleFailure = function(o) 
        {
            alert('get workspace groups failure '  + o.status + ' status text ' + o.statusText);
        };
    
        YAHOO.util.Connect.asyncRequest('GET', 
        		getWorkspaceGroupsAction + '?groupWorkspaceId=' + groupWorkspaceId +'&bustcache=' + new Date().getTime(),
        {success: handleSuccess, failure: handleFailure});       
    },
    
    
    
    
    
    
	/**
	 * Creates a YUI new modal dialog for when a user wants to delete 
	 * a content type
	 *
	 */
	createDeleteWorkspaceGroupDialog :function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
		    YAHOO.util.Connect.setForm('deleteWorkspaceGroup');
		    
		    //delete the content type
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        		deleteGroupAction, callback);
		};
		
			
		// handle a cancel of deleting content type dialog
		var handleCancel = function() 
		{
			YAHOO.ur.user.workspace_group.deleteWorkspaceGroupDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        var div = document.getElementById('workspace_groups_table');
		        div.innerHTML = o.responseText;
		        YAHOO.ur.user.workspace_group.deleteWorkspaceGroupDialog.hide();
	        }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
			YAHOO.ur.user.workspace_group.deleteWorkspaceGroupDialog.hide();
		    alert('delete group workspace failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new content type button is clicked.
		YAHOO.ur.user.workspace_group.deleteWorkspaceGroupDialog = new YAHOO.widget.Dialog('deleteWorkspaceGroupDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
			
	    // show and center the delete dialog
		YAHOO.ur.user.workspace_group.deleteWorkspaceGroupDialog.showDialog = function()
	    {
			YAHOO.ur.user.workspace_group.deleteWorkspaceGroupDialog.center();
			YAHOO.ur.user.workspace_group.deleteWorkspaceGroupDialog.show();
	    }
	    
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
		// Render the Dialog
		YAHOO.ur.user.workspace_group.deleteWorkspaceGroupDialog.render();

	},
	
	// delete the group space with the given id
	deleteWorkspaceGroup : function(id)
	{
		document.getElementById('deleteId').value=id;
		YAHOO.ur.user.workspace_group.deleteWorkspaceGroupDialog.showDialog();
	},
    
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	    YAHOO.ur.user.workspace_group.createCreateWorkspaceGroupDialog();
	    YAHOO.ur.user.workspace_group.createDeleteWorkspaceGroupDialog();
  	}
	
}	

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.user.workspace_group.init);