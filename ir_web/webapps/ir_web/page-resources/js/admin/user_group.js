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
 * This code is for dealing with user groups
 */
YAHOO.namespace("ur.usergroup");

// action to perform when submitting the user groups
var getUserGroupsAction = basePath + 'admin/getUserGroups.action';

// actions for adding and updating user groups
var newUserGroupAction = basePath + 'admin/createUserGroup.action';
var deleteUserGroupAction = basePath + 'admin/deleteUserGroup.action';

// object to hold the specified content type data.
var myUserGroupsTable = new YAHOO.ur.table.Table('myUserGroups', 'newUserGroups');


YAHOO.ur.usergroup = 
{
    /**
     *  Function that retireves content information
     *  based on the given content id.
     *
     *  The content id used to get the folder.
     */
    getUserGroups : function(rowStart, startPageNumber, currentPageNumber, order)
    {
        /*
         * This call back updates the html when a new content type is
         * retrieved.
         */
        var callback =
        {
            success: function(o) 
            {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {                           
                    var divToUpdate = document.getElementById('newUserGroups');
                    divToUpdate.innerHTML = o.responseText;
                } 
            },
	
	        failure: function(o) 
	        {
	            alert('Get user groups Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };

        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getUserGroupsAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(),  
            callback, null);
    },
    
    /**
     * clear the user group form
     */
    clearUserGroupForm : function()
    {
        var div = document.getElementById('userGroupError');
        div.innerHTML = "";
        
        document.getElementById('newUserGroupForm_name').value = "";
	    document.getElementById('newUserGroupForm_description').value = "";
	    document.getElementById('newUserGroupForm_id').value = "";
    },
    
    /**
     *  Check all check boxes for content types
     */
    setCheckboxes : function()
    {
        checked = document.myUserGroups.checkAllSetting.checked;
        var userGroupIds = document.getElementsByName('userGroupIds');
        urUtil.setCheckboxes(userGroupIds, checked);
    }, 
    
    createNewUserGroupDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
            this.submit();
	    };	
		
	    // handle a cancel of the adding content type dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.usergroup.newUserGroupDialog.hide();
	        YAHOO.ur.usergroup.clearUserGroupForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               	    
	            //get the response from adding a content type
	            var response = o.responseText;
	            var userGroupForm = document.getElementById('newUserGroupDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            userGroupForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newUserGroupForm_success").value;
	  
	            //if the content type was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.usergroup.newUserGroupDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the content type was added
	                YAHOO.ur.usergroup.clearUserGroupForm();
	                YAHOO.ur.usergroup.newUserGroupDialog.hide();
	            }
	            myUserGroupsTable.submitForm(getUserGroupsAction);
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o)
	    {
	        alert('user group submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new user group button is clicked.
	    YAHOO.ur.usergroup.newUserGroupDialog = new YAHOO.widget.Dialog('newUserGroupDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					  { text:'Cancel', handler:handleCancel } ]
		    } );
		
		YAHOO.ur.usergroup.newUserGroupDialog.submit = function()
		{
			YAHOO.util.Connect.setForm('newUserGroupForm');
	        if( YAHOO.ur.usergroup.newUserGroupDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new content type) based on the action.
                var action = newUserGroupAction;
                var cObj = YAHOO.util.Connect.asyncRequest('post',
                action, callback);
            }
		}
		
        // show and center the dialog box
        YAHOO.ur.usergroup.newUserGroupDialog.showDialog = function()
        {
            YAHOO.ur.usergroup.newUserGroupDialog.center();
            YAHOO.ur.usergroup.newUserGroupDialog.show()
        }
    
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.usergroup.newUserGroupDialog.validate = function() {
	        var name = document.getElementById('newUserGroupForm_name').value;
		    if (name == "" || name == null) {
		       alert('A user group name must be entered');
			    return false;
		    } 
		    else 
		    {
			    return true;
		    }
	    };

	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
			
	    // Render the Dialog
	   YAHOO.ur.usergroup.newUserGroupDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showUserGroup", "click", 
	        YAHOO.ur.usergroup.newUserGroupDialog.showDialog, 
	        YAHOO.ur.usergroup.newUserGroupDialog, true);
 
    },
    
     /** 
      * clear out any form data messages or input
      * in the new content type form
      */
    clearDeleteUserGroupForm  : function()
    {
          var div = document.getElementById('userGroupError');
          div.innerHTML = "";
    },
    
    /**
     * Creates a YUI new content type modal dialog for when a user wants to create 
     * a new content type
     *
     */
    createDeleteUserGroupDialog : function()
    {
        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('myUserGroups');
	    
	        //delete the content type
            var cObj = YAHOO.util.Connect.asyncRequest('post',
            deleteUserGroupAction, callback);
	    };
	
		
	    // handle a cancel of deleting content type dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.usergroup.deleteUserGroupDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               	    
	            //get the response from adding a content type
	            var response = eval("("+o.responseText+")");
	    
	            //if the content type was not deleted then show the user the error message.
	            // received from the server
	            if( response.userGroupDeleted == "false" )
	            {
	                var deleteUserGroupError = document.getElementById('form_deleteUserGroupError');
                    deleteUserGroupError.innerHTML = '<p id="newDeleteUserGroupError">' 
                    + response.message + '</p>';
                    YAHOO.ur.usergroup.deleteUserGroupDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the content types were deleted
	                YAHOO.ur.usergroup.clearDeleteUserGroupForm();
	                YAHOO.ur.usergroup.deleteUserGroupDialog.hide();
	            }
	            // reload the table
	            myUserGroupsTable.submitForm(getUserGroupsAction);
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	        alert('delete user group type submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new content type button is clicked.
	    YAHOO.ur.usergroup.deleteUserGroupDialog = new YAHOO.widget.Dialog('deleteUserGroupDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
					  { text:'No', handler:handleCancel } ]
		    } );
		
        // show and center the delete dialog
        YAHOO.ur.usergroup.deleteUserGroupDialog.showDialog = function()
        {
            YAHOO.ur.usergroup.deleteUserGroupDialog.center();
            YAHOO.ur.usergroup.deleteUserGroupDialog.show();
        }
    
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.usergroup.deleteUserGroupDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showDeleteUserGroup", "click", 
	        YAHOO.ur.usergroup.deleteUserGroupDialog.showDialog, 
	        YAHOO.ur.usergroup.deleteUserGroupDialog, true);
    },
    
    init : function()
    {
        YAHOO.ur.usergroup.getUserGroups(0,1,1,'asc');
        YAHOO.ur.usergroup.createNewUserGroupDialog();
        YAHOO.ur.usergroup.createDeleteUserGroupDialog();
    }
    
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.usergroup.init);