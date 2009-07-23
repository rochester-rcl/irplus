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
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table class="greyBorderTable" >
    <c:forEach items="${itemObjects}" var="object">
	<tr>
	    <td width="10" class="noBorderTabletd"></td>
		<td class="bottomBorder" align="left">
										   
		<c:if test="${object.type == 'FILE'}">
		    <ir:fileTypeImg cssClass="tableImg" irFile="${object.irFile}"/>
			
		    <!--  the is Preview flag indicates that the file is being previewed by a user this should be passed in
		          by the importing file -->		
			<c:if test='${!param.isPreview && !param.isResearcherView}'>    	
			    <c:if test="${object.public || institutionalItem.owner == user || ir:hasPermission('ITEM_FILE_READ',object) }">
			        <c:url var="itemFileDownload" value="/fileDownloadForInstitutionalItem.action">
				        <c:param value="${institutionalItemVersion.item.id}" name="itemId"/>
					    <c:param value="${object.id}" name="itemFileId"/>
				    </c:url>
				    <a href="${itemFileDownload}">
	                 ${object.irFile.nameWithExtension}</a> &nbsp; <ir:fileSizeDisplay sizeInBytes="${object.irFile.fileInfo.size}"/> (No. of downloads : ${ir:fileDownloadCount(object.irFile)})
		        </c:if>
		        <c:if test="${!object.public && institutionalItem.owner != user && !ir:hasPermission('ITEM_FILE_READ',object) }">
		            ${object.irFile.nameWithExtension}&nbsp;(Private - try <a href="<c:url value="/user/workspace.action"/>">Logging In</a> if not already) 
		        </c:if>
		    </c:if>
		    <c:if test='${param.isPreview && !param.isResearcherView}'>
		         ${object.irFile.nameWithExtension}</a> &nbsp; <ir:fileSizeDisplay sizeInBytes="${object.irFile.fileInfo.size}"/>
		    </c:if>
		    <c:if test='${param.isResearcherView}'>
					<c:url var="itemFileDownload" value="/downloadResearcherPublicationFile.action">
					    <c:param value="${researcherPublicationId}" name="publicationId"/>
						<c:param value="${object.id}" name="itemFileId"/>
					</c:url>
					<a href="${itemFileDownload}"> ${object.irFile.nameWithExtension}</a> &nbsp; <ir:fileSizeDisplay sizeInBytes="${object.irFile.fileInfo.size}"/> (No. of downloads : ${ir:fileDownloadCount(object.irFile)})
		    </c:if>
												   
	    </c:if>
											    
		<c:if test="${object.type == 'URL'}">
		    <img  alt="" class="tableImg" src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/>
	    	<ur:maxText numChars="40" text="${object.name}"></ur:maxText>
		</c:if>
											
		</td>
		
		<td class="bottomBorder" align="left">
		    ${object.description}
		</td>
	</tr>
	</c:forEach>
</table>