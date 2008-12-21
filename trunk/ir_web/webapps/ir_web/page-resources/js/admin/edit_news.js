
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

YAHOO.namespace("ur.edit.news");



YAHOO.ur.edit.news = 
{
    createPictureUploadDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        // action to perform when submitting the news items.
            var uploadPictureDialogAction = basePath + 'admin/uploadNewsPicture.action';
	        YAHOO.util.Connect.setForm('addNewsPicture', true, true);
	    	    
	        if( YAHOO.ur.edit.news.uploadNewsPictureDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new news item) based on the action.
                var cObj = YAHOO.util.Connect.asyncRequest('post',
                     uploadPictureDialogAction, callback);
           
            }
	    };
	
	    // handle a cancel of the adding news item dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.edit.news.uploadNewsPictureDialog.hide();
	        YAHOO.ur.edit.news.clearUploadPictureForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        var response = o.responseText;
	        var uploadForm = document.getElementById('upload_form_fields');
	    
	        // update the form fields with the response.  This updates
	        // the form, if there was an issue, update the form with
	        // the error messages.
	        uploadForm.innerHTML = o.responseText;
	    
	    	// determine if the add/edit was a success
	        var success = document.getElementById("picture_added").value;
	        var itemId = document.getElementById("news_item_id").value;
	  
	        //if the content type was not added then show the user the error message.
	        // received from the server
	        if( success == "false" )
	        {
                YAHOO.ur.edit.news.uploadNewsPictureDialog.showDialog();
	        }
	        else
	        {
	            // we can clear the upload form and get the pictures
	            YAHOO.ur.edit.news.uploadNewsPictureDialog.hide();
	            YAHOO.ur.edit.news.clearUploadPictureForm();
	            YAHOO.ur.edit.news.getNewsPictures(itemId);
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert('Picture upload submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new news item button is clicked.
	    YAHOO.ur.edit.news.uploadNewsPictureDialog = new YAHOO.widget.Dialog('uploadNewsPictureDialog', 
        { width : "600px",
		  visible : false, 
		  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					  { text:'Cancel', handler:handleCancel } ]
		} );
		
   
 	    // Validate the entries in the form to require that a name is entered
	    YAHOO.ur.edit.news.uploadNewsPictureDialog.validate = function() 
	    {
	        var fileName = document.getElementById('picture_file_name').value;
	    
		    if (fileName == "" || fileName == null) {
		        alert('A File name must be entered');
			    return false;
		    } 
		    else 
		    {
			    return true;
		    }
		
	    };
	    
	    YAHOO.ur.edit.news.uploadNewsPictureDialog.showDialog = function()
	    {
	        YAHOO.ur.edit.news.uploadNewsPictureDialog.center();
	        YAHOO.ur.edit.news.uploadNewsPictureDialog.show();
	    }
	
	    // Wire up the success and failure handlers
	    var callback = {  upload: handleSuccess, failure: handleFailure };
			
			
	    // Render the Dialog
	    YAHOO.ur.edit.news.uploadNewsPictureDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showUploadPicture", "click", 
	        YAHOO.ur.edit.news.uploadNewsPictureDialog.showDialog, 
	        YAHOO.ur.edit.news.uploadNewsPictureDialog, true);
    },
    
    /**
     * Clear the upload form
     */
    clearUploadPictureForm : function()
    {
        var div = document.getElementById('news_upload_error');
        div.innerHTML = "";
        document.getElementById('picture_file_name').value = "";
	    document.getElementById('primary_picture').checked = "";
    },
    
    /*
     * Get the news pictures 
     */
    getNewsPictures : function(newsItemId)
    {
        // action to get repository pictures
        var getPicturesAction =  basePath + 'admin/getNewsPictures.action';

        // Success action on getting the picture
        var handleSuccess = function(o) 
        {
            var divToUpdate = document.getElementById('news_item_pictures');
            divToUpdate.innerHTML = o.responseText; 
        };
    
        // Faiure action on getting a picture
        var handleFailure = function(o) 
	    {
	        alert('Could not get picture for news ' 
	            + o.status + ' status text ' + o.statusText );
	    };
	
	    //  Get the set of pictures for the repository
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getPicturesAction + '?id='+ newsItemId +
            '&bustcache='+new Date().getTime(), 
            {success: handleSuccess, failure: handleFailure}, null);
    },
    
    /**
     * Confirm with the user to delete a picture
     */
    createPictureDeleteConfirmDialog : function()
    {

        // Define various event handlers for Dialog
	    var handleYes = function() 
	    {
	       	// action to perform when submitting the news items.
            var deleteAction = basePath + 'admin/deleteNewsPicture.action';
	        YAHOO.util.Connect.setForm('deleteNewsPictureForm');
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
                     deleteAction, callback);
 	    };
	    
	    var handleNo = function() 
	    {
		    YAHOO.ur.edit.news.deletePictureDialog.hide();
		    YAHOO.ur.edit.news.clearPicutreDeleteForm();
	    };
	    
	    //handle success
        var handleSuccess = function(o) 
        {
            var divToUpdate = document.getElementById('news_item_pictures');
            divToUpdate.innerHTML = o.responseText; 
            YAHOO.ur.edit.news.deletePictureDialog.hide();
            YAHOO.ur.edit.news.clearPicutreDeleteForm();
        };
    
        // handle failure
        var handleFailure = function(o) 
	    {
	        alert('Could not delete picture for news ' 
	            + o.status + ' status text ' + o.statusText );
	    };
	    
	    // Wire up the success and failure handlers
	    var callback = {  success: handleSuccess, failure: handleFailure };
	    
	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new news item button is clicked.
	    YAHOO.ur.edit.news.deletePictureDialog = new YAHOO.widget.Dialog('deletePictureDialog', 
         { width : "500px",
		   visible : false, 
		   buttons: [ { text:"Yes", handler:handleYes, isDefault:true },
					  { text:"No",  handler:handleNo } ]
		  } );
		  
        YAHOO.ur.edit.news.deletePictureDialog.showDialog = function()
        {
            YAHOO.ur.edit.news.deletePictureDialog.center();
	        YAHOO.ur.edit.news.deletePictureDialog.show();
        };
        
	    // Render the Dialog and show it
	    YAHOO.ur.edit.news.deletePictureDialog.render();
    },
 
    /**
     * Confirm the delete of a selected picture
     */
    confirmPictureDelete : function(itemId, irFileId, primaryPicture)
    {
        document.getElementById("delete_news_item_id").value = itemId;
        document.getElementById("delete_picture_id").value = irFileId;
        document.getElementById("delete_primary_news_picture").value = primaryPicture;
        YAHOO.ur.edit.news.deletePictureDialog.showDialog();
    },
    
    /**
     * Clear the picture delete form
     */
    clearPicutreDeleteForm : function()
    {
        document.getElementById("delete_news_item_id").value = "";
        document.getElementById("delete_picture_id").value = "";
        document.getElementById("delete_primary_news_picture").value = "";
    },
    
    /**
     * Initialization on the page
     */
    init : function()
    {
    	// create tabs for editing news
        var myTabs = new YAHOO.widget.TabView("edit-news-tabs");
        
        //The SimpleEditor config
        var myConfig = {
                height: '500px',
                width: '700px',
                dompath: true,
                focusAtStart: true,
                handleSubmit: true
        };

        //Now let's load the SimpleEditor..
        var myEditor = new YAHOO.widget.Editor('msgpost', myConfig);
        myEditor.render();
            
        YAHOO.ur.edit.news.createPictureDeleteConfirmDialog();
        YAHOO.ur.edit.news.createPictureUploadDialog();
    }
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.edit.news.init);