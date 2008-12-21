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
 * This code is for dealing with adding files to the item
 */
YAHOO.namespace("ur.researcher.institutional.item");

// action to perform when submitting the personal folder form.
var myPersonalCollectionAction =  basePath + 'user/getPersonalCollectionsAndItems.action';

// Action to perform when submitting selected files form
var myResearcherFoldersAction = basePath + 'user/getResearcherFileSystemTable.action';

var addInstitutionalItemAction = basePath + 'user/addResearcherInstitutionalItem.action';

/**
 * Researcher publications namespace
 */
YAHOO.ur.researcher.institutional.item = {

	/**
	 * To retrieve the selected files and display in the table
	 *
	 */
	getResearcherFolders : function()
	{ 
		var callback =
		{
		    success: function(o) 
		    {
		        var divToUpdate = document.getElementById('newResearcherFolders');
		        divToUpdate.innerHTML = o.responseText; 
		    },
			
			failure: function(o) 
			{
			    //alert('Get selected files Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	
		YAHOO.util.Connect.setForm('myResearcherFolders');
	   
	    var cObj = YAHOO.util.Connect.asyncRequest('POST',
	           myResearcherFoldersAction , callback);
	   
	},
	
	/**
	 *  Function that retireves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getResearcherFolderById : function(folderId)
	{
	
	    document.getElementById('myResearcherFolders_parentFolderId').value = folderId;
	    
	    YAHOO.ur.researcher.institutional.item.getResearcherFolders();
	},
	
	// Add institutional Item to researcher
	addInstitutionalItem : function(institutionalItemId) 
	{

		document.myResearcherFolders.action = addInstitutionalItemAction;
		document.myResearcherFolders.submit();
	},
	
	/* initialize the page this is called once the dom has
	 * been created
	 */
	init : function() {
	
	    YAHOO.ur.researcher.institutional.item.getResearcherFolders();
	    
	}
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.researcher.institutional.item.init);