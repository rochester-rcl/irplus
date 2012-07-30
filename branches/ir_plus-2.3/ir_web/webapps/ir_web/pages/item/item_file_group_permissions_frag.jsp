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

<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:forEach 	items="${item.itemFiles}" var="itemFile">
	<table width="95%">
	<tr>
	<td>
		User Groups For File : ${itemFile.irFile.nameWithExtension} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 
		<c:url var="viewItemFileGroupsUrl" value="/admin/viewGroupsForItemFile.action">
			                         <c:param name="itemId" value="${item.id}"/>
			                         <c:param name="institutionalItemId" value="${institutionalItemId}"/>
			                         <c:param name="itemFileId" value="${itemFile.id}"/>
		</c:url>
		
		<a href="${viewItemFileGroupsUrl}">Add new user group</a>	
	</td>
	<td align="right">
		Is the file viewable by all users:
		<input type="radio" name="isPublic_${itemFile.id}" value="true" onClick="javascript:YAHOO.ur.institution.item.permission.updateFilePublicView(${itemFile.id}, ${institutionalItemId}, 'true')"
		<c:if test="${itemFile.isPublic}">
			checked
		</c:if>
		> Yes  <c:if test="${item.embargoed}"><span class="errorMessage">(After Embargo)&nbsp;</span></c:if>
		<input type="radio" name="isPublic_${itemFile.id}"  value="false" onClick="javascript:YAHOO.ur.institution.item.permission.updateFilePublicView(${itemFile.id}, ${institutionalItemId}, 'false')"
		<c:if test="${!itemFile.isPublic}">
			checked
		</c:if>
		> No  
	</td>
	</tr>
	</table>
	<div class="clear">&nbsp;</div>
	<ir:fileGroupEntries itemFile="${itemFile}">
		<div class="dataTable">
		    <urstb:table width="95%">
		            <urstb:thead>
		                <urstb:tr>
		                    <urstb:td>Actions</urstb:td>
		                    <urstb:td>Group Name</urstb:td>
		                    <urstb:td>Item file read</urstb:td>
		                </urstb:tr>
		            </urstb:thead>
		            <urstb:tbody
		                var="entry" 
		                oddRowClass="odd"
		                evenRowClass="even"
		                currentRowClassVar="rowClass"
		                collection="${fileEntries}">
		                
		                <urstb:tr 
		                    cssClass="${rowClass}"
		                    onMouseOver="this.className='highlight'"
		                    onMouseOut="this.className='${rowClass}'">
		                        <urstb:td><a href="javascript:YAHOO.ur.institution.item.permission.showRemoveItemFileGroupDialog(${entry.userGroup.id}, ${itemFile.id}, ${institutionalItemId});">Remove</a> </urstb:td>
		                        <urstb:td>${entry.userGroup.name}</urstb:td>
		                        <urstb:td><input type="checkbox" disabled="disabled" checked="checked"></urstb:td>
		                        
		                </urstb:tr>
		            </urstb:tbody>
		        </urstb:table>
		</div>
	</ir:fileGroupEntries>
	<br/>
</c:forEach>	