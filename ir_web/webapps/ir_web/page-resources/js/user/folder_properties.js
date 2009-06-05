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
 * This code is for dealing with display of folder properties
 */
YAHOO.namespace("ur.folder.properties");

var getFolderPropertiesAction = basePath + 'user/getPersonalFolderProperties.action';

var updateFolderAction = basePath + 'user/updatePersonalFolder.action';
var getFolderAction = basePath + 'user/getPersonalFolder.action';


YAHOO.ur.folder.properties = 
{
 
    /** get the folder properties for the specified folder */
    getFolderProperties : function(folderId)
    {
        var callback = 
        {
            success : function(o)
            {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {                   
                    var div = document.getElementById('folder_properties');
                    div.innerHTML = o.responseText;
                }
            },
            
            failure : function(o)
            {
                alert( "failure to get properties "  + o.status + " status text " + o.statusText);
            }
        };
            
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getFolderPropertiesAction + '?personalFolderId=' + folderId + 
            '&bustcache='+new Date().getTime(), callback, null);
    },
     
   /**
     * Clear the folder form
     */
    clearFolderForm : function()
    {
        // clear out the error message
        var folderError = document.getElementById('folder_error_div');
        folderError.innerHTML = "";    
	
	    document.newFolderForm.folderName.value = "";
	    document.newFolderForm.folderDescription.value = "";
	    document.newFolderForm.newFolder.value = "true";
	    document.newFolderForm.updateFolderId.value = "";
    
    },
    
    /**
     * Dialog to create new folders
     */
    createNewFolderDialog : function()
    {
	    // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
            this.submit();
	    };
		
	    // handle a cancel of the adding folder dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.folder.properties.clearFolderForm();
	        YAHOO.ur.folder.properties.newFolderDialog.hide();
	    };
	
	   // handle a successful return
	   var handleSuccess = function(o) 
	   {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	   
	            //get the response from adding a folder
	            var response = o.responseText;
	            var folderForm = document.getElementById('newFolderDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            folderForm.innerHTML = response;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newFolderForm_success").value;
	  
	            //if the affiliation was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.folder.properties.newFolderDialog.showFolder();
	            }
	            else
	            {
	                // we can clear the form if the folder was added
	                YAHOO.ur.folder.properties.newFolderDialog.hide();
	                YAHOO.ur.folder.properties.clearFolderForm();
	                var folderId = document.getElementById("personalFolderId").value;
	                YAHOO.ur.folder.properties.getFolderProperties(folderId);
	            }
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert("Workspace submission failed due to a network issue: " + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new folder button is clicked.
	    YAHOO.ur.folder.properties.newFolderDialog = new YAHOO.widget.Dialog('newFolderDialog', 
        { width : "600px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
					  { text:"Cancel", handler:handleCancel } ]
		} );
	
	    // override the submit
	    YAHOO.ur.folder.properties.newFolderDialog.submit = function()
	    {
	        YAHOO.util.Connect.setForm('newFolderForm');
	    
	        if( YAHOO.ur.folder.properties.newFolderDialog.validate() )
	        {
                var cObj = YAHOO.util.Connect.asyncRequest('POST',
                updateFolderAction, callback);
            }
	    };
	    
	    YAHOO.ur.folder.properties.newFolderDialog.showFolder = function()
	    {
	        YAHOO.ur.folder.properties.newFolderDialog.center();
	        YAHOO.ur.folder.properties.newFolderDialog.show();
	    };
   
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.folder.properties.newFolderDialog.validate = function() {
	        var data = this.getData();
		    if (data.folderName == "" ) 
		    {
		        alert("A folder name must be entered");
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
	    YAHOO.ur.folder.properties.newFolderDialog.render();
    },
    
    /**
     * Function to edit folder information
     */
    editFolder : function(id)
    {
	    /*
         * This call back updates the html when editing the folder
         */
        var callback =
        {
            success: function(o) 
            {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {                   
                    var divToUpdate = document.getElementById('newFolderDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                    document.newFolderForm.newFolder.value = "false";
                    YAHOO.ur.folder.properties.newFolderDialog.showFolder();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Edit Folder Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getFolderAction + '?updateFolderId=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
                	
	},

   init : function()
    {
       YAHOO.ur.folder.properties.createNewFolderDialog();

    }
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.folder.properties.init);