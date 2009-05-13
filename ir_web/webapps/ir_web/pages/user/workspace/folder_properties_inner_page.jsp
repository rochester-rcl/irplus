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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ir" uri="ir-tags"%>


            <!-- This is only to top level properties information -->
             <ur:basicForm  id="folder_properties_form" name="folderPropertiesForm" 
                 action="user/viewFolderProperties.action">
	             <input type="hidden" id="personalFolderId" 
	             name="personalFolderId"  value="${personalFolder.id}"/>
	         </ur:basicForm>
	        
            <c:url var="rootFolderUrl" value="/user/workspace.action">
               <c:param name="parentFolderId" value="0"/>
            </c:url>
            <h3> Folder Properties for : ${personalFolder.name}</h3>
            <h3> Path: /<span class="folderBtnImg">&nbsp;</span><a href="${rootFolderUrl}">${user.username}</a>/
                 <c:forEach var="folder" items="${folderPath}">
                     <c:url var="folderUrl" value="/user/workspace.action">
                         <c:param name="parentFolderId" value="${folder.id}"/>
                     </c:url>
                     <span class="folderBtnImg">&nbsp;</span><a href="${folderUrl}">${folder.name}</a>/
                 </c:forEach>
             </h3>
	    
	         <br/>
              <table class="table">
                  <tr>
                     <td><strong>Description:&nbsp;</strong></td>
                     <td>${personalFolder.description}</td>
                  </tr>
                  <tr>
                      <td><strong>No. of files:&nbsp;</strong></td>
                      <td>${filesCount}
                      </td>
                  </tr>
                  <tr>
                      <td><strong>Size of folder:&nbsp;</strong></td>
                      <td><ir:fileSizeDisplay sizeInBytes="${folderSize}"/></td>
                  </tr>
              </table>
              
              <table>
	             <tr>
	                 <td>
                        <button class="ur_button" id="edi_folder_name" 
                            onclick="YAHOO.ur.folder.properties.editFolder(${personalFolder.id})";
                            onmouseover="this.className='ur_buttonover';"
	                        onmouseout="this.className='ur_button';">Edit Folder Information</button>
	                 </td>
	             </tr>
	          </table>
          
             