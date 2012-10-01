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
YAHOO.namespace("ur.identifier_type_dc_mapping");

//action to perform when submitting the fields.
var getIdentifierTypeDcMappingTable = basePath + 'admin/getIdentifierTypeDublinCoreMappings.action';

// actions for adding, editing and removing fields
var updateIdentifierTypeDcMappingAction = basePath + 'admin/updateIdentifierTypeDublinCoreMapping.action';
var newIdentifierTypeDcMappingAction = basePath + 'admin/createIdentifierTypeDublinCoreMapping.action';
var deleteIdentifierTypeDcMappingAction = basePath + 'admin/deleteIdentifierTypeDublinCoreMapping.action';
var getIdentifierTypeDcMappingAction = basePath + 'admin/getIdentifierTypeDublinCoreMapping.action';



/* Create the namespace and functions */
YAHOO.ur.identifier_type_dc_mapping = 
{
		
	/**
	 * clear the add form
	 */
	clearForm : function ()
	{
	    var errorDiv = document.getElementById('error');
	    errorDiv.innerHTML = "";
	    document.getElementById('identifierTypeDcMappingForm_id').value = "";
	    document.newIdentifierTypeMappingForm.update.value = "false";
	},
	
	
	/**
	  * function to edit a single mapping
	  */
	edit : function(id)
	{	
	    /*
	     * This call back updates the html when a editing a mapping
	     */
	    var callback =
	    {
	        success: function(o) 
	        {
	        	// check for the timeout - forward user to login page if timeout
	            // occurred
		        if( !urUtil.checkTimeOut(o.responseText) )
		        {     
	                var divToUpdate = document.getElementById('identifierTypeMappingDialogFields');
	                divToUpdate.innerHTML = o.responseText; 
	                document.newIdentifierTypeMappingForm.update.value = "true";
	                YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog.showDialog();
	            }
	        },
		
		    failure: function(o) 
		    {
		       alert('edit mapping failed ' + o.status + ' status text ' + o.statusText );
		    }
	    };
	        
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	    		getIdentifierTypeDcMappingAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
	        callback, null);
	},		
	
	
    /** create the dialog for dealing with a new contributor type dc mapping */
    createNewIdentifierTypeDcMappingDialog : function()
    {
        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
            this.submit();
	    };
	
	    // handle a cancel of the adding field dialog
	    var handleCancel = function()
	    {
	    	YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog.hide();
	    	YAHOO.ur.identifier_type_dc_mapping.clearForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        // check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
                //get the response from adding an external account type
	            var response = o.responseText;
	            var externalAccountTypeForm = document.getElementById('identifierTypeMappingDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            externalAccountTypeForm.innerHTML = o.responseText;
	             
	            // determine if the add/edit was a success
	            var success = document.getElementById("newIdentifierTypeDublinCoreMapping_success").value;
	            //if the field was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
	            	YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog.showDialog();
	            }
	            else
	            {
	            	YAHOO.ur.identifier_type_dc_mapping.getMappingTable();
	            	YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog.hide();
	            	YAHOO.ur.identifier_type_dc_mapping.clearForm();
	            }
	        }
	    };

	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert('Create contributor type dublin core mapping failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new field button is clicked.
	    YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog = new YAHOO.widget.Dialog('newIdentifierTypeMappingDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					     { text:'Cancel', handler:handleCancel } ]
		    } );
	
	    // show and center the field dialog
	    YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog.showDialog = function()
        {
	    	YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog.center();
	    	YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog.show();
        };

        // override the submit function
        YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog.submit = function()
        {
        	YAHOO.util.Connect.setForm('newIdentifierTypeMappingForm');
	        if( YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new field) based on the action.
               var action = newIdentifierTypeDcMappingAction;
	           if( document.newIdentifierTypeMappingForm.update.value == 'true')
	           {
	               action = updateIdentifierTypeDcMappingAction;
	           }
                var cObj = YAHOO.util.Connect.asyncRequest('post',
                action, callback);
            }
        	
        };
    
 	    // Validate the entries in the form to require that both first and last name are entered
        YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog.validate = function()
        {
			return true;
	    };

	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess,
	                 failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showMapping", "click", 
	    		YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog.showDialog, 
	    		YAHOO.ur.identifier_type_dc_mapping.newIdentifierTypeDcMappingDialog, true);
	},
	
 	/**
 	 * Get the contributor dc mapping table
 	 */
    getMappingTable : function ()
    {
        /*
         * This call back updates the html when the mapping table is
         * are returned
         */
         var callback =
         {
             success: function(o) 
             {
                 // check for the timeout - forward user to login page if timeout
                 // occurred
                 if( !urUtil.checkTimeOut(o.responseText) )
                 {
                     var divToUpdate = document.getElementById('mappings');
                     divToUpdate.innerHTML = o.responseText; 
                 }
             },

             failure: function(o) 
             {
                 alert('Get dc mapping Failure ' + o.status + ' status text ' + o.statusText );
             }
         };
     
         var transaction = YAHOO.util.Connect.asyncRequest('GET', 
        		 getIdentifierTypeDcMappingTable +  '?bustcache='+new Date().getTime(), 
                 callback, null);
    },
	
	
	
	/**
	 * Sets up the form and the delete
	 */
	deleteMapping : function(id)
	{
 	    document.getElementById('deleteId').value=id;	
	    YAHOO.ur.identifier_type_dc_mapping.deleteIdentifierTypeDcMappingDialog.showDialog();
	},
	
	/**
	 * Creates a YUI new external account type modal dialog for when a user wants to delete 
	 * a statement
	 *
	 */
	createDeleteIdentifierTypeDcMappingDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() {
		    YAHOO.util.Connect.setForm('deleteIdentifierTypeDublinCoreMapping');
		    //delete 
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deleteIdentifierTypeDcMappingAction, callback);
		};
		
			
		// handle a cancel of deleting 
		var handleCancel = function() {
			YAHOO.ur.identifier_type_dc_mapping.deleteIdentifierTypeDcMappingDialog.hide();
		};
		
		var handleSuccess = function(o) {
		
			// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a copyright statement
		        var response = eval("("+o.responseText+")");
		    
		        //if not deleted then show the user the error message.
		        // received from the server
		        if( response.deleted == "false" )
		        {
		            var deleteError = document.getElementById('deleteExternalAccountTypeError');
	                deleteError.innerHTML = '<p id="newDeleteCopyrightStatementError">' 
	                + response.message + '</p>';
	                YAHOO.ur.identifier_type_dc_mapping.deleteIdentifierTypeDcMappingDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the copyrigght statements were deleted
		        	YAHOO.ur.identifier_type_dc_mapping.getMappingTable();
		        	YAHOO.ur.identifier_type_dc_mapping.deleteIdentifierTypeDcMappingDialog.hide();
		        	YAHOO.ur.identifier_type_dc_mapping.clearDeleteForm();
		        }
		        // reload the table
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('Mapping delete submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new copyrigght statement button is clicked.
		YAHOO.ur.identifier_type_dc_mapping.deleteIdentifierTypeDcMappingDialog = new YAHOO.widget.Dialog('deleteIdentifierTypeMappingDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
	     
	    //center and show the delete dialog
		YAHOO.ur.identifier_type_dc_mapping.deleteIdentifierTypeDcMappingDialog.showDialog = function()
	    {
			YAHOO.ur.identifier_type_dc_mapping.deleteIdentifierTypeDcMappingDialog.center();
			YAHOO.ur.identifier_type_dc_mapping.deleteIdentifierTypeDcMappingDialog.show();
	    }
	   
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.identifier_type_dc_mapping.deleteIdentifierTypeDcMappingDialog.render();
	
	},
    
	
	
	 /** 
	  * clear out any form data messages or input
	  * in the delete form
	  */
	clearDeleteForm : function()
	{
	    var div = document.getElementById('deleteIdentifierTypeDublinCoreMappingError');
	    document.getElementById('deleteId').value="";	
	    div.innerHTML = "";
	},
    
    init : function()
    {
		YAHOO.ur.identifier_type_dc_mapping.createNewIdentifierTypeDcMappingDialog();
		YAHOO.ur.identifier_type_dc_mapping.createDeleteIdentifierTypeDcMappingDialog();
    }
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.identifier_type_dc_mapping.init);