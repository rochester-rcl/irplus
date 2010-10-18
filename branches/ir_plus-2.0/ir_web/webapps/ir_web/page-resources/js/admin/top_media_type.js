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
YAHOO.namespace("ur.topMediaType");

// action to perform when submitting the personal content types.
var myTopMediaTypeAction = basePath + 'admin/getTopMediaTypes.action';

// actions for adding and removing top media types
var updateTopMediaTypeAction = basePath + 'admin/updateTopMediaType.action';
var newTopMediaTypeAction = basePath + 'admin/createTopMediaType.action';
var deleteTopMediaTypeAction = basePath + 'admin/deleteTopMediaType.action';
var getTopMediaTypeAction = basePath + 'admin/getTopMediaType.action';

// object to hold the specified content type data.
var myTopMediaTypesTable = new YAHOO.ur.table.Table('myTopMediaTypes', 'newTopMediaTypes');


YAHOO.ur.topMediaType = {

    /**
     *  Function that retireves content information
     *  based on the given content id.
     *
     *  The top media type id used to get the folder.
     */
    getTopMediaTypes : function(rowStart, startPageNumber, currentPageNumber, order)
    {
        /*
         * This call back updates the html when a new content type is
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
                    var divToUpdate = document.getElementById('newTopMediaTypes');
                    divToUpdate.innerHTML = o.responseText; 
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get top media type Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            myTopMediaTypeAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(), 
            callback, null);
    }, 
    
    /**
     * Clear the top media type form
     */
    clearTopMediaTypeForm : function()
    {
        var div = document.getElementById('topMediaTypeError');
        div.innerHTML = "";
	    document.getElementById('newTopMediaTypeForm_name').value = "";
	    document.getElementById('newTopMediaTypeForm_description').value = "";
	    document.getElementById('newTopMediaTypeForm_id').value = "";
	    document.newTopMediaTypeForm.newTopMediaType.value = "true";
    },
    
    /**
     * Sets all checkboxes to checked
     */
    setCheckboxes : function()
    {
        checked = document.myTopMediaTypes.checkAllSetting.checked;
        var topMediaTypeIds = document.getElementsByName('topMediaTypeIds');
        urUtil.setCheckboxes(topMediaTypeIds, checked);
    },
    
    /**
     * Creates a new media type dialog
     */
    createNewTopMediaTypeDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	       this.submit();
	    };
	
	    // handle a cancel of the adding top media type dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.topMediaType.clearTopMediaTypeForm();
	        YAHOO.ur.topMediaType.newTopMediaTypeDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               
	    	    //get the response from adding a content type
	            var response = o.responseText;
	            var topMediaTypeForm = document.getElementById('newTopMediaTypeDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            topMediaTypeForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newTopMediaTypeForm_success").value;
	    
	            //if the top media type was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.topMediaType.newTopMediaTypeDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the top media type was added
	                YAHOO.ur.topMediaType.newTopMediaTypeDialog.hide();
	                YAHOO.ur.topMediaType.clearTopMediaTypeForm();
	            }
	            myTopMediaTypesTable.submitForm(myTopMediaTypeAction);
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert('top media type submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new top media type button is clicked.
	    YAHOO.ur.topMediaType.newTopMediaTypeDialog = new YAHOO.widget.Dialog('newTopMediaTypeDialog', 
            { width : "600px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					      { text:'Cancel', handler:handleCancel } ]
		    } );
		
		// override the submit function
        YAHOO.ur.topMediaType.newTopMediaTypeDialog.submit = function()
        {
            YAHOO.util.Connect.setForm('newTopMediaTypeForm');
	        if( YAHOO.ur.topMediaType.newTopMediaTypeDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new top media type) based on the action.
                var action = newTopMediaTypeAction;
	            if( document.newTopMediaTypeForm.newTopMediaType.value != 'true')
	            {
	               action = updateTopMediaTypeAction;
	            }

                var cObj = YAHOO.util.Connect.asyncRequest('post', action, callback);
             }
        }
        
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.topMediaType.newTopMediaTypeDialog.validate = function() 
	    {
	        var name = document.getElementById('newTopMediaTypeForm_name').value;
		    if (name == "" || name == null) 
		    {
		        alert('A top media type name must be entered');
			    return false;
		    }     
		    else 
		    {
			    return true;
		    }
	    };
	    
	    //center and show the dialog
	    YAHOO.ur.topMediaType.newTopMediaTypeDialog.showDialog = function()
	    {
	        YAHOO.ur.topMediaType.newTopMediaTypeDialog.center();
	        YAHOO.ur.topMediaType.newTopMediaTypeDialog.show();
	    };

	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
			
	    // Render the Dialog
	    YAHOO.ur.topMediaType.newTopMediaTypeDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showTopMediaType", "click", 
	        YAHOO.ur.topMediaType.newTopMediaTypeDialog.showDialog, 
	        YAHOO.ur.topMediaType.newTopMediaTypeDialog, true);
	    
    },
    
    /**
     * function to edit top media type information
     */
    editTopMediaType : function(id)
    {	    
	    /*
         * This call back updates the html when a editing a mime top media type
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {               
                    var divToUpdate = document.getElementById('newTopMediaTypeDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                    document.newTopMediaTypeForm.newTopMediaType.value = "false";
                    YAHOO.ur.topMediaType.newTopMediaTypeDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Edit top media type Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getTopMediaTypeAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
    },
    
    /**
     * Clear the delete form
     */
    clearDeleteTopMediaTypeForm : function()
    {
         var div = document.getElementById('topMediaTypeError');
         div.innerHTML = "";
    },
    
    /**
     * Create the delete dialog
     */
    createDeleteTopMediaTypeDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function()
	    {
	        YAHOO.util.Connect.setForm('myTopMediaTypes');
	    
	        //delete the top media type
            var cObj = YAHOO.util.Connect.asyncRequest('post',
            deleteTopMediaTypeAction, callback);
	    };
	
		
	    // handle a cancel of deleting top media type dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.topMediaType.deleteTopMediaTypeDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               
	            //get the response from adding a top media type
	            var response = eval("("+o.responseText+")");
	    
	            //if the top media type was not deleted then show the user the error message.
	            // received from the server
	            if( response.topMediaTypeDeleted == "false" )
	            {
	                var deleteTopMediaTypeError = document.getElementById('form_deleteTopMediaTypeError');
                    deleteTopMediaTypeError.innerHTML = '<p id="newDeleteTopMediaTypeError">' 
                    + response.message + '</p>';
                    YAHOO.ur.topMediaType.deleteTopMediaTypeDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the top media types were deleted
	                YAHOO.ur.topMediaType.clearDeleteTopMediaTypeForm();
	                YAHOO.ur.topMediaType.deleteTopMediaTypeDialog.hide();
	            }
	            // reload the table
	            myTopMediaTypesTable.submitForm(myTopMediaTypeAction);
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	        alert('delete top media type failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new top media type button is clicked.
	    YAHOO.ur.topMediaType.deleteTopMediaTypeDialog = new YAHOO.widget.Dialog('deleteTopMediaTypeDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
					      { text:'No', handler:handleCancel } ]
		    } );
		
		YAHOO.ur.topMediaType.deleteTopMediaTypeDialog.showDialog = function()
		{
		    YAHOO.ur.topMediaType.deleteTopMediaTypeDialog.center();
		    YAHOO.ur.topMediaType.deleteTopMediaTypeDialog.show();
		}
   
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.topMediaType.deleteTopMediaTypeDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showDeleteTopMediaType", "click", 
	        YAHOO.ur.topMediaType.deleteTopMediaTypeDialog.showDialog, 
	        YAHOO.ur.topMediaType.deleteTopMediaTypeDialog, true);
    },
    
    init : function()
    {
        YAHOO.ur.topMediaType.getTopMediaTypes(0,1,1,'asc');
        YAHOO.ur.topMediaType.createNewTopMediaTypeDialog();
        YAHOO.ur.topMediaType.createDeleteTopMediaTypeDialog();
    }   
}


// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.topMediaType.init);

