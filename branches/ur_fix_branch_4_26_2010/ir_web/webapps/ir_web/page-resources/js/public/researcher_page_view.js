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
 * This code is for dealing with display of researcher page
 * 
 */
YAHOO.namespace("ur.researcher.page");

YAHOO.ur.researcher.page = {

    /*
     * Get the picture 
     * 
     * currentLocation = location of the current picture
     * type = INIT for initial load
     *         NEXT for next picture
     *         PREV for previous picture
     */
    getResearcherPicture : function(currentLocation, type)
    {
        // action for getting the picture
        var getResearcherPictureAction =  basePath + 'nextPicture.action';

        // Success action on getting the picture
        var handleSuccess = function(o) 
        {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               
                var divToUpdate = document.getElementById('researcher_picture');
                divToUpdate.innerHTML = o.responseText; 
            }
        };
    
        //Faiure action on getting a picture
        var handleFailure = function(o) 
	    {
	        alert('Could not get picture ' 
	            + o.status + ' status text ' + o.statusText );
	    };
		
		// Get the next picture
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getResearcherPictureAction +"?currentLocation="+ 
            currentLocation +'&type='+ type +'&researcherId='+ document.getElementById('researcher_id').value +'&bustcache='+new Date().getTime(), 
            {success: handleSuccess, failure: handleFailure}, null);
    },
	
	createFolderTree : function() 
	{
			//create the TreeView instance:
			var tree = new YAHOO.widget.TreeView("treeDiv");
			// this stops the tree from hiding the URL - this is a problem with 
			// YUI 2.7.X - see http://yuilibrary.com/projects/yui2/ticket/2527720
			tree.subscribe('clickEvent',function () {return false;});
			
			//get a reusable reference to the root node:
			var root = tree.getRoot();
			tree.render(); 
			tree.expandAll();
	
	},
	

	
	// initialize the page
	// this is called once the dom has
	// been created
	init : function() 
	{
	    YAHOO.ur.researcher.page.createFolderTree();
		YAHOO.ur.researcher.page.getResearcherPicture(0, 'INIT');

	}
}

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.researcher.page.init);