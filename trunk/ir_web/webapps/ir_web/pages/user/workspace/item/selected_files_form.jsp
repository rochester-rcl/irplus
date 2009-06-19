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

<!-- Form to select files to add to the item -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<!-- Form to store the selected file ids and item id the user is working on -->
<form name="myFiles" method="post">
	<input type="hidden" id="myFiles_itemId" name="genericItemId" value="${item.id}"/>
    <input type="hidden" id="myFiles_parentCollectionId" 
	                                   name="parentCollectionId" 
	                                   value="${parentCollectionId}"/>
    <input type="hidden" id="myFiles_institutionalItemId" 
	                                   name="institutionalItemId" 
	                                   value="${institutionalItemId}"/>


<!-- Table to hold the selected files -->
<table class="itemSelectTable" width="100%" >
	<thead>
		<tr>
			<th colspan="2" class="thItemSelect">Files Added to Publication</th>
			<th class="thItemSelect" width="15%">Remove</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="itemFileVersion" items="${itemFileVersions}" varStatus="status">
			<tr>
				<td class="tdItemFileLeftBorder" width="5%">
					<table>
						<tr>
								<c:if test="${status.count == 1}">
								<td>
									<img class="tableImg" alt="" src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_up_mute.gif" />
								</td>
								</c:if>
								<c:if test="${status.count != 1}">
								<td>
									<a href="javascript:YAHOO.ur.item.moveUp(${itemFileVersion.itemObject.id}, '${itemFileVersion.itemObject.type}');"><img class="tableImg" alt="" 
									    src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_up.gif" /></a>
								</td>
								</c:if>
						</tr>
						<tr>
							<td> &nbsp;</td>
						</tr>		
						<tr>
								<c:if test="${status.count != numberOfItemObjects}">
								<td>
									<a href="javascript:YAHOO.ur.item.moveDown(${itemFileVersion.itemObject.id}, '${itemFileVersion.itemObject.type}');"><img class="tableImg" alt="" 
									    src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_down.gif" /></a>
									</td>
								</c:if>  
								<c:if test="${status.count == numberOfItemObjects}">
									<td>
									<img class="tableImg" alt="" src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_down_mute.gif" />
									</td>
								</c:if>  
						</tr>							
					</table>
				</td>
				
				<td class="tdItemFileRightBorder" width="75%">
					 <c:if test="${itemFileVersion.itemObject.type == 'FILE'}">
                     	<ir:fileTypeImg cssClass="tableImg" irFile="${itemFileVersion.itemObject.irFile}"/> <ur:maxText numChars="35" text="${itemFileVersion.itemObject.irFile.name}"></ur:maxText>
				      	   <select id="file_version" name="version_${itemFileVersion.itemObject.id}" onChange="javascript:YAHOO.ur.item.changeFileVersion(this, '${itemFileVersion.itemObject.id}');" /> 
				      	   
					      		<c:forEach var="version" items="${itemFileVersion.versions}" >
					      			<option value = "${version.id}"
					      			<c:if test="${itemFileVersion.itemObject.irFile.id == version.irFile.id}">
					      				selected
					      			</c:if>  
					      			> ${version.versionNumber}</option>
					      		</c:forEach>
				      	   </select>                     	
                     </c:if>
					 <c:if test="${itemFileVersion.itemObject.type == 'URL'}">
                     	<img  alt="" class="buttonImg" src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/>  <ur:maxText numChars="40" text="${itemFileVersion.itemObject.name}"></ur:maxText>
                     	&nbsp; <a href="javascript:YAHOO.ur.item.editLink('${itemFileVersion.itemObject.id}', '${itemFileVersion.itemObject.name}', '${itemFileVersion.itemObject.description}', '${itemFileVersion.itemObject.urlValue}')"> <img  alt="" class="buttonImg" src="${pageContext.request.contextPath}/page-resources/images/all-images/link_edit.gif"/> </a>
                     </c:if>

				</td>

				
				
				<td class="tdItemFileRightBorder" width="15%">
				     <span class="deleteBtnImg">&nbsp;</span><a href="javascript:YAHOO.ur.item.removeFile('${itemFileVersion.itemObject.id}', '${itemFileVersion.itemObject.type}');"> Remove </a>
				</td>
			</tr>
			
			<tr>
				<td class="tdItemFile" colspan="2">
				 <c:if test="${itemFileVersion.itemObject.type == 'FILE'}">
                     <textarea name="description_${itemFileVersion.itemObject.id}" rows="2" cols="50" onChange="javascript:YAHOO.ur.item.updateDescription(this, '${itemFileVersion.itemObject.id}', '${itemFileVersion.itemObject.type}');">${itemFileVersion.itemObject.description}</textarea>
				 </c:if>     
				</td>			
				<td class="tdItemFile">
             
				</td>			
			</tr>
			
		</c:forEach>
	</tbody>
</table>

</form>