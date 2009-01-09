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
 * This code is for dealing with viewing collection pictures
 */
YAHOO.namespace("ur.public.collection.view");

YAHOO.ur.public.collection.view = 
{
    getCollectionPicture : function(currentLocation, type)
    {
        // action for getting the picture
        var getCollectionPictureAction =  basePath + 'nextCollectionPicture.action';

        var collectionId = document.getElementById('current_collection_id').value;
   
        //Success action on getting the picture
        var handleSuccess = function(o) 
        {
            // check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {                       
                var divToUpdate = document.getElementById('collection_picture');
                divToUpdate.innerHTML = o.responseText;
            } 
        };
    
        // Faiure action on getting a picture
        var handleFailure = function(o) 
	    {
	        alert('Could not get picture ' 
	            + o.status + ' status text ' + o.statusText );
	    };
	
	    //Get the next picture
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getCollectionPictureAction +"?currentLocation="+ 
            currentLocation +'&type='+ type +
            '&collectionId=' + collectionId +
            '&bustcache='+new Date().getTime(), 
            {success: handleSuccess, failure: handleFailure}, null);
    },
    
    subscribe : function() {
    	 // action for getting the picture
        var subscribeToCollectionAction =  basePath + 'user/subscribeToCollection.action';

        var collectionId = document.getElementById('current_collection_id').value;
   
        //Success action on getting the picture
        var handleSuccess = function(o) 
        {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               
        	    //get the response from subscribing to collection
		        var response = o.responseText;
                var divToUpdate = document.getElementById('collection_subscription');
                divToUpdate.innerHTML = response;
            }
        };
    
        // Faiure action on getting a picture
        var handleFailure = function(o) 
	    {
	        alert('Could not get picture ' 
	            + o.status + ' status text ' + o.statusText );
	    };
	
	    //Get the next picture
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            subscribeToCollectionAction + "?collectionId=" + collectionId + "&includeSubCollections=" + document.getElementById('include_sub_collections').checked
            + '&bustcache='+new Date().getTime(), 
            {success: handleSuccess, failure: handleFailure}, null);
    },

    unsubscribe : function() {
    	 // action for getting the picture
        var unSubscribeFromCollectionAction =  basePath + 'user/unSubscribeFromCollection.action';

        var collectionId = document.getElementById('current_collection_id').value;
   
        //Success action on getting the picture
        var handleSuccess = function(o) 
        {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               
        	    //get the response from subscribing to collection
		        var response = o.responseText;
                var divToUpdate = document.getElementById('collection_subscription');
                divToUpdate.innerHTML = response;
            }
        };
    
        // Faiure action on getting a picture
        var handleFailure = function(o) 
	    {
	        alert('Could not get picture ' 
	            + o.status + ' status text ' + o.statusText );
	    };
	
	    //Get the next picture
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            unSubscribeFromCollectionAction + "?collectionId=" + collectionId
            + '&bustcache='+new Date().getTime(), 
            {success: handleSuccess, failure: handleFailure}, null);
    },
    
    getUserSubscriptionForThisCollection : function() 
    {
    	 // action for getting the picture
        var getUserSubscriptionAction =  basePath + 'user/getUserSubscription.action';

        var collectionId = document.getElementById('current_collection_id').value;
   
        //Success action on getting the picture
        var handleSuccess = function(o) 
        {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {                       
        	    //get the response from subscribing to collection
		        var response = o.responseText;
                var divToUpdate = document.getElementById('collection_subscription');
                divToUpdate.innerHTML = response;
            }
        };
    
        // Faiure action on getting a picture
        var handleFailure = function(o) 
	    {
	        alert('Could not get picture ' 
	            + o.status + ' status text ' + o.statusText );
	    };
	
	    //Get the next picture
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getUserSubscriptionAction + "?collectionId=" + collectionId 
            + '&bustcache='+new Date().getTime(), 
            {success: handleSuccess, failure: handleFailure}, null);    	
    },
    
    getStatisticsForThisCollection : function() 
    {

    	 // action for getting the statistics
        var getStatisticsAction =  basePath + 'getInstitutionalCollectionStatistics.action';

        var collectionId = document.getElementById('current_collection_id').value;
   
        // Success action on getting the statistics
        var handleSuccess = function(o) 
        {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {               
        	    //get the response statistics
		        var response = o.responseText;
                var divToUpdate = document.getElementById('statistics_div');
                divToUpdate.innerHTML = response;
            }
        };
    
        // Faiure action on getting a statistics
        var handleFailure = function(o) 
	    {
	        alert('Could not get picture ' 
	            + o.status + ' status text ' + o.statusText );
	    };
	
	    //Get the statistics
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            getStatisticsAction + "?collectionId=" + collectionId 
            + '&bustcache=' + new Date().getTime(), 
            {success: handleSuccess, failure: handleFailure}, null);    	
    },
    
    init : function()
    {
        YAHOO.ur.public.collection.view.getCollectionPicture( 0, 'INIT');
        
        if (document.getElementById("user_id").value != '') 
        {
        	YAHOO.ur.public.collection.view.getUserSubscriptionForThisCollection();
        }
        YAHOO.ur.public.collection.view.getStatisticsForThisCollection();
    }
};


// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.public.collection.view.init);