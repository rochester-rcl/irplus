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

 YAHOO.namespace("ur.researcher.tabs");

 /* define global variable for tabs */
 var myTabs;

/**
 * sponsor namespace
 */
YAHOO.ur.researcher.tabs = {
 
	 /*
	  * This loads the tab based on the needs of the system
	  * and what to display to the user.
	  */
	 setActiveIndex : function(tabName)
	 {
	 
	     if( tabName == "INFO")
	     {
	         myTabs.set('activeIndex', 0);
	     }
	     else if( tabName == "FOLDERS" )
	     {
	         myTabs.set('activeIndex', 1);
	     }
	     else if( tabName == "PICTURE" )
	     {
	         myTabs.set(activeIndex, 2);
	     }
	     else if( tabName == "PREVIEW" )
	     {
	         myTabs.set(activeIndex, 3);
	     }
	    
	 },

     /**
      * initialize the tabs
      */
     init : function()
     {
          myTabs = new YAHOO.widget.TabView("researcher-properties-tabs");
     }
}

//initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady( YAHOO.ur.researcher.tabs.init);