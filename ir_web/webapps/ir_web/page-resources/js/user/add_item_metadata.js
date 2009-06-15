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

/*
 * This JS file helps to create the metadata info
 * for the item
 */

YAHOO.namespace("ur.item.metadata");

// Action to save metadata to item
var saveItemMetadataAndFinishLaterAction = basePath + 'user/saveItemMetadataAndFinishLater.action';
var saveItemMetadataGoToInstitutionalItemAction = basePath + 'user/saveItemMetadataGoToInstitutionalItem.action';

// Actions to save metadata and update the screen with newly created series/identifier / sponsor / publisher
var saveItemMetadataAndGetSeriesAction = basePath + 'user/saveItemMetadataAndGetSeries.action';
var saveItemMetadataAndGetIdentifierAction = basePath + 'user/saveItemMetadataAndGetIdentifier.action';
var saveItemMetadataAndGetExtentAction = basePath + 'user/saveItemMetadataAndGetExtent.action';
var saveItemMetadataAndGetSponsorAction = basePath + 'user/saveItemMetadataAndGetSponsor.action';
var saveItemMetadataAndGetPublisherAction = basePath + 'user/saveItemMetadataAndGetPublisher.action';
var saveItemTypeAndUpdateSecondaryTypesAction = basePath + 'user/saveContentType.action';

// Action to save metadata to item
var saveMetadataAndShowAddFilesAction = basePath + 'user/saveMetadataAndShowAddFiles.action';
var saveMetadataAndViewContributorsAction = basePath + 'user/saveMetadataAndViewContributors.action';
var saveMetadataAndPreviewAction = basePath + 'user/saveMetadataAndPreview.action';
var saveReviewItemMetadataAction = basePath + 'user/saveItemMetadata.action';
var cancelReviewItemMetadataAction = basePath + 'user/reviewItem.action';

// Action to get the series
var seriesAction = basePath + 'user/getSeries.action';

// Action to get the identifiers
var identifierAction = basePath + 'user/getIdentifiers.action';

// Action to get the extents
var extentAction = basePath + 'user/getExtents.action';

// Action to get the sponsors
var sponsorAction = basePath + 'user/getSponsors.action';

// Actions to create new series /identifier / sponsor / publisher
var newSeriesAction = basePath + 'user/createSeries.action';
var newSponsorAction = basePath + 'user/createSponsor.action';
var newIdentifierTypeAction = basePath + 'user/createIdentifierType.action';
var newExtentTypeAction = basePath + 'user/createExtentType.action';
var newPublisherAction = basePath + 'user/createPublisher.action';

