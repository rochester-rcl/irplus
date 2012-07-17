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
 * This code is for dealing with adding invite user
 */
YAHOO.namespace("ur.auto_share");

var getPermissionsAction = basePath + 'user/getFolderAutoSharePermissions.action';
var editPermissionsAction = basePath + 'user/updateFolderAutoSharePermissions.action';

YAHOO.ur.auto_share = 
{
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
            YAHOO.ur.auto_share.getCollaboratorCallback, null);
    },
    
    /**
     * function to remove autosharing on folder
     */
    unshareFolder : function(autoshareId, action) 
    {
		document.getElementById('unshareFormAutoShareInfoId').value = autoshareId;
		document.unshareForm.action = action;
		YAHOO.ur.auto_share.unshareFolderDialog.showDialog();
	},
    
    /**
     * Dialog to confirm unsharing of the files
     */
    createUnshareFolderConfirmDialog : function() 
    {
        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	    	document.unshareForm.submit();
            this.hide();
           
	    };

	    var handleCancel = function() 
	    {
	    	document.unshareForm.action = "";
		    this.hide();
	    };

	    // Instantiate the Dialog
	    YAHOO.ur.auto_share.unshareFolderDialog = 
	        new YAHOO.widget.Dialog("unshareFolderConfirmDialog", 
									     { width: "500px",
										   visible: false,
										   modal: true,
										   buttons: [ { text:"Yes", handler:handleSubmit, isDefault:true },
													  { text:"No",  handler:handleCancel } ]
										} );
	
	    YAHOO.ur.auto_share.unshareFolderDialog.showDialog = function()
	    {
	        YAHOO.ur.auto_share.unshareFolderDialog.center();
	        YAHOO.ur.auto_share.unshareFolderDialog.show();
	    };
	    
	    YAHOO.ur.auto_share.unshareFolderDialog.setHeader("Remove auto share from the folder?");
	
	    // Render the Dialog
	    YAHOO.ur.auto_share.unshareFolderDialog.render();
    },
    
 
    /**
     * Dialog to confirm unsharing of the files
     */
    createEditPermissionsDialog : function() 
    {
	    var handleCancel = function() 
	    {
		    this.hide();
	    };
	    
	    var handleFailure = function(o) 
	    {
	        alert('Permissions submission failed ' + o.status);
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
	            	YAHOO.ur.auto_share.editPermissionsDialog.showDialog();
	            }
	            else
	            {
	            	YAHOO.ur.auto_share.editPermissionsDialog.hide();
	            }
	        }
	    };
	    
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
	    
        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	    	YAHOO.util.Connect.setForm('editPermissionsForm');

	        if(YAHOO.ur.auto_share.editPermissionsDialog.validate())
	        {
                var cObj = YAHOO.util.Connect.asyncRequest('post', editPermissionsAction, 
                    callback);
            }
           
	    };



	    // Instantiate the Dialog
	    YAHOO.ur.auto_share.editPermissionsDialog = 
	        new YAHOO.widget.Dialog("editFolderPermissionsDialog", 
									     { width: "500px",
										   visible: false,
										   modal: true,
										   buttons: [ { text:"Yes", handler:handleSubmit, isDefault:true },
													  { text:"No",  handler:handleCancel } ]
										} );
	
	    YAHOO.ur.auto_share.editPermissionsDialog.showDialog = function()
	    {
	        YAHOO.ur.auto_share.editPermissionsDialog.center();
	        YAHOO.ur.auto_share.editPermissionsDialog.show();
	    };
	    
	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.auto_share.editPermissionsDialog.validate = function() 
	    {
		    if (!urUtil.checkForNoSelections(document.editPermissionsForm.selectedPermissions)) 
		    {
			     alert('Please select at least one permission.');
	  		     return false;
	        } 
	    
	        return true;
	    };
	    
	    YAHOO.ur.auto_share.editPermissionsDialog.setHeader("Edit Permissions");
	    


    },
    
 
    
    /**
     * Edit the permissions for the collaborator
     */
    editPermissions : function(autoShareId)
    {
        // This call back updates the html when a new collaborator is added 
        var callback =
        {
            success: function(o) 
            {
			    // check for the timeout - forward user to login page if timeout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {                   
                    var divToUpdate = document.getElementById('editPermissionsDialogFields');
                    divToUpdate.innerHTML = o.responseText;
                    YAHOO.ur.auto_share.editPermissionsDialog.render();
                    YAHOO.ur.auto_share.editPermissionsDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get Auto Share Permission Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
    
	    var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            getPermissionsAction +'?folderAutoShareInfoId=' + autoShareId , 
            callback, null);
    },
    
    validateAutoShareForm : function()
    {
	    var email = urUtil.trim(document.newInviteForm.emails.value);
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
	    		checkEmail = urUtil.trim(emails[i])
	    		// last one - this needs to be checked
	    		// for ending semicolon
	    		if( emails.length > 0 && i == (emails.length -1) )
	    		{
	    			if ( checkEmail == "" || checkEmail == null)
	    			{
	    				// ok
	    			}
	    		}
	    		else
	    		{
		            if (!urUtil.emailcheck(checkEmail)) 
		            {
			            alert('Invalid E-mail address ' + emails[i]);
			            return false;
		            }
	    		}
	    	}
	    }
	
	    if (!urUtil.checkForNoSelections(document.newInviteForm.selectedPermissions))
	    {
		     alert('Please select at least one permission.');
  		     return false;
        } 
        	        
	    return  true;
    },
    
    /**
     * initialize the page
     * this is called once the dom has
     * been created
     */
    init : function()
    {
 	    YAHOO.ur.auto_share.createUnshareFolderConfirmDialog();
 	    YAHOO.ur.auto_share.createEditPermissionsDialog();
    }
    
};

YAHOO.util.Event.onDOMReady(YAHOO.ur.auto_share.init);