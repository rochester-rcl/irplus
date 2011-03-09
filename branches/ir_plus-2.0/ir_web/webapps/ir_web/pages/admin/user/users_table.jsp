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

<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

<fmt:setBundle basename="messages"/>

<c:if test="${totalHits > 0}">
	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
</c:if>

<c:import url="browse_all_users_pager.jsp"/>


<div class="dataTable">
	<form method="post" id="users" name="myUsers" >
	             
	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
	                <urstb:td>Id</urstb:td>
             
	                <c:set var="userNameSort" value="none"/>
	                <c:if test='${sortElement == "username"}'>
	                    <c:set var="userNameSort" value="${sortType}"/>
	                </c:if>
	                
	                <urstb:tdHeadSort  height="33"
	                    currentSortAction="${userNameSort}"
	                    ascendingSortAction="javascript:YAHOO.ur.user.getUsers(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'username', 'asc');"
	                    descendingSortAction="javascript:YAHOO.ur.user.getUsers(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'username', 'desc');">
	                    <u>User Name</u>                                              
	                    <urstb:thImgSort
	                                 sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
	                                 sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
	
	                <c:set var="lastNameSort" value="none"/>
	                <c:if test='${sortElement == "lastName"}'>
	                    <c:set var="lastNameSort" value="${sortType}"/>
	                </c:if>
	                
	                <urstb:tdHeadSort  height="33"
	                    currentSortAction="${lastNameSort}"
	                    ascendingSortAction="javascript:YAHOO.ur.user.getUsers(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'lastName', 'asc');"
	                    descendingSortAction="javascript:YAHOO.ur.user.getUsers(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'lastName', 'desc');">
	                    <u>Last Name</u>                                              
	                    <urstb:thImgSort
	                                 sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
	                                 sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
	
	                <urstb:td>First Name</urstb:td>
              
	                <c:set var="emailSort" value="none"/>
	                <c:if test='${sortElement == "email"}'>
	                    <c:set var="emailSort" value="${sortType}"/>
	                </c:if>
	                
	                <urstb:tdHeadSort  height="33"
	                    currentSortAction="${emailSort}"
	                    ascendingSortAction="javascript:YAHOO.ur.user.getUsers(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'email', 'asc');"
	                    descendingSortAction="javascript:YAHOO.ur.user.getUsers(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'email', 'desc');">
	                    <u>Email</u>                                              
	                    <urstb:thImgSort
	                                 sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
	                                 sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
	                <urstb:td>Login Date</urstb:td>
	                <urstb:td>Change password</urstb:td>
	                <urstb:td>Login as user</urstb:td>
	                <urstb:td>Delete</urstb:td>
	                </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="user" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${users}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        ${user.id}
	                        </urstb:td>
	                        <urstb:td>
		                   		<c:url var="viewEmailUrl" value="/admin/userEditView.action">
		                           <c:param name="id" value="${user.id}"/>
		                        </c:url>
			                   
			                   <a href="${viewEmailUrl}">${user.username}</a>
	                        </urstb:td>
	                        <urstb:td>
		                   		${user.lastName}
	                        </urstb:td>
	                        <urstb:td>
	                             ${user.firstName}
	                        </urstb:td>
	                        <urstb:td>
	                             ${user.defaultEmail.email}
	                        </urstb:td>	 
	                        <urstb:td>
	                             ${user.lastLoginDate}
	                        </urstb:td>	                           
	                        <urstb:td>
		                   		<a href="javascript:YAHOO.ur.user.changePassword('${user.id}');">Change password</a> 
	                        </urstb:td>
	                        <urstb:td>
	                        	<a href="javascript:YAHOO.ur.user.loginAsUser('${user.id}');">Login as user</a>
	                    	</urstb:td>
	                    	<urstb:td>
	                        	<a href="javascript:YAHOO.ur.user.deleteUser('${user.id}')">Delete</a>
	                    	</urstb:td>
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
		</form>
</div>	

<c:import url="browse_all_users_pager.jsp"/>


