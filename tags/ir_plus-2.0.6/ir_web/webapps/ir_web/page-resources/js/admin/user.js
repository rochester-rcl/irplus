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
 * This code is for dealing with managing users
 * 
 */
YAHOO.namespace("ur.user");

var changePasswordAction = basePath + 'admin/changePassword.action';

// action to perform when submitting the personal contributor types.
var myUserAction = basePath + 'admin/getUsers.action';
var mySearchUserAction = basePath + 'admin/searchUser.action';

// actions for adding and removing users
var updateUserAction = basePath + 'admin/updateUser.action';
var newUserAction = basePath + 'admin/createUser.action';
var deleteUserAction = basePath + 'admin/deleteUser.action';
var viewEditUserAction = basePath + 'admin/userEditView.action';

// Action for admin to login as any user
var loginAsUserAction = basePath + 'admin/loginAsUser.action';

// object to hold the specified users data.
var myUserTable = new YAHOO.ur.table.Table('myUsers', 'newUsers');


/**
 * sponsor namespace
 */
YAHOO.ur.user = {

    /**
     * Make sure the permissions are set correctly
     */
    autoCheckRoles : function(permission) 
    {
        var userRole = document.getElementById("newUserForm_isUser");
        var adminRole = document.getElementById("newUserForm_isAdmin");
        var authorRole = document.getElementById("newUserForm_isAuthor");
        var researcherRole = document.getElementById("newUserForm_isResearcher");
        var collectionAdminRole = document.getElementById("newUserForm_isCollectionAdmin");
        
	    if (permission.id == 'newUserForm_isAdmin') 
	    {
		    if (permission.checked) 
		    {
		        userRole.checked = true;
		        authorRole.checked = true;
		        researcherRole.checked = true;
		    }
	    }
	
	    if (permission.id == 'newUserForm_isResearcher') 
	    {
		    if (permission.checked) 
		    {
		        userRole.checked = true;
		        authorRole.checked = true;
		    } 
		    else 
		    {
		        adminRole.checked = false;
		    }
	    }

	    if (permission.id == 'newUserForm_isAuthor') 
	    {
		    if (permission.checked) 
		    {
		        userRole.checked = true;
		    } 
		    else
		    {
		        adminRole.checked = false;
		        researcherRole.checked = false;
		    }
	    }
	    
	    if (permission.id == 'newUserForm_isCollectionAdmin') 
	    {
		    if (permission.checked) 
		    {
		        userRole.checked = true;
		        authorRole.checked = true;
		    } 
	    }
	    
	    if (permission.id == 'newUserForm_isUser') 
	    {
		    if (!permission.checked) 
		    {
		        adminRole.checked = false;
		        authorRole.checked = false;
		        researcherRole.checked = false;
		        collectionAdminRole.checked = false;
		    }
	    }
	    return true;
    },
	
	/**
	 *  Function that retireves users from 
	 *  the server
	 */
	 getUsers : function(rowStart, startPageNumber, currentPageNumber, sortElement, sortType)
	 {
		var getUserCallback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {               
		            var divToUpdate = document.getElementById('newUsers');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get user Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        myUserAction + '?rowStart=' + rowStart +  '&startPageNumber=' + startPageNumber + '&currentPageNumber=' + currentPageNumber
	        	+ '&sortElement=' + sortElement + '&sortType=' + sortType + '&bustcache=' + new Date().getTime(), 
	        getUserCallback, null);		
	},

	 /** 
	  * clear out any form data messages or input
	  * in the new user form
	  */
	clearAddUserForm : function()
	{
	        document.getElementById('userError').innerHTML ="";
		    document.getElementById('newUserForm_password').value ="";
		    document.getElementById('newUserForm_password_check').value ="";
	        document.getElementById('newUserForm_id').value = "";
	    	document.getElementById('newUserForm_first_name').value ="";
		    document.getElementById('newUserForm_last_name').value ="";
	    	document.getElementById('newUserForm_name').value ="";
		    document.getElementById('newUserForm_email').value ="";
		    document.getElementById('newUserForm_phone_number').value ="";
		    document.getElementById('newUserForm_account_locked').checked = false;
		    document.getElementById('newUserForm_account_expired').checked = false;
		    document.getElementById('newUserForm_credentials_expired').checked = false;
		    document.getElementById('newUserForm_send_Email').checked = false;
		    document.getElementById('newUserForm_isUser').checked = false;
		    document.getElementById('newUserForm_isAdmin').checked = false;
		    document.getElementById('newUserForm_isAuthor').checked = false;
		    document.getElementById('newUserForm_isResearcher').checked = false;
		    document.getElementById('newUserForm_isCollectionAdmin').checked = false;
	
	},
	
    /**
     * Set all user id's form
     */
    setCheckboxes : function()
    {
        checked = document.myUsers.checkAllSetting.checked;
        var userIds = document.getElementsByName('userIds');
        urUtil.setCheckboxes(userIds, checked);
        
    }, 	
 	
	/**
	 * Creates a YUI new user modal dialog for when a user wants to create 
	 * a new user
	 */
	createNewUserDialog : function()
	{
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
			
		// handle a cancel of the adding user dialog
		var handleCancel = function() 
		{
			YAHOO.ur.user.userDialog.hide();
		    YAHOO.ur.user.clearAddUserForm();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               		
		        var response = o.responseText;
		    
		        var userForm = document.getElementById('newUserDialogFields');
	
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        userForm.innerHTML = o.responseText;
		    
		        // determine if the add was a success
		        var success = document.getElementById("newUserForm_success").value;
		        var userId = document.getElementById("newUserForm_id").value;
	
		        //if the user was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.user.userDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the user was added
		            YAHOO.ur.user.userDialog.hide();
		            YAHOO.ur.user.clearAddUserForm();
				    window.location = viewEditUserAction + '?id=' + userId;	        
		        }
		    }
		    
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('user submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new user button is clicked.
		YAHOO.ur.user.userDialog = new YAHOO.widget.Dialog('newUserDialog', 
	        { width : "590px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
		
		//get the roles for the user and
	    //show them.
	    YAHOO.ur.user.userDialog.showDialog = function()
	    {
	        YAHOO.ur.user.userDialog.center();
	        YAHOO.ur.user.userDialog.show();
	    }	
	   
		// Submit the form
	    YAHOO.ur.user.userDialog.submit = function()
	    {		   
		    YAHOO.util.Connect.setForm('newUserForm');
		    
			if (YAHOO.ur.user.userDialog.validate()) {
		        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        	newUserAction, callback);
	        }
	    }   
	        	   	
	 	// Validate the entries in the form
		YAHOO.ur.user.userDialog.validate = function() 
		{
	
			if (document.getElementById('newUserForm_name').value == '') {
			    alert('A user name must be entered');
				return false;
			} 
	
	    	if (document.getElementById('newUserForm_password').value == '') {
	    		alert('Please enter password.');
	    		return false;
	    	}
	
	    	if (document.getElementById('newUserForm_password_check').value == '') {
	    		alert('Please enter password check.');
	    		return false;
	    	}
	    	
	    	if (document.getElementById('newUserForm_password_check').value != document.getElementById('newUserForm_password').value) {
	    		alert('The password check does not match with the password.');
	    		return false;
	    	}
	
	    	if (document.getElementById('newUserForm_email').value == '') {
	    		alert('Please enter E-mail address.');
	    		return false;
	    	}
	
	    	if (!urUtil.emailcheck(urUtil.trim(document.getElementById('newUserForm_email').value))) {
	    		alert('Invalid E-mail address.');
	    		return false;
	    	}
	    	
			if ((document.getElementById('newUserForm_isUser').checked== false) & 
					(document.getElementById('newUserForm_isAdmin').checked== false)) {
			    alert('Select a Role for the user');
				return false;
			} 
	       	return true;
	
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.user.userDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showUser", "click", 
		    YAHOO.ur.user.userDialog.showDialog, 
		    YAHOO.ur.user.userDialog, true);
	
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the change password form
	  */
	clearChangePasswordForm : function()
	{
			document.getElementById('changePasswordForm_id').value ="";
		    document.getElementById('changePasswordForm_password').value ="";
		    document.getElementById('changePasswordForm_password_check').value ="";
		    document.getElementById('changePasswordForm_email_message').value ="";
		    document.getElementById('changePasswordForm_send_email').checked =false;
	},
	
	
	createChangePasswordDialog : function() 
	{
		// Define various event handlers for Dialog
		var handleSubmit = function() {
			this.submit();
		};
		
			
		// handle a cancel of the change password
		var handleCancel = function() {
		    YAHOO.ur.user.changePasswordDialog.hide();
		    YAHOO.ur.user.clearChangePasswordForm();
		};
		
		var handleSuccess = function(o) {
	        YAHOO.ur.user.changePasswordDialog.hide();
	        YAHOO.ur.user.clearChangePasswordForm();
	
		    myUserTable.submitForm(myUserAction);
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('Change password submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// change password button is clicked.
		YAHOO.ur.user.changePasswordDialog = new YAHOO.widget.Dialog('changePasswordDialog', 
	        { width : "550px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
		// Show the dialog.
	    YAHOO.ur.user.changePasswordDialog.showDialog = function()
	    {
	        YAHOO.ur.user.changePasswordDialog.show();
	        YAHOO.ur.user.changePasswordDialog.center();
	    }
	    	
	    // Submit the form
	    YAHOO.ur.user.changePasswordDialog.submit = function()	
	    {			    
		    YAHOO.util.Connect.setForm('changePasswordForm');
		    
			if (YAHOO.ur.user.changePasswordDialog.validate()) {
		        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        	changePasswordAction, callback);
	        }
	    }
	   
	 	// Validate the entries in the form 
		YAHOO.ur.user.changePasswordDialog.validate = function() 
		{
	    	if (document.getElementById('changePasswordForm_password').value == '') {
	    		alert('Please enter password.');
	    		return false;
	    	}
	
	    	if (document.getElementById('changePasswordForm_password_check').value == '') {
	    		alert('Please enter password confirm.');
	    		return false;
	    	}
	    	
	    	if (document.getElementById('changePasswordForm_password_check').value != document.getElementById('changePasswordForm_password').value) {
	    		alert('The password check does not match with the password.');
	    		return false;
	    	}
	
	       	return true;
	
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.user.changePasswordDialog.render();
	},
	
    //show the change password dialog
    changePassword : function(id)
    {
    	document.getElementById('changePasswordForm_id').value=id;
        YAHOO.ur.user.changePasswordDialog.showDialog();
    },	
	
	 /** 
	  * clear out any form data messages or input
	  * in the new user form
	  */
	clearDeleteUserForm : function()
	{
	    var userError = document.getElementById('newUserForm_nameError');
	    var div = document.getElementById('userError');
	    
		//clear out any error information
		if( userError != null )
		{
		    if( userError.innerHTML != null 
		        || userError.innerHTML != "")
		    { 
		        div.removeChild(userError);
		    }
		}
	},
	
	/**
	 * Delete user function - sets the user id and shows the dialog
	 */
	deleteUser : function (userId)
	{
		document.getElementById('deleteUserId').value = userId;
		YAHOO.ur.user.deleteUserDialog.showDialog();
	},
	
	/**
	 * Creates a YUI new user modal dialog for when a user wants to create 
	 * a new user
	 *
	 */
	createDeleteUserDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
		    YAHOO.util.Connect.setForm('deleteUser');
		    
		    //delete the user
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deleteUserAction, callback);
		};
		
			
		// handle a cancel of deleting user dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.user.deleteUserDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {  
	            // we can clear the form if the users were deleted
	            YAHOO.ur.user.clearDeleteUserForm();
	            YAHOO.ur.user.deleteUserDialog.hide();
	            
		        //get the response from adding a user
		        var response = eval("("+o.responseText+")");
		        
		        
		        //if the user was not deleted then show the user the error message.
		        // received from the server
		        if( response.userDeleted == "false" )
		        {
		            var deleteUserError = document.getElementById('default_error_dialog_content');
	                deleteUserError.innerHTML =  response.message;
	                YAHOO.ur.user.errorDialog.showDialog();
		        }
		       
		        // reload the table
		        YAHOO.ur.user.getUsers(0,1,1,'lastName','asc');
		        // reload search - if query exists
		        if( document.getElementById('userSearchQuery').value != null && document.getElementById('userSearchQuery').value != '')
		        {
		        	YAHOO.ur.user.searchUser(0,1,1);
		        }
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('delete user submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new user button is clicked.
		YAHOO.ur.user.deleteUserDialog = new YAHOO.widget.Dialog('deleteUserDialog', 
	        { width : "400px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
			
		// Show the dialog
	    YAHOO.ur.user.deleteUserDialog.showDialog = function()
	    {
	        YAHOO.ur.user.deleteUserDialog.show();
	        YAHOO.ur.user.deleteUserDialog.center();
	    }
	    	   
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.user.deleteUserDialog.render();

	},
	
	/**
	 * Creates dialog for a user to login as another user 
	 */
	createLoginAsUserDialog : function() 
	{
		// Define various event handlers for Dialog
		var handleSubmit = function() {
	
		    YAHOO.ur.user.loginAsUserDialog.hide();
	
		    document.loginAsUserForm.submit();
		};
		
			
		// handle a cancel of login as another user
		var handleCancel = function() {
		    YAHOO.ur.user.loginAsUserDialog.hide();
		};
		
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// login as user link is clicked.
		YAHOO.ur.user.loginAsUserDialog = new YAHOO.widget.Dialog('loginAsUserDialog', 
	        { width : "350px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
			
		// Show the dialog
	    YAHOO.ur.user.loginAsUserDialog.showDialog = function()
	    {
	        YAHOO.ur.user.loginAsUserDialog.show();
	        YAHOO.ur.user.loginAsUserDialog.center();
	    }			
	
				
		// Render the Dialog
		YAHOO.ur.user.loginAsUserDialog.render();
	},
	
	/**
	 * Search user
	 */
	searchUser : function(rowStart, startPageNumber, currentPageNumber) {

		var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {               		    
		            var divToUpdate = document.getElementById('search_results_div');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get user search results ' + o.status + ' status text ' + o.statusText );
			}
		}
		YAHOO.util.Connect.setForm('userSearchForm');
		
	    var transaction = YAHOO.util.Connect.asyncRequest('POST', 
	        mySearchUserAction + '?rowStart=' + rowStart +  '&startPageNumber=' + startPageNumber + '&currentPageNumber=' + currentPageNumber, 
	        callback, null);			
	},	
	
	createErrorDialog : function()
	{
	    // Define various event handlers for Dialog
		var handleYes = function() 
		{
		    var contentArea = document.getElementById('default_error_dialog_content');
		    contentArea.innerHTML = ""; 
		    this.hide();
		};

		// Instantiate the Dialog
		YAHOO.ur.user.errorDialog = 
		    new YAHOO.widget.Dialog("error_dialog_box", 
										     { width: "600px",
											   visible: false,
											   modal: true,
											   close: false,										   
											   buttons: [ { text:"Ok", handler:handleYes, isDefault:true } ]
											} );
		
		YAHOO.ur.user.errorDialog.setHeader("Error");
		
		// Show the dialog
	    YAHOO.ur.user.errorDialog.showDialog = function()
	    {
	        YAHOO.ur.user.errorDialog.show();
	        YAHOO.ur.user.errorDialog.center();
	    }			
		
		    // Render the Dialog
		YAHOO.ur.user.errorDialog.render();
	}, 
	
 	//show the confirmation dialog
    loginAsUser : function(id)
    {
    	document.getElementById('loginAsUserForm_id').value=id;
        YAHOO.ur.user.loginAsUserDialog.showDialog();
    },	

	/** initialize the page this is called once the dom has
	 *  been created
	 */ 
	init : function() 
	{
	    YAHOO.ur.user.getUsers(0,1,1,'lastName','asc');
	    YAHOO.ur.user.createErrorDialog();
	    YAHOO.ur.user.createNewUserDialog();
	    YAHOO.ur.user.createDeleteUserDialog();
	    YAHOO.ur.user.createChangePasswordDialog();
	    YAHOO.ur.user.createLoginAsUserDialog();
	    var myTabs = new YAHOO.widget.TabView("user-tabs");
	}	
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.user.init);
