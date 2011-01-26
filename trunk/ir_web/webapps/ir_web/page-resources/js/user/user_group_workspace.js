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
YAHOO.namespace("ur.user.group_workspace");


//actions for adding,updating and removing content types
var updateGroupWorkspaceAction = basePath + 'user/updateGroupWorkspace.action';
var newGroupWorkspaceAction = basePath + 'user/createGroupWorkspace.action';
var deleteGroupWorkspaceAction = basePath + 'user/deleteGroupWorkspace.action';
var getGroupWorkspaceAction = basePath + 'user/getGroupWorkspace.action';
var viewGroupWorkspaceAction =  basePath + 'user/viewGroupWorkspaceFolders.action';
var viewGroupWorkspacesAction = basePath + '/user/viewGroupWorkspaces.action';

//If there is no bookmarked state, assign the default state:
var groupWorkspaceFolderState = "0";

/**
 * content type namespace
 */
YAHOO.ur.user.group_workspace = {
	
		
	/**
	  * function to handle state changes
	  */
    groupWorkspaceStateChangeHandler : function(workspaceId)
	{
	    var currentState = YAHOO.util.History.getCurrentState("groupWorkspaceFolderModule"); 
	    YAHOO.ur.user.workspace.setActiveIndex("GROUP_WORKSPACE");
	    
	    if( workspaceId == 0 )
	    {
	    	YAHOO.ur.user.group_workspace.getGroupWorkspaces();
	    }
	    else
	    {
	    	YAHOO.ur.user.group_workspace.getGroupWorkspaceById(workspaceId);
	    }
	    
	    //var currentFolder = document.getElementById('myFolders_parentFolderId').value;
	    
	    // do not change state if we are on the current file / folder
	    /*if( currentState != currentFolder )
	    {
	        document.getElementById('myFolders_parentFolderId').value = folderId;
	        var folderId = document.getElementById("myFolders_parentFolderId").value;
	        YAHOO.ur.folder.getFolderById(folderId, -1); 
	        YAHOO.ur.folder.insertHiddenParentFolderId();
	    }*/
	},	
		
	/**
	 * Get all group workspaces for a user
	 */
    getGroupWorkspaces : function ()
    {
	    // handle a successful return
        var handleSuccess = function(o) 
        {
    	    YAHOO.ur.util.wait.waitDialog.hide();
		    // check for the timeout - forward user to login page if timeout
            // occurred
            if( !urUtil.checkTimeOut(o.responseText) )
            {       	    
                var response = o.responseText;
                document.getElementById('group_workspaces').innerHTML = response;
            
                // this is for capturing history
                // it may fail if this is not an A grade browser so we need to
                // catch the error.
                // this will store the folder Id in the URL
                try 
                {
            	    // do not remove the string conversion on folder id otherwise an error occurs
                    YAHOO.util.History.navigate( "groupWorkspaceFolderModule", "0" );
                } 
                catch ( e ) 
                {
                    // history failed
                }
            
                // update shared inbox count
                YAHOO.ur.shared.file.inbox.getSharedFilesCount();
                
                // hide the wait dialog
                YAHOO.ur.util.wait.waitDialog.hide();
            }
        };

        // handle form submission failure
        var handleFailure = function(o) 
        {
    	    YAHOO.ur.util.wait.waitDialog.hide();
            alert('get workspaces by user id failure '  + o.status + ' status text ' + o.statusText);
        };

        //destroy the folder menus
        //YAHOO.ur.folder.destroyFolderMenus();

        // set the state for the folder id
        //personalFolderState = folderId;

    
        YAHOO.util.Connect.asyncRequest('GET', 
        		viewGroupWorkspacesAction + '?bustcache=' + new Date().getTime(),
        {success: handleSuccess, failure: handleFailure});       
    },
		
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
		    YAHOO.ur.user.group_workspace.newGroupWorkspaceDialog.hide();
		    YAHOO.ur.user.group_workspace.clearGroupWorkspaceForm();
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
		        var success = document.getElementById("success").value;
		  
		        //if the content type was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.user.group_workspace.newGroupWorkspaceDialog.showDialog();
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
		YAHOO.ur.user.group_workspace.newGroupWorkspaceDialog = new YAHOO.widget.Dialog('newGroupWorkspaceDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
		//submit form
		YAHOO.ur.user.group_workspace.newGroupWorkspaceDialog.submit = function() 
		{
		    YAHOO.util.Connect.setForm('addGroupWorkspace');
		    if( YAHOO.ur.user.group_workspace.newGroupWorkspaceDialog.validate() )
		    {
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            		newGroupWorkspaceAction, callback);
	        }
	     }		

	    
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.user.group_workspace.newGroupWorkspaceDialog.validate = function() 
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
		YAHOO.ur.user.group_workspace.newGroupWorkspaceDialog.render();			
			
	    // show and center the dialog box
	    YAHOO.ur.user.group_workspace.newGroupWorkspaceDialog.showDialog = function()
	    {
	        YAHOO.ur.user.group_workspace.newGroupWorkspaceDialog.center();
	        YAHOO.ur.user.group_workspace.newGroupWorkspaceDialog.show();
	    }
		    
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
			YAHOO.ur.user.group_workspace.deleteGroupWorkspaceDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        var div = document.getElementById('group_workspaces');
		        div.innerHTML = o.responseText;
		        YAHOO.ur.user.group_workspace.deleteGroupWorkspaceDialog.hide();
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
			YAHOO.ur.user.group_workspace.deleteGroupWorkspaceDialog.hide();
		    alert('delete group workspace failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new content type button is clicked.
		YAHOO.ur.user.group_workspace.deleteGroupWorkspaceDialog = new YAHOO.widget.Dialog('deleteGroupWorkspaceDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
			
	    // show and center the delete dialog
		YAHOO.ur.user.group_workspace.deleteGroupWorkspaceDialog.showDialog = function()
	    {
			YAHOO.ur.user.group_workspace.deleteGroupWorkspaceDialog.center();
			YAHOO.ur.user.group_workspace.deleteGroupWorkspaceDialog.show();
	    }
	    
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
		// Render the Dialog
		YAHOO.ur.user.group_workspace.deleteGroupWorkspaceDialog.render();

	},
	
	// delete the group space with the given id
	deleteGroupWorkspace : function(id)
	{
		document.getElementById('deleteId').value=id;
		YAHOO.ur.user.group_workspace.deleteGroupWorkspaceDialog.showDialog();
	},
	
    /**
     *  Function that retrieves folder information
     *  based on the given folder id.  If a file id is passed in
     *  then the browser location will be pointed to for the file download.  
     *  This is required to work for all browsers Safari/IE/Chrome/Fire Fox.
     *
     *  folderId - The folder id used to get the folder.
     *  fileId - id of the file a -1 indicates no file id 
     */
    getGroupWorkspaceById : function(workspaceId)
    {
    	
		// handle a successful return
	    var handleSuccess = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
			// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	    
	            var response = o.responseText;
	            document.getElementById('group_workspaces').innerHTML = response;	 
	            YAHOO.util.History.navigate( "groupWorkspaceFolderModule", workspaceId + "" );
	            YAHOO.ur.util.wait.waitDialog.hide();
	            
	            // this is for capturing history
                // it may fail if this is not an A grade browser so we need to
                // catch the error.
                // this will store the folder Id in the URL
                try 
                {
            	    // do not remove the string conversion on folder id otherwise an error occurs
                    YAHOO.util.History.navigate( "groupWorkspaceModule", workspaceId );
                } 
                catch ( e ) 
                {
                    // history failed
                }
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('get folder by id failure '  + o.status + ' status text ' + o.statusText);
	    };

    
        YAHOO.util.Connect.asyncRequest('GET', viewGroupWorkspaceAction + 
        		'?groupWorkspaceId=' + workspaceId +  '&bustcache=' + new Date().getTime(),
          {success: handleSuccess, failure: handleFailure});
    },
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	    YAHOO.ur.user.group_workspace.createCreateGroupWorkspaceDialog();
	    YAHOO.ur.user.group_workspace.createDeleteGroupWorkspaceDialog();
	    
        // register the history system
        YAHOO.util.History.register("groupWorkspaceFolderModule", 
        		groupWorkspaceFolderState, 
        		YAHOO.ur.user.group_workspace.groupWorkspaceStateChangeHandler);

  	}
	
}	

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.user.group_workspace.init);