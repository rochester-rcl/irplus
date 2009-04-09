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
 * This code is for dealing with adding and removing file servers
 */
YAHOO.namespace("ur.file.server");

// action to perform when submitting the file servers.
var myFileServerAction = basePath + 'admin/getAllFileServer.action';

// actions for adding, editing and removing file servers
var updateFileServerAction = basePath + 'admin/updateFileServer.action';
var newFileServerAction = basePath + 'admin/createFileServer.action';
var deleteFileServerAction = basePath + 'admin/deleteFileServer.action';
var getFileServerAction = basePath + 'admin/getFileServer.action';

// object to hold the specified FileServer data.
var myFileServerTable = new YAHOO.ur.table.Table('myFileServers', 'newFileServers');


YAHOO.ur.file.server = 
{
   /**
    *  Function that retrieves file server
    */
    getFileServers : function ()
    {
        var callback =
        {
            success: function(o) 
            {
                // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
                    var divToUpdate = document.getElementById('fileServers');
                    divToUpdate.innerHTML = o.responseText; 
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get file servers Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            myFileServerAction + '?bustcache='+new Date().getTime(),  
            callback, null);
    }, 
    
    /**
     *  clear out the file server form data
     */
    clearFileServerForm : function()
    {
        var errorDiv = document.getElementById('error_div');
        errorDiv.innerHTML = "";
	
	    document.getElementById('newFileServerFormName').value = "";
	    document.getElementById('newFileServerFormDescription').value = "";
	    document.getElementById('newFileServerFormId').value = "";
	    document.addFileServerForm.newFileServerVal.value = "true";
    }, 
    
 
    /**
     * Create a file server dialog box for editing a file server
     */
    createNewFileServerDialog : function()
    {
        // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	    	this.submit();
	    };
	
	    // handle a cancel of the adding FileServer dialog
	    var handleCancel = function()
	    {
	        YAHOO.ur.file.server.newFileServerDialog.hide();
	        YAHOO.ur.file.server.clearFileServerForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
                //get the response from adding a FileServer
	            var response = o.responseText;
	            var fileServerForm = document.getElementById('newFileServerDialogFields');
	    
	            // update the form FileServers with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            fileServerForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newFileServerFormSuccess").value;
	  
	            //if the FileServer was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.file.server.newFileServerDialog.showDialog();
	            }
	            else
	            {
	            	YAHOO.ur.file.server.getFileServers();
	                // we can clear the form if the FileServer was added
	                YAHOO.ur.file.server.newFileServerDialog.hide();
	                YAHOO.ur.file.server.clearFileServerForm();
	            }
	           
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert('file server submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new FileServer button is clicked.
	    YAHOO.ur.file.server.newFileServerDialog = new YAHOO.widget.Dialog('newFileServerDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					     { text:'Cancel', handler:handleCancel } ]
		    } );
	
	    // show and center the FileServer dialog
        YAHOO.ur.file.server.newFileServerDialog.showDialog = function()
        {
            YAHOO.ur.file.server.newFileServerDialog.center();
            YAHOO.ur.file.server.newFileServerDialog.show();
        };
        
        // override the submit function
        YAHOO.ur.file.server.newFileServerDialog.submit = function()
        {
        	YAHOO.util.Connect.setForm('addFileServerForm');
	        if( YAHOO.ur.file.server.newFileServerDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new FileServer) based on the action.
               var action = newFileServerAction;
	           if( document.addFileServerForm.newFileServerVal.value != 'true')
	           {
	        	   
	               action = updateFileServerAction;
	           }

                var cObj = YAHOO.util.Connect.asyncRequest('post',
                action, callback);
            }
        	
        };
    
 	    // Validate the entries in the form to require server name is entered
	    YAHOO.ur.file.server.newFileServerDialog.validate = function()
        {
	        var name = document.getElementById('newFileServerFormName').value;
		    if (name == "" || name == null) 
		    {
		        alert('A File Server name must be entered');
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
	    YAHOO.ur.file.server.newFileServerDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showNewFileServer", "click", 
	        YAHOO.ur.file.server.newFileServerDialog.showDialog, 
	        YAHOO.ur.file.server.newFileServerDialog, true);
	},
    
 
    /**
     * function to edit File Server information
     */
    edit : function(id)
    {	    
	    /*
         * This call back updates the html when a editing a FileServer
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('newFileServerDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                    document.addFileServerForm.newFileServerVal.value = "false";
	                YAHOO.ur.file.server.newFileServerDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get FileServer failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getFileServerAction + '?fileServerId=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
     },
    
    /**
     * Clear the delete fine
     */
    clearDeleteFileServerForm : function()
    {
        var fileServerError = document.getElementById('newDeleteFileServerError');
        if( fileServerError != null)
        {
            fileServerError.innerHTML = "";
        }
    },
    
    /**
     * Create the delete dialog
     */
    createDeleteFileServerDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	    	this.submit();
	    };
		
	    // handle a cancel of deleting FileServer dialog
	    var handleCancel = function() {
	        YAHOO.ur.file.server.deleteFileServerDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            //get the response from adding a FileServer
	            var response = eval("("+o.responseText+")");
	            //if the FileServer was not deleted then show the user the error message.
	            // received from the server
	            if( response.fileServerDeleted == "false" )
	            {
	                var deleteFileServerError = document.getElementById('deleteFileServerError');
                    deleteFileServerError.innerHTML = '<p id="newDeleteFileServerError">' 
                    + response.message + '</p>';
                    YAHOO.ur.file.server.deleteFileServerDialog.showDialog();
	            }
	            else
	            {
	            	document.getElementById('deleteFileServerId').value = "";
	            	YAHOO.ur.file.server.getFileServers();
	                // we can clear the form if the FileServers were deleted
	                YAHOO.ur.file.server.deleteFileServerDialog.hide();
	                YAHOO.ur.file.server.clearDeleteFileServerForm();
	            }
	           
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	        alert('File Server delete submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new FileServer button is clicked.
	    YAHOO.ur.file.server.deleteFileServerDialog = new YAHOO.widget.Dialog('deleteFileServerDialog', 
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
	    YAHOO.ur.file.server.deleteFileServerDialog.showDialog = function()
	    {
	        YAHOO.ur.file.server.deleteFileServerDialog.show();
	        YAHOO.ur.file.server.deleteFileServerDialog.center();
	    };
	    
        // override the submit function
	    YAHOO.ur.file.server.deleteFileServerDialog.submit = function()
        {
        	YAHOO.util.Connect.setForm('deleteFileServer');
        	var cObj = YAHOO.util.Connect.asyncRequest('post',
            		deleteFileServerAction, callback);
          
        };
			
	    // Render the Dialog
	    YAHOO.ur.file.server.deleteFileServerDialog.render();
    }, 
    
   deleteFileServer : function(id)
   {
    	document.getElementById('deleteFileServerId').value = id;
    	YAHOO.ur.file.server.deleteFileServerDialog.showDialog();
   }, 
   
    
    init : function()
    {
        YAHOO.ur.file.server.getFileServers();
        YAHOO.ur.file.server.createNewFileServerDialog();
        YAHOO.ur.file.server.createDeleteFileServerDialog();
    }
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.file.server.init);