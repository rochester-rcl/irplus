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
YAHOO.namespace("ur.workspace.search");

YAHOO.ur.workspace.search = 
{
    executeUserSearch : function()
    {
        YAHOO.util.Connect.setForm('userSearchForm');
    
        // This call back updates the html when a new folder is
        // retrieved.
        var callback =
        {
            success: function(o) 
            {
                var divToUpdate = document.getElementById('search_results');
                divToUpdate.innerHTML = o.responseText; 
            },
	
	        failure: function(o) 
	        {
	            alert('User Search Failure ' + o.status + ' status text ' + o.statusText );
	        }
        };
    
        // action to perform when submitting the personal folder form.
        var userSearchAction =  basePath + 'user/userWorkspaceSearch.action';
        var cObj = YAHOO.util.Connect.asyncRequest('POST',
                userSearchAction, callback);
        
        YAHOO.ur.shared.file.inbox.getSharedFilesCount();
    },

    /**
     * If a user selects a folder this sets the folder and activates the
     * tab
     */
    showFolder : function(folderId)
    {
        YAHOO.ur.folder.getFolderById(folderId);
        YAHOO.ur.user.workspace.setActiveIndex("FOLDER");
    },
    
    /**
     * If a user selects a collection this sets the collection and activates the
     * tab
     */
    showCollection : function(collectionId)
    {
        YAHOO.ur.personal.collection.getCollectionById(collectionId);
        YAHOO.ur.user.workspace.setActiveIndex("COLLECTION");
    }
};

