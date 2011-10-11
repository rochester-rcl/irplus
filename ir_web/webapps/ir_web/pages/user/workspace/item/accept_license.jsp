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
        <title>Accept License for: ${repository.name}</title>
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
 	    <ur:js src="page-resources/js/util/base_path.jsp"/>
 	    <ur:js src="page-resources/js/util/ur_util.js"/>
   
          <script language ="javascript">

           // make sure the user accepts the license
           function formValidation() {
               if(urUtil.checkForAtLeastOneSelection(document.getElementById('acceptLicense')) == false )
        	   {
                   alert('You must agree to the license');
            	   return false;
        	   }
        	   return true;
           }
        
        </script>
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body regin of the page -->
            <div id="bd">
               <h3>Accept License</h3> 
             
               <c:url var="acceptLicense" value="/user/acceptRepositoryLicense.action"/>
               
               <form id="addUser" name="irUserForm" method="post" 
		              action="${acceptLicense}" onSubmit="return formValidation();">
		              
                  <table class="formTable">
                      <tr>
                          <td colspan="2">
		                      <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                       key="licenseChangeError"/></p>
		                      <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                       key="licenseError"/></p>
			              </td>
			          </tr>
                      <tr>
                          <td align="left" class="label">I accept the terms of the License</td>
                          <td><input id="acceptLicense" name="acceptLicense" value="true" type="checkbox"/></td>
                      </tr>
                      <tr>
                          <td align="left" class="label">License</td>
                          <td><textarea readonly="true" rows="20" cols="120"><c:out value="${repository.defaultLicense.license.text}"/> </textarea></td>
                      </tr>
    
                      <tr>
                      <td colspan="2" align="center">
						  <button id="create_account" class="ur_button" type="submit"
 		                               onmouseover="this.className='ur_buttonover';"
 		                               onmouseout="this.className='ur_button';">Continue</button>                      
                      </td>
                      </tr>
                      
                      
                  </table>
		          <input type="hidden" name="licenseId" value="${repository.defaultLicense.id}"/>
		          <input type="hidden" name="genericItemId" value="${genericItemId}"/>

		        </form>
               
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
