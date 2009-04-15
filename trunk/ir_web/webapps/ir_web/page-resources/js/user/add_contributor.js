
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
 * This code is for dealing with adding contributors to the item
 */
YAHOO.namespace("ur.item.contributor");

// action to perform for searching names
var mySearchNameAction =  basePath + 'user/searchNameAction.action';

// Action to get contributors for the item
var getContributorsAction = basePath + 'user/getContributors.action';

// Action to add, remove contributor to the item
var removeContributorAction = basePath + 'user/removeContributor.action';
var addNameAction = basePath + 'user/addPersonName.action';
var addContributorTypeAction =  basePath + 'user/addContributorType.action';

//Action to move the file up/down
var moveContributorDownAction = basePath + 'user/moveContributorDown.action';
var moveContributorUpAction = basePath + 'user/moveContributorUp.action';

// Action to goto other screens
var viewAddMetadataAction = basePath + 'user/viewItemMetadata.action';
var viewAddFilesAction = basePath + 'user/viewEditItem.action';
var publicationWorkspaceAction = basePath + 'user/workspace.action?showCollectionTab=true';
var viewInstitutionalItemAction = basePath + 'institutionalPublicationPublicView.action';
var previewPublicationAction = basePath + 'user/previewPublication.action';

// Action to add Person / Person name
var newPersonNameAction = basePath + 'user/addNewPersonName.action';
var newPersonAction = basePath + 'user/addNewPerson.action';

/**
 * Name space for adding contributor to publication
 */
