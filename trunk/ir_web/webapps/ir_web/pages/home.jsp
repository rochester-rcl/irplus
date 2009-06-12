<!--  
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
-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>


<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title>Welcome</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!-- Core + Skin CSS -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
        <ur:styleSheet href="page-resources/css/base-ur.css"/>
        <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>

        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/home_page_content_area.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>

        <!-- Dependencies --> 
        <ur:js src="page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"/>
        <ur:js src="page-resources/yui/connection/connection-min.js"/>
        <ur:js src="page-resources/yui/container/container_core-min.js"/>
        <ur:js src="page-resources/yui/menu/menu-min.js"/>
        
        <!-- Source File -->
        <ur:js src="page-resources/js/menu/main_menu.js"/>
        
        <!--  base path information -->
 	    <ur:js src="pages/js/base_path.js"/>
 	    <ur:js src="page-resources/js/util/ur_util.js"/>
        <ur:js src="page-resources/js/public/home.js"/>
        
        <!--  styling for page specific elements -->
        <style type="text/css">
            .ur_button
            {
                width: 100px;
                margin: 0px;
                padding: 0px;
            }
            
            .ur_buttonover
            {
                width: 100px;
                margin: 0px;
                padding: 0px;
            }
            
 
        </style>
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body regin of the page -->
            <div id="bd">
                
                <!--  create the first column -->
                <div class="yui-g">
                <div class="yui-u first">
                    
                   <div class="contentContainer">
                       <div class="contentBoxTitle">
                           <c:url var="browseRepositoryItems" value="/browseRepositoryItems.action"/>
                           <p><a href="${browseRepositoryItems}">Browse/Search</a></p>
                       </div>
                   
                       <div class="contentBoxContent">
                           <c:url var="searchRepositoryItems" value="/searchRepositoryItems.action"/>
                           <form method="GET" action="${searchRepositoryItems}">
                                <p>Search: <input type="text" name="query" size="50"/>
                                 <br/>
                                 <br/>
                                    <button type="submit" class="ur_button" 
		                               onmouseover="this.className='ur_buttonover';"
	 		                           onmouseout="this.className='ur_button';">Search</button>
                                </p>
                                
                           </form>
                           
                       </div>
                   </div>
                   
                    <div class="contentContainer">
                        <div class="contentBoxTitle">
                            <p>Institutional Collections</p>
                        </div>
                        <div id="institutional_collections" class="contentBoxContent">
                        </div>
                    </div>

                   <div class="contentContainer">
                       <div class="contentBoxTitle">
                           <p>Statistics </p>
                       </div>
                   
                       <div class="contentBoxContent">
                       		<div id="statistics_div">
                       		</div>
                      </div>
                   </div>
                    
                </div>
                <!--  end the first column -->
            
                <!--  Start the second column -->
                <div class="yui-u">
                    <div class="contentContainer">
                        <div class="contentBoxTitle">
                            <p>What is irplus?</p>
                        </div>
                        <div class="contentBoxContent">
                            <div id="ur_research_description" >
                            <p>A university-based, long-term, digital repository for the management,
                             dissemination and stewardship of digital materials. </p>
                            </div>
                        </div>
                    </div>
                   <div class="contentContainer">
                        <div class="contentBoxTitle">
                        	<c:url var="browseResearchers" value="/viewResearcherBrowse.action"/>
                            <p><a href="${browseResearchers}">Researchers</a></p>
                        </div>
                        <div class="contentBoxContent">
                            <div id="researcher_picture" >
                            </div>
                        </div>
                    </div>
                    
                    <div class="contentContainer">
                        <div class="contentBoxTitle">
                            <p>Pictures - See pictures of our campus!</p>
                        </div>
                   
                        <div class="contentBoxContentPicture">
                            <div id="repository_picture" >
                            </div>
                        </div>
                    </div>
                    
                    <div class="contentContainer">
                        <div class="contentBoxTitle">
                            <p>News</p>
                        </div>
                        <div id="news_items" class="contentBoxContent">
                        </div>
                    </div>
                </div>
                <!--  end the second column -->
                
                </div>
                <!--  end the grid -->
                
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
