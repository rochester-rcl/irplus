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
 * This code is for dealing with editing researcher
 */
YAHOO.namespace("ur.edit.researcher");


/**
 * Edit researcher namespace
 */
YAHOO.ur.edit.researcher = 
{
	/*
	 * update the researcher information
	 */
	updatePersonalInfo : function() 
	{
	
	    YAHOO.ur.util.wait.waitDialog.showDialog(); 
	    var callback = 
	    {
	    		 
	        success : function(o) 
	        {
		        // check for the timeout - forward user to login page if timout
                // occured
                if( !urUtil.checkTimeOut(o.responseText) )
                {   
                	YAHOO.ur.util.wait.waitDialog.hide(); 
	            } 
	        },
	    
	        failure : function(o) 
		    {
	        	YAHOO.ur.util.wait.waitDialog.hide();
		        alert('Could not save researcher info ' 
		            + o.status + ' status text ' + o.statusText );
		    }
	    }
	    
	    // action to perform when updating a collection
	    var updatePersonalInfo = basePath + 'user/updatePersonalInformation.action';
	
	 	YAHOO.util.Connect.setForm('base_researcher_information');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('POST',
	        updatePersonalInfo, callback, null);
	 
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the new field form
	  */
	clearFieldForm : function()
	{
        // clear out any errors
        var div = document.getElementById('error_div');
        div.innerHTML = "";
		
		document.getElementById('newFieldForm_name').value = "";
		document.getElementById('newFieldForm_description').value = "";
		document.getElementById('newFieldForm_id').value = "";
		document.newFieldForm.newField.value = "true";
	},
	
	/**
	 * Creates a YUI new field modal dialog for when a user wants to create 
	 * a new field
	 *
	 */
	createNewFieldDialog : function() 
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
			
		// handle a cancel of the adding field dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.edit.researcher.newFieldDialog.hide();
		    YAHOO.ur.edit.researcher.clearFieldForm();
		};
		
		var handleSuccess = function(o) 
		{
		    // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       
		        //get the response from adding a field
		        var response = o.responseText;
		        var fieldForm = document.getElementById('newFieldDialogFields');
		    
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        fieldForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("newFieldForm_success").value;
		  
		        //if the field was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.edit.researcher.newFieldDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the field was added
		            YAHOO.ur.edit.researcher.newFieldDialog.hide();
			        YAHOO.ur.edit.researcher.clearFieldForm();
		            YAHOO.ur.edit.researcher.saveResearcherAndGetField();
		        }
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('field submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new field button is clicked.
		YAHOO.ur.edit.researcher.newFieldDialog = new YAHOO.widget.Dialog('newFieldDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
		
		// show and center the field dialog
	    YAHOO.ur.edit.researcher.newFieldDialog.showDialog = function()
	    {
	        YAHOO.ur.edit.researcher.newFieldDialog.show();
	        YAHOO.ur.edit.researcher.newFieldDialog.center();
	    }
	    
		 // Submit form
	    YAHOO.ur.edit.researcher.newFieldDialog.submit = function()
	    {  
		    YAHOO.util.Connect.setForm('newFieldForm');
		    
		     var newFieldAction = basePath + 'user/createField.action';	    
		    if( YAHOO.ur.edit.researcher.newFieldDialog.validate() )
		    {
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            newFieldAction, callback);
	        }
	    }
	       	    
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.edit.researcher.newFieldDialog.validate = function() {
		    var name = document.getElementById('newFieldForm_name').value;
			if (name == "" || name == null) {
			    alert('A field name must be entered');
				return false;
			} else {
				return true;
			}
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
		// Render the Dialog
		YAHOO.ur.edit.researcher.newFieldDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("show_field", "click", 
		    YAHOO.ur.edit.researcher.newFieldDialog.showDialog, 
		    YAHOO.ur.edit.researcher.newFieldDialog, true);
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the new department form
	  */
	clearDepartmentForm : function()
	{
	    // clear out any errors
        var div = document.getElementById('departmentError');
        div.innerHTML = "";
		
		document.getElementById('newDepartmentForm_name').value = "";
		document.getElementById('newDepartmentForm_description').value = "";
		document.getElementById('newDepartmentForm_id').value = "";
		document.newDepartmentForm.newDepartment.value = "true";
	},
	
	/**
	 * Creates a YUI new department modal dialog for when a user wants to create 
	 * a new department
	 *
	 */
	createNewDepartmentDialog : function() 
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
			
		// handle a cancel of the adding department dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.edit.researcher.clearDepartmentForm();
		    YAHOO.ur.edit.researcher.newDepartmentDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		
		        //get the response from adding a department
		        var response = o.responseText;
		        var departmentForm = document.getElementById('newDepartmentDialogFields');
		    
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        departmentForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("newDepartmentForm_success").value;
		    
		  
		        //if the department was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.edit.researcher.newDepartmentDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the department was added
		            YAHOO.ur.edit.researcher.newDepartmentDialog.hide();
		            YAHOO.ur.edit.researcher.clearDepartmentForm();
	                YAHOO.ur.edit.researcher.saveResearcherAndGetDepartment();
		        }
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('department submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new department button is clicked.
		YAHOO.ur.edit.researcher.newDepartmentDialog = new YAHOO.widget.Dialog('newDepartmentDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
		
		// show and center the department dialog
	    YAHOO.ur.edit.researcher.newDepartmentDialog.showDialog = function()
	    {
	        YAHOO.ur.edit.researcher.newDepartmentDialog.show();
	        YAHOO.ur.edit.researcher.newDepartmentDialog.center();
	    }
	    
	    // show and center the department dialog
	    YAHOO.ur.edit.researcher.newDepartmentDialog.submit = function()
	    {
		    YAHOO.util.Connect.setForm('newDepartmentForm');
		    
		    var newDepartmentAction = basePath + 'user/createDepartment.action';	    
		    if( YAHOO.ur.edit.researcher.newDepartmentDialog.validate() )
		    {
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            newDepartmentAction, callback);
	        }
	    }	    
	    
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.edit.researcher.newDepartmentDialog.validate = function() {
		    var name = document.getElementById('newDepartmentForm_name').value;
			if (name == "" || name == null) {
			    alert('A department name must be entered');
				return false;
			} else {
				return true;
			}
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
		// Render the Dialog
		YAHOO.ur.edit.researcher.newDepartmentDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("show_department", "click", 
		    YAHOO.ur.edit.researcher.newDepartmentDialog.showDialog, 
		    YAHOO.ur.edit.researcher.newDepartmentDialog, true);
	},
	
	/**
	 * Creates a YUI new researcher modal dialog for when a user wants to  
	 * upload a picture.
	 *
	 */
	createPictureUploadDialog : function()
	{
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
		// handle a cancel of the adding researcher item dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.edit.researcher.uploadResearcherPictureDialog.hide();
		    YAHOO.ur.edit.researcher.clearUploadPictureForm();
		};
		
		var handleSuccess = function(o) 
		{
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
		        var researcherId = document.getElementById("researcher_id").value;
		  
		        //if the content type was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.edit.researcher.uploadResearcherPictureDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the upload form and get the pictures
		            YAHOO.ur.edit.researcher.uploadResearcherPictureDialog.hide();
		            YAHOO.ur.edit.researcher.clearUploadPictureForm();
		            YAHOO.ur.edit.researcher.getResearcherPictures(researcherId);
		        }
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('Picture upload submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new researcher item button is clicked.
		YAHOO.ur.edit.researcher.uploadResearcherPictureDialog = 
		   new YAHOO.widget.Dialog('uploadResearcherPictureDialog', 
	        { width : "600px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
		
		//show and center the dialog
	    YAHOO.ur.edit.researcher.uploadResearcherPictureDialog.showDialog = function()
	    {
	        YAHOO.ur.edit.researcher.uploadResearcherPictureDialog.center();
	        YAHOO.ur.edit.researcher.uploadResearcherPictureDialog.show();
	    }
	    
		// Submit the form
	    YAHOO.ur.edit.researcher.uploadResearcherPictureDialog.submit = function()
	    {
		    // action to perform when submitting the researcher items.
	        var uploadPictureDialogAction = basePath + 'user/uploadResearcherPicture.action';
		    YAHOO.util.Connect.setForm('addResearcherPicture', true, true);
		    	    
		    if( YAHOO.ur.edit.researcher.uploadResearcherPictureDialog.validate() )
		    {
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	                 uploadPictureDialogAction, callback);
	           
	        }	    
	    }
	    
	 	// Validate the entries in the form to require that a name is entered
		YAHOO.ur.edit.researcher.uploadResearcherPictureDialog.validate = function() {
		    var fileName = document.getElementById('picture_file_name').value;
		    
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
		YAHOO.ur.edit.researcher.uploadResearcherPictureDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showUploadPicture", "click", 
		    YAHOO.ur.edit.researcher.uploadResearcherPictureDialog.showDialog, 
		    YAHOO.ur.edit.researcher.uploadResearcherPictureDialog, true);
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the upload picture form
	  */
	clearUploadPictureForm : function()
	{
		document.getElementById('picture_file_name').value = "";
		document.getElementById('primary_picture').checked = "";
		
        // clear out any errors
        var div = document.getElementById('upload_error');
        div.innerHTML = "";
	},
	
	/*
	 * Gets the pictures after a modification has occured
	 */
	deletePicture : function(researcherId, irFileId, primaryPicture)
	{
	  
	   // action to get repository pictures
	   var deletePictureAction = 
	       basePath + 'user/deleteResearcherPicture.action';
		
		var callback = 
		{
		    success : function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
		            var divToUpdate = document.getElementById('researcher_pictures');
		            divToUpdate.innerHTML = o.responseText;
		        } 
		    },
		    
		    failure : function(o) 
			{
			    alert('Could not delete picture for researcher ' 
			        + o.status + ' status text ' + o.statusText );
			}
		}		

		/*
		 *  Get the set of pictures for the repository
		 */
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        deletePictureAction + '?pictureId='+ irFileId + 
	        '&researcherId='+ researcherId +
	        '&primaryResearcherPicture='+primaryPicture +
	        '&bustcache='+new Date().getTime(), callback, null);
	},
	
	/*
	 * Get the researcher pictures 
	 */
	getResearcherPictures : function(researcherId)
	{
	  // action to get repository pictures
	   var getPicturesAction = 
	       basePath + 'user/getResearcherPictures.action';
	
		var callback =
		{	 
		    success : function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
		            var divToUpdate = document.getElementById('researcher_pictures');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
		    
		    failure : function(o) 
			{
			    alert('Could not get picture for researcher ' 
			        + o.status + ' status text ' + o.statusText );
			}
		}
		
		/*
		 *  Get the set of pictures for the researcher
		 */
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        getPicturesAction + '?researcherId='+ researcherId +
	        '&bustcache='+new Date().getTime(), callback, null);
	},
	
	
	// create a dialog to confirm the deletion of pictures
	createPictureDeleteConfirmDialog : function(researcherId, 
	    irFileId, 
	    primaryPicture)
	{
	    // Define various event handlers for Dialog
		var handleYes = function() {
			YAHOO.ur.edit.researcher.deletePicture(researcherId, irFileId, primaryPicture);
			this.hide();
		};
		var handleNo = function() {
			this.hide();
		};
	
		// Instantiate the Dialog
		YAHOO.ur.edit.researcher.deletePictureDialog = 
		    new YAHOO.widget.SimpleDialog("deletePicture", 
										     { width: "500px",
											   visible: false,
											   close: true,
											   text: "Do you want to delete the selected picture?",
											   icon: YAHOO.widget.SimpleDialog.ICON_HELP,
											   buttons: [ { text:"Yes", handler:handleYes, isDefault:true },
														  { text:"No",  handler:handleNo } ]
											} );
		
		YAHOO.ur.edit.researcher.deletePictureDialog.setHeader("Delete?");
		
		// show and center the dialog
		YAHOO.ur.edit.researcher.deletePictureDialog.showDialog = function()
		{
		    YAHOO.ur.edit.researcher.deletePictureDialog.center();
		    YAHOO.ur.edit.researcher.deletePictureDialog.show();
		}
		
		// Render the Dialog
		YAHOO.ur.edit.researcher.deletePictureDialog.render("deletePictureDiv");
	
	},
	
	// creates the confirm dialog and
	// shows it for the specified id
	confirmPictureDelete : function(itemId, irFileId, primaryPicture)
	{
	    YAHOO.ur.edit.researcher.createPictureDeleteConfirmDialog(itemId, irFileId, primaryPicture);
	    YAHOO.ur.edit.researcher.deletePictureDialog.showDialog();
	},
	
	// Sets the default picture
	setAsDefaultPicture : function(researcherId, irFileId)
	{
	   // action set the default picture
	   var setDefaultPictureAction = 
	       basePath + 'user/setDefaultPicture.action';
	
	    /*
	     * Success action on setting the default picture
	     */
	    var getDefaultPicturesSuccess = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	    
	            var divToUpdate = document.getElementById('researcher_pictures');
	            divToUpdate.innerHTML = o.responseText; 
	        }
	    };
	    
	    /*
	     * Faiure action on setting the default picture
	     */
	    var getDefaultPicturesFailure = function(o) 
		{
		    alert('Could not set default picture for researcher ' 
		        + o.status + ' status text ' + o.statusText );
		};
		
		/*
		 *  Set the default picture for researcher
		 */
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        setDefaultPictureAction + '?pictureId='+ irFileId + 
	        '&researcherId='+ researcherId +
	        '&bustcache='+new Date().getTime(), 
	        {success: getDefaultPicturesSuccess, failure: getDefaultPicturesFailure}, null);
	},
	
	// create a message dialog
	errorDialog : function() 
	{
	    // Define various event handlers for Dialog
		var handleYes = function() {
			var contentArea = document.getElementById('default_error_dialog_content');
		    contentArea.innerHTML = ""; 
		    this.hide();
		};
		
	
		// Instantiate the Dialog
		YAHOO.ur.edit.researcher.errorDialog = 
		    new YAHOO.widget.Dialog("error_dialog_box", 
										     { width: "600px",
											   visible: false,
											   modal: true,
											   close: false,										   
											   buttons: [ { text:"Ok", handler:handleYes, isDefault:true } ]
											} );
		
		YAHOO.ur.edit.researcher.errorDialog.setHeader("Error");
		
		// show and center the dialog
		YAHOO.ur.edit.researcher.errorDialog.showDialog = function()
		{
		    YAHOO.ur.edit.researcher.errorDialog.center();
		    YAHOO.ur.edit.researcher.errorDialog.show();
		}
		
		// Render the Dialog
		YAHOO.ur.edit.researcher.errorDialog.render();
	},
	
	/*
	 *  Sets the researcher page public/hidden
	 */
	changePublicValue : function(publicValue) {
	
		var setResearcherPagePermissionAction = 
	       basePath + 'user/setResearcherPagePermission.action';
		
		/*
		 *  Set the researcher page public/hidden
		 */
	    var transaction = YAHOO.util.Connect.asyncRequest('POST', 
	        setResearcherPagePermissionAction + '?public='+ publicValue + '&researcherId=' + researcherId, null);
		
	},
	
	/*
	 *  Sets the researcher page public/hidden
	 */
	viewAddFiles : function() {
		
		document.myFolders.action =  basePath + 'user/viewAddFiles.action';
		document.myFolders.submit();
	},
	
	/*
	 *  Sets the researcher page public/hidden
	 */
	viewAddPublications : function() {
		
		document.myFolders.action =  basePath + 'user/viewAddPublications.action';
		document.myFolders.submit();
	},

	/*
	 * Removes the Field
	 */
	removeField : function(tableId)
	{
	        var table_div = document.getElementById("field_form");
	        var child = document.getElementById(tableId);
	        table_div.removeChild(child); 
	        
	},
		
	/**
	 *  Function that retrieves fields from 
	 *  the server
	 */
	getFields :function()
	{
	   // action to get repository pictures
	   var getFieldsAction = 
	       basePath + 'user/getAllFields.action';
	       
		var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
		            var divToUpdate = document.getElementById('field_form');
		            var newField = o.responseText;
		        
		            // id to give to the table
		    	    document.getElementById("field_table_id").value = parseInt(document.getElementById("field_table_id").value) + 1; 
		        
		            // Replace the table id with the latest Id
		            newField = newField.replace("field_table_i","field_table_" + document.getElementById("field_table_id").value, "gm");
		        
		            var newDiv = document.getElementById('new_field');
		            newDiv.innerHTML = newField;
		            divToUpdate.appendChild(document.getElementById('field_table_'+document.getElementById("field_table_id").value));
		            newDiv.innerHTML = "";
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get Extent Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	       
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        getFieldsAction + '?bustcache='+new Date().getTime(), 
	        callback, null);
	},
	
	/**
	 * Save researcher and update the screen with latest field
	 *
	 */
	saveResearcherAndGetField : function()
	{
		 var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
		            var divToUpdate = document.getElementById('field_form');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get researcher information failure ' + o.status + ' status text ' + o.statusText );
			}
		}

		YAHOO.util.Connect.setForm('researcherInformation');
		var saveResearcherAndGetFieldAction = basePath + 'user/saveResearcherAndGetFields.action';
		var transaction = YAHOO.util.Connect.asyncRequest('POST', 
	        saveResearcherAndGetFieldAction, 
	        callback);
	},

	/*
	 * Removes the Department
	 */
	removeDepartment : function(tableId)
	{
	        var table_div = document.getElementById("department_form");
	        var child = document.getElementById(tableId);
	        table_div.removeChild(child); 
	        
	},
		
	/**
	 *  Function that retrieves departments from 
	 *  the server
	 */
	getDepartments :function()
	{
	   // action to get repository pictures
	   var getDepartmentsAction = 
	       basePath + 'user/getAllDepartments.action';
	       
		var callback =
		{
		
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
		            var divToUpdate = document.getElementById('department_form');
		            var newDepartment = o.responseText;
		        
		            // id to give to the table
		    	    document.getElementById("department_table_id").value = parseInt(document.getElementById("department_table_id").value) + 1; 
		        
		            // Replace the table id with the latest Id
		            newDepartment = newDepartment.replace("department_table_i","department_table_" + document.getElementById("department_table_id").value, "gm");
		        
		            var newDiv = document.getElementById('new_department');
		            newDiv.innerHTML = newDepartment;
		            divToUpdate.appendChild(document.getElementById('department_table_'+document.getElementById("department_table_id").value));
		            newDiv.innerHTML = "";
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get Extent Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	       
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        getDepartmentsAction + '?bustcache='+new Date().getTime(), 
	        callback, null);
	},
	
	/**
	 * Save researcher and update the screen with latest department
	 *
	 */
	saveResearcherAndGetDepartment : function()
	{
		 var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
		            var divToUpdate = document.getElementById('department_form');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get researcher information failure ' + o.status + ' status text ' + o.statusText );
			}
		}

		YAHOO.util.Connect.setForm('researcherInformation');
		var saveResearcherAndGetDepartmentAction = basePath + 'user/saveResearcherAndGetDepartments.action';
		var transaction = YAHOO.util.Connect.asyncRequest('POST', 
	        saveResearcherAndGetDepartmentAction, 
	        callback);
	},
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() {
	    YAHOO.ur.edit.researcher.errorDialog();
	    YAHOO.ur.edit.researcher.createPictureUploadDialog();
	    YAHOO.ur.edit.researcher.createNewDepartmentDialog();
	    YAHOO.ur.edit.researcher.createNewFieldDialog();
	    
		
	    researcherId = document.getElementById("researcherId").value;
	    YAHOO.ur.edit.researcher.getResearcherPictures(researcherId);
	    
	}
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.edit.researcher.init);