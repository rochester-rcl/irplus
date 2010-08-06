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
		            Move To Location: /<ur:a href="javascript:YAHOO.ur.shared.file.move.getMoveFolder('0');">My Files</ur:a>/
		            <c:forEach var="folder" items="${destinationPath}">
			            <ur:a href="javascript:YAHOO.ur.shared.file.move.getMoveFolder('${folder.id}')">${folder.name}</ur:a>/
                    </c:forEach>
                </div>
            
                <br/>
                <br/>                                 
            
            </td>
	    </tr>
	    <tr>
		    <td width="400px" align="left" valign="top">
		    <div class="dataTable">
              <urstb:table width="100%">
                  <urstb:thead>
                      <urstb:tr>
                          <urstb:td>Shared Files to Move</urstb:td>
                      </urstb:tr>
                  </urstb:thead>
                  <urstb:tbody
                      var="file" 
                      oddRowClass="odd"
                      evenRowClass="even"
                      currentRowClassVar="rowClass"
                      collection="${filesToMove}">
                      <urstb:tr 
                          cssClass="${rowClass}"
                          onMouseOver="this.className='highlight'"
                          onMouseOut="this.className='${rowClass}'">
                          <urstb:td>
                                <ir:fileTypeImg cssClass="tableImg" versionedFile="${file.versionedFile}"/>${file.name}
                          </urstb:td>
                      </urstb:tr>
                  </urstb:tbody>
              </urstb:table>
              </div>
		    </td>
		    <td width="100px" align="center" valign="top">
		
		        <button class="ur_button" id="move_button"
			            onclick="javascript:YAHOO.ur.shared.file.move.moveFile();"
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
 
                   <c:forEach items="${filesToMove}" var="file">
	                   <input type="hidden" value="${file.id}" name="sharedInboxFileIds" />
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
						            <span class="folderBtnImg"></span><a
							            href="javascript:YAHOO.ur.shared.file.move.getMoveFolder(${fileSystemObject.id});">${fileSystemObject.name}</a>
					           </c:if>
			           	       <c:if test="${fileSystemObject.fileSystemType.type == 'personalFile'}">
						            <ir:fileTypeImg cssClass="tableImg" versionedFile="${fileSystemObject.versionedFile}"/>${fileSystemObject.name}
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
