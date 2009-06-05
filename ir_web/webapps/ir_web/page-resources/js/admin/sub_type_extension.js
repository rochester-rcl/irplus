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
YAHOO.namespace("ur.subTypeExtension");


// action to perform when submitting the personal content types.
var mySubTypeExtensionAction = basePath + 'admin/allSubTypeExtension.action';

// actions for adding and removing sub type extensions
var editSubTypeExtensionAction = basePath + 'admin/getSubTypeExtension.action';
var updateSubTypeExtensionAction = basePath + 'admin/updateSubTypeExtension.action';
var newSubTypeExtensionAction = basePath + 'admin/createSubTypeExtension.action';
var deleteSubTypeExtensionAction = basePath + 'admin/deleteSubTypeExtension.action';

// object to hold the specified sub type data.
var mySubTypeExtensionsTable = new YAHOO.ur.table.Table('mySubTypeExtensions', 'newSubTypeExtensions');

YAHOO.ur.subTypeExtension = 
{
    // function to the sub type extensions
    getSubTypeExtensions : function(rowStart, startPageNumber, currentPageNumber, order)
    {
        //This call back updates the html when a new sub type is
        //retrieved.
        var callback =
        {
            success: function(o) 
            {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {                
                    var divToUpdate = document.getElementById('newSubTypeExtensions');
                    divToUpdate.innerHTML = o.responseText; 
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get sub type extension Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var subTypeId = document.getElementById('form_data_subTypeId').value;
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            mySubTypeExtensionAction + '?subTypeId=' + subTypeId + '&rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(), 
            callback, null);
    },
    
    /**
     * clear the sub type form
     */
    clearSubTypeExtensionForm : function()
    {
        //clear out any error information
        var div = document.getElementById('SubTypeExtensionError');
        div.innerHTML = "";
        
 	    document.getElementById('newSubTypeExtensionForm_name').value = "";
	    document.getElementById('newSubTypeExtensionForm_description').value = "";
	    document.getElementById('newSubTypeExtensionForm_subTypeExtensionId').value = "";
	    document.newSubTypeExtensionForm.newSubTypeExtension.value = "true";
    }, 
    
    /**
     * Check / uncheck all checkboxes
     */
    setCheckboxes : function()
    {
        checked = document.mySubTypeExtensions.checkAllSetting.checked;
        var subTypeIds = document.getElementsByName('subTypeExtensionIds');
        urUtil.setCheckboxes(subTypeIds, checked);
    },
    
    /**
     * Creates a YUI new top media type modal dialog for when a 
     * user wants to create a sub type extension
     *
     */
    createNewSubTypeExtensionDialog : function ()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	       this.submit();
	    };
	
	    // handle a cancel of the adding top media type dialog
	    var handleCancel = function() {
	        YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog.hide();
	        YAHOO.ur.subTypeExtension.clearSubTypeExtensionForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               	    
	            //get the response from adding a sub type
	            var response = o.responseText;
	            var SubTypeExtensionForm = document.getElementById('newSubTypeExtensionDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            SubTypeExtensionForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newSubTypeExtensionForm_success").value;
	    
	            //if the top media type was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the top media type was added
	                YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog.hide();
	                YAHOO.ur.subTypeExtension.clearSubTypeExtensionForm();
	            }
	            mySubTypeExtensionsTable.submitForm(mySubTypeExtensionAction);
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert('sub type extension Submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new sub type button is clicked.
	    YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog = new YAHOO.widget.Dialog('newSubTypeExtensionDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					      { text:'Cancel', handler:handleCancel } ]
		    } );
		
		YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog.submit = function()
		{
		    YAHOO.util.Connect.setForm('newSubTypeExtensionForm');
	        if( YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new top media type) based on the action.
                var action = newSubTypeExtensionAction;
	            if( document.newSubTypeExtensionForm.newSubTypeExtensionForm_new.value != 'true')
	            {
	               action = updateSubTypeExtensionAction;
	            }

                var cObj = YAHOO.util.Connect.asyncRequest('post', action, callback);
            }
		};
		
		//center and show the dialog
		YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog.showDialog = function()
		{
		    YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog.center();
		    YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog.show();
		};
   
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog.validate = function() 
	    {
	        var name = document.getElementById('newSubTypeExtensionForm_name').value;
		    if (name == "" || name == null) 
		    {
		        alert('A sub type extension name must be entered');
			    return false;
		    } else 
		    {
			    return true;
		    }
	    };

	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showSubTypeExtension", "click", 
	        YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog.showDialog, 
	        YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog, true);
    },
    
    // function to edit sub type information
    editSubTypeExtension : function(id)
    {
            /*
         * This call back updates the html when a editing a mime top media type
         */
        var callback =
        {
            success: function(o) 
            {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {                           
                    var divToUpdate = document.getElementById('newSubTypeExtensionDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                    document.newSubTypeExtensionForm.newSubTypeExtension.value = "false";
                    YAHOO.ur.subTypeExtension.newSubTypeExtensionDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Edit sub type extension Failure ' + o.status + 
	            ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            editSubTypeExtensionAction + '?subTypeExtensionId=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
    },
    
    // clear the sub type extension form
    clearDeleteSubTypeExtensionForm : function()
    {
        var div = document.getElementById('SubTypeExtensionError');
        div.innerHTML = "";
    }, 
    
    
    /**
     * Dialog to show deleting a sub type
     */
    createDeleteSubTypeExtensionDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('mySubTypeExtensions');
	    
	        //delete the sub type
            var cObj = YAHOO.util.Connect.asyncRequest('post',
            deleteSubTypeExtensionAction, callback);
	    };
	
	    // handle a cancel of deleting sub type dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.subTypeExtension.deleteSubTypeExtensionDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               	    
	            //get the response from adding a sub type
	            var response = eval("("+o.responseText+")");
	    
	            //if the sub type was not deleted then show the user the error message.
	            // received from the server
	            if( response.SubTypeExtensionDeleted == "false" )
	            {
	                var deleteSubTypeExtensionError = document.getElementById('form_deleteSubTypeExtensionError');
                    deleteSubTypeExtensionError.innerHTML = '<p id="newDeleteSubTypeExtensionError">' 
                    + response.message + '</p>';
                    YAHOO.ur.subTypeExtension.deleteSubTypeExtensionDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the sub types were deleted
	                YAHOO.ur.subTypeExtension.deleteSubTypeExtensionDialog.hide();
	                YAHOO.ur.subTypeExtension.clearDeleteSubTypeExtensionForm();
	            }
	            // reload the table
	            mySubTypeExtensionsTable.submitForm(mySubTypeExtensionAction);
	        }
	    };
	
	    // handle form Submission failure
	    var handleFailure = function(o) {
	        alert('delete sub type Submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new sub type button is clicked.
	    YAHOO.ur.subTypeExtension.deleteSubTypeExtensionDialog = new YAHOO.widget.Dialog('deleteSubTypeExtensionDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
					  { text:'No', handler:handleCancel } ]
		} );
		
		// center and show the dialog
        YAHOO.ur.subTypeExtension.deleteSubTypeExtensionDialog.showDialog = function()
        {
            YAHOO.ur.subTypeExtension.deleteSubTypeExtensionDialog.center();
            YAHOO.ur.subTypeExtension.deleteSubTypeExtensionDialog.show();
        }
        
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.subTypeExtension.deleteSubTypeExtensionDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showDeleteSubTypeExtension", "click", 
	        YAHOO.ur.subTypeExtension.deleteSubTypeExtensionDialog.showDialog, 
	        YAHOO.ur.subTypeExtension.deleteSubTypeExtensionDialog, true);
    
    },
    
    /**
     * Init the javascript on the page
     */
    init : function()
    {
        YAHOO.ur.subTypeExtension.getSubTypeExtensions(0,1,1,'asc');
        YAHOO.ur.subTypeExtension.createNewSubTypeExtensionDialog();
        YAHOO.ur.subTypeExtension.createDeleteSubTypeExtensionDialog();
    }
}

// call init once dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.subTypeExtension.init); 

