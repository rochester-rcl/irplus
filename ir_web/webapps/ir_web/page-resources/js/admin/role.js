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
YAHOO.namespace("ur.role");

// action to perform when submitting the personal content types.
var myRoleAction = basePath + 'admin/getRoles.action';

// actions for adding and removing folders
var updateRoleAction = basePath + 'admin/updateRole.action';
var newRoleAction = basePath + 'admin/createRole.action';
var deleteRoleAction = basePath + 'admin/deleteRole.action';

// object to hold the specified content type data.
var myRoleTable = new YAHOO.ur.table.Table('myRoles', 'newRoles');


/**
 * role namespace
 */
YAHOO.ur.role = {

	/**
	 *  Function that retrieves content information
	 *  based on the given content id.
	 *
	 *  The content id used to get the folder.
	 */
	 getRoles : function(rowStart, startPageNumber, currentPageNumber, order)
	 {
		var callback =
		{
		    success: function(o) 
		    {
		        var divToUpdate = document.getElementById('newRoles');
		        divToUpdate.innerHTML = o.responseText; 
		    },
			
			failure: function(o) 
			{
			    alert('Get role Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        myRoleAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(),  
	        callback, null);		
	},

	 /** 
	  * clear out any form data messages or input
	  * in the new role form
	  */
	clearRoleForm : function()
	{

        // clear out any errors
        var div = document.getElementById('roleError');
        div.innerHTML = "";
		
		document.getElementById('newRoleForm_name').value = "";
		document.getElementById('newRoleForm_description').value = "";
		document.getElementById('newRoleForm_id').value = "";
		document.newRoleForm.newRole.value = "true";
	},

    /**
     * Set all role id's form
     */
    setCheckboxes : function()
    {
        checked = document.myRoles.checkAllSetting.checked;
        var roleIds = document.getElementsByName('roleIds');
        urUtil.setCheckboxes(roleIds, checked);
        
    },	
	
	/**
	 * Creates a YUI new role modal dialog for when a user wants to create 
	 * a new role
	 *
	 */
	createNewRoleDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() {
		   this.submit();
		};
			
		// handle a cancel of the adding role dialog
		var handleCancel = function() {
			YAHOO.ur.role.newRoleDialog.hide();
		    YAHOO.ur.role.clearRoleForm();
		};
		
		var handleSuccess = function(o) {

		    //get the response from adding a role
		    var response = eval("("+o.responseText+")");
		    
		    //if the role was not added then show the user the error message.
		    // received from the server
		    if( response.roleAdded == "false" )
		    {
		        var roleNameError = document.getElementById('roleError');
	            roleNameError.innerHTML = '<p id="newRoleForm_nameError">' + response.message + '</p>';
	            YAHOO.ur.role.newRoleDialog.showDialog();
		    }
		    else
		    {
		   
		        // we can clear the form if the role was added
		        YAHOO.ur.role.newRoleDialog.hide();
		        YAHOO.ur.role.clearRoleForm();
		    }
		    myRoleTable.submitForm(myRoleAction);
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('role submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new role button is clicked.
		YAHOO.ur.role.newRoleDialog = new YAHOO.widget.Dialog('newRoleDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
	   	// show and center the sponsor dialog
        YAHOO.ur.role.newRoleDialog.showDialog = function()
        {
            YAHOO.ur.role.newRoleDialog.center();
            YAHOO.ur.role.newRoleDialog.show();
        },		
		
		// submit form	
		YAHOO.ur.role.newRoleDialog.submit = function() {
			YAHOO.util.Connect.setForm('newRoleForm');
		    	    
		    if( YAHOO.ur.role.newRoleDialog.validate() )
		    {
		        //based on what we need to do (update or create a 
		        // new role) based on the action.
	            var action = newRoleAction;
		        if( document.newRoleForm.newRole.value != 'true')
		        {
		           action = updateRoleAction;
		        }
	
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            action, callback);
	        }
	    }
			
	   
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.role.newRoleDialog.validate = function() {
		    var name = document.getElementById('newRoleForm_name').value;
			if (name == "" || name == null) {
			    alert('A role name must be entered');
				return false;
			} else {
				return true;
			}
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.role.newRoleDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showRole", "click", 
		    YAHOO.ur.role.newRoleDialog.showDialog, 
		    YAHOO.ur.role.newRoleDialog, true);
	},
    
    // function to edit role information
    editRole  : function(id, name, description)
    {
    	document.getElementById('newRoleForm_name').value = name;
	    document.getElementById('newRoleForm_description').value = description;
	    document.getElementById('newRoleForm_id').value = id;
	    document.newRoleForm.newRole.value = "false";
	    YAHOO.ur.role.newRoleDialog.showDialog();
    },
	
	 /** 
	  * clear out any form data messages or input
	  * in the new role form
	  */
	clearDeleteRoleForm : function()
	{
	    var roleError = document.getElementById('newRoleForm_nameError');
	    var div = document.getElementById('roleError');
		//clear out any error information
		if( roleError != null )
		{
		    if( roleError.innerHTML != null 
		        || roleError.innerHTML != "")
		    { 
		        div.removeChild(roleError);
		    }
		}
	},
	
	/**
	 * Creates a YUI new role modal dialog for when a user wants to create 
	 * a new role
	 *
	 */
	createDeleteRoleDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() {

		    YAHOO.util.Connect.setForm('myRoles');
		    
		    //delete the role
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deleteRoleAction, callback);
		};
		
			
		// handle a cancel of deleting role dialog
		var handleCancel = function() {
		    YAHOO.ur.role.deleteRoleDialog.hide();
		};
		
		var handleSuccess = function(o) {
		    //get the response from adding a role
		    var response = eval("("+o.responseText+")");

		    //if the role was not deleted then show the user the error message.
		    // received from the server
		    if( response.roleDeleted == "false" )
		    {
		        var deleteRoleError = document.getElementById('form_deleteRoleError');
	            deleteRoleError.innerHTML = '<p id="newDeleteRoleError">' 
	            + response.message + '</p>';
	            YAHOO.ur.role.deleteRoleDialog.showDialog();
		    }
		    else
		    {
		        // we can clear the form if the roles were deleted
		        YAHOO.ur.role.deleteRoleDialog.hide();
		        YAHOO.ur.role.clearDeleteRoleForm();

		    }
		    // reload the table
		    myRoleTable.submitForm(myRoleAction);
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('role submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new role button is clicked.
		YAHOO.ur.role.deleteRoleDialog = new YAHOO.widget.Dialog('deleteRoleDialog', 
	        { width : "400px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
			
	   	// show and center the sponsor dialog
        YAHOO.ur.role.deleteRoleDialog.showDialog = function()
        {
            YAHOO.ur.role.deleteRoleDialog.center();
            YAHOO.ur.role.deleteRoleDialog.show();
        }	
        	   
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,   failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.role.deleteRoleDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showDeleteRole", "click", 
		    YAHOO.ur.role.deleteRoleDialog.showDialog, 
		    YAHOO.ur.role.deleteRoleDialog, true);
	},

	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	    YAHOO.ur.role.getRoles(0,1,1,'asc');
	    YAHOO.ur.role.createNewRoleDialog();
	    YAHOO.ur.role.createDeleteRoleDialog();
	}	
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.role.init);