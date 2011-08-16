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

<!--  form fragment for dealing with adding new users
      this form will return with error messages in it if there
      is an issue.
 -->
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<div id="userError">
        
          <p class="errorMessage" id="user_already_exists"><ir:printError errors="${fieldErrors}" 
                   key="userAlreadyExists"/></p>
                    
          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
                   key="emailExistError"/></p>
           
          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
                   key="rolesNotSelectedError"/></p>
                   
          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
                   key="externalAccountError"/></p>
</div>

<table class="formTable">

		<!--  represents a successful submission -->
		<input type="hidden" id="newUserForm_success" value="${added}"/>
		   
		<input type="hidden" id="newUserForm_id" name="id" value="${irUser.id}"/>         	   		              
			         
         <tr>  
             <td align="left" class="label">First Name:</td>
             <td align="left" class="input"><input type="text" 
              id="newUserForm_first_name" name="irUser.firstName" value="${irUser.firstName}" size="45"/> </td>
         </tr>    
   
         <tr>  
             <td align="left" class="label">Last Name:</td>
             <td align="left" class="input">
   		         <input type="text" 
   	              id="newUserForm_last_name" name="irUser.lastName" value="${irUser.lastName}" size="45"/></td>
         </tr>
   
         <tr>  
             <td align="left" class="label">User Name:*</td>
             <td align="left" class="input"><input type="text"  
              id="newUserForm_name" name="irUser.username" value="${irUser.username}" size="45"/></td>
         </tr>
          

         <tr>  
             <td align="left" class="label">Password:*</td>
             <td align="left" class="input"><input type="password"
	              id="newUserForm_password" name="irUser.password" value="${irUser.password}" size="45"/></td>
	     </tr>
  
         <tr>
              <td align="left" class="label">Password Check:*</td>
              <td align="left" class="input"><input type="password" 
              id="newUserForm_password_check" name="passwordCheck" value="${irUser.password}" size="45"/></td>
         </tr>
         
        <c:if test="${repositoryService.externalAuthenticationEnabled }">
             <tr>
                 <td align="left" class="label">External Account Username:</td>
                 <td align="left" class="input"><input type="text" 
                     id="newUserForm_password_check" 
                     name="externalAccountUserName" 
                     value="${irUser.externalAccount.externalUserAccountName}" 
                     size="45"/></td>
             </tr>
             <tr>
      	         <td align="left" class="label">External Account Type:</td>
      	         <td align="left" class="input">

      	             <select id="newUserForm_department" name="departmentId" />
      	   		         <option value = "0"> N/A</option>
	      		         <c:forEach items="${externalAccountTypes}" var="externalAccountType">
	      			        <option value = "${externalAccountType.id}"> ${externalAccountType.name}</option>
	      		         </c:forEach>
      	             </select>
      	          </td>
      	     </tr>
         </c:if>
         
         <tr>
  			<td align="left" class="label"> E-mail:*</td>
  			<td align="left" class="input"><input type="text" 
              id="newUserForm_email" name="defaultEmail.email" value="${defaultEmail.email}" size="45"/></td>
         </tr>
         
         <tr>
             <td align="left" class="label">Phone Number:</td>
             <td align="left" class="input"> <input type="text" 
              id="newUserForm_phone_number" name="phoneNumber" value="${irUser.phoneNumber}" size="45"/>
             </td>
         </tr>
         
         <tr>
             <td align="left" class="label"> Affiliation:</td>
             <td align="left" class="input">

      	         <select id="newUserForm_affiliation" name="affiliationId" />
	      		     <c:forEach items="${affiliations}" var="affiliation">
	      			     <option value = "${affiliation.id}"> ${affiliation.name}</option>
	      		     </c:forEach>
      	         </select>
      	     </td>
      	 </tr>
      	 
      	 <tr>
      	     <td align="left" class="label">Department:</td>
      	     <td align="left" class="input">

      	         <select id="newUserForm_department" name="departmentId" />
      	   		     <option value = "0"> N/A</option>
	      		     <c:forEach items="${departments}" var="department">
	      			    <option value = "${department.id}"> ${department.name}</option>
	      		     </c:forEach>
      	         </select>
      	     </td>
      	 </tr>
      	 
      	 <tr>
             <td align="left" class="label"> Account Locked:</td>
             <td align="left" class="input"><input type="checkbox" 
              id="newUserForm_account_locked" name="accountLocked" value="true" 
	              <c:if test="${accountLocked == true}"> 
	              	checked
	              </c:if>
              /> 
              </td>
         </tr>
         
         <tr>
             <td align="left" class="label"> Account Expired:</td>
             <td align="left" class="input"><input type="checkbox" 
              id="newUserForm_account_expired" name="accountExpired" value="true" 
	               <c:if test="${accountExpired == true}"> 
	              	checked
	              </c:if>
              /></td>
         </tr>
         
         <tr>
             <td align="left" class="label"> Credentials Expired:</td>
             <td align="left" class="input"><input type="checkbox" 
              id="newUserForm_credentials_expired" name="credentialsExpired" value="true" 
	              <c:if test="${credentialsExpired == true}"> 
	              	checked
	              </c:if>
              
              /></td>
         </tr>
         
         <tr>
             <td align="left" class="label"> Roles</td>
         </tr>
         <tr>
             <td align="left" class="input">
                 <input
                      onclick="YAHOO.ur.user.autoCheckRoles(this);"
	                 <c:if test="${adminRole == true}"> 
                        checked="true" 
                     </c:if>
                     type="checkbox" id="newUserForm_isAdmin" name="adminRole" value="true"/>  Admin
             </td>
         </tr>
        
         <tr>
             <td align="left" class="input">
                 <input
                      onclick="YAHOO.ur.user.autoCheckRoles(this);"
                     <c:if test="${userRole == true}"> 
                        checked="true" 
                     </c:if>
                     type="checkbox"  class="input" name="userRole" value="true" id="newUserForm_isUser"/>  User
            </td>
        </tr>
        
        <tr>
            <td align="left" class="input">
                <input
                     onclick="YAHOO.ur.user.autoCheckRoles(this);"
                    <c:if test="${authorRole == true}"> 
                        checked="true" 
                     </c:if>
                    type="checkbox"  class="input" name="authorRole" value="true" id="newUserForm_isAuthor"/> Author
            </td>
        </tr>
        <tr>
            <td align="left" class="input">
                <input 
                     onclick="YAHOO.ur.user.autoCheckRoles(this);"
                    <c:if test="${researcherRole == true}"> 
                        checked="true" 
                     </c:if>
                    type="checkbox"  class="input" name="researcherRole" value="true" id="newUserForm_isResearcher"/> Researcher
            </td>
        </tr>
        <tr>
            <td align="left" class="input">
                
                <input
                     onclick="YAHOO.ur.user.autoCheckRoles(this);" 
                    <c:if test="${collectionAdminRole == true}"> 
                        checked="true" 
                     </c:if>
                    type="checkbox"  class="input" name="collectionAdminRole" value="true" id="newUserForm_isCollectionAdmin"/> Collection Administrator
            </td>
        </tr>
        <tr>
		<td class="label" colspan="2"> 
		 <input type="checkbox"	
		     onclick="YAHOO.ur.user.autoCheckRoles(this);"
		     <c:if test='${ir:checkUserHasRole(irUser, "ROLE_IMPORTER", "")}'> 
                checked="true" 
             </c:if>
              name="importerRole" value="true" id="newUserForm_isImporter"/> Importer
 		</td>
	    </tr>      
        <tr>
            <td align="left" class="input">
            Email user name and password to user
		        <input type="checkbox" 
		            <c:if test="${emailPassword == true}"> 
                        checked="true" 
                    </c:if>
		        id="newUserForm_send_Email" name="emailPassword" value="true">
		
		    </td>
		</tr>
</table>        