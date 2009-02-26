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
 * This code is for dealing with editing intitutional collections
 */
YAHOO.namespace("ur.group.collection");

YAHOO.ur.group.collection = {

    /**
     * Creates a YUI new collection modal dialog for when a user wants to  
     * add permissions to a group for a collection
     *
     */
    createAddGroupPermissionsDialog : function()
    {
	    // Define various event handlers for Dialog
	    var submit = function() 
	    {
            var formObject = document.getElementById('permissions_for_collection_form');
	        YAHOO.util.Connect.setForm(formObject);
	
            var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            document.permissionsCollectionForm.action, 
            callback);
         
	    };
	
	    // handle a cancel of the adding the group
	    var cancel = function() 
	    {
	        YAHOO.ur.group.collection.editGroupPermissionsDialog.hide();
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
	            var divToUpdate = document.getElementById('current_collection_groups_div');
                divToUpdate.innerHTML = o.responseText; 
                YAHOO.ur.group.collection.editGroupPermissionsDialog.hide();
            
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
	    // new collection item button is clicked.
	    YAHOO.ur.group.collection.editGroupPermissionsDialog = 
	       new YAHOO.widget.Dialog('edit_group_permissions', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Save', handler:submit, isDefault:true },
					      { text:'Cancel', handler:cancel } ]
		    } );
	

 	    // Validate the entries in the form to require that a name is entered
	    YAHOO.ur.group.collection.editGroupPermissionsDialog.validate = function() 
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
	    YAHOO.ur.group.collection.editGroupPermissionsDialog.render();

    },
    
    /**
     * Show the group permissions dialog box
     */
    showGroupPermissionsDialog : function(groupId)
    {
        document.getElementById('group_permissions_group_id').value = groupId;
        YAHOO.ur.group.collection.editGroupPermissionsDialog.center();
        YAHOO.ur.group.collection.editGroupPermissionsDialog.show();
    },
    
    /**
     * Get the permissions for the specified group
     */
    editPermissionsForGroup : function(groupId, collectionId)
    {
        // action to perform for searching names
        var action =  basePath + 'admin/getGroupPermissionsForCollection.action';
            
	    var success = function(o) 
	    {
	    
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var divToUpdate = document.getElementById('permissions_for_group');
                divToUpdate.innerHTML = o.responseText; 
                YAHOO.ur.group.collection.showGroupPermissionsDialog(groupId);
            }
	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure ' + o.status);
	    };
	    
        var transaction = YAHOO.util.Connect.asyncRequest('POST', 
        action, 
        {success:success, failure:failure}, 
        "groupId=" + groupId + "&collectionId=" + collectionId);
    },
    
    /**
     * Make sure the permissions are set correctly
     */
    autoCheckPermission : function(permission, permissions) 
    {
	    if (permission.id == 'ADMINISTRATION') 
	    {
		    if (permission.checked) 
		    {
		         permissions[1].checked=true;
		         permissions[2].checked=true;
		         permissions[3].checked=false;
		         permissions[4].checked=true;
		    }
	    }
	
	    if (permission.id == 'REVIEWER') 
	    {
		    if (permission.checked) 
		    {
			    permissions[2].checked=true;
			    permissions[3].checked=false;
		        permissions[4].checked=true;
		    } 
		    else 
		    {
			    permissions[0].checked=false; 
		    }
	    }

	    if (permission.id == 'DIRECT_SUBMIT') 
	    {
		    if (permission.checked) 
		    {
			    permissions[3].checked=false;
			    permissions[4].checked=true;
		    } 
		    else
		    {
		        permissions[0].checked=false; 
		        permissions[1].checked=false;
		    }
	    }
	    
	    if (permission.id == 'REVIEW_SUBMIT') 
	    {
		    if (permission.checked) 
		    {
			    permissions[4].checked=true;
			    permissions[0].checked=false; 
		        permissions[1].checked=false;
		        permissions[2].checked=false;
		    } 
		    else
		    {
			    permissions[4].checked=false;
		    }
	    }
	    
	    if (permission.id == 'VIEW') 
	    {
		    if (!permission.checked) 
		    {
			    urUtil.setCheckboxes(permissions, false);
		    } 
		    
	    }
	    
	    return true;
    },
    
    /** 
     * initialize the page
     * this is called once the dom has
     * been created 
     */
    init : function() 
    {
        YAHOO.ur.group.collection.createAddGroupPermissionsDialog();
    }

};


// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.group.collection.init);