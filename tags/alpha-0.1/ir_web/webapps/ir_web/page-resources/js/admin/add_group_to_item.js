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
 * This code is for dealing with editing intitutional items
 */
YAHOO.namespace("ur.group.item");

YAHOO.ur.group.item = {

    /**
     * Creates a YUI new item modal dialog for when a user wants to  
     * add permissions to a group for a item
     *
     */
    createAddGroupPermissionsDialog : function()
    {
	    // Define various event handlers for Dialog
	    var submit = function() 
	    {
            var formObject = document.getElementById('permissions_for_item_form');
	        YAHOO.util.Connect.setForm(formObject);
	
            var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            document.permissionsItemForm.action, 
            callback);
         
	    };
	
	    // handle a cancel of the adding the group
	    var cancel = function() 
	    {
	        YAHOO.ur.group.item.editGroupPermissionsDialog.hide();
	        //set permission checkbox's to off
            var permissionIds = document.getElementsByName('permissionIds');
            urUtil.setCheckboxes(permissionIds, false);
	    };
	
	    var success = function(o) 
	    {
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var divToUpdate = document.getElementById('all_user_groups');
                divToUpdate.innerHTML = o.responseText; 
                YAHOO.ur.group.item.editGroupPermissionsDialog.hide();
            
                //set permission checkbox's to off
                var permissionIds = document.getElementsByName('permissionIds');
                urUtil.setCheckboxes(permissionIds, false);
            }
	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new item item button is clicked.
	    YAHOO.ur.group.item.editGroupPermissionsDialog = 
	       new YAHOO.widget.Dialog('edit_group_permissions', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Save', handler:submit, isDefault:true },
					      { text:'Cancel', handler:cancel } ]
		    } );
	

 	    // Validate the entries in the form to require that a name is entered
	    YAHOO.ur.group.item.editGroupPermissionsDialog.validate = function() 
	    {
	       alert('validate');
	    };

	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
			
			
	    // Render the Dialog
	    YAHOO.ur.group.item.editGroupPermissionsDialog.render();

    },
    
    /**
     * Show the group permissions dialog box
     */
    showGroupPermissionsDialog : function(groupId)
    {
        document.getElementById('group_permissions_group_id').value = groupId;
        YAHOO.ur.group.item.editGroupPermissionsDialog.center();
        YAHOO.ur.group.item.editGroupPermissionsDialog.show();
    },
    
    /**
     * Get the permissions for the specified group
     */
    editPermissionsForGroup : function(groupId, itemId)
    {
        // action to perform for searching names
        var action =  basePath + 'admin/getGroupPermissionsForItem.action';
	    
	    var success = function(o) 
	    {
	    
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var divToUpdate = document.getElementById('permissions_for_group');
                divToUpdate.innerHTML = o.responseText; 
                YAHOO.ur.group.item.showGroupPermissionsDialog(groupId);
            }
	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure ' + o.status);
	    };
	    
        var transaction = YAHOO.util.Connect.asyncRequest('POST', 
        action, 
        {success:success, failure:failure}, 
        "groupId=" + groupId + "&itemId=" + itemId);
    },
    
    /**
     * Get the permissions for the specified group
     */
    addGroupsToItemFile : function(groupId, itemFileId, itemId)
    {
        // action to perform for searching names
        var action =  basePath + 'admin/addGroupToItemFile.action';
	    
	    var success = function(o) 
	    {
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var divToUpdate = document.getElementById('all_user_groups');
                divToUpdate.innerHTML = o.responseText; 
            }
	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure ' + o.status);
	    };
	    
        var transaction = YAHOO.util.Connect.asyncRequest('POST', 
        action + "?groupId=" + groupId + "&itemFileId=" + itemFileId + "&itemId=" + itemId, 
        {success:success, failure:failure} 
        );
    },
    
    /** 
     * initialize the page
     * this is called once the dom has
     * been created 
     */
    init : function() 
    {
        YAHOO.ur.group.item.createAddGroupPermissionsDialog();
    }

};


// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.group.item.init);