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
YAHOO.namespace("ur.file.database.folder");

// action to perform when submitting the file databases.
var myFileDatabaseAction = basePath + 'admin/getAllFileDatabaseFolder.action';

// actions for adding, editing and removing file databases
var updateFileDatabaseFolderAction = basePath + 'admin/updateFileDatabaseFolder.action';
var newFileDatabaseFolderAction = basePath + 'admin/createFileDatabaseFolder.action';
var deleteFileDatabaseFolderAction = basePath + 'admin/deleteFileDatabaseFolder.action';
var getFileDatabaseFolderAction = basePath + 'admin/getFileDatabaseFolder.action';


YAHOO.ur.file.database.folder = 
{
   /**
    *  Function that retrieves file database folders
    */
    getFileDatabaseFolders : function ()
    {
        var callback =
        {
            success: function(o) 
            {
                // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
                    var divToUpdate = document.getElementById('fileDatabaseFolders');
                    divToUpdate.innerHTML = o.responseText; 
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get file database folders Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var fileServerId = document.getElementById("fileDatabaseId").value;
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            myFileDatabaseFolderAction + '?fileDatabaseId=' + fileDatabaseId +'&bustcache='+new Date().getTime(),  
            callback, null);
    }, 
    
    /**
     *  clear out the file database form data
     */
    clearFileDatabaseFolderForm : function()
    {
        var errorDiv = document.getElementById('error_div');
        errorDiv.innerHTML = "";
	
	    document.getElementById('newFileDatabaseFolderFormName').value = "";
	    document.getElementById('newFileDatabaseFolderFormPath').value = "";
	    document.getElementById('newFileDatabaseFolderFormDescription').value = "";
	    document.getElementById('newFileDatabaseFolderFormId').value = "";
	    document.addFileDatabaseFolderForm.newFileDatabaseFolderVal.value = "true";
    }, 
    
 
    /**
     * Create a file database dialog box for editing a file database
     */
    createNewFileDatabaseFolderDialog : function()
    {
        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	    	this.submit();
	    };
	
	    // handle a cancel of the adding FileDatabaseFolder dialog
	    var handleCancel = function()
	    {
	        YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog.hide();
	        YAHOO.ur.file.database.folder.clearFileDatabaseFolderForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
                //get the response from adding a FileDatabaseFolder
	            var response = o.responseText;
	            var fileServerForm = document.getElementById('newFileDatabaseFolderDialogFields');
	    
	            // update the form FileDatabaseFolders with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            fileServerForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newFileDatabaseFolderFormSuccess").value;
	  
	            //if the FileDatabaseFolder was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog.showDialog();
	            }
	            else
	            {
	            	YAHOO.ur.file.database.folder.getFileDatabaseFolders();
	                // we can clear the form if the FileDatabaseFolder was added
	                YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog.hide();
	                YAHOO.ur.file.database.folder.clearFileDatabaseFolderForm();
	            }
	           
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert('file database submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new FileDatabaseFolder button is clicked.
	    YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog = new YAHOO.widget.Dialog('newFileDatabaseFolderDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					     { text:'Cancel', handler:handleCancel } ]
		    } );
	
	    // show and center the FileDatabaseFolder dialog
        YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog.showDialog = function()
        {
            YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog.center();
            YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog.show();
        };
        
        // override the submit function
        YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog.submit = function()
        {
        	YAHOO.util.Connect.setForm('addFileDatabaseFolderForm');
	        if( YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new FileDatabaseFolder) based on the action.
               var action = newFileDatabaseFolderAction;
	           if( document.addFileDatabaseFolderForm.newFileDatabaseFolderVal.value != 'true')
	           {
	        	   
	               action = updateFileDatabaseFolderAction;
	           }

                var cObj = YAHOO.util.Connect.asyncRequest('post',
                action, callback);
            }
        	
        };
    
 	    // Validate the entries in the form to require that file database name is entered
	    YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog.validate = function()
        {
	        var name = document.getElementById('newFileDatabaseFolderFormName').value;
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
	    YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showNewFileDatabaseFolder", "click", 
	        YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog.showDialog, 
	        YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog, true);
	},
    
 
    /**
     * function to edit File Server information
     */
    edit : function(fileDatabaseId, fileDatabaseId)
    {	    
	    /*
         * This call back updates the html when a editing a FileDatabaseFolder
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('newFileDatabaseFolderDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                    document.addFileDatabaseFolderForm.newFileDatabaseFolderVal.value = "false";
	                YAHOO.ur.file.database.folder.newFileDatabaseFolderDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Edit FileDatabaseFolder failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getFileDatabaseFolderAction + '?fileDatabaseId=' + fileDatabaseId +  '&fileFolderId=' + fileFolderId+'&bustcache='+new Date().getTime(), 
            callback, null);
     },
    
    /**
     * Clear the delete fine
     */
    clearDeleteFileDatabaseFolderForm : function()
    {
        var fileServerError = document.getElementById('newDeleteFileDatabaseFolderError');
        if( fileServerError != null)
        {
            fileServerError.innerHTML = "";
        }
    },
    
    /**
     * Create the delete dialog
     */
    createDeleteFileDatabaseFolderDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	    	this.submit();
	    };
		
	    // handle a cancel of deleting FileDatabaseFolder dialog
	    var handleCancel = function() {
	        YAHOO.ur.file.database.folder.deleteFileDatabaseFolderDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            //get the response from adding a FileDatabaseFolder
	            var response = eval("("+o.responseText+")");
	            //if the FileDatabaseFolder was not deleted then show the user the error message.
	            // received from the server
	            if( response.fileServerDeleted == "false" )
	            {
	                var deleteFileDatabaseFolderError = document.getElementById('deleteFileDatabaseFolderError');
                    deleteFileDatabaseFolderError.innerHTML = '<p id="newDeleteFileDatabaseFolderError">' 
                    + response.message + '</p>';
                    YAHOO.ur.file.database.folder.deleteFileDatabaseFolderDialog.showDialog();
	            }
	            else
	            {
	            	document.getElementById('deleteFileDatabaseFolderId').value = "";
	            	YAHOO.ur.file.database.folder.getFileDatabaseFolders();
	                // we can clear the form if the FileDatabaseFolders were deleted
	                YAHOO.ur.file.database.folder.deleteFileDatabaseFolderDialog.hide();
	                YAHOO.ur.file.database.folder.clearDeleteFileDatabaseFolderForm();
	            }
	           
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	        alert('File database delete submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new FileDatabaseFolder button is clicked.
	    YAHOO.ur.file.database.folder.deleteFileDatabaseFolderDialog = new YAHOO.widget.Dialog('deleteFileDatabaseFolderDialog', 
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
	    YAHOO.ur.file.database.folder.deleteFileDatabaseFolderDialog.showDialog = function()
	    {
	        YAHOO.ur.file.database.folder.deleteFileDatabaseFolderDialog.show();
	        YAHOO.ur.file.database.folder.deleteFileDatabaseFolderDialog.center();
	    };
	    
        // override the submit function
	    YAHOO.ur.file.database.folder.deleteFileDatabaseFolderDialog.submit = function()
        {
        	YAHOO.util.Connect.setForm('deleteFileDatabaseFolder');
        	var cObj = YAHOO.util.Connect.asyncRequest('post',
            		deleteFileDatabaseFolderAction, callback);
          
        };
			
	    // Render the Dialog
	    YAHOO.ur.file.database.folder.deleteFileDatabaseFolderDialog.render();
    }, 
    
   deleteFileDatabaseFolder : function(id)
   {
    	document.getElementById('deleteFileDatabaseFolderId').value = id;
    	YAHOO.ur.file.database.folder.deleteFileDatabaseFolderDialog.showDialog();
   }, 
   
    
    init : function()
    {
        YAHOO.ur.file.database.folder.createNewFileDatabaseFolderDialog();
        YAHOO.ur.file.database.folder.createDeleteFileDatabaseFolderDialog();
    }
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.file.database.folder.init);