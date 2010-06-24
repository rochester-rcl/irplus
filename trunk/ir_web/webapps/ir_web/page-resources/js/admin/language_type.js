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
YAHOO.namespace("ur.language.type");

// action to perform when submitting the personal language types.
var myLanguageTypeAction = basePath + 'admin/getLanguageTypes.action';

// actions for adding and removing folders
var updateLanguageTypeAction = basePath + 'admin/updateLanguageType.action';
var newLanguageTypeAction = basePath + 'admin/createLanguageType.action';
var deleteLanguageTypeAction = basePath + 'admin/deleteLanguageType.action';
var getLanguageTypeAction = basePath + 'admin/getLanguageType.action';


// object to hold the specified language type data.
var myLanguageTypeTable = new YAHOO.ur.table.Table('myLanguageTypes', 'newLanguageTypes');

YAHOO.ur.language.type = {
   
   /**
    *  Function that retireves folder information
    *  based on the given folder id.
    *
    *  The folder id used to get the folder.
    */
    getLanguageTypes : function (rowStart, startPageNumber, currentPageNumber, order)
    {
    
        /*
         * This call back updates the html when a new language type is
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
                    var divToUpdate = document.getElementById('newLanguageTypes');
                    divToUpdate.innerHTML = o.responseText; 
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get language type Failure ' + o.status + ' status text ' + o.statusText );
	        }
        }
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            myLanguageTypeAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(),
            callback, null);
    },
    
    // function to clear language types
    clearLanguageTypeForm : function ()
    {
        var div = document.getElementById('languageTypeError');
        div.innerHTML = "";
	    
	    document.getElementById('newLanguageTypeForm_name').value="";
	    document.getElementById('newLanguageTypeForm_description').value="";
	    document.getElementById('newLanguageTypeForm_languageTypeId').value="";
	    document.getElementById('newLanguageTypeForm_639_2').value="";
	    document.getElementById('newLanguageTypeForm_639_1').value="";

	    document.newLanguageType.newLanguageType.value = "true";
    }, 
    
    /**
     * set the checkboxes
     */
    setCheckboxes : function()
    {
        checked = document.myLanguageTypes.checkAllSetting.checked;
        var languageIds = document.getElementsByName('languageTypeIds');
        urUtil.setCheckboxes(languageIds, checked);
    }, 
    
    
       /**
     * Creates a YUI new language type modal 
     * dialog for when a user wants to create 
     * a new language type
     *
     */
    createNewLanguageTypeDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        this.submit();
	    };
		
	    // handle a cancel of the adding language type dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.language.type.languageTypeDialog.hide();
	        YAHOO.ur.language.type.clearLanguageTypeForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            //get the response from adding a language type
	            var response = eval("("+o.responseText+")");
	    
	            //if the language type was not added then show the user the error message.
	            // received from the server
	            if( response.languageTypeAdded == "false" )
	            {
	                var languageTypeNameError = document.getElementById('languageTypeError');
                    languageTypeNameError.innerHTML = '<p id="newLanguageTypeForm_nameError">' + response.message + '</p>';
                    YAHOO.ur.language.type.languageTypeDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the language type was added
	                YAHOO.ur.language.type.languageTypeDialog.hide();
	                YAHOO.ur.language.type.clearLanguageTypeForm();
	            }
	            myLanguageTypeTable.submitForm(myLanguageTypeAction);
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('language type submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new language type button is clicked.
	    YAHOO.ur.language.type.languageTypeDialog = new YAHOO.widget.Dialog('newLanguageTypeDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					      { text:'Cancel', handler:handleCancel } ]
		    } );
		
        // center and show the dialog box
        YAHOO.ur.language.type.languageTypeDialog.showDialog = function()
        {
            YAHOO.ur.language.type.languageTypeDialog.center();
            YAHOO.ur.language.type.languageTypeDialog.show();
        };
        
        // override the submit of the dialog
        YAHOO.ur.language.type.languageTypeDialog.submit = function()
        {
        	YAHOO.ur.util.wait.waitDialog.showDialog();
            YAHOO.util.Connect.setForm('newLanguageType');
	        if( YAHOO.ur.language.type.languageTypeDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new language type) based on the action.
                var action = newLanguageTypeAction;
	            if( document.newLanguageType.newLanguageType.value != 'true')
	            {
	                action = updateLanguageTypeAction;
	            }

                var cObj = YAHOO.util.Connect.asyncRequest('post',
                action, callback);
            }
        };
    
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.language.type.languageTypeDialog.validate = function() 
	    {
	        name = document.getElementById('newLanguageTypeForm_name');
		    if (name == "" || name == null) {
		        alert('A Language type name must be entered');
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
	    YAHOO.ur.language.type.languageTypeDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showLanguageType", "click", 
	        YAHOO.ur.language.type.languageTypeDialog.showDialog, 
	        YAHOO.ur.language.type.languageTypeDialog, true);
    
    }, 
    
    /**
     * function to edit language type information
     */
    editLanguageType : function(id)
    {	    
	    /*
         * This call back updates the html when a editing a language type
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('languageTypeForm');
                    divToUpdate.innerHTML = o.responseText; 
                    document.newLanguageType.newLanguageType.value = "false";
	                YAHOO.ur.language.type.languageTypeDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Edit language type failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getLanguageTypeAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
    },
    
    
    /**
     * Clear out the form
     */
    clearDeleteLanguageTypeForm : function()
    {
        var div = document.getElementById('languageTypeError');
	    div.innerHTML = "";
    },
    
    /**
     * Create the delete form
     */
    createDeleteLanguageTypeDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('myLanguageTypes');
	    
	        //delete the language type
            var cObj = YAHOO.util.Connect.asyncRequest('post', deleteLanguageTypeAction, callback);
	    };
	
		
	    // handle a cancel of deleting language type dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.language.type.deleteLanguageTypeDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            //get the response from adding a language type
	            var response = eval("("+o.responseText+")");
	    
	            //if the language type was not deleted then show the user the error message.
	            // received from the server
	            if( response.languageTypeDeleted == "false" )
	            {
	                var deleteLanguageTypeError = document.getElementById('form_deleteLanguageTypeError');
                    deleteLanguageTypeError.innerHTML = '<p id="newDeleteLanguageTypeError">' 
                    + response.message + '</p>';
                    YAHOO.ur.language.type.deleteLanguageTypeDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the language types were deleted
	                YAHOO.ur.language.type.clearDeleteLanguageTypeForm();
	                YAHOO.ur.language.type.deleteLanguageTypeDialog.hide();
	            }
	            // reload the table
	            myLanguageTypeTable.submitForm(myLanguageTypeAction);
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) {
	        alert('delete language type submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new language type button is clicked.
	    YAHOO.ur.language.type.deleteLanguageTypeDialog = new YAHOO.widget.Dialog('deleteLanguageTypeDialog', 
           { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
					      { text:'No', handler:handleCancel } ]
		    } );
		
        //center and show the dialog
        YAHOO.ur.language.type.deleteLanguageTypeDialog.showDialog = function()
        {
            YAHOO.ur.language.type.deleteLanguageTypeDialog.show();
            YAHOO.ur.language.type.deleteLanguageTypeDialog.center();
        }
    
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.language.type.deleteLanguageTypeDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showDeleteLanguageType", "click", 
	        YAHOO.ur.language.type.deleteLanguageTypeDialog.showDialog, 
	        YAHOO.ur.language.type.deleteLanguageTypeDialog, true);
    },
    
    /**
     * Initalize the page setup the dialog boxes
     */
    init : function()
    {
        YAHOO.ur.language.type.getLanguageTypes(0,1,1,'asc');
        YAHOO.ur.language.type.createNewLanguageTypeDialog();
        YAHOO.ur.language.type.createDeleteLanguageTypeDialog();
    }
 
}


// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.language.type.init);