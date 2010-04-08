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
YAHOO.namespace("ur.extent.type");

// action to perform when submitting the personal extent types.
var myExtentTypeAction = basePath + 'admin/getExtentTypes.action';

// actions for adding and removing folders
var updateExtentTypeAction = basePath + 'admin/updateExtentType.action';
var newExtentTypeAction = basePath + 'admin/createExtentType.action';
var deleteExtentTypeAction = basePath + 'admin/deleteExtentType.action';
var getExtentTypeAction = basePath + 'admin/getExtentType.action';

// object to hold the specified extent type data.
var myExtentTypeTable = new YAHOO.ur.table.Table('myExtentTypes', 'newExtentTypes');

YAHOO.ur.extent.type = 
{

    getExtentTypes : function (rowStart, startPageNumber, currentPageNumber, order)
    {
        /*
         * This call back updates the html when a new extent type is
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
                     var divToUpdate = document.getElementById('newExtentTypes');
                     divToUpdate.innerHTML = o.responseText; 
                 }
             },
	
	         failure: function(o) 
	         {
	             alert('Get extent type Failure ' + o.status + ' status text ' + o.statusText );
	         }
         }
         var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            myExtentTypeAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(), 
            callback, null);
    },
     
    /** 
     * clear out any form data messages or input
     * in the new extent type form
     */
    clearExtentTypeForm : function()
    {
	    //clear out any error information
        var div = document.getElementById('extentTypeError');
        div.innerHTML = "";

	    document.getElementById('newExtentTypeForm_name').value="";
	    document.getElementById('newExtentTypeForm_description').value="";
	    document.getElementById('newExtentTypeForm_id').value="";
	    document.newExtentType.newExtentType.value = "true";
    }, 
    
    /**
     * Check or uncheck all extent type ids
     */
    setCheckboxes : function()
    {
        checked = document.myExtentTypes.checkAllSetting.checked;
        var extentTypeIds = document.getElementsByName('extentTypeIds');
        urUtil.setCheckboxes(extentTypeIds, checked);
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
	        YAHOO.ur.extent.type.extentTypeDialog.hide();
	        YAHOO.ur.extent.type.clearExtentTypeForm();
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
                    YAHOO.ur.extent.type.extentTypeDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the extent type was added
	                YAHOO.ur.extent.type.extentTypeDialog.hide();
	                YAHOO.ur.extent.type.clearExtentTypeForm();
	        
	            }
	            myExtentTypeTable.submitForm(myExtentTypeAction);
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) {
	        alert('extent type submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new extent type button is clicked.
	    YAHOO.ur.extent.type.extentTypeDialog = new YAHOO.widget.Dialog('newExtentTypeDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					      { text:'Cancel', handler:handleCancel } ]
		    } );
		
		// override the submit function
		YAHOO.ur.extent.type.extentTypeDialog.submit = function()
		{

		    YAHOO.util.Connect.setForm('newExtentType');

	        //based on what we need to do (update or create a 
	        // new extent type) based on the action.
            var action = newExtentTypeAction;

            if( YAHOO.ur.extent.type.validate() )
	        {

	            if( document.newExtentType.newExtentType.value != 'true')
	            {
	                action = updateExtentTypeAction;
	            }
	            var cObj = YAHOO.util.Connect.asyncRequest('post', action, callback);
	        }
            
		}
		
        //center and show the dialog box 
        YAHOO.ur.extent.type.extentTypeDialog.showDialog = function()
        {
            YAHOO.ur.extent.type.extentTypeDialog.show();
            YAHOO.ur.extent.type.extentTypeDialog.center();
        }
 	
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.extent.type.validate = function() 
	    {
	        var name = document.getElementById('newExtentTypeForm_name').value;

		    if (name == "" || name == null) 
		    {
		        alert('An Extent type name must be entered');
			    return false;
		    } 
		    else 
		    {
			    return true;
		    }
	    };

	    // Wire up the success and failure handlers
	     var callback = { success: handleSuccess,
					      failure: handleFailure };
			
			
	    // Render the Dialog
	    YAHOO.ur.extent.type.extentTypeDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showExtentType", "click", 
	        YAHOO.ur.extent.type.extentTypeDialog.showDialog, 
	        YAHOO.ur.extent.type.extentTypeDialog, true);
    },
    

     /**
     * function to edit extent type information
     */
     editExtentType : function(id)
    {	    
	    /*
         * This call back updates the html when a editing an extent type
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('extent_type_form_fields');
                    divToUpdate.innerHTML = o.responseText; 
                   	document.newExtentType.newExtentType.value = "false";
	                YAHOO.ur.extent.type.extentTypeDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('edit extent types failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getExtentTypeAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
     },
    
    /*
     * Clear the form of any error messages
     */
    clearDeleteExtentTypeForm : function()
    {
        var div = document.getElementById('extentTypeError');
        div.innerHTML = "";
    }, 
    
    /**
     * Creates a YUI new extent type modal dialog for when a user wants to delete 
     * an extent type
     *
     */
    createDeleteExtentTypeDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function()
	    {
	        YAHOO.util.Connect.setForm('myExtentTypes');
	    
	        //delete the extent type
            var cObj = YAHOO.util.Connect.asyncRequest('post',
            deleteExtentTypeAction, callback);
	    };
	
		
	    // handle a cancel of deleting extent type dialog
	    var handleCancel = function() {
	        YAHOO.ur.extent.type.deleteExtentTypeDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            //get the response from adding a extent type
	            var response = eval("("+o.responseText+")");
	    
	            //if the extent type was not deleted then show the user the error message.
	            // received from the server
	            if( response.extentTypeDeleted == "false" )
	            {
	                var deleteExtentTypeError = document.getElementById('form_deleteExtentTypeError');
                    deleteExtentTypeError.innerHTML = '<p id="newDeleteExtentTypeError">' 
                    + response.message + '</p>';
                    YAHOO.ur.extent.type.deleteExtentTypeDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the extent types were deleted
	                YAHOO.ur.extent.type.deleteExtentTypeDialog.hide();
	                YAHOO.ur.extent.type.clearDeleteExtentTypeForm();
	            }
	            // reload the table
	            myExtentTypeTable.submitForm(myExtentTypeAction);
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) {
	        alert('delete extent type submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new extent type button is clicked.
	    YAHOO.ur.extent.type.deleteExtentTypeDialog = new YAHOO.widget.Dialog('deleteExtentTypeDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
					  { text:'No', handler:handleCancel } ]
		    } );
		
        // center and show the dialog
        YAHOO.ur.extent.type.deleteExtentTypeDialog.showDialog = function()
        {
            YAHOO.ur.extent.type.deleteExtentTypeDialog.center();
            YAHOO.ur.extent.type.deleteExtentTypeDialog.show();        
        }
    
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
			
	    // Render the Dialog
	    YAHOO.ur.extent.type.deleteExtentTypeDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showDeleteExtentType", "click", 
	        YAHOO.ur.extent.type.deleteExtentTypeDialog.showDialog, 
	        YAHOO.ur.extent.type.deleteExtentTypeDialog, true);
    }, 
    
    init : function()
    {
        YAHOO.ur.extent.type.getExtentTypes(0, 1, 1, 'asc');
        YAHOO.ur.extent.type.createNewExtentTypeDialog();
        YAHOO.ur.extent.type.createDeleteExtentTypeDialog();
    }
    
    
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.extent.type.init);