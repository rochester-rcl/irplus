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
 * This code is for dealing with adding collections to the item
 */
YAHOO.namespace("ur.item.collection");

// Action to get collections 
var getCollectionsAction =  basePath + 'user/getInstitutionalCollections.action';

// Action to add, remove contributor to the item
var removeCollectionAction = basePath + 'user/removeInstitutionalCollection.action';
var addCollectionAction =  basePath + 'user/addInstitutionalCollection.action';

// Action to goto other screens
var submitPublicationAction = basePath + 'user/submitPublicationAndFinishLater.action';
var publicationWorkspaceAction = basePath + 'user/workspace.action?showCollectionTab=true';

YAHOO.ur.item.collection = {

	/**
	 *  Function that retrives the institutional collections within
	 *  the given parent collection
	 */
	getCollections : function(parentInstitutionalCollectionId)
	{
		var callback =
		{
		    success: function(o) 
		    {
		        var divToUpdate = document.getElementById('all_collections');
		        divToUpdate.innerHTML = o.responseText; 
		    },
			
			failure: function(o) 
			{
			    alert('Get collection Failure ' + o.status + ' status text ' + o.statusText );
			}
		};
	
		document.getElementById('all_collections_form_institutional_parentId').value = parentInstitutionalCollectionId; 
		
		YAHOO.util.Connect.setForm('allCollectionsForm');
		
	    var transaction = YAHOO.util.Connect.asyncRequest('post', 
	        getCollectionsAction, 
	        callback);
	},
	
	/**
	 * Function to add collection to the item
	 *
	 */
	addCollectionToPublication : function(collectionId) 
	{ 
		var handleSuccess = function(o) {
	
	 		var divToUpdate = document.getElementById('selected_collections');
	        divToUpdate.innerHTML = o.responseText; 	
		   
		   document.getElementById('all_collections_form_collectionIds').value 
		   		= document.getElementById('selected_collections_form_collectionIds').value
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('Adding the collection failed ' + o.status);
		};
	
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };
		
		YAHOO.util.Connect.setForm('allCollectionsForm');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           addCollectionAction + '?institutionalCollectionId=' + collectionId, callback);		
	    
	},
	
	/**
	 * Function to remove collection
	 *
	 */
	 removeCollectionFromPublication : function(collectionId) 
	 {
		var handleSuccess = function(o) {
	
	 		var divToUpdate = document.getElementById('selected_collections');
	        divToUpdate.innerHTML = o.responseText; 	
		   
		   document.getElementById('all_collections_form_collectionIds').value 
		   		= document.getElementById('selected_collections_form_collectionIds').value
		};
		
		// handle form submission failure
		var handleFailure = function(o) {
		    alert('Collection removal failed ' + o.status);
		};
	
		// Wire up the success and failure handlers
		var callback = { success: handleSuccess,  failure: handleFailure };

		YAHOO.util.Connect.setForm('selectedCollectionsForm');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post',
	           removeCollectionAction + '?institutionalCollectionId=' + collectionId, callback);
											         
	 	
	},
	
	/**
	 *  Function that retrives the institutional collections within
	 *  the given parent collection
	 */
	submitPublication : function()
	{
		document.allCollectionsForm.action = submitPublicationAction;
		document.allCollectionsForm.submit();	
		
	},
	
	/**
	 * Function to goto the Publication table
	 *
	 */
	cancel : function()
	{
		document.allCollectionsForm.action = publicationWorkspaceAction;
		document.allCollectionsForm.submit();	
	},
	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
		YAHOO.ur.item.collection.getCollections(0);
	}
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.item.collection.init);
