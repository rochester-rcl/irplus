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
 * This code is for dealing with editing intitutional collections
 */
YAHOO.namespace("ur.edit.institution.collection");

YAHOO.ur.edit.institution.collection = {

    /**
     * Creates a YUI new collection modal dialog for when a user wants to  
     * upload a picture.
     *
     */
    createPictureUploadDialog : function()
    {
    	 
	    // Define various event handlers for Dialog
	    var submit = function() 
	    {
	    	YAHOO.ur.edit.institution.collection.uploadCollectionPictureDialog.hide();
	    	YAHOO.ur.util.wait.waitDialog.showDialog();
	        this.submit();
	    };
	
	    // handle a cancel of the adding collection item dialog
	    var cancel = function() 
	    {
	        YAHOO.ur.edit.institution.collection.uploadCollectionPictureDialog.hide();
	        YAHOO.ur.edit.institution.collection.clearUploadPictureForm();
	    };
	
	    var success = function(o) 
	    {
	    
	    	YAHOO.ur.util.wait.waitDialog.hide();
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var response = o.responseText;
	            var uploadForm = document.getElementById('upload_form_fields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            uploadForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("picture_added").value;
	            var collectionId = document.getElementById("collection_id").value;
	  
	            //if the content type was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.edit.institution.collection.uploadCollectionPictureDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the upload form and get the pictures
	                
	                YAHOO.ur.edit.institution.collection.clearUploadPictureForm();
	                YAHOO.ur.edit.institution.collection.getCollectionPictures(collectionId);
	            } 
	        }
	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('Picture upload submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new collection item button is clicked.
	    YAHOO.ur.edit.institution.collection.uploadCollectionPictureDialog = 
	       new YAHOO.widget.Dialog('uploadCollectionPictureDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:submit, isDefault:true },
					      { text:'Cancel', handler:cancel } ]
		    } );
	
	    //show and center the dialog
        var showDialog = function()
        {
            YAHOO.ur.edit.institution.collection.uploadCollectionPictureDialog.center();
            YAHOO.ur.edit.institution.collection.uploadCollectionPictureDialog.show();
        }
    
 	    // Validate the entries in the form to require that a name is entered
	    YAHOO.ur.edit.institution.collection.uploadCollectionPictureDialog.validate = function() 
	    {
	        var fileName = document.getElementById('picture_file_name').value;
	    
		    if (fileName == "" || fileName == null) {
		        alert('A File name must be entered');
			    return false;
		    } else {
			    return true;
		    }
	    };
	    
	    // override the submit function
	    YAHOO.ur.edit.institution.collection.uploadCollectionPictureDialog.submit = function()
	    {
	        // action to perform when submitting the collection items.
            var uploadPictureDialogAction = basePath + 'admin/uploadInstitutionalCollectionPicture.action';
	        YAHOO.util.Connect.setForm('addCollectionPicture', true, true);
	    	    
	        if( YAHOO.ur.edit.institution.collection.uploadCollectionPictureDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new collection item) based on the action.
                var cObj = YAHOO.util.Connect.asyncRequest('POST',
                     uploadPictureDialogAction, 
                     callback);
            }
	    };

	    // Wire up the success and failure handlers
	    var callback =  {  upload: success, failure: failure  };
			
	    // Render the Dialog
	    YAHOO.ur.edit.institution.collection.uploadCollectionPictureDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showUploadPicture", "click", 
	        showDialog, 
	        YAHOO.ur.edit.institution.collection.uploadCollectionPictureDialog, true);
    },

    /** 
     * clear out any form data messages or input
     * in the upload picture form
     */
    clearUploadPictureForm : function()
    {
	    document.getElementById('picture_file_name').value = "";
	    document.getElementById('primary_picture').checked = "";
	
        var div = document.getElementById('collection_upload_error');
        div.innerHTML = "";
	    
    },

    /*
     * Gets the pictures after a modification has occured
     */
    deletePicture : function(collectionId, irFileId, primaryPicture)
    {
    	YAHOO.ur.util.wait.waitDialog.showDialog();
       // action to get repository pictures
       var deletePictureAction = 
           basePath + 'admin/deleteInstitutionalCollectionPicture.action';

       // Success action on getting the picture
       var getPicturesSuccess = function(o) 
       {
    	    YAHOO.ur.util.wait.waitDialog.hide();
           	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
               var divToUpdate = document.getElementById('collection_pictures');
                divToUpdate.innerHTML = o.responseText;
            } 
       };
    
       // Faiure action on getting a picture
       var getPicturesFailure = function(o) 
	   {
    	   YAHOO.ur.util.wait.waitDialog.hide();
	        alert('Could not delete picture for collection ' 
	            + o.status + ' status text ' + o.statusText );
	   };
	
	   //  Delete the picture from the repository
       var transaction = YAHOO.util.Connect.asyncRequest('GET', 
           deletePictureAction + '?pictureId='+ irFileId + 
           '&collectionId='+ collectionId +
           '&primaryCollectionPicture='+primaryPicture +
           '&bustcache='+new Date().getTime(), 
           {success: getPicturesSuccess, failure: getPicturesFailure}, null);
    },

    /*
     * Get the institutional collection pictures 
     */
     getCollectionPictures : function(collectionId)
     {
         // action to get repository pictures
         var getPicturesAction = 
           basePath + 'admin/getInstitutionalCollectionPictures.action';

        // Success action on getting the picture
        var getPicturesSuccess = function(o) 
        {
            // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
                var divToUpdate = document.getElementById('collection_pictures');
                divToUpdate.innerHTML = o.responseText; 
            }
        };
    
        //  Faiure action on getting a picture
        var getPicturesFailure = function(o) 
	    {
	        alert('Could not delete picture for collection ' 
	            +  o.status + ' status text ' + o.statusText );
	    };
	
	    //  Get the set of pictures for the institutional collection
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getPicturesAction + '?collectionId='+ collectionId +
            '&bustcache='+new Date().getTime(), 
            {success: getPicturesSuccess, failure: getPicturesFailure}, null);
    },


    /** create a dialog to confirm the deletion of pictures */
    createPictureDeleteConfirmDialog : function(collectionId, 
        irFileId, 
        primaryPicture)
    {
        // Define various event handlers for Dialog
	    var handleYes = function() 
	    {
		    YAHOO.ur.edit.institution.collection.deletePicture(collectionId, irFileId, primaryPicture);
		    this.hide();
	    };
	    
	    var handleNo = function() 
	    {
		    this.hide();
	    };

	    // Instantiate the Dialog
	    YAHOO.ur.edit.institution.collection.deletePictureDialog = 
	        new YAHOO.widget.SimpleDialog("deletePictureDialog", 
									     { width: "500px",
										   visible: false,
										   close: true,
										   text: "Do you want to delete the selected picture?",
										   icon: YAHOO.widget.SimpleDialog.ICON_HELP,
										   buttons: [ { text:"Yes", handler:handleYes, isDefault:true },
													  { text:"No",  handler:handleNo } ]
										} );
	
	    YAHOO.ur.edit.institution.collection.deletePictureDialog.setHeader("Delete?");
	
	    // show and center the dialog
	    YAHOO.ur.edit.institution.collection.deletePictureDialog.showDialog = function()
	    {
	        YAHOO.ur.edit.institution.collection.deletePictureDialog.center();
	        YAHOO.ur.edit.institution.collection.deletePictureDialog.show();
	    }
	
	    // Render the Dialog
	    YAHOO.ur.edit.institution.collection.deletePictureDialog.render("deletePictureDiv");
    },

    /** creates the confirm dialog and shows it for the specified id */
    confirmPictureDelete : function(itemId, irFileId, primaryPicture)
    {
        YAHOO.ur.edit.institution.collection.createPictureDeleteConfirmDialog(itemId, irFileId, primaryPicture);
        YAHOO.ur.edit.institution.collection.deletePictureDialog.showDialog();
    },

    /** create a message dialog */
    errorDialog : function() {
 
        // Define various event handlers for Dialog
	    var handleYes = function() {
		    var contentArea = document.getElementById('default_error_dialog_content');
	        contentArea.innerHTML = ""; 
	        this.hide();
	    };
	

	    // Instantiate the Dialog
	    YAHOO.ur.edit.institution.collection.errorDialog = 
	        new YAHOO.widget.Dialog("error_dialog_box", 
									     { width: "600px",
										   visible: false,
										   modal: true,
										   close: false,										   
										   buttons: [ { text:"Ok", handler:handleYes, isDefault:true } ]
										} );
	
	    YAHOO.ur.edit.institution.collection.errorDialog.setHeader("Error");
	
	    // show and center the dialog
	    YAHOO.ur.edit.institution.collection.errorDialog.showDialog = function()
	    {
	        YAHOO.ur.edit.institution.collection.errorDialog.center();
	        YAHOO.ur.edit.institution.collection.errorDialog.show();
	    }
	
	    // Render the Dialog
	    YAHOO.ur.edit.institution.collection.errorDialog.render();
    },

    /*
     * update the institutional collection
     */
    updateCollection : function() 
    {
    	YAHOO.ur.util.wait.waitDialog.showDialog();
 
        // action to perform when updating a collection
        var updateCollection = basePath + 'admin/updateInstitutionalCollection.action';
 
	    YAHOO.util.Connect.setForm('base_collection_information');
	
	    // handle a successful return
	    var success = function(o) 
	    { 
	    	YAHOO.ur.util.wait.waitDialog.hide();
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            // received from the server
	            var response = o.responseText;
	            var contentArea = document.getElementById('default_error_dialog_content');
	            contentArea.innerHTML = o.responseText; 
	            var success = document.getElementById('base_collection_updated').value;
	    
	            //if the collection was not added then show the user the error message.
	            if( success == "false" )
	            {
	                var collectionName = document.getElementById('current_collection_name').value;
	                var collectionDescription = document.getElementById('current_collection_description').value;
	                document.getElementById('collection_name').value = collectionName;
	                document.getElementById('collection_description').value = collectionDescription;
                    YAHOO.ur.edit.institution.collection.errorDialog.showDialog();
	            }
	           else
	           {
	               contentArea.innerHTML = "";
	           }
	        }
	    };
	
	    // handle form sbumission failure
	    var failure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert("Save collection failed "  + o.status + ' status text ' + o.statusText);
	    };
	    
	    // Wire up the success and failure handlers
	    var callback = { success: success, failure: failure };
		                       		
        var cObj = YAHOO.util.Connect.asyncRequest('POST',
            updateCollection, callback);
    },


    /**
     * Creates a YUI  dialog for when a user wants to  
     * edit permissions to the collection
     *
     */
    createEditPermissionsDialog : function()
    {
	    // Define various event handlers for Dialog
	    var submit = function() 
	    {
			if (YAHOO.ur.edit.institution.collection.editPermissionDialog.validate()) {
	    
			    // action for updating the collection permission
		        var updateCollectionAction =  basePath + 'admin/editInstitutionalCollectionViewPermission.action';
	
	            YAHOO.util.Connect.setForm('editPermissionCollectionForm');
			
            	var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            		updateCollectionAction, callback);
            } else {
            	YAHOO.ur.edit.institution.collection.editPermissionDialog.hide();
            	document.getElementById('edit_permission_collection_form_collection_id').value = '';
            }
         
	    };
	
	    // handle a cancel of the adding the group
	    var cancel = function() 
	    {
	         YAHOO.ur.edit.institution.collection.editPermissionDialog.hide();
	    };
	
	    var success = function(o) 
	    {
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var divToUpdate = document.getElementById('edit_view_permission');
                divToUpdate.innerHTML = o.responseText; 
                YAHOO.ur.edit.institution.collection.editPermissionDialog.hide();
            }
            
	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new collection item button is clicked.
	    YAHOO.ur.edit.institution.collection.editPermissionDialog = 
	       new YAHOO.widget.Dialog('edit_permission_dialog', 
            { width : "550px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Save', handler:submit, isDefault:true },
					      { text:'Cancel', handler:cancel } ]
		    } );

		YAHOO.ur.edit.institution.collection.editPermissionDialog.validate = function() 
		{
            
            chosen = "";
			len = document.editPermissionCollectionForm.updateChildrenPermission.length;
			
			for (i = 0; i <len; i++) {
				if (document.editPermissionCollectionForm.updateChildrenPermission[i].checked) 
				{
					chosen = document.editPermissionCollectionForm.updateChildrenPermission[i].value
				}
			}	
			
			if (chosen == "0")
			{
				return false;
			} else {
				return true;
			}
					
		}
		
	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
			
			
	    // Render the Dialog
	    YAHOO.ur.edit.institution.collection.editPermissionDialog.render();

	    // show and center the permission dialog box
	     YAHOO.ur.edit.institution.collection.editPermissionDialog.showDialog = function()
	    {
	        YAHOO.ur.edit.institution.collection.editPermissionDialog.center();
	        YAHOO.ur.edit.institution.collection.editPermissionDialog.show();
	    }	    

    },
    
    showPermissionDialog : function() 
    {
    
        // action for getting the picture
        var getCollectionAction =  basePath + 'admin/getCollection.action';

        // Success action on getting the picture
        var handleSuccess = function(o) 
        {
            // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
                var divToUpdate = document.getElementById('edit_permission');
                divToUpdate.innerHTML = o.responseText; 
                YAHOO.ur.edit.institution.collection.editPermissionDialog.showDialog();
            }
        };
    
        //Faiure action on getting a picture
        var handleFailure = function(o) 
	    {
	        alert('Could not get picture ' 
	            + o.status + ' status text ' + o.statusText );
	    };
		

		// Get the next picture
        var transaction = YAHOO.util.Connect.asyncRequest('get', 
            getCollectionAction +'?collectionId=' + document.getElementById('hidden_collection_id').value + '&bustcache='+new Date().getTime(), 
            {success: handleSuccess, failure: handleFailure}, null);   	
    },
    
    
    /**
     * asks user to confirm removal of a group
     */
    removeGroupConfirmDialog : function()
    {
	    // Define various event handlers for Dialog
	    var submit = function() 
	    {
	    	YAHOO.ur.edit.institution.collection.removeGroupConfirmDialog.hide();
	    	YAHOO.ur.util.wait.waitDialog.showDialog();
	        // action to perform for searching names
            var action =  basePath + 'admin/removeGroupFromCollection.action';
            
            var formObject = document.getElementById('remove_group_from_collection_form');
	        YAHOO.util.Connect.setForm(formObject);
	
            var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            action, 
            callback);
         
	    };
	
	    // handle a cancel of the adding collection item dialog
	    var cancel = function() 
	    {
	        YAHOO.ur.edit.institution.collection.removeGroupConfirmDialog.hide();
	    };
	
	    var success = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	    	// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var divToUpdate = document.getElementById('current_collection_groups_div');
                divToUpdate.innerHTML = o.responseText; 
                
            }
	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('remove group failure ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new collection item button is clicked.
	    YAHOO.ur.edit.institution.collection.removeGroupConfirmDialog = 
	       new YAHOO.widget.Dialog('remove_group_confirm', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:submit, isDefault:true },
					      { text:'No', handler:cancel } ]
		    } );
	
	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
			
			
	    // Render the Dialog
	    YAHOO.ur.edit.institution.collection.removeGroupConfirmDialog.render();

    },
    
    /**
     * Show the group permissions dialog box
     */
    showRemoveGroupDialog : function(groupId)
    {
        document.getElementById('remove_group_id').value = groupId;
        YAHOO.ur.edit.institution.collection.removeGroupConfirmDialog.center();
        YAHOO.ur.edit.institution.collection.removeGroupConfirmDialog.show();
    },
    
    /**
     * Dialog to create/edit link information
     */
    createNewLinkDialog : function()
    {
 	    // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	    	YAHOO.ur.edit.institution.collection.newLinkDialog.hide();
	    	YAHOO.ur.util.wait.waitDialog.showDialog();
            this.submit();
	    };
		
	    // handle a cancel of the adding folder dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.edit.institution.collection.clearLinkForm();
	        YAHOO.ur.edit.institution.collection.newLinkDialog.hide();
	    };
	
	    // handle a successful return
	    var handleSuccess = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	    	// received from the server
	        var response = o.responseText;
	        
	        var div = document.getElementById('new_link_fields');
	        div.innerHTML = o.responseText;
	        
	        
	        var success = document.getElementById('new_link_form_success').value;
	    
	        //if the link was not saved then show the user the error message.
	        if( success == "false" )
	        {
	           YAHOO.ur.edit.institution.collection.newLinkDialog.showLinkDialog();
	        }
	        else
	        {
	            collectionId = document.getElementById('hidden_collection_id').value;
	            YAHOO.ur.edit.institution.collection.newLinkDialog.hide();
	            YAHOO.ur.edit.institution.collection.clearLinkForm();
	            YAHOO.ur.edit.institution.collection.viewLinks(collectionId);
	        }
	         
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert("Link submission failed due to a network issue: " + o.status  + " status text " + o.statusText);
	    };

 	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new folder button is clicked.
	    YAHOO.ur.edit.institution.collection.newLinkDialog = new YAHOO.widget.Dialog('newLinkDialog', 
        { width : "600px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
					  { text:"Cancel", handler:handleCancel } ]
		} );
	
	    // override the submit
	   YAHOO.ur.edit.institution.collection.newLinkDialog.submit = function()
	   {
	        YAHOO.util.Connect.setForm('newLinkForm');
	        if( YAHOO.ur.edit.institution.collection.newLinkDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new link) based on the action.
	            	
                var action =  basePath + 'admin/addCollectionLink.action';
 	            if( document.newLinkForm.linkId.value != '')
	            {
	               action = basePath + 'admin/updateCollectionLink.action';
	            }
                var cObj = YAHOO.util.Connect.asyncRequest('POST',
                action, callback);
            }
	    };
	    
	    YAHOO.ur.edit.institution.collection.newLinkDialog.showLinkDialog = function()
	    {
	        YAHOO.ur.edit.institution.collection.newLinkDialog.center();
	        YAHOO.ur.edit.institution.collection.newLinkDialog.show();
	    };
   
 	    // Validate the entries in the form to require that both first and last name are entered
	   YAHOO.ur.edit.institution.collection.newLinkDialog.validate = function() {
	        var data = this.getData();
		    if (data.linkName == "" ) 
		    {
		        alert("A link name must be entered");
			    return false;
		    } 
		    if (data.linkUrl == "" ) 
		    {
		        alert("A link URL must be entered");
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
	    YAHOO.ur.edit.institution.collection.newLinkDialog.render();
    },
    
    

    
    /**
     * Clear the link form of data
     */
    clearLinkForm: function()
    {
    	document.getElementById('link_name').value = "";
	    document.getElementById('link_url').value = "http://";
	    document.getElementById('link_description').value = "";
	    document.getElementById('new_link_id').value = "";
	
        var div = document.getElementById('link_error_div');
        div.innerHTML= "";
    },
    
    
    /**
     * Retireve link information for editing
     */
    editLink : function(collectionId, linkId)
    {
        var success = function(o) 
	    {
	        var divToUpdate = document.getElementById('new_link_fields');
            divToUpdate.innerHTML = o.responseText; 
            YAHOO.ur.edit.institution.collection.newLinkDialog.showLinkDialog();
 	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure to get link ' + o.status);
	    };
	    
	    	
	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
	    
	    // action to perform for searching names
        var action =  basePath + 'admin/editLinkView.action?collectionId=' + collectionId + '&linkId=' + linkId;
            
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            action, 
            callback);
    },
    
    /**
     * asks user to confirm delete a link
     */
    deleteLinkDialog : function()
    {
	    // Define various event handlers for Dialog
	    var submit = function() 
	    {
	        // action to perform for searching names
            var action =  basePath + 'admin/deleteCollectionLink.action';
            
            var formObject = document.getElementById('remove_link_collection_form');
	        YAHOO.util.Connect.setForm(formObject);
	
            var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            action, 
            callback);
         
	    };
	
	    // handle a cancel of the adding collection item dialog
	    var cancel = function() 
	    {
	        YAHOO.ur.edit.institution.collection.deleteLinkDialog.hide();
	    };
	
	    var success = function(o) 
	    {
	        var divToUpdate = document.getElementById('collection_links');
            divToUpdate.innerHTML = o.responseText; 
            YAHOO.ur.edit.institution.collection.deleteLinkDialog.hide();

	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new collection item button is clicked.
	    YAHOO.ur.edit.institution.collection.deleteLinkDialog = 
	       new YAHOO.widget.Dialog('remove_link_confirm', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:submit, isDefault:true },
					      { text:'No', handler:cancel } ]
		    } );
	
	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
		
		YAHOO.ur.edit.institution.collection.deleteLinkDialog.showDialog = function()
	    {
	        YAHOO.ur.edit.institution.collection.deleteLinkDialog.center();
	        YAHOO.ur.edit.institution.collection.deleteLinkDialog.show();
	    };
			
	    // Render the Dialog
	    YAHOO.ur.edit.institution.collection.deleteLinkDialog.render();

    },
    
    /**
     * remove the link with the specified name from the collection
     */
    removeLink : function(linkId)
    {
       //set the name in the form
       document.getElementById('remove_link_id').value = linkId;
       YAHOO.ur.edit.institution.collection.deleteLinkDialog.showDialog();
    },
    
    /**
     * Get and display the links
     */
    viewLinks : function(collectionId)
    {
    	var success = function(o) 
	    {
	        var divToUpdate = document.getElementById('collection_links');
                divToUpdate.innerHTML = o.responseText; 
 	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure to get links ' + o.status);
	    };
	    
	    	
	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
	    
	    // action to perform for searching names
        var action =  basePath + 'admin/viewCollectionLinks.action?collectionId=' + collectionId;
            
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            action + '&bustcache='+new Date().getTime(), 
            callback);
    },
    
    /**
     * Move a link up one position
     */
    moveLinkUp : function(linkId, collectionId)
    {
        var success = function(o) 
	    {
	        var divToUpdate = document.getElementById('collection_links');
                divToUpdate.innerHTML = o.responseText; 
 	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure to get links ' + o.status);
	    };
	    
	    	
	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
	    
	    // action to perform move
	    var action =  basePath + 'admin/moveCollectionLinkUp.action?collectionId=' + collectionId + '&linkId=' + linkId + '&bustcache='+new Date().getTime();
            
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            action, 
            callback);
    },
    
    /**
     * Move a link up down one position
     */
    moveLinkDown : function(linkId, collectionId)
    {
        var success = function(o) 
	    {
	        var divToUpdate = document.getElementById('collection_links');
                divToUpdate.innerHTML = o.responseText; 
 	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure to get links ' + o.status);
	    };
	    
	    	
	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
	    
	    // action to perform for searching names
        var action =  basePath + 'admin/moveCollectionLinkDown.action?collectionId=' + collectionId + '&linkId=' + linkId + '&bustcache='+new Date().getTime();
            
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            action, 
            callback);
    },
    
    /**
     * Un-subscribes a user from a collection
     */
    unsubscribeUser : function(userId, collectionId)
    {
        var success = function(o) 
	    {
	        var divToUpdate = document.getElementById('collection_subscribers');
                divToUpdate.innerHTML = o.responseText; 
 	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure to unsubscribe user ' + o.status);
	    };
	    
	    	
	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
	    
	    // action to perform move
	    var action =  basePath + 'admin/unSubscribeFromCollection.action';
            
        var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            action, 
            callback, 
            'collectionId=' + collectionId + '&subscribeUserId=' + userId);
    },
    
    /** 
     * initialize the page
     * this is called once the dom has
     * been created 
     */
    init : function() 
    {
        YAHOO.ur.edit.institution.collection.removeGroupConfirmDialog();
        YAHOO.ur.edit.institution.collection.errorDialog();
        YAHOO.ur.edit.institution.collection.createPictureUploadDialog();
        YAHOO.ur.edit.institution.collection.createNewLinkDialog();
        YAHOO.ur.edit.institution.collection.clearLinkForm();
        YAHOO.ur.edit.institution.collection.deleteLinkDialog();
        YAHOO.ur.edit.institution.collection.createEditPermissionsDialog();
        YAHOO.ur.edit.institution.collection.getCollectionPictures(document.getElementById("collectionId").value);
        var myTabs = new YAHOO.widget.TabView("collection-properties-tabs");
    }

};


// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.edit.institution.collection.init);