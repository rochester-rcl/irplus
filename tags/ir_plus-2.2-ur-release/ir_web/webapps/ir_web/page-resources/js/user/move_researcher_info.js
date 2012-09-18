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
 * This code is for dealing with moving files and folders 
 * in the workspace.
 */
YAHOO.namespace("ur.researcher.filesystem.move");


YAHOO.ur.researcher.filesystem.move = 
{
    getMoveFolder : function(destinationId)
    {
        var viewMoveFoldersAction = basePath + 'user/getMoveResearcherFolderDestinations.action';
        document.getElementById("destination_id").value = destinationId;
        document.viewChildContentsForMove.action = viewMoveFoldersAction;

	    // handle a successful return
	    var handleSuccess = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	    
	            var response = o.responseText;
	            document.getElementById('move_researcher_object_frag').innerHTML = response;
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	        alert('Get researcher destinations failure '  + o.status + ' status text ' + o.statusText);
	    };

	    YAHOO.util.Connect.setForm('viewChildContentsForMove');
    
        YAHOO.util.Connect.asyncRequest('POST', viewMoveFoldersAction,
          {success: handleSuccess, failure: handleFailure});
    },
    
    /**
     *  Actually perform the 
     */
    moveFolder : function()
    {
    	YAHOO.ur.util.wait.waitDialog.showDialog();
    	// handle a successful return
	    var handleSuccess = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
			// check for the timeout - forward user to login page if timeout
	        // occurred
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       	    
	            var response = o.responseText;
	            document.getElementById('move_researcher_object_frag').innerHTML = response;

	            var success = document.getElementById('action_success').value;
	  
	            if( success != 'true' )
	            {
	                var errorMessage = document.getElementById('move_error').innerHTML;
	                document.getElementById('default_error_dialog_content').innerHTML= errorMessage;
	                YAHOO.ur.researcher.filesystem.move.moveErrorDialog.center();
	                YAHOO.ur.researcher.filesystem.move.moveErrorDialog.show();
 	            }
	            else
	            {
	                var destinationId = document.getElementById('destination_id').value;
	                var researcherId = document.getElementById('researcher_id').value;
	                var getFoldersAction = basePath + 'user/viewResearcher.action'
	        
	                getFoldersAction = getFoldersAction +
	                     '?researcherId=' + researcherId + '&showFoldersTab=true&parentFolderId=' + destinationId + '&bustcache='+new Date().getTime();
	            
	                window.location = getFoldersAction;
	            }
	        }	
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('Move failure '  + o.status + ' status text ' + o.statusText);
	    };


	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };


	    YAHOO.util.Connect.setForm('viewChildContentsForMove');
        var action = basePath + "user/moveResearcherInformation.action";
        var cObj = YAHOO.util.Connect.asyncRequest('POST',
                action, callback);
    },
    
    createMoveErrorDialog : function()
    {
        // Define various event handlers for Dialog
	    var handleYes = function() {
		    var contentArea = document.getElementById('default_error_dialog_content');
	        contentArea.innerHTML = ""; 
	       this.hide();
	    };

	    // Instantiate the Dialog
	    YAHOO.ur.researcher.filesystem.move.moveErrorDialog = 
	    new YAHOO.widget.Dialog("error_dialog_box", 
									     { width: "600px",
										   visible: false,
										   modal: true,
										   close: false,										   
										   buttons: [ { text:"Ok", handler:handleYes, isDefault:true } ]
										} );
	
	    YAHOO.ur.researcher.filesystem.move.moveErrorDialog.setHeader("Error");
	
	    // Render the Dialog
	    YAHOO.ur.researcher.filesystem.move.moveErrorDialog.render();
    }, 
    
    init : function()
    {
        YAHOO.ur.researcher.filesystem.move.createMoveErrorDialog();
    }
    
}


// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.researcher.filesystem.move.init);