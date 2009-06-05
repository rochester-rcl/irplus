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
 * This code is for dealing with managing user email and editing user
 * 
 */
YAHOO.namespace("ur.email");


// action to perform when submitting the email form.
var myEmailAction = basePath + 'admin/getEmails.action';

// actions for adding and removing emails and editing the user information
var updateUserAction = basePath + 'admin/updateUser.action';
var newEmailAction = basePath + 'admin/createEmail.action';
var deleteEmailAction = basePath + 'admin/deleteEmail.action';
var updateEmailAction = basePath + 'admin/updateEmail.action';
var defaultEmailAction = basePath + 'admin/setDefaultEmail.action';

// action to perform for searching names
var mySearchNameAction =  basePath + 'admin/searchAuthoritativeName.action';
var removeNameAction =  basePath + 'admin/removeUserAuthoritativeName.action';
var addNameAction =  basePath + 'admin/addUserAuthoritativeName.action';

// object to hold the user emails.
var myEmailTable = new YAHOO.ur.table.Table('myEmails', 'newEmails');


/**
 * email namespace
 */
YAHOO.ur.email = {

    /**
     * Make sure the permissions are set correctly
     */
    autoCheckRoles : function(permission) 
    {
        var userRole = document.getElementById("editUserForm_isUser");
        var adminRole = document.getElementById("editUserForm_isAdmin");
        var authorRole = document.getElementById("editUserForm_isAuthor");
        var researcherRole = document.getElementById("editUserForm_isResearcher");
        var collectionAdminRole = document.getElementById("editUserForm_isCollectionAdmin");
        
	    if (permission.id == 'editUserForm_isAdmin') 
	    {
		    if (permission.checked) 
		    {
		        userRole.checked = true;
		        authorRole.checked = true;
		        researcherRole.checked = true;
		    }
	    }
	
	    if (permission.id == 'editUserForm_isResearcher') 
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

	    if (permission.id == 'editUserForm_isAuthor') 
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
	    
	    if (permission.id == 'editUserForm_isCollectionAdmin') 
	    {
		    if (permission.checked) 
		    {
		        userRole.checked = true;
		        authorRole.checked = true;
		    } 
	    }
	    
	    if (permission.id == 'editUserForm_isUser') 
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
	 *  Function that retireves emails from 
	 *  the server
	 */
	getEmails : function() 
	{
		var callback =
		{
		    success: function(o) 
		    {
		        // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
		            var divToUpdate = document.getElementById('newEmails');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get user email Failure ' + o.status + ' status text ' + o.statusText );
			}
		}

		var userId = document.getElementById('editUserForm_id').value;
		
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        myEmailAction + '?bustcache='+new Date().getTime() + '&id=' + userId, 
	        callback, null);
	},

	
	/** 
	  * clear out any form data messages or input
	  * in the new email form
	  */
	clearEmailForm : function()
	{
		    document.getElementById('newEmailForm_emailId').value ="";
		    document.getElementById('newEmailForm_email').value ="";
		    document.newEmailForm.newEmail.value = "true";
	},
	
	/**
	 * Creates the dialog for adding / editing email
	 *
	 */
	createNewEmailDialog : function()
	{
	   
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
			
		// handle a cancel of the adding/editing email dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.email.clearEmailForm();
		    YAHOO.ur.email.newEmailDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a email
		        var response = o.responseText;
		        var emailForm = document.getElementById('newEmailDialogFields');
		    
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        emailForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("newEmailForm_success").value;
		  
		        //if the email was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
            	    YAHOO.ur.email.newEmailDialog.showDialog();
		        }
		        else
		        {
		    	    if (document.getElementById("newEmailForm_message").value != '')
		    	    {
			    	    var emailConfirmationMessage = document.getElementById('emailConfirmationDialogFields');
				        emailConfirmationMessage.innerHTML = document.getElementById("newEmailForm_message").value;
				    
			            // we can clear the form if the email was added
			            YAHOO.ur.email.clearEmailForm();
			            YAHOO.ur.email.newEmailDialog.hide();				    
		    		
		    		    YAHOO.ur.email.emailConfirmationDialog.showDialog();
		    	    } 
		    	    else 
		    	    {
			            // we can clear the form if the email was added
			            YAHOO.ur.email.clearEmailForm();
			            YAHOO.ur.email.newEmailDialog.hide();
		    	    } 

		        }
		        YAHOO.ur.email.getEmails();
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('Email submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new Email button is clicked.
		YAHOO.ur.email.newEmailDialog = new YAHOO.widget.Dialog('newEmailDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
	   	// show and center the email dialog
        YAHOO.ur.email.newEmailDialog.showDialog = function()
        {
            YAHOO.ur.email.newEmailDialog.center();
            YAHOO.ur.email.newEmailDialog.show();
        }
        			
		
		// Submit the form
		YAHOO.ur.email.newEmailDialog.submit = function()
		{
	       YAHOO.util.Connect.setForm('newEmailForm');
		    
		    	    
		    if( YAHOO.ur.email.newEmailDialog.validate() )
		    {
		        //based on what we need to do (update or create a 
		        // new email) based on the action.
	            var action = newEmailAction;
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            action, callback);
	        }
	    }
	        	   
	 	// Validate the email entries in the form 
		YAHOO.ur.email.newEmailDialog.validate = function() {
		    var name = urUtil.trim(document.getElementById('newEmailForm_email').value);
			if (name == "" || name == null) {
			    alert('Email address must be entered');
				return false;
			} 
			
			if (!urUtil.emailcheck(name)) {
	    		alert('Invalid E-mail address.');
	    		return false;
	    	}
	    	return true;
			
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.email.newEmailDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showEmail", "click", 
		    YAHOO.ur.email.newEmailDialog.showDialog, 
		    YAHOO.ur.email.newEmailDialog, true);
	},

	/**
	 * Creates the dialog for showing email information
	 *
	 */
	createEmailConfirmationDialog : function()
	{
	   
		// Define various event handlers for Dialog
		var handleOk = function() {
			YAHOO.ur.email.emailConfirmationDialog.hide();
		};
		
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new Email button is clicked.
		YAHOO.ur.email.emailConfirmationDialog = new YAHOO.widget.Dialog('emailConfirmationDialog', 
	        { width : "600px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Ok', handler:handleOk, isDefault:true } ]
			} );
			
	   	// show and center the email dialog
        YAHOO.ur.email.emailConfirmationDialog.showDialog = function()
        {
            YAHOO.ur.email.emailConfirmationDialog.center();
            YAHOO.ur.email.emailConfirmationDialog.show();
        }
				
				
		// Render the Dialog
		YAHOO.ur.email.emailConfirmationDialog.render();

	},
	
	
		
	/**
	 * Save the user information
	 *
	 */
	saveEditUser : function()
	{

		var callback = 
		{
			success: function(o)
			{
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
			        var response = o.responseText;
			    
			        var userForm = document.getElementById('editUserDialogFields');
		
			        // update the form fields with the response.  This updates
			        // the form, if there was an issue, update the form with
			        // the error messages.
			        userForm.innerHTML = o.responseText;
			        alert("Saved");
			    }
		    
			},
		
			failure : function(o) 
			{
		    	alert('user update failed ' + o.status  + ' status text ' + o.statusText);
			}
		}
	
	 	// Validate the entries in the form 
		var validateEditForm = function() {
	
			if (document.getElementById('editUserForm_name').value == '') {
			    alert('A user name must be entered');
				return false;
			} 
	
			if ((document.getElementById('editUserForm_isUser').checked== false) & 
					(document.getElementById('editUserForm_isAdmin').checked== false)) {
			    alert('Select a Role for the user');
				return false;
			} 
	
	       	return true;
	
		};
							 
	    YAHOO.util.Connect.setForm('editUserForm');
		if (validateEditForm()) {
		    var userForm = document.getElementById('editUserDialogFields');
	        YAHOO.util.Connect.asyncRequest('post', updateUserAction, callback)
        }							 
	
	},
	
	 /** 
	  * clear out any error message
	  */
	clearDeleteEmailForm : function()
	{
	    var emailError = document.getElementById('newDeleteEmailError');
	    var div = document.getElementById('emailError');
	
		//clear out any error information
		if( emailError != null )
		{
		    if( emailError.innerHTML != null 
		        || emailError.innerHTML != "")
		    { 
		        div.removeChild(emailError);
		    }
		}
	},
	
	/**
	 * Creates a YUI dialog for when a user wants to 
	 * delete an email
	 *
	 */
	createDeleteEmailDialog : function()
	{
	
		// Define various event handlers for Dialog
		var handleSubmit = function() {
		    YAHOO.util.Connect.setForm('deleteEmail');
		    //delete the email
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deleteEmailAction, callback);
		};
		
			
		// handle a cancel of deleting email dialog
		var handleCancel = function() {
		    YAHOO.ur.email.deleteEmailDialog.hide();
		};
		
		var handleSuccess = function(o) {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from deleting the email
		        var response = eval("("+o.responseText+")");
		    
		        //if the email was not deleted then show the user the error message.
		        // received from the server
		        if( response.emailDeleted == "false" )
		        {
		            var deleteEmailError = document.getElementById('deleteEmailError');
	                deleteEmailError.innerHTML = '<p id="newDeleteEmailError">' 
	                + response.message + '</p>';
	                YAHOO.ur.email.deleteEmailDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the emails were deleted
		            YAHOO.ur.email.deleteEmailDialog.hide();
		            YAHOO.ur.email.clearDeleteEmailForm();
		        }
		    
		        // reload the table
		        YAHOO.ur.email.getEmails();
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('Email deletion failed ' + o.status  + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// delete email button is clicked.
		YAHOO.ur.email.deleteEmailDialog = new YAHOO.widget.Dialog('deleteEmailDialog', 
	        { width : "400px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );

	   	// show and center the sponsor dialog
        YAHOO.ur.email.deleteEmailDialog.showDialog = function()
        {
            YAHOO.ur.email.deleteEmailDialog.center();
            YAHOO.ur.email.deleteEmailDialog.show();
        }			
	   
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.email.deleteEmailDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showDeleteEmail", "click", 
		    YAHOO.ur.email.deleteEmailDialog.showDialog, 
		    YAHOO.ur.email.deleteEmailDialog, true);
	},
	
	/**
	 * Handles deleting a single email
	 */
	deleteEmail : function(emailId)
	{
		document.getElementById('deleteEmailId').value = emailId;
		YAHOO.ur.email.deleteEmailDialog.showDialog();
	},
	
	/**
	 * Set an email as default
	 */
	defaultEmail : function(emailId, userId)
	{
		var callback =
		{
		    success: function(o) 
		    {
		        // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
		            var divToUpdate = document.getElementById('newEmails');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Set default email Failure ' + o.status + ' status text ' + o.statusText );
			}
		}

		var userId = document.getElementById('editUserForm_id').value;
		
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	    	defaultEmailAction + '?bustcache='+new Date().getTime() + '&emailId=' + emailId + '&userId=' + userId, 
	        callback, null);
	},
	

	/**
	 * handles the serarch form submit
	 */
	handleSearchFormSubmit : function()
	{

		    var formObject = document.getElementById('search_form');
			YAHOO.util.Connect.setForm(formObject);
			    
		    if( YAHOO.ur.email.validate() )
		    {
		        var cObj = YAHOO.util.Connect.asyncRequest('POST',
		        mySearchNameAction, YAHOO.ur.email.getNameCallback);
		    }
	},
	
	validate : function() 
	{
	    	var name = document.getElementById('search_query').value;
	
			if (name == "" || name == null) {
				return false;
			} else {
				return true;
			}
			
	},
	
	/**
	 * This call back updates the html when the names are retrieved
	 * 
	 */
	getNameCallback : 
	{
	    success: function(o) 
	    {
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var divToUpdate = document.getElementById('newNames');
	            divToUpdate.innerHTML = o.responseText; 
	        }
	    },
		
		failure: function(o) 
		{
		    alert('Get name Failure ' + o.status + ' status text ' + o.statusText );
		}
	},

	/**
	 * This call back updates the html when the names are retrieved
	 * 
	 */
	getUserAuthoritativeNameCallback : 
	{
	    success: function(o) 
	    {
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var divToUpdate = document.getElementById('user_auth_name');
	            divToUpdate.innerHTML = o.responseText;
	            YAHOO.ur.email.handleSearchFormSubmit(); 
	        }
	    },
		
		failure: function(o) 
		{
		    alert('getUserAuthoritativeName Failure ' + o.status + ' status text ' + o.statusText );
		}
	    

	},
		
	/**
	 *  Function that searches the names
	 *  and returns from start row and end row 
	 */
	getNames : function(rowStart)
	{
		document.myNames.rowStart.value=rowStart;
		var formObject = document.getElementById('names');
		YAHOO.util.Connect.setForm(formObject);
		
	    var transaction = YAHOO.util.Connect.asyncRequest('post', 
	        mySearchNameAction, 
	        YAHOO.ur.email.getNameCallback);
	},
	
	removeName : function(userId)
	{
		
	    var cObj = YAHOO.util.Connect.asyncRequest('get',
	           removeNameAction + '?id=' + userId, YAHOO.ur.email.getUserAuthoritativeNameCallback);
	           
	},
	
	/**
	 * Function to add contributor to the item
	 *
	 */
	addName : function(userId, authoritativeNameId) 
	{ 
	    var cObj = YAHOO.util.Connect.asyncRequest('get',
	           addNameAction + '?id=' + userId + '&authorityId=' + authoritativeNameId, YAHOO.ur.email.getUserAuthoritativeNameCallback);
	},
	
	/** initialize the page this is called once the dom has
	 *  been created
	 */ 
	init : function() 
	{
	    YAHOO.ur.email.getEmails();
	    YAHOO.ur.email.createNewEmailDialog();
	    YAHOO.ur.email.createDeleteEmailDialog();
	    YAHOO.ur.email.createEmailConfirmationDialog();
	    YAHOO.util.Event.addListener("search_button", "click", 
		    YAHOO.ur.email.handleSearchFormSubmit); 
	}    	
}


// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.email.init);