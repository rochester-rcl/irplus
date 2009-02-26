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
YAHOO.namespace("ur.login");

// action for forgot password
var savePasswordTokenAction = basePath + 'savePasswordToken.action';


YAHOO.ur.login = 
{
   /** 
     * clear out any form data messages or input
     * in the forgot password form
     */
    clearForgotPasswordForm : function()
    {
	    document.getElementById('forgotPasswordForm_email').value = "";
    },
    
    /**
     * Creates a YUI forgot password modal dialog 
     */
    createForgotPasswordDialog : function()
    {
    
	    // Define various event handlers for Dialog
	    var handleSubmit = function() {

	        YAHOO.util.Connect.setForm('forgotPasswordForm');
	    	    
	        if( YAHOO.ur.login.forgotPasswordDialog.validate() )
	        {
                var cObj = YAHOO.util.Connect.asyncRequest('post',
                savePasswordTokenAction, callback);
            }
	    };
	
		
	    // handle a cancel of the forgot password dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.login.clearForgotPasswordForm();
	        YAHOO.ur.login.forgotPasswordDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	    
	            //get the response from adding a password token
	            var response = o.responseText;
	            var forgotPasswordForm = document.getElementById('forgotPasswordDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            forgotPasswordForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("forgotPasswordForm_success").value;
	            var message = document.getElementById("forgotPasswordForm_message").value;
	  
	            //if the password token not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
	                var emailError = document.getElementById('emailError');
                    emailError.innerHTML = '<p id="forgotPasswordForm_emailError">' + message + '</p>';
   
                    YAHOO.ur.login.forgotPasswordDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the password token was added
	                YAHOO.ur.login.clearForgotPasswordForm();
	                YAHOO.ur.login.forgotPasswordDialog.hide();
	            }
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o)
	    {
	        alert('Email submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // forgot password link is clicked.
	    YAHOO.ur.login.forgotPasswordDialog = new YAHOO.widget.Dialog('forgotPasswordDialog', 
        { width : "500px",
		  visible : false, 
		  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					  { text:'Cancel', handler:handleCancel } ]
		} );
		
		/**
		 *  show and center the dialog
		 */
		YAHOO.ur.login.forgotPasswordDialog.showDialog = function()
		{
		    YAHOO.ur.login.forgotPasswordDialog.center();
		    YAHOO.ur.login.forgotPasswordDialog.show();
		}
   
 	    // Validate the email id
	    YAHOO.ur.login.forgotPasswordDialog.validate = function() {
	        var email = document.getElementById('forgotPasswordForm_email').value;
		    if (email == "" || email == null) 
		    {
		        alert('Email Id must be entered');
			    return false;
		    } 
		    else 
		    {
			    if (!urUtil.emailcheck(email)) 
			    {
				    alert('Invalid email address');
				    return false;
			    }
			    return true;
		    }
	    };

	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.login.forgotPasswordDialog.render();
    },
    
    /**
     * Forgot the password
     */
    forgotPassword : function()
    {
	        YAHOO.ur.login.forgotPasswordDialog.showDialog();
    },
    
    init : function()
    {
        YAHOO.ur.login.createForgotPasswordDialog();
    }
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady( YAHOO.ur.login.init); 

