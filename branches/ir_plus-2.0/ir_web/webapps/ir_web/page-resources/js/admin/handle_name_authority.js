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
 * This code is for dealing with adding and handle name authorities
 */
YAHOO.namespace("ur.handle.authority");

// action to perform when getting handle name authorities.
var viewHandleNameAuthorities = basePath + 'admin/getHandleNameAuthorities.action';

// actions for adding/updating and removing handle name authorities
var updateHandleNameAuthorityAction = basePath + 'admin/updateHandleNameAuthority.action';
var newHandleNameAuthorityAction = basePath + 'admin/createHandleNameAuthority.action';
var deleteHandleNameAuthorityAction = basePath + 'admin/deleteHandleNameAuthority.action';
var getHandleNameAuthorityAction = basePath + 'admin/getHandleNameAuthority.action';

// object to hold the specified content type data.
var myHandleNameAuthorityAction = new YAHOO.ur.table.Table('myHandleNameAuthorities', 'newHandleNameAuthorities');


/**
 * content type namespace
 */
YAHOO.ur.handle.authority = {
	
	/**
	 *  Function that retrieves hanele name authorities
	 *
	 */
	getHandleNameAuthorities : function()
	{
			var callback =
			{
			    success: function(o) 
			    {
			        // check for the timeout - forward user to login page if timout
	                // occured
	                if( !urUtil.checkTimeOut(o.responseText) )
	                {
			            var divToUpdate = document.getElementById('newHandleNameAuthorities');
			            divToUpdate.innerHTML = o.responseText; 
			        }
			    },
				
				failure: function(o) 
				{
				    alert('Get handle name authorities Failure ' + o.status + ' status text ' + o.statusText );
				}
			}
			
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        viewHandleNameAuthorities + '?bustcache='+new Date().getTime(), 
	        callback, null);			
	},
	
		
	 /** 
	  * clear out any form data messages or input
	  * in the new content type form
	  */
	clearHandleNameAuthorityForm : function()
	{
	    
        var div = document.getElementById('handleNameAuthorityError');
        div.innerHTML = "";
	    
	    document.getElementById('newHandleNameAuthorityForm_name').value = "";
		document.getElementById('newHandleNameAuthorityForm_baseUrl').value = "";
		document.getElementById('newHandleNameAuthorityForm_description').value = "";
		document.getElementById('newHandleNameAuthorityForm_id').value = "";
		document.newHandleNameAuthorityForm.newHandleNameAuthority.value = "true";
		
	},
	
		
    /**
     * Selects or unselects all affilation checkboxes
     */
    setCheckboxes : function()
    {
        checked = document.myHandleNameAuthorities.checkAllSetting.checked;
        var handleNameAuthorityIds = document.getElementsByName('nameAuthorityIds');
        urUtil.setCheckboxes(handleNameAuthorityIds, checked);
    },	
	
	
	/**
	 * Creates a YUI new content type modal dialog for when a user wants to create 
	 * a new handle name authority
	 *
	 */
	createNewHandleNameAuthorityDialog : function()
	{
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
		// handle a cancel of the adding handle name authority dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.handle.authority.newHandleNameAuthorityDialog.hide();
		    YAHOO.ur.handle.authority.clearHandleNameAuthorityForm();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a content type
		        var response = o.responseText;
		        var handleNameAuthorityForm = document.getElementById('newHandleNameAuthorityDialogFields');
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        handleNameAuthorityForm.innerHTML = o.responseText;
		        // determine if the add/edit was a success
		        var success = document.getElementById("newHandleNameAuthorityForm_success").value;
		    
		        //if the content type was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.handle.authority.newHandleNameAuthorityDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the content type was added
		            YAHOO.ur.handle.authority.newHandleNameAuthorityDialog.hide();
		            YAHOO.ur.handle.authority.clearHandleNameAuthorityForm();
		        }
		        YAHOO.ur.handle.authority.getHandleNameAuthorities();
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('New Handle name authority form submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new hanlde name authority button is clicked.
		YAHOO.ur.handle.authority.newHandleNameAuthorityDialog = new YAHOO.widget.Dialog('newHandleNameAuthorityDialog', 
	        { width : "600px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
		//submit form
		YAHOO.ur.handle.authority.newHandleNameAuthorityDialog.submit = function() 
		{
		    YAHOO.util.Connect.setForm('newHandleNameAuthorityForm');
		    if( YAHOO.ur.handle.authority.newHandleNameAuthorityDialog.validate() )
		    {
		        //based on what we need to do (update or create a 
		        // new content type) based on the action.
	            var action = newHandleNameAuthorityAction;
		        if( document.newHandleNameAuthorityForm.newHandleNameAuthority.value != 'true')
		        {
		           action = updateHandleNameAuthorityAction;
		        }
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            action, callback);
	        }
	     }		

	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.handle.authority.newHandleNameAuthorityDialog.validate = function() 
		{
		    var name = document.getElementById('newHandleNameAuthorityForm_name').value;
			if (name == "" || name == null) {
			    alert('A handle naming authority must be entered');
				return false;
			}
		    return true;	
		
		};			
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,   failure: handleFailure };
				
		// Render the Dialog
		YAHOO.ur.handle.authority.newHandleNameAuthorityDialog.render();			
			
	    // show and center the dialog box
	    YAHOO.ur.handle.authority.newHandleNameAuthorityDialog.showDialog = function()
	    {
	    	
	        YAHOO.ur.handle.authority.newHandleNameAuthorityDialog.center();
	        YAHOO.ur.handle.authority.newHandleNameAuthorityDialog.show()
	    }

	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showHandleNameAuthority", "click", 
		    YAHOO.ur.handle.authority.newHandleNameAuthorityDialog.showDialog, 
		    YAHOO.ur.handle.authority.newHandleNameAuthorityDialog, true);
		    
	},
	
    /**
     * function to edit content type information
    */
    editHandleNameAuthority : function(id)
    {	    
	   
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('newHandleNameAuthorityDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
                    document.newHandleNameAuthorityForm.newHandleNameAuthority.value = "false";
                    YAHOO.ur.handle.authority.newHandleNameAuthorityDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('Edit handle name authority failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getHandleNameAuthorityAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
    },
    
	
	/** 
	  * clear out any form data messages or input
	  * in the new hande name authority form
	  */
	clearDeleteHandleNameAuthorityForm : function()
	{
	    var handleNameAuthorityError = document.getElementById('newHandleNameAuthorityForm_nameError');
	    var div = document.getElementById('handleNameAuthorityError');
		//clear out any error information
		if( handleNameAuthorityError != null )
		{
		    if( handleNameAuthorityError.innerHTML != null 
		        || handleNameAuthorityError.innerHTML != "")
		    { 
		        div.removeChild(handleNameAuthorityError);
		    }
		}
	},
	
	
	/**
	 * Creates a YUI new handle name authority modal dialog for when a user wants to delete
	 * a handle name authority
	 *
	 */
	createDeleteHandleNameAuthorityDialog :function()
	{
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
		    YAHOO.util.Connect.setForm('myHandleNameAuthorities');
		    //delete the content type
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deleteHandleNameAuthorityAction, callback);
	       
		};
			
		// handle a cancel of deleting content type dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.handle.authority.deleteHandleNameAuthorityDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	        	alert(o.responseText);
		        //get the response from adding a content type
		        var response = eval("("+o.responseText+")");
		        //if the handle name authority was not deleted then show the user the error message.
		        // received from the server
		        if( response.handleNameAuthorityDeleted == "false" )
		        {
		            var deleteHandleNameAuthorityError = document.getElementById('form_deleteHandleNameAuthorityError');
	                deleteHandleNameAuthorityError.innerHTML = '<p id="newDeleteContentTypeError">' 
	                + response.message + '</p>';
	                YAHOO.ur.handle.authority.deleteHandleNameAuthorityDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the content types were deleted
		            YAHOO.ur.handle.authority.clearDeleteHandleNameAuthorityForm();
		            YAHOO.ur.handle.authority.deleteHandleNameAuthorityDialog.hide();
		        }
		        // reload the table
	        	YAHOO.ur.handle.authority.getHandleNameAuthorities();
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('delete name authority submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new content type button is clicked.
		YAHOO.ur.handle.authority.deleteHandleNameAuthorityDialog = new YAHOO.widget.Dialog('deleteHandleNameAuthorityDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
		
	    // show and center the delete dialog
	    YAHOO.ur.handle.authority.deleteHandleNameAuthorityDialog.showDialog = function()
	    {
	        YAHOO.ur.handle.authority.deleteHandleNameAuthorityDialog.center();
	        YAHOO.ur.handle.authority.deleteHandleNameAuthorityDialog.show();
	    }
	    
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.handle.authority.deleteHandleNameAuthorityDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showDeleteHandleNameAuthority", "click", 
		    YAHOO.ur.handle.authority.deleteHandleNameAuthorityDialog.showDialog, 
		    YAHOO.ur.handle.authority.deleteHandleNameAuthorityDialog, true);
	},
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() {
	    YAHOO.ur.handle.authority.getHandleNameAuthorities();
	    YAHOO.ur.handle.authority.createNewHandleNameAuthorityDialog();
	    YAHOO.ur.handle.authority.createDeleteHandleNameAuthorityDialog();
	}
	
}	

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.handle.authority.init);