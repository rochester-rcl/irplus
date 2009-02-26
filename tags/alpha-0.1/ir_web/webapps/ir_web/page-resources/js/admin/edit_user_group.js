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
YAHOO.namespace("ur.usergroup.edit");


YAHOO.ur.usergroup.edit = {

    
    /*
     * Execute the user search and update div to show found users
     */
    userSearch: function(rowStart, startPageNumber, currentPageNumber)
    {
        // action to perform for searching names
        var userSearchAction =  basePath + 'admin/userGroupUserSearch.action';
        
        /*  This call back updates the html when the names are retrieved   */
        var callback = 
        {
            success: function(o) 
            {  
            	 // check for the timeout - forward user to login page if timout
	             // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
                    var divToUpdate = document.getElementById('users_search_results_div');
                    divToUpdate.innerHTML = o.responseText; 
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Users search Failure ' + o.status + ' status text ' + o.statusText );
	        }
        }
        
        if( document.userSearchForm.query.value != null && document.userSearchForm.query.value != '')
        {
    
	        var formObject = document.getElementById('user_search_form');
	        YAHOO.util.Connect.setForm(formObject);
	
            var transaction = YAHOO.util.Connect.asyncRequest('POST', 
                userSearchAction + '?rowStart=' + rowStart +  '&startPageNumber=' + startPageNumber + '&currentPageNumber=' + currentPageNumber, 
                callback);
        }
        else
        {
            var divToUpdate = document.getElementById('users_search_results_div');
            divToUpdate.innerHTML = ""; 
        }
    },
    
    /* Adds a member to the group*/
    addMember: function(userId, groupId, rowStart, startPageNumber, currentPageNumber)
    {
            // action to perform for searching names
        var action =  basePath + 'admin/addUserToGroup.action';
        
        /*  This call back updates the html when the names are retrieved   */
        var callback = 
        {
            success: function(o) 
            {  
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
                    var divToUpdate = document.getElementById('group_members_div');
                    divToUpdate.innerHTML = o.responseText; 
                    YAHOO.ur.usergroup.edit.userSearch(rowStart, startPageNumber, currentPageNumber);
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Users search Failure ' + o.status + ' status text ' + o.statusText );
	        }
        }
    
         var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            action, 
            callback, "userId="+ userId + "&id=" + groupId);
    },
    
    /* Removes a member from the group*/
    removeMember: function(userId, groupId, rowStart, startPageNumber, currentPageNumber)
    {
            // action to perform for searching names
        var action =  basePath + 'admin/removeUserFromGroup.action';
        
        /*  This call back updates the html when the names are retrieved   */
        var callback = 
        {
            success: function(o) 
            {  
                // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
                    var divToUpdate = document.getElementById('group_members_div');
                    divToUpdate.innerHTML = o.responseText;
                    YAHOO.ur.usergroup.edit.userSearch(rowStart, startPageNumber, currentPageNumber); 
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Users search Failure ' + o.status + ' status text ' + o.statusText );
	        }
        }
    
         var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            action, 
            callback, "userId="+ userId + "&id=" + groupId);
    },
    
    /*
     * Execute the user search and update div to show found users
     */
    adminSearch: function(rowStart, startPageNumber, currentPageNumber)
    {
        // action to perform for searching names
        var adminSearchAction =  basePath + 'admin/userGroupAdminSearch.action';
        
        /*  This call back updates the html when the names are retrieved   */
        var callback = 
        {
            success: function(o) 
            {  
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
                    var divToUpdate = document.getElementById('admin_search_results_div');
                    divToUpdate.innerHTML = o.responseText;
                } 
            },
	
	        failure: function(o) 
	        {
	            alert('Admin search Failure ' + o.status + ' status text ' + o.statusText );
	        }
        }
        
        if( document.adminSearchForm.query.value != null && document.adminSearchForm.query.value != '')
        {
    
	        var formObject = document.getElementById('admin_search_form');
	        YAHOO.util.Connect.setForm(formObject);
	
            var transaction = YAHOO.util.Connect.asyncRequest('POST', 
                adminSearchAction + '?rowStart=' + rowStart +  '&startPageNumber=' + startPageNumber + '&currentPageNumber=' + currentPageNumber, 
                callback);
        }
        else
        {
            var divToUpdate = document.getElementById('admin_search_results_div');
            divToUpdate.innerHTML = ""; 
        }
    },
    
    /* Adds a member to the group*/
    addAdmin: function(userId, groupId)
    {
        // action to perform add admin to group
        var action =  basePath + 'admin/addAdminToGroup.action';
        
        /*  This call back updates the html when the names are retrieved   */
        var callback = 
        {
            success: function(o) 
            {  
                // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
                    var divToUpdate = document.getElementById('group_admins_div');
                    divToUpdate.innerHTML = o.responseText; 
                    rowStart = document.getElementById('admin_row_start').value; 
                    YAHOO.ur.usergroup.edit.adminSearch(rowStart);
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Users search Failure ' + o.status + ' status text ' + o.statusText );
	        }
        }
    
         var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            action, 
            callback, "userId="+ userId + "&id=" + groupId);
    },
    
    /* Removes a member from the group*/
    removeAdmin: function(userId, groupId)
    {
            // action to perform for searching names
        var action =  basePath + 'admin/removeAdminFromGroup.action';
        
        /*  This call back updates the html when the names are retrieved   */
        var callback = 
        {
            success: function(o) 
            {  
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
                    var divToUpdate = document.getElementById('group_admins_div');
                    divToUpdate.innerHTML = o.responseText;
                    rowStart = document.getElementById('admin_row_start').value; 
                    YAHOO.ur.usergroup.edit.adminSearch(rowStart);
                } 
            },
	
	        failure: function(o) 
	        {
	            alert('Admin search Failure ' + o.status + ' status text ' + o.statusText );
	        }
        }
    
         var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            action, 
            callback, "userId="+ userId + "&id=" + groupId);
    },
    
    /* init for when the page is loaded */
    init: function()
    {
        var myTabs = new YAHOO.widget.TabView("user-group-tabs");
    }
    
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.usergroup.edit.init);