YAHOO.ur.item.metadata = {
	
	/**
	 * Function to save item metadata and goto the Publication table
	 *
	 */
	saveItemMetadata : function()
	{
		if (YAHOO.ur.item.metadata.validateReleaseDate()) {
			if (document.itemForm.institutionalItemId.value != '') {
				document.itemForm.action = saveItemMetadataGoToInstitutionalItemAction;
			} else {
				document.itemForm.action = saveItemMetadataAndFinishLaterAction;;
			}
				
		    document.itemForm.submit();
		}
	},
	
	/**
	 * Release date should be entered completely or should be completely empty
	 */
	validateReleaseDate : function() {
		if ((document.getElementById('itemForm_releaseDate_day').value == '' && document.getElementById('itemForm_releaseDate_month').value == '' && document.getElementById('itemForm_releaseDate_year').value == '')
		  		|| (document.getElementById('itemForm_releaseDate_day').value !=  '' && document.getElementById('itemForm_releaseDate_month').value !=  '' && document.getElementById('itemForm_releaseDate_year').value != ''))
		{
		 	return true;
		} else {
			alert('Please enter the complete day, month and year for "Date this publication can be made available to public". Or empty day, month and year boxes.');
		 	return false;
		}
	},

	/**
	 * Function to save review item metadata and goto the item preview screen
	 *
	 */
	saveReviewItemMetadata : function()
	{
		document.itemForm.action = saveReviewItemMetadataAction;
	    document.itemForm.submit();
	},

	/**
	 * Function to cancel review item metadata edit and goto the item preview screen
	 *
	 */
	cancelReviewItemMetadata : function()
	{
		document.itemForm.action = cancelReviewItemMetadataAction;
	    document.itemForm.submit();
	},
	
	/**
	 * Function to save the item and go to add files screen
	 *
	 */
	showAddFiles : function()
	{
		if (YAHOO.ur.item.metadata.validateReleaseDate()) {
			document.itemForm.action = saveMetadataAndShowAddFilesAction;
		    document.itemForm.submit();
		}
	},
	
	/**
	 * Function to save the item and go to preview screen
	 *
	 */
	gotoPreview : function()
	{
		if (YAHOO.ur.item.metadata.validateReleaseDate()) {
			document.itemForm.action = saveMetadataAndPreviewAction;
		    document.itemForm.submit();
		}
	},
	
	/**
	 * Function to save the item and go to add contributors screen
	 *
	 */
	saveMetadataAndViewContributors : function()
	{
		if (YAHOO.ur.item.metadata.validateReleaseDate()) {
			document.itemForm.action = saveMetadataAndViewContributorsAction;
		    document.itemForm.submit();
		}
	},

	/**
	 * Save type and update the secondary with types other than primary type
	 *
	 */
	saveItemTypeAndUpdateSecondaryTypes : function()
	{
		var	callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {               			    
		            var divToUpdate = document.getElementById('type_form');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('saveItemTypeAndUpdateSecondaryTypes failure ' + o.status + ' status text ' + o.statusText );
			}
		};
		

		YAHOO.util.Connect.setForm('itemForm');
		 
		var transaction = YAHOO.util.Connect.asyncRequest('POST', 
	        saveItemTypeAndUpdateSecondaryTypesAction, 
	        callback);
	},
		
	/**
	 * Save metadata and update the screen with latest data
	 *
	 */
	saveItemMetadataAndGetSeries : function()
	{
	
		var	callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {               			    
		            var divToUpdate = document.getElementById('series_forms');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('saveItemMetadataAndGetSeries failure ' + o.status + ' status text ' + o.statusText );
			}
		};
		

		YAHOO.util.Connect.setForm('itemForm');
		 
		var transaction = YAHOO.util.Connect.asyncRequest('POST', 
	        saveItemMetadataAndGetSeriesAction, 
	        callback);
	},
	
	/*
	 * Removes the series
	 */
	removeSeries : function(tableId)
	{
	    var table_div = document.getElementById("series_forms");
	    var child = document.getElementById(tableId);
	    table_div.removeChild(child); 
	},
	
	
	/**
	 *  Function that retrieves series
	 * 
	 */
	getSeries : function()
	{
		var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {               			    
		            var divToUpdate = document.getElementById('series_forms');
		            var newSeries = o.responseText;
		                
		            // id to give to the table
		    	    document.getElementById("series_table_id").value = parseInt(document.getElementById("series_table_id").value) + 1; 
		    	
		            // Replace the table id with the latest Id
		            newSeries = newSeries.replace("series_table_i","series_table_" + document.getElementById("series_table_id").value, "gm");
		        
		            var newDiv = document.getElementById('new_series');
		            newDiv.innerHTML = newSeries;
		            divToUpdate.appendChild(document.getElementById('series_table_'+document.getElementById("series_table_id").value));
		            newDiv.innerHTML = "";
		        }
		
		    },
			
			failure: function(o) 
			{
			    alert('Get series Failure ' + o.status + ' status text ' + o.statusText );
			}
		};

	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        seriesAction + '?bustcache='+new Date().getTime(), 
	        callback, null);
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the new series form
	  */
	clearSeriesForm : function()
	{
        // clear out the error message
        var seriesError = document.getElementById('seriesError');
        seriesError.innerHTML = "";
		
		document.getElementById('newSeriesForm_name').value = "";
		document.getElementById('newSeriesForm_number').value = "";
		document.getElementById('newSeriesForm_description').value = "";
		document.getElementById('newSeriesForm_id').value = "";
	},
	
	/**
	 * Creates a YUI new series modal dialog for when a user wants to create 
	 * a new series
	 *
	 */
	createNewSeriesDialog : function()
	{
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
			
		// handle a cancel of the adding series dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.item.metadata.newSeriesDialog.hide();
		    YAHOO.ur.item.metadata.clearSeriesForm();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               			
		        //get the response from adding a series
		        var response = o.responseText;
		        var seriesForm = document.getElementById('newSeriesDialogFields');
		    
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        seriesForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("newSeriesForm_success").value;
		    
		        //if the series was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.item.metadata.newSeriesDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the series was added
		            YAHOO.ur.item.metadata.newSeriesDialog.hide();
		            YAHOO.ur.item.metadata.clearSeriesForm();
		            YAHOO.ur.item.metadata.saveItemMetadataAndGetSeries();
		        }
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('series submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new series button is clicked.
		YAHOO.ur.item.metadata.newSeriesDialog = new YAHOO.widget.Dialog('newSeriesDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );

	    // show and center the affilaition dialog box
	    YAHOO.ur.item.metadata.newSeriesDialog.showDialog = function()
	    {
	       YAHOO.ur.item.metadata.newSeriesDialog.center();
	       YAHOO.ur.item.metadata.newSeriesDialog.show();
	    } 	

	    // Submit form
	    YAHOO.ur.item.metadata.newSeriesDialog.submit = function()
	    {
		    YAHOO.util.Connect.setForm('newSeriesForm');
		    
		    if( YAHOO.ur.item.metadata.newSeriesDialog.validate() )
		    {
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            newSeriesAction, callback);
	        }	
	    }    		
	   
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.item.metadata.newSeriesDialog.validate = function() {
		    var name = document.getElementById('newSeriesForm_name').value;
			if (name == "" || name == null) {
			    alert('A series name must be entered');
				return false;
			} else {
				return true;
			}
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
		// Render the Dialog
		YAHOO.ur.item.metadata.newSeriesDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("show_series", "click", 
		    YAHOO.ur.item.metadata.newSeriesDialog.showDialog, 
		    YAHOO.ur.item.metadata.newSeriesDialog, true);
	},
	
	/*
	 * Removes the identifier
	 */
	removeIdentifier : function(tableId)
	{
	        var table_div = document.getElementById("identifier_forms");
	        var child = document.getElementById(tableId);
	        table_div.removeChild(child); 
	        
	},
	
	/**
	 *  Function that retrieves Identifiers from 
	 *  the server
	 */
	getIdentifiers :function()
	{
		var callback =
		{
		
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {               			    
		            var divToUpdate = document.getElementById('identifier_forms');
		            var newIdentifier = o.responseText;
		        
		            // id to give to the table
		    	    document.getElementById("identifier_table_id").value = parseInt(document.getElementById("identifier_table_id").value) + 1; 
		        
		            // Replace the table id with the latest Id
		            newIdentifier = newIdentifier.replace("identifier_table_i","identifier_table_" + document.getElementById("identifier_table_id").value, "gm");
		        
		            var newDiv = document.getElementById('new_identifier');
		            newDiv.innerHTML = newIdentifier;
		            divToUpdate.appendChild(document.getElementById('identifier_table_'+document.getElementById("identifier_table_id").value));
		            newDiv.innerHTML = "";
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get Identifier Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	       
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        identifierAction + '?bustcache='+new Date().getTime(), 
	        callback, null);
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the new identifier type form
	  */
	clearIdentifierTypeForm : function()
	{
        // clear out the error message
        var identifierTypeError = document.getElementById('identifierTypeError');
        identifierTypeError.innerHTML = "";
        
		document.getElementById('newIdentifierTypeForm_name').value="";
		document.getElementById('newIdentifierTypeForm_description').value="";
	
	},
	
	/**
	 * Save metadata and update the screen with latest identifier
	 *
	 */
	saveItemMetadataAndGetIdentifier : function()
	{
		 var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {               			    
		            var divToUpdate = document.getElementById('identifier_forms');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('saveItemMetadataAndGetIdentifier failure ' + o.status + ' status text ' + o.statusText );
			}
		}

		YAHOO.util.Connect.setForm('itemForm');
		 
		var transaction = YAHOO.util.Connect.asyncRequest('POST', 
	        saveItemMetadataAndGetIdentifierAction, 
	        callback);
	},
	
	
	/**
	 * Creates a YUI new identifier type modal dialog for when a user wants to create 
	 * a new identifier type
	 *
	 */
	createNewIdentifierTypeDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
			
		// handle a cancel of the adding identifier type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.item.metadata.identifierTypeDialog.hide();
		    YAHOO.ur.item.metadata.clearIdentifierTypeForm();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		
		        //get the response from adding a identifier type
		        var response = eval("("+o.responseText+")");
		    
		        //if the identifier type was not added then show the user the error message.
		        // received from the server
		        if( response.identifierTypeAdded == "false" )
		        {
		            var identifierTypeNameError = document.getElementById('identifierTypeError');
	                identifierTypeNameError.innerHTML = '<p id="newIdentifierTypeForm_nameError">' + response.message + '</p>';
	                YAHOO.ur.item.metadata.identifierTypeDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the identifier type was added
		            YAHOO.ur.item.metadata.clearIdentifierTypeForm();
		            YAHOO.ur.item.metadata.identifierTypeDialog.hide();
		       	    YAHOO.ur.item.metadata.saveItemMetadataAndGetIdentifier();
		        }
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('identifier type submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new identifier type button is clicked.
		YAHOO.ur.item.metadata.identifierTypeDialog = new YAHOO.widget.Dialog('newIdentifierTypeDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
		
	    // show and center the affilaition dialog box
	    YAHOO.ur.item.metadata.identifierTypeDialog.showDialog = function()
	    {
	       YAHOO.ur.item.metadata.identifierTypeDialog.center();
	       YAHOO.ur.item.metadata.identifierTypeDialog.show();
	    }
	    		
		// Submit form	
		YAHOO.ur.item.metadata.identifierTypeDialog.submit = function() 
		{
		    YAHOO.util.Connect.setForm('newIdentifierType');
	
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        newIdentifierTypeAction, callback);
	    }
	        	   
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.item.metadata.identifierTypeDialog.validate = function() {
		    var data = this.getData();
			if (data.folderName == "" || data.folderName == null) {
			    alert('An Identifier type name must be entered');
				return false;
			} else {
				return true;
			}
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.item.metadata.identifierTypeDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("show_identifier_type", "click", 
		    YAHOO.ur.item.metadata.identifierTypeDialog.showDialog, 
		    YAHOO.ur.item.metadata.identifierTypeDialog, true);
	},


	/*
	 * Removes the extent
	 */
	removeExtent : function(tableId)
	{
	        var table_div = document.getElementById("extent_forms");
	        var child = document.getElementById(tableId);
	        table_div.removeChild(child); 
	},
	 
	/**
	 *  Function that retrieves Extents from 
	 *  the server
	 */
	getExtents :function()
	{
		var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
		            var divToUpdate = document.getElementById('extent_forms');
		            var newExtent = o.responseText;
		        
		            // id to give to the table
		    	    document.getElementById("extent_table_id").value = parseInt(document.getElementById("extent_table_id").value) + 1; 
		        
		            // Replace the table id with the latest Id
		            newExtent = newExtent.replace("extent_table_i","extent_table_" + document.getElementById("extent_table_id").value, "gm");
		        
		            var newDiv = document.getElementById('new_extent');
		            newDiv.innerHTML = newExtent;
		            divToUpdate.appendChild(document.getElementById('extent_table_'+document.getElementById("extent_table_id").value));
		            newDiv.innerHTML = "";
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get Extent Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	       
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        extentAction + '?bustcache='+new Date().getTime(), 
	        callback, null);
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the new extent type form
	  */
	clearExtentTypeForm : function()
	{
        // clear out the error message
        var extentTypeError = document.getElementById('extentTypeError');
        extentTypeError.innerHTML = "";
        
		document.getElementById('newExtentTypeForm_name').value="";
		document.getElementById('newExtentTypeForm_description').value="";
	
	},
	
	/**
	 * Save metadata and update the screen with latest extent
	 *
	 */
	saveItemMetadataAndGetExtent : function()
	{
		 var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
		            var divToUpdate = document.getElementById('extent_forms');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('saveItemMetadataAndGetExtent failure ' + o.status + ' status text ' + o.statusText );
			}
		}

		YAHOO.util.Connect.setForm('itemForm');
		 
		var transaction = YAHOO.util.Connect.asyncRequest('POST', 
	        saveItemMetadataAndGetExtentAction, 
	        callback);
	},
	
	
	/**
	 * Creates a YUI new extent type modal dialog for when a user wants to create 
	 * a new extent type
	 *
	 */
	createNewExtentTypeDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
			
		// handle a cancel of the adding extent type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.item.metadata.extentTypeDialog.hide();
		    YAHOO.ur.item.metadata.clearExtentTypeForm();
		};
		
		var handleSuccess = function(o) 
		{
		    // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		
		        //get the response from adding a extent type
		        var response = eval("("+o.responseText+")");
		    
		        //if the extent type was not added then show the user the error message.
		        // received from the server
		        if( response.extentTypeAdded == "false" )
		        {
		            var extentTypeNameError = document.getElementById('extentTypeError');
	                extentTypeNameError.innerHTML = '<p id="newExtentTypeForm_nameError">' + response.message + '</p>';
	                YAHOO.ur.item.metadata.extentTypeDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the extent type was added
		            YAHOO.ur.item.metadata.clearExtentTypeForm();
		            YAHOO.ur.item.metadata.extentTypeDialog.hide();
		       	    YAHOO.ur.item.metadata.saveItemMetadataAndGetExtent();
		        }
		    }  
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('extent type submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new extent type button is clicked.
		YAHOO.ur.item.metadata.extentTypeDialog = new YAHOO.widget.Dialog('newExtentTypeDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
		
	    // show and center the affilaition dialog box
	    YAHOO.ur.item.metadata.extentTypeDialog.showDialog = function()
	    {
	       YAHOO.ur.item.metadata.extentTypeDialog.center();
	       YAHOO.ur.item.metadata.extentTypeDialog.show();
	    }
	    		
		// Submit form	
		YAHOO.ur.item.metadata.extentTypeDialog.submit = function() 
		{
		    YAHOO.util.Connect.setForm('newExtentType');
	
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        newExtentTypeAction, callback);
	    }
	        	   
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.item.metadata.extentTypeDialog.validate = function() {
		    var data = this.getData();
			if (data.folderName == "" || data.folderName == null) {
			    alert('An Extent type name must be entered');
				return false;
			} else {
				return true;
			}
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.item.metadata.extentTypeDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("show_extent_type", "click", 
		    YAHOO.ur.item.metadata.extentTypeDialog.showDialog, 
		    YAHOO.ur.item.metadata.extentTypeDialog, true);
	},

	/*
	 * Removes the extent
	 */
	removeSponsor : function(tableId)
	{
	        var table_div = document.getElementById("sponsor_forms");
	        var child = document.getElementById(tableId);
	        table_div.removeChild(child); 
	},
	 
	/**
	 *  Function that retrieves sponsor from 
	 *  the server
	 */
	getSponsors :function()
	{
		var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
		            var divToUpdate = document.getElementById('sponsor_forms');
		            var newSponsor = o.responseText;
		        
		            // id to give to the table
		    	    document.getElementById("sponsor_table_id").value = parseInt(document.getElementById("sponsor_table_id").value) + 1; 
		        
		            // Replace the table id with the latest Id
		            newSponsor = newSponsor.replace("sponsor_table_i","sponsor_table_" + document.getElementById("sponsor_table_id").value, "gm");
		        
		            var newDiv = document.getElementById('new_sponsor');
		            newDiv.innerHTML = newSponsor;
		            divToUpdate.appendChild(document.getElementById('sponsor_table_'+document.getElementById("sponsor_table_id").value));
		            newDiv.innerHTML = "";
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get sponsor Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	       
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        sponsorAction + '?bustcache='+new Date().getTime(), 
	        callback, null);
	},

	 /** 
	  * clear out any form data messages or input
	  * in the new sponsor form
	  */
	clearSponsorForm : function()
	{
        // clear out the error message
        var sponsorError = document.getElementById('sponsorError');
        sponsorError.innerHTML = "";	
        	
		document.getElementById('newSponsorForm_name').value = "";
		document.getElementById('newSponsorForm_description').value = "";
		document.getElementById('newSponsorForm_id').value = "";
	},
	
	/**
	 * Save metadata and update the screen with latest sponsors list
	 *
	 */
	saveItemMetadataAndGetSponsor :function()
	{	
		var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
		            var divToUpdate = document.getElementById('sponsor_forms');
		            divToUpdate.innerHTML = o.responseText;
		        } 
		    },
			
			failure: function(o) 
			{
			    alert('saveItemMetadataAndGetSponsor failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	

		YAHOO.util.Connect.setForm('itemForm');
		 
		var transaction = YAHOO.util.Connect.asyncRequest('POST', 
	        saveItemMetadataAndGetSponsorAction, 
	        callback);
	},
	
	
	/**
	 * Creates a YUI new sponsor modal dialog for when a user wants to create 
	 * a new sponsor
	 *
	 */
	createNewSponsorDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
			
		// handle a cancel of the adding sponsor dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.item.metadata.newSponsorDialog.hide();
		    YAHOO.ur.item.metadata.clearSponsorForm();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		
		        //get the response from adding a sponsor
		        var response = o.responseText;
		        var sponsorForm = document.getElementById('newSponsorDialogFields');
		    
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        sponsorForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("newSponsorForm_success").value;
		  
		        //if the sponsor was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.item.metadata.newSponsorDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the sponsor was added
		            YAHOO.ur.item.metadata.newSponsorDialog.hide();
		            YAHOO.ur.item.metadata.clearSponsorForm();
		            YAHOO.ur.item.metadata.saveItemMetadataAndGetSponsor();
		        }
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('sponsor submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new sponsor button is clicked.
		YAHOO.ur.item.metadata.newSponsorDialog = new YAHOO.widget.Dialog('newSponsorDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
		
	    // show and center the sponsor dialog box
	    YAHOO.ur.item.metadata.newSponsorDialog.showDialog = function()
	    {
	       YAHOO.ur.item.metadata.newSponsorDialog.center();
	       YAHOO.ur.item.metadata.newSponsorDialog.show();
	    } 
	    		
		// Submit form	
		YAHOO.ur.item.metadata.newSponsorDialog.submit = function() 
		{   
		    YAHOO.util.Connect.setForm('newSponsorForm');
		    	    
		    if( YAHOO.ur.item.metadata.newSponsorDialog.validate() )
		    {
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            newSponsorAction, callback);
	        }
	    }			
	   
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.item.metadata.newSponsorDialog.validate = function() {
		    var name = document.getElementById('newSponsorForm_name').value;
			if (name == "" || name == null) {
			    alert('A Content type name must be entered');
				return false;
			} else {
				return true;
			}
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.item.metadata.newSponsorDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("show_sponsor", "click", 
		    YAHOO.ur.item.metadata.newSponsorDialog.showDialog, 
		    YAHOO.ur.item.metadata.newSponsorDialog, true);
	
	},
	
	/**
	 * Save metadata and update the screen with latest publisher data
	 *
	 */
	saveItemMetadataAndGetPublisher : function()
	{
		var callback =
		{
		    success: function(o) 
		    {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       		    
		            var divToUpdate = document.getElementById('publisher_form');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('saveItemMetadataAndGetPublisher failure ' + o.status + ' status text ' + o.statusText );
			}
		}
		

		YAHOO.util.Connect.setForm('itemForm');
		 
		var transaction = YAHOO.util.Connect.asyncRequest('POST', 
	        saveItemMetadataAndGetPublisherAction, 
	        callback);
	        
	},
	
	
	 /** 
	  * clear out any form data messages or input
	  * in the new publisher form
	  */
	clearPublisherForm : function()
	{
        // clear out the error message
        var publisherError = document.getElementById('publisherError');
        publisherError.innerHTML = "";		
        
		document.getElementById('newPublisherForm_name').value = "";
		document.getElementById('newPublisherForm_description').value = "";
		document.getElementById('newPublisherForm_id').value = "";
	},
	
	/**
	 * Creates a YUI new publisher modal dialog for when a user wants to create 
	 * a new publisher
	 *
	 */
	createNewPublisherDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
			
		// handle a cancel of the adding publisher dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.item.metadata.clearPublisherForm();
		    YAHOO.ur.item.metadata.newPublisherDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		
		        //get the response from adding a publisher
		        var response = o.responseText;
		        var publisherForm = document.getElementById('newPublisherDialogFields');
		    
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        publisherForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("newPublisherForm_success").value;
		    
		        //if the publisher was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.item.metadata.newPublisherDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the publisher was added
		            YAHOO.ur.item.metadata.newPublisherDialog.hide();
		            YAHOO.ur.item.metadata.clearPublisherForm();
		            YAHOO.ur.item.metadata.saveItemMetadataAndGetPublisher();
		        }
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('publisher submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new publisher button is clicked.
		YAHOO.ur.item.metadata.newPublisherDialog = new YAHOO.widget.Dialog('newPublisherDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );

	    // show and center the affilaition dialog box
	    YAHOO.ur.item.metadata.newPublisherDialog.showDialog = function()
	    {
	       YAHOO.ur.item.metadata.newPublisherDialog.center();
	       YAHOO.ur.item.metadata.newPublisherDialog.show();
	    } 
	    		
		// Submit form	
		YAHOO.ur.item.metadata.newPublisherDialog.submit = function()
		{   
		    YAHOO.util.Connect.setForm('newPublisherForm');
		    
		    	    
		    if( YAHOO.ur.item.metadata.newPublisherDialog.validate() )
		    {
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            newPublisherAction, callback);
	        }
	    }
	        	   
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.item.metadata.newPublisherDialog.validate = function() {
		    var name = document.getElementById('newPublisherForm_name').value;
			if (name == "" || name == null) {
			    alert('A Content type name must be entered');
				return false;
			} else {
				return true;
			}
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.item.metadata.newPublisherDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("show_publisher", "click", 
		    YAHOO.ur.item.metadata.newPublisherDialog.showDialog, 
		    YAHOO.ur.item.metadata.newPublisherDialog, true);
	
	
	},
	
	/*
	 * Show the form to enter the externally published data
	 */
	showPublisherForm : function() 
	{
		var publisherDiv = document.getElementById("publisherDiv");
		publisherDiv.style.visibility = 'visible'; 
	},
	
	/*
	 * Hide the externally published data form
	 */
	hidePublisherForm : function()
	{
		var publisherDiv = document.getElementById("publisherDiv");
		publisherDiv.style.visibility = 'hidden'; 
	},
	
	/*
	 * Show the calender
	 */
	showCalendar : function()
	{
	
		YAHOO.ur.item.metadata.cal1 = new YAHOO.widget.Calendar("cal1Container",{
	            iframe:false,          // Turn iframe off, since container has iframe support.
	            hide_blank_weeks:true  // Enable, to demonstrate how we handle changing height, using changeContent
	        });
		 
		var handleSelect = function(type,args,obj) {
		
			var dates = args[0];
			var date = dates[0];
			var year = date[0], month = date[1], day = date[2];
	
			document.getElementById("itemForm_publishedDate_day").value = day;
			document.getElementById("itemForm_publishedDate_month").value = month;
			document.getElementById("itemForm_publishedDate_year").value = year;	
		}
	
		var dialog = new YAHOO.widget.Dialog("containerDialog", {
	            context:["show", "tl", "bl"],
	           
	            width:"250px",  // Sam Skin dialog needs to have a width defined (7*2em + 2*1em = 16em).
	           
	            draggable:false,
	            close:true
	        });
	        
	   	
		YAHOO.ur.item.metadata.cal1.render();
		
		dialog.render();
		
		// Using dialog.hide() instead of visible:false is a workaround for an IE6/7 container known issue with border-collapse:collapse.
	    dialog.hide();
	
		YAHOO.ur.item.metadata.cal1.selectEvent.subscribe(handleSelect, YAHOO.ur.item.metadata.cal1, true);
		
		YAHOO.util.Event.on("show", "click", dialog.show, dialog, true);
	
	},
	
	
	/*
	 * Show the calender
	 */
	showReleaseCalendar : function()
	{
	
		YAHOO.ur.item.metadata.cal2 = new YAHOO.widget.Calendar("cal2Container",{
	            iframe:false,          // Turn iframe off, since container has iframe support.
	            hide_blank_weeks:true  // Enable, to demonstrate how we handle changing height, using changeContent
	        });
		 
		var handleSelect = function(type,args,obj) {
		
			var dates = args[0];
			var date = dates[0];
			var year = date[0], month = date[1], day = date[2];
	
			document.getElementById("itemForm_releaseDate_day").value = day;
			document.getElementById("itemForm_releaseDate_month").value = month;
			document.getElementById("itemForm_releaseDate_year").value = year;		
		}
	
		var dialog = new YAHOO.widget.Dialog("containerDialog2", {
	            context:["show_release_calendar", "tl", "bl"],
	           
	            width:"250px",  // Sam Skin dialog needs to have a width defined (7*2em + 2*1em = 16em).
	           
	            draggable:false,
	            close:true
	        });
	        
	   	
		YAHOO.ur.item.metadata.cal2.render();
		
		dialog.render();
		
		// Using dialog.hide() instead of visible:false is a workaround for an IE6/7 container known issue with border-collapse:collapse.
	    dialog.hide();
	
		YAHOO.ur.item.metadata.cal2.selectEvent.subscribe(handleSelect, YAHOO.ur.item.metadata.cal2, true);
		
		YAHOO.util.Event.on("show_release_calendar", "click", dialog.show, dialog, true);
	
	},

	/*
	 * Show the calender for created date
	 */
	showCreatedDateCalendar : function()
	{
		
		YAHOO.ur.item.metadata.cal4 = new YAHOO.widget.Calendar("cal4Container",{
	            iframe:false,          // Turn iframe off, since container has iframe support.
	            hide_blank_weeks:true  // Enable, to demonstrate how we handle changing height, using changeContent
	        });
		 
		var handleSelect = function(type,args,obj) {
		
			var dates = args[0];
			var date = dates[0];
			var year = date[0], month = date[1], day = date[2];
	
			document.getElementById("itemForm_createdDate_day").value = day;
			document.getElementById("itemForm_createdDate_month").value = month;
			document.getElementById("itemForm_createdDate_year").value = year;		
		}
	
		var dialog = new YAHOO.widget.Dialog("containerDialog4", {
	            context:["show_createdDate_calendar", "tl", "bl"],
	           
	            width:"250px",  // Sam Skin dialog needs to have a width defined (7*2em + 2*1em = 16em).
	           
	            draggable:false,
	            close:true
	        });
	        
	   	
		YAHOO.ur.item.metadata.cal4.render();
		
		dialog.render();
		
		// Using dialog.hide() instead of visible:false is a workaround for an IE6/7 container known issue with border-collapse:collapse.
	    dialog.hide();
	
		YAHOO.ur.item.metadata.cal4.selectEvent.subscribe(handleSelect, YAHOO.ur.item.metadata.cal4, true);
		
		YAHOO.util.Event.on("show_createdDate_calendar", "click", dialog.show, dialog, true);
	
	},
	
	/*
	 * Show the calender
	 */
	showDateAvailableCalendar :function()
	{
	
		YAHOO.ur.item.metadata.cal3 = new YAHOO.widget.Calendar("cal3Container",{
	            iframe:false,          // Turn iframe off, since container has iframe support.
	            hide_blank_weeks:true  // Enable, to demonstrate how we handle changing height, using changeContent
	        });
		 
		var handleSelect = function(type,args,obj) {
		
			var dates = args[0];
			var date = dates[0];
			var year = date[0], month = date[1], day = date[2];
	
			document.getElementById("itemForm_dateAvailable_day").value = day;
			document.getElementById("itemForm_dateAvailable_month").value = month;
			document.getElementById("itemForm_dateAvailable_year").value = year;
		}
	
		var dialog = new YAHOO.widget.Dialog("containerDialog3", {
	            context:["show_available_calendar", "tl", "bl"],
	           
	            width:"250px",  // Sam Skin dialog needs to have a width defined (7*2em + 2*1em = 16em).
	           
	            draggable:false,
	            close:true
	        });
	        
	   	
		YAHOO.ur.item.metadata.cal3.render();
		
		dialog.render();
		
		// Using dialog.hide() instead of visible:false is a workaround for an IE6/7 container known issue with border-collapse:collapse.
	    dialog.hide();
	
		YAHOO.ur.item.metadata.cal3.selectEvent.subscribe(handleSelect, YAHOO.ur.item.metadata.cal3, true);
		
		YAHOO.util.Event.on("show_available_calendar", "click", dialog.show, dialog, true);
	
	},
	
	/*
	 * Add title row
	 */
	addSubTitleRow : function() 
	{
	
	        var divToUpdate = document.getElementById('title_forms');
	                
	        // id to give to the table
	    	document.getElementById("title_table_id").value = parseInt(document.getElementById("title_table_id").value) + 1; 
	    	var tableId = 'title_table_' + document.getElementById("title_table_id").value;
	
			// Create table row for Title
	 		mytable     = document.createElement("table");
	 		mytable.setAttribute("id", "title_table_" + document.getElementById("title_table_id").value);
	 		mytable.setAttribute("class", "noPaddingTable");
	        mytablebody = document.createElement("tbody");
	        
	        // creates a <tr> element
	        mycurrent_row = document.createElement("tr");
	
	        // creates a <td> element
	        mycurrent_cell1 = document.createElement("td");
	        mycurrent_cell1.setAttribute("width", "80%");
	
	        // creates a Text Node
	        currenttext = document.createElement("input");
	        currenttext.setAttribute("type", "text");
	        currenttext.setAttribute("name", "subTitles");
	        currenttext.setAttribute("size", "103");
	        
	        // appends the Text Node we created into the cell <td>
	        mycurrent_cell1.appendChild(currenttext);
	        // appends the cell <td> into the row <tr>
	        mycurrent_row.appendChild(mycurrent_cell1);
	
	
	        // creates a <td> element
	        mycurrent_cell2 = document.createElement("td");
	        mycurrent_cell2.setAttribute("width", "18%");
	
	        // creates a Button
	        button = document.createElement("input");
	        button.setAttribute("type", "button");
	        button.setAttribute("value", "Remove SubTitle");
	        button.setAttribute("class", "ur_button");
	        button.setAttribute("onclick", "javascript:YAHOO.ur.item.metadata.removeSubTitle('" + tableId + "')");
	        
	        // appends the Text Node we created into the cell <td>
	        mycurrent_cell2.appendChild(button);
	        // appends the cell <td> into the row <tr>
	        mycurrent_row.appendChild(mycurrent_cell2);
	
	        // appends the row <tr> into <tbody>
	        mytablebody.appendChild(mycurrent_row);
	
	        // appends <tbody> into <table>
	        mytable.appendChild(mytablebody);
	        
	        divToUpdate.appendChild(mytable);
	
	},
	
	/*
	 * Removes the title
	 */
	removeSubTitle : function(tableId)
	{
	        var table_div = document.getElementById("title_forms");
	
	        var child = document.getElementById(tableId);
	        table_div.removeChild(child); 
	        
	},
	    
	    
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	
	 	YAHOO.ur.item.metadata.createNewSeriesDialog();
		YAHOO.ur.item.metadata.createNewSponsorDialog();
		YAHOO.ur.item.metadata.createNewIdentifierTypeDialog();
		YAHOO.ur.item.metadata.createNewExtentTypeDialog();
		YAHOO.ur.item.metadata.createNewPublisherDialog();
		
		if (document.getElementById("itemForm_isExternallyPublished").checked) {
			YAHOO.ur.item.metadata.showPublisherForm();
		}
		else {
			YAHOO.ur.item.metadata.hidePublisherForm();
			
		} 
	
	   YAHOO.ur.item.metadata.showCalendar();
	   YAHOO.ur.item.metadata.showReleaseCalendar();
	   YAHOO.ur.item.metadata.showCreatedDateCalendar();
	   YAHOO.ur.item.metadata.showDateAvailableCalendar();
	    
	
	}
} 

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.item.metadata.init);