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
YAHOO.namespace("ur.researcher.folder");

// action to perform when submitting the personal folder form.
var myFolderAction =  basePath + 'user/viewResearcherFolders.action';

// actions for adding and removing folders
var updateFolderAction = basePath + 'user/updateResearcherFolder.action';
var newFolderAction = basePath + 'user/addResearcherFolder.action';
var deleteFolderAction = basePath + 'user/deleteResearcherFileSystemObjects.action';

// actions for adding and removing links
var updateLinkAction = basePath + 'user/updateResearcherLink.action';
var newLinkAction = basePath + 'user/addResearcherLink.action';

// object to hold the specified folder data.
var myResearcherFolderTable = new YAHOO.ur.table.Table('myFolders', 'newResearcherFolders');


/**
 * Folder namespace
 */
YAHOO.ur.researcher.folder = {
	
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
		    YAHOO.ur.researcher.folder.newFolderDialog.hide();
		    YAHOO.ur.researcher.folder.clearFolderForm();
		};
		
		// handle a successful return
		var handleSuccess = function(o) 
		{
		    //get the response from adding a folder
		    var response = eval("("+o.responseText+")");
		    
		    //if the folder was not added then show the user the error message.
		    // received from the server
		    if( response.added == "false" )
		    {
		        var folderNameError = document.getElementById('folderNameError');
	            folderNameError.innerHTML = '<p id="newFolderForm_nameError">' + response.message + '</p>';
	            YAHOO.ur.researcher.folder.newFolderDialog.showDialog();
		    }
		    else
		    {
		        // we can clear the form if the folder was added
		        YAHOO.ur.researcher.folder.clearFolderForm();
		        YAHOO.ur.researcher.folder.newFolderDialog.hide();
		    }
		    myResearcherFolderTable.submitForm(myFolderAction);
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
		YAHOO.ur.researcher.folder.newFolderDialog = new YAHOO.widget.Dialog('newFolderDialog', 
	        { width : "600px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
						  { text:"Cancel", handler:handleCancel } ]
			} );
		
		// Submit form
		YAHOO.ur.researcher.folder.newFolderDialog.submit = function() {
		
		    YAHOO.util.Connect.setForm('newFolderForm');
		    
		    if( YAHOO.ur.researcher.folder.newFolderDialog.validate() )
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
	        		
		YAHOO.ur.researcher.folder.newFolderDialog.showDialog = function()
		{
		    YAHOO.ur.researcher.folder.newFolderDialog.center();
		    YAHOO.ur.researcher.folder.newFolderDialog.show();
		    
		}
	   
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.researcher.folder.newFolderDialog.validate = function() {
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
		YAHOO.ur.researcher.folder.newFolderDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showFolder", "click", 
		    YAHOO.ur.researcher.folder.newFolderDialog.showDialog, 
		    YAHOO.ur.researcher.folder.newFolderDialog, true);
	},
	    
    // function to edit folder information
    editFolder : function(folderId, folderName, folderDescription)
    {
    	document.newFolderForm.folderName.value = folderName;
	    document.newFolderForm.folderDescription.value = folderDescription;
	    document.newFolderForm.updateFolderId.value = folderId;
	    document.newFolderForm.newFolder.value = "false";
	    YAHOO.ur.researcher.folder.newFolderDialog.showDialog();
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
	},
	
	// create a dialog to confirm the deletion of folders.
	createFolderDeleteConfirmDialog : function() 
	{
	    // Define various event handlers for Dialog
		var handleYes = function() {
	 		
	 		YAHOO.util.Connect.setForm('myFolders');
		    
	            var cObj = YAHOO.util.Connect.asyncRequest('POST',
	            deleteFolderAction, callback);
	        
			this.hide();
		};
		
		var handleNo = function() {
		    //uncheck all the ones that have been checked
		    checked = document.myFolders.checkAllSetting.checked = false;
		    YAHOO.ur.researcher.folder.setCheckboxes();
			this.hide();
		};
		
		    // success when getting the file properties
	    var handleSuccess = function(o) {
		    var response = o.responseText;
		    var contentArea = document.getElementById('newResearcherFolders');
		    contentArea.innerHTML = o.responseText; 
		    
		    //show buttons that  make sense
	        var buttonsDiv = document.getElementById("files_folders_buttons");
	        buttonsDiv.style.visibility = 'visible';
	        
	    };
	   
	    // success when getting the file properties
	    var handleFailure = function(o) {
		    alert('Get file properties failed ' + o.status);
	    };
	
		// Instantiate the Dialog
		YAHOO.ur.researcher.folder.deleteFolder = 
		    new YAHOO.widget.Dialog("deleteFileFolderConfirmDialog", 
										     { width: "400px",
											   visible: false,
											   modal: true,
											   close: true,										   
											   buttons: [ { text:"Yes", handler:handleYes, isDefault:true },
														  { text:"No",  handler:handleNo } ]
											} );
		
		YAHOO.ur.researcher.folder.deleteFolder.setHeader("Delete?");
	   
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };	
		
		//show the dialog and center
		YAHOO.ur.researcher.folder.deleteFolder.showDialog = function()
		{
		     YAHOO.ur.researcher.folder.deleteFolder.center();
		     YAHOO.ur.researcher.folder.deleteFolder.show();
		}
		
		// Render the Dialog
		YAHOO.ur.researcher.folder.deleteFolder.render();
	
		YAHOO.util.Event.addListener("showDeleteFolder", 
		    "click", 
		    YAHOO.ur.researcher.folder.deleteFolder.showDialog, 
		    YAHOO.ur.researcher.folder.deleteFolder, true);
	
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
		    YAHOO.ur.researcher.folder.newLinkDialog.hide();
		    YAHOO.ur.researcher.folder.clearLinkForm();
		};
		
		// handle a successful return
		var handleSuccess = function(o) 
		{
	
		    //get the response from adding a folder
		    var response = eval("("+o.responseText+")");
		    
		    //if the folder was not added then show the user the error message.
		    // received from the server
		    if( response.added == "false" )
		    {
		        var linkNameError = document.getElementById('linkNameError');
	            linkNameError.innerHTML = '<p id="newLinkForm_nameError">' + response.message + '</p>';
	            YAHOO.ur.researcher.folder.newLinkDialog.show();
		    }
		    else
		    {
		        // we can clear the form if the folder was added
		        YAHOO.ur.researcher.folder.newLinkDialog.hide();
		        YAHOO.ur.researcher.folder.clearLinkForm();
		    }
		    myResearcherFolderTable.submitForm(myFolderAction);
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) 
		{
		    alert("The submission failed due to a network issue: " + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new folder button is clicked.
		YAHOO.ur.researcher.folder.newLinkDialog = new YAHOO.widget.Dialog('newLinkDialog', 
	        { width : "600px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
						  { text:"Cancel", handler:handleCancel } ]
			} );
		
		YAHOO.ur.researcher.folder.newLinkDialog.showLink = function()
		{
		    YAHOO.ur.researcher.folder.newLinkDialog.center();
		    YAHOO.ur.researcher.folder.newLinkDialog.show();
		}
	   
	    // Submit form
		YAHOO.ur.researcher.folder.newLinkDialog.submit = function()
		{	   
		    YAHOO.util.Connect.setForm('newLinkForm');
		    
		    if( YAHOO.ur.researcher.folder.newLinkDialog.validate() )
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
		YAHOO.ur.researcher.folder.newLinkDialog.validate = function() {
		    var data = this.getData();
			if (data.linkName == "" ) {
			    alert("A link name must be entered");
				return false;
			} else {
				return true;
			}
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
		// Render the Dialog
		YAHOO.ur.researcher.folder.newLinkDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showLink", "click", 
		    YAHOO.ur.researcher.folder.newLinkDialog.showLink, 
		    YAHOO.ur.researcher.folder.newLinkDialog, true);
		    
	 },
	   
    // function to edit folder information
    editLink : function(linkId, linkName, linkDescription, linkUrl)
    {
    	document.newLinkForm.linkName.value = linkName;
	    document.newLinkForm.linkDescription.value = linkDescription;
	    document.newLinkForm.linkUrl.value = linkUrl;
	    document.newLinkForm.updateLinkId.value = linkId;
	    document.newLinkForm.newLink.value = "false";
	    YAHOO.ur.researcher.folder.newLinkDialog.showLink();
    },
	
	/**
	 * This creates a hidden field appends it to the form for
	 * adding new sub folders for a given parent folder id.
	 */ 
	insertHiddenParentFolderId : function()
	{
	    var value = document.getElementById('myFolders_parentFolderId').value
	    document.getElementById('newFolderForm_parentFolderId').value = value;
	    document.getElementById('newLinkForm_parentFolderId').value = value;
	},
	
	/**
	 *  Function that retireves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getFolderById : function(folderId)
	{
	
		var callback =
		{
		    success : function(o) {
			    var response = o.responseText;
			    var contentArea = document.getElementById('newResearcherFolders');
			    contentArea.innerHTML = o.responseText; 
			    
			    //show buttons that  make sense
		        var buttonsDiv = document.getElementById("files_folders_buttons");
		        buttonsDiv.style.visibility = 'visible';
		        
		        YAHOO.ur.researcher.folder.insertHiddenParentFolderId();
		    },
		   
		    failure : function(o) {
			    alert('Get file properties failed ' + o.status);
		    }
		}	
	    
	    // execute the transaction
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	             myFolderAction +"?parentFolderId="+ 
	             folderId + '&researcherId='+ researcherId +'&bustcache='+new Date().getTime(), 
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
	    myResearcherFolderTable.submitForm(myFolderAction);
	},
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
		if (document.getElementById('myFolders_showFoldersTab').value == 'true') {
			YAHOO.ur.researcher.tabs.setActiveIndex('FOLDERS');
		}
		
	    var parentFolderId = document.getElementById('myFolders_parentFolderId').value;
	    
	    YAHOO.ur.researcher.folder.getFolderById(parentFolderId);
	    YAHOO.ur.researcher.folder.createNewFolderDialog();
	    YAHOO.ur.researcher.folder.createNewLinkDialog();
	    YAHOO.ur.researcher.folder.createFolderDeleteConfirmDialog();
	}
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.researcher.folder.init);