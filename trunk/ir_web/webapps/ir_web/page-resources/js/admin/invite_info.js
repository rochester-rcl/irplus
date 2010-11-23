
/*
   Copyright 2008-2010 University of Rochester

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
 * This code is for dealing with removing invite info objects
 */
YAHOO.namespace("ur.invite_info");


var deleteInviteInfoAction = basePath + 'admin/deleteInviteInfo.action';

/* Create the namespace and functions */
YAHOO.ur.invite_info = 
{
		/**
		 * Sets up the form and the delete
		 */
		deleteInvite : function(id)
		{
	 	    document.getElementById('deleteId').value=id;	
		    YAHOO.ur.invite_info.deleteInviteInfoDialog.showDialog();
		},
		
		/**
		 * Creates a YUI new external account type modal dialog for when a user wants to delete 
		 * a statement
		 *
		 */
		createDeleteInviteInfoDialog : function()
		{
			// Define various event handlers for Dialog
			var handleSubmit = function() {
			   window.location = deleteInviteInfoAction + "?inviteInfoId=" +  document.getElementById('deleteId').value;
			};
				
			// handle a cancel of deleting 
			var handleCancel = function() {
				YAHOO.ur.invite_info.deleteInviteInfoDialog.hide();
			};
	
		
			// Instantiate the Dialog
			// make it modal - 
			// it should not start out as visible - it should not be shown until 
			// new copyrigght statement button is clicked.
			YAHOO.ur.invite_info.deleteInviteInfoDialog = new YAHOO.widget.Dialog('deleteInviteInfoDialog', 
		        { width : "500px",
				  visible : false, 
				  modal : true,
				  buttons : [ { text:'Yes', handler:handleSubmit},
							  { text:'No', handler:handleCancel, isDefault:true } ]
				} );
		    
		    //center and show the delete dialog
			YAHOO.ur.invite_info.deleteInviteInfoDialog.showDialog = function()
		    {
				alert('1');
				YAHOO.ur.invite_info.deleteInviteInfoDialog.center();
				YAHOO.ur.invite_info.deleteInviteInfoDialog.show();
		    }
			
			// Render the Dialog
			YAHOO.ur.invite_info.deleteInviteInfoDialog.render();
		
		},
	    
	    init : function()
	    {
			YAHOO.ur.invite_info.createDeleteInviteInfoDialog();
	    }
}

//initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.invite_info.init);
    