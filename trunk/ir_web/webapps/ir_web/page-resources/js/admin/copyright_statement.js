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
YAHOO.namespace("ur.copyright.statement");

// action to perform when submitting the personal copyrigght statements.
var myCopyrightStatementAction = basePath + 'admin/getCopyrightStatements.action';

// actions for adding and removing folders
var updateCopyrightStatementAction = basePath + 'admin/updateCopyrightStatement.action';
var newCopyrightStatementAction = basePath + 'admin/createCopyrightStatement.action';
var deleteCopyrightStatementAction = basePath + 'admin/deleteCopyrightStatement.action';
var getCopyrightStatementAction = basePath + 'admin/getCopyrightStatement.action';

// object to hold the specified copyrigght statement data.
var myCopyrightStatementTable = new  YAHOO.ur.table.Table('myCopyrightStatements', 'newCopyrightStatements');

/**
 * contributor namespace
 */
YAHOO.ur.copyright.statement = {

	/**
	 *  Function that retireves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getCopyrightStatements : function(rowStart, startPageNumber, currentPageNumber, order)
	{
		 var callback =
         {
		    success: function(o) 
		    {
		    	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
		            var divToUpdate = document.getElementById('newCopyrightStatements');
		            divToUpdate.innerHTML = o.responseText;
		        } 
		    },
			
			failure: function(o) 
			{
			    alert('Get copyrigght statement Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        myCopyrightStatementAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&orderType=' + order 
	        					+ '&bustcache='+new Date().getTime(), 
	        callback, null);		
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the new copyrigght statement form
	  */
	clearCopyrightStatementForm : function()
	{
	    // clear out any errors
        var div = document.getElementById('error');
        div.innerHTML = "";
        
        document.getElementById('newCopyrightStatementForm_id').value="";
		document.getElementById('newCopyrightStatementForm_name').value="";
		document.getElementById('newCopyrightStatementForm_description').value="";
		document.getElementById('newCopyrightStatementForm_text').value="";
	
		document.newCopyrightStatement.newCopyrightStatement.value = "true";
	},
	
	/**
	 * Creates a YUI new copyright statement modal dialog for when a user wants to create 
	 * a new copyright statement
	 *
	 */
	createNewCopyrightStatementDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() {
			this.submit();
		};
		
			
		// handle a cancel of the adding copyrigght statement dialog
		var handleCancel = function() {
		    YAHOO.ur.copyright.statement.copyrightStatementDialog.hide();
		    YAHOO.ur.copyright.statement.clearCopyrightStatementForm();
		};
		
		var handleSuccess = function(o) {
			YAHOO.ur.util.wait.waitDialog.hide();
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {		       
		        //get the response from adding a department
	            var response = o.responseText;
	            var copyrightStatementForm = document.getElementById('contributorTypeDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            copyrightStatementForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newCopyrightForm_success").value;
		    
		        //if the copyrigght statement was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.copyright.statement.copyrightStatementDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the copyrigght statement was added
		            YAHOO.ur.copyright.statement.copyrightStatementDialog.hide();
		            YAHOO.ur.copyright.statement.clearCopyrightStatementForm();
		        }
		        myCopyrightStatementTable.submitForm(myCopyrightStatementAction);
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
			YAHOO.ur.util.wait.waitDialog.hide();
		    alert('copyrigght statement submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new copyrigght statement button is clicked.
		YAHOO.ur.copyright.statement.copyrightStatementDialog = new YAHOO.widget.Dialog('newCopyrightStatementDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
		YAHOO.ur.copyright.statement.copyrightStatementDialog.submit = function()
		{  
			
		    YAHOO.util.Connect.setForm('newCopyrightStatement');
		    
		    if( YAHOO.ur.copyright.statement.copyrightStatementDialog.validate() )
		    {
		    	YAHOO.ur.util.wait.waitDialog.showDialog();
			    //based on what we need to do (update or create a 
			    // new copyrigght statement) based on the action.
		        var action = newCopyrightStatementAction;
			    if( document.newCopyrightStatement.newCopyrightStatement.value != 'true')
			    {
			       action = updateCopyrightStatementAction;
			    }
		
		        var cObj = YAHOO.util.Connect.asyncRequest('post',
		        action, callback);		
		    }
		};
			
	    //center and show the copyrightStatementDialog
	    YAHOO.ur.copyright.statement.copyrightStatementDialog.showDialog = function()
	    {
	        YAHOO.ur.copyright.statement.copyrightStatementDialog.center();
	        YAHOO.ur.copyright.statement.copyrightStatementDialog.show();
	    };
	    
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.copyright.statement.copyrightStatementDialog.validate = function() 
		{
		
		    var name = document.getElementById('newCopyrightStatementForm_name').value;

			if (name == "" || name == null) {
			    alert('Copyright statement name must be entered');
				return false;
			} else {
				return true;
			}
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.copyright.statement.copyrightStatementDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showCopyrightStatement", "click", 
		    YAHOO.ur.copyright.statement.copyrightStatementDialog.showDialog, 
		    YAHOO.ur.copyright.statement.copyrightStatementDialog, true);
	},
	
    /**
     * function to edit copyrigght statement information
     */
    editCopyrightStatement : function(id)
    {	    
	    /*
         * This call back updates the html when a editing a content type
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('contributorTypeDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                   	document.newCopyrightStatement.newCopyrightStatement.value = "false";
	                YAHOO.ur.copyright.statement.copyrightStatementDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get copyrigght statement failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getCopyrightStatementAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
     },
	
	 /** 
	  * clear out any form data messages or input
	  * in the delete form
	  */
	clearDeleteCopyrightStatementForm : function()
	{
	    var copyrightStatementError = document.getElementById('newCopyrightStatementForm_nameError');
	    var div = document.getElementById('copyrightStatementError');
		//clear out any error information
		if( copyrightStatementError != null )
		{
		    if( copyrightStatementError.innerHTML != null 
		        || copyrightStatementError.innerHTML != "")
		    { 
		        div.removeChild(copyrightStatementError);
		    }
		}
	},
	
	/**
	 * Sets up the form and the delete
	 */
	deleteCopyright : function(id)
	{
	    document.getElementById('delete_id').value=id;	
	    YAHOO.ur.copyright.statement.deleteCopyrightStatementDialog.showDialog();
	},
	
	/**
	 * Creates a YUI new copyright statement modal dialog for when a user wants to delete 
	 * a copyright statement
	 *
	 */
	createDeleteCopyrightStatementDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() {
		    YAHOO.util.Connect.setForm('deleteCopyrightStatement');
		    
		    //delete the copyrigght statement
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deleteCopyrightStatementAction, callback);
		};
		
			
		// handle a cancel of deleting copyrigght statement dialog
		var handleCancel = function() {
		    YAHOO.ur.copyright.statement.deleteCopyrightStatementDialog.hide();
		};
		
		var handleSuccess = function(o) {
		
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a copyrigght statement
		        var response = eval("("+o.responseText+")");
		    
		        //if the copyrigght statement was not deleted then show the user the error message.
		        // received from the server
		        if( response.deleted == "false" )
		        {
		            var deleteCopyrightStatementError = document.getElementById('form_deleteCopyrightStatementError');
	                deleteCopyrightStatementError.innerHTML = '<p id="newDeleteCopyrightStatementError">' 
	                + response.message + '</p>';
	                YAHOO.ur.copyright.statement.deleteCopyrightStatementDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the copyrigght statements were deleted
		            YAHOO.ur.copyright.statement.deleteCopyrightStatementDialog.hide();
		            YAHOO.ur.copyright.statement.clearDeleteCopyrightStatementForm();
		        }
		        // reload the table
		        myCopyrightStatementTable.submitForm(myCopyrightStatementAction);
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('copyright statement delete submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new copyrigght statement button is clicked.
		YAHOO.ur.copyright.statement.deleteCopyrightStatementDialog = new YAHOO.widget.Dialog('deleteCopyrightStatementDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
	     
	    //center and show the delete dialog
	    YAHOO.ur.copyright.statement.deleteCopyrightStatementDialog.showDialog = function()
	    {
	        YAHOO.ur.copyright.statement.deleteCopyrightStatementDialog.center();
	        YAHOO.ur.copyright.statement.deleteCopyrightStatementDialog.show();
	    }
	   
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.copyright.statement.deleteCopyrightStatementDialog.render();
	
	},
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	    YAHOO.ur.copyright.statement.getCopyrightStatements(0,1,1,'asc');
	    YAHOO.ur.copyright.statement.createNewCopyrightStatementDialog();
	    YAHOO.ur.copyright.statement.createDeleteCopyrightStatementDialog();
	}	
}	

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.copyright.statement.init);