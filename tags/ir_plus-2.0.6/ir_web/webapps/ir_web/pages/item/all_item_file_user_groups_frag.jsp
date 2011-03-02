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

<div class="dataTable">
    <urstb:table width="95%">
        <urstb:caption>All User Groups</urstb:caption>
            <urstb:thead>
                <urstb:tr>
                    <urstb:td>Action</urstb:td>
                    <urstb:td>Name</urstb:td>
                    <urstb:td>Description</urstb:td>
                </urstb:tr>
            </urstb:thead>
            <urstb:tbody
                var="userGroup" 
                oddRowClass="odd"
                evenRowClass="even"
                currentRowClassVar="rowClass"
                collection="${userGroups}">
                
                <urstb:tr 
                    cssClass="${rowClass}"
                    onMouseOver="this.className='highlight'"
                    onMouseOut="this.className='${rowClass}'">
                        <c:if test="${!ir:groupHasAceForItemFile(acl, itemFile, userGroup)}">
                            <urstb:td><a href="javascript:YAHOO.ur.group.item.addGroupsToItemFile(${userGroup.id}, ${itemFile.id}, ${item.id});">Add</a></urstb:td>
                        </c:if>
                        <c:if test="${ir:groupHasAceForItemFile(acl, itemFile, userGroup)}">
                            <urstb:td>Added</urstb:td>
                        </c:if>
                        <urstb:td>${userGroup.name}</urstb:td>
                        <urstb:td>${userGroup.description}</urstb:td>
                </urstb:tr>
            </urstb:tbody>
        </urstb:table>
</div>