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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<fmt:setBundle basename="messages"/>


    <h3>Path: /<span class="worldBtnImg">&nbsp;</span>
         <c:if test="${parentCollectionId != 0}">
             <a href="javascript:YAHOO.ur.item.version.getCollectionById('0')">personalCollections</a>&nbsp;/
         </c:if>
         <c:if test="${parentCollectionId == 0}">
                   personalCollections&nbsp;/
         </c:if>
         
         <c:forEach var="collection" items="${collectionPath}">
               <span class="worldBtnImg">&nbsp;</span>
                   <c:if test="${collection.id != parentCollectionId}">
                       <a href="javascript:YAHOO.ur.item.version.getCollectionById('${collection.id}')">${collection.name}</a>&nbsp;/
                   </c:if>
                   <c:if test="${collection.id == parentCollectionId}">
                       ${collection.name}&nbsp;/
                   </c:if>
         </c:forEach>
    </h3>



<div class="dataTable">
   
	<ur:basicForm method="post" id="collections" name="myCollections" >
	    
           <input type="hidden" id="myCollections_parentCollectionId" 
               name="parentCollectionId" value="${parentCollectionId}"/>

		   <input type="hidden" id="myCollections_institutionalItemId" 
		               name="institutionalItemId" value="${institutionalItemId}"/>

        <urstb:table width="100%">
            <urstb:thead>
                <urstb:tr>
                    <urstb:td> Id </urstb:td>
                    <urstb:td> Name </urstb:td>
                    <urstb:td> Version </urstb:td>
                    <urstb:td> Add </urstb:td>
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
                             ${fileSystemObject.id}
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalCollection'}">
	                            <span class="worldBtnImg">&nbsp;</span>
	                            <a href="javascript:YAHOO.ur.item.version.getCollectionById('${fileSystemObject.id}')">${fileSystemObject.name}</a>
	                        </c:if>
	                        <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
                            	<span class="packageBtnImg">&nbsp;</span>
                            	${fileSystemObject.name} 
	                        	<c:if test="${fileSystemObject.versionedItem.currentVersion.item.locked == 'true'}">
	                            	${fileSystemObject.name} (locked for review)
	                            </c:if>	                            
	                        </c:if>
                        </urstb:td>
                        <urstb:td>
 	                        <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
                            	<select id="version_${fileSystemObject.id}" name="version_${fileSystemObject.id}" /> 
				      	   
						      		<c:forEach var="version" items="${fileSystemObject.versionedItem.itemVersions}" >
						      			<option value = "${version.id}"
						      			<c:if test="${fileSystemObject.versionedItem.largestVersion == version.versionNumber}">
					      					selected
					      				</c:if>
						      			> ${version.versionNumber}</option>
						      		</c:forEach>
				      	  		 </select> 
                           
	                        </c:if>
                        </urstb:td>                        
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
								<button class="ur_button" type="button" onclick="javascript:YAHOO.ur.item.version.addVersion('${fileSystemObject.id}')"
				                       onmouseover="this.className='ur_buttonover';"
			 		                   onmouseout="this.className='ur_button';"
				                       id="new_version">Add as version</button>  	                            
                            </c:if>
                        </urstb:td>
                 </urstb:tr>
            </urstb:tbody>
        </urstb:table>
    
    </ur:basicForm>
</div>