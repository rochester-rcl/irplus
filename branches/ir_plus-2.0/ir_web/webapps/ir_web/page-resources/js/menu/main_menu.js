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

  
  // Initialize and render the menu bar when it is available in the DOM
  YAHOO.util.Event.onContentReady("mainMenu", function () {
      // Instantiate and render the menu bar
      var oMenuBar = new YAHOO.widget.MenuBar("mainMenu", { autosubmenudisplay: false, hidedelay: 750, lazyload: true });
      /*
          Call the "render" method with no arguments since the markup for 
          this menu already exists in the DOM.
      */
      oMenuBar.render();
  });

  