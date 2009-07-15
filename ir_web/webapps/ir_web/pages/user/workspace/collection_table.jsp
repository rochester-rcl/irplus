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
<%@ taglib prefix="ir" uri="ir-tags"%>

<c:if test='${ir:userHasRole("ROLE_AUTHOR", "OR")}'>
<fmt:setBundle basename="messages"/>


    <h3>Path: /<span class="folderBtnImg">&nbsp;</span>
         <c:if test="${parentCollectionId != 0}">
             <a href="javascript:YAHOO.ur.personal.collection.getCollectionById('0')">My Publications</a>&nbsp;/
         </c:if>
         <c:if test="${parentCollectionId == 0}">
                   My Publications&nbsp;/
         </c:if>
         
         <c:forEach var="collection" items="${collectionPath}">
               <span class="folderBtnImg">&nbsp;</span>
                   <c:if test="${collection.id != parentCollectionId}">
                       <a href="javascript:YAHOO.ur.personal.collection.getCollectionById('${collection.id}')">${collection.name}</a>&nbsp;/
                   </c:if>
                   <c:if test="${collection.id == parentCollectionId}">
                       ${collection.name}&nbsp;/
                   </c:if>
         </c:forEach>
    </h3>

    <table width="100%">
        <tr>
            <td align="left">
                <select onchange="javascript:YAHOO.ur.personal.collection.executeCheckboxAction(this.options[this.selectedIndex].value);">
                    <option value="action" id="collection_checkbox_action_set"  selected="selected">Action on checked collections and publications ...</option>
                    <option value="delete">Delete</option>
                    <option value="move">Move</option>
	            </select>
	        </td>
	        <td align="right">
 		        <button class="ur_button" 
 		            onclick="javascript:YAHOO.ur.personal.collection.newCollectionDialog.showDialog();"
 		            onmouseover="this.className='ur_buttonover';"
 		            onmouseout="this.className='ur_button';"
 		            id="showCollection"><span class="addFolderBtnImg">&nbsp;</span><fmt:message key="new_collection"/></button> 
 	            
	            <button class="ur_button" 
	        	    onclick="javascript:YAHOO.ur.personal.collection.newItemDialog.createFromPersonalCollection();" 
	                onmouseover="this.className='ur_buttonover';"
 		            onmouseout="this.className='ur_button';"
	                id="addItemButton"><span class="packageAddBtnImg">&nbsp;</span>Create Publication</button>

	        </td>
        </tr>
    </table>

