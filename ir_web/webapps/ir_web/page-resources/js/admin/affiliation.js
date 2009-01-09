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
YAHOO.namespace("ur.affiliation");

// action to perform when submitting the personal affiliations.
var myAffiliationAction = basePath + 'admin/getAffiliations.action';

// actions for adding and removing folders
var updateAffiliationAction = basePath + 'admin/updateAffiliation.action';
var newAffiliationAction = basePath + 'admin/createAffiliation.action';
var deleteAffiliationAction = basePath + 'admin/deleteAffiliation.action';

// object to hold the specified affiliation data.
var myAffiliationTable = new YAHOO.ur.table.Table('myAffiliations', 'newAffiliations');

YAHOO.ur.affiliation = {

    /**
     * Function to get all affilations
     */
    getAffiliations : function(rowStart, startPageNumber, currentPageNumber, order)
    {
         var callback =
         {
             success: function(o) 
             {
 	             // check for the timeout - forward user to login page if timout
	             // occured
	             if( !urUtil.checkTimeOut(o.responseText) )
	             {                    
                      var divToUpdate = document.getElementById('newAffiliations');
                     divToUpdate.innerHTML = o.responseText; 
                 }
             },
	
	         failure: function(o) 
	         {
	             alert('Get affiliation Failure ' + o.status + ' status text ' + o.statusText );
	         }
        }
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            myAffiliationAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(),  
            callback, null);
    },
    
    /**
     * clear the affilations form
     */
    clearNewAffiliationForm : function()
    {
        // clear out the error message
        var affiliationError = document.getElementById('affiliation_error_div');
        affiliationError.innerHTML = "";
        
	    document.getElementById('newAffiliationForm_name').value = "";
	    document.getElementById('newAffiliationForm_description').value = "";
	    document.getElementById('newAffiliationForm_id').value = "";
	    document.getElementById('newAffiliationForm_author').checked = false;
	    document.getElementById('newAffiliationForm_researcher').checked = false;
	    document.getElementById('newAffiliationForm_needsApproval').checked = false;
	    document.newAffiliationForm.newAffiliation.value = "true";
    }, 
    
    /**
     * Selects or unselects all affilation checkboxes
     */
    setCheckboxes : function()
    {
        checked = document.myAffiliations.checkAllSetting.checked;
        var affiliationIds = document.getElementsByName('affiliationIds');
        urUtil.setCheckboxes(affiliationIds, checked);
    },
    
    
    /**
     * Create a new affilation dialog box for creating / editing
     * affiliations
     */
    createNewAffiliationDialog : function()
    {
        // Define various event handlers for Dialog
	    var handleSubmit = function() {
	        this.submit();
	    };
	
	    // handle a cancel of the adding affiliation dialog
	    var handleCancel = function() 
	    {
	    	YAHOO.ur.affiliation.newAffiliationDialog.hide();
	        YAHOO.ur.affiliation.clearNewAffiliationForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
	        //get the response from adding a affiliation
	        var response = o.responseText;
	        
	         // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var affiliationForm = document.getElementById('newAffiliationDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            affiliationForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newAffiliationForm_success").value;
	    
	  
	            //if the affiliation was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.affiliation.newAffiliationDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the affiliation was added
	                YAHOO.ur.affiliation.newAffiliationDialog.hide();
	                YAHOO.ur.affiliation.clearNewAffiliationForm();
	            }
	            myAffiliationTable.submitForm(myAffiliationAction);
	        }
	    };

	    // handle form sbumission failure
	    var handleFailure = function(o) {
	        alert('affiliation submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new affiliation button is clicked.
	    YAHOO.ur.affiliation.newAffiliationDialog = new YAHOO.widget.Dialog('newAffiliationDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					      { text:'Cancel', handler:handleCancel } ]
		    } );
		
		//override the dialog submit function
		YAHOO.ur.affiliation.newAffiliationDialog.submit = function()
		{
		    YAHOO.util.Connect.setForm('newAffiliationForm');
	    	    
	        if( YAHOO.ur.affiliation.newAffiliationDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new affiliation) based on the action.
                var action = newAffiliationAction;
	            if( document.newAffiliationForm.newAffiliation.value != 'true')
	            {
	               action = updateAffiliationAction;
	            }

                var cObj = YAHOO.util.Connect.asyncRequest('post',
                action, callback);
            }
		};
		
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.affiliation.newAffiliationDialog.validate = function() {
	        var name = document.getElementById('newAffiliationForm_name').value;
		    if (name == "" || name == null) {
		        alert('An affiliation name must be entered');
			    return false;
		    } else {
			    return true;
		    }
	    };
	
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess,
                         failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.affiliation.newAffiliationDialog.render();

	    // show and center the affilaition dialog box
	    YAHOO.ur.affiliation.newAffiliationDialog.showDialog = function()
	    {
	       YAHOO.ur.affiliation.newAffiliationDialog.center();
	       YAHOO.ur.affiliation.newAffiliationDialog.show();
	    } 

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showAffiliation", "click", 
	        YAHOO.ur.affiliation.newAffiliationDialog.showDialog, 
	        YAHOO.ur.affiliation.newAffiliationDialog, true);
    },
     
     /**
      * Allow a user to edit an affilation
      */
    editAffiliation : function (id, name, description, author, researcher, needsApproval)
    {
        document.getElementById('newAffiliationForm_name').value = name;
	    document.getElementById('newAffiliationForm_description').value = description;
	    document.getElementById('newAffiliationForm_id').value = id;

	    if (author == "true") 
	    {
	        document.getElementById('newAffiliationForm_author').checked = true;
	    } 
	    else 
	    {
	        document.getElementById('newAffiliationForm_author').checked = false;
	    }

	    if (researcher == "true") 
	    {
	        document.getElementById('newAffiliationForm_researcher').checked= true;
	    } 
	    else 
	    {
	        document.getElementById('newAffiliationForm_researcher').checked= false;
	    }
	    
	    if (needsApproval == "true") 
	    {
	        document.getElementById('newAffiliationForm_needsApproval').checked= true;
	    } 
	    else 
	    {
	        document.getElementById('newAffiliationForm_needsApproval').checked= false;
	    }
	    
	    document.newAffiliationForm.newAffiliation.value = "false";
	    YAHOO.ur.affiliation.newAffiliationDialog.showDialog();
    
    },
    
    /**
     * clears the delete affiliation form data
     */
    clearDeleteAffiliationForm : function ()
    {
        var div = document.getElementById('affiliationError');
        div.innerHTML = "";
    },
    
    /**
     * Creates the delete affiliation dialog
     */
    createDeleteAffiliationDialog : function ()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('myAffiliations');
	    
	        //delete the affiliation
            var cObj = YAHOO.util.Connect.asyncRequest('post',
            deleteAffiliationAction, callback);
	    };
	
	    // handle a cancel of deleting affiliation dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.affiliation.deleteAffiliationDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            //get the response from adding a affiliation
	            var response = eval("("+o.responseText+")");
	    
	            //if the affiliation was not deleted then show the user the error message.
	            // received from the server
	            if( response.affiliationDeleted == "false" )
	            {
	                var deleteAffiliationError = document.getElementById('form_deleteAffiliationError');
                    deleteAffiliationError.innerHTML = '<p id="newDeleteAffiliationError">' 
                    + response.message + '</p>';
                    YAHOO.ur.affiliation.deleteAffiliationDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the affiliations were deleted
	                YAHOO.ur.affiliation.deleteAffiliationDialog.hide();
	                YAHOO.ur.affiliation.clearDeleteAffiliationForm();
	            }
	            // reload the table
	            myAffiliationTable.submitForm(myAffiliationAction);
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	        alert('affiliation submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new affiliation button is clicked.
	    YAHOO.ur.affiliation.deleteAffiliationDialog = new YAHOO.widget.Dialog('deleteAffiliationDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
					      { text:'No', handler:handleCancel } ]
		    } );
	
	    // show the dialog and center it
        YAHOO.ur.affiliation.deleteAffiliationDialog.showDialog = function()
        {
            YAHOO.ur.affiliation.deleteAffiliationDialog.show();
            YAHOO.ur.affiliation.deleteAffiliationDialog.center();
        }
    
	    // Wire up the success and failure handlers
	     var callback = { success: handleSuccess,
	                      failure: handleFailure };
			
			
	    // Render the Dialog
	    YAHOO.ur.affiliation.deleteAffiliationDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showDeleteAffiliation", "click", 
	        YAHOO.ur.affiliation.deleteAffiliationDialog.showDialog, 
	        YAHOO.ur.affiliation.deleteAffiliationDialog, true);
    
    },
    
    /**
     * Init function to be called when page loads
     */
    init : function()
    {
        YAHOO.ur.affiliation.getAffiliations(0,1,1,'asc');
        YAHOO.ur.affiliation.createNewAffiliationDialog();
        YAHOO.ur.affiliation.createDeleteAffiliationDialog();
    }
};



// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.affiliation.init);