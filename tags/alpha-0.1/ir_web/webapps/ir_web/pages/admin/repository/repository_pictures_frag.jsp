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
<%@ taglib prefix="ir" uri="ir-tags"%>

<table class="simpleTable">
                      <thead>
                          <tr>    
	                          <th>
	                              Thumbnail
	                          </th>
	                          <th>
	                              Description
	                          </th>
	                          <th>
	                              Created Date
	                          </th>
	                          <th>
	                              Created By
	                          </th>
	                          <th>
	                              Delete
	                          </th>
                          </tr>
                      </thead>
                      <tbody>
                          <c:forEach var="irFile" varStatus="status" items="${repository.pictures}">
                              <c:if test="${ (status.count % 2) == 0}">
                                  <c:set value="even" var="rowType"/>
                              </c:if>
                              <c:if test="${ (status.count % 2) == 1}">
                                  <c:set value="odd" var="rowType"/>
                              </c:if>
                         
                           <tr>
                               <td class="${rowType}">
                                   <ir:transformUrl systemCode="PRIMARY_THUMBNAIL" download="true" irFile="${irFile}" var="url"/>
                                   <c:if test="${url != null}">
                                        <img  src="${url}"/>
                                   </c:if>
                               </td>
     
                               <td class="${rowType}">${irFile.description}</td>
                               <td class="${rowType}">${irFile.fileInfo.createdDate}</td>
                               <td class="${rowType}">${irFile.owner.username}</td>
                               <td class="${rowType}">
 		                              <button class="ur_button" 
 		                               onmouseover="this.className='ur_buttonover';"
 		                               onmouseout="this.className='ur_button';"
 		                               onclick="javascript:YAHOO.ur.repository.confirmPictureDelete(${irFile.id});"
 		                               id="showFolder">Delete Picture</button> 
	                           </td>
                           </tr>
                  
                           </c:forEach>  
                       </tbody>  
                   </table>