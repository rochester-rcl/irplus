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
        <title>Register</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!-- Core + Skin CSS -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
        <ur:styleSheet href="page-resources/css/base-ur.css"/>
        <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>

        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>

        <!-- javascript files --> 
        <ur:js src="page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"/>
        <ur:js src="page-resources/yui/container/container_core-min.js"/>
        <ur:js src="page-resources/yui/menu/menu-min.js"/>

        <ur:js src="page-resources/js/menu/main_menu.js"/>
        <ur:js src="page-resources/js/util/ur_util.js"/>
	    <ur:js src="pages/js/base_path.js"/>    
       
       <script language ="javascript">
        
        function formValidation() {

        	
        	
        	if (document.getElementById('irUserForm_first_name').value == '') {
        		alert('Please enter first Name.');
        		return false;
        	}

        	if (document.getElementById('irUserForm_last_name').value == '') {
        		alert('Please enter last Name.');
        		return false;
        	}

        	if (document.getElementById('irUserForm_name').value == '') {
        		alert('Please enter user Name.');
        		return false;
        	}

        	if (document.getElementById('irUserForm_password').value == '' ||document.getElementById('irUserForm_password').value.length < 8 ) {
        		alert('Please enter password at least 8 characters long.');
        		return false;
        	}

        	if (document.getElementById('irUserForm_password_check').value == '') {
        		alert('Please enter password check.');
        		return false;
        	}

        	if (document.getElementById('irUserForm_password_check').value != document.getElementById('irUserForm_password').value) {
        		alert('The password check does not match with the password.');
        		return false;
        	}

         	if (document.getElementById('irUserForm_email').value == '') {
        		alert('Please enter E-mail address.');
        		return false;
        	}

        	if (!urUtil.emailcheck(document.getElementById('irUserForm_email').value)) {
        		alert('Invalid E-mail address.');
        		return false;
        	}

        	
        	if(urUtil.checkForAtLeastOneSelection(document.getElementById('acceptLicense')) == false )
        	{
            	alert('You must agree to the license');
            	return false;
        	}

        	if ( document.getElementById('irUserForm_ldapUserName').value != '') {
            	if( document.getElementById('irUserForm_ldapPassword').value == null || document.getElementById('irUserForm_ldapPassword').value == '')
            	{
        		    alert('You must enter your Net ID Password for verification or clear out the Net Id field');
        		    return false;
            	}
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
            <br/>
            Please enter the following information to create an account.

                  <form id="addUser" name="irUserForm" method="post" 
		              action="registerNewUser.action" onSubmit="return formValidation();">
		              
		              <input type="hidden" name="token" value="${token}"/>
		               <c:url  var="loginPage" value="/acegilogin.jsp"/>
		               
					  <table class="formTable">
					  <tr>
					      <td colspan="2">
				              <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                       key="userNameError"/></p>
		                      <p class="errorMessage">
		                           <ir:printError errors="${fieldErrors}" key="emailExistError"/>
		                           <c:if test="${emailAlreadyExists}"> - Use the forgot password link on <a href="${loginPage}">this page</a> to get a new password</c:if>
		                       </p>
		                      <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                       key="licenseChangeError"/></p>
		                      <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                       key="licenseError"/></p>
		                      <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                       key="enterPassword"/></p>
		                       <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                       key="netIdPasswordFail"/></p>
		                       <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                       key="netIdPasswordEmpty"/></p>
		                       <p class="errorMessage">
		                           <ir:printError errors="${fieldErrors}"  key="nnetIdAlreadyExists"/> 
		                           <c:if test="${netIdAlreadyExists}"> - Try <a href="${loginPage}">Logging In</a></c:if>
		                       </p>
			              </td>
			          </tr>    
   

					  <tr>
					  <td align="left" class="label">
				          First Name:*
			          </td>
			          <td align="left" class="input">
				          <input type="text" 
				              id="irUserForm_first_name" name="irUser.firstName" value="${irUser.firstName}" size="40"/> 
				              
			          </td>
			          </tr>    

					  <tr>
					  <td  align="left" class="label">
			
				          Last Name:*
				      </td>
				      <td align="left" class="input">
				      		<input type="text" class="input"  
				              id="irUserForm_last_name" name="irUser.lastName" value="${irUser.lastName}" size="40"/>
				      </td>
				      </tr align="left" class="label">
			          
			          <tr> 
			              <td align="left" class="label">
				          User Name:*
					      </td>
					      <td align="left" class="input">
					  		<input type="text" 
				              id="irUserForm_name" name="irUser.username" value="${irUser.username}" size="40"/>
				         </td>
				     </tr>

				     <tr>
				         <td align="left" class="label"> 				          
				              Password(At least 8 characters):*
					     </td>
					     <td align="left" class="input">
					      <input type="password" 
				              id="irUserForm_password" name="irUser.password" value="" size="40"/>
				          </td>
				      </tr>

				      <tr>
				      <td align="left" class="label">				  
				         Retype Password:*
				      </td>
				      <td align="left" class="input"> <input type="password" 
				              id="irUserForm_password_check" name="passwordCheck" value="" size="40"/>
				             </td>
	                  </tr>
	            
	                  
					  <tr>
						<td align="left" class="label"> 
			                E-mail:*
			            </td>
			            <td align="left" class="input"> <input type="text" 
				              id="irUserForm_email" name="defaultEmail.email" value="${defaultEmail.email}" size="40"/>
			            </td>
			          </tr>
			            
			          <tr>
			            <td align="left" class="label">
			              Phone Number:
		                </td>
		                <td align="left" class="input">
		               		<input type="text" 
			              id="irUserForm_phone_number" name="irUser.phoneNumber" value="${irUser.phoneNumber}" size="40"/>
	                      </td>
                      </tr>
                      
                      <tr>
			            <td align="left" class="label">
			              University Affiliation:
		                </td>
		                <td  align="left" class="input">
           		      	   <select id="irUserForm_affiliation" name="affiliationId" />
					      		<c:forEach items="${affiliations}" var="affiliation">
					      		    <c:if test="${affiliation.id != affiliationId}">
					      			    <option value = "${affiliation.id}"> ${affiliation.name}</option>
					      			</c:if>
					      			<c:if test="${affiliation.id == affiliationId}">
					      			    <option selected="selected" value = "${affiliation.id}"> ${affiliation.name}</option>
					      			</c:if>
					      		</c:forEach>
				      	   </select>

	                      </td>
                      </tr>
                      
                     <tr>
		                 <td align="left" class="label">Department(s):</td>
                         <td align="left" class="input"> 
      	                     <select multiple="multiple" id="editUserForm_department" name="departmentIds" />
      	   		
	      		             <c:forEach items="${departments}" var="department">
	      			             <option value = "${department.id}"
	      			             <c:forEach items="${irUser.departments}" var="userDepartment">
	      			                 <c:if test="${department.id == userDepartment.id}">
	      				             selected
	      			                 </c:if>
	      			             </c:forEach>
	      			             > ${department.name}</option>
	      		             </c:forEach>
      	                     </select>
      	                 </td> 
	                  </tr>              
                      <tr>
	                       <td align="left" class="label" colspan="2">		
	                          If you wish, you may use your Net ID  and password to login. <strong>Net ID is NOT required to use this system.</strong>		  
				          </td>
				      </tr>
	                  <tr> 
			              <td align="left" class="label">
				              Net ID:
					      </td>
					      <td align="left" class="input">
					  		<input type="text" 
				              id="irUserForm_ldapUserName" name="irUser.ldapUserName" value="${irUser.ldapUserName}" size="40"/>
				         </td>
				     </tr>
				     <tr>
				         <td align="left" class="label"> 				          
				              Net ID Password:
					     </td>
					     <td align="left" class="input">
					      <input type="password" 
				              id="irUserForm_ldapPassword" name="netIdPassword" value="" size="40"/>
				          </td>
				      </tr>
                      <tr>
                          <td align="left" class="label">I accept the terms of the License</td>
                          <td><input id="acceptLicense" name="acceptLicense" value="true" type="checkbox"/></td>
                      </tr>
                      <tr>
                          <td align="left" class="label">License</td>
                          <td><textarea disabled="disabled" rows="20" cols="60"><c:out value="${repository.defaultLicense.license.text}"/> </textarea></td>
                      </tr>
    
                      <tr>
                      <td colspan="2" align="center">
						  <button id="create_account" class="ur_button" type="submit"
 		                               onmouseover="this.className='ur_buttonover';"
 		                               onmouseout="this.className='ur_button';">Create account</button>                      
                      </td>
                      </tr>
                      
                      
                      </table>
		              <input type="hidden" name="licenseId" value="${repository.defaultLicense.id}"/>
		          </form>
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
