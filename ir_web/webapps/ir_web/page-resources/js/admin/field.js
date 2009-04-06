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
 * This code is for dealing with adding and removing fields
 * in administration
 */
YAHOO.namespace("ur.field");

// action to perform when submitting the fields.
var myFieldAction = basePath + 'admin/getFields.action';

// actions for adding, editing and removing fields
var updateFieldAction = basePath + 'admin/updateField.action';
var newFieldAction = basePath + 'admin/createField.action';
var deleteFieldAction = basePath + 'admin/deleteField.action';
var getFieldAction = basePath + 'admin/getField.action';

// object to hold the specified field data.
var myFieldTable = new YAHOO.ur.table.Table('myFields', 'newFields');


YAHOO.ur.field = 
{
   /**
    *  Function that retrieves field information
    */
    getFields : function (rowStart, startPageNumber, currentPageNumber, order)
    {
        var callback =
        {
            success: function(o) 
            {
                // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
                    var divToUpdate = document.getElementById('newFields');
                    divToUpdate.innerHTML = o.responseText; 
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get field Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            myFieldAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(),  
            callback, null);
    }, 
    
    clearFieldForm : function()
    {
        var errorDiv = document.getElementById('error_div');
        errorDiv.innerHTML = "";
	
	    document.getElementById('newFieldForm_name').value = "";
	    document.getElementById('newFieldForm_description').value = "";
	    document.getElementById('newFieldForm_id').value = "";
	    document.newFieldForm.newField.value = "true";
    }, 
    
    /**
     * Set all department id's form
     */
    setCheckboxes : function()
    {
        checked = document.myFields.checkAllSetting.checked;
        var fieldIds = document.getElementsByName('researcherFieldIds');
        urUtil.setCheckboxes(fieldIds, checked);
    },
    
    
    createNewFieldDialog : function()
    {
        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
            this.submit();
	    };
	
	    // handle a cancel of the adding field dialog
	    var handleCancel = function()
	    {
	        YAHOO.ur.field.newFieldDialog.hide();
	        YAHOO.ur.field.clearFieldForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
                //get the response from adding a field
	            var response = o.responseText;
	            var fieldForm = document.getElementById('newFieldDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            fieldForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newFieldForm_success").value;
	  
	            //if the field was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.field.newFieldDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the field was added
	                YAHOO.ur.field.newFieldDialog.hide();
	                YAHOO.ur.field.clearFieldForm();
	            }
	            myFieldTable.submitForm(myFieldAction);
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert('field submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new field button is clicked.
	    YAHOO.ur.field.newFieldDialog = new YAHOO.widget.Dialog('newFieldDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					     { text:'Cancel', handler:handleCancel } ]
		    } );
	
	    // show and center the field dialog
        YAHOO.ur.field.newFieldDialog.showDialog = function()
        {
            YAHOO.ur.field.newFieldDialog.center();
            YAHOO.ur.field.newFieldDialog.show();
        };
        
        // override the submit function
        YAHOO.ur.field.newFieldDialog.submit = function()
        {
        	YAHOO.util.Connect.setForm('newFieldForm');
	        if( YAHOO.ur.field.newFieldDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new field) based on the action.
               var action = newFieldAction;
	           if( document.newFieldForm.newField.value != 'true')
	           {
	               action = updateFieldAction;
	           }

                var cObj = YAHOO.util.Connect.asyncRequest('post',
                action, callback);
            }
        	
        };
    
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.field.newFieldDialog.validate = function()
        {
	        var name = document.getElementById('newFieldForm_name').value;
		    if (name == "" || name == null) 
		    {
		        alert('A field name must be entered');
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
	    YAHOO.ur.field.newFieldDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showField", "click", 
	        YAHOO.ur.field.newFieldDialog.showDialog, 
	        YAHOO.ur.field.newFieldDialog, true);
	},
    
 
    /**
     * function to edit field information
     */
    editField : function(id)
    {	    
	    /*
         * This call back updates the html when a editing a field
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('newFieldDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                   	document.newFieldForm.newField.value = "false";
	                YAHOO.ur.field.newFieldDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get field failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getFieldAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
     },
    
    /**
     * Clear the delete fine
     */
    clearDeleteFieldForm : function()
    {
        var fieldError = document.getElementById('deleteField');
        fieldError.innerHTML = "";
    },
    
    /**
     * Create the delete dialog
     */
    createDeleteFieldDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('myFields');
	    
	        //delete the field
            var cObj = YAHOO.util.Connect.asyncRequest('post',
            deleteFieldAction, callback);
	    };
	
		
	    // handle a cancel of deleting field dialog
	    var handleCancel = function() {
	        YAHOO.ur.field.deleteFieldDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            //get the response from adding a field
	            var response = eval("("+o.responseText+")");
	    
	            //if the field was not deleted then show the user the error message.
	            // received from the server
	            if( response.fieldDeleted == "false" )
	            {
	                var deleteFieldError = document.getElementById('form_deleteFieldError');
                    deleteFieldError.innerHTML = '<p id="newDeleteFieldError">' 
                    + response.message + '</p>';
                    YAHOO.ur.field.deleteFieldDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the fields were deleted
	                YAHOO.ur.field.deleteFieldDialog.hide();
	                YAHOO.ur.field.clearDeleteFieldForm();
	            }
	            // reload the table
	            myFieldTable.submitForm(myFieldAction);
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	        alert('field submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new field button is clicked.
	    YAHOO.ur.field.deleteFieldDialog = new YAHOO.widget.Dialog('deleteFieldDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
					      { text:'No', handler:handleCancel } ]
		    } );
		
   
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess,
	                 failure: handleFailure };
	
	    // show and center the dialog box		
	    YAHOO.ur.field.deleteFieldDialog.showDialog = function()
	    {
	        YAHOO.ur.field.deleteFieldDialog.show();
	        YAHOO.ur.field.deleteFieldDialog.center();
	    }
			
	    // Render the Dialog
	    YAHOO.ur.field.deleteFieldDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showDeleteField", "click", 
	        YAHOO.ur.field.deleteFieldDialog.showDialog, 
	        YAHOO.ur.field.deleteFieldDialog, true);
    }, 
    
    init : function()
    {
        YAHOO.ur.field.getFields(0,1,1,'asc');
        YAHOO.ur.field.createNewFieldDialog();
        YAHOO.ur.field.createDeleteFieldDialog();
    }
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.field.init);