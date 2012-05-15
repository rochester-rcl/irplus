/*
   Copyright 2008 - 2012 University of Rochester

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
YAHOO.namespace("ur.groupworkspace.file_system");

// action to perform when submitting the personal folder form.
var getProjectPageFileSystemAction = basePath + 'user/viewGroupWorkspaceProjectPageFolders.action';



// actions for adding and removing folders
var updateFolderAction = basePath + 'user/updateGroupWorkspaceProjectPageFolder.action';
var newFolderAction = basePath + 'user/addGroupWorkspaceProjectPageFolder.action';
var deleteFolderAction = basePath + 'user/deleteGroupWorkspaceProjectPageFileSystemObjects.action';

// actions for adding and removing links
var updateLinkAction = basePath + 'user/updateGroupWorkspaceProjectPageFileSystemLink.action';
var newLinkAction = basePath + 'user/addGroupWorkspaceProjectPageFileSystemLink.action';
var getLinkAction = basePath + 'user/getGroupWorkspaceProjectPageFileSystemLink.action';

// object to hold the specified folder data.
//var myResearcherFolderTable = new YAHOO.ur.table.Table('myFolders', 'newResearcherFolders');


/**
 * Folder namespace
 */
YAHOO.ur.groupworkspace.file_system = {
	
	/** 
	  * clear out any form data messages or input
	  * in the folder form
	  */
	clearFolderForm : function()
	{
	   // clear out any errors
        var div = document.getElementById('folderNameError');
        div.innerHTML = "";
        
		document.newFolderForm.folderName.value = "";
		document.newFolderForm.folderDescription.value = "";
		document.newFolderForm.newFolder.value = "true";
	},
	
	
	/**
	 * Creates a YUI new folder modal dialog for when a user wants to create 
	 * a new folder
	 *
	 */
	createNewFolderDialog : function()
	{
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
			
		// handle a cancel of the adding folder dialog
		var handleCancel = function() 
		{
			YAHOO.ur.groupworkspace.file_system.newFolderDialog.hide();
			YAHOO.ur.groupworkspace.file_system.clearFolderForm();
		};
		
		// handle a successful return
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		
		        //get the response from adding a folder
		        var response = eval("("+o.responseText+")");
		    
		        //if the folder was not added then show the user the error message.
		        // received from the server
		        if( response.added == "false" )
		        {
		            var folderNameError = document.getElementById('folderNameError');
	                folderNameError.innerHTML = '<p id="newFolderForm_nameError">' + response.message + '</p>';
	                YAHOO.ur.groupworkspace.file_system.newFolderDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the folder was added
		        	YAHOO.ur.groupworkspace.file_system.clearFolderForm();
		            YAHOO.ur.groupworkspace.file_system.newFolderDialog.hide();
		        }
		        var projectId = document.getElementById('myFolders_groupWorkspaceProjectPageId').value;
		        var folderId = document.getElementById('myFolders_parentFolderId').value;
		        alert("success folderId = " + folderId + " project Id = " + projectId)
		        YAHOO.ur.groupworkspace.file_system.getFolderById(projectId,folderId);
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) 
		{
		    alert("The sumbission failed due to a network issue " + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new folder button is clicked.
		YAHOO.ur.groupworkspace.file_system.newFolderDialog = new YAHOO.widget.Dialog('newFolderDialog', 
	        { width : "600px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
						  { text:"Cancel", handler:handleCancel } ]
			} );
		
		// Submit form
		YAHOO.ur.groupworkspace.file_system.newFolderDialog.submit = function() {
		
		    YAHOO.util.Connect.setForm('newFolderForm');
		    
		    if( YAHOO.ur.groupworkspace.file_system.newFolderDialog.validate() )
		    {
		        //based on what we need to do (update or create a 
		        // new folder) based on the action.
	            var action = newFolderAction;
		        if( document.newFolderForm.newFolder.value != 'true')
		        {
		           action = updateFolderAction;
		        }
	
	            var cObj = YAHOO.util.Connect.asyncRequest('POST',
	           		action, callback);
	        }
	    },
	        		
	    YAHOO.ur.groupworkspace.file_system.newFolderDialog.showDialog = function()
		{
	    	YAHOO.ur.groupworkspace.file_system.newFolderDialog.center();
	    	YAHOO.ur.groupworkspace.file_system.newFolderDialog.show();
		    
		}
	   
	 	// Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.groupworkspace.file_system.newFolderDialog.validate = function() {
		    var data = this.getData();
			if (data.folderName == "" ) {
			    alert("A folder name must be entered");
				return false;
			} else {
				return true;
			}
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
		// Render the Dialog
		YAHOO.ur.groupworkspace.file_system.newFolderDialog.render();
	},
	
    // function to edit folder information
    editFolder : function(folderId, folderName, folderDescription)
    {
    	document.newFolderForm.folderName.value = folderName;
	    document.newFolderForm.folderDescription.value = folderDescription;
	    document.newFolderForm.updateFolderId.value = folderId;
	    document.newFolderForm.newFolder.value = "false";
	    YAHOO.ur.groupworkspace.file_system.newFolderDialog.showDialog();
    },
    
    // function to edit link information
    editLink : function(linkId)
    {
	     /*  This call back updates the html when editing the link */
	      var callback =
	      {
	          success: function(o) 
	          {
	 		      // check for the timeout - forward user to login page if timeout
		          // occurred
		          if( !urUtil.checkTimeOut(o.responseText) )
		          {       		           
	                  var divToUpdate = document.getElementById('groupWorkspaceProjectPageLinkFields');
	                  divToUpdate.innerHTML = o.responseText;

	                  document.getElementById('newLinkForm_new').value="false";
	                  YAHOO.ur.groupworkspace.file_system.newLinkDialog.showLink();
	              }
	        },
		
		    failure: function(o) 
		    {
		        alert('Edit group workspace project page link Failure ' + o.status + ' status text ' + o.statusText );
		    }
	    };
	        
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	            getLinkAction + '?linkId=' + linkId +  '&bustcache='+new Date().getTime(), 
	            callback, null);
    },
	
	/**
	 *  Check all check boxes for files and folders
	 */
	setCheckboxes : function()
	{
	     checked = document.myFolders.checkAllSetting.checked;
	     
	     // since the number of folders can be 0 or more
	     // first make sure there are folder id's
	     var folderIds = document.getElementsByName('folderIds');
	     urUtil.setCheckboxes(folderIds, checked);
	     
	     var fileIds = document.getElementsByName('fileIds');
	     urUtil.setCheckboxes(fileIds, checked);
	     
	     var publicationIds = document.getElementsByName('publicationIds');
	     urUtil.setCheckboxes(publicationIds, checked);
	     
	     var linkIds = document.getElementsByName('linkIds');
	     urUtil.setCheckboxes(linkIds, checked);
	     
	     var linkIds = document.getElementsByName('itemIds');
	     urUtil.setCheckboxes(linkIds, checked);
	},
	
	// create a dialog to confirm the deletion of folders.
	createFolderDeleteConfirmDialog : function() 
	{
	    // Define various event handlers for Dialog
		var handleYes = function() 
		{
	 		
	 		YAHOO.util.Connect.setForm('myFolders');
		    
	            var cObj = YAHOO.util.Connect.asyncRequest('POST',
	            deleteFolderAction, callback);
	        
			this.hide();
		};
		
		var handleNo = function() 
		{
		    //uncheck all the ones that have been checked
		    checked = document.myFolders.checkAllSetting.checked = false;
		    YAHOO.ur.groupworkspace.file_system.setCheckboxes();
			this.hide();
		};
		
		    // success when getting the file properties
	    var handleSuccess = function(o) 
	    {
	    	 var divToUpdate = document.getElementById('fileSystemTable');
             divToUpdate.innerHTML = o.responseText;
	    };
	   
	    // success when getting the file properties
	    var handleFailure = function(o) {
		    alert('Get file properties failed ' + o.status);
	    };
	
		// Instantiate the Dialog
		YAHOO.ur.groupworkspace.file_system.deleteFolder = 
		    new YAHOO.widget.Dialog("deleteFileFolderConfirmDialog", 
										     { width: "400px",
											   visible: false,
											   modal: true,
											   close: true,										   
											   buttons: [ { text:"Yes", handler:handleYes, isDefault:true },
														  { text:"No",  handler:handleNo } ]
											} );
		
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };	
		
		//show the dialog and center
		YAHOO.ur.groupworkspace.file_system.deleteFolder.showDialog = function()
		{
		     YAHOO.ur.groupworkspace.file_system.deleteFolder.center();
		     YAHOO.ur.groupworkspace.file_system.deleteFolder.show();
		}
		
		// Render the Dialog
		YAHOO.ur.groupworkspace.file_system.deleteFolder.render();
	},
	
	/** 
	  * clear out any form data messages or input
	  * in the link form
	  */
	clearLinkForm : function()
	{
	     // clear out any errors
        var div = document.getElementById('linkNameError');
        div.innerHTML = "";
        
		document.newLinkForm.linkName.value = "";
		document.newLinkForm.linkDescription.value = "";
		document.newLinkForm.linkUrl.value = "http://";
		document.newLinkForm.newLink.value = "true";
	},
	
	/**
	 * Creates a YUI new link modal dialog for when a user wants to create 
	 * a new link
	 *
	 */
	createNewLinkDialog : function()
	{
	
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
			
		// handle a cancel of the adding folder dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.groupworkspace.file_system.newLinkDialog.hide();
		    YAHOO.ur.groupworkspace.file_system.clearLinkForm();
		};
		
		// handle a successful return
		var handleSuccess = function(o) 
		{
			
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		 	
	        	var response = eval("("+o.responseText+")");
		        //if the folder was not added then show the user the error message.
		        // received from the server
		        if( response.added == "false" )
		        {
		            var linkNameError = document.getElementById('linkNameError');
	                linkNameError.innerHTML = '<p id="newLinkForm_nameError">' + response.message + '</p>';
	                YAHOO.ur.groupworkspace.file_system.newLinkDialog.show();
		        }
		        else
		        {
		            // we can clear the form if the folder was added
		            YAHOO.ur.groupworkspace.file_system.newLinkDialog.hide();
		            YAHOO.ur.groupworkspace.file_system.clearLinkForm();
		        }
		        YAHOO.ur.groupworkspace.file_system.getFolderById(document.getElementById('myFolders_groupWorkspaceProjectPageId').value, 
		        		document.getElementById('myFolders_parentFolderId').value)
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) 
		{
		    alert("Create new link failed : " + o.status  + " status text " + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new folder button is clicked.
		YAHOO.ur.groupworkspace.file_system.newLinkDialog = new YAHOO.widget.Dialog('newLinkDialog', 
	        { width : "600px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
						  { text:"Cancel", handler:handleCancel } ]
			} );
		
		YAHOO.ur.groupworkspace.file_system.newLinkDialog.showLink = function()
		{
			document.getElementById('newLinkForm_parentFolderId').value =
	    	    document.getElementById('myFolders_parentFolderId').value;
			
		    YAHOO.ur.groupworkspace.file_system.newLinkDialog.center();
		    YAHOO.ur.groupworkspace.file_system.newLinkDialog.show();
		}
	   
	    // Submit form
		YAHOO.ur.groupworkspace.file_system.newLinkDialog.submit = function()
		{	   
		    YAHOO.util.Connect.setForm('newLinkForm');
		    
		    if( YAHOO.ur.groupworkspace.file_system.newLinkDialog.validate() )
		    {
		        //based on what we need to do (update or create a 
		        // new folder) based on the action.
	            var action = newLinkAction;
		        if( document.newLinkForm.newLink.value != 'true')
		        {
		           action = updateLinkAction;
		        }
		        
	            var cObj = YAHOO.util.Connect.asyncRequest('POST',
	            action, callback);
	        }
	    }
	        	   
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.groupworkspace.file_system.newLinkDialog.validate = function() {
		    var data = this.getData();
			if (urUtil.trim(data.linkName) == "" ) {
			    alert("A link name must be entered");
				return false;
			} 
			if (urUtil.trim(data.linkUrl) == "" || urUtil.trim(data.linkUrl) == "http://" ) {
			    alert("A url must be entered");
				return false;
			} 
			
			return true;
			
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
		// Render the Dialog
		YAHOO.ur.groupworkspace.file_system.newLinkDialog.render();
	 },
	   
 
	
	/**
	 * This creates a hidden field appends it to the form for
	 * adding new sub folders for a given parent folder id.
	 */ 
	insertHiddenParentFolderId : function()
	{
	    var value = document.getElementById('myFolders_parentFolderId').value
	    document.getElementById('newFolderForm_parentFolderId').value = value;
	    //document.getElementById('newLinkForm_parentFolderId').value = value;
	},
	
	/**
	 *  Function that retrieves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getFolderById : function(projectPageId, folderId)
	{
		var callback =
		{
		    success : function(o) {
			    // check for the timeout - forward user to login page if timeout
	            // occurred
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		 		    
			        var response = o.responseText;
			        var contentArea = document.getElementById('fileSystemTable');
			        contentArea.innerHTML = o.responseText; 
		            YAHOO.ur.groupworkspace.file_system.insertHiddenParentFolderId();
		        }
		    },
		   
		    failure : function(o) {
			    alert('Get folder by id failed ' + o.status);
		    }
		}	
	    
	    // execute the transaction
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	    		getProjectPageFileSystemAction +"?parentFolderId="+ 
	             folderId + '&groupWorkspaceProjectPageId=' + projectPageId +'&bustcache='+new Date().getTime(), 
	             callback, null);
	   
	},
	
	/**
	 * Set the sort type [ asc, desc, none ]
	 * Set the element to sort on
	 */
	updateSort : function(sortType, sortElement)
	{
	    document.getElementById('folder_sort_type').value = sortType;
	    document.getElementById('folder_sort_element').value = sortElement;
	    //myResearcherFolderTable.submitForm(myFolderAction);
	},
	
	/**
     * Allow researcher information to be moved
     */
    moveResearcherData : function()
    {
        if (!urUtil.checkForNoSelections(document.myFolders.folderIds) &&
	        !urUtil.checkForNoSelections(document.myFolders.fileIds) &&
	        !urUtil.checkForNoSelections(document.myFolders.publicationIds)&&
	        !urUtil.checkForNoSelections(document.myFolders.linkIds)&&
	        !urUtil.checkForNoSelections(document.myFolders.itemIds)
	        )
		{
			 alert('Please select at least one checkbox of item you wish to move.');
	    } 
	    else
	    {
            var viewMoveAction = basePath + 'user/viewMoveResearcherLocations.action';
            document.myFolders.action = viewMoveAction;
            document.myFolders.submit();
        }
    },
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
 		//if (document.getElementById('myFolders_showFoldersTab').value == 'true') {
		//	YAHOO.ur.researcher.tabs.setActiveIndex('FOLDERS');
		//}
		
	    //var parentFolderId = document.getElementById('myFolders_parentFolderId').value;
	    
	    //YAHOO.ur.groupworkspace.file_system.getFolderById(parentFolderId);
    	YAHOO.ur.groupworkspace.file_system.createNewFolderDialog();
	    YAHOO.ur.groupworkspace.file_system.createNewLinkDialog();
	    YAHOO.ur.groupworkspace.file_system.createFolderDeleteConfirmDialog();
	}
	
 
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.groupworkspace.file_system.init);