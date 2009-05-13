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
 * This code is for dealing with adding invite user
 */
YAHOO.namespace("ur.invite");

// action to perform when submitting the invite form.
var getCollaboratorAction = basePath + 'user/getCollaborators.action';

// actions for adding and removing collaborators
var newInviteUserAction = basePath + 'user/addInviteUser.action';
var editPermissionsAction = basePath + 'user/updatePermissions.action';
var getPermissionsAction = basePath + 'user/getPermissions.action';
var deleteCollaboratorAction = basePath + 'user/deleteCollaborator.action';

var unShareFileAction = basePath + 'user/deleteCollaborator.action';
var unSharePendingInviteeAction = basePath + 'user/deletePendingInvitee.action';
var removeFileAction = basePath + '/user/removeSharedFile.action';

YAHOO.ur.invite = 
{

    //This call back updates the html when a new collaborator is added 
    getCollaboratorCallback :
    {
        success: function(o) 
        {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               
                var divToUpdate = document.getElementById('newCollaborators');
                divToUpdate.innerHTML = o.responseText; 
                document.newInviteForm.shareFileIds.value = document.getElementById('collaborators_share_file_ids').value;
            }
        },
	
	    failure: function(o) 
	    {
	        alert('Get collaborators Failure ' + o.status + ' status text ' + o.statusText );
	    }	
    },
    
    /**
     * Make sure the permissions are set correctly
     */
    autoCheckPermission : function(permission, permissions) 
    {
	    if (permission.id == 'VIEW') 
	    {
		    if (!permission.checked) 
		    {
			    urUtil.setCheckboxes(permissions, false);
		    }
	    }
	
	    if (permission.id == 'EDIT') 
	    {
		    if (permission.checked) 
		    {
			    permissions[0].checked=true;
		    } 
		    else 
		    {
			    permissions[2].checked=false;
		    }
	    }

	    if (permission.id == 'SHARE') 
	    {
		    if (permission.checked) 
		    {
			    permissions[0].checked=true;
			    permissions[1].checked=true;
		    } 
	    }
	    return true;
    },
    
    /**
     *  Function that retrieves collaborator information
     *  based on the given personal file id.
     *
     *  The personal file id used to get the collaborator.
     */
    getCollaboratorById : function(shareFileIds)
    {
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getCollaboratorAction +"?shareFileIds="+shareFileIds+'&bustcache='+new Date().getTime(),
            YAHOO.ur.invite.getCollaboratorCallback, null);
    },
    
    /** 
     * clear out any form data messages or input
     * in the folder form
     */
    clearInviteForm : function()
    {
        var inviteEmailError = document.getElementById('inviteForm_emailError');
        var div = document.getElementById('inviteUserError');
	    //clear out any error information
	    if( inviteEmailError != null )
	    {
	        if( inviteEmailError.innerHTML != null && inviteEmailError.innerHTML != "")
	        { 
	            div.removeChild(inviteEmailError);
	        }
	    }
	    document.newInviteForm.email.value = "";
	    document.newInviteForm.inviteMessage.value = "";
	    urUtil.setCheckboxes(document.newInviteForm.selectedPermissions, false);
    },
    
    /**
     * Create the dialog for inviting users
     */
    createNewInviteDialog : function()
    {
	    // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('newInviteForm');
	        if(validate())
	        {
	            // invite the user
                var action = newInviteUserAction;
                YAHOO.ur.invite.waitDialog.showDialog();
                var cObj = YAHOO.util.Connect.asyncRequest('post', action, callback);
            }
	    };

	    YAHOO.util.Event.addListener("inviteUser", "click", handleSubmit);  	

	    // handle a successful return
	    var handleSuccess = function(o) 
	    {
	    	 YAHOO.ur.invite.waitDialog.hide();
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	    
	            //get the response from adding a contributor type
	            var response = eval("("+o.responseText+")");
	    
	            //if the contributor type was not added then show the user the error message.
	            // received from the server
	            if( response.inviteSent == "false" )
	            {
	                var inviteError = document.getElementById('inviteUserError');
                    inviteError.innerHTML = '<p id="inviteForm_emailError">' + response.inviteErrorMessage + '</p>';
	            }
	            else
	            {
	                // we can clear the form if the contributor type was added
	                YAHOO.ur.invite.clearInviteForm();
	            }

    	        YAHOO.ur.invite.getCollaboratorById(document.newInviteForm.shareFileIds.value);
    	    }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.invite.waitDialog.hide();
	        alert('Invite user failed ' + o.status);
	    };
  
 	    // Validate the entries in the form to require that both first and last name are entered
	    var validate = function() 
	    {

	        if (document.getElementById('collaborators_share_file_ids').value == '')
	        {
	        	alert('No files selected for sharing. Please go back to workspace and select atleast one file to share!');
	        	return false; 
	        }
	        	    
		    var email = urUtil.trim(document.newInviteForm.email.value);
		    if (email == "") 
		    {
		        alert('Please enter a valid E-mail address.');
			    return false;
		    } 
		    else 
		    {
		    	var emails = email.split(";");
		    	for( i = 0; i < emails.length; i++)
		    	{
			        if (!urUtil.emailcheck(emails[i])) 
			        {
				        alert('Invalid E-mail address ' + emails[i]);
				        return false;
			        }
		    	}
		    }
		
		    if (!urUtil.checkForNoSelections(document.newInviteForm.selectedPermissions))
		    {
			     alert('Please select at least one permission.');
	  		     return false;
	        } 
	        	        
		    return  true;	
	    };
		
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
    },
    
    /**
     * Dialog to edit permissions
     */
    createEditPermissionsDialog : function()
    {
	    // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('editPermissionsForm');

	        if(YAHOO.ur.invite.editPermissionsDialog.validate())
	        {
                var cObj = YAHOO.util.Connect.asyncRequest('post', editPermissionsAction, 
                    callback);
            }
	    };
	
		
	    // handle a cancel of the editing permissions dialog
	    var handleCancel = function() 
	    {
	         YAHOO.ur.invite.editPermissionsDialog.hide();
	    };

	    var handleSuccess = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	    
	        
	            //get the response from updating permissions
	            var response = o.responseText;
	            var permissionForm = document.getElementById('editPermissionsDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            permissionForm.innerHTML = o.responseText;

	            // determine if the edit was a success
	            var success = document.getElementById("editPermissionseForm_success").value;
	    
	            //if the update was not successfull then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.invite.editPermissionsDialog.showDialog();
	            }
	            else
	            {
		            YAHOO.ur.invite.clearEditPermissionsForm();
	                YAHOO.ur.invite.editPermissionsDialog.hide();
	            }
	        }
	    };

	    var handleFailure = function(o) 
	    {
	        alert('Permissions submission failed ' + o.status);
	    };


	    var myButtons = [ { text:"Submit", handler:handleSubmit, isDefault:true },
					      { text:"Cancel", handler:handleCancel } ];


    
	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new content type button is clicked.
	    YAHOO.ur.invite.editPermissionsDialog = new YAHOO.widget.Dialog('editPermissionsDialog', 
	    { width : "500px",
	      visible : false, 
	      modal : true
	    } );

	    YAHOO.ur.invite.editPermissionsDialog.cfg.queueProperty("buttons", myButtons);
	    
	    // center and show the dialog
	    YAHOO.ur.invite.editPermissionsDialog.showDialog = function()
	    {
	        YAHOO.ur.invite.editPermissionsDialog.center();
	        YAHOO.ur.invite.editPermissionsDialog.show();
	    };
	
	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.invite.editPermissionsDialog.validate = function() 
	    {
		    if (!urUtil.checkForNoSelections(document.editPermissionsForm.selectedPermissions)) 
		    {
			     alert('Please select at least one permission.');
	  		     return false;
	        } 
	    
	        return true;
	    };
	

	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
    },
    
    /**
     * Clear the edit permissions form
     */
    clearEditPermissionsForm : function()
    {
	    YAHOO.ur.invite.clearInviteForm();
	    urUtil.setCheckboxes(document.newInviteForm.selectedPermissions, false);
    },
    
    /**
     * Edit the permissions for the collaborator
     */
    editPermissions : function(fileCollaboratorId)
    {
        // This call back updates the html when a new collaborator is added 
        var callback =
        {
            success: function(o) 
            {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {                   
                    var divToUpdate = document.getElementById('editPermissionsDialogFields');
                    divToUpdate.innerHTML = o.responseText;
		            YAHOO.ur.invite.editPermissionsDialog.render();
                    YAHOO.ur.invite.editPermissionsDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get Permission Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
    
	    var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            getPermissionsAction +'?fileCollaboratorId=' + fileCollaboratorId , 
            callback, null);
    },
    
    /**
     * Dialog to confirm unsharing of the files
     */
    createUnshareFileConfirmDialog : function() 
    {
        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('unshareForm');
            this.hide();
            document.unshareForm.submit();
	    };

	    var handleCancel = function() 
	    {
		    this.hide();
	    };

	    // Instantiate the Dialog
	    YAHOO.ur.invite.unshareFileDialog = 
	        new YAHOO.widget.Dialog("unshareFileConfirmDialog", 
									     { width: "500px",
										   visible: false,
										   modal: true,
										   buttons: [ { text:"Yes", handler:handleSubmit, isDefault:true },
													  { text:"No",  handler:handleCancel } ]
										} );
	
	    YAHOO.ur.invite.unshareFileDialog.showDialog = function()
	    {
	        YAHOO.ur.invite.unshareFileDialog.center();
	        YAHOO.ur.invite.unshareFileDialog.show();
	    };
	    
	    YAHOO.ur.invite.unshareFileDialog.setHeader("UnShare the file?");
	
	    // Render the Dialog
	    YAHOO.ur.invite.unshareFileDialog.render();
    },
    
    /**
     * function to unshare files
     */
    unshareFile : function(fileCollaboratorId, personalFileId, shareFileIds) 
    {
		document.getElementById('unshareForm_fileCollaboratorId').value = fileCollaboratorId;
		document.getElementById('unshareForm_personalFileId').value = personalFileId;
		document.getElementById('unshareForm_share_file_ids').value = shareFileIds;
		YAHOO.ur.invite.unshareFileDialog.showDialog();
	},
	
	/**
	 * create a dialog to confirm the unsharing of a file that is pending.
	 */
	createUnsharePendingInviteeConfirmDialog : function() 
	{

        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('unsharePendingInviteeForm');
            this.hide();
            var cObj = YAHOO.util.Connect.asyncRequest('post', unSharePendingInviteeAction,
             YAHOO.ur.invite.getCollaboratorCallback);
	    };

	    var handleCancel = function() {
		    this.hide();
	    };

	    // Instantiate the Dialog
	    YAHOO.ur.invite.unsharePendingInviteeDialog = 
	        new YAHOO.widget.Dialog("unsharePendingInviteeConfirmDialog", 
									     { width: "500px",
										   visible: false,
										   modal: true,
										   buttons: [ { text:"Yes", handler:handleSubmit, isDefault:true },
													  { text:"No",  handler:handleCancel } ]
										} );
	
	    YAHOO.ur.invite.unsharePendingInviteeDialog.setHeader("Unshare file?");
	    
	    YAHOO.ur.invite.unsharePendingInviteeDialog.showDialog = function()
	    {
	        YAHOO.ur.invite.unsharePendingInviteeDialog.center();
	        YAHOO.ur.invite.unsharePendingInviteeDialog.show();
	    }
	
	    // Render the Dialog
	    YAHOO.ur.invite.unsharePendingInviteeDialog.render();
    },
    
    /**
     * Set the datat to unshare and show
     */
    unsharePendingInvitee : function(inviteInfoId, personalFileId, shareFileIds)
	{
		document.getElementById('unsharePendingInviteeForm_inviteInfoId').value = inviteInfoId;
		document.getElementById('unsharePendingInviteeForm_personalFileId').value = personalFileId;
		document.getElementById('unsharePendingInviteeForm_share_file_ids').value = shareFileIds;
		YAHOO.ur.invite.unsharePendingInviteeDialog.showDialog();
	},
	
	/**
	 * create a dialog to confirm the remove a file from the share view.
	 */
	createRemoveFileConfirmDialog : function() 
	{

        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('removeFileForm');
            this.hide();
            var cObj = YAHOO.util.Connect.asyncRequest('post', removeFileAction, 
                YAHOO.ur.invite.getCollaboratorCallback);
	    };

	    var handleCancel = function() 
	    {
		    this.hide();
	    };

	    // Instantiate the Dialog
	    YAHOO.ur.invite.removeFileConfirmDialog = 
	        new YAHOO.widget.Dialog("removeFileConfirmDialog", 
									     { width: "500px",
										   visible: false,
										   modal: true,
										   buttons: [ { text:"Yes", handler:handleSubmit, isDefault:true },
													  { text:"No",  handler:handleCancel } ]
										} );
	
	    YAHOO.ur.invite.removeFileConfirmDialog.setHeader("Remove file?");
	    
	    YAHOO.ur.invite.removeFileConfirmDialog.showDialog = function()
	    {
	        YAHOO.ur.invite.removeFileConfirmDialog.center();
	        YAHOO.ur.invite.removeFileConfirmDialog.show();
	    };
	
	    // Render the Dialog
	    YAHOO.ur.invite.removeFileConfirmDialog.render();
    },
    
    /**
     * Confirm dialog for remove the file
     */
	removeFile : function(personalFileId, shareFileIds) {
		document.getElementById('remove_file_form_personal_file_id').value = personalFileId;
		document.getElementById('removeFileForm_share_file_ids').value = shareFileIds;
		YAHOO.ur.invite.removeFileConfirmDialog.showDialog();
	},
	
    /**
     * Dialog to handle waiting display
     */
    createWaitDialog : function()
    {
         var handleClose = function()
         {
        	 YAHOO.ur.invite.waitDialog.close();
         };
          
	     // Instantiate the Dialog
         YAHOO.ur.invite.waitDialog = 
	         new YAHOO.widget.Dialog("wait_dialog_box", 
									     { width: "600px",
										   visible: false,
										   modal: true,
										   close: false
										  } );
										
         YAHOO.ur.invite.waitDialog.showDialog = function()
		 {
        	 YAHOO.ur.invite.waitDialog.center();
        	 YAHOO.ur.invite.waitDialog.show();
		 };
		 
		 YAHOO.ur.invite.waitDialog.render();
    },
	
    /**
     * initialize the page
     * this is called once the dom has
     * been created
     */
    init : function()
    {
        var shareFileIds = document.getElementById('share_file_ids').value;
        YAHOO.ur.invite.getCollaboratorById(shareFileIds);
        YAHOO.ur.invite.createNewInviteDialog();
        YAHOO.ur.invite.createEditPermissionsDialog();
	    YAHOO.ur.invite.createUnshareFileConfirmDialog();
	    YAHOO.ur.invite.createUnsharePendingInviteeConfirmDialog();
	    YAHOO.ur.invite.createRemoveFileConfirmDialog();
	    YAHOO.ur.invite. createWaitDialog();
    }
    
};

YAHOO.util.Event.onDOMReady(YAHOO.ur.invite.init);
