
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
 * This code is for dealing with shared files 
 */
YAHOO.namespace("ur.shared.file.inbox");

// object to hold the specified shared files data.
var inboxFilesTable = new YAHOO.ur.table.Table('mySharedInboxFiles', 'shared_folder_inbox');

YAHOO.ur.shared.file.inbox = 
{
    /**
     * check/uncheck all files and folders
     */
    setCheckboxes : function()
    {
         checked = document.mySharedInboxFiles.checkAllSetting.checked;
         var inboxFileIds = document.getElementsByName('sharedInboxFileIds');
         urUtil.setCheckboxes(inboxFileIds, checked);
    },
    
    /**
     * Get the shared files
     */
    getSharedFiles : function()
    {
        // action to perform when submitting the personal folder form.
        var viewSharedInboxFiles =  basePath + 'user/viewSharedInboxFiles.action';
    
        // success when getting the file properties
        var handleSuccess = function(o) 
        {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		         
	            var response = o.responseText;
	            var contentArea = document.getElementById('shared_folder_inbox');
	            contentArea.innerHTML = o.responseText; 
	        
	            // Set the number of inbox files in Tab 
 			    var inboxFilesCount = document.getElementById('inbox_files_count');
 			    var countAndSpan = '<span id="inbox_files_count">' + document.getElementById('shared_inbox_files_count').value + '</span>' ;
	            inboxFilesCount.innerHTML = countAndSpan ; 
	        }	        
        };
   
        // success when getting the file properties
        var handleFailure = function(o) 
        {
	        alert('Get Shared inbox files failed:  ' + o.status + ' status text ' + o.statusText);
        };
   
        // Wire up the success and failure handlers
        var callback = { success: handleSuccess, failure: handleFailure };
    
        // execute the transaction
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
                viewSharedInboxFiles +'?bustcache='+new Date().getTime(), 
                callback, null);
    },
    
    /**
     * Get the shared files count
     */
    getSharedFilesCount : function()
    {
   
        // action to perform when submitting the personal folder form.
        var getNumberOfSharedInboxFiles =  basePath + 'user/getNumberOfSharedInboxFiles.action';
    
        // success when getting the file properties
        var handleSuccess = function(o) 
        {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		         
	            var response = o.responseText;
	            var contentArea = document.getElementById('shared_folder_inbox');
	            contentArea.innerHTML = o.responseText; 
	        
	            // Set the number of inbox files in Tab 
 			    var inboxFilesCount = document.getElementById('inbox_files_count');
 			    var countAndSpan = '<span id="inbox_files_count">' + document.getElementById('shared_inbox_files_count').value + '</span>';
	            inboxFilesCount.innerHTML = countAndSpan;
	        } 	        
        };
   
        // success when getting the file properties
        var handleFailure = function(o) 
        {
	        alert('Get Shared inbox files count failed:  ' + o.status + ' status text ' + o.statusText);
        };
   
        // Wire up the success and failure handlers
        var callback = { success: handleSuccess, failure: handleFailure };
    
        // execute the transaction
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
                getNumberOfSharedInboxFiles +'?bustcache='+new Date().getTime(), 
                callback, null);
    }, 

    /**
     * Allow files to be moved to workspace
     */
    moveSharedFiles : function()
    {
    	// make sure a file has been selected
    	if ( document.mySharedInboxFiles.sharedInboxFileIds == null )
    	{
 		    alert('There are currently no files in your inbox to move.');
		}
	    else if (!urUtil.checkForNoSelections(document.mySharedInboxFiles.sharedInboxFileIds) )
		{
		    alert('Please select at least one checkbox next to the files you wish to move.');
		}
		else
		{
            var viewMoveSharedFilesAction = basePath + 'user/viewMoveSharedFilesLocations.action';
            document.mySharedInboxFiles.action = viewMoveSharedFilesAction;
            document.mySharedInboxFiles.submit();
        }
    },
    
    /**
     * Delete the shared files
     */
    deleteSharedFiles : function()
    {
    
        // success when getting the file properties
        var handleSuccess = function(o) 
        {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		         
	        	YAHOO.ur.shared.file.inbox.getSharedFiles();
	        }	        
        };
   
        // success when getting the file properties
        var handleFailure = function(o) 
        {
	        alert('Get Shared inbox files failed:  ' + o.status + ' status text ' + o.statusText);
        };
   
        // Wire up the success and failure handlers
        var callback = { success: handleSuccess, failure: handleFailure };
    
        var deleteFolderAction = basePath + 'user/deleteInboxFile.action';
	    YAHOO.util.Connect.setForm('mySharedInboxFiles');
	    
        YAHOO.util.Connect.asyncRequest('POST', deleteFolderAction, callback);
        
      
    },
    
    /**
	 * Dialog to confirm delete of files
	 */
	createInboxFileDeleteConfirmDialog : function() 
	{
        // Define various event handlers for Dialog
	    var handleYes = function() 
	    {
		    this.hide();
		    YAHOO.ur.shared.file.inbox.deleteSharedFiles();
	    };
	    
	    var handleNo = function() 
	    {
	        //uncheck all the ones that have been checked
	        checked = document.mySharedInboxFiles.checkAllSetting.checked = false;
	        YAHOO.ur.shared.file.inbox.setCheckboxes();
		    this.hide();
	    };

	    // Instantiate the Dialog
	    YAHOO.ur.shared.file.inbox.deleteInboxFileConfirmDialog = 
	        new YAHOO.widget.Dialog("deleteInboxFileConfirmDialog", 
									     { width: "400px",
										   visible: false,
										   modal: true,
										   buttons: [ { text:"Yes", handler:handleYes, isDefault:true },
													  { text:"No",  handler:handleNo } ]
										} );
	
	    YAHOO.ur.shared.file.inbox.deleteInboxFileConfirmDialog.setHeader("Delete?");
	
	    //show the dialog and center
	    YAHOO.ur.shared.file.inbox.deleteInboxFileConfirmDialog.showDialog = function()
	    {
	        if (!urUtil.checkForNoSelections(document.mySharedInboxFiles.sharedInboxFileIds))
		    {
			     alert('Please select at least one checkbox next to the files or folders you wish to delete.');
	        } 
	        else
	        {
	            YAHOO.ur.shared.file.inbox.deleteInboxFileConfirmDialog.center();
	            YAHOO.ur.shared.file.inbox.deleteInboxFileConfirmDialog.show();
	        }
	    };
	
	    // Render the Dialog
	    YAHOO.ur.shared.file.inbox.deleteInboxFileConfirmDialog.render();

    },
    
    init : function()
    {
    	YAHOO.ur.shared.file.inbox.createInboxFileDeleteConfirmDialog();	
    }
       

}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.shared.file.inbox.init);