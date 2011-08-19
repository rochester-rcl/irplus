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
YAHOO.namespace("ur.item");

// action to perform when submitting the personal folder form.
var myFolderAction =  basePath + 'user/getPersonalFolders.action';

// Action to perform when submitting selected files form
var mySelectedFilesAction = basePath + 'user/getSelectedFiles.action';

// Action to add, remove files from the selected list
var removeFileAction = basePath + 'user/removeFile.action';
var addFileAction = basePath + 'user/addFile.action';
var changeFileVersionAction = basePath + 'user/changeFileVersion.action';
var updateDescriptionAction = basePath + 'user/updateDescription.action';

// actions for adding and removing links
var updateLinkAction = basePath + 'user/updateItemLink.action';
var newLinkAction = basePath + 'user/addItemLink.action';

// Action to add files to item
var publicationWorkspaceAction = basePath + 'user/workspace.action?tabName=COLLECTION';
var viewInstitutionalItemAction = basePath + 'institutionalPublicationPublicView.action';

// Action to add files to item
var viewMetadataAction = basePath + 'user/viewItemMetadata.action';
var viewContributorsAction  = basePath + 'user/viewItemContributor.action';
var itemPreviewAction = basePath + 'user/previewPublication.action';

//Action to move the file up/down
var moveFileDownAction = basePath + 'user/moveFileDown.action';
var moveFileUpAction = basePath + 'user/moveFileUp.action';

