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

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title><s:text name="home.welcome"/></title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!-- Core + Skin CSS -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
        <ur:styleSheet href="page-resources/css/base-ur.css"/>
        <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>

        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/home_page_content_area.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>

        <!-- javascript files --> 
        <ur:js src="page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"/>
        <ur:js src="page-resources/yui/container/container_core-min.js"/>
        <ur:js src="page-resources/yui/menu/menu-min.js"/>

        <ur:js src="page-resources/js/menu/main_menu.js"/>
	    <ur:js src="pages/js/base_path.js"/>
	    <ur:js src="page-resources/js/util/ur_util.js"/>
       <script language ="javascript">
        
        function formValidation() {

        	if (document.getElementById('newUserForm_first_name').value == '') {
        		alert('Please enter first Name.');
        		return false;
        	}

        	if (document.getElementById('newUserForm_last_name').value == '') {
        		alert('Please enter last Name.');
        		return false;
        	}

        	if (document.getElementById('newUserForm_name').value == '') {
        		alert('Please enter user Name.');
        		return false;
        	}

        	if (document.getElementById('newUserForm_password').value == '') {
        		alert('Please enter password.');
        		return false;
        	}

        	if (document.getElementById('newUserForm_password_check').value == '') {
        		alert('Please enter password check.');
        		return false;
        	}
        	
        	if (document.getElementById('newUserForm_password_check').value != document.getElementById('newUserForm_password').value) {
        		alert('The password check does not match with the password.');
        		return false;
        	}

        	if (document.getElementById('newUserForm_email').value == '') {
        		alert('Please enter E-mail address.');
        		return false;
        	}

        	if (!urUtil.emailcheck(document.getElementById('newUserForm_email').value)) {
        		alert('Invalid E-mail address.');
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
            <br/>
            Please enter the following information to create an account.
 			<div class="clear">&nbsp;</div>
                  <ur:basicForm id="addUser" name="newUserForm" method="post" 
		              action="createNewUser.action" onSubmit="return formValidation();">
		              
		              <input type="hidden" name="token" value="${token}"/>
		               
					  <table class="formTable">
					  <tr>
					  <td colspan="2">
				          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                       key="userNameError"/></p>

			          </td>
			          </tr>    

					  <tr>
					  <td colspan="2">
				          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                       key="emailExistError"/></p>
			          </td>
			          </tr>    

					  <tr>
					  <td>
				          <label for="newUserForm_first_name" class="label">First Name:</label>
			          </td>
			          <td>
				          <input type="text" class="input"
				              id="newUserForm_first_name" name="irUser.firstName" value="${irUser.firstName}" size="40"/> 
				              
			          </td>
			          </tr>    

					  <tr>
					  <td>
			
				          <label class="label" for="newUserForm_last_name">Last Name:</label>
				      </td>
				      <td>
				      		<input type="text" class="input"  
				              id="newUserForm_last_name" name="irUser.lastName" value="${irUser.lastName}" size="40"/>
				      </td>
				      </tr>

			          <tr>
			          <td>
				          <label class="label" for="newUserForm_name">User Name:</label>
					  </td>
					  <td>
					  		<input type="text" class="input"  
				              id="newUserForm_name" name="irUser.username" value="${irUser.username}" size="40"/>
				     </td>
				     </tr>

				     <tr>
						<td> 				          
				          <label class="label" for="newUserForm_password">Password:</label>
					  </td>
					  <td>
					  <input type="password" 
				              id="newUserForm_password" name="irUser.password" value="" size="35"/>
				      </td>
				      </tr>

				      <tr>
				      <td>				  
				          <label class="label" for="newUserForm_password_check">Password Check:</label>
				      </td>
				      <td> <input type="password" 
				              id="newUserForm_password_check" name="passwordCheck" value="" size="35"/>
				             </td>
	                  </tr>
					  <tr>
						<td> 
			                <label class="label" for="newUserForm_email">E-mail:</label>
			            </td>
			            <td> <input type="text" class="input"  
				              id="newUserForm_email" name="defaultEmail.email" value="${defaultEmail.email}" size="40"/>
			            </td>
			          </tr>
			            
			          <tr>
			            <td>
			              <label class="label" for="newUserForm_phone_number">Phone Number:</label>
		                </td>
		                <td>
		               		<input type="text" class="input"  
			              id="newUserForm_phone_number" name="irUser.phoneNumber" value="${irUser.phoneNumber}" size="40"/>
	                      </td>
                      </tr>
                      
                      <tr>
			            <td>
			              <label class="label" for="newUserForm_affiliation">Affiliation:</label>
		                </td>
		                <td>
           		      	   <select id="newUserForm_affiliation" name="affiliationId" />
					      		<c:forEach items="${affiliations}" var="affiliation">
					      			<option value = "${affiliation.id}"> ${affiliation.name}</option>
					      		</c:forEach>
				      	   </select>

	                      </td>
                      </tr>

                      <tr>
			            <td>
			              <label class="label" for="newUserForm_department">Department:</label>
		                </td>
		                <td>
           		      	   <select id="newUserForm_department" name="departmentId" />
           		      	   		<option value = "0"> N/A</option>
					      		<c:forEach items="${departments}" var="department">
					      			<option value = "${department.id}"> ${department.name}</option>
					      		</c:forEach>
				      	   </select>

	                      </td>
                      </tr>
                      
    
                      <tr>
                      <td colspan="2">
                      <input type="submit" value="Create account" >
                      </td>
                      </tr>
                      </table>
		      
		          </ur:basicForm>
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
