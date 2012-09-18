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
YAHOO.namespace("ur.folder");

// action to perform when submitting the personal folder form.
var myFolderAction =  basePath + 'user/viewPersonalFolders.action';
var lockFileAction = basePath + 'user/lockVersionedFile.action';
var unLockFileAction = basePath + 'user/unLockVersionedFile.action';

// actions for adding and removing folders
var updateFolderAction = basePath + 'user/updatePersonalFolder.action';
var newFolderAction = basePath + 'user/addPersonalFolder.action';
var deleteFolderAction = basePath + 'user/deletePersonalFileSystemObjects.action';
var getFolderAction = basePath + 'user/getPersonalFolder.action';

// Action to rename file
var fileRenameAction = basePath + 'user/renameFile.action';
var getFileNameAction = basePath + 'user/getFile.action';

// actions for moving folders
var myAvailableFolderMoveAction = basePath + 'user/availablePersonalFoldersMove.action';

// actions for inviting users
var inviteUserAction = basePath + 'user/viewInviteUser.action';
var checkSharePermissionsAction = basePath + 'user/checkSharePermission.action';

// view the version of a file
var viewFileVersion = basePath + 'user/viewNewFileVersionUpload.action';

// object to hold the specified folder data.
var myPersonalFolderTable = new YAHOO.ur.table.Table('myFolders', 'newPersonalFolders');

// array to hold drop down menus created
// this will be needed to later destroy the menus
// otherwise the menus will not render correctly
// in subsequent ajax calls.
var folderMenuArray = new Array();

// If there is no bookmarked state, assign the default state:
var personalFolderState = "0";


