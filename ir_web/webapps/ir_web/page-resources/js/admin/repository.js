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
YAHOO.namespace("ur.repository");

YAHOO.ur.repository = 
{
    // confirm deleting a picture
    confirmPictureDelete : function(irFileId)
    {
        document.getElementById('picture_id').value = irFileId;
        YAHOO.ur.repository.deleteRepositoryPictureDialog.showDialog();
    },
    
    // creae the picture dialog
    createPictureDeleteConfirmDialog : function()
    {
        // action to get repository pictures
        var deletePictureAction = 
           basePath + 'admin/deleteRepositoryPicture.action'; 
           
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('delete_repository_picture');
	    
	        //delete the news item 
            var cObj = YAHOO.util.Connect.asyncRequest('post',
            deletePictureAction, callback);
	    };
	
	    // handle a cancel of deleting news item dialog
	    var handleCancel = function() {
	         YAHOO.ur.repository.deleteRepositoryPictureDialog.hide();
	    };
        
        //success action
        var handleSuccess = function(o) 
        {
            YAHOO.ur.repository.deleteRepositoryPictureDialog.hide();
            var divToUpdate = document.getElementById('repository_pictures');
            divToUpdate.innerHTML = o.responseText; 
        };
    
        // Faiure action on getting a picture
        var handleFailure = function(o) 
	    {
	        alert('Could not get delete picture for repository ' 
	            + o.status + ' status text ' + o.statusText );
	    };
	
		// Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new news item button is clicked.
	    YAHOO.ur.repository.deleteRepositoryPictureDialog = new YAHOO.widget.Dialog('deletePictureDialog', 
        { width : "500px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
					  { text:'No', handler:handleCancel } ]
		 } );
		 
		 // cneter and show the dialog
		 YAHOO.ur.repository.deleteRepositoryPictureDialog.showDialog = function()
		 {
		     YAHOO.ur.repository.deleteRepositoryPictureDialog.center();
		     YAHOO.ur.repository.deleteRepositoryPictureDialog.show();
		 };
		 
	   // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
		
		// Render the Dialog
	    YAHOO.ur.repository.deleteRepositoryPictureDialog.render();
    },
    
    // init the page
    init : function()
    {
        YAHOO.ur.repository.createPictureDeleteConfirmDialog();
    }

};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.repository.init);






