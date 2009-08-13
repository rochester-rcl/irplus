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
YAHOO.namespace("ur.file.properties");

// action for locking / unlocking files
var lockFileAction = basePath + 'user/lockVersionedFile.action';
var unLockFileAction = basePath + 'user/unLockVersionedFile.action';

// Action to rename file
var fileRenameAction = basePath + 'user/renameFile.action';
var getFileNameAction = basePath + 'user/getFile.action';
var changeOwnerAction = basePath + 'user/changeOwner.action';

// actions for inviting users
var inviteUserAction = basePath + 'user/viewInviteUser.action';

// add a new version of a file
var uploadNewFileVersion = basePath + 'user/viewNewFileVersionUpload.action';
var getFilePropertiesAction = basePath + 'user/getPersonalFileProperties.action';


YAHOO.ur.file.properties = 
{

    /** get the file properties for the specified file */
    getFileProperties : function(fileId)
    {
        var callback = 
        {
            success : function(o)
            {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {                   
                    var div = document.getElementById('file_properties');
                    div.innerHTML = o.responseText;
                }
            },
            
            failure : function(o)
            {
                alert( "failure to get properties");
            }
        };
            
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getFilePropertiesAction + '?personalFileId=' + fileId + 
            '&bustcache='+new Date().getTime(), callback, null);
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
	                //allow user to download the file
	                window.location = basePath + 'user/personalFileDownload.action' + '?personalFileId=' +response.personalFileId ;
	                YAHOO.ur.file.properties.getFileProperties(fileId);
	            }
	            else if( response.lockStatus == 'LOCKED_BY_USER')
	            {
	                alert('File already locked by ' + response.lockUsername);
	                YAHOO.ur.file.properties.getFileProperties(fileId);
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
                //determine what page we are on
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
	                var fileId = document.getElementById("personal_file_id").value;
	                YAHOO.ur.file.properties.getFileProperties(fileId);
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
	    
	        if( YAHOO.ur.file.properties.versionedFileUploadDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new news item) based on the action.
                var cObj = YAHOO.util.Connect.asyncRequest('post',
                 versionedFileUploadAction, callback);
            }
	    };
	
	    // handle a cancel of the adding a new version
	    var handleCancel = function() 
	    {
	        YAHOO.ur.file.properties.versionedFileUploadDialog.hide();
	        YAHOO.ur.file.properties.clearVersionedFileUploadForm();
	    };
	
	    //handle the sucessful upload
	    var handleSuccess = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	    
	            var response = o.responseText;
	            var uploadForm = document.getElementById('version_upload_form_fields');
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            uploadForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("version_added").value;
	    
	            //if the new version not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.file.properties.versionedFileUploadDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the upload form and get the pictures
	                YAHOO.ur.file.properties.versionedFileUploadDialog.hide();
	                var fileId = document.getElementById("personal_file_id").value;
	                YAHOO.ur.file.properties.getFileProperties(fileId);
	                YAHOO.ur.file.properties.clearVersionedFileUploadForm();
	            }
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o)
	    {
	        alert('New version file upload submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new news item button is clicked.
	    YAHOO.ur.file.properties.versionedFileUploadDialog = new YAHOO.widget.Dialog('versionedFileUploadDialog', 
        { width : "850px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					  { text:'Cancel', handler:handleCancel } ]
		} );
	
	    // center and show the dialog
        YAHOO.ur.file.properties.versionedFileUploadDialog.showDialog = function()
        {
            YAHOO.ur.file.properties.versionedFileUploadDialog.center();
            YAHOO.ur.file.properties.versionedFileUploadDialog.show();
        }
    
 	    // Validate the entries in the form to require that a name is entered
	    YAHOO.ur.file.properties.versionedFileUploadDialog.validate = function() 
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
	    YAHOO.ur.file.properties.versionedFileUploadDialog.render();
    },
    
     /** 
      * clear out any form data messages or input
      * in the upload single file form
      */
    clearVersionedFileUploadForm : function()
    {
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
    
    /**
     * clear the file rename form
     */
    clearFileRenameForm : function()
    {
        // clear out the error message
        var renameError = document.getElementById('rename_error_div');
        renameError.innerHTML = "";   
        
        document.renameForm.newFileName.value = "";
	    document.renameForm.personalFileId.value = "";
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
	        YAHOO.ur.file.properties.clearFileRenameForm();
	        YAHOO.ur.file.properties.renameFileDialog.hide();
	    };
	
	    // handle a successful return
	    var handleSuccess = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	    
	            //get the response from renaming a file
	            var response = o.responseText;
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
                    YAHOO.ur.file.properties.renameFileDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the file was renamed
	                YAHOO.ur.file.properties.renameFileDialog.hide();
	                YAHOO.ur.file.properties.clearFileRenameForm();
	                var fileId = document.getElementById("personal_file_id").value;
	                YAHOO.ur.file.properties.getFileProperties(fileId);
	            }
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert("rename file dialog failed  : " + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // rename file button is clicked.
	    YAHOO.ur.file.properties.renameFileDialog = new YAHOO.widget.Dialog('renameFileDialog', 
            { width : "600px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
					      { text:"Cancel", handler:handleCancel } ]
		    } );

		YAHOO.ur.file.properties.renameFileDialog.submit = function()
	    {
	        YAHOO.util.Connect.setForm('renameForm');
	    
	        if( YAHOO.ur.file.properties.renameFileDialog.validate() )
	        {
                var cObj = YAHOO.util.Connect.asyncRequest('POST',
                fileRenameAction, callback);
            }
        }
            	
	    YAHOO.ur.file.properties.renameFileDialog.showDialog = function()
	    {
	        YAHOO.ur.file.properties.renameFileDialog.center();
	        YAHOO.ur.file.properties.renameFileDialog.show();
	    }
   
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.file.properties.renameFileDialog.validate = function() 
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
	    YAHOO.ur.file.properties.renameFileDialog.render();
        
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
				    YAHOO.ur.file.properties.renameFileDialog.showDialog(); 
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
     * Function to create the change owner dialog
     */
    createChangeOwnerDialog : function()
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
	            	if( o.responseText != null && o.responseText != "")
	            	{
	            	    var response = eval("("+o.responseText+")");
	            	    if( response.messageType == 'accessDenied')
	            	    {
	            		    alert('Access denied - Reason: ' + response.reason);
	            	    }
	            	}
	            	else
	            	{
	            		// assume success update the page
	            		var fileId = document.getElementById("personal_file_id").value;
		                YAHOO.ur.file.properties.getFileProperties(fileId);
	            	}
				}               
            },
	
	        failure: function(o) 
	        {
	            alert('Change owner file Failure ' + o.status + ' status text ' + o.statusText );
	        }
        }
        
	    // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
			this.submit();
			YAHOO.ur.file.properties.changeOwnerDialog.hide();
	    };
		
	    // handle a cancel dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.file.properties.changeOwnerDialog.hide();
	    };
	
	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // rename file button is clicked.
	    YAHOO.ur.file.properties.changeOwnerDialog = new YAHOO.widget.Dialog('changeOwnerDialog', 
            { width : "600px",
		      visible : false, 
		      buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
					      { text:"Cancel", handler:handleCancel } ]
		    } );

		YAHOO.ur.file.properties.changeOwnerDialog.submit = function()
	    {
	        YAHOO.util.Connect.setForm('changeOwnerForm');
	    
                var cObj = YAHOO.util.Connect.asyncRequest('POST',
                changeOwnerAction, callback);
        }
            	
	    YAHOO.ur.file.properties.changeOwnerDialog.showDialog = function()
	    {
	        YAHOO.ur.file.properties.changeOwnerDialog.center();
	        YAHOO.ur.file.properties.changeOwnerDialog.show();
	    }

	    // Render the Dialog
	    YAHOO.ur.file.properties.changeOwnerDialog.render();
        
    },
  
      
    /**
     * Initialize the page
     */
    init : function()
    {
        YAHOO.ur.file.properties.versionedFileUpload();
        YAHOO.ur.file.properties.createFileRenameDialog();
        YAHOO.ur.file.properties.createChangeOwnerDialog();
     }
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.file.properties.init);