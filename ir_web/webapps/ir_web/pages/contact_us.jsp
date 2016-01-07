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
<%@ taglib prefix="ir" uri="ir-tags"%>



<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title>Contact Us</title>
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
        <ur:js src="page-resources/js/google_analytics.js"/>
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body regin of the page -->
            <div id="bd">
               <h3>Contact Us</h3> 
               
               <form  method="post" 
		              action="<c:url value="/sendHelpEmail.action"/>">
		              <table class="formTable">  
		                  
			              <tr>       
	                          <td align="left" class="label">
	                              Subject:*
	                          </td>
	                          <td align="left" class="input">
	                              <span class="errorMessage"><ir:printError errors="${fieldErrors}" key="subject"/></span> <br/>
	                              <input type="text" name="subject"  value="${subject}" size="82"/> 
	                          </td>
			              </tr>
			              <tr>       
	                          <td align="left" class="label">
	                              Email Address:*
	                          </td>
	                          <td align="left" class="input">
	                              <span class="errorMessage"><ir:printError errors="${fieldErrors}" key="from"/></span> <br/>
	                              <input type="text" name="from"  value="${email}" size="82"/> 
	                          </td>
			              </tr>
			              <tr>
			                  <td align="left" class="label">
			                      Message*:
			                  </td>
			                  <td align="left" colspan="2" class="input"> 
			                      <span class="errorMessage"><ir:printError errors="${fieldErrors}" key="message"/></span> <br/>
			                      <textarea name="message" 
		                           cols="80" rows="20">${message}</textarea>
	                          </td>
			              </tr>
			              <tr class="email-special-label">
			                  <td align="left" >
			                      Reason*:
			                  </td>
			                  <td>
			                      <span class="errorMessage"><ir:printError errors="${fieldErrors}" key="reason"/></span> <br/>
			                      <input type="text" name="reason" size="82"/>
			                  </td>
			              </tr>
			              <tr>
			                  <td><input type="submit" value="Send"/></td>
			              </tr>
	                  </table>
               </form>
               
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
