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
		            Move To Location: /<ur:a href="javascript:YAHOO.ur.institutional.item.move.getMoveCollection('0');">irplus</ur:a>/
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
		        <table class="simpleTable" width="400px">
			        <thead>
				        <tr>
					        <th>Collections &amp; Publications to Move</th>
				        </tr>
			        </thead>
			        <tbody>
				        <c:forEach items="${collectionsToMove}" var="collection">
					        <tr>
						        <td><span class="worldBtnImg">&nbsp;</span>${collection.name}</td>
					        </tr>
				        </c:forEach>

				        <c:forEach items="${itemsToMove}" var="item">
					        <tr>
						        <td><span class="packageBtnImg">&nbsp;</span>${item.name}</td>
					        </tr>
				        </c:forEach>
			        </tbody>
		        </table>
		    </td>
		    <td width="100px" align="center">
		
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
               <table class="simpleTable" width="400px">
	               <thead>
		               <tr>
			               <th class="thItemFolder">Destination</th>
		               </tr>
	               </thead>
	               <tbody>
		           <c:forEach items="${currentDestinationContents}"
			              var="fileSystemObject">
			           <tr>
			           	   <c:if test="${fileSystemObject.fileSystemType.type == 'institutionalCollection'}">
						            <td><span class="worldBtnImg">&nbsp;</span><a
							            href="javascript:YAHOO.ur.institutional.item.move.getMoveCollection(${fileSystemObject.id});">${fileSystemObject.name}</a></td>
					       </c:if>
			           	   <c:if test="${fileSystemObject.fileSystemType.type == 'institutionalItem'}">
					           <c:if test="${!ir:isInstitutionalPublicationToBeMoved(itemsToMove, fileSystemObject)}">
						            <td><span class="packageBtnImg">&nbsp;</span>${fileSystemObject.name}</td>
						       </c:if>
					           <c:if test="${ir:isInstitutionalPublicationToBeMoved(itemsToMove, fileSystemObject)}">
						            <td  class="errorMessage"><span class="packageBtnImg">&nbsp;</span>${fileSystemObject.name} [Moving]</td>
						       </c:if>						       
					       </c:if>					       
			           </tr>
		           </c:forEach>
	               </tbody>
               </table>
		   </td>
	   </tr>
   </table>

<div id="move_error" class="hidden">
<ir:printError key="moveError" errors="${fieldErrors}"></ir:printError>
</div>
