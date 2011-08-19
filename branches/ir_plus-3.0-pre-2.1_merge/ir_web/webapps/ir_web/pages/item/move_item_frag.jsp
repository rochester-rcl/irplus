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


	<c:forEach items="${itemsToMove}" var="item">
	     <c:set  value="${item.id}" var="list_of_itemIds"/>
	</c:forEach>

    <c:url var="cancelUrl" value="/institutionalPublicationPublicView.action">
        <c:param name="institutionalItemId" value="${list_of_itemIds}"/>
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
		            Move To Location: /<ur:a href="javascript:YAHOO.ur.institutional.item.move.getMoveCollection('0');">${repository.name}</ur:a>/
		            <c:forEach var="collection" items="${destinationPath}">
			            <ur:a href="javascript:YAHOO.ur.institutional.item.move.getMoveCollection('${collection.id}')">${collection.name}</ur:a>/
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
                          <urstb:td>Publications to Move</urstb:td>
                      </urstb:tr>
                  </urstb:thead>
                  <urstb:tbody
                      var="item" 
                      oddRowClass="odd"
                      evenRowClass="even"
                      currentRowClassVar="rowClass"
                      collection="${itemsToMove}">
                      <urstb:tr 
                          cssClass="${rowClass}"
                          onMouseOver="this.className='highlight'"
                          onMouseOut="this.className='${rowClass}'">
                          <urstb:td>
                              <span class="packageBtnImg">&nbsp;</span>${item.name}
                          </urstb:td>
                      </urstb:tr>
                  </urstb:tbody>
              </urstb:table>
              </div>
		    </td>
		    <td width="100px" align="center" valign="top">
		
		        <button class="ur_button" id="move_button"
			            onclick="javascript:YAHOO.ur.institutional.item.move.moveCollection();"
			            onmouseover="this.className='ur_buttonover';"
			            onmouseout="this.className='ur_button';"
			            >Move<span class="pageWhiteGoBtnImg">&nbsp;</span>
			    </button>
		   </td>
		   <td width="400px" align="left" valign="top">
		       <form name="viewChildContentsForMove"
	              id="move_collection_form"><input type="hidden"
	              id="destination_id" name="destinationId" value="${destinationId}" />
	              <input type="hidden" name="parentCollectionId" value="${parentCollectionId}" /> 
	
	               <c:forEach items="${collectionsToMove}" var="collection">
	                   <input type="hidden" value="${collection.id}" name="collectionIds" />
                   </c:forEach> 
 
                   <input type="hidden" value="${list_of_itemIds}" name="itemIds" id="item_ids"/>
    
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
                              <c:if test="${fileSystemObject.fileSystemType.type == 'institutionalCollection'}">
						            <span class="worldBtnImg">&nbsp;</span><a
							            href="javascript:YAHOO.ur.institutional.item.move.getMoveCollection(${fileSystemObject.id});">${fileSystemObject.name}</a>
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
