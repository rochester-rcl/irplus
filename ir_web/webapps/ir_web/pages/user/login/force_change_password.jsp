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
        <title>Change Password</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!-- Core + Skin CSS -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
        <ur:styleSheet href="page-resources/css/base-ur.css"/>
        <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>

        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/home_page_content_area.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>

        <!-- Dependencies --> 
        <ur:js src="page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"/>
        <ur:js src="page-resources/yui/container/container_core-min.js"/>
        <ur:js src="page-resources/yui/menu/menu-min.js"/>
        
        <!-- Source File -->
        <ur:js src="page-resources/js/menu/main_menu.js"/>
    
               <script language ="javascript">
        
        function formValidation() {

        	if (document.getElementById('changePasswordForm_password').value == '') {
        		alert('Please enter password.');
        		return false;
        	}

        	if (document.getElementById('changePasswordForm_confirm_password').value == '') {
        		alert('Please enter confirm password.');
        		return false;
        	}
        	
        	if (document.getElementById('changePasswordForm_confirm_password').value != document.getElementById('changePasswordForm_password').value) {
        		alert('The password does not match with the confirm password.');
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
                
                <!--  create the first column -->
                <div class="yui-g">
                <div class="yui-u first">
                    
                   <div class="contentContainer">
                       <div class="contentBoxTitle">
                           <p>Change password </p>
                       </div>
                   
                       <div class="contentBoxContent">
                       	  </br>
                       
                       	  <p> 
                       	  
	            	          <ur:basicForm id="changePassword" name="changePasswordForm" method="POST" 
					              action="user/forceChangePassword.action" onSubmit="return formValidation();">
					              
					              <input type="hidden" name="token" value=${token}> 
		        					<table class="formTable">
			        					<tr>
			        					<td>  <label  for="newPassword" class="label"> New Password </label> </td> 
			        				    <td> <input type="password" class="input" id="changePasswordForm_password" name="password"> </td>
			        				    </tr>
			        				    
			        				    <tr>
			        					<td> <label  for="newPassword" class="label"> Confirm Password </label>  </td> 
			        				    <td>  <input type="password" class="input" id="changePasswordForm_confirm_password" name="confirmPassword"></td>
			        				    </tr>
			        				   
			        				   <tr>
			        					<td colspan="2" align="center">  <input type="submit" value="Save"/> </td>
			        				   </tr>
			        				 </table>
		        				    
					          </ur:basicForm>
					      
				          </p>
                       </div>
                   </div>
                </div>
                <!--  end the first column -->
                 
                 
            
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

    
