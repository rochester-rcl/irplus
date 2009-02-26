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

<!-- Displays the files and folders in a table.
Displayed on the left hand side of the add files to item page -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<table>
<tr>
<td>
	<!-- Displays the collection path -->
	/<ur:a href="javascript:YAHOO.ur.item.collection.getCollections('0')">${repository.name}</ur:a>/
	   <c:forEach var="collection" items="${collectionPath}">
	       <ur:a href="javascript:YAHOO.ur.item.collection.getCollections('${collection.id}')">${collection.name}</ur:a>/
	   </c:forEach>
</td>
</tr>
</table>
	    
 

<!-- Table for files and folders  -->            
<table class="itemFolderTable" width="100%">
	<thead>
		<tr>
			<th class="thItemFolder" width="30%">Submit</th>
			<th class="thItemFolder">irplus Collections</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="collectionPermission" items="${collectionsPermission}">
			<tr >
				<td class="tdItemFolderLeftBorder" >
					<c:if test="${collectionPermission.permission == 'DIRECT_SUBMIT'}">
		            	<span class="addBtnImg">&nbsp;</span><a href="JAVASCRIPT:YAHOO.ur.item.collection.addCollectionToPublication('${collectionPermission.institutionalCollection.id}');">Direct</a>
	                </c:if>
					<c:if test="${collectionPermission.permission == 'REVIEW_SUBMIT'}">
		                <span class="groupAddBtnImg">&nbsp;</span><a href="JAVASCRIPT:YAHOO.ur.item.collection.addCollectionToPublication('${collectionPermission.institutionalCollection.id}');">Review</a>
	                </c:if>
					<c:if test="${collectionPermission.permission == 'NO_PERMISSION'}">
		                 <span class="deleteBtnImg">&nbsp;</span> Can't Submit
	                </c:if>
					<c:if test="${collectionPermission.permission == 'ALREADY_SUBMITTED'}">
		                 <span class="worldBtnImg">&nbsp;</span>Already submitted
	                </c:if>
					<c:if test="${collectionPermission.permission == 'REVIEW_PENDING'}">
		                  <span class="magnifierBtnImg">&nbsp;</span>Review Pending 
	                </c:if>
				</td>
				
				<td class="tdItemFolderRightBorder">
                 	<span class="folderImg">&nbsp;</span>
                 	<ur:a href="javascript:YAHOO.ur.item.collection.getCollections('${collectionPermission.institutionalCollection.id}')"> ${collectionPermission.institutionalCollection.name} </ur:a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
