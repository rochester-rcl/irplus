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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

<div class="dataTable">

	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
	                <urstb:td>Id</urstb:td>
					<urstb:td>Content Type</urstb:td>
					<urstb:td>Action</urstb:td>
	            </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="mapping" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${marcContentTypeFieldMappings}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        ${mapping.id}
	                        </urstb:td>
	                        <urstb:td>
			                   ${mapping.contentType.name}
	                        </urstb:td>
	                        <urstb:td>
	                           
	                           <c:url value="/admin/editContentTypeMarcMapping.action" var="editUrl">
	                               <c:param name="id" value="${mapping.id}"/>
	                           </c:url>
	                           <a href="${editUrl}">Edit</a>/<a href="javascript:YAHOO.ur.marc.contentTypeMapping.deleteMapping(${mapping.id});">Delete</a> 
	                        </urstb:td>
	                       
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
  
</div>	



