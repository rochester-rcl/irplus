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

<fmt:setBundle basename="messages"/>

<c:if test="${totalHits > 0}">
	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
	<c:import url="browse_all_sub_type_extension_pager.jsp"/>
</c:if>



<div class="dataTable">
	<ur:basicForm method="post" id="subTypeExtensions" name="mySubTypeExtensions" >
	    <input type="hidden" name="subTypeId" value="${subTypeId}"/>         
	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
					<urstb:td><ur:checkbox name="checkAllSetting" 
								value="off" onClick="YAHOO.ur.subTypeExtension.setCheckboxes();"/></urstb:td>         
	                <urstb:td>Id</urstb:td>
             
	                <urstb:tdHeadSort  height="33"
	                    currentSortAction="${sortType}"
	                    ascendingSortAction="javascript:YAHOO.ur.subTypeExtension.getSubTypeExtensions(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'asc');"
	                    descendingSortAction="javascript:YAHOO.ur.subTypeExtension.getSubTypeExtensions(${rowStart}, ${startPageNumber}, ${currentPageNumber}, 'desc');">
	                    <u>Name</u>                                              
	                    <urstb:thImgSort
	                                 sortAscendingImage="page-resources/images/all-images/bullet_arrow_down.gif"
	                                 sortDescendingImage="page-resources/images/all-images/bullet_arrow_up.gif"/></urstb:tdHeadSort>
					<urstb:td>Description</urstb:td>
	                <urstb:td>Edit</urstb:td>
	            </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="subTypeExtension" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${subTypeExtensions}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        <ur:checkbox name="subTypeExtensionIds" value="${subTypeExtension.id}"/>
	                        </urstb:td>
	                        <urstb:td>
		                        ${subTypeExtension.id}
	                        </urstb:td>
	                        <urstb:td>
			                   ${subTypeExtension.name}
	                        </urstb:td>
	                        <urstb:td>
	                             ${subTypeExtension.description}
	                        </urstb:td>
	                        <urstb:td>
			                     <a href="javascript:YAHOO.ur.subTypeExtension.editSubTypeExtension('${subTypeExtension.id}');">Edit</a>
	                        </urstb:td>

	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
		</ur:basicForm>
</div>	

<c:if test="${totalHits > 0}">
	<c:import url="browse_all_sub_type_extension_pager.jsp"/>
</c:if>