<div class="dataTable">
   
	<c:url var="addFilesToItem" value="/user/viewAddFilesToItemPage.action"/>
	
	<form method="get" action="${addFilesToItem}" name="addFilesToItem">
	    <input type="hidden" value="${parentCollectionId}" name="collectionId"/>
	</form>
	
    <c:url var="myFoldersUrl" value="/user/workspace.action"/>
	<form method="post" id="collections" name="myCollections" >
	    
           <input type="hidden" id="myCollections_parentCollectionId" 
               name="parentCollectionId" value="${parentCollectionId}"/>

           <!--  values set for sorting -->
	       <input type="hidden" id="collection_sort_type" name="sortType" value="${sortType}"/>
	       <input type="hidden" id="collection_sort_element" name="sortElement" value="${sortElement}"/>
        <c:url var="downArrow" value="/page-resources/images/all-images/bullet_arrow_down.gif" />
        <urstb:table width="100%">
            <urstb:thead>
                <urstb:tr>
                    <urstb:td>
                        <input type="checkbox" name="checkAllSetting" 
	                    value="off" 
	                    onClick="YAHOO.ur.personal.collection.setCheckboxes();"/>
                    </urstb:td>
                    <urstb:tdHeadSort
                       height="33"
                       currentSortAction="${collectionTypeSort}"
                       ascendingSortAction="javascript:YAHOO.ur.personal.collection.updateSort('asc', 'type');"
                       descendingSortAction="javascript:YAHOO.ur.personal.collection.updateSort('desc', 'type');">
                       <u>Type</u><urstb:thImgSort
                            sortAscendingImage="page-resources/images/all-images/bullet_arrow_down.gif"
                            sortDescendingImage="page-resources/images/all-images/bullet_arrow_up.gif"/></urstb:tdHeadSort>
                    
                    <urstb:tdHeadSort
                       height="33"
                       currentSortAction="${collectionNameSort}"
                       ascendingSortAction="javascript:YAHOO.ur.personal.collection.updateSort('asc', 'name');"
                       descendingSortAction="javascript:YAHOO.ur.personal.collection.updateSort('desc', 'name');">
                       <u>Name</u><urstb:thImgSort
                            sortAscendingImage="page-resources/images/all-images/bullet_arrow_down.gif"
                            sortDescendingImage="page-resources/images/all-images/bullet_arrow_up.gif"/></urstb:tdHeadSort>
                    <urstb:td> Version </urstb:td>
                    <urstb:td> Properties </urstb:td>
                    <urstb:td> Submit </urstb:td>
                    <urstb:td> Current Version Published </urstb:td>
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
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalCollection'}">
	                            <input type="checkbox" name="collectionIds" id="collection_checkbox_${fileSystemObject.id}"  value="${fileSystemObject.id}"/>
	                        </c:if>
	                        <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
	                            <input type="checkbox" name="itemIds" id="item_checkbox_${fileSystemObject.id}"value="${fileSystemObject.id}"/>
	                         </c:if>
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalCollection'}">
                                <div id="collection_${fileSystemObject.id}">
	                            <button type="button" class="table_button" 
	                                 onmouseover="this.className='table_buttonover';"
 		                             onmouseout="this.className='table_button';"
	                                onclick="javascript:YAHOO.ur.personal.collection.buildCollectionMenu(this, 'collection_'+ ${fileSystemObject.id}, 
	                                 'collection_menu_' + ${fileSystemObject.id}, 
	                                 ${fileSystemObject.id} );"> <span class="folderBtnImg">&nbsp;</span><img src="${downArrow}"/></button>
	                            </div>
	                         </c:if>
	                         <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
	                             <div id="item_${fileSystemObject.id}">
	                             <button type="button" class="table_button"
	                                onmouseover="this.className='table_buttonover';"
 		                            onmouseout="this.className='ur_button';" 
	                                onclick="javascript:YAHOO.ur.personal.collection.buildItemMenu(this, 'item_'+ ${fileSystemObject.id}, 
	                                 'item_menu_' + ${fileSystemObject.id}, 
	                                 ${fileSystemObject.id} );"> <span class="packageBtnImg">&nbsp;</span><img src="${downArrow}"/></button>
	                             </div>
	                         </c:if>
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalCollection'}">
	                            <a href="javascript:YAHOO.ur.personal.collection.getCollectionById('${fileSystemObject.id}')">${fileSystemObject.name}</a>
	                        </c:if>
	                        <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
	                        	<c:if test="${fileSystemObject.versionedItem.currentVersion.item.locked == 'false'}">
	                            	<a href="JAVASCRIPT:YAHOO.ur.personal.collection.editPublication('${fileSystemObject.id}', '${fileSystemObject.versionedItem.currentVersion.item.id}', '${fileSystemObject.personalCollection.id}', '${fileSystemObject.versionedItem.currentVersion.item.publishedToSystem}');">${fileSystemObject.name}</a>
	                            </c:if>
	                        	<c:if test="${fileSystemObject.versionedItem.currentVersion.item.locked == 'true'}">
	                            	${fileSystemObject.name} (locked for review)
	                            </c:if>	                            
	                        </c:if>
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
	                             ${fileSystemObject.versionedItem.largestVersion}
	                        </c:if>
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
                                <c:url var="itemPropertiesUrl" value="/user/viewPersonalItemProperties.action">
                                    <c:param name="personalItemId" value="${fileSystemObject.id}"/>
                                </c:url>                            
	                            <a href="${itemPropertiesUrl}">properties</a>
	                        </c:if>
	                        <c:if test="${fileSystemObject.fileSystemType.type == 'personalCollection'}">
	                           <a href="javascript:YAHOO.ur.personal.collection.editCollection('${fileSystemObject.id}');">Edit</a>
	                        </c:if>
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
	                            <c:url var="submitPublicationUrl" value="/user/viewAddItemToCollections.action">
	                                <c:param name="genericItemId" value="${fileSystemObject.versionedItem.currentVersion.item.id}"/>
	                                <c:param name="parentCollectionId" value="${fileSystemObject.personalCollection.id}"/>
	                            </c:url>
	                            <a href="${submitPublicationUrl}">Submit publication</a>
                            </c:if>
                        </urstb:td>
                        <urstb:td>
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
                                <c:if test="${fileSystemObject.versionedItem.currentVersion.item.publishedToSystem}">
                                   Yes
                                </c:if>
                                <c:if test="${!fileSystemObject.versionedItem.currentVersion.item.publishedToSystem}">
                                    No
                                </c:if>
                            </c:if>
                        </urstb:td>
                 </urstb:tr>
            </urstb:tbody>
        </urstb:table>
	    <input type="hidden" id="move_items_collections_destination_id" name="destinationCollectionId" value=""/>
	    
    </form>
</div>
</c:if>
<c:if test='${!ir:userHasRole("ROLE_AUTHOR", "OR")}'>
    <br/>
    <h3>You do not have permissions to create publications</h3>
</c:if>