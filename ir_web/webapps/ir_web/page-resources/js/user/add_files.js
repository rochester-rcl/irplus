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
YAHOO.namespace("ur.researcher.files");

// action to perform when submitting the personal folder form.
var myPersonalFolderAction =  basePath + 'user/getPersonalFilesFolders.action';

// Action to perform when submitting selected files form
var myResearcherFoldersAction = basePath + 'user/getResearcherFileSystem.action';

// Action to add, remove files from the selected list
var removeFileAction = basePath + 'user/removeResearcherFile.action';
var addFileAction = basePath + 'user/addResearcherFile.action';

// Action to add files to item
var researcherFolderAction = basePath + 'user/viewResearcher.action?showFoldersTab=true';
var changeFileVersionAction = basePath + 'user/changeResearcherFileVersion.action';

/**
 * researcher files namespace
 */
YAHOO.ur.researcher.files = {
	
	/**
	 *  Function that change file version
	 *
	 */
	changeFileVersion : function(object, researcherFileId) {

		YAHOO.util.Connect.setForm('myPersonalFolders');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           changeFileVersionAction + '?researcherFileId=' + researcherFileId + '&fileVersionId=' + object.value, null);

	           
	           
	},
		
	/*
	 * Action to go back to researcher folders
	 */
	viewResearcherFolders : function()
	{
	
	    document.myResearcherFolders.action = researcherFolderAction;
	    
	    document.myResearcherFolders.submit();
	},
	 
	/**
	 *  Function that retireves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getPersonalFolder : function() 
	{
		var callback =
		{
		    success: function(o) 
		    {
		        var divToUpdate = document.getElementById('newPersonalFolders');
		        divToUpdate.innerHTML = o.responseText; 
		    },
			
			failure: function(o) 
			{
			    alert('Get personal folder Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	   
	    YAHOO.util.Connect.setForm('myPersonalFolders');
	       
	    var cObj = YAHOO.util.Connect.asyncRequest('POST',
	           myPersonalFolderAction , callback);
	},

	/**
	 *  Function that retrieves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getPersonalFolderById : function(folderId)
	{
	    document.getElementById('myFolders_parentPersonalFolderId').value = folderId;
	    YAHOO.ur.researcher.files.getPersonalFolder();
	},	
	
	/**
	 * To retrieve the selected files and display in the table
	 *
	 */
	 getResearcherFolders : function()
	 {
		var callback =
		{
		    success: function(o) 
		    {
		        var divToUpdate = document.getElementById('newResearcherFolders');
		        divToUpdate.innerHTML = o.responseText; 
		    },
			
			failure: function(o) 
			{
			    //alert('Get selected files Failure ' + o.status + ' status text ' + o.statusText );
			}
		}

		YAHOO.util.Connect.setForm('myResearcherFolders');
	   
	    var cObj = YAHOO.util.Connect.asyncRequest('POST',
	           myResearcherFoldersAction , callback);
	},
	
	/**
	 *  Function that retireves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getResearcherFolderById : function(folderId)
	{
	    document.getElementById('myResearcherFolders_parentFolderId').value = folderId;
	    document.getElementById('myFolders_parentFolderId').value = folderId;
	    YAHOO.ur.researcher.files.getResearcherFolders();
	},	
	
	/**
	 * Add file to researcher
	 */	
	addFile : function(fileId) 
	{
		 var callback =
		 {
		 	success : function(o) {
		    	YAHOO.ur.researcher.files.getResearcherFolders();
			},
			
			failure : function(o) {
			    alert('Get selected files Failure ' + o.status + ' status text ' + o.statusText );
			} 
		}	
		
		YAHOO.util.Connect.setForm('myPersonalFolders');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	         addFileAction + '?versionedFileId=' + fileId, callback, null);
	},
	
	
	/* initialize the page this is called once the dom has been created */
	init : function() {
	
	    YAHOO.ur.researcher.files.getPersonalFolder();
	    YAHOO.ur.researcher.files.getResearcherFolders();
	    YAHOO.ur.researcher.files.createFileErrorDialog();
	    
	}
}
// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.researcher.files.init);