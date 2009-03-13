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
 * This code is for dealing with adding and removing content types
 */
YAHOO.namespace("ur.content.type");

// action to perform when getting the personal content types.
var myContentTypeAction = basePath + 'admin/getContentTypes.action';

// actions for adding,updating and removing content types
var updateContentTypeAction = basePath + 'admin/updateContentType.action';
var newContentTypeAction = basePath + 'admin/createContentType.action';
var deleteContentTypeAction = basePath + 'admin/deleteContentType.action';
var getContentTypeAction = basePath + 'admin/getContentType.action';

// object to hold the specified content type data.
var myContentTypeTable = new YAHOO.ur.table.Table('myContentTypes', 'newContentTypes');


/**
 * content type namespace
 */
YAHOO.ur.content.type = {
	
	/**
	 *  Function that retireves content information
	 */
	getContentTypes : function(rowStart, startPageNumber, currentPageNumber, order)
	{
			var callback =
			{
			    success: function(o) 
			    {
			        // check for the timeout - forward user to login page if timout
	                // occured
	                if( !urUtil.checkTimeOut(o.responseText) )
	                {
			            var divToUpdate = document.getElementById('newContentTypes');
			            divToUpdate.innerHTML = o.responseText; 
			        }
			    },
				
				failure: function(o) 
				{
				    alert('Get content type Failure ' + o.status + ' status text ' + o.statusText );
				}
			}
			
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        myContentTypeAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(), 
	        callback, null);			
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the new content type form
	  */
	clearContentTypeForm : function()
	{
	    
	    // clear out any errors
        var div = document.getElementById('contentTypeError');
        div.innerHTML = "";
	    
	    document.getElementById('newContentTypeForm_name').value = "";
		document.getElementById('newContentTypeForm_description').value = "";
		document.getElementById('newContentTypeForm_id').value = "";
		document.newContentTypeForm.newContentType.value = "true";
	},
	
    /**
     * Selects or unselects all affilation checkboxes
     */
    setCheckboxes : function()
    {
        checked = document.myContentTypes.checkAllSetting.checked;
        var contentTypeIds = document.getElementsByName('contentTypeIds');
        urUtil.setCheckboxes(contentTypeIds, checked);
    },	
	
	
	/**
	 * Creates a YUI new content type modal dialog for when a user wants to create 
	 * a new content type
	 *
	 */
	createNewContentTypeDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
			
		// handle a cancel of the adding content type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.content.type.newContentTypeDialog.hide();
		    YAHOO.ur.content.type.clearContentTypeForm();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a content type
		        var response = o.responseText;
		        var contentTypeForm = document.getElementById('newContentTypeDialogFields');
		    
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        contentTypeForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("newContentTypeForm_success").value;
		    
		  
		        //if the content type was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.content.type.newContentTypeDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the content type was added
		            YAHOO.ur.content.type.newContentTypeDialog.hide();
		            YAHOO.ur.content.type.clearContentTypeForm();
		        }
		        myContentTypeTable.submitForm(myContentTypeAction);
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('content type submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new content type button is clicked.
		YAHOO.ur.content.type.newContentTypeDialog = new YAHOO.widget.Dialog('newContentTypeDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
		//submit form
		YAHOO.ur.content.type.newContentTypeDialog.submit = function() 
		{
		    YAHOO.util.Connect.setForm('newContentTypeForm');
		    	    
		    if( YAHOO.ur.content.type.newContentTypeDialog.validate() )
		    {
		        //based on what we need to do (update or create a 
		        // new content type) based on the action.
	            var action = newContentTypeAction;
		        if( document.newContentTypeForm.newContentType.value != 'true')
		        {
		           action = updateContentTypeAction;
		        }
	
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            action, callback);
	        }
	     }		

	    
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.content.type.newContentTypeDialog.validate = function() 
		{
		    var name = document.getElementById('newContentTypeForm_name').value;
			if (name == "" || name == null) {
			    alert('A Content type name must be entered');
				return false;
			} else {
				return true;
			}
		};			
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,   failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.content.type.newContentTypeDialog.render();			
			
	    // show and center the dialog box
	    YAHOO.ur.content.type.newContentTypeDialog.showDialog = function()
	    {
	        YAHOO.ur.content.type.newContentTypeDialog.center();
	        YAHOO.ur.content.type.newContentTypeDialog.show()
	    }

	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showContentType", "click", 
		    YAHOO.ur.content.type.newContentTypeDialog.showDialog, 
		    YAHOO.ur.content.type.newContentTypeDialog, true);
		    
	},
	
    /**
     * function to edit content type information
     */
    editContentType : function(id)
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
                    var divToUpdate = document.getElementById('newContentTypeDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                    document.newContentTypeForm.newContentType.value = "false";
                    YAHOO.ur.content.type.newContentTypeDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get content type failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getContentTypeAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
    },
    	
	 /** 
	  * clear out any form data messages or input
	  * in the new content type form
	  */
	clearDeleteContentTypeForm : function()
	{
	    var contentTypeError = document.getElementById('newContentTypeForm_nameError');
	    var div = document.getElementById('contentTypeError');
		//clear out any error information
		if( contentTypeError != null )
		{
		    if( contentTypeError.innerHTML != null 
		        || contentTypeError.innerHTML != "")
		    { 
		        div.removeChild(contentTypeError);
		    }
		}
	},
	
	/**
	 * Creates a YUI new content type modal dialog for when a user wants to create 
	 * a new content type
	 *
	 */
	createDeleteContentTypeDialog :function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
		    YAHOO.util.Connect.setForm('myContentTypes');
		    
		    //delete the content type
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deleteContentTypeAction, callback);
		};
		
			
		// handle a cancel of deleting content type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.content.type.deleteContentTypeDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a content type
		        var response = eval("("+o.responseText+")");
		    
		        //if the content type was not deleted then show the user the error message.
		        // received from the server
		        if( response.contentTypeDeleted == "false" )
		        {
		            var deleteContentTypeError = document.getElementById('form_deleteContentTypeError');
	                deleteContentTypeError.innerHTML = '<p id="newDeleteContentTypeError">' 
	                + response.message + '</p>';
	                YAHOO.ur.content.type.deleteContentTypeDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the content types were deleted
		            YAHOO.ur.content.type.clearDeleteContentTypeForm();
		            YAHOO.ur.content.type.deleteContentTypeDialog.hide();
		        }
		        // reload the table
		        myContentTypeTable.submitForm(myContentTypeAction);
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('content type submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new content type button is clicked.
		YAHOO.ur.content.type.deleteContentTypeDialog = new YAHOO.widget.Dialog('deleteContentTypeDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
			
	    // show and center the delete dialog
	    YAHOO.ur.content.type.deleteContentTypeDialog.showDialog = function()
	    {
	        YAHOO.ur.content.type.deleteContentTypeDialog.center();
	        YAHOO.ur.content.type.deleteContentTypeDialog.show();
	    }
	    
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.content.type.deleteContentTypeDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showDeleteContentType", "click", 
		    YAHOO.ur.content.type.deleteContentTypeDialog.showDialog, 
		    YAHOO.ur.content.type.deleteContentTypeDialog, true);
	},
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() {
	    YAHOO.ur.content.type.getContentTypes(0, 1, 1, 'asc');
	    YAHOO.ur.content.type.createNewContentTypeDialog();
	    YAHOO.ur.content.type.createDeleteContentTypeDialog();
	}
	
}	

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.content.type.init);