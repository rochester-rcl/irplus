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
YAHOO.namespace("ur.group_workspace_invite");

var removeGroupWorkspaceUserAction = basePath + 'user/removeGroupWorkspaceUser.action';

YAHOO.ur.group_workspace_invite = 
{
		/**
		 * Submit the form to invite users
		 */
		submitInviteForm : function ()
		{
	        if( YAHOO.ur.group_workspace_invite.validateEmails() )
	        {
	        	document.newInviteForm.submit();
	        }
		},

		/**
		 * Validate the emails before sending
		 */
		validateEmails : function ()
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
            return  true;	
		},
		
		/**
		 * Remove the user from the group.
		 */
		removeUser : function(userId)
		{
			 document.getElementById('removeUserFormUserId').value = userId;
			 YAHOO.ur.group_workspace_invite.removeUserConfirmDialog.showDialog();
		},
		
		
	    /**
	     * Make sure the permissions are set correctly
	     */
	    autoCheckPermission : function(permission, permissions) 
	    {
			owner = document.getElementById('owner');
		    if (permission.id == 'GROUP_WORKSPACE_READ') 
		    {
			    if (!permission.checked) 
			    {
				    urUtil.setCheckboxes(permissions, false);
				    owner.checked = false;
			    }
		    }
		
		    if (permission.id == 'GROUP_WORKSPACE_ADD_FILE') 
		    {
			    if (permission.checked) 
			    {
				    permissions[0].checked=true;
				    
			    } 
			    else 
			    {
				    permissions[2].checked=false;
				    owner.checked = false;
			    }
		    }

		    if (permission.id == 'GROUP_WORKSPACE_EDIT') 
		    {
			    if (permission.checked) 
			    {
				    permissions[0].checked=true;
				    permissions[1].checked=true;
			    } 
			    else
			    {
			    	owner.checked = false;
			    }
		    }
		    return true;
	    },
	    
	    /**
	     *  If setting the owner flag
	     *  set all other permissions
	     */
	    setOwner : function (owner, permissions)
	    {
	    	if( owner.checked )
	    	{
	    		permissions[0].checked=true;
			    permissions[1].checked=true;
			    permissions[2].checked=true;
	    	}
	    },
		
	    /**
	      * Dialog to confirm unsharing of the files
	      */
	    createRemoveUserConfirmDialog : function() 
	    {
	            // Define various event handlers for Dialog
	            var handleSubmit = function() 
	            {
	            	 document.removeUserForm.submit();
	            	 this.hide();
	            };

	            var handleCancel = function() 
	            {
		            this.hide();
	            };

	            // Instantiate the Dialog
	            YAHOO.ur.group_workspace_invite.removeUserConfirmDialog = 
	                 new YAHOO.widget.Dialog("removeUserConfirmDialog", 
									     { width: "500px",
										   visible: false,
										   modal: true,
										   buttons: [ { text:"Yes", handler:handleSubmit, isDefault:true },
													  { text:"No",  handler:handleCancel } ]
										} );
	            YAHOO.ur.group_workspace_invite.removeUserConfirmDialog.showDialog = function()
	            {
	            	YAHOO.ur.group_workspace_invite.removeUserConfirmDialog.center();
	            	YAHOO.ur.group_workspace_invite.removeUserConfirmDialog.show();
	            };
	     
	            YAHOO.ur.group_workspace_invite.removeUserConfirmDialog.setHeader("Remove user from Group?");
	            
	            // Render the Dialog
	            YAHOO.ur.group_workspace_invite.removeUserConfirmDialog.render();
	    },
	    
		/**
		 * Remove the user from the group.
		 */
		removeInvite : function(inviteId)
		{
			 document.getElementById('removeInviteFormInviteId').value = inviteId;
			 YAHOO.ur.group_workspace_invite.removeInviteConfirmDialog.showDialog();
		},
		
	    
	    /**
	      * Dialog to confirm unsharing of invite to group
	      */
	    createRemoveInviteConfirmDialog : function() 
	    {
	            // Define various event handlers for Dialog
	            var handleSubmit = function() 
	            {
	            	 document.removeInviteForm.submit();
	            	 this.hide();
	            };

	            var handleCancel = function() 
	            {
		            this.hide();
	            };

	            // Instantiate the Dialog
	            YAHOO.ur.group_workspace_invite.removeInviteConfirmDialog = 
	                 new YAHOO.widget.Dialog("removeInviteConfirmDialog", 
									     { width: "500px",
										   visible: false,
										   modal: true,
										   buttons: [ { text:"Yes", handler:handleSubmit, isDefault:true },
													  { text:"No",  handler:handleCancel } ]
										} );
	            YAHOO.ur.group_workspace_invite.removeInviteConfirmDialog.showDialog = function()
	            {
	            	YAHOO.ur.group_workspace_invite.removeInviteConfirmDialog.center();
	            	YAHOO.ur.group_workspace_invite.removeInviteConfirmDialog.show();
	            };
	     
	            YAHOO.ur.group_workspace_invite.removeInviteConfirmDialog.setHeader("Remove invite from Group?");
	            
	            // Render the Dialog
	            YAHOO.ur.group_workspace_invite.removeInviteConfirmDialog.render();
	    },
	        
	        
	    // initialize the page
		// this is called once the dom has
		// been created
		init : function() 
		{
		     YAHOO.ur.group_workspace_invite.createRemoveUserConfirmDialog();
		     YAHOO.ur.group_workspace_invite.createRemoveInviteConfirmDialog();
		}
}

//initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.group_workspace_invite.init);