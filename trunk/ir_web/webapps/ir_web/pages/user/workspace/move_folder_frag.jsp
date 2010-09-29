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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
 
    <c:url var="cancelUrl" value="/user/workspace.action">
        <c:param name="parentFolderId" value="${parentFolderId}"/>
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
		            Move To Location: /<ur:a href="javascript:YAHOO.ur.folder.move.getMoveFolder('0');">${user.username}</ur:a>/
		            <c:forEach var="folder" items="${destinationPath}">
			            <ur:a href="javascript:YAHOO.ur.folder.move.getMoveFolder('${folder.id}')">${folder.name}</ur:a>/
                    </c:forEach>
                </div>
            
                <br/>
                <br/>                                 
            
            </td>
	    </tr>
	    <tr>
		    <td width="400px" align="left" valign="top">
		        <div class="dataTable">
		        <table width="400px">
			        <thead>
				        <tr>
					        <td>Folders &amp; Files to Move</td>
				        </tr>
			        </thead>
			        <tbody>
				        <c:forEach items="${foldersToMove}"  varStatus="status" var="folder">
				            <c:if test="${ (status.count % 2) == 1}">
                                <c:set value="even" var="rowType"/>
                                <c:set value="this.className='even'" var="onmouseout"/>
                            </c:if>
                            <c:if test="${ (status.count % 2) == 0}">
                                <c:set value="odd" var="rowType"/>
                                <c:set value="this.className='odd'" var="onmouseout"/>
                            </c:if>
					        <tr onmouseout="${onmouseout}" onmouseover="this.className='highlight'" class="${rowType}">
						        <td><span class="folderBtnImg">&nbsp;</span>${folder.name}</td>
					        </tr>
					        <c:if test="${ (status.count % 2) == 1}">
					            <c:set value="0" var="modEven"/>
					            <c:set value="1" var="modOdd"/>
					        </c:if>
					        <c:if test="${(status.count % 2) == 0}">
					            <c:set value="1" var="modEven"/>
					            <c:set value="0" var="modOdd"/>
					        </c:if>
				        </c:forEach>

				        <c:forEach items="${filesToMove}"  varStatus="status" var="file">
				            <c:if test="${ (status.count % 2) == modEven}">
                                <c:set value="even" var="rowType"/>
                                <c:set value="this.className='even'" var="onmouseout"/>
                            </c:if>
                            <c:if test="${ (status.count % 2) == modOdd}">
                                <c:set value="odd" var="rowType"/>
                                <c:set value="this.className='odd'" var="onmouseout"/>
                            </c:if>
					        <tr onmouseout="${onmouseout}" onmouseover="this.className='highlight'" class="${rowType}"> 
						        <td><ir:fileTypeImg cssClass="tableImg" versionedFile="${file.versionedFile}"/>${file.name}</td>
					        </tr>
				        </c:forEach>
			        </tbody>
		        </table>
		        </div>
		    </td>
		    <td width="100px" valign="top" align="center">
		
		        <button class="ur_button" id="move_button"
			            onclick="javascript:YAHOO.ur.folder.move.moveFolder();"
			            onmouseover="this.className='ur_buttonover';"
			            onmouseout="this.className='ur_button';"
			            >Move<span class="pageWhiteGoBtnImg">&nbsp;</span>
			    </button>
		   </td>
		   <td width="400px" align="left" valign="top">
		       <form name="viewChildContentsForMove"
	              id="move_folder_form"><input type="hidden"
	              id="destination_id" name="destinationId" value="${destinationId}" />
	              <input type="hidden" name="parentFolderId" value="${parentFolderId}" /> 
	
	               <c:forEach items="${foldersToMove}" var="folder">
	                   <input type="hidden" value="${folder.id}" name="folderIds" />
                   </c:forEach> 
 
                   <c:forEach items="${filesToMove}" var="file">
	                   <input type="hidden" value="${file.id}" name="fileIds" />
                   </c:forEach>
    
	               <!-- set to indicate a success full move -->
	               <input type="hidden" id="action_success" value="${actionSuccess}" name="actionSuccess"/>
               </form>
               <div class="dataTable">
              <urstb:table width="100%">
                  <urstb:thead>
                      <urstb:tr>
                          <urstb:td>Destination</urstb:td>
                      </urstb:tr>
                  </urstb:thead>
                  <urstb:tbody
                      var="fileSystemObject" 
                      oddRowClass="odd"
                      evenRowClass="even"
                      currentRowClassVar="rowClass"
                      collection="${currentDestinationContents}">
                      <urstb:tr 
                          cssClass="${rowClass}"
                          onMouseOver="this.className='highlight'"
                          onMouseOut="this.className='${rowClass}'">
                          <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalFolder'}">
					           <c:if test="${ir:canMoveToFolder(foldersToMove, fileSystemObject)}">
						            <span class="folderBtnImg"></span><a
							            href="javascript:YAHOO.ur.folder.move.getMoveFolder(${fileSystemObject.id});">${fileSystemObject.name}</a>
					           </c:if>
					           <c:if test="${!ir:canMoveToFolder(foldersToMove, fileSystemObject)}">
						
						           <span class="folderBtnImg"></span><span class="errorMessage">${fileSystemObject.name} [Moving]</span>
					           </c:if>
					       </c:if>
			           	   <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
			           	   		<c:if test="${!ir:isFileToBeMoved(filesToMove, fileSystemObject)}">
						            <ir:fileTypeImg cssClass="tableImg" versionedFile="${fileSystemObject.versionedFile}"/>${fileSystemObject.name}
						        </c:if>
			           	   		<c:if test="${ir:isFileToBeMoved(filesToMove, fileSystemObject)}">
						            <ir:fileTypeImg cssClass="tableImg" versionedFile="${fileSystemObject.versionedFile}"/><span class="errorMessage">${fileSystemObject.name}[Moving] </span>
						        </c:if>
					       </c:if>	 
                          </urstb:td>
                      </urstb:tr>
                  </urstb:tbody>
              </urstb:table>
              </div>
		   </td>
	   </tr>
   </table>

<div id="move_error" class="hidden">
<ir:printError key="moveError" errors="${fieldErrors}"></ir:printError>
</div>
