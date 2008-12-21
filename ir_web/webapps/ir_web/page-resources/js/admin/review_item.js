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
 * This code is for dealing with accepting/rejecting item 
 */
YAHOO.namespace("ur.review.item");


/**
 * Review item namespace
 */
YAHOO.ur.review.item = {

	viewReviewPendingItems : function()
	{
		document.previewForm.action = 'admin/viewReviewPendingItems.action';
		document.previewForm.submit();
	},
	
	acceptItem : function()
	{
		document.previewForm.action = 'admin/acceptReviewableItem.action';
		document.previewForm.submit();
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the rejection form
	  */
	clearRejectionForm : function()
	{
	    document.getElementById('rejectionForm_reason').value = "";
	},
	
	
	/**
	 * Creates a YUI new content type modal dialog for rejecting an item
	 *
	 */
	createRejectionDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() {
			this.submit();
		};
		
			
		// handle a cancel of the item rejection dialog
		var handleCancel = function() {
		    YAHOO.ur.review.item.rejectionDialog.hide();
		    YAHOO.ur.review.item.clearRejectionForm();
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// reject item button is clicked.
		YAHOO.ur.review.item.rejectionDialog = new YAHOO.widget.Dialog('rejectionDialog', 
	        { width : "500px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
		//submit form
		YAHOO.ur.review.item.rejectionDialog.submit = function() 
		{
		    document.rejectionForm.action='admin/rejectReviewableItem.action';
		    document.rejectionForm.submit();	    
	     }		

	    
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.review.item.rejectionDialog.validate = function() 
		{
		    var reason = document.getElementById('rejectionForm_reason').value;
			if (reason == "" || reason == null) {
			    alert('Reason for rejection must be entered');
				return false;
			} else {
				return true;
			}
		};			
	
			
		// Render the Dialog
		YAHOO.ur.review.item.rejectionDialog.render();			
			
	    // show and center the dialog box
	    YAHOO.ur.review.item.rejectionDialog.showDialog = function()
	    {
	        YAHOO.ur.review.item.rejectionDialog.center();
	        YAHOO.ur.review.item.rejectionDialog.show()
	    }

	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showRejection", "click", 
		    YAHOO.ur.review.item.rejectionDialog.showDialog, 
		    YAHOO.ur.review.item.rejectionDialog, true);
		    
	},

	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() {
	    YAHOO.ur.review.item.createRejectionDialog();

	}
	
}	

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.review.item.init);