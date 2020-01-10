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
YAHOO.namespace("ur.person.name.authority.identifier.type");

// action to perform when submitting the personal identifier types.
var myIdentifierTypeAction = basePath + 'admin/getPersonNameAuthorityIdentTypes.action';

// actions for adding and removing folders
var updateIdentifierTypeAction = basePath + 'admin/updatePersonNameAuthorityIdentType.action';
var newIdentifierTypeAction = basePath + 'admin/createPersonNameAuthorityIdentType.action';
var deleteIdentifierTypeAction = basePath + 'admin/deletePersonNameAuthorityIdentType.action';
var getIdentifierTypeAction = basePath + 'admin/getPersonNameAuthorityIdentType.action';

// object to hold the specified identifier type data.
var myIdentifierTypeTable = new YAHOO.ur.table.Table('myPersonNameAuthorityIdentifierTypes', 'newPersonNameAuthorityIdentifierTypes');

YAHOO.ur.person.name.authority.identifier.type = 
{

    getIdentifierTypes : function (rowStart, startPageNumber, currentPageNumber, order)
    {
        /*
         * This call back updates the html when a new identifier type is
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
                     var divToUpdate = document.getElementById('newPersonNameAuthorityIdentifierTypes');
                     divToUpdate.innerHTML = o.responseText; 
                 }
             },
	
	         failure: function(o) 
	         {
	             alert('Get identifier type Failure ' + o.status + ' status text ' + o.statusText );
	         }
         }
         var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            myIdentifierTypeAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(), 
            callback, null);
    },
     
    /** 
     * clear out any form data messages or input
     * in the new identifier type form
     */
    clearIndentifierTypeForm : function()
    {
	    //clear out any error information
        var div = document.getElementById('personNameAuthorityIdentifierTypeError');
        div.innerHTML = "";

	    document.getElementById('newPersonNameAuthorityIdentifierTypeForm_name').value="";
	    document.getElementById('newPersonNameAuthorityIdentifierTypeForm_description').value="";
	    document.newPersonNameAuthorityIdentifierTypeForm.newPersonNameAuthorityIdentifierType.value = "true";
    }, 
    
    /**
     * Check or uncheck all identifier type ids
     */
    setCheckboxes : function()
    {
        checked = document.myPersonNameAuthorityIdentifierTypes.checkAllSetting.checked;
        var identifierTypeIds = document.getElementsByName('personNameAuthorityIdentifierTypeIds');
        urUtil.setCheckboxes(identifierTypeIds, checked);
    },
    
    /**
     * Creates a YUI new identifier type modal dialog for when a user wants to create 
     * a new identifier type
     *
     */
    createNewIdentifierTypeDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        this.submit();
	    };
	
		
	    // handle a cancel of the adding identifier type dialog
	    var handleCancel = function() 
	    {
	    	YAHOO.ur.person.name.authority.identifier.type.identifierTypeDialog.hide();
	    	YAHOO.ur.person.name.authority.identifier.type.clearDeleteIdentifierTypeForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            //get the response from adding a identifier type
	            var response = eval("("+o.responseText+")");
	    
	            //if the identifier type was not added then show the user the error message.
	            // received from the server
	            if( response.personNameAuthorityIdentifierTypeAdded == "false" )
	            {	 
	                var identifierTypeNameError = document.getElementById('personNameAuthorityIdentifierTypeError');
                    identifierTypeNameError.innerHTML = '<p id="newIdentifierTypeForm_nameError">' + response.message + '</p>';
                    YAHOO.ur.person.name.authority.identifier.type.identifierTypeDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the identifier type was added
	            	YAHOO.ur.person.name.authority.identifier.type.identifierTypeDialog.hide();
	            	YAHOO.ur.person.name.authority.identifier.type.clearIndentifierTypeForm();
	            }
	            myIdentifierTypeTable.submitForm(myIdentifierTypeAction);
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('identifier type submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new identifier type button is clicked.
	    YAHOO.ur.person.name.authority.identifier.type.identifierTypeDialog = new YAHOO.widget.Dialog('newPersonNameAuthorityIdentifierTypeDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					      { text:'Cancel', handler:handleCancel } ]
		    } );
		
		// override the submit function
	    YAHOO.ur.person.name.authority.identifier.type.identifierTypeDialog.submit = function()
		{
		    YAHOO.util.Connect.setForm('newPersonNameAuthorityIdentifierTypeForm');
	        //based on what we need to do (update or create a 
	        // new identifier type) based on the action.
            var action = newIdentifierTypeAction;
            if( YAHOO.ur.person.name.authority.identifier.type.validate() )
	        {
            	YAHOO.ur.util.wait.waitDialog.showDialog();
	            if( document.newPersonNameAuthorityIdentifierTypeForm.newPersonNameAuthorityIdentifierType.value != 'true')
	            {
	                action = updateIdentifierTypeAction;
	            }
	            var cObj = YAHOO.util.Connect.asyncRequest('post', action, callback);
	        }
           
		}
		
        //center and show the dialog box 
	    YAHOO.ur.person.name.authority.identifier.type.identifierTypeDialog.showDialog = function()
        {
	    	YAHOO.ur.person.name.authority.identifier.type.identifierTypeDialog.show();
	    	YAHOO.ur.person.name.authority.identifier.type.identifierTypeDialog.center();
        }
 	
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.person.name.authority.identifier.type.validate = function() 
	    {
	        var name = document.getElementById('newPersonNameAuthorityIdentifierTypeForm_name').value;
		    if (name == "" || name == null) 
		    {
		        alert('A Person Name Authority Identifier type name must be entered');
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
	     YAHOO.ur.person.name.authority.identifier.type.identifierTypeDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showNewPersonNameAuthorityIdentifierType", "click", 
	    		YAHOO.ur.person.name.authority.identifier.type.identifierTypeDialog.showDialog, 
	    		YAHOO.ur.person.name.authority.identifier.type.identifierTypeDialog, true);
    },
    
    /**
     * function to edit content type information
     */
    editIdentifierType : function(id)
    {	    
	    /*
         * This call back updates the html when a editing an identifier type
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('newPersonNameAuthorityIdentifierTypeTypeDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
	                document.getElementById('newPersonNameAuthorityIdentifierTypeForm_id').value = id;
	                document.newPersonNameAuthorityIdentifierTypeForm.newPersonNameAuthorityIdentifierType.value = "false";
	                YAHOO.ur.person.name.authority.identifier.type.identifierTypeDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Edit identifier type failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getIdentifierTypeAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
    },
    
    
    /*
     * Clear the form of any error messages
     */
    clearDeleteIdentifierTypeForm : function()
    {
        var div = document.getElementById('deletePersonNameAuthorityIdentifierTypeError');
        div.innerHTML = "";
    }, 
    
    /**
     * Creates a YUI new identifier type modal dialog for when a user wants to delete 
     * an identifier type
     *
     */
    createDeleteIdentifierTypeDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function()
	    {
	        YAHOO.util.Connect.setForm('personNameAuthorityIdentifierTypes');
	    
	        //delete the identifier type
            var cObj = YAHOO.util.Connect.asyncRequest('post',
            deleteIdentifierTypeAction, callback);
	    };
	
	    // handle a cancel of deleting identifier type dialog
	    var handleCancel = function() {
	    	YAHOO.ur.person.name.authority.identifier.type.deleteIdentifierTypeDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            //get the response from adding a identifier type
	            var response = eval("("+o.responseText+")");
	    
	            //if the identifier type was not deleted then show the user the error message.
	            // received from the server
	            if( response.identifierTypeDeleted == "false" )
	            {
	                 var deleteIdentifierTypeError = document.getElementById('deletePersonNameAuthorityIdentifierTypeError');
                    deleteIdentifierTypeError.innerHTML = '<p id="newDeleteIdentifierTypeError">' 
                    + response.message + '</p>';
                    YAHOO.ur.person.name.authority.identifier.type.deleteIdentifierTypeDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the identifier types were deleted
	            	YAHOO.ur.person.name.authority.identifier.type.deleteIdentifierTypeDialog.hide();
	            	YAHOO.ur.person.name.authority.identifier.type.clearDeleteIdentifierTypeForm();
	            }
	            // reload the table
	            myIdentifierTypeTable.submitForm(myIdentifierTypeAction);
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) {
	        alert('delete identifier type submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new identifier type button is clicked.
	    YAHOO.ur.person.name.authority.identifier.type.deleteIdentifierTypeDialog = new YAHOO.widget.Dialog('deletePersonNameAuthorityIdentifierTypeDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
					  { text:'No', handler:handleCancel } ]
		    } );
		
        // center and show the dialog
	    YAHOO.ur.person.name.authority.identifier.type.deleteIdentifierTypeDialog.showDialog = function()
        {
        	YAHOO.ur.person.name.authority.identifier.type.deleteIdentifierTypeDialog.center();
        	YAHOO.ur.person.name.authority.identifier.type.deleteIdentifierTypeDialog.show();        
        }
    
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
			
	    // Render the Dialog
	    YAHOO.ur.person.name.authority.identifier.type.deleteIdentifierTypeDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showDeletePersonNameAuthorityIdentifierType", "click", 
	    		YAHOO.ur.person.name.authority.identifier.type.deleteIdentifierTypeDialog.showDialog, 
	    		YAHOO.ur.person.name.authority.identifier.type.deleteIdentifierTypeDialog, true);
    }, 
    
    init : function()
    {
        YAHOO.ur.person.name.authority.identifier.type.getIdentifierTypes(0, 1, 1, 'asc');
        YAHOO.ur.person.name.authority.identifier.type.createNewIdentifierTypeDialog();
        YAHOO.ur.person.name.authority.identifier.type.createDeleteIdentifierTypeDialog();
    }
    
    
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.person.name.authority.identifier.type.init);