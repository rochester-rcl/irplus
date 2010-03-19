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
YAHOO.namespace("ur.person");

// action to perform when submitting the person information
var myPersonAction = basePath + 'admin/getPersons.action';
var mySearchPersonAction = basePath + 'admin/searchPerson.action';

// actions for adding and removing people
var newPersonAction = basePath + 'user/createPerson.action';
var deletePersonAction = basePath + 'user/deletePerson.action';
var updatePersonAction = basePath + 'user/updatePerson.action';
var getPersonAction = basePath + 'user/getPerson.action';

// object to hold the specified person type data.
var myPersonTable = new YAHOO.ur.table.Table('myPersons', 'newPersons');



/**
 * person namespace
 */
YAHOO.ur.person = {
		
	/**
	 * function to edit field information
	 */
	get : function(id)
	{	    
	    /*
	     * This call back updates the html when a editing a field
	     */
	    var callback =
	    {
	        success: function(o) 
	        {
	        	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
	                var divToUpdate = document.getElementById('new_person_fields');
	                divToUpdate.innerHTML = o.responseText; 
	               	document.newPersonForm.newPerson.value = "false";
	               	YAHOO.ur.person.newPersonDialog.showDialog();
	            }
	        },
		
	        failure: function(o) 
	        {
	            alert('edit person id failed ' + o.status + ' status text ' + o.statusText );
	        }
	    };
	        
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	    	getPersonAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
	        callback, null);
	},		


	/**
	 *  Function that retireves person information
	 *  based on the given person id.
	 *
	 *  The person id used to get the folder.
	 */
	getPersons : function (rowStart, startPageNumber, currentPageNumber, sortElement, sortType)
	{
		/*
		 * This call back updates the html when a new person is
		 * retrieved.
		 */
		var callback =
		{
		    success: function(o) 
		    {
		        // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
		            var divToUpdate = document.getElementById('newPersons');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get person Failure ' + o.status + ' status text ' + o.statusText );
			}
			
		}
		
		var transaction = YAHOO.util.Connect.asyncRequest('GET', 
		        myPersonAction + '?rowStart=' + rowStart +  '&startPageNumber=' + startPageNumber + '&currentPageNumber=' + currentPageNumber
	        	+ '&sortElement=' + sortElement + '&sortType=' + sortType + '&bustcache=' + new Date().getTime(), 
		        callback, null);
	},

	
	 /** 
	  * clear out any form data messages or input
	  * in the new person type form
	  */
	clearPersonForm : function()
	{
	
		// clear out any errors
        var div = document.getElementById('personError');
        div.innerHTML = "";
        
		document.getElementById("person_first_name").value = "";
		document.getElementById("person_last_name").value = "";
		document.getElementById("person_middle_name").value = "";
		document.getElementById("person_family_name").value = "";
		document.getElementById("person_initials").value = "";
		document.getElementById("person_numeration").value = "";
		document.getElementById("person_birthdate_year").value = "";
		document.getElementById("person_deathdate_year").value = "";
		document.getElementById('newPersonForm_auth_id').value = "";
		document.getElementById('newPersonForm_id').value = "";
		document.getElementById("new_person").value = "true";
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
			
		// handle a cancel of the adding person type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.person.clearPersonForm();
		    YAHOO.ur.person.newPersonDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
		    // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	        	//get the response from adding a field
	            var response = o.responseText;
	            var personForm = document.getElementById('new_person_fields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            personForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("name_added").value;
		    
		        //if the person type was not added then show the user the error message.
		        // received from the server
		        if( response.personAdded == "false" )
		        {
		            var personNameError = document.getElementById('personError');
	                personNameError.innerHTML = '<p id="newPersonForm_Error">' + response.message + '</p>';
	                YAHOO.ur.person.newPersonDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the person type was added
		            YAHOO.ur.person.clearPersonForm();
		            YAHOO.ur.person.newPersonDialog.hide();
		        }
		        myPersonTable.submitForm(myPersonAction);

		        // re-execute the query if user is in search page
		        var query = document.getElementById("search_person_query").value;
		        if(query != null)
		        {
		        	YAHOO.ur.person.searchPerson(0,1,1);
		        }
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('person submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new person type button is clicked.
		YAHOO.ur.person.newPersonDialog = new YAHOO.widget.Dialog('newPersonDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
		
		// Submit the form			
		YAHOO.ur.person.newPersonDialog.submit = function() {
		
		    YAHOO.util.Connect.setForm('newPersonForm');
		    	    
		    if( YAHOO.ur.person.newPersonDialog.validate() )
		    {
		        //based on what we need to do (update or create a 
		        // new person type) based on the action.
	            var action = newPersonAction;
		        if( document.newPersonForm.newPerson.value != 'true')
		        {
		           action = updatePersonAction;
		        }
	
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            action, callback);
	        }
	    }			
	   
	   	 // show and center the series dialog
        YAHOO.ur.person.newPersonDialog.showDialog = function()
        {
            YAHOO.ur.person.newPersonDialog.center();
            YAHOO.ur.person.newPersonDialog.show();
        }
        
	 	// Validate the entries in the form 
		YAHOO.ur.person.newPersonDialog.validate = function() {
	    	if (document.getElementById('person_last_name').value == '') {
	    		alert('Please enter Last name');
	    		return false;
	    	}
	
			return true;
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.person.newPersonDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showPerson", "click", 
		    YAHOO.ur.person.newPersonDialog.showDialog, 
		    YAHOO.ur.person.newPersonDialog, true);
	},
	    

	 /** 
	  * clear out any form data messages or input
	  * in the new person type form
	  */
	clearDeletePersonForm : function()
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
	
	/**
	 * Creates a YUI new person type modal dialog for when a user wants to create 
	 * a new person type
	 *
	 */
	createDeletePersonDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			
		    YAHOO.util.Connect.setForm('deletePersonForm');
		    
		    //delete the person type
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deletePersonAction, callback);
		};
			
		// handle a cancel of deleting person type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.person.deletePersonDialog.hide();
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
		        if( response.personDeleted == "false" )
		        {
		    	    YAHOO.ur.person.deletePersonDialog.hide();
		        
		            var deletePersonError = document.getElementById('deletePersonMessage');
	                deletePersonError.innerHTML = '<p id="newDeletePersonMessage">' 
	                + response.message + '</p>';
	            
	                YAHOO.ur.person.deletePersonMessageDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the person types were deleted
		            YAHOO.ur.person.clearDeletePersonForm();
		            YAHOO.ur.person.deletePersonDialog.hide();
		        }
		        // reload the table
		        myPersonTable.submitForm(myPersonAction);
		        
		        var query = document.getElementById("search_person_query").value;
		        if(query != null)
		        {
		        	YAHOO.ur.person.searchPerson(0,1,1);
		        }
		        
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('delete person submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new person button is clicked.
		YAHOO.ur.person.deletePersonDialog = new YAHOO.widget.Dialog('deletePersonDialog', 
	        { width : "400px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
			
		// show and center the dialog box
	    YAHOO.ur.person.deletePersonDialog.showDialog = function()
	    {
	        YAHOO.ur.person.deletePersonDialog.center();
	        YAHOO.ur.person.deletePersonDialog.show();
	    }
	       
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
		// Render the Dialog
		YAHOO.ur.person.deletePersonDialog.render();

	},
	
	/**
	 * Delete a person
	 */
	deletePerson : function(personId)
	{
		document.getElementById("delete_person_id").value = personId;
		YAHOO.ur.person.deletePersonDialog.showDialog();
	},
	
	createDeletePersonMessageDialog : function() {	
	
		// handle message from deleting a person form success.
		var handleOk = function(o) {
			YAHOO.ur.person.deletePersonMessageDialog.hide();
		}
	
	    
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new person button is clicked.
		YAHOO.ur.person.deletePersonMessageDialog = new YAHOO.widget.Dialog('deletePersonMessageDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Ok', handler:handleOk, isDefault:true } ]
			} );
				
		// show and center the dialog box
	    YAHOO.ur.person.deletePersonMessageDialog.showDialog = function()
	    {
	        YAHOO.ur.person.deletePersonMessageDialog.center();
	        YAHOO.ur.person.deletePersonMessageDialog.show()
	    }
	    
		// Render the Dialog
		YAHOO.ur.person.deletePersonMessageDialog.render();
		    
	},

	/**
	 * Search user
	 */
	searchPerson : function(rowStart, startPageNumber, currentPageNumber) {

		var callback =
		{
		    success: function(o) 
		    {
		        // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
		            var divToUpdate = document.getElementById('search_results_div');
		            divToUpdate.innerHTML = o.responseText;
		        } 
		    },
			
			failure: function(o) 
			{
			    alert('Get person search results  failure ' + o.status + ' status text ' + o.statusText );
			}
		}
		YAHOO.util.Connect.setForm('personSearchForm');
		
	    var transaction = YAHOO.util.Connect.asyncRequest('POST', 
	        mySearchPersonAction + '?rowStart=' + rowStart +  '&startPageNumber=' + startPageNumber + '&currentPageNumber=' + currentPageNumber, 
	        callback, null);			
	},	
	
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() {
	    YAHOO.ur.person.getPersons(0,1,1,'surname','asc');
	    YAHOO.ur.person.createNewPersonDialog();
	    YAHOO.ur.person.createDeletePersonDialog();
	    YAHOO.ur.person.createDeletePersonMessageDialog();
	    var myTabs = new YAHOO.widget.TabView("person-tabs");
	}	
}	

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.person.init);