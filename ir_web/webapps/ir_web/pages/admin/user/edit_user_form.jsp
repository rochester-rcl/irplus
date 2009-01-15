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

<!--  form fragment for dealing with editing user
      this form will return with error messages in it if there
      is an issue.
 -->
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<table class="formTable">

		<!--  represents a successful submission -->
		 <input type="hidden" id="editUserForm_success" value="${added}"/>
		
          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
                   key="userAlreadyExists"/></p>
                    
          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
                   key="emailExistError"/></p>
           
          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
                   key="rolesNotSelectedError"/></p>
          
          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
                   key="ldapNameExists"/></p>
          
	<tr>
		 <td align="left" class="label"> First Name:</td>
         <td align="left" class="input"><input type="text" 
              id="editUserForm_first_name" name="irUser.firstName" value="${irUser.firstName}" size="45"/> </td> 
	</tr>              

	<tr>
		 <td align="left" class="label"> Last Name:</td>
         <td align="left" class="input"> <input type="text"  
   	              id="editUserForm_last_name" name="irUser.lastName" value="${irUser.lastName}" size="45"/> </td> 
	</tr>              

	<tr>
		 <td align="left" class="label"> User Name:*</td>
         <td align="left" class="input"><input type="text"  
              id="editUserForm_name" name="irUser.username" value="${irUser.username}" size="45"/> </td> 
	</tr>  
	
	<tr>
              <td align="left" class="label">Net Id:</td>
              <td align="left" class="input"><input type="text" 
              id="editUserForm_password_check" name="irUser.ldapUserName" value="${irUser.ldapUserName}" size="45"/></td>
    </tr>            

	<tr>
		 <td align="left" class="label">Phone Number:</td>
         <td align="left" class="input"> <input type="text"
              id="editUserForm_phone_number" name="phoneNumber" value="${irUser.phoneNumber}" size="45"/> </td> 
	</tr>              

	<tr>
		 <td align="left" class="label">Affiliation: 
           <input type="hidden" id="editUserForm_affiliation_name" value="${irUser.affiliation.name}"/>  </td>
		<td align="left" class="input"> 
      	   <select id="editUserForm_affiliation" name="affiliationId" />
	      		<c:forEach items="${affiliations}" var="affiliation">
	      			<option value = "${affiliation.id}"
	      			<c:if test="${affiliation.id == irUser.affiliation.id}">
	      				selected
	      			</c:if>
	      			> ${affiliation.name}</option>
	      		</c:forEach>
      	   </select>
		</td> 
	</tr>              

	<tr>
		 <td align="left" class="label">   Department:
		   <input type="hidden" id="editUserForm_department_name" value="${irUser.department.name}"/>
		 </td>
         <td align="left" class="input"> 
      	   <select id="editUserForm_department" name="departmentId" />
      	   		<option value = "0"> N/A</option>
	      		<c:forEach items="${departments}" var="department">
	      			<option value = "${department.id}"
	      			<c:if test="${department.id == irUser.department.id}">
	      				selected
	      			</c:if>
	      			> ${department.name}</option>
	      		</c:forEach>
      	   </select>
      	   </td> 
	</tr>              

	<tr>
		 <td align="left" class="label"> Account Locked: </td>
         <td align="left" class="input"> <input type="checkbox" 
              id="editUserForm_account_locked" name="accountLocked" value="true" 
	              <c:if test="${irUser.accountLocked == true}"> 
	              	checked
	              </c:if>
              
          /> </td> 
	</tr>              

	<tr>
		 <td class="label"> Account Expired: </td>
         <td align="left" class="input"> <input type="checkbox" 
              id="editUserForm_account_expired" name="accountExpired" value="true" 
	               <c:if test="${irUser.accountExpired == true}"> 
	              	checked
	              </c:if>
         /> </td> 
	</tr>              

	<tr>
		 <td align="left" class="label"> Credentials Expired:</td>
         <td align="left" class="input"> <input type="checkbox" 
              id="editUserForm_credentials_expired" name="credentialsExpired" value="true" 
	              <c:if test="${irUser.credentialsExpired == true}"> 
	              	checked
	              </c:if>
              
          /></td> 
	</tr>              

	<tr>
		 <td align="left" class="label" colspan="2"> <strong> Roles: </strong> </td>
	</tr>
	
	<tr>
		 <td class="label" colspan="2"> 		  
 		     <input type="checkbox"
 		            onclick="YAHOO.ur.email.autoCheckRoles(this);"
 		     <c:if test='${ir:checkUserHasRole(irUser, "ROLE_ADMIN", "")}'> 
                 checked="true" 
             </c:if>
             id="editUserForm_isAdmin" name="adminRole" value="true"/>  Admin
		</td>
	</tr>

	<tr>
		 <td class="label" colspan="2"> 	
             <input type="checkbox"
                 onclick="YAHOO.ur.email.autoCheckRoles(this);"
             <c:if test='${ir:checkUserHasRole(irUser, "ROLE_USER", "")}'> 
              checked="true" 
             </c:if>
               name="userRole" value="true" id="editUserForm_isUser"/>  User
		</td>
	</tr>

	<tr>
		<td class="label" colspan="2"> 	
            <input type="checkbox"
                onclick="YAHOO.ur.email.autoCheckRoles(this);"
            <c:if test='${ir:checkUserHasRole(irUser, "ROLE_AUTHOR", "")}'> 
                checked="true" 
            </c:if>  
            name="authorRole" value="true" id="editUserForm_isAuthor"/> Author
		</td>
	</tr>        

	<tr>
		<td class="label" colspan="2"> 	
            <input type="checkbox"
                onclick="YAHOO.ur.email.autoCheckRoles(this);"
            <c:if test='${ir:checkUserHasRole(irUser, "ROLE_RESEARCHER", "")}'> 
                 checked="true"   
            </c:if>
            name="researcherRole" value="true" id="editUserForm_isResearcher"/> Researcher
 		</td>
	</tr>
	
	<tr>
		<td class="label" colspan="2"> 
		 <input type="checkbox"	
		     onclick="YAHOO.ur.email.autoCheckRoles(this);"
		     <c:if test='${ir:checkUserHasRole(irUser, "ROLE_COLLECTION_ADMIN", "")}'> 
                checked="true" 
             </c:if>
              name="collectionAdminRole" value="true" id="editUserForm_isCollectionAdmin"/>Collection Administrator
 		</td>
	</tr>  
        
</table>	