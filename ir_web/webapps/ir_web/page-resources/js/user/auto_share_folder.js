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
     * initialize the page
     * this is called once the dom has
     * been created
     */
    init : function()
    {
 	    YAHOO.ur.auto_share.createUnshareFolderConfirmDialog();
    }
    
};

YAHOO.util.Event.onDOMReady(YAHOO.ur.auto_share.init);