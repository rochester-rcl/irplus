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
YAHOO.namespace("ur.person.names");

// action to perform when submitting the person name 
var myPersonNamesAction = basePath + 'user/getPersonNames.action';


// actions for adding and removing person names
var newPersonNameAction = basePath + 'user/createPersonName.action';
var deletePersonNameAction = basePath + 'user/deletePersonName.action';
var updatePersonNameAction = basePath + 'user/updatePersonName.action';

var deleteIdentifierAction = basePath + 'admin/deletePersonNameAuthorityIdentifier.action';

// actions for adding people
var newPersonAction = basePath + 'user/createPerson.action';

// object to hold the specified person type data.
var myPersonNamesTable = new YAHOO.ur.table.Table('myPersonNames', 'personNames');

/**
 * person namespace
 */
YAHOO.ur.person.names = {

	/**
	 *  Function that retireves person name information
	 *  based on the given person id.
	 *
	 *  The person id used to get the person
	 */
	getPersonNames : function(personId)
	{
		var callback =
		{
		    success: function(o) 
		    {
		    	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
		            var divToUpdate = document.getElementById('personNames');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get person Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	
		var personId = document.getElementById('newPersonNameForm_personId').value;
	    
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
		        myPersonNamesAction + '?personId='+ personId + '&bustcache='+new Date().getTime(), 
		        callback, null);	
	},	
	
	 /** 
	  * clear out any form data messages or input
	  * in the new person type form
	  */
	clearPersonNameForm : function()
	{
        // clear out any errors
        var div = document.getElementById('personError');
        div.innerHTML = "";
        	
	    document.newPersonNameForm.newPersonName.value = "true";
		document.getElementById("newPersonNameForm_id").value = "";
		document.getElementById("newPersonNameFormFirstName").value = "";
		document.getElementById("newPersonNameFormLastName").value = "";
		document.getElementById("newPersonNameFormMiddleName").value = "";
		document.getElementById("newPersonNameFormFamilyName").value = "";
		document.getElementById("newPersonNameFormInitials").value = "";
		document.getElementById("newPersonNameFormNumeration").value = "";
		document.getElementById("newPersonNameFormAuthoritative").checked = false;
	},
	
    /**
     * Set all person id's form
     */
    setCheckboxes : function()
    {
        checked = document.myPersonNames.checkAllSetting.checked;
        var personNameIds = document.getElementsByName('personNameIds');
        urUtil.setCheckboxes(personNameIds, checked);
        
    },
	
	/**
	 * Creates a YUI new person type modal dialog for when a user wants to create 
	 * a new person
	 *
	 */
	createNewPersonNameDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
		   this.submit();
		};
		
		// Define various event handlers for Dialog
		var addPersonForUser = function() 
		{
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            newPersonAction, personCallback);
		};
		
			
		// handle a cancel of the adding person type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.person.names.newPersonNameDialog.hide();
		    YAHOO.ur.person.names.clearPersonNameForm();
		};
		
		var handleNewPersonNameFormSuccess = function(o) 
		{
			YAHOO.ur.util.wait.waitDialog.hide();

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
	                YAHOO.ur.person.names.newPersonNameDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the person type was added
		            YAHOO.ur.person.names.newPersonNameDialog.hide();
		            YAHOO.ur.person.names.clearPersonNameForm();
		        }
		        myPersonNamesTable.submitForm(myPersonNamesAction);
		    }
		};
	
		// handle form submission failure
		var handleNewPersonNameFormFailure = function(o) 
		{
			YAHOO.ur.util.wait.waitDialog.hide();
		    alert('person name submission failed ' + o.status  + ' status text ' + o.statusText);
		};
	
		// Adds new person to a user
		var handleNewPersonForUserFormSuccess = function(o) 
		{
			YAHOO.ur.util.wait.waitDialog.hide();
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
	                YAHOO.ur.person.names.newPersonNameDialog.show();
		        }
		        else
		        {
		            // we can clear the form if the person type was added
		            YAHOO.ur.person.names.newPersonNameDialog.hide();
		            YAHOO.ur.person.names.clearPersonNameForm();
			        document.getElementById('newPersonNameForm_personId').value = response.personId; 
			        YAHOO.ur.person.names.getPersonNames(response.personId);
			    }
			}
		};
		
		// handle form sbumission failure
		var handleNewPersonForUserFormFailure = function(o) {
			YAHOO.ur.util.wait.waitDialog.hide();
		    alert('person submission failed ' + o.status  + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new person type button is clicked.
		YAHOO.ur.person.names.newPersonNameDialog = new YAHOO.widget.Dialog('newPersonNameDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
		YAHOO.ur.person.names.newPersonNameDialog.submit = function()
		{
			
		    YAHOO.util.Connect.setForm('newPersonNameForm');
		    	    
		    if( YAHOO.ur.person.names.newPersonNameDialog.validate() )
		    {
		    	YAHOO.ur.util.wait.waitDialog.showDialog();
				// If person doesnot exist then create person and add to the user
		    	if (document.getElementById('newPersonNameForm_personId').value == '') {
		   		 	addPersonForUser();
		   		} else {
			        //based on what we need to do (update or create a 
			        // new person type) based on the action.
		            var action = newPersonNameAction;
			        if( document.newPersonNameForm.newPersonName.value != 'true')
			        {
			           action = updatePersonNameAction;
			        }   
	
		            var cObj = YAHOO.util.Connect.asyncRequest('post',
		            action, callback);
		        }
	        }
	      }
			
	
		// show and center the dialog box
	    YAHOO.ur.person.names.newPersonNameDialog.showDialog = function()
	    {
	        YAHOO.ur.person.names.newPersonNameDialog.center();
	        YAHOO.ur.person.names.newPersonNameDialog.show();
	    }
			
	 
	 	// Validate the entries in the form 
		YAHOO.ur.person.names.newPersonNameDialog.validate = function() 
		{
			
	    	if (document.getElementById('newPersonNameFormLastName').value == '') {
	    		alert('Please enter Last name');
	    		return false;
	    	}
	
			return true;
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleNewPersonNameFormSuccess,
											         failure: handleNewPersonNameFormFailure };
											         
		// Wire up the success and failure handlers
		var personCallback = { success: handleNewPersonForUserFormSuccess,
											         failure: handleNewPersonForUserFormFailure };
		
				
		// Render the Dialog
		YAHOO.ur.person.names.newPersonNameDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showPersonName", "click", 
		    YAHOO.ur.person.names.newPersonNameDialog.showDialog, 
		    YAHOO.ur.person.names.newPersonNameDialog, true);
	},
	
    // function to allow for the editing of an existing folder
	editPersonName : function(personId,
	                   id,
	                   surname, 
	                   forename, 
	                   middleName, 
	                   familyName, 
	                   initials,
	                   numeration,
	                   authoritativeId)
	{
		document.newPersonNameForm.newPersonName.value = "false";
	    document.getElementById("newPersonNameFormFirstName").value = forename;
	    document.getElementById("newPersonNameFormLastName").value = surname;
	    document.getElementById("newPersonNameFormMiddleName").value = middleName;
	    document.getElementById("newPersonNameFormFamilyName").value = familyName;
	    document.getElementById("newPersonNameFormInitials").value = initials;
	    document.getElementById("newPersonNameFormNumeration").value = numeration;
	    document.getElementById("newPersonNameForm_personId").value = personId;
	    document.getElementById("newPersonNameForm_id").value = id;
	    if( authoritativeId == id )
	    {
	        document.getElementById("newPersonNameFormAuthoritative").checked = true;
	    }
	    else
	    {
	        document.getElementById("newPersonNameFormAuthoritative").checked = false;
	    }
	    YAHOO.ur.person.names.newPersonNameDialog.showDialog();
	},
	
	/**
	 * Creates a YUI new person type modal dialog for when a user wants to create 
	 * a new person type
	 *
	 */
	createDeletePersonNameDialog : function()
	{
	
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
		    YAHOO.util.Connect.setForm('myPersonNames');
		    
		    //delete the person type
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deletePersonNameAction, callback);
		};
		
			
		// handle a cancel of deleting person type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.person.names.deletePersonNameDialog.hide();
		};
		
		// handle deleting a person form success.
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a person type
		        var response = eval("("+o.responseText+")");
	
		        //if the person type was not deleted then show the user the error message.
		        // received from the server
		        if( response.personNameDeleted == "false" )
		        {
		    	    YAHOO.ur.person.names.deletePersonNameDialog.hide();
		            var deletePersonNameError = document.getElementById('deletePersonNameMessage');
	                deletePersonNameError.innerHTML = '<p id="newDeletePersonNameError">' 
	                + response.message + '</p>';
	            
	                YAHOO.ur.person.names.deletePersonNameMessageDialog.showDialog();
	            
		        }
		        else
		        {
		            // we can clear the form if the person types were deleted
		            YAHOO.ur.person.names.deletePersonNameDialog.hide();
		            YAHOO.ur.person.names.clearDeletePersonNameForm();
		        }
		        // reload the table
		        myPersonNamesTable.submitForm(myPersonNamesAction);
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('delete person submission failed ' + o.status  + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new person button is clicked.
		YAHOO.ur.person.names.deletePersonNameDialog = new YAHOO.widget.Dialog('deletePersonNameDialog', 
	        { width : "400px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
			
		// show and center the dialog box
	    YAHOO.ur.person.names.deletePersonNameDialog.showDialog = function()
	    {
	        YAHOO.ur.person.names.deletePersonNameDialog.center();
	        YAHOO.ur.person.names.deletePersonNameDialog.show();
	    }
			
	   
		// Wire up the success and failure handlers
		callback = { success: handleSuccess, failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.person.names.deletePersonNameDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showDeletePersonName", "click", 
		    YAHOO.ur.person.names.deletePersonNameDialog.showDialog, 
		    YAHOO.ur.person.names.deletePersonNameDialog, true);
		    
	
	},
	
	/** 
	  * clear out any form data messages or input
	  * in the new person type form
	  */
	clearDeletePersonNameForm : function()
	{
	    var personTypeError = document.getElementById('newPersonForm_nameError');
	    var div = document.getElementById('personError');
		//clear out any error information
		if( personTypeError != null )
		{
		    if( personTypeError.innerHTML != null 
		        || personTypeError.innerHTML != "")
		    { 
		        div.removeChild(personTypeError);
		    }
		}
	},
	
	createDeletePersonNameMessageDialog : function() 
	{	
	
		// handle message from deleting a person form success.
		var handleOk = function(o) {
			YAHOO.ur.person.names.deletePersonNameMessageDialog.hide();
		}
	
	    
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new person button is clicked.
		YAHOO.ur.person.names.deletePersonNameMessageDialog = new YAHOO.widget.Dialog('deletePersonNameMessageDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Ok', handler:handleOk, isDefault:true } ]
			} );
				
		// show and center the dialog box
	    YAHOO.ur.person.names.deletePersonNameMessageDialog.showDialog = function()
	    {
	        YAHOO.ur.person.names.deletePersonNameMessageDialog.center();
	        YAHOO.ur.person.names.deletePersonNameMessageDialog.show()
	    }
	    
		// Render the Dialog
		YAHOO.ur.person.names.deletePersonNameMessageDialog.render();
		    
	},
	
	
	/**
	 * Creates a mac content type
	 *
	 */
	createDeleteIdentifierDialog : function()
	{
		// redirect with delete
		var handleIdentifierSubmit = function() {
		    window.location = deleteIdentifierAction + '?id=' + deleteIdentifierId ;
		};
		
			
		// handle a cancel of deleting copyrigght statement dialog
		var handleIdentifierCancel = function() {
			YAHOO.ur.person.names.deleteIdentifierDialog.hide();
		};
		
		
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new copyrigght statement button is clicked.
		YAHOO.ur.person.names.deleteIdentifierDialog = new YAHOO.widget.Dialog('deleteIdentifierDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleIdentifierSubmit, isDefault:true },
						  { text:'No', handler:handleIdentifierCancel } ]
			} );
	     
	    //center and show the delete dialog
		YAHOO.ur.person.names.deleteIdentifierDialog.showDialog = function()
	    {
			YAHOO.ur.person.names.deleteIdentifierDialog.center();
			YAHOO.ur.person.names.deleteIdentifierDialog.show();
	    }
	   
				
		// Render the Dialog
		YAHOO.ur.person.names.deleteIdentifierDialog.render();
	
	},
	
	/**
	 * Sets up the form and the delete
	 */
	deleteIdentifierMapping : function(id)
	{
	    deleteIdentifierId = id;	
	    YAHOO.ur.person.names.deleteIdentifierDialog.showDialog();
	},
	
    	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() {
	
	    YAHOO.ur.person.names.getPersonNames();
	    YAHOO.ur.person.names.createNewPersonNameDialog();
	    YAHOO.ur.person.names.createDeletePersonNameDialog();
	    YAHOO.ur.person.names.createDeletePersonNameMessageDialog();
	    YAHOO.ur.person.names.createDeleteIdentifierDialog();
	 }	
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.person.names.init);