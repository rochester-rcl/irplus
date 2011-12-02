/**
 * This code is for dealing with adding and removing folders 
 * in the group workspace.
 */
YAHOO.namespace("ur.user.group_workspace.permissions");



YAHOO.ur.user.group_workspace.permissions = 
{
	    /**
	     * Make sure the permissions are set correctly
	     */
	    checkFolderPermission : function(permission, permissions) 
	    {
	        
		    if (permission.id == 'GROUP_WORKSPACE_FOLDER_READ') 
		    {
			    if (!permission.checked) 
			    {
				    urUtil.setCheckboxes(permissions, false);
			    }
		    }
		
		    if (permission.id == 'GROUP_WORKSPACE_FOLDER_ADD_FILE') 
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

		    if (permission.id == 'GROUP_WORKSPACE_FOLDER_EDIT') 
		    {
			    if (permission.checked) 
			    {
				    permissions[0].checked=true;
				    permissions[1].checked=true;
				    document.getElementById('applyToChildren').checked=true
			    } 
		    }
		    return true;
	    },
	    
	    checkApplyToChildrenFolder : function()
	    {
	    	if( !document.getElementById('applyToChildren').checked )
	    	{
	    		document.getElementById('GROUP_WORKSPACE_FOLDER_EDIT').checked=false;
	    	}
	    		
	    },
	    
	    /**
	     * Make sure the permissions are set correctly
	     */
	    checkGroupPermission : function(permission, permissions) 
	    {
	        
		    if (permission.id == 'GROUP_WORKSPACE_READ') 
		    {
			    if (!permission.checked) 
			    {
				    urUtil.setCheckboxes(permissions, false);
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
			    }
		    }

		    if (permission.id == 'GROUP_WORKSPACE_EDIT') 
		    {
			    if (permission.checked) 
			    {
				    permissions[0].checked=true;
				    permissions[1].checked=true;
				    document.getElementById('applyToChildren').checked=true
			    } 
		    }
		    return true;
	    },
	    
	    checkApplyToChildrenGroup : function()
	    {
	    	if( !document.getElementById('applyToChildren').checked )
	    	{
	    		document.getElementById('GROUP_WORKSPACE_EDIT').checked=false;
	    	}
	    		
	    }
}