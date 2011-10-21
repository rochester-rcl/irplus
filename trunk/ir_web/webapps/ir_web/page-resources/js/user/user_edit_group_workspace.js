/*
   Copyright 2008-2010 University of Rochester

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
 * This code is for dealing with adding and removing group spaces
 */
YAHOO.namespace("ur.user.edit.groupspace");

//actions for adding,updating and removing content types
var getGroupSpaceAction = basePath + 'user/getGroupWorkspace.action';

var myTabs;
/**
 * content type namespace
 */
YAHOO.ur.user.edit.groupspace = 
{
	     /*
	      * This loads the tab based on the needs of the system
	      * and what to display to the user.
	      */
	     setActiveIndex : function(tabName)
	     {
	 
	         if( tabName == "WORKSPACE")
	         {
	             myTabs.set('activeIndex', 0);
	         }
	         else if( tabName == "INVITE")
	         {
	             myTabs.set('activeIndex', 1);
	         }
	         else if( tabName == "USERS" )
	         {
	             myTabs.set('activeIndex', 2);
	         }
	         else
	         {
	        	 //default to workspace
	        	 myTabs.set('activeIndex', 0);
	         }
	     },
	     
		// initialize the page
		// this is called once the dom has
		// been created
		init : function() 
		{
	        myTabs = new YAHOO.widget.TabView("groupWorkspacePropertiesTabs");
	        var tabName = document.getElementById('set_tab_name').value;
	        YAHOO.ur.user.edit.groupspace.setActiveIndex(tabName);
		}
}

//initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.user.edit.groupspace.init);