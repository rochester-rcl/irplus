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
 * This code is for dealing with adding files to the item
 */
YAHOO.namespace("ur.group_workspace_project_page.files");

// action to perform when submitting the personal folder form.
var myGroupWorkspaceFolderAction =  basePath + 'user/getGroupWorkspaceProjectPageFilesFolders.action';

// Action to perform when submitting selected files form
var myProjectPageFoldersAction = basePath + 'user/getGroupWorkspaceProjectPageFileSystem.action';

// Action to add, remove files from the selected list
//var removeFileAction = basePath + 'user/removeResearcherFile.action';
var addFileAction = basePath + 'user/addGroupWorkspaceProjectPageFile.action';

// Action to add files to item
var projectPageFolderAction = basePath + 'user/editGroupWorkspaceProjectPageFileSystem.action';
var changeFileVersionAction = basePath + 'user/changeGroupWorkspaceProjectPageFileVersion.action';

/**
 * researcher files namespace
 */
YAHOO.ur.group_workspace_project_page.files = {
	
	/**
	*  Function that change file version
	*
	*/
	changeFileVersion : function(object, groupWorkspaceProjectPageFileId) {

	    document.getElementById('updateProjectPageFileVersionFormProjectPageFileId').value = groupWorkspaceProjectPageFileId;
        document.getElementById('updateProjectPageFileVersionFormFileVersionId').value = object.value;
	    YAHOO.util.Connect.setForm('updateProjectPageFileVersionForm');
		var cObj = YAHOO.util.Connect.asyncRequest('post',
			           changeFileVersionAction, null);
	},

	/*
	 * Action to go back to researcher folders
	 */
	viewProjectPageFolders : function()
	{
        document.groupWorkspaceFolders.action = projectPageFolderAction;
		document.groupWorkspaceFolders.submit();
    },
    
	/**
	 *  Function that retrieves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getWorkspaceFolder : function() 
	{
		var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timeout
	            // occurred
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {               			    
		            var divToUpdate = document.getElementById('myGroupWorkspaceFolders');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get group workspace folder Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	   
	    YAHOO.util.Connect.setForm('groupWorkspaceFolders');
	       
	    var cObj = YAHOO.util.Connect.asyncRequest('POST',
	    		myGroupWorkspaceFolderAction, callback);
	},
	
	/**
	 *  Function that retrieves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getWorkspaceFolderById : function(folderId)
	{
	    document.getElementById('myFoldersParentFolderId').value = folderId;
	    YAHOO.ur.group_workspace_project_page.files.getWorkspaceFolder();
	},	
	
	/**
	 * To retrieve the selected files and display in the table
	 *
	 */
	 getProjectPageFolders : function()
	 {
		var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timeout
	            // occurred
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {               			    
		            var divToUpdate = document.getElementById('myProjectPageFolders');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    //alert('Get selected files Failure ' + o.status + ' status text ' + o.statusText );
			}
		}

		YAHOO.util.Connect.setForm('projectPageFoldersForm');
	    var cObj = YAHOO.util.Connect.asyncRequest('POST',
	           myProjectPageFoldersAction , callback);
	},
	
	/**
	 *  Function that retrieves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getProjectPageFolderById : function(folderId)
	{
	    document.getElementById('myGroupWorkspaceProjectPageFoldersParentFolderId').value = folderId;
	    YAHOO.ur.group_workspace_project_page.files.getProjectPageFolders();
	},	
    
	/**
	 * Add file to researcher
	 */	
	addFile : function(fileId) 
	{
		 var callback =
		 {
		 	success : function(o) {
			 YAHOO.ur.group_workspace_project_page.files.getProjectPageFolders();
			},
			
			failure : function(o) {
			    alert('Get selected files Failure ' + o.status + ' status text ' + o.statusText );
			} 
		}	
		
		// file to add
		document.getElementById('projectPageFolderFormVersionedFileId').value = fileId
		YAHOO.util.Connect.setForm('projectPageFoldersForm');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	         addFileAction , callback, null);
	}
	
	

}
