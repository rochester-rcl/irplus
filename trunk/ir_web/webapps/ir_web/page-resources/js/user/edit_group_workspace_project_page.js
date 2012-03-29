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
	    
		/**
		 * Creates a YUI  modal dialog for when a user wants to change
		 * their page status to public
		 *
		 */
		createConfirmPublicDialog : function() 
		{
			// Define various event handlers for Dialog
			var handleSubmit = function() 
			{
				this.submit();
			};
				
			// handle a cancel of the adding department dialog
			var handleCancel = function() 
			{
				 var off = document.getElementById('group_worksapce_project_page_off');
				 off.checked = true;
				 YAHOO.ur.edit.group_workspace_project_page.confirmPublicDialog.hide();
			};
			
			var handleSuccess = function(o) 
			{
				// check for the timeout - forward user to login page if timeout
		        // occurred
		        if( !urUtil.checkTimeOut(o.responseText) )
		        {       		
		        	var divToUpdate = document.getElementById('group_workspace_project_page_status');
		            divToUpdate.innerHTML = o.responseText; 
			    }
		        YAHOO.ur.edit.group_workspace_project_page.confirmPublicDialog.hide();
			};
			
			// handle form sbumission failure
			var handleFailure = function(o) {
			    alert('change to public project page status failed ' + o.status);
			};
		
			// Instantiate the Dialog
			// make it modal - 
			// it should not start out as visible - it should not be shown until 
			// new department button is clicked.
			YAHOO.ur.edit.group_workspace_project_page.confirmPublicDialog = new YAHOO.widget.Dialog('confirmPublicDialog', 
		        { width : "500px",
				  visible : false, 
				  modal : true,
				  buttons : [ { text:'Ok', handler:handleSubmit, isDefault:true },
							  { text:'Cancel', handler:handleCancel } ]
				} );
			
			// show and center the department dialog
		    YAHOO.ur.edit.group_workspace_project_page.confirmPublicDialog.showDialog = function()
		    {
		        YAHOO.ur.edit.group_workspace_project_page.confirmPublicDialog.show();
		        YAHOO.ur.edit.group_workspace_project_page.confirmPublicDialog.center();
		    }
		    
		    // submit to make public
		    YAHOO.ur.edit.group_workspace_project_page.confirmPublicDialog.submit = function()
		    {
		    	var setGroupWorkspaceProjectPagePermissionAction = 
				       basePath + 'user/setGroupWorkspaceProjectPagePermission.action';
					
		    	var cObj = YAHOO.util.Connect.asyncRequest('post',
		    			setGroupWorkspaceProjectPagePermissionAction, callback, 'isPublic=true&groupWorkspaceProjectPageId=' + document.getElementById('group_workspace_project_page_id').value );
		    }	    
		
			// Wire up the success and failure handlers
			var callback = { success: handleSuccess, failure: handleFailure };
					
			// Render the Dialog
			YAHOO.ur.edit.group_workspace_project_page.confirmPublicDialog.render();
		 
		},
		
		/**
		 * Creates a YUI  modal dialog for when a user wants to change
		 * their page status to public
		 *
		 */
		createConfirmPrivateDialog : function() 
		{
			// Define various event handlers for Dialog
			var handleSubmit = function() 
			{
				this.submit();
			};
				
			// handle a cancel of the adding department dialog
			var handleCancel = function() 
			{
				 var on = document.getElementById('group_workspace_project_page_on');
				 on.checked = true;
				 YAHOO.ur.edit.group_workspace_project_page.confirmPrivateDialog.hide();
			};
			
			var handleSuccess = function(o) 
			{
				// check for the timeout - forward user to login page if timeout
		        // occurred
		        if( !urUtil.checkTimeOut(o.responseText) )
		        {       		
		        	var divToUpdate = document.getElementById('group_worksapce_project_page_status');
		            divToUpdate.innerHTML = o.responseText; 
			    }
		        YAHOO.ur.edit.group_workspace_project_page.confirmPrivateDialog.hide();
			};
			
			// handle form sbumission failure
			var handleFailure = function(o) {
			    alert('change to private group workspace page status failed ' + o.status);
			};
		
			// Instantiate the Dialog
			// make it modal - 
			// it should not start out as visible - it should not be shown until 
			// new department button is clicked.
			YAHOO.ur.edit.group_workspace_project_page.confirmPrivateDialog = new YAHOO.widget.Dialog('confirmPrivateDialog', 
		        { width : "500px",
				  visible : false, 
				  modal : true,
				  buttons : [ { text:'Ok', handler:handleSubmit, isDefault:true },
							  { text:'Cancel', handler:handleCancel } ]
				} );
			
			// show and center the dialog
		    YAHOO.ur.edit.group_workspace_project_page.confirmPrivateDialog.showDialog = function()
		    {
		        YAHOO.ur.edit.group_workspace_project_page.confirmPrivateDialog.show();
		        YAHOO.ur.edit.group_workspace_project_page.confirmPrivateDialog.center();
		    }
		    
		    // submit to make public
		    YAHOO.ur.edit.group_workspace_project_page.confirmPrivateDialog.submit = function()
		    {
		    	var setGroupWorkspaceProjectPagePermissionAction = 
				       basePath + 'user/setGroupWorkspaceProjectPagePermission.action';
					
		    	var cObj = YAHOO.util.Connect.asyncRequest('post',
		    			setGroupWorkspaceProjectPagePermissionAction, callback, 'isPublic=false&groupWorkspaceProjectPageId=' + document.getElementById('group_workspace_project_page_id').value);
		    }	    
		
			// Wire up the success and failure handlers
			var callback = { success: handleSuccess, failure: handleFailure };
					
			// Render the Dialog
			YAHOO.ur.edit.group_workspace_project_page.confirmPrivateDialog.render();
		 
		},
	    
		
		// initialize the page
		// this is called once the dom has
		// been created
		init : function() 
		{
	    	YAHOO.ur.edit.group_workspace_project_page.getImage(0, 'INIT');
	    	YAHOO.ur.edit.group_workspace_project_page.createConfirmPublicDialog();
		    YAHOO.ur.edit.group_workspace_project_page.createConfirmPrivateDialog();
		}
}

//initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.edit.group_workspace_project_page.init);