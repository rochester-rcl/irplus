<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

<!--  
   Copyright 2008-2011 University of Rochester

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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>


<div class="dataTable">
    <urstb:table width="100%">
        <urstb:thead>
            <urstb:tr>
                <urstb:td>User</urstb:td>
                <urstb:td>Owner</urstb:td>
                <urstb:td>Edit</urstb:td>
                <urstb:td>Add File</urstb:td>
                <urstb:td>Read</urstb:td>
                <urstb:td>Action</urstb:td>
            </urstb:tr>
        </urstb:thead>
        <urstb:tbody
            var="entry" 
            oddRowClass="odd"
            evenRowClass="even"
            currentRowClassVar="rowClass"
            collection="${groupWorkspaceAcl.userEntries}">
            <urstb:tr 
                cssClass="${rowClass}"
                onMouseOver="this.className='highlight'"
                onMouseOut="this.className='${rowClass}'">
                            
                <urstb:td>
                    <c:if test="${entry.sid.researcher.primaryPicture != null }">
                        <c:url var="url" value="/researcherThumbnailDownloader.action">
                            <c:param name="irFileId" value="${entry.sid.researcher.primaryPicture.id}"/>
                            <c:param name="researcherId" value="${entry.sid.researcher.id}"/>
                        </c:url>
                        <img src="${url}"/>
                                   
                    </c:if>
                                
                    <c:if test="${entry.sid.researcher.primaryPicture == null}">
                        <img class="noimage_size" src="${pageContext.request.contextPath}/page-resources/images/all-images/noimage.jpg" />
                    </c:if>	
                    <br/> 
                    <c:url var="editUserPermissions" value="/user/editUserGroupWorkspacePermissions.action">
                        <c:param name="editUserPermissionsId" value="${entry.sid.id}"/>
                        <c:param name="groupWorkspaceId" value="${groupWorkspace.id}"/>
                    </c:url>
                    <a href="${editUserPermissions}">${entry.sid.firstName}&nbsp;${entry.sid.lastName}</a>
                </urstb:td>

                <urstb:td>
                    <c:if test="${ir:isUserGroupWorkspaceOwner(groupWorkspace, entry.sid)}">
                    Yes
                    </c:if>
                    <c:if test="${!ir:isUserGroupWorkspaceOwner(groupWorkspace, entry.sid)}">
                    No
                    </c:if>
                </urstb:td>
                            
                <urstb:td>
                    <c:if test='${ir:entryHasPermission(entry, "GROUP_WORKSPACE_EDIT")}'>Yes</c:if>
                    <c:if test='${ !ir:entryHasPermission(entry, "GROUP_WORKSPACE_EDIT")}'>No</c:if>
                </urstb:td>

               <urstb:td>
                   <c:if test='${ir:entryHasPermission(entry, "GROUP_WORKSPACE_ADD_FILE")}'>Yes</c:if>
                   <c:if test='${ !ir:entryHasPermission(entry, "GROUP_WORKSPACE_ADD_FILE")}'>No</c:if>
               </urstb:td>

               <urstb:td>
                   <c:if test='${ir:entryHasPermission(entry, "GROUP_WORKSPACE_READ")}'>Yes</c:if>
                   <c:if test='${!ir:entryHasPermission(entry, "GROUP_WORKSPACE_READ")}'>No</c:if>
               </urstb:td>
               
               <urstb:td>
                       <c:url var="removeUserUrl" value="/user/removeGroupWorkspaceUser.action">
                            <c:param name="removeUserId" value="${entry.sid.id}"/>
                            <c:param name="groupWorkspaceId" value="${groupWorkspace.id}"/>
                       </c:url>
                       <a href="${removeUserUrl}">Remove</a> 
               </urstb:td>         
                        
           </urstb:tr>
       </urstb:tbody>
   </urstb:table>
</div>    