YAHOO.ur.folder = 
{
    /**
     * function to handle state changes
     */
    personalFolderStateChangeHandler : function(folderId)
    {
        var currentState = YAHOO.util.History.getCurrentState("personalFolderModule"); 
        var currentFolder = document.getElementById('myFolders_parentFolderId').value;
        // do not change state if we are on the current file / folder
        if( currentState != currentFolder )
        {
            document.getElementById('myFolders_parentFolderId').value = folderId;
            var folderId = document.getElementById("myFolders_parentFolderId").value;
            YAHOO.ur.folder.getFolderById(folderId, -1); 
            YAHOO.ur.folder.insertHiddenParentFolderId();
        }
    },
    
    /**
     * Clear the folder form
     */
    clearFolderForm : function()
    {
        // clear out the error message
        var folderError = document.getElementById('folder_error_div');
        folderError.innerHTML = "";    
	
	    document.newFolderForm.folderName.value = "";
	    document.newFolderForm.folderDescription.value = "";
	    document.newFolderForm.newFolder.value = "true";
	    document.newFolderForm.updateFolderId.value = "";
	   
    
    },
    
    /**
     * Set the sort type [ asc, desc, none ]
     * Set the element to sort on
     */
    updateSort : function(sortType, sortElement)
    {
        document.getElementById('folder_sort_type').value = sortType;
        document.getElementById('folder_sort_element').value = sortElement;
        var folderId = document.getElementById("myFolders_parentFolderId").value;
        YAHOO.ur.util.wait.waitDialog.showDialog();
        YAHOO.ur.folder.getFolderById(folderId, -1); 
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
    getFolderById : function(folderId, fileId)
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
	            document.getElementById('newPersonalFolders').innerHTML = response;
	            YAHOO.ur.folder.insertHiddenParentFolderId();
	            
	            // this is for capturing history
	            // it may fail if this is not an A grade browser so we need to
	            // catch the error.
	            // this will store the folder Id in the URL
	            try 
	            {
	                YAHOO.util.History.navigate( "personalFolderModule", folderId );
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
	            	window.location.href= basePath + 'user/personalFileDownload.action' + '?personalFileId=' +fileId;
	            }
	            YAHOO.ur.util.wait.waitDialog.hide();
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('get folder by id failure '  + o.status + ' status text ' + o.statusText);
	    };

	    //destroy the folder menus
        YAHOO.ur.folder.destroyFolderMenus();
    
        // set the state for the folder id
        personalFolderState = folderId;
 
        //set the folder id
        document.getElementById('myFolders_parentFolderId').value = folderId;
	    
	    YAHOO.util.Connect.setForm('myFolders');
    
        YAHOO.util.Connect.asyncRequest('POST', myFolderAction,
          {success: handleSuccess, failure: handleFailure});
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
    },
    
    /**
     * This creates a hidden field appends it to the form for
     * adding new sub folders for a given parent folder id.
     */ 
    insertHiddenParentFolderId : function()
    {
        var value = document.getElementById('myFolders_parentFolderId').value
        // create the input tag
        document.getElementById('newFolderForm_parentFolderId').value = value;
	    document.getElementById('file_upload_parent_folder_id').value = value;
    },
    
    /**
     * Dialog to create new folders
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
	        YAHOO.ur.folder.clearFolderForm();
	        YAHOO.ur.folder.newFolderDialog.hide();
	    };
	
	   // handle a successful return
	   var handleSuccess = function(o) 
	   {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		 	   
	            //get the response from adding a folder
	            var response = o.responseText;
	            var folderForm = document.getElementById('newFolderDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            folderForm.innerHTML = response;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newFolderForm_success").value;
	    
	            //if the folder was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.folder.newFolderDialog.showFolder();
	            }
	            else
	            {
	                // we can clear the form if the folder was added
	                YAHOO.ur.folder.newFolderDialog.hide();
	                YAHOO.ur.folder.clearFolderForm();
	            }
	            var folderId = document.getElementById("myFolders_parentFolderId").value;
                YAHOO.ur.folder.getFolderById(folderId, -1); 
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert("Create folder failed due to a network issue: " + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new folder button is clicked.
	    YAHOO.ur.folder.newFolderDialog = new YAHOO.widget.Dialog('newFolderDialog', 
        { width : "600px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
					  { text:"Cancel", handler:handleCancel } ]
		} );
	
	    // override the submit
	    YAHOO.ur.folder.newFolderDialog.submit = function()
	    {
	    	YAHOO.ur.folder.newFolderDialog.hide();
	    	YAHOO.ur.util.wait.waitDialog.showDialog();
	    	YAHOO.ur.folder.destroyFolderMenus();
	        YAHOO.util.Connect.setForm('newFolderForm');
	    
	        if( YAHOO.ur.folder.newFolderDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new folder) based on the action.
                var action = newFolderAction;
	            if( document.newFolderForm.updateFolderId.value != '')
	            {
	               action = updateFolderAction;
	            }

                var cObj = YAHOO.util.Connect.asyncRequest('POST',
                action, callback);
            }
	    };
	    
	    YAHOO.ur.folder.newFolderDialog.showFolder = function()
	    {
	        YAHOO.ur.folder.newFolderDialog.center();
	        YAHOO.ur.folder.newFolderDialog.show();
	        document.getElementById("folder").focus();
	        YAHOO.ur.shared.file.inbox.getSharedFilesCount();
	    };
   
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.folder.newFolderDialog.validate = function() {
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
	    YAHOO.ur.folder.newFolderDialog.render();
    },
    
    /**
     * Function to edit folder information
     */
    editFolder : function(id)
    {
	    /*
         * This call back updates the html when editing the folder
         */
        var callback =
        {
            success: function(o) 
            {
 			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		             
                    var divToUpdate = document.getElementById('newFolderDialogFields');
                    divToUpdate.innerHTML = o.responseText;

                    document.newFolderForm.newFolder.value = "false";
                    YAHOO.ur.folder.newFolderDialog.showFolder();
                }
                
            },
	
	        failure: function(o) 
	        {
	            alert('Edit Folder Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getFolderAction + '?updateFolderId=' + id  + '&bustcache='+new Date().getTime(), 
            callback, null);
                	
	},
	
	/**
     * Function to create a new folder
     */
    newFolder : function(parentId)
    {
	    /*
         * This call back updates the html when editing the folder
         */
        var callback =
        {
            success: function(o) 
            {
 			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		             
                    var divToUpdate = document.getElementById('newFolderDialogFields');
                    divToUpdate.innerHTML = o.responseText;

                    document.newFolderForm.newFolder.value = "true";
                    YAHOO.ur.folder.newFolderDialog.showFolder();
                }
                
            },
	
	        failure: function(o) 
	        {
	            alert('Edit Folder Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getFolderAction + '?parentFolderId=' + parentId  + '&bustcache='+new Date().getTime(), 
            callback, null);
                	
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
	            document.getElementById('newPersonalFolders').innerHTML = response;
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('delete files folders failure '  + o.status + ' status text ' + o.statusText);
	    };

	    YAHOO.util.Connect.setForm('myFolders');
    
        YAHOO.util.Connect.asyncRequest('POST', deleteFolderAction,
          {success: handleSuccess, failure: handleFailure});
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
        	YAHOO.ur.folder.destroyFolderMenus();
		    this.hide();
		    YAHOO.ur.folder.deleteFilesFolders();
	    };
	    
	    var handleNo = function() 
	    {
	        //uncheck all the ones that have been checked
	        checked = document.myFolders.checkAllSetting.checked = false;
	        YAHOO.ur.folder.setCheckboxes();
		    this.hide();
	    };

	    // Instantiate the Dialog
	    YAHOO.ur.folder.deleteFolder = 
	        new YAHOO.widget.Dialog("deleteFileFolderConfirmDialog", 
									     { width: "400px",
										   visible: false,
										   modal: true,
										   buttons: [ { text:"Yes", handler:handleYes, isDefault:true },
													  { text:"No",  handler:handleNo } ]
										} );
	
	    YAHOO.ur.folder.deleteFolder.setHeader("Delete?");
	
	    //show the dialog and center
	    YAHOO.ur.folder.deleteFolder.showDialog = function()
	    {
	        if (!urUtil.checkForNoSelections(document.myFolders.folderIds) &&
	            !urUtil.checkForNoSelections(document.myFolders.fileIds))
		    {
			     alert('Please select at least one checkbox next to the files or folders you wish to delete.');
	        } 
	        else
	        {
	            YAHOO.ur.folder.deleteFolder.center();
	            YAHOO.ur.folder.deleteFolder.show();
	        }
	    };
	
	    // Render the Dialog
	    YAHOO.ur.folder.deleteFolder.render();

    },
    
    /**
     * Select and delete the folder or file 
     */
    deleteSingleConfirm : function(elementId)
    {
       //uncheck all the ones that have been checked
       checked = document.myFolders.checkAllSetting.checked = false;
       YAHOO.ur.folder.setCheckboxes();
   
       element = document.getElementById(elementId);
       element.checked=true;
       YAHOO.ur.folder.deleteFolder.showDialog();
    },
    
    /**
     * Select and move the folder or file 
     */
    moveSingle : function(elementId)
    {
       //uncheck all the ones that have been checked
       checked = document.myFolders.checkAllSetting.checked = false;
       YAHOO.ur.folder.setCheckboxes();
   
       element = document.getElementById(elementId);
       element.checked=true;
       YAHOO.ur.folder.moveFolderData();
    },
    
    /**
     * Select and share the file / folder
     */
    shareSingleConfirm : function(elementId)
    {
       //uncheck all the ones that have been checked
       checked = document.myFolders.checkAllSetting.checked = false;
       YAHOO.ur.folder.setCheckboxes();
   
       element = document.getElementById(elementId);
       element.checked=true;
       YAHOO.ur.folder.inviteUser();
    },
    
    /**
     * Select and publish a single file
     */
    publishSingle : function(elementId)
    {
       //uncheck all the ones that have been checked
       checked = document.myFolders.checkAllSetting.checked = false;
       YAHOO.ur.folder.setCheckboxes();
   
       element = document.getElementById(elementId);
       element.checked=true;
       YAHOO.ur.personal.collection.newItemDialog.createFromFilesFolders();
    },
    
    /**
     * function to build the folder menu
     */
    buildFolderMenu : function(folderId, element, div, menuName)
    {
     
        var buttonMenu = document.getElementById(div);
        var xVal = YAHOO.util.Dom.getX(div);
        var yVal = YAHOO.util.Dom.getY(div);
    
        // get the menu
        var other = document.getElementById(menuName);

       // we only want to initialize once  first time the menu
       // has been clicked
       if( other == null )
       {
              
            /*
              Instantiate the menu.  The first argument passed to the 
              constructor is the id of the DOM element to be created for the 
              menu; the second is an object literal representing a set of 
              configuration properties for the menu.
            */
            var dropMenu = new YAHOO.widget.Menu(menuName, {x:xVal, y:yVal});
            folderMenuArray.push(dropMenu);
        
            /*
              Add items to the menu by passing an array of object literals 
              (each of which represents a set of YAHOO.widget.MenuItem 
              configuration properties) to the "addItems" method.
             */
             dropMenu.addItems([
                 { text: '<span class="wrenchBtnImg">&nbsp;</span> Edit',  url: "javascript:YAHOO.ur.folder.editFolder(" + folderId + ")" },
                 { text: '<span class="pageWhiteGoBtnImg">&nbsp;</span> Move', url: "javascript:YAHOO.ur.folder.moveSingle('folder_checkbox_"+ folderId +"')" },
                 { text: '<span class="groupAddBtnImg">&nbsp;</span> Share',  url: "javascript:YAHOO.ur.folder.shareSingleConfirm('folder_checkbox_"+ folderId +"')" },
                 { text: '<span class="reportGoBtnImg">&nbsp;</span> Publish', url: "javascript:YAHOO.ur.folder.publishSingle('folder_checkbox_"+ folderId +"')" },  
                 { text: '<span class="deleteBtnImg">&nbsp;</span> Delete', url: "javascript:YAHOO.ur.folder.deleteSingleConfirm('folder_checkbox_"+ folderId +"')" }
             ]);
               
             dropMenu.showEvent.subscribe(function () {
                 this.focus();
             });

             /*
                Since this menu is built completely from script, call the "render" 
                method passing in the id of the DOM element that the menu's 
                root element should be appended to.
              */
              dropMenu.render(buttonMenu);
              YAHOO.util.Event.addListener(element, "click", dropMenu.show, null, dropMenu);
              dropMenu.show();
        }
        
        YAHOO.ur.shared.file.inbox.getSharedFilesCount();
    },
    
    /*
     * Destroys all of the drop down menues in the table
     * this is needed for ajax calls and re-rendering the table drop down menus
     */
    destroyFolderMenus : function()
    {
        for( var i = 0; i < folderMenuArray.length; i++ )
        {
             folderMenuArray[i].destroy();
             folderMenuArray[i] = null;
        }
    
        var size = folderMenuArray.length;
    
        for( var i = 0; i < size; i++ )
        {
            folderMenuArray.pop();
        }
    },
    
   /** 
    * causes the button
    * to attach the menu to 
    * unique menu id
    * id of the file 
    */
    buildFileMenu : function(element, div, 
        menuName, 
        fileId, 
        userId, 
        locked, 
        canUnLock, 
        canLock, 
        canBreakLock,
        canShare, 
        canEdit, 
        fileName,
        fileNameWithoutExtension){
       

        var buttonMenu = document.getElementById(div);
        var xVal = YAHOO.util.Dom.getX(div);
        var yVal = YAHOO.util.Dom.getY(div);
    
        // get the menu
        var other = document.getElementById(menuName);
   
        // we only want to initialize once  first time the menu
        // has been clicked
        if( other == null )
        {
            //path to look at properties for a file
            filePropertiesUrl = basePath + 'user/viewPersonalFile.action?personalFileId='+ fileId;
               
            /*
               Instantiate the menu.  The first argument passed to the 
               constructor is the id of the DOM element to be created for the 
               menu; the second is an object literal representing a set of 
               configuration properties for the menu.
            */
            var dropMenu = new YAHOO.widget.Menu(menuName, {x:xVal, y:yVal });
         
             //place the menus in an array to be destroyed later
             //this is for ajax rendering
             folderMenuArray.push(dropMenu);

             dropMenu.addItem({text: '<span class="pageWhitePutBtnImg">&nbsp;</span> Download', url: basePath + 'user/personalFileDownload.action' + '?personalFileId=' +fileId });
         
             dropMenu.addItem({text: '<span class="reportEditBtnImg">&nbsp;</span> Edit Name/Description', url: 'javascript:YAHOO.ur.folder.renameFile( ' + fileId + ')' });
         
             /*
               Add items to the menu by passing an array of object literals 
               (each of which represents a set of YAHOO.widget.MenuItem 
                configuration properties) to the "addItems" method.
              */
             if( !locked && canLock)
             {
                 dropMenu.addItem({ text: '<span class="lockBtnImg">&nbsp;</span> Lock &amp; Edit',  url: 'javascript:YAHOO.ur.folder.getLockOnFileId('+ fileId + ', ' + userId + ')' });
             }
              
             if( canUnLock )
             {
                 dropMenu.addItem({ text: '<span class="unlockBtnImg">&nbsp;</span> UnLock',  url: 'javascript:YAHOO.ur.folder.unLockFile(' + fileId +', ' + userId + ')' });
             }
         
             if( canEdit )
             {
                 dropMenu.addItem({text: '<span class="pageAddBtnImg">&nbsp;</span> Add New Version', url: 'javascript:YAHOO.ur.folder.dropDownVersionedFileUpload( ' + fileId + ')'});
             }
         
             if( canBreakLock )
             {
                 dropMenu.addItem({ text: '<span class="deleteLockBtnImg">&nbsp;</span> Override Lock',  url: 'javascript:YAHOO.ur.folder.unLockFile(' + fileId +', ' + userId + ')' });
             }  
         
             if( canShare )
             {
                 dropMenu.addItem({text: '<span class="groupAddBtnImg">&nbsp;</span> Share', url: 'javascript:YAHOO.ur.folder.shareSingleConfirm(' + "'" + 'file_checkbox_' + fileId + "'" + ')' });
             }
         
             dropMenu.addItems([
                 { text: '<span class="reportGoBtnImg">&nbsp;</span> Publish', url: "javascript:YAHOO.ur.folder.publishSingle('file_checkbox_"+ fileId +"')" },             
                 { text: '<span class="pageWhiteGoBtnImg">&nbsp;</span> Move', url: "javascript:YAHOO.ur.folder.moveSingle('file_checkbox_"+ fileId +"')" },
                 { text: '<span class="wrenchBtnImg">&nbsp;</span> Properties',  url: filePropertiesUrl },
                 { text: '<span class="deleteBtnImg">&nbsp;</span> Delete', url: "javascript:YAHOO.ur.folder.deleteSingleConfirm('file_checkbox_"+ fileId +"')" }
             ]);
         
             dropMenu.showEvent.subscribe(function () {
                 this.focus();
             });

             /*
                Since this menu is built completely from script, call the "render" 
                method passing in the id of the DOM element that the menu's 
                root element should be appended to.
              */
              dropMenu.render(buttonMenu);
              YAHOO.util.Event.addListener(element, "click", dropMenu.show, null, dropMenu);
              dropMenu.show();
        }
        
        YAHOO.ur.shared.file.inbox.getSharedFilesCount();
    },
        
    /**
     *  Function requests a lock on a specified file
     *
     *  The id of the file to lock and the user id
     */
    getLockOnFileId : function (fileId, userId)
    {
        var callback =
        {
            success: function(o) 
            {
                /* evaluate the response */
                if( o != null )
                {
                    if( o.responseText != null )
                    {   
	                     // check for the timeout - forward user to login page if timout
	                     // occured
	                     if( !urUtil.checkTimeOut(o.responseText) )
	                     {
	                         var response = eval("("+o.responseText+")");
	                     }
	                 }
	            }
	    
	            if( response.lockStatus == 'LOCK_OBTAINED')
	            {
	                var folderId = document.getElementById("myFolders_parentFolderId").value;
	                YAHOO.ur.folder.getFolderById(folderId, response.personalFileId); 
	            	

	                
	            }
	            else if( response.lockStatus == 'LOCKED_BY_USER')
	            {
	                alert('Folder already locked by ' + response.lockUsername);
	                var folderId = document.getElementById("myFolders_parentFolderId").value;
	                YAHOO.ur.folder.getFolderById(folderId, -1); 
	            }
	            else if( response.lockStatus == 'LOCK_NOT_ALLOWED')
	            {
	                alert('You are not allowed to lock the specified file');
	            }
	            else
	            {
	                alert( 'Lock status ' + response.lockStatus + ' is not understood' );
	            }
            },
	
	        failure: function(o) 
	        {
	            alert('Get lock on file failure status: ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            lockFileAction + '?personalFileId=' + fileId + '&userId=' + userId + 
            '&bustcache='+new Date().getTime(), callback, null);
    },
    
    /**
     * make a call to un-lock the specified id
     */
    unLockFile : function(fileId, userId)
    {
    	
        var callback =
        {
            success: function(o) 
            {
                if( o != null )
                {
                    if( o.responseText != null )
                    {
	                     // check for the timeout - forward user to login page if timout
	                     // occured
	                     if( !urUtil.checkTimeOut(o.responseText) )
	                     {
	                         var response = eval("("+o.responseText+")");
	                     }
	                }
	            }
	    
	            if( response.unLockStatus == 'UN_LOCKED_BY_USER')
	            {
	                var folderId = document.getElementById("myFolders_parentFolderId").value;
	                YAHOO.ur.folder.getFolderById(folderId, -1); 
	            }
	            else if( response.unLockStatus == 'UN_LOCK_NOT_ALLOWED')
	            {
	                alert('You do not have permission to unlock the specified file');
	            }
	            else
	            {
	                alert( 'Un lock status ' + response.lockStatus + ' is not understood' );
	            }
            },
	
	        failure: function(o) 
	        {
	            alert('Unlock file failure status: ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
        unLockFileAction + '?personalFileId=' + fileId + '&userId=' + userId + 
        '&bustcache='+new Date().getTime(), callback, null);
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
            var singleFileUploadAction = basePath + 'user/singleFileUpload.action';
	        YAHOO.util.Connect.setForm('singleFileUploadForm', true, true);
	    	    
	        if( YAHOO.ur.folder.singleFileUploadDialog.validate() )
	        {
	            YAHOO.ur.folder.singleFileUploadDialog.hide();
		    	YAHOO.ur.util.wait.waitDialog.showDialog();
	            
	            //based on what we need to do (update or create a 
	            // new news item) based on the action.
                var cObj = YAHOO.util.Connect.asyncRequest('post',
                      singleFileUploadAction, callback);
                
                // clear the upload form of the file name
                YAHOO.ur.folder.clearSingleFileUploadForm();
                
           
            }
	    };
	
	    // handle a cancel of the adding news item dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.folder.singleFileUploadDialog.hide();
	        YAHOO.ur.folder.clearSingleFileUploadForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        
	        YAHOO.ur.folder.destroyFolderMenus();
	        var response = o.responseText;
	        
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	        
	            var uploadForm = document.getElementById('upload_form_fields');
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            uploadForm.innerHTML = o.responseText;
	         
	            try{
	                // determine if the add/edit was a success
	                var success = document.getElementById("file_added").value;
	                var parentFolderId = document.getElementById("file_upload_parent_folder_id").value;
	                //if the content type was not added then show the user the error message.
	                // received from the server
	                if( success == "false" )
	                {
	        	    	YAHOO.ur.util.wait.waitDialog.hide();
                        YAHOO.ur.folder.singleFileUploadDialog.showDialog();
	                }
	                else
	                {
	                    // we can clear the upload form and get the pictures
	           
	                    YAHOO.ur.folder.clearSingleFileUploadForm();
	                    var folderId = document.getElementById("myFolders_parentFolderId").value;
		                YAHOO.ur.folder.getFolderById(folderId, -1); 
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
	        alert('Single file upload submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new news item button is clicked.
	
	    YAHOO.ur.folder.singleFileUploadDialog = new YAHOO.widget.Dialog('singleFileUploadDialog', 
        { width : "850px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					  { text:'Cancel', handler:handleCancel } ]
        } );
		
        //shows
        //and centers the dialog box
        YAHOO.ur.folder.singleFileUploadDialog.showDialog = function()
        {
            YAHOO.ur.folder.singleFileUploadDialog.center();
            YAHOO.ur.folder.singleFileUploadDialog.show();
            YAHOO.ur.shared.file.inbox.getSharedFilesCount();
        }
    
 	    // Validate the entries in the form to require that a name is entered
	    YAHOO.ur.folder.singleFileUploadDialog.validate = function() 
	    {
	        var fileName = document.getElementById('single_file_upload').value;
	    
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
	    YAHOO.ur.folder.singleFileUploadDialog.render();

    },
    
   /** 
    * clear out any form data messages or input
    * in the upload single file form
    */
    clearSingleFileUploadForm : function()
    {
	    document.getElementById('single_file_upload').value = "";
	    document.getElementById('user_file_description').value = "";
	
        var div = document.getElementById('single_file_upload_error');
        div.innerHTML= "";
    },   
    
    /**
     * function to change the class on a button
     */
    changeButtonClass : function(buttonId, cssClass)
    {
        var button = document.getElementById(buttonId);
        button.className = cssClass
    },
    
    /**
     * Versioned file upload
     */
   dropDownVersionedFileUpload : function(fileId)
    {	    
	    /*
         * This call back updates the html when a adding a new versioned file
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {   
                    var divToUpdate = document.getElementById('version_upload_form_fields');
                    divToUpdate.innerHTML = o.responseText; 
	                YAHOO.ur.folder.versionedFileUploadDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get new versioned info file failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
           viewFileVersion + '?personalFileId=' + fileId +  '&bustcache='+new Date().getTime(), 
            callback, null);
    },
    
    /**
     * Function to deal with uploading a versioned file
     */
    versionedFileUpload : function()
    {
      // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	       
	        // action to perform when submitting the news items.
            var versionedFileUploadAction = basePath + 'user/uploadNewFileVersion.action';
	        YAHOO.util.Connect.setForm('versionedFileUploadForm', true, true);
	    
	        if( YAHOO.ur.folder.versionedFileUploadDialog.validate() )
	        {
	            YAHOO.ur.folder.versionedFileUploadDialog.hide()
	            	    	YAHOO.ur.util.wait.waitDialog.showDialog();
	            //based on what we need to do (update or create a 
	            // new news item) based on the action.
                var cObj = YAHOO.util.Connect.asyncRequest('post',
                 versionedFileUploadAction, callback);
                YAHOO.ur.folder.clearVersionedFileUploadForm();
            }
	    };
	
	    // handle a cancel of the adding a new version
	    var handleCancel = function() 
	    {
	        YAHOO.ur.folder.versionedFileUploadDialog.hide();
	        YAHOO.ur.folder.clearVersionedFileUploadForm();
	    };
	
	    //handle the sucessful upload
	    var handleSuccess = function(o) 
	    {
	        YAHOO.ur.folder.destroyFolderMenus();
	        var response = o.responseText;
	        
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	        
	            var uploadForm = document.getElementById('version_upload_form_fields');
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            uploadForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("version_added").value;
	    
	            var pageType = document.getElementById("page_type").value;
	            //if the content type was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
	    	    	YAHOO.ur.util.wait.waitDialog.hide();
                    YAHOO.ur.folder.versionedFileUploadDialog.show();
	            }
	            else
	            {
	                // we can clear the upload form and get the pictures
	                if( pageType == "properties_page" )
	                {
	                    var fileId = document.getElementById("personal_file_id").value;
	                    YAHOO.ur.folder.getPropertiesForFile(fileId);
	                }
	                else 
	                {
	                    var folderId = document.getElementById("myFolders_parentFolderId").value;
	                    YAHOO.ur.folder.getFolderById(folderId, -1); 
	                }
	    	    	YAHOO.ur.util.wait.waitDialog.hide();
	            }
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o)
	    {
	        alert('Single file upload submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new news item button is clicked.
	    YAHOO.ur.folder.versionedFileUploadDialog = new YAHOO.widget.Dialog('versionedFileUploadDialog', 
        { width : "850px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					  { text:'Cancel', handler:handleCancel } ]
		} );
	
	    // center and show the dialog
        YAHOO.ur.folder.versionedFileUploadDialog.showDialog = function()
        {
            YAHOO.ur.folder.versionedFileUploadDialog.center();
            YAHOO.ur.folder.versionedFileUploadDialog.show();
        }
    
 	    // Validate the entries in the form to require that a name is entered
	    YAHOO.ur.folder.versionedFileUploadDialog.validate = function() 
	    {
	        var fileName = document.getElementById('new_version_file').value;
	    
		    if (fileName == "" || fileName == null) {
		        alert('A File name must be entered');
			    return false;
		    } 
		    else 
		    {
			    return true;
		    }
	    };
	

	    // Wire up the success and failure handlers
	    var callback = { upload: handleSuccess, failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.folder.versionedFileUploadDialog.render();
    },
    
     /** 
      * clear out any form data messages or input
      * in the upload single file form
      */
    clearVersionedFileUploadForm : function()
    {
	    document.getElementById('personal_file_id').value = "";
	    document.getElementById('new_version_file').value = "";
	    document.getElementById('user_file_description').value = "";
	    var versionUploadHeader = document.getElementById('add_version_note');
        versionUploadHeader.innerHTML = '';
	
	    var uploadError = document.getElementById('locked_by_user_error');
   
	    //clear out any error information
	    if( uploadError != null )
	    {
	        if( uploadError.innerHTML != null && uploadError.innerHTML != "")
	        { 
	            uploadError.innerHTML="";
	        }
	    }
	
	    var uploadError = document.getElementById('cannot_lock_error');
  
	    //clear out any error information
	    if( uploadError != null )
	    {
	        if( uploadError.innerHTML != null && uploadError.innerHTML != "")
	        { 
	            uploadError.innerHTML="";
	        }
	    }
    },
    
    /*
     * Dialog to show users the files that cannot be shared
     */
    createInviteErrorDialog : function() 
    {
        // Handler to continur sharing the files
	    var handleSubmit = function() 
	    {
            YAHOO.ur.folder.inviteErrorDialog.hide();
        
            // Go to the share screen only if there can any files to be shared
            if (document.inviteFilesForm.shareFileIds.value != '') {
        	    document.inviteFilesForm.action = inviteUserAction;
			    document.inviteFilesForm.submit();
		    }
	    };

	    // Handler to cancel the share
	    var handleCancel = function() 
	    {
		    YAHOO.ur.folder.inviteErrorDialog.hide();
	    };

	    // handle the successful check for file SHARE permissions
	    var handleSuccess = function(o) 
	    {

	        var response = o.responseText;
	        
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	        
	            var inviteForm = document.getElementById('invite_form_fields');

	            // update the form fields with the response.
	            inviteForm.innerHTML = o.responseText;
	    
	            // determine if there any files that has no SHARE permission
	            var hasSharePermission = document.getElementById("has_permission").value;
	    
	            //if there are files that cannot be shared then show the list of files
	            // that has no SHARE permission
	            if( hasSharePermission == "false" )
	            {
                    YAHOO.ur.folder.inviteErrorDialog.showDialog();
	            }
	            else
	            {
	                // make sure a file or folder has been selected
	                if (!urUtil.checkForNoSelections(document.myFolders.folderIds) &&
	                    !urUtil.checkForNoSelections(document.myFolders.fileIds))
		            {
			             alert('Please select at least one checkbox next to the files or folders you wish to share.');
	                } 
	                else
	                {
		                YAHOO.ur.folder.destroyFolderMenus();
		    
		                // If user have SHARE permission for all files then goto invite screen
			            document.inviteFilesForm.action = inviteUserAction;
			            document.inviteFilesForm.submit();
			        }
	            }
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) {
	        alert('Inviting the user to share a file failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // the files has to be displayed.
	    YAHOO.ur.folder.inviteErrorDialog = new YAHOO.widget.Dialog('inviteConfirmDialog', 
        { width : "500px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:'Ok', handler:handleSubmit, isDefault:true },
					  { text:'Cancel', handler:handleCancel } ]
		} );
		
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
		
		
		YAHOO.ur.folder.inviteErrorDialog.showDialog = function()
		{
		    YAHOO.ur.folder.inviteErrorDialog.center();
		    YAHOO.ur.folder.inviteErrorDialog.show();
		}
		
	    // Render the Dialog
	    YAHOO.ur.folder.inviteErrorDialog.render();


	    /*
	     * Function to invite user to collaborate on set of files
	     */
	    YAHOO.ur.folder.inviteUser = function()
	    {
		    YAHOO.util.Connect.setForm('myFolders');
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	                 checkSharePermissionsAction,  callback);
	    };
    },
    
    /**
     * clear the file rename form
     */
    clearFileRenameForm : function()
    {
        // clear out the error message
        var renameError = document.getElementById('rename_error_div');
        renameError.innerHTML = "";   
        
        document.renameForm.newFileName.value = "";
        document.renameForm.fileDescription.value ="";
    },
    
    /**
     * Function to create the rename dialog
     */
    createFileRenameDialog : function()
    {
	    // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
			this.submit();
	    };
		
	    // handle a cancel of the adding folder dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.folder.clearFileRenameForm();
	        YAHOO.ur.folder.renameFileDialog.hide();
	    };
	
	    // handle a successful return
	    var handleSuccess = function(o) 
	    {
	        //get the response from renaming a file
	        var response = o.responseText;
	        
	        	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var renameForm = document.getElementById('renameFileDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            renameForm.innerHTML = response;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("renameForm_success").value;
	    
	            //if the rename was not success then show the user the error message
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.folder.renameFileDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the file was renamed
	            	YAHOO.ur.folder.renameFileDialog.hide();
	                YAHOO.ur.folder.clearFileRenameForm();
	                var folderId = document.getElementById("myFolders_parentFolderId").value;
	                YAHOO.ur.folder.getFolderById(folderId, -1); 
	            }
	        }

	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert("Submision failed due to a network issue : " + o.status + " status text " + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // rename file button is clicked.
	    YAHOO.ur.folder.renameFileDialog = new YAHOO.widget.Dialog('renameFileDialog', 
            { width : "600px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
					      { text:"Cancel", handler:handleCancel } ]
		    } );
	
		YAHOO.ur.folder.renameFileDialog.submit = function()
		{
	        YAHOO.ur.folder.destroyFolderMenus();
	        YAHOO.util.Connect.setForm('renameForm');
	    
	        if( YAHOO.ur.folder.renameFileDialog.validate() )
	        {
                var cObj = YAHOO.util.Connect.asyncRequest('POST',
                fileRenameAction, callback);
            }
        }
        	
	    YAHOO.ur.folder.renameFileDialog.showDialog = function()
	    {
	        YAHOO.ur.folder.renameFileDialog.center();
	        YAHOO.ur.folder.renameFileDialog.show();
	    }
   
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.folder.renameFileDialog.validate = function() 
	    {
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
	    YAHOO.ur.folder.renameFileDialog.render();
        
    },
    
    /**
     * Function to rename file
     */
    renameFile : function(fileId)
    {
	    /*
         * This call back updates the html when editing file name
         */
        var callback =
        {
            success: function(o) 
            {
                // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
                    var divToUpdate = document.getElementById('renameFileDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
				    YAHOO.ur.folder.renameFileDialog.showDialog();   
				}             
            },
	
	        failure: function(o) 
	        {
	            alert('Rename a file Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getFileNameAction + '?personalFileId=' + fileId +  '&bustcache='+new Date().getTime(), 
            callback, null);
                	
	},
    
    /**
     * Allow files and folders to be moved
     */
    moveFolderData : function()
    {
        if (!urUtil.checkForNoSelections(document.myFolders.folderIds) &&
	        !urUtil.checkForNoSelections(document.myFolders.fileIds))
		{
			 alert('Please select at least one checkbox next to the files or folders you wish to move.');
	    } 
	    else
	    {
            var viewMoveFoldersAction = basePath + 'user/viewMovePersonalFolderLocations.action';
            document.myFolders.action = viewMoveFoldersAction;
            document.myFolders.submit();
        }
    },
    
    init : function()
    {
        var parentFolderId = document.getElementById('myFolders_parentFolderId').value
        YAHOO.ur.folder.getFolderById(parentFolderId, -1);
        YAHOO.ur.folder.createNewFolderDialog();
        YAHOO.ur.folder.createFolderDeleteConfirmDialog();
        YAHOO.ur.folder.singleFileUpload();
        YAHOO.ur.folder.versionedFileUpload();
        YAHOO.ur.folder.createInviteErrorDialog();
        YAHOO.ur.folder.createFileRenameDialog();
        
        // register the history system
        YAHOO.util.History.register("personalFolderModule", personalFolderState, 
        YAHOO.ur.folder.personalFolderStateChangeHandler);
    }
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.folder.init);