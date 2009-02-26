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
 * This code is for dealing with moving collections and items
 * in the workspace.
 */
YAHOO.namespace("ur.collection.move");


YAHOO.ur.collection.move = 
{
    getMoveCollection : function(destinationId)
    {
        var viewMoveCollectionsAction = basePath + 'user/getMovePersonalCollectionDestinations.action';
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
	        alert('Get destinations failure ' + o.status);
	    };

	    YAHOO.util.Connect.setForm('viewChildContentsForMove');
    
        YAHOO.util.Connect.asyncRequest('POST', viewMoveCollectionsAction,
          {success: handleSuccess, failure: handleFailure});
    },
    
    moveCollection : function()
    {
    	// handle a successful return
	    var handleSuccess = function(o) 
	    {
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
	                YAHOO.ur.collection.move.moveErrorDialog.center();
	                YAHOO.ur.collection.move.moveErrorDialog.show();
 	            }
	            else
	            {
	                var destinationId = document.getElementById('destination_id').value;
	                var getCollectionsAction = basePath + 'user/workspace.action'
	        
	                getCollectionsAction = getCollectionsAction +
	                 '?parentCollectionId=' + destinationId +
	                 '&showCollectionTab=true' + '&bustcache='+new Date().getTime();
	            
	                window.location = getCollectionsAction;
	            }
	        }
	
	    };
	
	    // handle form submission failure
	    var handleFailure = function(o) 
	    {
	        alert('Move failure ' + o.status);
	    };


	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };


	    YAHOO.util.Connect.setForm('viewChildContentsForMove');
        var action = basePath + "user/movePersonalCollectionInformation.action";
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
	    YAHOO.ur.collection.move.moveErrorDialog = 
	    new YAHOO.widget.Dialog("error_dialog_box", 
									     { width: "600px",
										   visible: false,
										   modal: true,
										   close: false,										   
										   buttons: [ { text:"Ok", handler:handleYes, isDefault:true } ]
										} );
	
	    YAHOO.ur.collection.move.moveErrorDialog.setHeader("Error");
	
	    // Render the Dialog
	    YAHOO.ur.collection.move.moveErrorDialog.render();
    }, 
    
    init : function()
    {
        YAHOO.ur.collection.move.createMoveErrorDialog();
    }
    
}


// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.collection.move.init);