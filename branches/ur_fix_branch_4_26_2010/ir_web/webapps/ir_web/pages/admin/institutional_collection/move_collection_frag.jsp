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
 
    <c:url var="cancelUrl" value="/admin/viewInstitutionalCollections.action">
        <c:param name="parentCollectionId" value="${parentCollectionId}"/>
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
		            Move To Location: /<ur:a href="javascript:YAHOO.ur.institution.getMoveCollection('0');">${repository.name}</ur:a>/
		            <c:forEach var="collection" items="${destinationPath}">
			            <ur:a href="javascript:YAHOO.ur.institution.getMoveCollection('${collection.id}')">${collection.name}</ur:a>/
                    </c:forEach>
                </div>
            
                <br/>
                <br/>                                 
            
                <c:if test="${destinationId > 0}">
		             <c:if test="${!ir:hasPermission('ADMINISTRATION',destination)}">
		                <p class="errorMessage">You are not the administrator of current location so you cannot move to this location</p>
		            </c:if>
		        </c:if>
		        <c:if test="${destinationId <= 0}">
		            <c:if test='${!ir:userHasRole("ROLE_ADMIN", "")}'>
    		            <p class="errorMessage">You are not the administrator of current location so you cannot move to this location</p>
		            </c:if>
		        </c:if>
		        	    
            </td>
	    </tr>
	    <tr>
		    <td width="400px" align="left" valign="top">
		        <table class="simpleTable" width="400px">
			        <thead>
				        <tr>
					        <th>Collections &amp; Items to Move</th>
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
			            onclick="javascript:YAHOO.ur.institution.moveCollection();"
			            onmouseover="this.className='ur_buttonover';"
			            onmouseout="this.className='ur_button';"
		                <c:if test="${!ir:hasPermission('ADMINISTRATION', destination)}">
		                    disabled="disabled"
		                </c:if>
			            >Move<span class="pageWhiteGoBtnImg">&nbsp;</span>
			    </button>
		   </td>
		   <td width="400px" align="left" valign="top">
		       <form name="viewChildContentsForMove"
	              id="move_institutional_collection_form"><input type="hidden"
	              id="destination_id" name="destinationId" value="${destinationId}" />
	              <input type="hidden" name="parentCollectionId" value="${parentCollectionId}" /> 
	
	               <c:forEach items="${collectionsToMove}" var="collection">
	                   <input type="hidden" value="${collection.id}" name="collectionIds" />
                   </c:forEach> 
 
                   <c:forEach items="${itemsToMove}" var="item">
	                   <input type="hidden" value="${item.id}" name="itemIds" />
                   </c:forEach>
    
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
				           <c:if test="${ir:canMoveToCollection(collectionsToMove, fileSystemObject)}">
					            <td><span class="worldBtnImg"></span><a
						            href="javascript:YAHOO.ur.institution.getMoveCollection(${fileSystemObject.id});">${fileSystemObject.name}</a></td>
				           </c:if>
				           <c:if test="${!ir:canMoveToCollection(collectionsToMove, fileSystemObject)}">
					
					           <td class="errorMessage"><span class="worldBtnImg"></span>${fileSystemObject.name} [Moving]</td>
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