YAHOO.ur.item = {
	
	/*
	 * This call back updates the html when a folder is
	 * clicked.
	 */
	getFolderCallback :
	{
	    success: function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	   
	            var divToUpdate = document.getElementById('newPersonalFolders');
	            divToUpdate.innerHTML = o.responseText; 
	        }
	    },
		
		failure: function(o) 
		{
	
		    alert('Get folder Failure ' + o.status + ' status text ' + o.statusText );
		}
	},
	
	/**
	 *  Function that retireves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getFolderById : function(folderId)
	{
	
	    document.getElementById('myFolders_parentFolderId').value = folderId;
	    
	    YAHOO.util.Connect.setForm('myFolders');
	       
	    var cObj = YAHOO.util.Connect.asyncRequest('POST',
	           myFolderAction , YAHOO.ur.item.getFolderCallback);
	},
	
	/**
	 *  Function that retireves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getFolder : function()
	{
	   
	    YAHOO.util.Connect.setForm('myFolders');
	       
	    var cObj = YAHOO.util.Connect.asyncRequest('POST',
	           myFolderAction , YAHOO.ur.item.getFolderCallback);
	},
	
	/*
	 * This call back updates the html when a file is added or 
	 * removed.
	 */
	getSelectedFilesCallback :
	{
	    success: function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	    
	            var divToUpdate = document.getElementById('newSelectedFiles');
	            divToUpdate.innerHTML = o.responseText; 
	        }
	    },
		
		failure: function(o) 
		{
		    //alert('Get selected files Failure ' + o.status + ' status text ' + o.statusText );
		}
	},
	
	/*
	 * This call back updates the html when a file is added or 
	 * removed. Updates the selected files with added file and updates the folders with "Added"
	 * message.
	 */
	updateFilesAndFoldersCallback :
	{
	    success: function(o) 
	    {
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       
	            var divToUpdate = document.getElementById('newSelectedFiles');
	            divToUpdate.innerHTML = o.responseText; 
	            YAHOO.ur.item.getFolder();
	        }
	    },
		
		failure: function(o) 
		{
		    alert('updateFilesAndFoldersCallback Failure ' + o.status + ' status text ' + o.statusText );
		}
	},
	
	/**
	 *  Function that change file version
	 *
	 */
	changeFileVersion : function(object, itemObjectId) {
		
		YAHOO.util.Connect.setForm('myFiles');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           changeFileVersionAction + '?itemObjectId=' + itemObjectId + '&fileVersionId=' + object.value, YAHOO.ur.item.getSelectedFilesCallback);
	           
	},
	
	/**
	 *  Function that change file version
	 *
	 */
	updateDescription : function(object, itemObjectId, type) {
		YAHOO.util.Connect.setForm('myFiles');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           updateDescriptionAction + '?itemObjectId=' + itemObjectId + '&description=' + object.value + '&itemObjectType=' + type,
	           YAHOO.ur.item.getSelectedFilesCallback);
	           
	},
	
	/**
	 * To retrieve the selected files and display in the table
	 *
	 */
	getSelectedFiles : function()
	{
		YAHOO.util.Connect.setForm('myFiles');
	   
	    var cObj = YAHOO.util.Connect.asyncRequest('POST',
	           mySelectedFilesAction , YAHOO.ur.item.getSelectedFilesCallback);
	   
	},

	/**
	 * Dialog to show error while adding file to item
	 */
	createAddFileErrorDialog : function() 
	{
        // Define various event handlers for Dialog
	    var handleOk = function() 
	    {
		    this.hide();
	    };
	    
	    // Instantiate the Dialog
	    YAHOO.ur.item.addFileErrorDialog = 
	        new YAHOO.widget.Dialog("addFileErrorDialog", 
									     { width: "500px",
										   visible: false,
										   modal: true,
										   buttons: [ { text:"Ok", handler:handleOk, isDefault:true } ]
										} );
	
	    YAHOO.ur.item.addFileErrorDialog.setHeader("Add File to Item");
	
	    //show the dialog and center
	    YAHOO.ur.item.addFileErrorDialog.showDialog = function()
	    {
	            YAHOO.ur.item.addFileErrorDialog.center();
	            YAHOO.ur.item.addFileErrorDialog.show();
	    };
	
	    // Render the Dialog
	    YAHOO.ur.item.addFileErrorDialog.render();

    },
    	
	/**
	 * Function to add file
	 *
	 */
	addFile : function(fileId)
	{

		var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
			        var response = eval("("+o.responseText+")");

			        // If file added, update selected files column
			        if( response.fileAdded == "true" )
			        {
					    YAHOO.ur.item.getSelectedFiles();
					    YAHOO.ur.item.getFolder();
			        }
			        else 
			        {
			            var divToUpdate = document.getElementById('fileNameErrorDiv');
			      
			            divToUpdate.innerHTML = response.message; 
			            YAHOO.ur.item.addFileErrorDialog.showDialog();
			        }
			    }
		    },
			
			failure: function(o) 
			{
			    alert('Add file Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
			
		YAHOO.util.Connect.setForm('myFolders');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           addFileAction + '?versionedFileId=' + fileId, callback);
	    
	},
	
	/**
	 * Function to add folder
	 */
	addFolder : function(folderId)
	{
		var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
			        var response = eval("("+o.responseText+")");

			        // If file added, update selected files column
			        if( response.fileAdded == "true" )
			        {
					    YAHOO.ur.item.getSelectedFiles();
			        }
			        else 
			        {
			    	    YAHOO.ur.item.getSelectedFiles();
			            var divToUpdate = document.getElementById('fileNameErrorDiv');
			            divToUpdate.innerHTML = response.message; 
			            YAHOO.ur.item.addFileErrorDialog.showDialog();
			        }
			     }
		    },
			
			failure: function(o) 
			{
			    alert('Add folder Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
			
		YAHOO.util.Connect.setForm('myFolders');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           addFileAction + '?folderId=' + folderId, callback);
	    
	},
	
	/**
	 * Function to remove file from the selected list
	 *
	 */
	removeFile : function(fileId, type)
	{
		YAHOO.util.Connect.setForm('myFiles');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           removeFileAction + '?itemObjectId=' + fileId + '&itemObjectType=' + type, YAHOO.ur.item.updateFilesAndFoldersCallback);
	},
	
	/**
	 * Function to add files to item and save the item
	 *
	 */
	finishLater : function()
	{
		if (document.myFiles.institutionalItemId.value != '') {
			document.myFiles.action = viewInstitutionalItemAction;
		} else {
			document.myFiles.action = publicationWorkspaceAction;;
		}
	    document.myFiles.submit();
	},
	
	/**
	 * Function to add files to item and save the item
	 *
	 */
	viewMetadata : function()
	{
		document.myFiles.action = viewMetadataAction;
	    document.myFiles.submit();
	},
	
	/**
	 * Function to add files to item and save the item
	 *
	 */
	viewContributors : function()
	{
	
		document.myFiles.action = viewContributorsAction;
	    document.myFiles.submit();
	},
	
	/**
	 * Function to save files and goto preview screen
	 *
	 */
	previewItem : function()
	{
	
		document.myFiles.action = itemPreviewAction;
	    document.myFiles.submit();
	},
	
	/**
	 * Function to move the file down
	 *
	 */
	moveDown : function(itemObjectId, type)
	{
		YAHOO.util.Connect.setForm('myFiles');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           moveFileDownAction + '?itemObjectId=' + itemObjectId + '&itemObjectType=' + type, YAHOO.ur.item.getSelectedFilesCallback);
	},
	
	/**
	 * Function to move the file up
	 *
	 */
	moveUp : function(itemObjectId, type)
	{
		YAHOO.util.Connect.setForm('myFiles');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           moveFileUpAction + '?itemObjectId=' + itemObjectId + '&itemObjectType=' + type, YAHOO.ur.item.getSelectedFilesCallback);
	},
	
	
	/** 
	  * clear out any form data messages or input
	  * in the link form
	  */
	clearLinkForm : function()
	{
        // clear out the error message
        var linkNameError = document.getElementById('linkNameError');
        linkNameError.innerHTML = "";

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
		    YAHOO.ur.item.newLinkDialog.hide();
		    YAHOO.ur.item.clearLinkForm();

		};
		
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new folder button is clicked.
		YAHOO.ur.item.newLinkDialog = new YAHOO.widget.Dialog('newLinkDialog', 
	        { width : "600px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
						  { text:"Cancel", handler:handleCancel } ]
			} );
		
		YAHOO.ur.item.newLinkDialog.showDialog = function()
		{
		    YAHOO.ur.item.newLinkDialog.center();
		    YAHOO.ur.item.newLinkDialog.show();
		    
		}

		YAHOO.ur.item.newLinkDialog.submit = function()
		{
		    YAHOO.util.Connect.setForm('newLinkForm');
		    
		    if( YAHOO.ur.item.newLinkDialog.validate() )
		    {
		        //based on what we need to do (update or create a 
		        // new folder) based on the action.
	            var action = newLinkAction;
		        if( document.newLinkForm.newLink.value != 'true')
		        {
		           action = updateLinkAction;
		        }
	
	            var cObj = YAHOO.util.Connect.asyncRequest('POST',
	            action, YAHOO.ur.item.getSelectedFilesCallback);
	            
	            YAHOO.ur.item.newLinkDialog.hide();
	            YAHOO.ur.item.clearLinkForm();
	        }	
	    }	
	   
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.item.newLinkDialog.validate = function() {
		    var data = this.getData();
			if (data.linkName == "" ) {
			    alert('Please enter link name.');
				return false;
			} 
			
			if (data.linkUrl == "" ) {
			    alert('Please enter the URL.');
				return false;
			}			
				return true;
			
		};
	
			
		// Render the Dialog
		YAHOO.ur.item.newLinkDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showLink", "click", 
		    YAHOO.ur.item.newLinkDialog.showDialog, 
		    YAHOO.ur.item.newLinkDialog, true);
	},
	
    // function to edit folder information
    editLink : function(linkId, linkName, linkDescription, linkUrl)
    {
    	document.newLinkForm.linkName.value = linkName;
	    document.newLinkForm.linkDescription.value = linkDescription;
	    document.newLinkForm.linkUrl.value = linkUrl;
	    document.newLinkForm.updateLinkId.value = linkId;
	    document.newLinkForm.newLink.value = "false";
	    YAHOO.ur.item.newLinkDialog.showDialog();
    },	
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	
	    var parentFolderId = document.getElementById('myFolders_parentFolderId').value;
	 
	    YAHOO.ur.item.getFolderById(parentFolderId);
	    
	    YAHOO.ur.item.getSelectedFiles();
	    YAHOO.ur.item.createNewLinkDialog();
	    YAHOO.ur.item.createAddFileErrorDialog();
	}
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.item.init);