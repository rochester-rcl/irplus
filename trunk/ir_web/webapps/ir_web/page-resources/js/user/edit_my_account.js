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
YAHOO.namespace("ur.user.account");


// action to perform when submitting the email form.
var myEmailAction = basePath + 'user/getEmails.action';

// actions for adding and removing emails and editing the user information
var newEmailAction = basePath + 'user/createEmail.action';
var deleteEmailAction = basePath + 'user/deleteEmail.action';
var updateEmailAction = basePath + 'user/updateEmail.action';


// Action to change password
var changePasswordAction = basePath + 'user/changeMyPassword.action';

YAHOO.ur.user.account = 
{
   
    /**
     * clear the change password form
     */
    clearChangePasswordForm : function()
    {
        document.getElementById('change_password_form_new_password').value ="";
	    document.getElementById('change_password_form_confirm_password').value ="";
    },
    
    /**
     * Change password dialog
     */
    createChangePasswordDialog : function()
    {
        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('changePasswordForm');
	    
		    if (changePasswordValidate()) 
		    {
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
        	    changePasswordAction, callback);
            }
	    };
	
		
	    // handle a cancel of the change password
	    var handleCancel = function() 
	    {
	        YAHOO.ur.user.account.clearChangePasswordForm();
	        YAHOO.ur.user.account.changePasswordDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        var divToUpdate = document.getElementById('new_password_dialog_fields');
            divToUpdate.innerHTML = o.responseText; 	
            YAHOO.ur.user.account.clearChangePasswordForm();
            YAHOO.ur.user.account.changePasswordDialog.hide();
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	        alert('Change password submission failed ' + o.status);
 	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // change password button is clicked.
	    YAHOO.ur.user.account.changePasswordDialog = new YAHOO.widget.Dialog('change_password_dialog', 
        { width : "450px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					  { text:'Cancel', handler:handleCancel } ]
		} );

        // show and center the dialog box
        YAHOO.ur.user.account.changePasswordDialog.showDialog = function()
        {
            YAHOO.ur.user.account.changePasswordDialog.center();
            YAHOO.ur.user.account.changePasswordDialog.show()
        }
   
 	    // Validate the entries in the form 
	    var changePasswordValidate = function() {
    	    if (document.getElementById('change_password_form_new_password').value == '') {
    		    alert('Please enter password.');
    		    return false;
    	    }

    	    if (document.getElementById('change_password_form_confirm_password').value == '') {
    		    alert('Please enter password confirm.');
    		    return false;
    	    }
    	
    	    if (document.getElementById('change_password_form_new_password').value != document.getElementById('change_password_form_confirm_password').value) {
    		    alert('The password does not match with the confirm password.');
    		    return false;
    	    }

       	    return true;

	    };

	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
			
	    // Render the Dialog
	    YAHOO.ur.user.account.changePasswordDialog.render();
	

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("show_change_password", "click", 
	        YAHOO.ur.user.account.changePasswordDialog.showDialog, 
	        YAHOO.ur.user.account.changePasswordDialog, true);	
    },
    
    /**
     * validate the form
     */
    formValidation : function() 
    {

        if (document.getElementById('newUserForm_first_name').value == '') {
        	alert('Please enter first Name.');
        	return false;
        }

        if (document.getElementById('newUserForm_last_name').value == '') {
        	alert('Please enter last Name.');
        	return false;
        }

        return true;
    },
    
    /**
     * init the information on the page
     */
    init : function()
    {
        YAHOO.ur.user.account.createChangePasswordDialog();
        var myTabs = new YAHOO.widget.TabView("user-account-tabs");
    }
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.user.account.init);

