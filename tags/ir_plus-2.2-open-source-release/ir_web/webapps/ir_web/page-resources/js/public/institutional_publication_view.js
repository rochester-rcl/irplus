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
YAHOO.namespace("ur.public.institutional.publication");


/**
 * Folder namespace
 */
YAHOO.ur.public.institutional.publication = {
	
	/** 
	  * clear out any form data messages or input
	  * in the witdraw form
	  */
	clearWithdrawPublicationForm : function()
	{
	   // clear out any errors
        var div = document.getElementById('withdrawPublicationError');
        div.innerHTML = "";
        
		document.withdrawPublicationForm.withdrawReason.value = "";
	},
	
	/**
	 * Creates a YUI new modal dialog for when a user wants to 
	 * withdraw publication 
	 *
	 */
	createWithdrawPublicationDialog : function()
	{

		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();

		};

		// handle a cancel of the adding folder dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.public.institutional.publication.withdrawPublicationDialog.hide();
		    YAHOO.ur.public.institutional.publication.clearWithdrawPublicationForm();
		};

		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new folder button is clicked.


	    YAHOO.ur.public.institutional.publication.withdrawPublicationDialog = new YAHOO.widget.Dialog('withdrawDialog', 
        { width : "850px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					  { text:'Cancel', handler:handleCancel } ]
        } );

		// Submit form
		YAHOO.ur.public.institutional.publication.withdrawPublicationDialog.submit = function() 
		{
			if (YAHOO.ur.public.institutional.publication.withdrawPublicationDialog.validate()) 
			{
			    document.withdrawPublicationForm.submit();
			    YAHOO.ur.public.institutional.publication.withdrawPublicationDialog.hide();
			    YAHOO.ur.public.institutional.publication.clearWithdrawPublicationForm();
			}		    
	    },
	    
	    // Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.public.institutional.publication.withdrawPublicationDialog.validate = function() 
		{
		    var data = this.getData();

			if (data.withdrawReason == '' ) {
			    alert("Enter reason for withdraw.");
				return false;
			} else {
				return true;
			}
		};
		
		YAHOO.ur.public.institutional.publication.withdrawPublicationDialog.showDialog = function()
		{
		    YAHOO.ur.public.institutional.publication.withdrawPublicationDialog.center();
		    YAHOO.ur.public.institutional.publication.withdrawPublicationDialog.show();
		    
		}
	
		// Render the Dialog
		YAHOO.ur.public.institutional.publication.withdrawPublicationDialog.render();
	
		YAHOO.util.Event.addListener("withdraw_publication", "click", YAHOO.ur.public.institutional.publication.withdrawPublicationDialog.showDialog,
		YAHOO.ur.public.institutional.publication.withdrawPublicationDialog, true);
 
	},
	
	
	
     /** 
	  * clear out any form data messages or input
	  * in the reinstate form
	  */
	clearWithdrawPublicationForm : function()
	{
	   // clear out any errors
        var div = document.getElementById('reinstatePublicationError');
        div.innerHTML = "";
        
		document.reinstatePublicationForm.reinstateReason.value = "";
	},
	
	/**
	 * Creates a YUI new modal dialog for when a user wants to 
	 * re-instate publication 
	 *
	 */
	createReinstatePublicationDialog : function()
	{

    	// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
			this.submit();

		};

		// handle a cancel of the adding folder dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.public.institutional.publication.reinstatePublicationDialog.hide();
		    YAHOO.ur.public.institutional.publication.clearReinstatePublicationForm();
		};

		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new folder button is clicked.


	    YAHOO.ur.public.institutional.publication.reinstatePublicationDialog = new YAHOO.widget.Dialog('reinstateDialog', 
        { width : "850px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
					  { text:'Cancel', handler:handleCancel } ]
        } );

		// Submit form
		YAHOO.ur.public.institutional.publication.reinstatePublicationDialog.submit = function() 
		{
			if (YAHOO.ur.public.institutional.publication.reinstatePublicationDialog.validate()) 
			{
			    document.reinstatePublicationForm.submit();
			    YAHOO.ur.public.institutional.publication.reinstatePublicationDialog.hide();
			    YAHOO.ur.public.institutional.publication.clearReinstatePublicationForm();
			}		    
	    },
	    
	    // Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.public.institutional.publication.reinstatePublicationDialog.validate = function() 
		{
		    var data = this.getData();

			if (data.withdrawReason == '' ) {
			    alert("Enter reason for reinstate.");
				return false;
			} else {
				return true;
			}
		};
		
		YAHOO.ur.public.institutional.publication.reinstatePublicationDialog.showDialog = function()
		{
		    YAHOO.ur.public.institutional.publication.reinstatePublicationDialog.center();
		    YAHOO.ur.public.institutional.publication.reinstatePublicationDialog.show();
		    
		}
	
		// Render the Dialog
		YAHOO.ur.public.institutional.publication.reinstatePublicationDialog.render();
	
		YAHOO.util.Event.addListener("reinstate_publication", "click", YAHOO.ur.public.institutional.publication.reinstatePublicationDialog.showDialog,
		YAHOO.ur.public.institutional.publication.reinstatePublicationDialog, true);
 
	},
	
	/**
	 * Dialog to confirm delete of folders and files
	 */
	createItemDeleteConfirmDialog : function() 
	{
        // Define various event handlers for Dialog
	    var handleYes = function() 
	    {
	    	document.deleteForm.submit();

		    this.hide();
	    };
	    
	    var handleNo = function() 
	    {
		    this.hide();
	    };

	    // Instantiate the Dialog
	    YAHOO.ur.public.institutional.publication.deleteItemConfirmDialog = 
	        new YAHOO.widget.Dialog("deleteItemConfirmDialog", 
									     { width: "400px",
										   visible: false,
										   modal: true,
										   buttons: [ { text:"Yes", handler:handleYes, isDefault:true },
													  { text:"No",  handler:handleNo } ]
										} );
	
	    YAHOO.ur.public.institutional.publication.deleteItemConfirmDialog.setHeader("Delete?");
	
	    //show the dialog and center
	    YAHOO.ur.public.institutional.publication.deleteItemConfirmDialog.showDialog = function()
	    {
            YAHOO.ur.public.institutional.publication.deleteItemConfirmDialog.center();
            YAHOO.ur.public.institutional.publication.deleteItemConfirmDialog.show();
	    };
	
	    // Render the Dialog
	    YAHOO.ur.public.institutional.publication.deleteItemConfirmDialog.render();
	    
	    YAHOO.util.Event.addListener("delete_item", "click", YAHOO.ur.public.institutional.publication.deleteItemConfirmDialog.showDialog,
	    	YAHOO.ur.public.institutional.publication.deleteItemConfirmDialog, true);

    },	
	
	
	    
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	    YAHOO.ur.public.institutional.publication.createWithdrawPublicationDialog();
	    YAHOO.ur.public.institutional.publication.createReinstatePublicationDialog();
	    YAHOO.ur.public.institutional.publication.createItemDeleteConfirmDialog();
	}
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.public.institutional.publication.init);