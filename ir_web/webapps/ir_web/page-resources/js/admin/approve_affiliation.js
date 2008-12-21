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
 * This code is for dealing with approving the affiliations
 */
YAHOO.namespace("ur.affiliation.approval");

// action to get the users who has pending affiliations
var myPendingApprovalAction = basePath + 'admin/getPendingApproval.action';

// actions for saving the affiliations
var saveAffiliationApprovalAction = basePath + 'admin/saveAffiliationApproval.action';

// object to hold the affiliation approval data.
var myPendingApprovalTable = new YAHOO.ur.table.Table('myPendingApprovals', 'newPendingApprovals');

/**
 * approve affiliation namespace
 */
YAHOO.ur.affiliation.approval = {

	/**
	 *  Function that retireves pending affiliation approval 
	 *
	 */
	getPendingApprovals : function(rowStart, startPageNumber, currentPageNumber, order)
	{

		var callback =
		{
		    success: function(o) 
		    {
		        var divToUpdate = document.getElementById('newPendingApprovals');
		        divToUpdate.innerHTML = o.responseText; 
		    },
			
			failure: function(o) 
			{
			    alert('Get pending affiliation Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        myPendingApprovalAction + '?rowStart=' + rowStart 
	        					+ '&startPageNumber=' + startPageNumber 
	        					+ '&currentPageNumber=' + currentPageNumber 
	        					+ '&sortType=' + order 
	        					+ '&bustcache='+new Date().getTime(), 
	        callback, null);
	},
	
    /**
     * Set all pending approval id's form
     */
    setCheckboxes : function()
    {
        checked = document.myPendingApprovals.checkAllSetting.checked;
        var userIds = document.getElementsByName('userIds');
        urUtil.setCheckboxes(userIds, checked);
        
    }, 	
	
	/**
	 * Creates a YUI modal dialog when a user wants to create 
	 * approve the affiliation
	 *
	 */
	createApproveAffiliationDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() {
			this.submit();
		};
			
		// handle a cancel of affiliation approval dialog
		var handleCancel = function() {
		    YAHOO.ur.affiliation.approval.approveAffiliationDialog.hide();
		};
		
		var handleSuccess = function(o) {
	
	        YAHOO.ur.affiliation.approval.approveAffiliationDialog.hide();
		    
		    // reload the table
		    myPendingApprovalTable.submitForm(myPendingApprovalAction);
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('Affiliation approval submission failed ' + o.status);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - 
		YAHOO.ur.affiliation.approval.approveAffiliationDialog = new YAHOO.widget.Dialog('approveAffiliationDialog', 
	        { width : "350px",
			  visible : false, 
			  modal : true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );

	   	// show and center the dialog
        YAHOO.ur.affiliation.approval.approveAffiliationDialog.showDialog = function()
        {
            YAHOO.ur.affiliation.approval.approveAffiliationDialog.center();
            YAHOO.ur.affiliation.approval.approveAffiliationDialog.show();
        }
        		
		// Submit the form	
		YAHOO.ur.affiliation.approval.approveAffiliationDialog.submit = function()
		{
		   YAHOO.util.Connect.setForm('myPendingApprovals');
		    
			var selectedAffiliationIds = "";
		    var first = true;
		 	
		 	if( document.myPendingApprovals.userIds.length != null )
		    {
		             var check_boxes = document.myPendingApprovals.userIds;
		             var affiliation_id = document.myPendingApprovals.affiliationId;
			         
			         for (var i=0; i<check_boxes.length; i++)
			         {
			            if( check_boxes[i].checked == true)
			            {
			                if(first)
			                {
			                    selectedAffiliationIds = affiliation_id[i].value;
			                    first = false;
			                }
			                else
			                {
			                
			                    selectedAffiliationIds = selectedAffiliationIds + "," + affiliation_id[i].value;
			                }
					    }
			         }
		    }
		    else
	         {
	             if(document.myPendingApprovals.userIds.checked == true)
	             {
	                 selectedAffiliationIds =document.myPendingApprovals.affiliationId.value;
	             }
	         }
		    
		     
		    //Approve affiliations
	        var cObj = YAHOO.util.Connect.asyncRequest('GET',
	        	saveAffiliationApprovalAction+ '?affiliationIds=' + selectedAffiliationIds,
	        	callback);	
	    };		
	   
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.affiliation.approval.approveAffiliationDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showApproveAffiliation1", "click", 
		    YAHOO.ur.affiliation.approval.approveAffiliationDialog.showDialog,
		    YAHOO.ur.affiliation.approval.approveAffiliationDialog, true);
	},
	
	/** initialize the page
	 * this is called once the dom has
	 * been created
	 */
	init : function() 
	{
	    YAHOO.ur.affiliation.approval.getPendingApprovals(0,1,1,'asc');
	    YAHOO.ur.affiliation.approval.createApproveAffiliationDialog();
	}	
}	

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.affiliation.approval.init);