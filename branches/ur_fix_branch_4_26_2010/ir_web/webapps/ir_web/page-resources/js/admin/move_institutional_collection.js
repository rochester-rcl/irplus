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
YAHOO.namespace("ur.institution");


YAHOO.ur.institution = 
{
    getMoveCollection : function(destinationId)
    {
        var viewMoveCollectionsAction = basePath + 'admin/getMoveInstitutionalCollectionDestinations.action';
        document.getElementById("destination_id").value = destinationId;
        document.viewChildContentsForMove.action = viewMoveCollectionsAction;

	    // handle a successful return
	    var handleSuccess = function(o) 
	    {
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var response = o.responseText;
	            document.getElementById('move_collection_frag').innerHTML = response;
	        }
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	        alert('Get destinations failure ' + o.status + ' status text ' + o.statusText);
	    };

	    YAHOO.util.Connect.setForm('viewChildContentsForMove');
    
        YAHOO.util.Connect.asyncRequest('POST', viewMoveCollectionsAction,
          {success: handleSuccess, failure: handleFailure});
    },
    
    moveCollection : function()
    {
    	YAHOO.ur.util.wait.waitDialog.showDialog();
    	// handle a successful return
	    var handleSuccess = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            var response = o.responseText;
	            document.getElementById('move_collection_frag').innerHTML = response;

	            var success = document.getElementById('action_success').value;
	  
	            if( success != 'true' )
	            {
	                var errorMessage = document.getElementById('move_error').innerHTML;
	                document.getElementById('default_error_dialog_content').innerHTML= errorMessage;
	                YAHOO.ur.institution.moveErrorDialog.center();
	                YAHOO.ur.institution.moveErrorDialog.show();
 	            }
	            else
	            {
	                var destinationId = document.getElementById('destination_id').value;
	                var getInstitutionalCollectionsAction = basePath + 
	                'admin/viewInstitutionalCollections.action'
	        
	                getInstitutionalCollectionsAction = getInstitutionalCollectionsAction +
	                 '?parentCollectionId=' + destinationId + '&bustcache='+new Date().getTime();
	            
	                window.location = getInstitutionalCollectionsAction;
	            }
	        }
	
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	    	YAHOO.ur.util.wait.waitDialog.hide();
	        alert('Move failure ' + o.status + ' status text ' + o.statusText);
	    };


	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };


	    YAHOO.util.Connect.setForm('viewChildContentsForMove');
        var action = basePath + "admin/moveInstitutionalCollectionInformation.action";
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
	    YAHOO.ur.institution.moveErrorDialog = 
	    new YAHOO.widget.Dialog("error_dialog_box", 
									     { width: "600px",
										   visible: false,
										   modal: true,
										   close: false,										   
										   buttons: [ { text:"Ok", handler:handleYes, isDefault:true } ]
										} );
	
	    YAHOO.ur.institution.moveErrorDialog.setHeader("Error");
	
	    // Render the Dialog
	    YAHOO.ur.institution.moveErrorDialog.render();
    }, 
    
    init : function()
    {
        YAHOO.ur.institution.createMoveErrorDialog();
    }
    
}


// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.institution.init);