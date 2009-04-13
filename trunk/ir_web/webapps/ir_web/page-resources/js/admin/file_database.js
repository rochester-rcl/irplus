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
 * This code is for dealing with adding, editing and managing file databases
 */
YAHOO.namespace("ur.file.database");

// action to perform when submitting the file databases.
var myFileDatabaseAction = basePath + 'admin/getAllFileDatabase.action';

// actions for adding, editing and removing file databases
var updateFileDatabaseAction = basePath + 'admin/updateFileDatabase.action';
var newFileDatabaseAction = basePath + 'admin/createFileDatabase.action';
var deleteFileDatabaseAction = basePath + 'admin/deleteFileDatabase.action';
var getFileDatabaseAction = basePath + 'admin/getFileDatabase.action';


YAHOO.ur.file.database = 
{
   /**
    *  Function that retrieves file databases
    */
    getFileDatabases : function ()
    {
        var callback =
        {
            success: function(o) 
            {
                // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
                    var divToUpdate = document.getElementById('fileDatabases');
                    divToUpdate.innerHTML = o.responseText; 
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get file databases Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var fileServerId = document.getElementById("fileServerId").value;
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            myFileDatabaseAction + '?fileServerId=' + fileServerId +'&bustcache='+new Date().getTime(),  
            callback, null);
    }, 
    
    /**
     *  clear out the file database form data
     */
    clearFileDatabaseForm : function()
    {
        var errorDiv = document.getElementById('error_div');
        errorDiv.innerHTML = "";
	
	    document.getElementById('newFileDatabaseFormName').value = "";
	    document.getElementById('newFileDatabaseFormPath').value = "";
	    document.getElementById('newFileDatabaseFormDescription').value = "";
	    document.getElementById('newFileDatabaseFormId').value = "";
	    document.addFileDatabaseForm.newFileDatabaseVal.value = "true";
    }, 
    
 
    /**
     * Create a file database dialog box for editing a file database
     */
    createNewFileDatabaseDialog : function()
    {
        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	    	this.submit();
	    };
	
	    // handle a cancel of the adding FileDatabase dialog
	    var handleCancel = function()
	    {
	        YAHOO.ur.file.database.newFileDatabaseDialog.hide();
	        YAHOO.ur.file.database.clearFileDatabaseForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
                //get the response from adding a FileDatabase
	            var response = o.responseText;
	            var fileServerForm = document.getElementById('newFileDatabaseDialogFields');
	    
	            // update the form FileDatabases with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            fileServerForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newFileDatabaseFormSuccess").value;
	  
	            //if the FileDatabase was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.file.database.newFileDatabaseDialog.showDialog();
	            }
	            else
	            {
	            	YAHOO.ur.file.database.getFileDatabases();
	                // we can clear the form if the FileDatabase was added
	                YAHOO.ur.file.database.newFileDatabaseDialog.hide();
	                YAHOO.ur.file.database.clearFileDatabaseForm();
	            }
	           
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert('file database submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new FileDatabase button is clicked.
	    YAHOO.ur.file.database.newFileDatabaseDialog = new YAHOO.widget.Dialog('newFileDatabaseDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					     { text:'Cancel', handler:handleCancel } ]
		    } );
	
	    // show and center the FileDatabase dialog
        YAHOO.ur.file.database.newFileDatabaseDialog.showDialog = function()
        {
            YAHOO.ur.file.database.newFileDatabaseDialog.center();
            YAHOO.ur.file.database.newFileDatabaseDialog.show();
        };
        
        // override the submit function
        YAHOO.ur.file.database.newFileDatabaseDialog.submit = function()
        {
        	YAHOO.util.Connect.setForm('addFileDatabaseForm');
	        if( YAHOO.ur.file.database.newFileDatabaseDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new FileDatabase) based on the action.
               var action = newFileDatabaseAction;
	           if( document.addFileDatabaseForm.newFileDatabaseVal.value != 'true')
	           {
	        	   
	               action = updateFileDatabaseAction;
	           }

                var cObj = YAHOO.util.Connect.asyncRequest('post',
                action, callback);
            }
        	
        };
    
 	    // Validate the entries in the form to require that file database name is entered
	    YAHOO.ur.file.database.newFileDatabaseDialog.validate = function()
        {
	        var name = document.getElementById('newFileDatabaseFormName').value;
		    if (name == "" || name == null) 
		    {
		        alert('A File Database name must be entered');
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
	    YAHOO.ur.file.database.newFileDatabaseDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showNewFileDatabase", "click", 
	        YAHOO.ur.file.database.newFileDatabaseDialog.showDialog, 
	        YAHOO.ur.file.database.newFileDatabaseDialog, true);
	},
    
 
    /**
     * function to edit File Server information
     */
    edit : function(id)
    {	    
	    /*
         * This call back updates the html when a editing a FileDatabase
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('newFileDatabaseDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                    document.addFileDatabaseForm.newFileDatabaseVal.value = "false";
	                YAHOO.ur.file.database.newFileDatabaseDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get FileDatabase failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getFileDatabaseAction + '?fileServerId=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
     },
    
    /**
     * Clear the delete fine
     */
    clearDeleteFileDatabaseForm : function()
    {
        var fileServerError = document.getElementById('newDeleteFileDatabaseError');
        if( fileServerError != null)
        {
            fileServerError.innerHTML = "";
        }
    },
    
    /**
     * Create the delete dialog
     */
    createDeleteFileDatabaseDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	    	this.submit();
	    };
		
	    // handle a cancel of deleting FileDatabase dialog
	    var handleCancel = function() {
	        YAHOO.ur.file.database.deleteFileDatabaseDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            //get the response from adding a FileDatabase
	            var response = eval("("+o.responseText+")");
	            //if the FileDatabase was not deleted then show the user the error message.
	            // received from the server
	            if( response.fileServerDeleted == "false" )
	            {
	                var deleteFileDatabaseError = document.getElementById('deleteFileDatabaseError');
                    deleteFileDatabaseError.innerHTML = '<p id="newDeleteFileDatabaseError">' 
                    + response.message + '</p>';
                    YAHOO.ur.file.database.deleteFileDatabaseDialog.showDialog();
	            }
	            else
	            {
	            	document.getElementById('deleteFileDatabaseId').value = "";
	            	YAHOO.ur.file.database.getFileDatabases();
	                // we can clear the form if the FileDatabases were deleted
	                YAHOO.ur.file.database.deleteFileDatabaseDialog.hide();
	                YAHOO.ur.file.database.clearDeleteFileDatabaseForm();
	            }
	           
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	        alert('File database delete submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new FileDatabase button is clicked.
	    YAHOO.ur.file.database.deleteFileDatabaseDialog = new YAHOO.widget.Dialog('deleteFileDatabaseDialog', 
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
	    YAHOO.ur.file.database.deleteFileDatabaseDialog.showDialog = function()
	    {
	        YAHOO.ur.file.database.deleteFileDatabaseDialog.show();
	        YAHOO.ur.file.database.deleteFileDatabaseDialog.center();
	    };
	    
        // override the submit function
	    YAHOO.ur.file.database.deleteFileDatabaseDialog.submit = function()
        {
        	YAHOO.util.Connect.setForm('deleteFileDatabase');
        	var cObj = YAHOO.util.Connect.asyncRequest('post',
            		deleteFileDatabaseAction, callback);
          
        };
			
	    // Render the Dialog
	    YAHOO.ur.file.database.deleteFileDatabaseDialog.render();
    }, 
    
   deleteFileDatabase : function(id)
   {
    	document.getElementById('deleteFileDatabaseId').value = id;
    	YAHOO.ur.file.database.deleteFileDatabaseDialog.showDialog();
   }, 
   
    
    init : function()
    {
        YAHOO.ur.file.database.createNewFileDatabaseDialog();
        YAHOO.ur.file.database.createDeleteFileDatabaseDialog();
    }
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.file.database.init);