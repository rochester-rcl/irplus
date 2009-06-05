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
YAHOO.namespace("ur.series");

// action to perform when submitting the personal series.
var mySeriesAction = basePath + 'admin/getAllSeries.action';

// actions for adding and removing folders
var updateSeriesAction = basePath + 'admin/updateSeries.action';
var newSeriesAction = basePath + 'admin/createSeries.action';
var deleteSeriesAction = basePath + 'admin/deleteSeries.action';
var getSeriesAction = basePath + 'admin/getSeries.action';

// object to hold the specified series data.
var mySeriesTable = new YAHOO.ur.table.Table('mySeries', 'newSeries');


/**
 * series namespace
 */
YAHOO.ur.series = {

	/**
	 *  Function that retireves series information
	 *  based on the given series id.
	 *
	 *  The series id used to get the folder.
	 */
	getSeries : function(rowStart, startPageNumber, currentPageNumber, order)
	{
		var callback = 
		{
		    success: function(o) 
		    {
		        // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {
		            var divToUpdate = document.getElementById('newSeries');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get series Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
		
	    var transaction = YAHOO.util.Connect.asyncRequest('GET', 
        mySeriesAction + '?rowStart=' + rowStart 
    					+ '&startPageNumber=' + startPageNumber 
    					+ '&currentPageNumber=' + currentPageNumber 
    					+ '&sortType=' + order 
    					+ '&bustcache='+new Date().getTime(), 
        callback, null);
	},
	
	 /** 
	  * clear out any form data messages or input
	  * in the new series form
	  */
	clearSeriesForm : function()
	{
	 	// clear out any errors
        var div = document.getElementById('seriesError');
        div.innerHTML = "";
        
	  	document.getElementById('newSeriesForm_name').value = "";
		document.getElementById('newSeriesForm_number').value = "";
		document.getElementById('newSeriesForm_description').value = "";
		document.getElementById('newSeriesForm_id').value = "";
		document.newSeriesForm.newSeries.value = "true";
	},
	
	/**
     * Set all series id's form
     */
    setCheckboxes : function()
    {
        checked = document.mySeries.checkAllSetting.checked;
        var seriesIds = document.getElementsByName('seriesIds');
        urUtil.setCheckboxes(seriesIds, checked);
        
    }, 

	
	/**
	 * Creates a YUI new series modal dialog for when a user wants to create 
	 * a new series
	 *
	 */
	createNewSeriesDialog : function(){
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() 
		{
		   this.submit();
		};
		
			
		// handle a cancel of the adding series dialog
		var handleCancel = function() 
		{
		    YAHOO.ur.series.newSeriesDialog.hide();
		    YAHOO.ur.series.clearSeriesForm();
		};
		
		var handleSuccess = function(o) 
		{
		    // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
		        //get the response from adding a series
		        var response = o.responseText;
		        var seriesForm = document.getElementById('newSeriesDialogFields');
		    
		        // update the form fields with the response.  This updates
		        // the form, if there was an issue, update the form with
		        // the error messages.
		        seriesForm.innerHTML = o.responseText;
		    
		        // determine if the add/edit was a success
		        var success = document.getElementById("newSeriesForm_success").value;
		  
		        //if the series was not added then show the user the error message.
		        // received from the server
		        if( success == "false" )
		        {
	                YAHOO.ur.series.newSeriesDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the series was added
		            YAHOO.ur.series.newSeriesDialog.hide();
				    YAHOO.ur.series.clearSeriesForm();
		        }
		        mySeriesTable.submitForm(mySeriesAction);
		    }
		};
		
		// handle form sbumission failure
		var handleFailure = function(o) {
		    alert('series submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new series button is clicked.
		YAHOO.ur.series.newSeriesDialog = new YAHOO.widget.Dialog('newSeriesDialog', 
	        { width : "500px",
			  visible : false, 
		      modal: true,
			  buttons : [ { text:'Submit', handler:handleSubmit, isDefault:true },
						  { text:'Cancel', handler:handleCancel } ]
			} );
			
	    // show and center the series dialog
        YAHOO.ur.series.newSeriesDialog.showDialog = function()
        {
            YAHOO.ur.series.newSeriesDialog.center();
            YAHOO.ur.series.newSeriesDialog.show();
        }
        	   
	 	// Validate the entries in the form to require that both first and last name are entered
		YAHOO.ur.series.newSeriesDialog.validate = function() {
		    var name = document.getElementById('newSeriesForm_name').value;
			if (name == "" || name == null) {
			    alert('A Series name must be entered');
				return false;
			}
			
			var number = document.getElementById('newSeriesForm_number').value;
			if (number == "" || number == null) {
			    alert('Series number must be entered.');
				return false;
			} else {
				return true;
			}
		};
		
		// Submit the form
		YAHOO.ur.series.newSeriesDialog.submit = function() {
			YAHOO.util.Connect.setForm('newSeriesForm');
		    	    
		    if( YAHOO.ur.series.newSeriesDialog.validate() )
		    {
		        //based on what we need to do (update or create a 
		        // new series) based on the action.
	            var action = newSeriesAction;
		        if( document.newSeriesForm.newSeries.value != 'true')
		        {
		           action = updateSeriesAction;
		        }
	
	            var cObj = YAHOO.util.Connect.asyncRequest('post',
	            action, callback);
	        }
	    };
		
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.series.newSeriesDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showSeries", "click", 
		    YAHOO.ur.series.newSeriesDialog.showDialog, 
		    YAHOO.ur.series.newSeriesDialog, true);
	},
	    
    /**
     * function to edit series information
     */
    editSeries : function(id)
    {	    
	    /*
         * This call back updates the html when a editing a series
         */
        var callback =
        {
            success: function(o) 
            {
            	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {     
                    var divToUpdate = document.getElementById('newSeriesDialogFields');
                    divToUpdate.innerHTML = o.responseText; 
	                document.newSeriesForm.newSeries.value = "false";
	                YAHOO.ur.series.newSeriesDialog.showDialog();
                }
            },
	
	        failure: function(o) 
	        {
	            alert('edit series failed ' + o.status + ' status text ' + o.statusText );
	        }
        };
        
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getSeriesAction + '?id=' + id +  '&bustcache='+new Date().getTime(), 
            callback, null);
    },
	
	/** 
	 * clear out any form data messages or input
	 * in the new series form
	 */
	clearDeleteSeriesForm : function()
	{
	    var seriesError = document.getElementById('newSeriesForm_nameError');
	    var div = document.getElementById('seriesError');
		//clear out any error information
		if( seriesError != null )
		{
		    if( seriesError.innerHTML != null 
		        || seriesError.innerHTML != "")
		    { 
		        div.removeChild(seriesError);
		    }
		}
	},
	
	/**
	 * Creates a YUI new series modal dialog for when a user wants to create 
	 * a new series
	 *
	 */
	createDeleteSeriesDialog : function()
	{
	    
		// Define various event handlers for Dialog
		var handleSubmit = function() {
		    YAHOO.util.Connect.setForm('mySeries');
		    
		    //delete the series
	        var cObj = YAHOO.util.Connect.asyncRequest('post',
	        deleteSeriesAction, callback);
		};
		
			
		// handle a cancel of deleting series dialog
		var handleCancel = function() {
		    YAHOO.ur.series.deleteSeriesDialog.hide();
		};
		
		var handleSuccess = function(o) 
		{
		    // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {		
		        //get the response from adding a series
		        var response = eval("("+o.responseText+")");
		    
		        //if the series was not deleted then show the user the error message.
		        // received from the server
		        if( response.seriesDeleted == "false" )
		        {
		            var deleteSeriesError = document.getElementById('form_deleteSeriesError');
	                deleteSeriesError.innerHTML = '<p id="newDeleteSeriesError">' 
	                + response.message + '</p>';
	                YAHOO.ur.series.deleteSeriesDialog.showDialog();
		        }
		        else
		        {
		            // we can clear the form if the series were deleted
		            YAHOO.ur.series.clearDeleteSeriesForm();
		            YAHOO.ur.series.deleteSeriesDialog.hide();
		        }
		        // reload the table
		        mySeriesTable.submitForm(mySeriesAction);
		    }
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('delete series submission failed ' + o.status + ' status text ' + o.statusText);
		};
	
		// Instantiate the Dialog
		// make it modal - 
		// it should not start out as visible - it should not be shown until 
		// new series button is clicked.
		YAHOO.ur.series.deleteSeriesDialog = new YAHOO.widget.Dialog('deleteSeriesDialog', 
	        { width : "500px",
			  visible : false, 
		      modal: true,
			  buttons : [ { text:'Yes', handler:handleSubmit, isDefault:true },
						  { text:'No', handler:handleCancel } ]
			} );

	    // show and center the series dialog
        YAHOO.ur.series.deleteSeriesDialog.showDialog = function()
        {
            YAHOO.ur.series.deleteSeriesDialog.center();
            YAHOO.ur.series.deleteSeriesDialog.show();
        }			
	   
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess, failure: handleFailure };
				
				
		// Render the Dialog
		YAHOO.ur.series.deleteSeriesDialog.render();
	
	    // listener for showing the dialog when clicked.
		YAHOO.util.Event.addListener("showDeleteSeries", "click", 
		    YAHOO.ur.series.deleteSeriesDialog.showDialog, 
		    YAHOO.ur.series.deleteSeriesDialog, true);
	},
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	    YAHOO.ur.series.getSeries(0, 1, 1, 'asc');
	    YAHOO.ur.series.createNewSeriesDialog();
	    YAHOO.ur.series.createDeleteSeriesDialog();
	}	
}

// initialize the code once the dom is ready
YAHOO.util.Event.addListener(window, "load", YAHOO.ur.series.init);