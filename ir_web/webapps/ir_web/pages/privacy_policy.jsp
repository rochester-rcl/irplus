
<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

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
        <title>Privacy Policy</title>
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
   
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body regin of the page -->
            <div id="bd">
               <h3>Privacy Policy</h3> 
               
               University of Rochester Libraries is committed to preserving your privacy. 
               The personal information we receive through UR Research, such as names, 
               emails and phone numbers, is used solely for the purposes of the 
               functioning and assessment of the system.
               <br><br>
               This system collects personal information from:
               <ol>

	               <li>users involved in the submission of UR Research content and metadata
	               <li>users who subscribe to the UR Research alerting service
	               <li>users who locate and download content from UR Research (i.e., 
	                user IP address)

               </ol>
               Personal information collected by UR Research will not be used for any 
               commercial or philanthropic purpose not directly connected with and 
               approved by the University of Rochester.
               <br><br>
               We do not disclose information about your individual visits to 
               our site, or personal information that you provide us, such as 
               your name, address, telephone number, etc. to any outside parties 
               except when (i) the law requires it, or (ii) that disclosure is 
               necessary to protect the rights and property of UR Research users.
               <br><br>

               Any DUR Research records used in publicly accessible forum, such as 
               demonstrations, presentations, or research papers, will be 
               scrubbed of specific references to real people and personal information.
               <br><br>

               
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
