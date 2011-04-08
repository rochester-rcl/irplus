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
 * This code is for dealing with adding, editing and removing  
 * personal links
 */
YAHOO.namespace("ur.researcher.link");


/**
 * links namespace
 */
YAHOO.ur.researcher.link = {
    
    /**
     * Dialog to create/edit link information
     */
    createNewLinkDialog : function()
    {
 	    // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
            this.submit();
	    };
		
	    // handle a cancel of the adding folder dialog
	    var handleCancel = function() 
	    {
	        YAHOO.ur.researcher.link.clearLinkForm();
	        YAHOO.ur.researcher.link.newLinkDialog.hide();
	    };
	
	    // handle a successful return
	    var handleSuccess = function(o) 
	    {
			// check for the timeout - forward user to login page if timeout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		 	    
	    	    // received from the server
	            var response = o.responseText;
	        
	            var div = document.getElementById('researcher_personal_link_fields');
	            div.innerHTML = o.responseText;
	        
	            var success = document.getElementById('researcher_personal_link_form_success').value;
	    
	            //if the link was not saved then show the user the error message.
	            if( success == "false" )
	            {
	                YAHOO.ur.researcher.link.newLinkDialog.showLinkDialog();
	            }
	            else
	            {
	                researcherId = document.getElementById('hidden_researcher_id').value;
	                YAHOO.ur.researcher.link.newLinkDialog.hide();
	                YAHOO.ur.researcher.link.clearLinkForm();
	                YAHOO.ur.researcher.link.viewLinks(researcherId);
	            }
	         }
	    };
	
	    // handle form sbumission failure
	    var handleFailure = function(o) 
	    {
	        alert("Link submission failed due to a network issue: " + o.status  +  " status text " + o.statusText);
	    };

 	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new folder button is clicked.
	    YAHOO.ur.researcher.link.newLinkDialog = new YAHOO.widget.Dialog('newResearcherPersonalLinkDialog', 
        { width : "600px",
		  visible : false, 
		  modal : true,
		  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
					  { text:"Cancel", handler:handleCancel } ]
		} );
	
	    // override the submit
	   YAHOO.ur.researcher.link.newLinkDialog.submit = function()
	   {
	        YAHOO.util.Connect.setForm('newResearcherPersonalLinkForm');
	        if( YAHOO.ur.researcher.link.newLinkDialog.validate() )
	        {
	            //based on what we need to do (update or create a 
	            // new link) based on the action.
	            	
                var action =  basePath + 'user/addResearcherPersonalLink.action';
 	            if( document.newResearcherPersonalLinkForm.linkId.value != '')
	            {
	               action = basePath + 'user/updateResearcherPersonalLink.action';
	            }
                var cObj = YAHOO.util.Connect.asyncRequest('POST',
                action, callback);
            }
	    };
	    
	    YAHOO.ur.researcher.link.newLinkDialog.showLinkDialog = function()
	    {
	        YAHOO.ur.researcher.link.newLinkDialog.center();
	        YAHOO.ur.researcher.link.newLinkDialog.show();
	    };
   
 	    // Validate the entries in the form to require that both first and last name are entered
	   YAHOO.ur.researcher.link.newLinkDialog.validate = function() {
	        var data = this.getData();
		    if (urUtil.trim(data.linkName) == "" ) 
		    {
		        alert("A link name must be entered");
			    return false;
		    } 
		    if (urUtil.trim(data.linkUrl) == "" ) 
		    {
		        alert("A link URL must be entered");
			    return false;
		    } 
		    else 
		    {
			    return true;
		    }
	    };

	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
			
	    // Render the Dialog
	    YAHOO.ur.researcher.link.newLinkDialog.render();
    },
    
    
    
    /**
     * Clear the link form of data
     */
    clearLinkForm: function()
    {
      
    	document.getElementById('researcher_personal_link_name').value = "";
	    document.getElementById('researcher_personal_link_url').value = "http://";
	    document.getElementById('researcher_personal_link_description').value = "";
	    document.getElementById('researcher_personal_link_id').value = "";
	
        var div = document.getElementById('researcher_personal_link_error_div');
        div.innerHTML= "";
    },
    
    
    /**
     * Retireve link information for editing
     */
    editLink : function(collectionId, linkId)
    {
        var success = function(o) 
	    {
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		 
	            var divToUpdate = document.getElementById('researcher_personal_link_fields');
                divToUpdate.innerHTML = o.responseText; 
                YAHOO.ur.researcher.link.newLinkDialog.showLinkDialog();
            }
 	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure to edit link ' + o.status  +  ' status text ' + o.statusText);
	    };
	    
	    	
	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
	    
	    // action to perform for searching names
        var action =  basePath + 'user/editResearcherPersonalLinkView.action?researcherId=' + researcherId + '&linkId=' + linkId;
            
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            action + '&bustcache='+new Date().getTime(), 
            callback);
    },
    
    /**
     * asks user to confirm delete a link
     */
    deleteLinkDialog : function()
    {
	    // Define various event handlers for Dialog
	    var submit = function() 
	    {
	        // action to perform for searching names
            var action =  basePath + 'user/deleteResearcherPersonalLink.action';
            
            var formObject = document.getElementById('remove_researcher_personal_link_form');
	        YAHOO.util.Connect.setForm(formObject);
	
            var transaction = YAHOO.util.Connect.asyncRequest('POST', 
            action, 
            callback);
         
	    };
	
	    // handle a cancel of the adding collection item dialog
	    var cancel = function() 
	    {
	        YAHOO.ur.researcher.link.deleteLinkDialog.hide();
	    };
	
	    var success = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		 	    
	            var divToUpdate = document.getElementById('researcher_personal_links');
                divToUpdate.innerHTML = o.responseText; 
                YAHOO.ur.researcher.link.deleteLinkDialog.hide();
            }
	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('delete link failure ' + o.status + ' status text ' + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new collection item button is clicked.
	    YAHOO.ur.researcher.link.deleteLinkDialog = 
	       new YAHOO.widget.Dialog('remove_researcher_personal_link_confirm', 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      buttons : [ { text:'Yes', handler:submit, isDefault:true },
					      { text:'No', handler:cancel } ]
		    } );
	
	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
		
		YAHOO.ur.researcher.link.deleteLinkDialog.showDialog = function()
	    {
	        YAHOO.ur.researcher.link.deleteLinkDialog.center();
	        YAHOO.ur.researcher.link.deleteLinkDialog.show();
	    };
			
	    // Render the Dialog
	    YAHOO.ur.researcher.link.deleteLinkDialog.render();

    },
    
    /**
     * remove the link with the specified name fromt the collection
     */
    removeLink : function(linkId)
    {
       //set the name in the form
       document.getElementById('remove_link_id').value = linkId;
       YAHOO.ur.researcher.link.deleteLinkDialog.showDialog();
    },
    
    /**
     * Get and display the links
     */
    viewLinks : function(researcherId)
    {
    	var success = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {      
	            var divToUpdate = document.getElementById('researcher_personal_links');
                divToUpdate.innerHTML = o.responseText; 
            }
 	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure to view links '  + o.status + ' status text ' + o.statusText);
	    };
	    
	    	
	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
	    
	    // action to perform for searching names
        var action =  basePath + 'user/viewResearcherPersonalLinks.action?researcherId=' + researcherId;
            
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            action + '&bustcache='+new Date().getTime(), 
            callback);
    },
    
    /**
     * Move a link up one position
     */
    moveLinkUp : function(linkId, researcherId)
    {
        var success = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		 	    
	            var divToUpdate = document.getElementById('researcher_personal_links');
                divToUpdate.innerHTML = o.responseText;
            } 
 	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure to  move link up '  + o.status + ' status text ' + o.statusText);
	    };
	    
	    	
	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
	    
	    // action to perform move
	    var action =  basePath + 'user/moveResearcherPersonalLinkUp.action?researcherId=' + researcherId + '&linkId=' + linkId;
            
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            action +'&bustcache='+new Date().getTime(), 
            callback);
    },
    
    /**
     * Move a link up down one position
     */
    moveLinkDown : function(linkId, researcherId)
    {
        var success = function(o) 
	    {
			// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {       		 	    
	            var divToUpdate = document.getElementById('researcher_personal_links');
                divToUpdate.innerHTML = o.responseText;
            } 
 	    };
	
	    // handle form sbumission failure
	    var failure = function(o) {
	        alert('failure to move links down ' + o.status + ' status text ' + o.statusText);
	    };
	    
	    	
	    // Wire up the success and failure handlers
	    var callback = 
	    { 
	        success: success,
			failure: failure 
	    };
	    
	    // action to perform for searching names
        var action =  basePath + 'user/moveResearcherPersonalLinkDown.action?researcherId=' + researcherId + '&linkId=' + linkId;
            
        var transaction = YAHOO.util.Connect.asyncRequest('GET', 
            action +'&bustcache='+new Date().getTime(), 
            callback);
    },
    
    /** 
     * initialize the page
     * this is called once the dom has
     * been created 
     */
    init : function() 
    {
        YAHOO.ur.researcher.link.createNewLinkDialog();
        YAHOO.ur.researcher.link.clearLinkForm();
        YAHOO.ur.researcher.link.deleteLinkDialog();
    }

};


// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.researcher.link.init);