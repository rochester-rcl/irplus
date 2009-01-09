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
YAHOO.namespace("ur.ignore.ipaddress");

// action to perform when submitting the ignore ipaddress
var myIgnoreIpAddressAction = basePath + 'admin/getIgnoreIpAddresses.action';

// actions for adding and removing ignore ipaddress
var updateIgnoreIpAddressAction = basePath + 'admin/updateIgnoreIpAddress.action';
var newIgnoreIpAddressAction = basePath + 'admin/createIgnoreIpAddress.action';
var deleteIgnoreIpAddressAction = basePath + 'admin/deleteIgnoreIpAddress.action';

// object to hold the specified ignore ipaddress data.
var myIgnoreIpAddressTable = new YAHOO.ur.table.Table('myIgnoreIpAddresses', 'newIgnoreIpAddresses');


/**
 * ignore ip address namespace
 */
YAHOO.ur.ignore.ipaddress = {
	
	/**
	 *  Function that retireves content information
	 *  based on the given content id.
	 *
	 *  The content id used to get the folder.
	 */
	getIgnoreIpAddresses : function(rowStart, startPageNumber, currentPageNumber, order)
	{
			var callback =
			{
			    success: function(o) 
			    {
			        // check for the timeout - forward user to login page if timout
	                // occured
	                if( !urUtil.checkTimeOut(o.responseText) )
	                {
			            var divToUpdate = document.getElementById('newIgnoreIpAddresses');
			            divToUpdate.innerHTML = o.responseText;
			        } 
			    },
				
				failure: function(o) 
				{
				    alert('Get ignore ipaddress Failure ' + o.status + ' status text ' + o.statusText );
				}
			}
			
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        myIgnoreIpAddressAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(), 
	        callback, null);			
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the new ignore ipaddress form
	  */
	clearIgnoreIpAddressForm : function()
	{
	    
	    // clear out any errors
        var div = document.getElementById('ignoreIpAddressError');
        div.innerHTML = "";
	    
	    document.getElementById('newIgnoreIpAddressForm_name').value = "";
		document.getElementById('newIgnoreIpAddressForm_description').value = "";
		document.getElementById('newIgnoreIpAddressForm_fromAddress1').value = "";
		document.getElementById('newIgnoreIpAddressForm_fromAddress2').value = "";
		document.getElementById('newIgnoreIpAddressForm_fromAddress3').value = "";
		document.getElementById('newIgnoreIpAddressForm_fromAddress4').value = "";
		document.getElementById('newIgnoreIpAddressForm_toAddress4').value = "";
		document.getElementById('newIgnoreIpAddressForm_id').value = "";
		document.newIgnoreIpAddressForm.newIgnoreIpAddress.value = "true";
	},
	
    /**
     * Selects or unselects all ip address checkboxes
     */
    setCheckboxes : function()
    {
        checked = document.myIgnoreIpAddresses.checkAllSetting.checked;
        var ignoreIpAddressIds = document.getElementsByName('ignoreIpAddressIds');
        urUtil.setCheckboxes(ignoreIpAddressIds, checked);
    },	
	
	
	/**
	 * Creates a YUI new ip address modal dialog for when a user wants to create 
	 * a new ignore ip address range
	 *
	 */
	createNewIgnoreIpAddressDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();
		};
		
			
		// handle a cancel of the adding ip address dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog.hide();
		    YAHOO.ur.ignore.ipaddress.clearIgnoreIpAddressForm();
		};
		
		var handleSuccess = function(o) 
		{
		    // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a ip address
		        var response = o.responseText;
		        var ignoreIpAddressForm = document.getElementById('newIgnoreIpAddressDialogFields');
		    
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        ignoreIpAddressForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("newIgnoreIpAddressForm_success").value;
		  
		        //if the ip address was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the ip address was added
		            YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog.hide();
		            YAHOO.ur.ignore.ipaddress.clearIgnoreIpAddressForm();
		        }
		        myIgnoreIpAddressTable.submitForm(myIgnoreIpAddressAction);
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('ip address submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new ip address button is clicked.
		YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog = new YAHOO.widget.Dialog('newIgnoreIpAddressDialog', 
	        { width : "600px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
		//submit form
		YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog.submit = function() 
		{
		    YAHOO.util.Connect.setForm('newIgnoreIpAddressForm');
		    	    
		    if( YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog.validate() )
		    {
		        //based on what we need to do (update or create a 
		        // new ip address) based on the action.
	            var action = newIgnoreIpAddressAction;
		        if( document.newIgnoreIpAddressForm.newIgnoreIpAddress.value != 'true')
		        {
		           action = updateIgnoreIpAddressAction;
		        }
	
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            action, callback);
	        }
	     }		

	    
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog.validate = function() 
		{
		    var name = document.getElementById('newIgnoreIpAddressForm_name').value;
			if (name == "" || name == null) {
			    alert('Name must be entered.');
				return false;
			}
			
			if (document.getElementById('newIgnoreIpAddressForm_fromAddress1').value == ""
				|| document.getElementById('newIgnoreIpAddressForm_fromAddress2').value == ""
				|| document.getElementById('newIgnoreIpAddressForm_fromAddress3').value == ""
				|| document.getElementById('newIgnoreIpAddressForm_fromAddress4').value == ""
				|| document.getElementById('newIgnoreIpAddressForm_toAddress4').value == "") {
				alert('Please enter complete IP address.');
				return false;
			}
			
			return true;
		};			
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,   failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog.render();			
			
	    // show and center the dialog box
	    YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog.showDialog = function()
	    {
	        YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog.center();
	        YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog.show()
	    }

	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showIgnoreIpAddress", "click", 
		    YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog.showDialog, 
		    YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog, true);
		    
	},
	
    // function to edit ip address information
    editIgnoreIpAddress : function(id, name, description, fromIp1, fromIp2, fromIp3, fromIp4, toIp4)
    {
    	document.getElementById('newIgnoreIpAddressForm_name').value = name;
	    document.getElementById('newIgnoreIpAddressForm_description').value = description;
	    document.getElementById('newIgnoreIpAddressForm_id').value = id;
	    document.getElementById('newIgnoreIpAddressForm_fromAddress1').value = fromIp1;
	    document.getElementById('newIgnoreIpAddressForm_fromAddress2').value = fromIp2;
	    document.getElementById('newIgnoreIpAddressForm_fromAddress3').value = fromIp3;
	    document.getElementById('newIgnoreIpAddressForm_fromAddress4').value = fromIp4;
	    document.getElementById('newIgnoreIpAddressForm_toAddress4').value = toIp4;
	    document.newIgnoreIpAddressForm.newIgnoreIpAddress.value = "false";
	    YAHOO.ur.ignore.ipaddress.newIgnoreIpAddressDialog.showDialog();
    },
    	
	 /** 
	  * clear out any form data messages or input
	  * in the new ip address form
	  */
	clearDeleteIgnoreIpAddressForm : function()
	{
	    var ignoreIpAddressError = document.getElementById('newIgnoreIpAddressForm_nameError');
	    var div = document.getElementById('ignoreIpAddressError');
		//clear out any error information
		if( ignoreIpAddressError != null )
		{
		    if( ignoreIpAddressError.innerHTML != null 
		        || ignoreIpAddressError.innerHTML != "")
		    { 
		        div.removeChild(ignoreIpAddressError);
		    }
		}
	},
	
	/**
	 * Creates a YUI new ip address modal dialog for when a user wants to create 
	 * a new ip address
	 *
	 */
	createDeleteIgnoreIpAddressDialog :function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() {
		    YAHOO.util.Connect.setForm('myIgnoreIpAddresses');
		    
		    //delete the ip address
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deleteIgnoreIpAddressAction, callback);
		};
		
			
		// handle a cancel of deleting ip address dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.ignore.ipaddress.deleteIgnoreIpAddressDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
		    // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a ip address
		        var response = eval("("+o.responseText+")");
		    
		        //if the ip address was not deleted then show the user the error message.
		        // received from the server
		        if( response.ignoreIpAddressDeleted == "false" )
		        {
		            var deleteIgnoreIpAddressError = document.getElementById('form_deleteIgnoreIpAddressError');
	                deleteIgnoreIpAddressError.innerHTML = '<p id="newDeleteIgnoreIpAddressError">' 
	                + response.message + '</p>';
	                YAHOO.ur.ignore.ipaddress.deleteIgnoreIpAddressDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the ip addresses were deleted
		            YAHOO.ur.ignore.ipaddress.clearDeleteIgnoreIpAddressForm();
		            YAHOO.ur.ignore.ipaddress.deleteIgnoreIpAddressDialog.hide();
		        }
		        // reload the table
		        myIgnoreIpAddressTable.submitForm(myIgnoreIpAddressAction);
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('ip address submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new ip address button is clicked.
		YAHOO.ur.ignore.ipaddress.deleteIgnoreIpAddressDialog = new YAHOO.widget.Dialog('deleteIgnoreIpAddressDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );
			
	    // show and center the delete dialog
	    YAHOO.ur.ignore.ipaddress.deleteIgnoreIpAddressDialog.showDialog = function()
	    {
	        YAHOO.ur.ignore.ipaddress.deleteIgnoreIpAddressDialog.center();
	        YAHOO.ur.ignore.ipaddress.deleteIgnoreIpAddressDialog.show();
	    }
	    
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.ignore.ipaddress.deleteIgnoreIpAddressDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showDeleteIgnoreIpAddress", "click", 
		    YAHOO.ur.ignore.ipaddress.deleteIgnoreIpAddressDialog.showDialog, 
		    YAHOO.ur.ignore.ipaddress.deleteIgnoreIpAddressDialog, true);
	},
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() {
	    YAHOO.ur.ignore.ipaddress.getIgnoreIpAddresses(0,1,1,'asc');
	    YAHOO.ur.ignore.ipaddress.createNewIgnoreIpAddressDialog();
	    YAHOO.ur.ignore.ipaddress.createDeleteIgnoreIpAddressDialog();
	}
	
}	

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.ignore.ipaddress.init);