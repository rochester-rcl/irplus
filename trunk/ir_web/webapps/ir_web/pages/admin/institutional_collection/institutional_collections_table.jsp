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
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="messages"/>

<table>
	<tr>
	    <c:if test="${parentCollectionId > 0}">
		    <ir:acl hasPermission="ADMINISTRATION" domainObject="${parent}">
		    <td>
		        <button class="ur_button"
			            onmouseover="this.className='ur_buttonover';"
			            onmouseout="this.className='ur_button';" id="showCollection"
			            onclick="javascript:YAHOO.ur.institution.newCollectionDialog.showDialog();"><span
			            class="worldAddBtnImg">&nbsp;</span> New Collection</button>
		    </td>
		   </ir:acl>
		</c:if>
		
		<c:if test="${parentCollectionId <= 0}">
		    <c:if test='${ir:userHasRole("ROLE_ADMIN", "")}'>
		    <td>
		        <button class="ur_button"
			            onmouseover="this.className='ur_buttonover';"
			            onmouseout="this.className='ur_button';" id="showCollection"
			            onclick="javascript:YAHOO.ur.institution.newCollectionDialog.showDialog();"><span
			            class="worldAddBtnImg">&nbsp;</span> New Collection</button>
			    
		    </td>
			</c:if>
		</c:if>
        <td>
		    <button class="ur_button" id="showDeleteCollection"
			        onmouseover="this.className='ur_buttonover';"
			        onmouseout="this.className='ur_button';"
			        onclick="javascript:YAHOO.ur.institution.deleteCollectionDialog.showDialog();"><span
			        class="deleteBtnImg">&nbsp;</span>Delete</button>
		</td>
		<td>
		    <button class="ur_button" id="showMoveCollection"
			        onmouseover="this.className='ur_buttonover';"
			        onmouseout="this.className='ur_button';"
			        onclick="javascrip:YAHOO.ur.institution.moveCollectionData();"><span
			        class="pageWhiteGoBtnImg">&nbsp;</span>Move</button>
		</td>
	</tr>
</table>


<c:if test="${totalHits > 0}">
	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
	<c:import url="browse_all_collections_pager.jsp"/>
	<br/>
</c:if>

<div class="dataTable">
	<form method="POST" id="institutionalCollections" name="institutionalCollections" >
	   
	   
	   /<a href="<c:url value="/admin/viewInstitutionalCollections.action"/>">${repository.name}</a>/
           <c:forEach var="collection" items="${collectionPath}">
              <c:url var="browseUrl" value="/admin/getInstitutionalCollections.action">
		           <c:param name="rowStart" value="0"/>
			       <c:param name="startPageNumber" value="1"/>
			       <c:param name="currentPageNumber" value="1"/>
			       <c:param name="sortType" value="asc"/>
			       <c:param name="parentCollectionId" value="${collection.id}"/>		
		       </c:url>
               <a href="${browseUrl}">${collection.name}</a>/
           </c:forEach>
	    
           <input type="hidden" id="institutionalCollections_parentCollectionId" 
               name="parentCollectionId" value="${parentCollectionId}"/>
           
           <!-- Indicates root destination -->    
           <input type="hidden" id="move_destination_Id" 
               name="destinationId" value="0"/>
        <br/>
        <br/>
	             
	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
					<urstb:td><input type="checkbox" name="checkAllSetting" 
								value="off" onClick="YAHOO.ur.institution.setCheckboxes();"/></urstb:td>         
	                <urstb:td>Id</urstb:td>
             
                    <c:url var="browseAscUrl" value="/admin/getInstitutionalCollections.action">
		                <c:param name="rowStart" value="${rowStart}"/>
			            <c:param name="startPageNumber" value="${startPageNumber}"/>
			            <c:param name="currentPageNumber" value="${currentPageNumber}"/>
			            <c:param name="sortType" value="asc"/>
			            <c:param name="parentCollectionId" value="${parentCollectionId}"/>		
		            </c:url>
		            
		            <c:url var="browseDescUrl" value="/admin/getInstitutionalCollections.action">
		                <c:param name="rowStart" value="${rowStart}"/>
			            <c:param name="startPageNumber" value="${startPageNumber}"/>
			            <c:param name="currentPageNumber" value="${currentPageNumber}"/>
			            <c:param name="sortType" value="desc"/>
			            <c:param name="parentCollectionId" value="${parentCollectionId}"/>		
		            </c:url>
             
	                <urstb:tdHeadSort  height="33"
	                    useHref="true"
					    hrefVar="href"
	                    currentSortAction="${sortType}"
	                    ascendingSortAction="${browseAscUrl}"
	                    descendingSortAction="${browseDescUrl}">
	                    <a href="${href}"><u>Name</u></a>                                              
	                    <urstb:thImgSort
	                                 sortAscendingImage="page-resources/images/all-images/bullet_arrow_up.gif"
	                                 sortDescendingImage="page-resources/images/all-images/bullet_arrow_down.gif"/></urstb:tdHeadSort>
	                <urstb:td>Virtual Path</urstb:td>
					<urstb:td>Properties</urstb:td>
	            </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="institutionalCollection" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${institutionalCollections}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
				                <ir:acl hasPermission="ADMINISTRATION" domainObject="${institutionalCollection}">
					                <input type="checkbox" name="collectionIds" value="${institutionalCollection.id}"/>
				                 </ir:acl>
	                        </urstb:td>
	                        <urstb:td>
		                        ${institutionalCollection.id}
	                        </urstb:td>
	                        <urstb:td>
	                           <c:url var="browseChildUrl" value="/admin/getInstitutionalCollections.action">
		                           <c:param name="rowStart" value="0"/>
			                       <c:param name="startPageNumber" value="1"/>
			                       <c:param name="currentPageNumber" value="1"/>
			                       <c:param name="sortType" value="${sortType}"/>
			                       <c:param name="parentCollectionId" value="${institutionalCollection.id}"/>		
		                       </c:url>
			                   <a href="${browseChildUrl}">${institutionalCollection.name}</a>
	                        </urstb:td>
	                        <urstb:td>
		                   		${institutionalCollection.path}
	                        </urstb:td>
	                        <urstb:td>
		                        <ir:acl domainObject="${institutionalCollection}" hasPermission="ADMINISTRATION">
		                            <c:url var="getCollectionPropertiesUrl" value="/admin/viewInstitutionalCollection.action">
		                                <c:param name="collectionId" value="${institutionalCollection.id}"/>
		                            </c:url>
		                            <a href="${getCollectionPropertiesUrl}">properties</a>
		                        </ir:acl>
	                        </urstb:td>
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
		</form>
</div>	

<c:if test="${totalHits > 0}">
	<c:import url="browse_all_collections_pager.jsp"/>
</c:if>

