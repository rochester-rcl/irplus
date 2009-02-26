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
YAHOO.namespace("ur.researcher.publications");

// action to perform when submitting the personal folder form.
var myPersonalCollectionAction =  basePath + 'user/getPersonalCollectionsAndItems.action';

// Action to perform when submitting selected files form
var myResearcherFoldersAction = basePath + 'user/getResearcherFolders.action';

// Action to add, remove files from the selected list
var removePublicationAction = basePath + 'user/removeResearcherPublication.action';
var addPublicationAction = basePath + 'user/addResearcherPublication.action';

// Action to add files to item
var researcherFolderAction = basePath + 'user/viewResearcher.action?showFoldersTab=true';

var changePersonalPublicationVersionAction = basePath + 'user/changeResearcherPublicationVersion.action';

/**
 * Researcher publications namespace
 */
YAHOO.ur.researcher.publications = {
	
	/**
	 *  Function the publication version
	 *
	 */
	changePersonalPublicationVersion : function(object, researcherPublicationId) {

	    var callback =
	    {
	        success: function(o) 
	        {
	            // check for the timeout - forward user to login page if timout
                // occured
                if( !urUtil.checkTimeOut(o.responseText) )
                {    
                	//do nothing
	            }
	        },
		
		    failure: function(o) 
		    {
		        alert('change personal publication Failure ' + o.status + ' status text ' + o.statusText );
		    }
	    }
		var cObj = YAHOO.util.Connect.asyncRequest('post',
		           changePersonalPublicationVersionAction, callback, 'publicationId=' + researcherPublicationId + '&itemVersionId=' + object.value);
	},	
		
	/**
	 * Action to go back to researcher folders
	 */
	viewResearcherFolders : function()
	{
	    document.myResearcherFolders.action = researcherFolderAction;
	    document.myResearcherFolders.submit();
	},
	 
	/**
	 *  Function that retrieves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	 getPersonalCollection : function()
	 {
		var callback =
		{
		    success: function(o) 
		    {
		        // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       
		            var divToUpdate = document.getElementById('newPersonalCollections');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get personal folder Failure ' + o.status + ' status text ' + o.statusText );
			}
		}
	
	    YAHOO.util.Connect.setForm('myPersonalCollections');
	       
	    var cObj = YAHOO.util.Connect.asyncRequest('POST',
	           myPersonalCollectionAction , callback);
	},
	
	/**
	 *  Function that retireves folder information
	 *  based on the given folder id.
	 *
	 *  The folder id used to get the folder.
	 */
	getPersonalCollectionById : function(folderId)
	{
	
	    document.getElementById('myCollections_parentCollectionId').value = folderId;
	    
	    YAHOO.ur.researcher.publications.getPersonalCollection();
	},	
	
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
		    	// check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {    
		            var divToUpdate = document.getElementById('newResearcherFolders');
		            divToUpdate.innerHTML = o.responseText; 
		        }
		    },
			
			failure: function(o) 
			{
			    alert('Get selected files Failure ' + o.status + ' status text ' + o.statusText );
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
	    document.getElementById('myCollections_parentFolderId').value = folderId;
	    
	    YAHOO.ur.researcher.publications.getResearcherFolders();
	},
	
	// Add publication to researcher
	addPublication : function(versionedPublicationId) 
	{
		var callback =
		{
            success : function(o) {
		    	YAHOO.ur.researcher.publications.getResearcherFolders();
			},
				
			failure : function(o) {
			    alert('Get selected files Failure ' + o.status + ' status text ' + o.statusText );
			} 
		}	
		
		document.getElementById('myCollections_versionedItemId').value = versionedPublicationId;
		YAHOO.util.Connect.setForm('myPersonalCollections');
		
	    var cObj = YAHOO.util.Connect.asyncRequest('post', addPublicationAction, callback);
	   
	},
	
	/* initialize the page this is called once the dom has
	 * been created
	 */
	init : function() {
	    YAHOO.ur.researcher.publications.getPersonalCollection();
	    YAHOO.ur.researcher.publications.getResearcherFolders();
	}
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.researcher.publications.init);