
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
 * This code is for dealing with adding and removing collections 
 * in the workspace.
 */
YAHOO.namespace("ur.public.home");

YAHOO.ur.public.home = 
{
    /*
     * Get the picture 
     * 
     * currentLocation = location of the current picture
     * type = INIT for initial load
     *         NEXT for next picture
     *         PREV for previous picture
     */
    getRepositoryPicture : function(currentLocation, type)
    {
       
        // action for getting the picture
        var getRepositoryPictureAction =  basePath + 'nextRepositoryPicture.action';

        // Success action on getting the picture
        var handleSuccess = function(o) 
        {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {                       
                var divToUpdate = document.getElementById('repository_picture');
                divToUpdate.innerHTML = o.responseText;
            } 
        };
    
        //Faiure action on getting a picture
        var handleFailure = function(o) 
	    {
        	// supress if user tries to move away from the picture before loading
	        if( o.status != 0 )
	        {
	            alert('Could not get picture ' 
	                + o.status + ' status text ' + o.statusText );
	        }
	    };
	
	    // Get the next picture
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getRepositoryPictureAction +"?currentLocation="+ 
            currentLocation +'&type='+ type +'&bustcache='+new Date().getTime(), 
            {success: handleSuccess, failure: handleFailure}, null);
    },
    
    /*
     * Get the news information
     * 
     * currentLocation = location of the current picture
     * type = INIT for initial load
     *         NEXT for next news item
     *         PREV for previous news item
     * numNewsItemsToGet = number of news item to retrieve at once
     */
    getNewsItems : function(currentLocation, type, numNewsItemsToGet)
    {
       // action for getting the picture
       var getNewsAction =  basePath + 'getNewsItems.action';

       //Success action on getting the news item
       var handleSuccess = function(o) 
       {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {                      
                var divToUpdate = document.getElementById('news_items');
                divToUpdate.innerHTML = o.responseText; 
            }
       };
    
       //Faiure action on getting the news item
       var handleFailure = function(o) 
	   {
	       if( o.status != 0 )
	       {
	            alert('Could not get news ' 
	            + o.status + ' status text ' + o.statusText );
	       }
	   };
	
	   // get the next news item
       var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getNewsAction +"?currentLocation="+ 
            currentLocation +'&type='+ type +
            '&numNewsItems=' + numNewsItemsToGet +
            '&bustcache='+new Date().getTime(), 
            {success: handleSuccess, failure: handleFailure}, null);
    }, 
    
    /*
     * Get the top level institutional collections 
     * 
     */
    getInstitutionalCollections : function()
    {
        // action for getting institutional collections
        var getInstitutionalCollections = basePath + 'getInstitutionalCollections.action';
    
        // Success action on getting the institutional collection
        var handleSuccess = function(o) 
        { 
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {                       
                var divToUpdate = document.getElementById('institutional_collections');
                divToUpdate.innerHTML = o.responseText; 
            }
        };
    
        // Faiure action on getting the institutional collection
    
        var handleFailure = function(o) 
	    {
	        if( o.status != 0 )
	        {
	            alert('Could not get institutional collections ' 
	                + o.status + ' status text ' + o.statusText );
	        }
	    };
	
	    // Get the next picture
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getInstitutionalCollections +"?parentId=0"+ 
            '&bustcache='+new Date().getTime(), 
            {success: handleSuccess, failure: handleFailure}, null);
    },
    
    getResearcherPicture : function(currentLocation, type)
    {
        // action for getting the researcher information
        var getResearcherPictureAction =  basePath + 'nextResearcherPicture.action';

        // Success action on getting the researcher information
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
    
        //Failure action on getting a picture
        var handleFailure = function(o) 
	    {
	        // prevents failure on page refresh
	    	if( o.status != 0 )
	        {
	            alert('Could not get researcher picture ' 
	                + o.status + ' status text ' + o.statusText );
	        }
	    };
	
	    //  Get the next picture
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getResearcherPictureAction +"?currentLocation="+ 
            currentLocation +'&type='+ type +'&bustcache='+new Date().getTime(), 
        {success: handleSuccess, failure: handleFailure}, null);
    },
    
    getHomeStatistics : function()
    {
    
        // action for getting the statistics information
        var getStatisticsAction =  basePath + 'loadHomeStatistics.action';

        // Success action on getting the statistics information
        var handleSuccess = function(o) 
        {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               
                var divToUpdate = document.getElementById('statistics_div');
                divToUpdate.innerHTML = o.responseText; 
            }
        };
    
        //Failure action on getting statistics 
        var handleFailure = function(o) 
	    {
	        if( o.status != 0 )
	        {
	            alert('Could not get statistics' 
	                + o.status + ' status text ' + o.statusText );
	        }
	    };
	
	    //  Get the statistics 
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getStatisticsAction, 
        	{success: handleSuccess, failure: handleFailure}, null);
    },
    
    init : function()
    {
        YAHOO.ur.public.home.getResearcherPicture(0, 'INIT');
        YAHOO.ur.public.home.getInstitutionalCollections();
        YAHOO.ur.public.home.getRepositoryPicture(0, 'INIT');
        YAHOO.ur.public.home.getNewsItems(0, 'INIT', 2);
        YAHOO.ur.public.home.getHomeStatistics();
    }
    
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.public.home.init);