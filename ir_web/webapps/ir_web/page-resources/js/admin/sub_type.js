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
YAHOO.namespace("ur.subType");

var updateTopMediaTypeAction = basePath + 'admin/updateTopMediaType.action';


// action to perform when submitting the personal content types.
var mySubTypeAction = basePath + 'admin/allSubType.action';

// actions for adding and removing folders
var updateSubTypeAction = basePath + 'admin/updateSubType.action';
var newSubTypeAction = basePath + 'admin/createSubType.action';
var deleteSubTypeAction = basePath + 'admin/deleteSubType.action';
var editSubTypeAction = basePath + 'admin/getSubType.action';

// object to hold the specified sub type data.
var mySubTypesTable = new YAHOO.ur.table.Table('mySubTypes', 'newSubTypes');



YAHOO.ur.subType = 
{
    /**
     *  Function that retireves sub information
     *  based on the given mime type id.
     *
     *  The top media type id used to get the folder.
     */
    getSubTypes : function(rowStart, startPageNumber, currentPageNumber, order)
    {
        // This call back updates the html when a new sub type is retrieved
        var callback =
        {
            success: function(o) 
            {
                // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {               
                    var divToUpdate = document.getElementById('newSubTypes');
                    divToUpdate.innerHTML = o.responseText; 
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get top media type Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var topMediaTypeId = document.topMediaTypeData.id.value;
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            mySubTypeAction + '?id=' + topMediaTypeId +  '&rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(), 
            callback, null);
    },
    
     /** 
      * clear out any form data messages or input
      * in the new top media type form
      */
     clearSubTypeForm : function()
     {
        var div = document.getElementById('SubTypeError');
        div.innerHTML = "";
	
	    document.getElementById('newSubTypeForm_name').value = "";
	    document.getElementById('newSubTypeForm_description').value = "";
	    document.getElementById('newSubTypeForm_subTypeId').value = "";
 	    document.newSubTypeForm.newSubType.value = "true";
     
     },
     
     /**
      *  Check all check boxes for top media types
      */
     setCheckboxes : function()
     {
         checked = document.mySubTypes.checkAllSetting.checked;
         var subTypeIds = document.getElementsByName('subTypeIds');
         urUtil.setCheckboxes(subTypeIds, checked);
     },
     
     /**
      * Creates a new sub type dialog
      */
     createNewSubTypeDialog : function()
     {
     	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        this.submit();    
	    };
		
	    // handle a cancel of the adding top media type dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.subType.clearSubTypeForm();
	        YAHOO.ur.subType.newSubTypeDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
		    // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               	    
	            //get the response from adding a sub type
	            var response = o.responseText;
	            var SubTypeForm = document.getElementById('newSubTypeDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            SubTypeForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newSubTypeForm_success").value;
	    
	            //if the top media type was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                    YAHOO.ur.subType.newSubTypeDialog.show();
	            }
	            else
	            {
	                // we can clear the form if the top media type was added
	                YAHOO.ur.subType.clearSubTypeForm();
	                YAHOO.ur.subType.newSubTypeDialog.hide();
	            }
	            mySubTypesTable.submitForm(mySubTypeAction);
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert('sub type Submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new sub type button is clicked.
	    YAHOO.ur.subType.newSubTypeDialog = new YAHOO.widget.Dialog('newSubTypeDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					      { text:'Cancel', handler:handleCancel } ]
		    } );
		
   
        // override the submit function
        YAHOO.ur.subType.newSubTypeDialog.submit = function()
        {
            YAHOO.util.Connect.setForm('newSubTypeForm');
	        if( YAHOO.ur.subType.newSubTypeDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new top media type) based on the action.
                var action = newSubTypeAction;
	            if( document.newSubTypeForm.newSubType.value != 'true')
	            {
	                action = updateSubTypeAction;
	            }

                var cObj = YAHOO.util.Connect.asyncRequest('post',
                action, callback);
            }
        };
        
        // center and show the dialog
        YAHOO.ur.subType.newSubTypeDialog.showDialog = function()
        {
            YAHOO.ur.subType.newSubTypeDialog.center();
            YAHOO.ur.subType.newSubTypeDialog.show();
        };
        
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.subType.newSubTypeDialog.validate = function() 
	    {
	        var name = document.getElementById('newSubTypeForm_name').value;
		    if (name == "" || name == null) 
		    {
		        alert('A sub type name must be entered');
			    return false;
		    } 
		    else 
		    {
			    return true;
		    }
	    };

	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.subType.newSubTypeDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showSubType", "click", 
	        YAHOO.ur.subType.newSubTypeDialog.showDialog, 
	        YAHOO.ur.subType.newSubTypeDialog, true);
     },
     
     /**
      * function to edit sub type information
      */
     editSubType : function(id)
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
                    var divToUpdate = document.getElementById('newSubTypeDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                    document.newSubTypeForm.newSubType.value = "false";
                    YAHOO.ur.subType.newSubTypeDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Edit sub type Failure ' + o.status + 
	            ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            editSubTypeAction + '?subTypeId=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
     },
     
     /**
      * Clear the delete form
      */
     clearDeleteSubTypeForm : function()
     {
          var div = document.getElementById('SubTypeError');
          div.innerHTML = "";
     },
     
     /**
      * create the delete sub type dialog
      */
     createDeleteSubTypeDialog : function()
     {
         // Define various event handlers for Dialog
	     var handleSubmit = function() 
	     {
	         YAHOO.util.Connect.setForm('mySubTypes');
	    
	        //delete the sub type
            var cObj = YAHOO.util.Connect.asyncRequest('post',
            deleteSubTypeAction, callback);
	     };
	
		
	    // handle a cancel of deleting sub type dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.subType.deleteSubTypeDialog.hide();
	        YAHOO.ur.subType.clearDeleteSubTypeForm();
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
	            if( response.subTypeDeleted == "false" )
	            {
	                var deleteTopMediaTypeError = document.getElementById('form_deleteSubTypeError');
                    deleteTopMediaTypeError.innerHTML = '<p id="newDeleteSubTypeError">' 
                    + response.message + '</p>';
                    YAHOO.ur.subType.deleteSubTypeDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the top media types were deleted
	                YAHOO.ur.subType.clearDeleteSubTypeForm();
	                YAHOO.ur.subType.deleteSubTypeDialog.hide();
	            }
	            // reload the table
	            mySubTypesTable.submitForm(mySubTypeAction);
	        }
	
	    };
	
	    // handle form Submission failure
	    var handleFailure = function(o) 
	    {
	        alert('sub type Submission failed ' + o.status);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new sub type button is clicked.
	    YAHOO.ur.subType.deleteSubTypeDialog = new YAHOO.widget.Dialog('deleteSubTypeDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
					      { text:'No', handler:handleCancel } ]
		    } );
		
		// show and center the dialog
        YAHOO.ur.subType.deleteSubTypeDialog.showDialog = function()
        {
            YAHOO.ur.subType.deleteSubTypeDialog.center();
            YAHOO.ur.subType.deleteSubTypeDialog.show();
        };
        
        //override the submit
        YAHOO.ur.subType.deleteSubTypeDialog.submit = function()
        {
            // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               
                //get the response from adding a sub type
	            var response = eval("("+o.responseText+")");
	    
	            //if the sub type was not deleted then show the user the error message.
	            // received from the server
	            if( response.SubTypeDeleted == "false" )
	            {
	                var deleteSubTypeError = document.getElementById('form_deleteSubTypeError');
                    deleteSubTypeError.innerHTML = '<p id="newDeleteSubTypeError">' 
                    + response.message + '</p>';
                    YAHOO.ur.subType.deleteSubTypeDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the sub types were deleted
	                YAHOO.ur.subType.deleteSubTypeDialog.clearDeleteSubTypeForm();
	                YAHOO.ur.subType.deleteSubTypeDialog.hide();
	            }
	            // reload the table
	            mySubTypesTable.submitForm(mySubTypeAction);
	        }
            
        }
        
	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.subType.deleteSubTypeDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showDeleteSubType", "click", 
	        YAHOO.ur.subType.deleteSubTypeDialog.showDialog, 
	        YAHOO.ur.subType.deleteSubTypeDialog, true);
     },
     
     init : function()
     {
         YAHOO.ur.subType.getSubTypes(0,1,1,'asc');
         YAHOO.ur.subType.createNewSubTypeDialog();
         YAHOO.ur.subType.createDeleteSubTypeDialog();
     }
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.subType.init);
