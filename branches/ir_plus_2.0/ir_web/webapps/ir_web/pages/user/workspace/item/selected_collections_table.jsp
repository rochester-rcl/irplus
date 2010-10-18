
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

<!-- Form that contains the item  contributors -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ir" uri="ir-tags"%>


<input type="hidden" id="selected_collections_form_collectionIds" name="selectedCollectionIds" value="${selectedCollectionIds}"/>

<!-- Table to hold the selected files -->
<table class="itemSelectTable" width="100%">
	<thead>
		<tr>

			<th class="thItemSelect" width="20%">Permissions</th>
			<th class="thItemSelect">Submit to these collections</th>
			<th class="thItemSelect" width="20%">Remove</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="collectionPermission" items="${selectedCollectionsPermission}" varStatus="status">
			<tr >
				<td class="tdItemSelectLeftBorder" >
					<c:if test="${collectionPermission.permission == 'DIRECT_SUBMIT'}">
		            	 <span class="addBtnImg">&nbsp;</span>Direct Submission
	                </c:if>
					<c:if test="${collectionPermission.permission == 'REVIEW_SUBMIT'}">
		                 <span class="groupAddBtnImg">&nbsp;</span>Reviewed Submission
	                </c:if>
				</td>
				
				<td class="tdItemSelect">
                 	${collectionPermission.institutionalCollection.name}
				</td>

				<td class="tdItemSelectRightBorder">
				     <span class="deleteBtnImg">&nbsp;</span>
				     <a href="javascript:YAHOO.ur.item.collection.removeCollectionFromPublication('${collectionPermission.institutionalCollection.id}');"> Remove </a>
				</td>
				
			</tr>
		</c:forEach>
	</tbody>
</table>