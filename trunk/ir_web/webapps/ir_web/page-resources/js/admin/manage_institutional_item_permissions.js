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
 * This code is for dealing with editing intitutional item
 */
YAHOO.namespace("ur.institution.item.permission");

var myTabs;

YAHOO.ur.institution.item.permission = {

    /**
     * Update view permission for file
     */
    updateFilePublicView : function(itemFileId, itemId, isPublic)
    {
        // action to perform 
        var action =  basePath + 'admin/updateFilePublicView.action';
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
        	action + '?itemFileId=' + itemFileId + '&public=' + isPublic, null);
    },

    /**
     * Update view permission for file
     */
    updateItemPublicView : function(itemId, isPublic)
    {
        // action to perform 
        var action =  basePath + 'admin/updateItemPublicView.action';
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
        	action + '?itemId=' + itemId + '&public=' + isPublic, null);
    },

    /**
     * asks user to confirm removal of a group
     */
    removeGroupConfirmDialog : function()
    {
	    // Define various event handlers for Dialog
	    var submit = function() 
	    {
	        // action to perform for searching names
            var action =  basePath + 'admin/removeGroupFromItem.action';
            
            var formObject = document.getElementById('remove_group_from_item_form');
	        YAHOO.util.Connect.setForm(formObject);
            var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            action, 
            callback);
         
	    };
	
	    // handle a cancel of the adding item item dialog
	    var cancel = function() 
	    {
	        YAHOO.ur.institution.item.permission.removeGroupConfirmDialog.hide();
	    };
	
	    var success = function(o) 
	    {
	        var divToUpdate = document.getElementById('all_user_groups');
            divToUpdate.innerHTML = o.responseText; 
            YAHOO.ur.institution.item.permission.removeGroupConfirmDialog.hide();

	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new item item button is clicked.
	    YAHOO.ur.institution.item.permission.removeGroupConfirmDialog = 
	       new YAHOO.widget.Dialog('remove_group_confirm', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:submit, isDefault:true },
					      { text:'No', handler:cancel } ]
		    } );
	

 	    // Validate the entries in the form to require that a name is entered
	    YAHOO.ur.institution.item.permission.removeGroupConfirmDialog.validate = function() 
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
	    YAHOO.ur.institution.item.permission.removeGroupConfirmDialog.render();

    },
    
    /**
     * Show the group permissions dialog box
     */
    showRemoveGroupDialog : function(groupId)
    {
        document.getElementById('remove_group_id').value = groupId;
        YAHOO.ur.institution.item.permission.removeGroupConfirmDialog.center();
        YAHOO.ur.institution.item.permission.removeGroupConfirmDialog.show();
    },
    
    /**
     * asks user to confirm removal of a group for item file
     */
    removeItemFileGroupConfirmDialog : function()
    {
	    // Define various event handlers for Dialog
	    var submit = function() 
	    {
	        // action to perform for searching names
            var action =  basePath + 'admin/removeGroupFromItemFile.action';
            
            var formObject = document.getElementById('remove_group_from_item_file_form');
	        YAHOO.util.Connect.setForm(formObject);
	
            var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            action, 
            callback);
         
	    };
	
	    // handle a cancel of the adding item item dialog
	    var cancel = function() 
	    {
	        YAHOO.ur.institution.item.permission.removeItemFileGroupConfirmDialog.hide();
	    };
	
	    var success = function(o) 
	    {
	        var divToUpdate = document.getElementById('file_user_groups');
            divToUpdate.innerHTML = o.responseText; 
            YAHOO.ur.institution.item.permission.removeItemFileGroupConfirmDialog.hide();

	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new item item button is clicked.
	    YAHOO.ur.institution.item.permission.removeItemFileGroupConfirmDialog = 
	       new YAHOO.widget.Dialog('remove_file_group_confirm', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:submit, isDefault:true },
					      { text:'No', handler:cancel } ]
		    } );
	

 	    // Validate the entries in the form to require that a name is entered
	    YAHOO.ur.institution.item.permission.removeItemFileGroupConfirmDialog.validate = function() 
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
	    YAHOO.ur.institution.item.permission.removeItemFileGroupConfirmDialog.render();

    },
    
    /**
     * Show the group permissions dialog box
     */
    showRemoveItemFileGroupDialog : function(groupId, itemFileId, institutionalItemId)
    {
        document.getElementById('remove_file_group_id').value = groupId;
        document.getElementById('remove_item_file_id').value = itemFileId;
        document.getElementById('remove_institutional_item_id').value = institutionalItemId;
        YAHOO.ur.institution.item.permission.removeItemFileGroupConfirmDialog.center();
        YAHOO.ur.institution.item.permission.removeItemFileGroupConfirmDialog.show();
    },
    
    /** 
     * initialize the page
     * this is called once the dom has
     * been created 
     */
    init : function() 
    {
        YAHOO.ur.institution.item.permission.removeGroupConfirmDialog();
        
        YAHOO.ur.institution.item.permission.removeItemFileGroupConfirmDialog();

        //itemId = document.getElementById("itemId").value;
   
        myTabs = new YAHOO.widget.TabView("item-permission-tabs");
        if (document.getElementById("show_item_file_tab").value == "true") {
        
        	myTabs.set('activeIndex', 1);
        }
    }

};


// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.institution.item.permission.init);