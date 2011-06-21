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
<%@ taglib prefix="ur" uri="ur-tags"%>


<!-- Table to hold the selected files -->
<table class="itemSelectTable" width="100%">
	<thead>
		<tr>
			<th colspan="2" class="thItemSelect">Contributor</th>
		
			<th class="thItemSelect">Type</th>
			<th class="thItemSelect">Remove</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="itemContributor" items="${contributors}" varStatus="status">
			<tr>
				<td class="tdItemSelectLeftBorder">
					<table>
						<tr>
								<c:if test="${status.count == 1}">
								<td>
									<img class="tableImg" alt="" src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_up_mute.gif" />
								</td>
								</c:if>

								<c:if test="${status.count != 1}">
								<td>
									<a href="javascript:YAHOO.ur.item.contributor.moveUp(${itemContributor.id});"><img class="tableImg" alt="" 
									    src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_up.gif" /></a>
								</td>
								</c:if>
						</tr>
						<tr>
							<td> &nbsp;</td>
						</tr>		
						<tr>
								<c:if test="${status.count != contributorsCount}">
								<td>
									<a href="javascript:YAHOO.ur.item.contributor.moveDown(${itemContributor.id});"><img class="tableImg" alt="" 
									    src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_down.gif" /></a>
									</td>
								</c:if>  
								<c:if test="${status.count == contributorsCount}">
									<td>
										<img class="tableImg" alt="" src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_down_mute.gif" />
									</td>
								</c:if>  
						</tr>							
					</table>
				</td>
				
				<td class="tdItemSelect">
				        <ir:authorName personName="${itemContributor.contributor.personName}" displayDates="true"/>
				</td>

				<td class="tdItemSelect">
		      	   <select id="contributor_type" name="contributorTypeId_${itemContributor.id}" onChange="javascript:YAHOO.ur.item.contributor.addContributorType(this, '${itemContributor.id}');" />
			      		<c:forEach items="${contributorTypes}" var="contributorType">
			      			<option value = "${contributorType.id}"
			      			<c:if test="${itemContributor.contributor.contributorType.id == contributorType.id}">
			      				selected
			      			</c:if>  
			      			> ${contributorType.name}</option>
			      		</c:forEach>
		      	   </select>
				</td>
				
				<td width="100" class="tdItemSelectRightBorder">
				     <span class="deleteBtnImg">&nbsp;</span> <a href="javascript:YAHOO.ur.item.contributor.removeContributor('${itemContributor.id}');"> Remove </a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>