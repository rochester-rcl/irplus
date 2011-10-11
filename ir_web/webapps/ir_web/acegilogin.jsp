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

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<fmt:setBundle basename="messages"/>

<html>
    <head>
        <title>Login</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!-- Core + Skin CSS -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
        <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
        <ur:styleSheet href="page-resources/css/base-ur.css"/>

        <ur:styleSheet href="page-resources/css/main_menu.css"/>
        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>
    
       
       <style type="text/css">
       
           .label
           {
               float: left;
               width: 20em;
               text-align: right;
               clear: left;
               margin-right: 15px;
               font-weight: bold;
           }
           
           .input
           {
               font-size: .9em;
               width: 200px;
               margin-top: -2px;
           }
           
           .a
           {
               margin-left: 300px;
           }
           
           #loginForm
           {
               background: #CCC;
               margin-left: 15%;
               margin-right: 20%;
               padding-top: 40px;
               padding-bottom: 40px;
           }
           
           #buttons
           {
               margin-left: 300px;
           }
               
	    <!--  Style for dialog boxes -->
	        dialogLabel { display:block;float:left;width:45%;clear:left; }
	        .clear { clear:both; }

	    </style>
	    

    
        <!--  Style for dialog boxes -->
        <ur:js src="page-resources/yui/utilities/utilities.js"/>
        <ur:js src="page-resources/yui/button/button-min.js"/>
        <ur:js src="page-resources/yui/container/container-min.js"/>
 	    <ur:js src="page-resources/yui/menu/menu-min.js"/>
        
        <!-- Source File -->
        <ur:js src="page-resources/js/util/base_path.jsp"/>
        <ur:js src="page-resources/js/util/ur_util.js"/>
        <ur:js src="page-resources/js/menu/main_menu.js"/>
        <ur:js src="page-resources/js/user/forgot_password.js"/>        
    </head>

    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body regin of the page -->
            <div id="bd">

               <h3>User Login</h3>
                   <%-- this form-login-page form is also used as the 
                   form-error-page to ask for a login again.
                   --%>
                   <c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION.message}">
                          <font color="red">
                             Your login attempt was not successful, try again.<br/><br/>
                             Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
                          </font>
                   </c:if>

                <!--  DO NOT chage the id value of this form  it is used to determine if the
                      login form is present see ur_util.js  -->
                <div id="loginForm">
                 <form name="f" action="<c:url value='j_spring_security_check'/>" method="POST">
                          <br/>
                          <br/>
                          <label class="label" for="j_name">User Name:</label>
                          <input tabindex="1" type='text' size="30" name='j_username' value='<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION.message}"><c:out escapeXml="false" value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/>
                           
                          <br/>
                          <br/>
                          <label class="label" for="j_name">Password:</label>
                          <input class="input" size="30" type='password' tabindex="2" id="password" autocomplete="off" name='j_password'/>
                          <br/>
                          <br/>
                          
                          <div id="buttons">
                              <input tabindex="3" value="<s:text name="login"/>" type="submit">
                              <input tabindex="4" value="<s:text name="reset"/>" type="reset">
                          </div>
                          
                          <br/>
                          <br/>
                          
                   </form>
                   <p align="left"><a class ="a" href="javascript:YAHOO.ur.login.forgotPassword();"> Forgot password? </a> or  
                          <a href="<c:url value='viewUserRegistration.action'/>">Create New Account?</a></p>
                   </div>
                   
			        <div id="forgotPasswordDialog" class="hidden">
		                <div class="hd">Forgot password</div>
		                <div class="bd">
		                    <form id="forgotPassword" name="forgotPasswordForm" 
				                    method="post" 
				                    action="<c:url value="/savePasswordToken.action"/>">
				              Please enter the email id associated with your account.
				              <div class="clear">&nbsp;</div>
				              
			                  <div id="forgotPasswordDialogFields">
			                      <c:import url="/pages/user/login/forgot_password_form.jsp"/>
			                  </div>
			                </form>
		                </div>
		            </div>
		            
		          
                   
                
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>