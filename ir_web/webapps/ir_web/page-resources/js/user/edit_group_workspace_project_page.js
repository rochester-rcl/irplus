/**
 * This code is for dealing with editing researcher
 */
YAHOO.namespace("ur.edit.group_workspace_project_page");


/**
 * Edit project page image name space
 */
YAHOO.ur.edit.group_workspace_project_page = 
{
	    /*
	     * Get the picture 
	     * 
	     * currentLocation = location of the current picture
	     * type = INIT for initial load
	     *         NEXT for next picture
	     *         PREV for previous picture
	     */
	    getImage : function(currentLocation, type)
	    {
	        // action for getting the picture
	        var getImageAction =  basePath + 'nextGroupWorkspaceProjectPageImage.action';

	        // Success action on getting the picture
	        var handleSuccess = function(o) 
	        {
				// check for the timeout - forward user to login page if timeout
		        // occurred
		        if( !urUtil.checkTimeOut(o.responseText) )
		        {               
	                var divToUpdate = document.getElementById('group_workspace_project_page_image');
	                divToUpdate.innerHTML = o.responseText; 
	            }
	        };
	    
	        //Failure action on getting an image
	        var handleFailure = function(o) 
		    {
		        alert('Could not get image ' 
		            + o.status + ' status text ' + o.statusText );
		    };
			
			// Get the next picture
	        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
	        		getImageAction +"?currentLocation="+ 
	            currentLocation +'&type='+ type +'&groupWorkspaceId='+ document.getElementById('group_workspace_id').value +'&bustcache='+new Date().getTime(), 
	            {success: handleSuccess, failure: handleFailure}, null);
	    },
	    
		
		// initialize the page
		// this is called once the dom has
		// been created
		init : function() 
		{
	    	YAHOO.ur.edit.group_workspace_project_page.getImage(0, 'INIT');
		}
}

//initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.edit.group_workspace_project_page.init);