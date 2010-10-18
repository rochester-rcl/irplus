/*
   Copyright 2008-2010 University of Rochester

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
 * This code is for dealing with adding and removing fields
 * in administration
 */
YAHOO.namespace("ur.external_account_type");

// action to perform when submitting the fields.
var getExternalAccountTypesTable = basePath + 'admin/getExternalAccountTypesTable.action';

// actions for adding, editing and removing fields
var updateExternalAccountTypeAction = basePath + 'admin/updateExternalAccountType.action';
var newExternalAccountTypeAction = basePath + 'admin/createExternalAccountType.action';
var deleteExternalAccountTypeAction = basePath + 'admin/deleteExternalAccountType.action';
var getExternalAccountTypeAction = basePath + 'admin/getExternalAccountType.action';


/* Create the namespace and functions */
YAHOO.ur.external_account_type = 
{
		
	/**
	  * function to edit a single external account type
	  */
	edit : function(id)
	{	
	    /*
	     * This call back updates the html when a editing an external account type
	     */
	    var callback =
	    {
	        success: function(o) 
	        {
	        	// check for the timeout - forward user to login page if timeout
	            // occurred
		        if( !urUtil.checkTimeOut(o.responseText) )
		        {     
	                var divToUpdate = document.getElementById('newExternalAccountTypeDialogFields');
	                divToUpdate.innerHTML = o.responseText; 
	                document.newExternalAccountTypeForm.newExternalAccountType.value = "false";
	                YAHOO.ur.external_account_type.newExternalAccountTypeDialog.showDialog();
	            }
	        },
		
		    failure: function(o) 
		    {
		       alert('edit external account type failed ' + o.status + ' status text ' + o.statusText );
		    }
	    };
	        
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	    		getExternalAccountTypeAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
	        callback, null);
	},		
		
    /**
     * clear the account type form
     */
    clearForm : function ()
    {
        var errorDiv = document.getElementById('error');
        errorDiv.innerHTML = "";

        document.getElementById('externalAccountType_name').value = "";
        document.getElementById('newExternalAccountTypeForm_description').value = "";
        document.getElementById('externalAccountTypeForm_id').value = "";
        document.getElementById('externalAccountType_case_sensitive').checked=false;
        document.newExternalAccountTypeForm.newExternalAccountType.value = "true";
    },
    
 	/**
 	 * Get the account types table
 	 */
    getAccountTypeTable : function ()
    {
        /*
         * This call back updates the html when the external account types
         * are returned
         */
         var callback =
         {
             success: function(o) 
             {
                 // check for the timeout - forward user to login page if timout
                 // occured
                 if( !urUtil.checkTimeOut(o.responseText) )
                 {
                     var divToUpdate = document.getElementById('external_account_table');
                     divToUpdate.innerHTML = o.responseText; 
                 }
             },

             failure: function(o) 
             {
                 alert('Get external account types Failure ' + o.status + ' status text ' + o.statusText );
             }
         };
     
         var transaction = YAHOO.util.Connect.asyncRequest('GET', 
        		 getExternalAccountTypesTable +  '?bustcache='+new Date().getTime(), 
                 callback, null);
    },
    
    /** create the dialog for dealing with a new external account type */
    createNewExternalAccountTypeDialog : function()
    {
        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
            this.submit();
	    };
	
	    // handle a cancel of the adding field dialog
	    var handleCancel = function()
	    {
	    	YAHOO.ur.external_account_type.newExternalAccountTypeDialog.hide();
	    	YAHOO.ur.external_account_type.clearForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        // check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
                //get the response from adding an external account type
	            var response = o.responseText;
	            var externalAccountTypeForm = document.getElementById('newExternalAccountTypeDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            externalAccountTypeForm.innerHTML = o.responseText;
	             
	            // determine if the add/edit was a success
	            var success = document.getElementById("newExternalAccountType_success").value;
	            //if the field was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
	            	YAHOO.ur.external_account_type.newExternalAccountTypeDialog.showDialog();
	            }
	            else
	            {
	            	YAHOO.ur.external_account_type.getAccountTypeTable();
	                YAHOO.ur.external_account_type.newExternalAccountTypeDialog.hide();
	                // we can clear the form if the field was added
	                YAHOO.ur.external_account_type.clearForm();
	            }
	        }
	    };

	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert('External account type submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new field button is clicked.
	    YAHOO.ur.external_account_type.newExternalAccountTypeDialog = new YAHOO.widget.Dialog('newExternalAccountTypeDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					     { text:'Cancel', handler:handleCancel } ]
		    } );
	
	    // show and center the field dialog
        YAHOO.ur.external_account_type.newExternalAccountTypeDialog.showDialog = function()
        {
            YAHOO.ur.external_account_type.newExternalAccountTypeDialog.center();
            YAHOO.ur.external_account_type.newExternalAccountTypeDialog.show();
        };

        // override the submit function
        YAHOO.ur.external_account_type.newExternalAccountTypeDialog.submit = function()
        {
        	YAHOO.util.Connect.setForm('newExternalAccountTypeForm');
	        if( YAHOO.ur.external_account_type.newExternalAccountTypeDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new field) based on the action.
               var action = newExternalAccountTypeAction;
	           if( document.newExternalAccountTypeForm.newExternalAccountType.value != 'true')
	           {
	               action = updateExternalAccountTypeAction;
	           }

                var cObj = YAHOO.util.Connect.asyncRequest('post',
                action, callback);
            }
        	
        };
    
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.external_account_type.validate = function()
        {
	        var name = document.getElementById('newExternalAccountTypeForm_name').value;
		    if (name == "" || name == null) 
		    {
		        alert('An External Account Type name must be entered');
			    return false;
		    } 
		    else 
		    {
			    return true;
		    }
	    };

	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess,
	                 failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.external_account_type.newExternalAccountTypeDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showAddExternalAccountType", "click", 
	        YAHOO.ur.external_account_type.newExternalAccountTypeDialog.showDialog, 
	        YAHOO.ur.external_account_type.newExternalAccountTypeDialog, true);
	},
	
	/**
	 * Sets up the form and the delete
	 */
	deleteAccount : function(id)
	{
	    document.getElementById('deleteId').value=id;	
	    YAHOO.ur.external_account_type.deleteExternalAccountTypeDialog.showDialog();
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the delete form
	  */
	clearDeleteForm : function()
	{
	    var div = document.getElementById('deleteExternalAccountTypeError');
	    document.getElementById('deleteId').value="";	
	    div.innerHTML = "";
	},
	
	/**
	 * Creates a YUI new external account type modal dialog for when a user wants to delete 
	 * a statement
	 *
	 */
	createDeleteExternalAccountTypeDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() {
		    YAHOO.util.Connect.setForm('deleteExternalAccountType');
		    
		    //delete the copyrigght statement
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deleteExternalAccountTypeAction, callback);
		};
		
			
		// handle a cancel of deleting copyrigght statement dialog
		var handleCancel = function() {
		    YAHOO.ur.external_account_type.deleteExternalAccountTypeDialog.hide();
		};
		
		var handleSuccess = function(o) {
		
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a copyright statement
		        var response = eval("("+o.responseText+")");
		    
		        //if the copyright statement was not deleted then show the user the error message.
		        // received from the server
		        if( response.deleted == "false" )
		        {
		            var deleteError = document.getElementById('deleteExternalAccountTypeError');
	                deleteError.innerHTML = '<p id="newDeleteCopyrightStatementError">' 
	                + response.message + '</p>';
	                YAHOO.ur.external_account_type.deleteExternalAccountTypeDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the copyrigght statements were deleted
		            YAHOO.ur.external_account_type.deleteExternalAccountTypeDialog.hide();
		            YAHOO.ur.external_account_type.clearDeleteForm();
		        }
		        // reload the table
		        YAHOO.ur.external_account_type.getAccountTypeTable();
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('External account type delete submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new copyrigght statement button is clicked.
		YAHOO.ur.external_account_type.deleteExternalAccountTypeDialog = new YAHOO.widget.Dialog('deleteExternalAccountTypeDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
	     
	    //center and show the delete dialog
	    YAHOO.ur.external_account_type.deleteExternalAccountTypeDialog.showDialog = function()
	    {
	        YAHOO.ur.external_account_type.deleteExternalAccountTypeDialog.center();
	        YAHOO.ur.external_account_type.deleteExternalAccountTypeDialog.show();
	    }
	   
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.external_account_type.deleteExternalAccountTypeDialog.render();
	
	},
    
    
    init : function()
    {
		YAHOO.ur.external_account_type.createNewExternalAccountTypeDialog();
		YAHOO.ur.external_account_type.createDeleteExternalAccountTypeDialog();
    }
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.external_account_type.init);