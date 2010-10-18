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
 * This code is for dealing with display of  items and collections 
 */
YAHOO.namespace("ur.item.version");

// action to perform when submitting the personal collection form.
var myCollectionAction = basePath + 'user/getPersonalItems.action';

var addVersionAction = basePath + 'user/addInstitutionalItemVersion.action';

// object to hold the specified collection data.
var myPersonalCollectionsTable = new YAHOO.ur.table.Table('myCollections', 'newPersonalCollections');



YAHOO.ur.item.version = 
{

   /**
    *  Function that retireves colection information
    *  based on the given collection id.
    *
    *  The collection id used to get the collection.
    */
    getCollectionById : function(collectionId)
    {
        document.getElementById('myCollections_parentCollectionId').value = collectionId;
        myPersonalCollectionsTable.submitForm(myCollectionAction);
    },

	/**
	 * Adds new version to institutional item
	 *
	 */
    addVersion : function(personalItemId)
    {
    	document.getElementById('addVersionForm_itemVersionId').value = document.getElementById('version_' +personalItemId).value;
    	document.addVersionForm.action = addVersionAction;
    	document.addVersionForm.submit();
    },

    /** 
     * initialize the page
     * this is called once the dom has
     * been created
     */
    init: function() 
    {
        YAHOO.ur.item.version.getCollectionById(0);
    }  
    
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.item.version.init);        