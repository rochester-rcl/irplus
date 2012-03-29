/**
 * This code is for dealing with editing researcher
 */
YAHOO.namespace("ur.edit.project_page_image");


/**
 * Edit project page image namespace
 */
YAHOO.ur.edit.project_page_image = 
{
		/*
		 * Get the project page images
		 */
		getImages : function(groupWorksapceProjectPageId)
		{
		  // action to get repository pictures
		   var getImagesAction = 
		       basePath + 'user/getGroupWorkspaceProjectPageImagesTable.action';
		
			var callback =
			{	 
			    success : function(o) 
			    {
				    // check for the timeout - forward user to login page if timeout
		            // occurred
		            if( !urUtil.checkTimeOut(o.responseText) )
		            {       		    
			            var divToUpdate = document.getElementById('group_workspace_project_page_images');
			            divToUpdate.innerHTML = o.responseText; 
			        }
			    },
			    
			    failure : function(o) 
				{
				    alert('Could not get pictures for project page ' 
				        + o.status + ' status text ' + o.statusText );
				}
			}
			
			/*
			 *  Get the set of pictures for the researcher
			 */
		    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
		        getImagesAction + '?groupWorkspaceProjectPageId='+ groupWorksapceProjectPageId +
		        '&bustcache='+new Date().getTime(), callback, null);
		},
		
		 /** 
		  * clear out any form data messages or input
		  * in the upload picture form
		  */
		clearUploadPictureForm : function()
		{
			document.getElementById('image_file_name').value = "";
	        // clear out any errors
	        var div = document.getElementById('upload_error');
	        div.innerHTML = "";
		},

    /**
	 * Creates a YUI new researcher modal dialog for when a user wants to  
	 * upload a picture.
	 *
	 */
	createImageUploadDialog : function()
	{
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
		// handle a cancel of the adding project page item dialog
		var handleCancel = function() 
		{
			YAHOO.ur.edit.project_page_image.uploadProjectPageImageDialog.hide();
			YAHOO.ur.edit.project_page_image.clearUploadImageForm();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       
		        var response = o.responseText;
		        var uploadForm = document.getElementById('upload_form_fields');
		    
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        uploadForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("image_added").value;
		        var groupWorkspaceProjectPageId = document.getElementById("group_workspace_project_page_id").value;
		  
		        //if the content type was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
		        	YAHOO.ur.edit.project_page_image.showDialog();
		        }
		        else
		        {
		            // we can clear the upload form and get the pictures
		        	YAHOO.ur.edit.project_page_image.uploadProjectPageImageDialog.hide();
		        	YAHOO.ur.edit.project_page_image.clearUploadImageForm();
		        	YAHOO.ur.edit.project_page_image.getImages(groupWorkspaceProjectPageId);
		        }
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('Image upload submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new researcher item button is clicked.
		YAHOO.ur.edit.project_page_image.uploadProjectPageImageDialog = 
		   new YAHOO.widget.Dialog('uploadProjectPageImageDialog', 
	        { width : "600px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
		
		//show and center the dialog
		YAHOO.ur.edit.project_page_image.uploadProjectPageImageDialog.showDialog = function()
	    {
			YAHOO.ur.edit.project_page_image.uploadProjectPageImageDialog.center();
			YAHOO.ur.edit.project_page_image.uploadProjectPageImageDialog.show();
	    }
	    
		// Submit the form
		YAHOO.ur.edit.project_page_image.uploadProjectPageImageDialog.submit = function()
	    {
		    // action to perform when submitting the researcher items.
	        var uploadImageDialogAction = basePath + 'user/uploadGroupWorkspaceProjectPageImage.action';
		    YAHOO.util.Connect.setForm('addProjectPageImage', true, true);
		    	    
		    if( YAHOO.ur.edit.project_page_image.uploadProjectPageImageDialog.validate() )
		    {
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	                 uploadImageDialogAction, callback);
	           
	        }	    
	    }
	    
	 	// Validate the entries in the form to require that a name is entered
		YAHOO.ur.edit.project_page_image.uploadProjectPageImageDialog.validate = function() {
		    var fileName = document.getElementById('image_file_name').value;
		    
			if (fileName == "" || fileName == null) {
			    alert('A File name must be entered');
				return false;
			} else {
				return true;
			}
			
		};
	
		// Wire up the success and failure handlers
		var callback = {  upload: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.edit.project_page_image.uploadProjectPageImageDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showUploadImage", "click", 
		    YAHOO.ur.edit.project_page_image.uploadProjectPageImageDialog.showDialog, 
		    YAHOO.ur.edit.project_page_image.uploadProjectPageImageDialog, true);
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the upload picture form
	  */
	clearUploadImageForm : function()
	{
		document.getElementById('image_file_name').value = "";
		
        // clear out any errors
        var div = document.getElementById('upload_error');
        div.innerHTML = "";
	},
	
	/*
	 * Delete the selected picture
	 */
	deleteImage : function(groupWorkspaceProjectPageId, imageId)
	{
	   // action to get repository pictures
	   var deletePictureAction = 
	       basePath + 'user/deleteGroupWorkspaceProjectPageImage.action';
		
		var callback = 
		{
		    success : function(o) 
		    {
			    // check for the timeout - forward user to login page if timeout
	            // occurred
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
		            var divToUpdate = document.getElementById('group_workspace_project_page_images');
		            divToUpdate.innerHTML = o.responseText;
		        } 
		    },
		    
		    failure : function(o) 
			{
		    	alert(o.responseText);
			    alert('Could not delete group workspace project page image ' 
			        + o.status + ' status text ' + o.statusText );
			}
		}		

		/*
		 *  Post the delete for the set of pictures
		 */
	    var transaction = YAHOO.util.Connect.asyncRequest('POST', 
	        deletePictureAction, callback, 'imageId='+ imageId + 
	        '&groupWorkspaceProjectPageId='+ groupWorkspaceProjectPageId);
	},
	
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() {
		YAHOO.ur.edit.project_page_image.createImageUploadDialog();
	}
}
//initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.edit.project_page_image.init);