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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="messages"/>

   <br/>

   <table width="100%">
   <tr>
       <td align="left" width="75%">
           <strong>Path:&nbsp;/
	          <span class="groupImg">&nbsp;</span>Group Workspaces /   
           </strong>
       </td>      
   </tr>
   </table>
   
   
   
  <div align="right">
      <c:if test='${ir:userHasRole("ROLE_AUTHOR", "OR")}'>
	           <button class="ur_button" 
 		               onmouseover="this.className='ur_buttonover';"
 		               onmouseout="this.className='ur_button';"
 		               onClick="javascript:YAHOO.ur.user.group_workspace.newGroupWorkspaceDialog.showDialog()"><span class="addFolderBtnImg">&nbsp;</span>New Group Workspace</button> 
      </c:if>
  </div>
  <br/>
 
               
  <div class="dataTable">
       
	<form method="post" id="group_workspaces" name="group_workspaces" action="">
       
        <c:url var="downArrow" value="/page-resources/images/all-images/bullet_arrow_down.gif" />
        <urstb:table width="100%">
            <urstb:thead>
                <urstb:tr>
                    <urstb:td>Type</urstb:td>
                    <urstb:td>Name</urstb:td>
                    <urstb:td>Description</urstb:td>
                    <urstb:td>Action</urstb:td>
                </urstb:tr>
            </urstb:thead>
            <urstb:tbody
                var="groupWorkspace" 
                oddRowClass="odd"
                evenRowClass="even"
                currentRowClassVar="rowClass"
                collection="${groupWorkspaces}">
                    <urstb:tr 
                        cssClass="${rowClass}"
                        onMouseOver="this.className='highlight'"
                        onMouseOut="this.className='${rowClass}'">
                        <urstb:td width="60px">
                            <!-- this deals with folder information
	                        folders get an id of the folder_checkbox_{id} 
	                        where id  is the id of the folder 
	                        clicking on a link creates a popup menu-->
	                        <div id="group_workspace_${groupWorkspace.id}">
	                             <button type="button"  class="table_button" 
	                                onmouseover="this.className='table_buttonover';"
 		                            onmouseout="this.className='table_button';"><span class="groupImg"></span><img src="${downArrow}"/></button>
	                        </div>
                        </urstb:td>
                        <urstb:td>
                           <a href="javascript:YAHOO.ur.user.group_workspace.getGroupWorkspaceById(${groupWorkspace.id});">${groupWorkspace.name}</a>
                        </urstb:td>
                        <urstb:td>
                            ${groupWorkspace.description}
                        </urstb:td>
                        <urstb:td>
                             <c:if test="${ir:isGroupWorkspaceOwner(groupWorkspace)}">
                                 <c:url value="/user/getGroupWorkspace.action" var="editGroupWorkspaceUrl">
						            <c:param name="id" value="${groupWorkspace.id}"/>
						         </c:url>
						         <a href="${editGroupWorkspaceUrl}">Edit</a>/<a href="javascript:YAHOO.ur.user.group_workspace.deleteGroupWorkspace(${groupWorkspace.id});">Delete</a>
                             </c:if>
                        </urstb:td>
                    </urstb:tr>
            </urstb:tbody>
        </urstb:table>
    </form>
</div>