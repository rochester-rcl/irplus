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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
 
    <c:url var="cancelUrl" value="/user/viewResearcher.action">
        <c:param name="showFoldersTab" value="true"/>
        <c:param name="parentFolderId" value="${parentFolderId}"/>
        <c:param name="researcherId" value="${researcherId}"/>
    </c:url>
    
    <button class="ur_button"
			onmouseover="this.className='ur_buttonover';"
			onmouseout="this.className='ur_button';"
			onclick="location.href='${cancelUrl}';">Cancel</button>

    <table>
        <tr>
		    <td width="400px" align="left" valign="top"></td>
		    <td width="100px"></td>
		    <td width="400px" align="left" valign="top">
		
		
		        <div id="destination_path">
		            Move To Location: /<ur:a href="javascript:YAHOO.ur.researcher.filesystem.move.getMoveFolder('0');">My Files</ur:a>/
		            <c:forEach var="folder" items="${destinationPath}">
			            <ur:a href="javascript:YAHOO.ur.researcher.filesystem.move.getMoveFolder('${folder.id}')">${folder.name}</ur:a>/
                    </c:forEach>
                </div>
            
                <br/>
                <br/>                                 
            
            </td>
	    </tr>
	    <tr>
		    <td width="400px" align="left" valign="top">
		        <table class="simpleTable" width="400px">
			        <thead>
				        <tr>
					        <th>Researcher Objects To Move</th>
				        </tr>
			        </thead>
			        <tbody>
				        <c:forEach items="${foldersToMove}" var="folder">
					        <tr>
						        <td><span class="folderBtnImg">&nbsp;</span>${folder.name}</td>
					        </tr>
				        </c:forEach>

				        <c:forEach items="${filesToMove}" var="file">
					        <tr>
						        <td><ir:fileTypeImg cssClass="tableImg" irFile="${file.irFile}"/>${file.name}</td>
					        </tr>
				        </c:forEach>
				        <c:forEach items="${itemsToMove}" var="item">
					        <tr>
						        <td><span class="packageBtnImg">&nbsp;</span>${item.name}</td>
					        </tr>
				        </c:forEach>
				        <c:forEach items="${publicationsToMove}" var="publication">
					        <tr>
						        <td><span class="scriptImg">&nbsp;</span>${publication.name}</td>
					        </tr>
				        </c:forEach>
				        <c:forEach items="${linksToMove}" var="link">
					        <tr>
						        <td><img  alt="" 
			                       src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/>${link.name}</td>
					        </tr>
				        </c:forEach>
			        </tbody>
		        </table>
		    </td>
		    <td width="100px" valign="top" align="center">
		
		        <button class="ur_button" id="move_button"
			            onclick="javascript:YAHOO.ur.researcher.filesystem.move.moveFolder();"
			            onmouseover="this.className='ur_buttonover';"
			            onmouseout="this.className='ur_button';"
			            >Move<span class="pageWhiteGoBtnImg">&nbsp;</span>
			    </button>
		   </td>
		   <td width="400px" align="left" valign="top">
		       <form name="viewChildContentsForMove"
	              id="move_folder_form">
	              <input type="hidden" id="destination_id" name="destinationId" value="${destinationId}" />
	              <input type="hidden" name="parentFolderId" value="${parentFolderId}" /> 
	              <input type="hidden" id="researcher_id"  name="researcherId" value="${researcherId}" /> 
	
	               <c:forEach items="${foldersToMove}" var="folder">
	                   <input type="hidden" value="${folder.id}" name="folderIds" />
                   </c:forEach> 
 
                   <c:forEach items="${filesToMove}" var="file">
	                   <input type="hidden" value="${file.id}" name="fileIds" />
                   </c:forEach>
                   
                   <c:forEach items="${itemsToMove}" var="item">
	                   <input type="hidden" value="${item.id}" name="itemIds" />
                   </c:forEach>
                   
                   <c:forEach items="${linksToMove}" var="link">
	                   <input type="hidden" value="${link.id}" name="linkIds" />
                   </c:forEach>
                   
                   <c:forEach items="${publicationsToMove}" var="publication">
	                   <input type="hidden" value="${publication.id}" name="publicationIds" />
                   </c:forEach>
    
	               <!-- set to indicate a success full move -->
	               <input type="hidden" id="action_success" value="${actionSuccess}" name="actionSuccess"/>
               </form>
               <table class="simpleTable" width="400px">
	               <thead>
		               <tr>
			               <th class="thItemFolder">Destination</th>
		               </tr>
	               </thead>
	               <tbody>
		           <c:forEach items="${currentDestinationContents}"
			              var="fileSystemObject">
			           <tr>
			           	   <c:if test="${fileSystemObject.fileSystemType.type == 'researcherFolder'}">
			           	       <c:if test="${ir:canMoveToResearcherFolder(foldersToMove, fileSystemObject)}">
						            <td><span class="folderBtnImg"></span><a
							            href="javascript:YAHOO.ur.researcher.filesystem.move.getMoveFolder(${fileSystemObject.id});">${fileSystemObject.name}</a></td>
					           </c:if>
					           <c:if test="${!ir:canMoveToResearcherFolder(foldersToMove, fileSystemObject)}">
					               <td class="errorMessage"><span class="folderBtnImg"></span>${fileSystemObject.name} [Moving]</td>
					           </c:if>
					       </c:if>
			           	   <c:if test="${fileSystemObject.fileSystemType.type == 'researcherFile'}">
			           	       <c:if test="${!ir:isResearcherObjectToBeMoved(filesToMove, fileSystemObject)}">
						           <td><ir:fileTypeImg cssClass="tableImg" irFile="${fileSystemObject.irFile}"/>${fileSystemObject.name}</td>
					           </c:if>
					           <c:if test="${ir:isResearcherObjectToBeMoved(filesToMove, fileSystemObject)}">
						           <td class="errorMessage"><ir:fileTypeImg cssClass="tableImg" irFile="${fileSystemObject.irFile}"/>${fileSystemObject.name}[Moving]</td>
					           </c:if>
					       </c:if>
					       <c:if test="${fileSystemObject.fileSystemType.type == 'researcherPublication'}">
			           	       <c:if test="${!ir:isResearcherObjectToBeMoved(publicationsToMove, fileSystemObject)}">
						            <td><span class="scriptImg">&nbsp;</span>${fileSystemObject.name}</td>
                               </c:if>
			           	       <c:if test="${ir:isResearcherObjectToBeMoved(publicationsToMove, fileSystemObject)}">
						            <td class="errorMessage"><span class="packageBtnImg">&nbsp;</span>${fileSystemObject.name}[Moving]</td>
                               </c:if>
					       </c:if>
					       <c:if test="${fileSystemObject.fileSystemType.type == 'researcherLink'}">
			           	       <c:if test="${!ir:isResearcherObjectToBeMoved(linksToMove, fileSystemObject)}">
						            <td><img  alt="" 
			                           src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/>${fileSystemObject.name}</td>
					           </c:if>
			           	       <c:if test="${ir:isResearcherObjectToBeMoved(linksToMove, fileSystemObject)}">
						            <td class="errorMessage"><img  alt="" 
			                           src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/>${fileSystemObject.name}[Moving]</td>
					           </c:if>

					       </c:if>
					       <c:if test="${fileSystemObject.fileSystemType.type == 'researcherInstitutionalItem'}">
			           	       <c:if test="${!ir:isResearcherObjectToBeMoved(itemsToMove, fileSystemObject)}">
						            <td><span class="packageBtnImg">&nbsp;</span> ${fileSystemObject.name}</td>
						       </c:if>
			           	       <c:if test="${ir:isResearcherObjectToBeMoved(itemsToMove, fileSystemObject)}">
						            <td class="errorMessage"><span class="packageBtnImg">&nbsp;</span> ${fileSystemObject.name} [Moving]</td>
						       </c:if>
					       </c:if>							       
			           </tr>
		           </c:forEach>
	               </tbody>
               </table>
		   </td>
	   </tr>
   </table>

<div id="move_error" class="hidden">
<ir:printError key="moveError" errors="${fieldErrors}"></ir:printError>
</div>
