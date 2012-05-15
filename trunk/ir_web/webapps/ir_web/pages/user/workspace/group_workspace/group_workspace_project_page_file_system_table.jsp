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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="messages"/>
<div class="dataTable">
   <h3>Path: /<span class="folderBtnImg">&nbsp;</span><a href="javascript:YAHOO.ur.groupworkspace.file_system.getFolderById(${groupWorkspaceProjectPage.id}, 0)">${groupWorkspaceProjectPage.groupWorkspace.name}</a>/
           <c:forEach var="folder" items="${folderPath}">
               <span class="folderBtnImg">&nbsp;</span><a href="javascript:YAHOO.ur.groupworkspace.file_system.getFolderById(${groupWorkspaceProjectPage.id}, ${folder.id})">${folder.name}</a>/
           </c:forEach>
    </h3>
    <div align="right" id="files_folders_buttons">
 	    <button class="ur_button" 
 		        onmouseover="this.className='ur_buttonover';"
 		        onmouseout="this.className='ur_button';"
 	 		    onclick="YAHOO.ur.groupworkspace.file_system.newFolderDialog.showDialog();"
 		        id="showFolder"><span class="addFolderBtnImg">&nbsp;</span><fmt:message key="new_folder"/></button> 
	                              
 		<button class="ur_button" 
 		        onmouseover="this.className='ur_buttonover';"
 		        onmouseout="this.className='ur_button';"
 	 		    onclick="YAHOO.ur.groupworkspace.file_system.newLinkDialog.showLink();"
 		        id="showLink"><img  alt="" class="buttonImg" src="${pageContext.request.contextPath}/page-resources/images/all-images/link_add.gif"/>
 		       <fmt:message key="new_link"/></button> 
	                              
	    <button class="ur_button" id="showAddFile"
	            onmouseover="this.className='ur_buttonover';"
 		        onmouseout="this.className='ur_button';"
 		        onclick="javascript:YAHOO.ur.edit.researcher.viewAddFiles();"><span class="pageAddBtnImg">&nbsp;</span>Add file</button>
	                              
	    <button class="ur_button" id="showAddPublication"
	            onmouseover="this.className='ur_buttonover';"
 		        onmouseout="this.className='ur_button';"
 		        onclick="javascript:YAHOO.ur.edit.researcher.viewAddPublications();"><span class="scriptImg">&nbsp;</span>Add Publication</button>
	                               
	   <button class="ur_button"
	           onmouseover="this.className='ur_buttonover';"
 		       onmouseout="this.className='ur_button';"
 		       onclick="javascript:YAHOO.ur.groupworkspace.file_system.moveResearcherData()"> <span class="pageWhiteGoBtnImg">&nbsp;</span>Move</button>
	                              
	    <button class="ur_button" id="showDeleteFolder"
	            onmouseover="this.className='ur_buttonover';"
 		        onmouseout="this.className='ur_button';"
 		        onclick="YAHOO.ur.groupworkspace.file_system.deleteFolder.showDialog();"><span class="deleteBtnImg">&nbsp;</span>Remove</button>
   
	</div>
    <br/>
	<form method="post" id="folders" name="myFolders" >
	   
	    
           <input type="hidden" id="myFolders_parentFolderId" name="parentFolderId" value="${parentFolderId}"/>
		   <input type="hidden" id="myFolders_groupWorkspaceProjectPageId" name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPage.id}"/>   
	       <input type="hidden" id="folder_sort_type" name="sortType" value="${sortType}"/>
	       <input type="hidden" id="folder_sort_element" name="sortElement" value="${sortElement}"/>
   
       
       <!-- Table for files and folders  -->    
        <urstb:table width="100%">
            <urstb:thead>
                <urstb:tr>
                    <urstb:td><input type="checkbox" name="checkAllSetting" 
	                    value="off" 
	                    onClick="YAHOO.ur.groupworkspace.file_system.setCheckboxes();"/>
	                </urstb:td>
                    
                    <urstb:tdHeadSort
                       height="33"
                       currentSortAction="${folderTypeSort}"
                       ascendingSortAction="javascript:YAHOO.ur.groupworkspace.file_system.updateSort('asc', 'type');"
                       descendingSortAction="javascript:YAHOO.ur.groupworkspace.file_system.updateSort('desc', 'type');">
                       <u>Type</u><urstb:thImgSort
                            sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
                            sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
                    
                    <urstb:tdHeadSort
                       height="33"
                       currentSortAction="${folderNameSort}"
                       ascendingSortAction="javascript:YAHOO.ur.groupworkspace.file_system.updateSort('asc', 'name');"
                       descendingSortAction="javascript:YAHOO.ur.groupworkspace.file_system.updateSort('desc', 'name');">
                       <u>Name</u><urstb:thImgSort
                            sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
                            sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
                        
                    <urstb:td>Properties</urstb:td>
                </urstb:tr>
            </urstb:thead>
            <urstb:tbody
                var="fileSystemObject" 
                oddRowClass="odd"
                evenRowClass="even"
                currentRowClassVar="rowClass"
                collection="${fileSystem}">
                    <urstb:tr 
                        cssClass="${rowClass}"
                        onMouseOver="this.className='highlight'"
                        onMouseOut="this.className='${rowClass}'">
                        <urstb:td>
							<c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFolder'}">
			                        <input type="checkbox" name="folderIds" id="folder_checkbox_${fileSystemObject.id}" 
			                            value="${fileSystemObject.id}"/>
		                     </c:if>
			                     
		  
		                     <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFile'}">
		                         <input type="checkbox"  name="fileIds" id="file_checkbox_${fileSystemObject.id}" 
		                             value="${fileSystemObject.id}"/>
		                     </c:if>	
		                     
		                     <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPagePublication'}">
		                         <input type="checkbox"  name="publicationIds" id="publication_checkbox_${fileSystemObject.id}" 
		                             value="${fileSystemObject.id}"/>
		                     </c:if>
		                     
		                     <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFileSystemLink'}">
		                         <input type="checkbox"  name="linkIds" id="link_checkbox_${fileSystemObject.id}" 
		                             value="${fileSystemObject.id}"/>
		                     </c:if>
		                     
		                     <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageInstitutionalItem'}">
		                         <input type="checkbox"  name="itemIds" id="item_checkbox_${fileSystemObject.id}" 
		                             value="${fileSystemObject.id}"/>
		                     </c:if>		                     
                        </urstb:td>                    
                        <urstb:td>
                            <!-- this deals with folder information
	                          folders get an id of the folder_checkbox_{id} 
	                          where id  is the id of the folder 
	                          clicking on a link creates a popup menu-->
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFolder'}">
                                <span class="folderBtnImg">&nbsp;</span>
	                         </c:if>
	                     
	                         <!-- clicking on a link creates a popup menu-->
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFile'}">
	                         	<ir:fileTypeImg cssClass="tableImg" irFile="${fileSystemObject.irFile}"/>
	                         </c:if>
							 <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPagePublication'}">
		                    	<span class="scriptImg">&nbsp;</span>
		                	 </c:if>		  
		                	
		                	 <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFileSystemLink'}">
		                    	<img  alt="" 
			                       src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/> 
		                	 </c:if>	
		                	 
		                	  <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageInstitutionalItem'}">
		                    	<span class="packageBtnImg">&nbsp;</span> 
		                	 </c:if>	               
	                         
                        </urstb:td>  
                      
                        <urstb:td>
							 <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFolder'}">
			                 	<a href="javascript:YAHOO.ur.groupworkspace.file_system.getFolderById(${fileSystemObject.groupWorkspaceProjectPage.id}, ${fileSystemObject.id})"><ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText> </a><c:if test="${fileSystemObject.description != '' && fileSystemObject.description != null}"><div class="smallText">Description: <ur:maxText numChars="50" text="${fileSystemObject.description}"/></div></c:if>
			                 </c:if>
			                 
			                 <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFile'}">
			                    <c:url var="researcherFileDownloadUrl" value="/user/researcherFileDownload.action">
			                            <c:param name="researcherFileId" value="${fileSystemObject.id}"/>
			                        </c:url>
		                        <a href="${researcherFileDownloadUrl}"><ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText></a> [v${fileSystemObject.versionNumber}]<c:if test="${fileSystemObject.description != '' && fileSystemObject.description != null}"><div class="smallText">Description: <ur:maxText numChars="50" text="${fileSystemObject.description}"/></div></c:if>
			                 </c:if>
	
							<c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPagePublication'}">
		                    	<ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText>[v${fileSystemObject.versionNumber}] <c:if test="${fileSystemObject.description != '' && fileSystemObject.description != null}"><div class="smallText">Description: <ur:maxText numChars="50" text="${fileSystemObject.description}"/></div></c:if>
		                	</c:if>		  
		                	
		                	<c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFileSystemLink'}">
		                    	 <a href="${fileSystemObject.url}" target="_blank"><ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText></a><c:if test="${fileSystemObject.description != '' && fileSystemObject.description != null}"><div class="smallText">Description: <ur:maxText numChars="50" text="${fileSystemObject.description}"/></div></c:if>
		                	</c:if>	 
		                		
							<c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageInstitutionalItem'}">
		                    	<ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText><c:if test="${fileSystemObject.description != '' && fileSystemObject.description != null}"><div class="smallText">Description: <ur:maxText numChars="50" text="${fileSystemObject.description}"/></div></c:if>
		                	</c:if>	
	                	</urstb:td>
	                	<urstb:td>
							 <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFolder'}">
			                 	 <ur:a href="javascript:YAHOO.ur.groupworkspace.file_system.editFolder('${fileSystemObject.id}', '${ur:escapeSingleQuote(fileSystemObject.name)}', '${ur:escapeSingleQuote(fileSystemObject.description)}')"> Edit </ur:a>
			                 </c:if>
			                 
		                	<c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFileSystemLink'}">
		                    	<ur:a href="javascript:YAHOO.ur.groupworkspace.file_system.editLink(${fileSystemObject.id})"> Edit </ur:a>
		                	</c:if>
		                </urstb:td>	 	                	
                    </urstb:tr>
            </urstb:tbody>
        </urstb:table>
                                      
   </form>       
</div>