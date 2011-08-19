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
                <urstb:td> Name </urstb:td>
                <urstb:td> Description </urstb:td>
                <urstb:td> Action </urstb:td>
            </urstb:tr>
        </urstb:thead>
        <urstb:tbody
            var="group" 
            oddRowClass="odd"
            evenRowClass="even"
            currentRowClassVar="rowClass"
            collection="${groupWorkspace.groups}">
            <urstb:tr 
                cssClass="${rowClass}"
                onMouseOver="this.className='highlight'"
                onMouseOut="this.className='${rowClass}'">
                <urstb:td>
                    ${group.name}
                </urstb:td>
                        
                <urstb:td>
                    ${group.description}
                </urstb:td>

                <urstb:td>
                    <c:url var="viewWorkspaceGroup" value="/user/getWorkspaceGroup.action">
                        <c:param name="id" value="${group.id}"/>
                    </c:url>
                    <a href="${viewWorkspaceGroup}">Edit</a>&nbsp;/&nbsp;<a href="javascript:YAHOO.ur.user.workspace_group.deleteWorkspaceGroup(${group.id})">Delete</a>
                </urstb:td>                        

            </urstb:tr>
        </urstb:tbody>
    </urstb:table>
</div>