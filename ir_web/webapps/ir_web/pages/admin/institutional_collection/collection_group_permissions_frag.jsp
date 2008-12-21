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
        <urstb:caption>Groups With Permissions</urstb:caption>
            <urstb:thead>
                <urstb:tr>
                    <urstb:td>Actions</urstb:td>
                    <urstb:td>Group Name</urstb:td>
                    <urstb:td>Administration</urstb:td>
                    <urstb:td>Reviewer</urstb:td>
                    <urstb:td>Direct Submit</urstb:td>
                    <urstb:td>Reviewed Submit</urstb:td>
                    <urstb:td>View</urstb:td>                    
                </urstb:tr>
            </urstb:thead>
            <urstb:tbody
                var="entry" 
                oddRowClass="odd"
                evenRowClass="even"
                currentRowClassVar="rowClass"
                collection="${entries}">
                
                <urstb:tr 
                    cssClass="${rowClass}"
                    onMouseOver="this.className='highlight'"
                    onMouseOut="this.className='${rowClass}'">
                        <urstb:td><a href="javascript:YAHOO.ur.edit.institution.collection.showRemoveGroupDialog(${entry.userGroup.id});">Remove</a> &nbsp;/&nbsp;<a href="javascript:YAHOO.ur.group.collection.editPermissionsForGroup(${entry.userGroup.id}, ${collection.id});">Edit</a></urstb:td>
                        <urstb:td>${entry.userGroup.name}</urstb:td>
                        <urstb:td><input type="checkbox" disabled="disabled" 
                            <c:if test='${ir:entryHasPermission(entry, "ADMINISTRATION")}'>checked="checked"</c:if> /></urstb:td>
                        <urstb:td><input type="checkbox" disabled="disabled" 
                            <c:if test='${ir:entryHasPermission(entry, "REVIEWER")}'>checked="checked"</c:if> /></urstb:td>
                        <urstb:td><input type="checkbox" disabled="disabled" 
                            <c:if test='${ir:entryHasPermission(entry, "DIRECT_SUBMIT")}'>checked="checked"</c:if>/></urstb:td>
                        <urstb:td><input type="checkbox" disabled="disabled" 
                            <c:if test='${ir:entryHasPermission(entry, "REVIEW_SUBMIT")}'>checked="checked"</c:if>/></urstb:td>
                        <urstb:td><input type="checkbox" disabled="disabled" 
                            <c:if test='${ir:entryHasPermission(entry, "VIEW")}'>checked="checked"</c:if>/></urstb:td>
                </urstb:tr>
            </urstb:tbody>
        </urstb:table>
</div>