YAHOO.ur.item.contributor = {
	
	/**
	 * Function to save item contributors and goto the Publication preview
	 *
	 */
	gotoPreview : function()
	{
		document.searchForm.action = previewPublicationAction;
	    document.searchForm.submit();
	},
	
	/**
	 * Function to save item contributors and goto the Publication table
	 *
	 */
	finishLater : function()
	{
		if (document.contributors_form.institutionalItemId.value != '') {
			document.contributors_form.action = viewInstitutionalItemAction;
		} else {
			document.contributors_form.action = publicationWorkspaceAction;;
		}	
	    document.contributors_form.submit();
	},
	
	/*
	 * This call back updates the html when the names are retrieved
	 * 
	 */
	getNameCallback : 
	{
	    success: function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               	    
	            var divToUpdate = document.getElementById('newNames');
	            divToUpdate.innerHTML = o.responseText; 
	        }
	    },
		
		failure: function(o) 
		{
		    alert('Get name Failure ' + o.status + ' status text ' + o.statusText );
		}
	},

	/**
	 *  Function that searches the names
	 * 
	 */
	getNames : function()
	{
		var formObject = document.getElementById('names');
		YAHOO.util.Connect.setForm(formObject);
		
	    var transaction = YAHOO.util.Connect.asyncRequest('post', 
	        mySearchNameAction, 
	        YAHOO.ur.item.contributor.getNameCallback);
	},
		
	/**
	 *  Function that searches the names
	 *  and returns from start row and end row 
	 */
	getNamesForPager : function(rowStart, startPageNumber, currentPageNumber)
	{
		document.myNames.rowStart.value=rowStart;
		document.myNames.startPageNumber.value=startPageNumber;
		document.myNames.currentPageNumber.value=currentPageNumber;
		
		var formObject = document.getElementById('names');
		YAHOO.util.Connect.setForm(formObject);
		
	    var transaction = YAHOO.util.Connect.asyncRequest('post', 
	        mySearchNameAction, 
	        YAHOO.ur.item.contributor.getNameCallback);
	},


		
	//handles the serarch form submit
	handleSearchFormSubmit : function()
	{
		    var formObject = document.getElementById('search_form');
			YAHOO.util.Connect.setForm(formObject);
			    
		    if( YAHOO.ur.item.contributor.validate() )
		    {
		        var cObj = YAHOO.util.Connect.asyncRequest('POST',
		        mySearchNameAction, YAHOO.ur.item.contributor.getNameCallback);
		    }
	},
	
	validate : function() 
	{
	    	var name = document.getElementById('search_query').value;
	
			if (name == "" || name == null) {
			    alert('Please enter search text');
				return false;
			} else {
				return true;
			}
			
	},
		     
	/**
	 * Function to add contributor to the item
	 *
	 */
	addName : function(personNameId) 
	{ 
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               		
		        var response = eval("("+o.responseText+")");
		    
		        //if the name was not added then show the user the error message.
		        // received from the server
		        if( response.nameAdded == "true" )
		        {
				    YAHOO.ur.item.contributor.getNames();
				    YAHOO.ur.item.contributor.getContributors();
		        }
		        else
		        {
		    	    alert('Adding name to the item failed');
		        }
		    }
		};

		// handle form submission failure
		var handleFailure = function(o) {
		    alert('content type submission failed ' + o.status);
		};

		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
		YAHOO.util.Connect.setForm('myNames');
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           addNameAction + '?personNameId=' + personNameId, callback);
	},
	
	/**
	 * Function to remove name
	 *
	 */
	removeContributor : function(contributorId)
	{
	
		YAHOO.util.Connect.setForm('contributors_form');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           removeContributorAction + '?contributorId=' + contributorId, YAHOO.ur.item.contributor.getContributorCallback);
	           
		YAHOO.ur.item.contributor.getNames();
	},
	
		
	/*
	 * This call back updates the html when a name is selected
	 */
	getContributorCallback : 
	{
	    success: function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               	    
	            var divToUpdate = document.getElementById('item_contributors');
	            divToUpdate.innerHTML = o.responseText;
	        } 
	    },
		
		failure: function(o) 
		{
		    alert('Get name Failure ' + o.status + ' status text ' + o.statusText );
		}
	},
	
	/**
	 *  Function that retireves the contributors from the 
	 *  given publication id.
	 *
	 *  
	 */
	getContributors : function()
	{
		YAHOO.util.Connect.setForm('contributors_form');
	    var transaction = YAHOO.util.Connect.asyncRequest('POST', 
	        getContributorsAction , 
	        YAHOO.ur.item.contributor.getContributorCallback, null);
	},
	
	/**
	 *  Function that add the contributor type
	 *  to  the contributor
	 *
	 */
	addContributorType : function(object, contributorId) {
		
		YAHOO.util.Connect.setForm('contributors_form');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           addContributorTypeAction + '?contributorId=' + contributorId + "&contributorTypeId=" + object.value, 
	           YAHOO.ur.item.contributor.getContributorCallback);
	           
	},
	
	/**
	 *  Function that moves the contributor up
	 *
	 */
	moveUp : function(contributorId) 
	{
	
		YAHOO.util.Connect.setForm('contributors_form');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           moveContributorUpAction + '?contributorId=' + contributorId , YAHOO.ur.item.contributor.getContributorCallback);
	           
	},
	
	/**
	 *  Function that moves the contributor down
	 *
	 */
	moveDown : function(contributorId) 
	{
	
		YAHOO.util.Connect.setForm('contributors_form');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           moveContributorDownAction + '?contributorId=' + contributorId , YAHOO.ur.item.contributor.getContributorCallback);
	           
	},
	
	/**
	 * Function to goto add files screen
	 *
	 */
	viewAddFiles : function()
	{
		document.contributors_form.action = viewAddFilesAction;
	    document.contributors_form.submit();
	},
	
	/**
	 * Function to goto metadata screen
	 *
	 */
	viewAddMetadata : function()
	{
		document.contributors_form.action = viewAddMetadataAction;
	    document.contributors_form.submit();
	},
	
	/** 
	  * clear out any form data messages or input
	  * in the new person type form
	  */
	clearPersonNameForm : function ()
	{
        // clear out the error message
        var personError = document.getElementById('personNameError');
        personError.innerHTML = "";
				document.getElementById("newPersonNameFormFirstName").value = "";
		document.getElementById("newPersonNameFormLastName").value = "";
		document.getElementById("newPersonNameFormMiddleName").value = "";
		document.getElementById("newPersonNameFormFamilyName").value = "";
		document.getElementById("newPersonNameFormInitials").value = "";
		document.getElementById("newPersonNameFormNumeration").value = "";
		document.getElementById("newPersonNameFormAuthoritative").checked = false;
	},
	
	/**
	 * Creates a YUI new person type modal dialog for when a user wants to create 
	 * a new person name
	 *
	 */
	createNewPersonNameDialog : function(){
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();		   
		};
		
			
		// handle a cancel of the adding person type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.item.contributor.newPersonNameDialog.hide();
		    YAHOO.ur.item.contributor.clearPersonNameForm();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               	
		        //get the response from adding a person type
		        var response = eval("("+o.responseText+")");

		        //if the person type was not added then show the user the error message.
		        // received from the server
		        if( response.personNameAdded == "false" )
		        {
		            var personNameError = document.getElementById('personNameError');
	                personNameError.innerHTML = '<p id="newPersonForm_Error">' + response.message + '</p>';
	                YAHOO.ur.item.contributor.newPersonNameDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the person type was added
		            YAHOO.ur.item.contributor.clearPersonNameForm();
		            YAHOO.ur.item.contributor.newPersonNameDialog.hide();
	        	    YAHOO.ur.item.contributor.getNames();
		        }
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) 
		{
		    alert('person name submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new person type button is clicked.
		YAHOO.ur.item.contributor.newPersonNameDialog = new YAHOO.widget.Dialog('newPersonNameDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
	
	    // show and center the dialog box
	    YAHOO.ur.item.contributor.newPersonNameDialog.showDialog = function()
	    {
	        YAHOO.ur.item.contributor.newPersonNameDialog.center();
	        YAHOO.ur.item.contributor.newPersonNameDialog.show()
	    }

	    // submit the form
	    YAHOO.ur.item.contributor.newPersonNameDialog.submit = function()
	    {
		    YAHOO.util.Connect.setForm('newPersonNameForm');
		    	    
		    if( YAHOO.ur.item.contributor.newPersonNameDialog.validate() )
		    {
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            newPersonNameAction, callback);
	        } 
		}
	   
	 	// Validate the entries in the form 
		YAHOO.ur.item.contributor.newPersonNameDialog.validate = function() {
		    return true
		};
	
		// Wire up the success and failure handlers
		callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.item.contributor.newPersonNameDialog.render();
	
	},

    // listener for showing the dialog when clicked.
	addPersonName : function(authoritativeNameId) 
	{
		document.getElementById('newPersonNameForm_personId').value = authoritativeNameId;
		YAHOO.ur.item.contributor.newPersonNameDialog.showDialog();
	}, 
		
	 /** 
	  * clear out any form data messages or input
	  * in the new person type form
	  */
	clearPersonForm : function()
	{
        // clear out the error message
        var personError = document.getElementById('personError');
        personError.innerHTML = "";
	
		document.getElementById("person_first_name").value = "";
		document.getElementById("person_last_name").value = "";
		document.getElementById("person_middle_name").value = "";
		document.getElementById("person_family_name").value = "";
		document.getElementById("person_initials").value = "";
		document.getElementById("person_numeration").value = "";
		document.getElementById("person_birthdate_year").value = "";
		document.getElementById("person_deathdate_year").value = "";
		document.getElementById("person_myName").checked = false;
		
	},
	
	/**
	 * Creates a YUI new person type modal dialog for when a user wants to create 
	 * a new person
	 *
	 */
	createNewPersonDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
		// Define various event handlers for Dialog
		var addPersonNameForUser = function() 
		{
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            newPersonNameAction, personNameCallback);
		};	
			
		// handle a cancel of the adding person type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.item.contributor.newPersonDialog.hide();
		    YAHOO.ur.item.contributor.clearPersonForm();
		};
		
		var handleNewPersonFormSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               
		        //get the response from adding a person type
		        var response = eval("("+o.responseText+")");

		        //if the person type was not added then show the user the error message.
		        // received from the server
		        if( response.personAdded == "false" )
		        {
		            var personNameError = document.getElementById('personError');
	                personNameError.innerHTML = '<p id="newPersonForm_Error">' + response.message + '</p>';
	                YAHOO.ur.item.contributor.newPersonDialog.showDialog();
		        }
		        else
		        {
				    if (document.newPersonForm.myName.checked) 
				    {
					    document.newPersonForm.personId.value = response.personNameAuthorityId;
				    }
					    
		            // we can clear the form if the person type was added
		            YAHOO.ur.item.contributor.newPersonDialog.hide();
		            YAHOO.ur.item.contributor.clearPersonForm();
		            YAHOO.ur.item.contributor.addName(response.personNameId);
		        }
		    }
		};
		
		// handle form sbumission failure
		var handleNewPersonFormFailure = function(o) 
		{
		    alert('person submission failed ' + o.status);
		};
	
		var handleNewPersonNameForUserFormSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               		
		        //get the response from adding a person type
		        var response = eval("("+o.responseText+")");
	
		        //if the person type was not added then show the user the error message.
		        // received from the server
		        if( response.personNameAdded == "false" )
		        {
		            var personNameError = document.getElementById('personError');
	                personNameError.innerHTML = '<p id="newPersonForm_Error">' + response.message + '</p>';
	                YAHOO.ur.item.contributor.newPersonDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the person type was added
		            YAHOO.ur.item.contributor.newPersonDialog.hide();
		            YAHOO.ur.item.contributor.clearPersonForm();
	        	    YAHOO.ur.item.contributor.addName(response.personNameId);
		        }
		    }
		};
		
		// handle form sbumission failure
		var handleNewPersonNameForUserFormFailure = function(o) {
		    alert('person name submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new person type button is clicked.
		YAHOO.ur.item.contributor.newPersonDialog = new YAHOO.widget.Dialog('newPersonDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
	
	    // show and center the dialog box
	    YAHOO.ur.item.contributor.newPersonDialog.showDialog = function()
	    {
	        YAHOO.ur.item.contributor.newPersonDialog.center();
	        YAHOO.ur.item.contributor.newPersonDialog.show()
	    }

	    // Submit form
	    YAHOO.ur.item.contributor.newPersonDialog.submit = function()
	    {
		    YAHOO.util.Connect.setForm('newPersonForm');
		    	    
		    if( YAHOO.ur.item.contributor.newPersonDialog.validate() )
		    {
	
				// If My name is true and person authoritative name already exist, then add it as Person name
		    	if ((document.newPersonForm.myName.checked) && (document.newPersonForm.personId.value != '')) {
		    	 	addPersonNameForUser();
		    	} else {
		            var cObj = YAHOO.util.Connect.asyncRequest('post',
		            newPersonAction, callback);
		        }
	        }
		};
	   
	 	// Validate the entries in the form 
		YAHOO.ur.item.contributor.newPersonDialog.validate = function() {
		    return true;
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleNewPersonFormSuccess,
											         failure: handleNewPersonFormFailure };
											         
		// Wire up the success and failure handlers
		var personNameCallback = { success: handleNewPersonNameForUserFormSuccess,
											         failure: handleNewPersonNameForUserFormFailure };										         
				
				
		// Render the Dialog
		YAHOO.ur.item.contributor.newPersonDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("new_person", "click", 
		    YAHOO.ur.item.contributor.newPersonDialog.showDialog, 
		    YAHOO.ur.item.contributor.newPersonDialog, true);
		    
	    
	
	},
	
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	
		YAHOO.ur.item.contributor.createNewPersonNameDialog();
		YAHOO.ur.item.contributor.createNewPersonDialog();
		
		YAHOO.ur.item.contributor.getContributors();
		YAHOO.util.Event.addListener("search_button", "click", 
		    YAHOO.ur.item.contributor.handleSearchFormSubmit); 
	
	}
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.item.contributor.init);
