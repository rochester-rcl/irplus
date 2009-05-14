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

<div class="dataTable">

	<ur:basicForm method="post" id="folders" name="myFolders" >
	   <h3>Path: /<span class="folderBtnImg">&nbsp;</span><ur:a href="javascript:YAHOO.ur.researcher.folder.getFolderById('0')">My Research</ur:a>/
           <c:forEach var="folder" items="${folderPath}">
               <span class="folderBtnImg">&nbsp;</span><ur:a href="javascript:YAHOO.ur.researcher.folder.getFolderById('${folder.id}')">${folder.name}</ur:a>/
           </c:forEach></h3>
	    
           <input type="hidden" id="myFolders_parentFolderId" name="parentFolderId" value="${parentFolderId}"/>
		   <input type="hidden" id="myFolders_researcherId" name="researcherId" value="${researcher.id}"/>   
	       <input type="hidden" id="folder_sort_type" name="sortType" value="${sortType}"/>
	       <input type="hidden" id="folder_sort_element" name="sortElement" value="${sortElement}"/>
   
       
       <!-- Table for files and folders  -->    
        <h3>Researcher Folders</h3>        
        <urstb:table width="100%">
            <urstb:thead>
                <urstb:tr>
                    <urstb:td><ur:checkbox name="checkAllSetting" 
	                    value="off" 
	                    onClick="YAHOO.ur.researcher.folder.setCheckboxes();"/>
	                </urstb:td>
                    
                    <urstb:tdHeadSort
                       height="33"
                       currentSortAction="${folderTypeSort}"
                       ascendingSortAction="javascript:YAHOO.ur.researcher.folder.updateSort('asc', 'type');"
                       descendingSortAction="javascript:YAHOO.ur.researcher.folder.updateSort('desc', 'type');">
                       <u>Type</u><urstb:thImgSort
                            sortAscendingImage="page-resources/images/all-images/bullet_arrow_down.gif"
                            sortDescendingImage="page-resources/images/all-images/bullet_arrow_up.gif"/></urstb:tdHeadSort>
                    
                    <urstb:td>Id</urstb:td>
                    
                    <urstb:tdHeadSort
                       height="33"
                       currentSortAction="${folderNameSort}"
                       ascendingSortAction="javascript:YAHOO.ur.researcher.folder.updateSort('asc', 'name');"
                       descendingSortAction="javascript:YAHOO.ur.researcher.folder.updateSort('desc', 'name');">
                       <u>Name</u><urstb:thImgSort
                            sortAscendingImage="page-resources/images/all-images/bullet_arrow_down.gif"
                            sortDescendingImage="page-resources/images/all-images/bullet_arrow_up.gif"/></urstb:tdHeadSort>
                        
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
							<c:if test="${fileSystemObject.fileSystemType.type == 'researcherFolder'}">
			                        <ur:checkbox name="folderIds" id="folder_checkbox_${fileSystemObject.id}" 
			                            value="${fileSystemObject.id}"/>
		                     </c:if>
			                     
		  
		                     <c:if test="${fileSystemObject.fileSystemType.type == 'researcherFile'}">
		                         <ur:checkbox name="fileIds" id="file_checkbox_${fileSystemObject.id}" 
		                             value="${fileSystemObject.id}"/>
		                     </c:if>	
		                     
		                     <c:if test="${fileSystemObject.fileSystemType.type == 'researcherPublication'}">
		                         <ur:checkbox name="publicationIds" id="publication_checkbox_${fileSystemObject.id}" 
		                             value="${fileSystemObject.id}"/>
		                     </c:if>
		                     
		                     <c:if test="${fileSystemObject.fileSystemType.type == 'researcherLink'}">
		                         <ur:checkbox name="linkIds" id="link_checkbox_${fileSystemObject.id}" 
		                             value="${fileSystemObject.id}"/>
		                     </c:if>
		                     
		                     <c:if test="${fileSystemObject.fileSystemType.type == 'researcherInstitutionalItem'}">
		                         <ur:checkbox name="institutionalItemIds" id="item_checkbox_${fileSystemObject.id}" 
		                             value="${fileSystemObject.id}"/>
		                     </c:if>		                     
                        </urstb:td>                    
                        <urstb:td>
                            <!-- this deals with folder information
	                          folders get an id of the folder_checkbox_{id} 
	                          where id  is the id of the folder 
	                          clicking on a link creates a popup menu-->
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'researcherFolder'}">
                                <span class="folderBtnImg">&nbsp;</span>
	                         </c:if>
	                     
	                         <!-- clicking on a link creates a popup menu-->
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'researcherFile'}">
	                         	<ir:fileTypeImg cssClass="tableImg" irFile="${fileSystemObject.irFile}"/>
	                         </c:if>
							 <c:if test="${fileSystemObject.fileSystemType.type == 'researcherPublication'}">
		                    	<span class="packageBtnImg">&nbsp;</span>
		                	 </c:if>		  
		                	
		                	 <c:if test="${fileSystemObject.fileSystemType.type == 'researcherLink'}">
		                    	<img  alt="" 
			                       src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/> 
		                	 </c:if>	
		                	 
		                	  <c:if test="${fileSystemObject.fileSystemType.type == 'researcherInstitutionalItem'}">
		                    	<span class="packageBtnImg">&nbsp;</span> 
		                	 </c:if>	               
	                         
                        </urstb:td>  
                        <urstb:td>
                            ${fileSystemObject.id}
                        </urstb:td>
                        <urstb:td>
							 <c:if test="${fileSystemObject.fileSystemType.type == 'researcherFolder'}">
			                 	<a href="javascript:YAHOO.ur.researcher.folder.getFolderById('${fileSystemObject.id}')"><ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText> </a><c:if test="${fileSystemObject.description != '' && fileSystemObject.description != null}"><div class="smallText">Description: <ur:maxText numChars="50" text="${fileSystemObject.description}"/></div></c:if>
			                 </c:if>
			                 
			                 <c:if test="${fileSystemObject.fileSystemType.type == 'researcherFile'}">
			                    <c:url var="researcherFileDownloadUrl" value="/user/researcherFileDownload.action">
			                            <c:param name="researcherFileId" value="${fileSystemObject.id}"/>
			                        </c:url>
		                        <a href="${researcherFileDownloadUrl}"><ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText></a> [v${fileSystemObject.versionNumber}]
			                 </c:if>
	
							<c:if test="${fileSystemObject.fileSystemType.type == 'researcherPublication'}">
		                    	<ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText>[v${fileSystemObject.versionNumber}]
		                	</c:if>		  
		                	
		                	<c:if test="${fileSystemObject.fileSystemType.type == 'researcherLink'}">
		                    	 <a href="${fileSystemObject.link}" target="_blank"> <ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText> </a>
		                	</c:if>	 
		                		
							<c:if test="${fileSystemObject.fileSystemType.type == 'researcherInstitutionalItem'}">
		                    	<ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText>
		                	</c:if>	
	                	</urstb:td>
	                	<urstb:td>
							 <c:if test="${fileSystemObject.fileSystemType.type == 'researcherFolder'}">
			                 	 <ur:a href="javascript:YAHOO.ur.researcher.folder.editFolder('${fileSystemObject.id}', '${ur:escapeSingleQuote(fileSystemObject.name)}', '${ur:escapeSingleQuote(fileSystemObject.description)}')"> Edit </ur:a>
			                 </c:if>
			                 
		                	<c:if test="${fileSystemObject.fileSystemType.type == 'researcherLink'}">
		                    	<ur:a href="javascript:YAHOO.ur.researcher.folder.editLink('${fileSystemObject.id}', '${ur:escapeSingleQuote(fileSystemObject.name)}', '${ur:escapeSingleQuote(fileSystemObject.description)}', '${ur:escapeSingleQuote(fileSystemObject.link)}')"> Edit </ur:a>
		                	</c:if>
		                </urstb:td>	 	                	
                    </urstb:tr>
            </urstb:tbody>
        </urstb:table>
                                      
   </ur:basicForm>       
</div>