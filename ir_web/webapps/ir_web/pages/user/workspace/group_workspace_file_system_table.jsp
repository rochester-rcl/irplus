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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="messages"/>

   <br/>

   <table width="100%">
   <tr>
       <td align="left" width="75%">
           <strong>Path:&nbsp;/
	          <span class="folderBtnImg">&nbsp;</span>
	          <a href="javascript:YAHOO.ur.user.group_workspace.getGroupWorkspaces()">Group Workspaces</a> /
	          <c:if test="${!empty folderPath}">
                   <a href="javascript:YAHOO.ur.groupFolder.getFolderById(0, -1)">${groupWorkspace.name}</a>&nbsp;/
               </c:if>
               <c:if test="${empty folderPath}">
                   ${groupWorkspace.name}&nbsp;/
               </c:if>
	    
               <c:forEach var="folder" items="${folderPath}">
               <span class="folderBtnImg">&nbsp;</span>
                   <c:if test="${folder.id != parentFolderId}">
                       <a href="javascript:YAHOO.ur.groupFolder.getFolderById(${folder.id}, -1)">${folder.name}</a>&nbsp;/
                   </c:if>
                   <c:if test="${folder.id == parentFolderId}">
                       ${folder.name}&nbsp;/
                   </c:if>
               </c:forEach>
           </strong>
       </td>      
   </tr>
   </table>
   
   
  <div align="right">
	           <button class="ur_button" 
 		                   onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
 		                   onClick="YAHOO.ur.folder.newFolderDialog.showFolder();"
 		                   id="showFolder"><span class="addFolderBtnImg">&nbsp;</span><fmt:message key="new_folder"/></button> 
	           <c:if test='${ir:userHasRole("ROLE_AUTHOR", "OR")}'>
	               <button class="ur_button" 
	                       onclick="YAHOO.ur.folder.singleFileUploadDialog.showDialog();"
	                       onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
	                       id="addSingleFileButton"><span class="pageAddBtnImg">&nbsp;</span>Add File</button>
	               <button class="ur_button" onclick="javascript:document.addFilesForm.submit();"
	                       onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
	                       id="addFilesButton"><span class="pageCopyBtnImg">&nbsp;</span>Add Files</button>
	          </c:if>
	               <button class="ur_button" 
	                       onclick="YAHOO.ur.folder.moveFolderData();"
	                       onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
	                       id="moveButton"><span class="pageWhiteGoBtnImg">&nbsp;</span>Move</button>
	               <button class="ur_button" 
	                       onclick="YAHOO.ur.folder.deleteFolder.showDialog();"
	                       onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
	                       id="deleteButton"><span class="deleteBtnImg">&nbsp;</span>Delete</button>
	                       
	          <c:if test='${ir:userHasRole("ROLE_AUTHOR", "OR")}'>
	               <button class="ur_button" 
	                       onclick="YAHOO.ur.folder.inviteUser();"
	                       onmouseover="this.className='ur_buttonover';"
 		                   onmouseout="this.className='ur_button';"
	                       id="shareButton"><span class="groupAddBtnImg">&nbsp;</span>Share</button>
	               <button class="ur_button" id="showPublishItem" 
	                   onclick="javascript:YAHOO.ur.personal.collection.newItemDialog.createFromFilesFolders();"
	                   onmouseover="this.className='ur_buttonover';"
 		               onmouseout="this.className='ur_button';"><span class="reportGoBtnImg">&nbsp;</span>Publish</button>
	           </c:if>
    </div>
    <br/>
 
               
<div class="dataTable">

</div>