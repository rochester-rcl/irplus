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
YAHOO.namespace("ur.department");

// action to perform when submitting the personal departments.
var myDepartmentAction = basePath + 'admin/getDepartments.action';

// actions for adding and removing folders
var updateDepartmentAction = basePath + 'admin/updateDepartment.action';
var newDepartmentAction = basePath + 'admin/createDepartment.action';
var deleteDepartmentAction = basePath + 'admin/deleteDepartment.action';
var getDepartmentAction = basePath + 'admin/getDepartment.action';

// object to hold the specified department data.
var myDepartmentTable = new YAHOO.ur.table.Table('myDepartments', 'newDepartments');

/**
 * Department namespace
 */
YAHOO.ur.department = {
    
    getDepartments : function(rowStart, startPageNumber, currentPageNumber, order)
    {
        //call back updates the html when a new department is
        //retrieved.
        var callback =
        {
            success: function(o) 
            {
                // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
                    var divToUpdate = document.getElementById('newDepartments');
                    divToUpdate.innerHTML = o.responseText; 
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get department Failure ' + o.status + ' status text ' + o.statusText );
	        }
        }
    
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            myDepartmentAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(),  
            callback, null);
    },
    
    /**
     * Clear the department form
     */
    clearDepartmentForm : function() 
    {
        // clear out any errors
        var div = document.getElementById('departmentError');
        div.innerHTML = "";
	    
	    document.getElementById('newDepartmentForm_name').value = "";
	    document.getElementById('newDepartmentForm_description').value = "";
	    document.getElementById('newDepartmentForm_id').value = "";
	    document.newDepartmentForm.newDepartment.value = "true";
    },
    
    /**
     * Set all department id's form
     */
    setCheckboxes : function()
    {
        checked = document.myDepartments.checkAllSetting.checked;
        var departmentIds = document.getElementsByName('departmentIds');
        urUtil.setCheckboxes(departmentIds, checked);
    }, 
    
    /*
     * Create the department dialog
     */
    createNewDepartmentDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        this.submit();
	    };
	
	    // handle a cancel of the adding department dialog
	    var handleCancel = function() 
	    {
	    	YAHOO.ur.department.newDepartmentDialog.hide();
	        YAHOO.ur.department.clearDepartmentForm();
	    };
	
	    var handleSuccess = function(o) 
	    {
	    
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            //get the response from adding a department
	            var response = o.responseText;
	            var departmentForm = document.getElementById('newDepartmentDialogFields');
	    
	            // update the form fields with the response.  This updates
	            // the form, if there was an issue, update the form with
	            // the error messages.
	            departmentForm.innerHTML = o.responseText;
	    
	            // determine if the add/edit was a success
	            var success = document.getElementById("newDepartmentForm_success").value;
	    
	  
	            //if the department was not added then show the user the error message.
	            // received from the server
	            if( success == "false" )
	            {
                     YAHOO.ur.department.newDepartmentDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the department was added
	                YAHOO.ur.department.newDepartmentDialog.hide();
	                YAHOO.ur.department.clearDepartmentForm();
	            }
	            myDepartmentTable.submitForm(myDepartmentAction);
	        }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert('department submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new department button is clicked.
	    YAHOO.ur.department.newDepartmentDialog = new YAHOO.widget.Dialog('newDepartmentDialog', 
            { width : "500px",
		      visible : false, 
		      fixedcenter : true,
		      constraintoviewport : true,
		      buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					      { text:'Cancel', handler:handleCancel } ]
		    } );
	
	    // show and center the department dialog
        YAHOO.ur.department.newDepartmentDialog.showDialog = function()
        {
            YAHOO.ur.department.newDepartmentDialog.center();
            YAHOO.ur.department.newDepartmentDialog.show();
        }
        
        // override the submit function
        YAHOO.ur.department.newDepartmentDialog.submit = function()
        {
           	YAHOO.util.Connect.setForm('newDepartmentForm');
	        if( YAHOO.ur.department.newDepartmentDialog.validate() )
	        {
	            
	            //based on what we need to do (update or create a 
	            // new department) based on the action.
                var action = newDepartmentAction;
	            if( document.newDepartmentForm.newDepartment.value != 'true')
	            {
	               action = updateDepartmentAction;
	            }
	            
                var cObj = YAHOO.util.Connect.asyncRequest('post', action, callback);
            }

        }
    
 	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.department.newDepartmentDialog.validate = function() 
	    {
	        var name = document.getElementById('newDepartmentForm_name').value;
		    if (name == "" || name == null) 
		    {
		        alert('A department name must be entered');
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
	    YAHOO.ur.department.newDepartmentDialog.render();
	    
        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showDepartment", "click", 
	        YAHOO.ur.department.newDepartmentDialog.showDialog, 
	        YAHOO.ur.department.newDepartmentDialog, true);
    },
    
    /**
     * function to edit department information
     */
    editDepartment : function(id)
    {	    
	    /*
         * This call back updates the html when a editing a department
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('newDepartmentDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                   	document.newDepartmentForm.newDepartment.value = "false";
	                YAHOO.ur.department.newDepartmentDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Get department failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getDepartmentAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
     },
    
    /**
     * clear out any error messages in the department form
     */
    clearDeleteDepartmentForm : function()
    {
        var div = document.getElementById('departmentError');
        div.innerHTML = "";
    },
    
    createDeleteDepartmentDialog : function()
    {
    	// Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        YAHOO.util.Connect.setForm('myDepartments');
	    
	        //delete the department
            var cObj = YAHOO.util.Connect.asyncRequest('post',
            deleteDepartmentAction, callback);
	    };
	
		
	    // handle a cancel of deleting department dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.department.deleteDepartmentDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            //get the response from adding a department
	            var response = eval("("+o.responseText+")");
	    
	            //if the department was not deleted then show the user the error message.
	            // received from the server
	            if( response.departmentDeleted == "false" )
	            {
	                var deleteDepartmentError = document.getElementById('form_deleteDepartmentError');
                    deleteDepartmentError.innerHTML = '<p id="newDeleteDepartmentError">' 
                    + response.message + '</p>';
                    YAHOO.ur.department.deleteDepartmentDialog.showDialog();
	            }
	            else
	            {
	                // we can clear the form if the departments were deleted
	                YAHOO.ur.department.deleteDepartmentDialog.hide();
	                YAHOO.ur.department.clearDeleteDepartmentForm();
	            }
	            // reload the table
	            myDepartmentTable.submitForm(myDepartmentAction);
	         }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	        alert('department submission failed ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new department button is clicked.
	    YAHOO.ur.department.deleteDepartmentDialog = new YAHOO.widget.Dialog('deleteDepartmentDialog', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
					      { text:'No', handler:handleCancel } ]
		    } );
		
   
	    // Wire up the success and failure handlers
	    callback = { success: handleSuccess,  failure: handleFailure };
	
 	    // show and center the dialog box		
	    YAHOO.ur.department.deleteDepartmentDialog.showDialog = function()
	    {
	        YAHOO.ur.department.deleteDepartmentDialog.center();
	        YAHOO.ur.department.deleteDepartmentDialog.show();
	    };
			
	    // Render the Dialog
	    YAHOO.ur.department.deleteDepartmentDialog.render();

        // listener for showing the dialog when clicked.
	    YAHOO.util.Event.addListener("showDeleteDepartment", "click", 
	        YAHOO.ur.department.deleteDepartmentDialog.showDialog, 
	        YAHOO.ur.department.deleteDepartmentDialog, true);
    },
    
    /**
     * init the department dialog
     */
    init : function()
    {
        YAHOO.ur.department.getDepartments(0,1,1,'asc');
        YAHOO.ur.department.createNewDepartmentDialog();
        YAHOO.ur.department.createDeleteDepartmentDialog();
    }
};


// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.department.init);