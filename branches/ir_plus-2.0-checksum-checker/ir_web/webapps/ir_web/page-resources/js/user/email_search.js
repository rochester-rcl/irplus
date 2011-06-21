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
YAHOO.namespace("ur.invite.search");


/**
 * approve affiliation namespace
 */
YAHOO.ur.invite.search = {

	
	executeEmailSearch : function(rowStart, startPageNumber, currentPageNumber)
	{
	    YAHOO.util.Connect.setForm('emailSearchForm');
	    
	    /*
	     * This call back updates the html when email search results are
	     * retrieved.
	     */
	    var callback =
	    {
	        success: function(o) 
	        {
			    // check for the timeout - forward user to login page if timout
	            // occured
	            if( !urUtil.checkTimeOut(o.responseText) )
	            {       	        
	                var divToUpdate = document.getElementById('search_results');
	                divToUpdate.innerHTML = o.responseText;
	            } 
	        },
		
		    failure: function(o) 
		    {
		        alert('Email Search Failure ' + o.status + ' status text ' + o.statusText );
		    }
	    }
	    
	
	    // action to perform when submitting the personal folder form.
	    var emailSearchAction =  basePath + 'user/emailSearch.action';
	    var cObj = YAHOO.util.Connect.asyncRequest('POST',
	            emailSearchAction + '?rowStart=' + rowStart +  '&startPageNumber=' + startPageNumber + '&currentPageNumber=' + currentPageNumber, callback);
	        
	},
	
	/*
	 * If a user selects an email, this populates email text with selected email and activates the
	 * invite tab
	 */ 
	showInvite : function(email)
	{
		var currentEmails = urUtil.trim(document.getElementById('newInviteForm_inviteEmail').value);
		if( currentEmails == null || currentEmails == "")
		{
		    document.getElementById('newInviteForm_inviteEmail').value = email;
		}
		else
		{
			document.getElementById('newInviteForm_inviteEmail').value = currentEmails + ";" + email
		}
	    myTabs.set('activeIndex', 0);
	    
	}
}	
