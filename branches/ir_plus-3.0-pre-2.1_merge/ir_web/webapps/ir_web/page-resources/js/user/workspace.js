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

 YAHOO.namespace("ur.user.workspace");
 
 /* define global variable for tabs */
 var myTabs;
  
 YAHOO.ur.user.workspace = 
 {
 
     /*
      * This loads the tab based on the needs of the system
      * and what to display to the user.
      */
     setActiveIndex : function(tabName)
     {
 
         if( tabName == "FOLDER")
         {
             myTabs.set('activeIndex', 0);
         }
         else if( tabName == "GROUP_WORKSPACE")
         {
             myTabs.set('activeIndex', 1);
         }
         else if( tabName == "COLLECTION" )
         {
             myTabs.set('activeIndex', 2);
         }
         else if( tabName == "SEARCH" )
         {
             myTabs.set('activeIndex', 3);
         }
         else if( tabName == "FILE_INBOX" )
         {
             myTabs.set('activeIndex', 4);
         }
         else
         {
        	 //default to folders tab
        	 myTabs.set('activeIndex', 0);
         }
     },
     
    init : function()
    {
        myTabs = new YAHOO.widget.TabView("workspace-tabs");
   		var tab0 = myTabs.getTab(0);
   		var tab1 = myTabs.getTab(1);
   		var tab2 = myTabs.getTab(2);
   		var tab3 = myTabs.getTab(3);
   		var tab4 = myTabs.getTab(4);
    
	    tab0.addListener('click', YAHOO.ur.shared.file.inbox.getSharedFilesCount);
	    tab1.addListener('click', YAHOO.ur.shared.file.inbox.getSharedFilesCount);
	    tab2.addListener('click', YAHOO.ur.shared.file.inbox.getSharedFilesCount);
	    tab3.addListener('click', YAHOO.ur.shared.file.inbox.getSharedFilesCount);
	    tab4.addListener('click', YAHOO.ur.shared.file.inbox.getSharedFiles);
	    
	    var tabName = document.getElementById('set_tab_name').value;
	    YAHOO.ur.user.workspace.setActiveIndex(tabName);
    }
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady( YAHOO.ur.user.workspace.init);