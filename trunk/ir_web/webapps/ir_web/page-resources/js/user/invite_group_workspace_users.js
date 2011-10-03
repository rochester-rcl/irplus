/*
   Copyright 2008-2011 University of Rochester

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
 * This code is for dealing with adding invite user
 */
YAHOO.namespace("ur.group_workspace_invite");

YAHOO.ur.group_workspace_invite = 
{
		/**
		 * Submit the form to invite users
		 */
		submitInviteForm : function ()
		{
	        if( YAHOO.ur.group_workspace_invite.validateEmails() )
	        {
	        	document.newInviteForm.submit();
	        }
		},

		/**
		 * Validate the emails before sending
		 */
		validateEmails : function ()
		{
	        var email = urUtil.trim(document.newInviteForm.emails.value);
            if (email == "") 
            {
                alert('Please enter a valid E-mail address.');
	            return false;
            } 
            else 
            {
    	        var emails = email.split(";");
    	        for( i = 0; i < emails.length; i++)
    	        {
    		        checkEmail = urUtil.trim(emails[i])
    		        // last one - this needs to be checked
    		        // for ending semicolon
    		        if( emails.length > 0 && i == (emails.length -1) )
    		        {
    			         if ( checkEmail == "" || checkEmail == null)
    			         {
    				          // ok
    			         }
    		        }
    		        else
    		        {
	                    if (!urUtil.emailcheck(checkEmail)) 
	                    {
		                    alert('Invalid E-mail address ' + emails[i]);
		                    return false;
	                    }
    		        }
    	        }
            }
            return  true;	
		}
}