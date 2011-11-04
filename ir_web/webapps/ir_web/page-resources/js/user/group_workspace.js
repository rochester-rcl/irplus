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
var viewGroupWorkspaceFolderAction =  basePath + 'user/viewGroupWorkspaceFolders.action';
var viewGroupWorkspacesAction = basePath + '/user/viewGroupWorkspaces.action';

//If there is no bookmarked state, assign the default state:
var groupWorkspaceState = "0";
var groupWorkspaceFolderState = "0";


//actions for adding and removing folders - group workspace
var updateGroupWorkspaceFolderAction = basePath + 'user/updateGroupWorkspaceFolder.action';
var newGroupWorkspaceFolderAction = basePath + 'user/addGroupWorkspaceFolder.action';
var deleteGroupWorkspaceFolderAction = basePath + 'user/deleteGroupWorkspaceFileSystemObjects.action';
var getGroupWorkspaceFolderAction = basePath + 'user/getGroupWorkspaceFolder.action';


/**
 * content type namespace
 */
YAHOO.ur.user.group_workspace = {
	
		
	/**
	  * function to handle state changes
	  */
    groupWorkspaceStateChangeHandler : function(workspaceId)
	{
	    var currentState = YAHOO.util.History.getCurrentState("groupWorkspaceModule"); 
	    YAHOO.ur.user.workspace.setActiveIndex("GROUP_WORKSPACE");
	    
	    var currentWorkspaceId = 0;
	    if( document.getElementById('groupFolderForm_workspaceId') != null )
	    {
	    	currentWorkspaceId = document.getElementById('groupFolderForm_workspaceId').value;
	    }
	    
	    if( workspaceId == 0 )
	    {
	    	YAHOO.ur.user.group_workspace.getGroupWorkspaces();
	    }
	    else
	    {
	    	if( currentWorkspaceId != workspaceId)
	    	{
	    	    YAHOO.ur.user.group_workspace.getGroupWorkspaceById(workspaceId);
	    	}
	    }
	    
	},	
	
	manageUsers : function(groupWorkspaceId)
	{
		window.location = getGroupWorkspaceAction + "?groupWorkspaceId=" + groupWorkspaceId + "&tabName=USERS";
	},
	
	/**
     *  Check all check boxes for files and folders
     */
    setCheckboxes : function()
    {
         checked = document.groupFolders.checkAllSetting.checked;
     
         // since the number of folders can be 0 or more
         // first make sure there are folder id's
         var folderIds = document.getElementsByName('groupFolderIds');
         urUtil.setCheckboxes(folderIds, checked);
     
         var fileIds = document.getElementsByName('groupFileIds');
         urUtil.setCheckboxes(fileIds, checked);
    },
	
	/**
	  * function to handle state changes for folders
	  */
    groupWorkspaceFolderStateChangeHandler : function(folderId)
	{
	    var currentState = YAHOO.util.History.getCurrentState("groupWorkspaceFolderModule"); 
	    YAHOO.ur.user.workspace.setActiveIndex("GROUP_WORKSPACE");
	   
	    // get the current folder id
	    var currentFolderId = 0;
	    if( document.getElementById('groupFolderForm_parentFolderId') != null )
	    {
	    	currentFolderId = document.getElementById('groupFolderForm_parentFolderId').value;
	    }
	   
	    if( folderId == 0 )
	    {
	    	var workspaceId = 0;
	    	if( document.getElementById('groupFolderForm_workspaceId') != null )
	    	{
	    		workspaceId = document.getElementById('groupFolderForm_workspaceId').value;
	    		YAHOO.ur.user.group_workspace.getGroupWorkspaceById(workspaceId);
	    	}
	    	
	    }
	    else
	    {
	    	if( currentFolderId != folderId )
	    	{
	    		workspaceId = document.getElementById('groupFolderForm_workspaceId').value;
	    	    YAHOO.ur.user.group_workspace.getFolderById(folderId, workspaceId, -1);
	    	}
	    }
	    
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
                    YAHOO.util.History.navigate( "groupWorkspaceModule", "0" );
                } 
                catch ( e ) 
                {
                	alert('fail');
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
		        	var id = document.newGroupWorkspaceForm.id.value;
		        	YAHOO.ur.user.group_workspace.newGroupWorkspaceDialog.hide();
		        	YAHOO.ur.user.group_workspace.clearGroupWorkspaceForm();
		        	YAHOO.ur.user.group_workspace.getGroupWorkspaceById(id);
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
		document.getElementById('groupWorkspaceDeleteId').value=id;
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
			// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	    
	            var response = o.responseText;
	            document.getElementById('group_workspaces').innerHTML = response;	 
	            YAHOO.ur.user.group_workspace.insertHiddenGroupWorkspaceFolderInfo(); 
	            
	            // this is for capturing history
                // it may fail if this is not an A grade browser so we need to
                // catch the error.
                // this will store the folder Id in the URL
                try 
                {
            	    // do not remove the string conversion on folder id otherwise an error occurs
                    YAHOO.util.History.navigate( "groupWorkspaceModule", workspaceId +"" );
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
	        alert('get group workspace by id failure '  + o.status + ' status text ' + o.statusText);
	    };

    
        YAHOO.util.Connect.asyncRequest('GET', viewGroupWorkspaceFolderAction + 
        		'?groupWorkspaceId=' + workspaceId +  '&bustcache=' + new Date().getTime(),
          {success: handleSuccess, failure: handleFailure});
    },
    
    
	/**
     * This will clear the hidden workspace id if it is set in the folder form
     * or file upload form it will indicate uploading a file to the group workspace
     * rather than the personal file workspace
     */
    clearHiddenWorkspaceInfo : function()
    {
        document.getElementById('groupFolderForm_workspaceId').value = '';
        document.getElementById('groupFolderForm_parentFolderId').value = '';
    },
    
    /**
     * This creates a hidden field appends it to the folder form for
     * adding new sub folders for a given parent folder id for a workspace.
     */ 
    insertHiddenGroupWorkspaceFolderInfo : function()
    {
        var parentFolderId = document.getElementById('groupFoldersParentFolderId').value;
        var groupWorkspaceId = document.getElementById('groupFoldersGroupWorkspaceId').value;
        
        // update the values
        document.getElementById('groupFolderForm_parentFolderId').value = parentFolderId;
        document.getElementById('groupFolderForm_workspaceId').value = groupWorkspaceId;
        document.getElementById('file_upload_group_workspace_parent_folder_id').value = parentFolderId;
        document.getElementById('file_upload_group_workspace_group_id').value = groupWorkspaceId;
    },
	
    /**
     * Clear the folder form
     */
    clearFolderForm : function()
    {
        // clear out the error message
        var folderError = document.getElementById('folder_error_div');
        folderError.innerHTML = "";    
	
	    document.groupFolderForm.folderName.value = "";
	    document.groupFolderForm.folderDescription.value = "";
	    document.groupFolderForm.newGroupFolder.value = "true";
	    document.groupFolderForm.updateFolderId.value = "";
	    document.groupFolderForm.groupWorkspaceId.value = "";
    },
    
    /**
     * Dialog to create new folders for group workspace
     */
    createFolderDialog : function()
    {
      
	    // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	    	this.submit();
	    };
		
	    // handle a cancel of the adding folder dialog
	    var handleCancel = function() 
	    {
	    	YAHOO.ur.user.group_workspace.clearFolderForm();
	    	YAHOO.ur.user.group_workspace.groupFolderDialog.hide();
	    };
	
	   // handle a successful return
	   var handleSuccess = function(o) 
	   {
		    // check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {  
	        	 YAHOO.ur.util.wait.waitDialog.hide();
	            //get the response from adding a folder
	            var response = o.responseText;
	            var folderForm = document.getElementById('groupFolderDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            folderForm.innerHTML = response;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("groupFolderForm_success").value;
	    
	            //if the folder was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
	            	YAHOO.ur.user.group_workspace.groupFolderDialog.showFolder();
	            }
	            else
	            {
	            	workspaceId = document.groupFolderForm.groupWorkspaceId.value;
	            	parentFolderId = document.groupFolderForm.parentFolderId.value;
	            	
	            	YAHOO.ur.user.group_workspace.groupFolderDialog.hide();
	            	YAHOO.ur.user.group_workspace.clearFolderForm();
	            	if( parentFolderId == null || parentFolderId < 0 )
	            	{
		                YAHOO.ur.user.group_workspace.getGroupWorkspaceById(workspaceId);
	            	}
	            	else
	            	{
	            		YAHOO.ur.user.group_workspace.getFolderById(parentFolderId, workspaceId, -1);
	            	}
	            }
	           
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert("Create group workspace folder failed due an error: " + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new folder button is clicked.
	    YAHOO.ur.user.group_workspace.groupFolderDialog = new YAHOO.widget.Dialog('groupFolderDialog', 
        { 
	    	width : "600px",
		    visible : false, 
		    modal : true,
		    buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
					  { text:"Cancel", handler:handleCancel } ]
		} );
	
	    // override the submit
	    YAHOO.ur.user.group_workspace.groupFolderDialog.submit = function()
	    {
	    	YAHOO.ur.user.group_workspace.groupFolderDialog.hide();
	    	YAHOO.ur.util.wait.waitDialog.showDialog();
	    	//THIS WILL NEED TO BE UPDATED Once folder menus are created
	    	//YAHOO.ur.user.group_workspace.destroyFolderMenus();
	        YAHOO.util.Connect.setForm('groupFolderForm');
	        if( YAHOO.ur.user.group_workspace.groupFolderDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new folder) based on the action.  
                var action = newGroupWorkspaceFolderAction;
                
	            if( document.groupFolderForm.updateFolderId.value != '' )
	            {
	               // update folder personal workspace
	               action = updateGroupWorkspaceFolderAction;
	            }
                var cObj = YAHOO.util.Connect.asyncRequest('POST',
                action, callback);
            }
	    };
	    
	    YAHOO.ur.user.group_workspace.groupFolderDialog.showFolder = function()
	    {
	        YAHOO.ur.user.group_workspace.groupFolderDialog.center();
	        YAHOO.ur.user.group_workspace.groupFolderDialog.show();
	        YAHOO.ur.shared.file.inbox.getSharedFilesCount();
	    };
   
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.user.group_workspace.groupFolderDialog.validate = function() {
	        var data = this.getData();
		    if (data.folderName == "" ) 
		    {
		        alert("A folder name must be entered");
			    return false;
		    } 
		    else 
		    {
			    return true;
		    }
	    };

	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.user.group_workspace.groupFolderDialog.render();
    },
    
	/**
	 * Dialog to confirm delete of folders and files
	 */
	createFolderDeleteConfirmDialog : function() 
	{
        // Define various event handlers for Dialog
	    var handleYes = function() 
	    {
	    	YAHOO.ur.util.wait.waitDialog.showDialog();
	    	//ADD THIS FUNCTION
        	//YAHOO.ur.user.group_workspace.destroyFolderMenus();
		    this.hide();
		    YAHOO.ur.user.group_workspace.deleteFilesFolders();
	    };
	    
	    var handleNo = function() 
	    {
	        //uncheck all the ones that have been checked
	        checked = document.groupFolders.checkAllSetting.checked = false;
	        
	        //ADD THIS FUNCTION
	        //YAHOO.ur.user.group_workspace.setCheckboxes();
		    this.hide();
	    };

	    // Instantiate the Dialog
	    YAHOO.ur.user.group_workspace.deleteFolder = 
	        new YAHOO.widget.Dialog("deleteGroupWorkspaceFileFolderConfirmDialog", 
									     { width: "400px",
										   visible: false,
										   modal: true,
										   buttons: [ { text:"Yes", handler:handleYes, isDefault:true },
													  { text:"No",  handler:handleNo } ]
										} );
	
	    YAHOO.ur.user.group_workspace.deleteFolder.setHeader("Delete?");
	
	    //show the dialog and center
	    YAHOO.ur.user.group_workspace.deleteFolder.showDialog = function()
	    {
	    	
	        if (!urUtil.checkForNoSelections(document.groupFolders.groupFolderIds) &&
	            !urUtil.checkForNoSelections(document.groupFolders.groupFileIds))
		    {
			     alert('Please select at least one checkbox next to the files or folders you wish to delete.');
	        } 
	        else
	        {
	            YAHOO.ur.user.group_workspace.deleteFolder.center();
	            YAHOO.ur.user.group_workspace.deleteFolder.show();
	        }
	    };
	
	    // Render the Dialog
	    YAHOO.ur.user.group_workspace.deleteFolder.render();

    },
    
	/**
	 * Delete the files and folders by submitting the form.
	 */
	deleteFilesFolders : function()
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
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('delete files folders failure '  + o.status + ' status text ' + o.statusText);
	    };

	    YAHOO.util.Connect.setForm('groupFolders');
    
        YAHOO.util.Connect.asyncRequest('POST', deleteGroupWorkspaceFolderAction,
          {success: handleSuccess, failure: handleFailure});
	},
	
    /**
     *  Function that retireves folder information
     *  based on the given folder id.  If a file id is passed in
     *  then the brower location will be pointed to for the file download.  
     *  This is required to work for all browsers Safari/IE/Chrome/Fire Fox.
     *
     *  folderId - The folder id used to get the folder.
     *  fileId - id of the file a -1 indicates no file id 
     */
    getFolderById : function(folderId, workspaceId,  fileId)
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
	            //alert(o.responseText);
	            document.getElementById('group_workspaces').innerHTML = response;
	            
	            YAHOO.ur.user.group_workspace.clearHiddenWorkspaceInfo();
	            YAHOO.ur.user.group_workspace.insertHiddenGroupWorkspaceFolderInfo();
	            
	            // this is for capturing history
	            // it may fail if this is not an A grade browser so we need to
	            // catch the error.
	            // this will store the folder Id in the URL
	            try 
	            {
	            	// do not remove the string conversion on folder id otherwise an error occurs
	                YAHOO.util.History.navigate( "groupWorkspaceFolderModule", folderId + "" );
	            } 
	            catch ( e ) 
	            {
	                // history failed
	            }
	            
	            // only call this if we do not need to download a file
	            // otherwise a javascript error will occur when chaning the window location
	            if( fileId == -1 )
	            {
	                YAHOO.ur.shared.file.inbox.getSharedFilesCount();
	            }
	            else
	            {
	            	alert('download file');
	            	//window.location.href= basePath + 'user/personalFileDownload.action' + '?personalFileId=' +fileId;
	            }
	            YAHOO.ur.util.wait.waitDialog.hide();
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('get group workspace folder by id failure '  + o.status + ' status text ' + o.statusText);
	    };

	    //destroy the folder menus
        //YAHOO.ur.folder.destroyFolderMenus();
    
      
        //set the folder id
        //document.getElementById('groupFoldersParentFolderId').value = folderId;
	    
	   // YAHOO.util.Connect.setForm('groupFolders');
	   
	    // execute the transaction
        //var transaction = YAHOO.util.Connect.asyncRequest('GET', 
        //        viewSharedInboxFiles +'?bustcache='+new Date().getTime(), 
        //        callback, null);
	    
       var getFoldersAction = viewGroupWorkspaceFolderAction + 
           '?parentFolderId=' + folderId + "&groupWorkspaceId=" + workspaceId + 
           '&bustcache='+new Date().getTime();
        YAHOO.util.Connect.asyncRequest('GET', getFoldersAction, 
          {success: handleSuccess, failure: handleFailure}, null);
    },
    
    /**
     * Function to upload a single file
     */
    singleFileUpload : function()
    {
	    // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        // action to perform when submitting the news items.
            var groupFileUploadAction = basePath + 'user/groupWorkspaceSingleFileUpload.action';
	        YAHOO.util.Connect.setForm('groupWorkspaceSingleFileUploadForm', true, true);
	    	
	        if( YAHOO.ur.user.group_workspace.singleFileUploadDialog.validate() )
	        {
	        	YAHOO.ur.user.group_workspace.singleFileUploadDialog.hide();
		    	YAHOO.ur.util.wait.waitDialog.showDialog();
	            
	            //based on what we need to do (update or create a 
	            // new news item) based on the action.
                var cObj = YAHOO.util.Connect.asyncRequest('post',
                      groupFileUploadAction, callback);
                // clear the upload form of the file name
                
                // FIX THIS
                //YAHOO.ur.user.group_workspace.clearSingleFileUploadForm();
            }
	    };
	
	    // handle a cancel of the adding news item dialog
	    var handleCancel = function() 
	    {
	    	YAHOO.ur.user.group_workspace.singleFileUploadDialog.hide();
	    	//YAHOO.ur.user.group_workspace.clearSingleFileUploadForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
	    	//YAHOO.ur.user.group_workspace.destroyFolderMenus();
	        var response = o.responseText;
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var uploadForm = document.getElementById('group_workspace_upload_form_fields');
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            uploadForm.innerHTML = o.responseText;
	            try{
	                // determine if the add/edit was a success
	                var success = document.getElementById("group_workspace_file_added").value;
	                //if the content type was not added then show the user the error message.
	                // received from the server
	                if( success == "false" )
	                {
	        	    	YAHOO.ur.util.wait.waitDialog.hide();
	        	    	YAHOO.ur.user.group_workspace.singleFileUploadDialog.showDialog();
	                }
	                else
	                {
	                    // we can clear the upload form and get the pictures
	                	
	                	//FIX THIS
	                	//YAHOO.ur.user.group_workspace.clearSingleFileUploadForm();
	                    var folderId = document.getElementById("groupFolderForm_parentFolderId").value;
	                    var workspaceId = document.getElementById("groupFolderForm_workspaceId").value;
	                    YAHOO.ur.user.group_workspace.getFolderById(folderId,workspaceId, -1); 
	        	    	YAHOO.ur.util.wait.waitDialog.hide();
	                }
	            }
	            catch(err)
	            {
	    	    	  YAHOO.ur.util.wait.waitDialog.hide();
	            	  txt="There was an error on this page.\n\n";
	            	  txt+="Error description: " + err + "\n\n";
	            	  txt+="Click OK to continue.\n\n";
	            	  alert(txt);
	            }
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('group workspace single file upload submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new news item button is clicked.
	
	    YAHOO.ur.user.group_workspace.singleFileUploadDialog = new YAHOO.widget.Dialog('groupWorkspaceSingleFileUploadDialog', 
        { width : "850px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					  { text:'Cancel', handler:handleCancel } ]
        } );
		
        //shows
        //and centers the dialog box
	    YAHOO.ur.user.group_workspace.singleFileUploadDialog.showDialog = function()
        {
	    	YAHOO.ur.user.group_workspace.singleFileUploadDialog.center();
	    	YAHOO.ur.user.group_workspace.singleFileUploadDialog.show();
            YAHOO.ur.shared.file.inbox.getSharedFilesCount();
        }
    
 	    // Validate the entries in the form to require that a name is entered
	    YAHOO.ur.user.group_workspace.singleFileUploadDialog.validate = function() 
	    {
	        var fileName = document.getElementById('group_workspace_single_file_upload').value;
		    if (fileName == "" || fileName == null) 
		    {
		        alert('A File name must be entered');
			    return false;
		    } 
		    else 
		    {
			    return true;
		    }
	    };

	    // Wire up the success and failure handlers
	    var callback = {  upload: handleSuccess, failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.user.group_workspace.singleFileUploadDialog.render();

    },
    
  	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	    YAHOO.ur.user.group_workspace.createCreateGroupWorkspaceDialog();
	    YAHOO.ur.user.group_workspace.createDeleteGroupWorkspaceDialog();
	    YAHOO.ur.user.group_workspace.createFolderDialog();	
	    YAHOO.ur.user.group_workspace.createFolderDeleteConfirmDialog();
	    YAHOO.ur.user.group_workspace.singleFileUpload();
	    
	    groupWorkspaceId = document.getElementById("groupWorkspaceFormGroupWorkspaceId").value;
	    groupWorkspaceFolderId = document.getElementById("groupWorkspaceFormGroupWorkspaceFolderId").value;
	    
	    // register the history system
        YAHOO.util.History.register("groupWorkspaceModule", 
        		groupWorkspaceState, 
        		YAHOO.ur.user.group_workspace.groupWorkspaceStateChangeHandler);
        
        // register the history system
        YAHOO.util.History.register("groupWorkspaceFolderModule", 
        		groupWorkspaceFolderState, 
        		YAHOO.ur.user.group_workspace.groupWorkspaceFolderStateChangeHandler);
        
     
	    if( groupWorkspaceId != null && groupWorkspaceId > 0 )
	    {
	    	 if( groupWorkspaceFolderId == null && groupWorkspaceFolderId < 0 )
	    	 { 
	    	     YAHOO.ur.user.group_workspace.getGroupWorkspaceById(groupWorkspaceId);
	    	 }
	    	 else
	    	 {
	    		 YAHOO.ur.user.group_workspace.getFolderById(groupWorkspaceFolderId, groupWorkspaceId, -1);
	    	 }
	    }
        
  	}
	
}	

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.user.group_workspace.init);