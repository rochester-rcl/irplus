
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

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp" />

<fmt:setBundle basename="messages" />

<html>
<head>

    <title>Move Personal Publication Information</title>
    <c:import url="/inc/meta-frag.jsp" />

    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>

    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
 
    
    <!--  Style for dialog boxes -->
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>

    <!-- Source File -->
    <ur:js src="page-resources/js/menu/main_menu.js"/>

    <!--  base path information -->
    <ur:js src="page-resources/js/util/base_path.jsp" />
    <ur:js src="page-resources/js/util/ur_util.js" />
    <ur:js src="page-resources/js/user/move_personal_collection.js" />
</head>

<body class="yui-skin-sam">
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2"><!--  this is the header of the page --> 
        <c:import url="/inc/header.jsp" /> 

        <h3>Move Personal Publication Information</h3>

        <!--  this is the body regin of the page -->
        <div id="bd">
                <c:url var="cancelUrl" value="/user/workspace.action">
        <c:param name="parentCollectionId" value="${parentCollectionId}"/>
        <c:param name="tabName" value="COLLECTION"/>
    </c:url>
    
    <button class="ur_button"
			onmouseover="this.className='ur_buttonover';"
			onmouseout="this.className='ur_button';"
			onclick="location.href='${cancelUrl}';">Cancel</button>
    
    <h3 class="errorMessage"><ir:printError key="moveError" errors="${fieldErrors}"></ir:printError></h3>

    <table>
        <tr>
		    <td width="400px" align="left" valign="top"></td>
		    <td width="100px"></td>
		    <td width="400px" align="left" valign="top">
		
		
		        <div id="destination_path">
		            Move To Location: /<ur:a href="javascript:YAHOO.ur.collection.move.getMoveCollection('0');">My Publications</ur:a>/
		            <c:forEach var="collection" items="${destinationPath}">
			            <ur:a href="javascript:YAHOO.ur.collection.move.getMoveCollection('${collection.id}')">${collection.name}</ur:a>/
                    </c:forEach>
                </div>
            
                <br/>
                <br/>                                 
            
            </td>
	    </tr>
	    <tr>
		    <td width="400px" align="left" valign="top">
		        <div class="dataTable">
		        <table width="400px">
			        <thead>
				        <tr>
					        <td>Folders &amp; Publications to Move</td>
				        </tr>
			        </thead>
			        <tbody>
			            
				        <c:forEach items="${collectionsToMove}" varStatus="status" var="collection">
				            <c:if test="${ (status.count % 2) == 1}">
                                <c:set value="even" var="rowType"/>
                                <c:set value="this.className='even'" var="onmouseout"/>
                            </c:if>
                            <c:if test="${ (status.count % 2) == 0}">
                                <c:set value="odd" var="rowType"/>
                                <c:set value="this.className='odd'" var="onmouseout"/>
                            </c:if>
					        <tr onmouseout="${onmouseout}" onmouseover="this.className='highlight'" class="${rowType}">
						        <td><span class="folderBtnImg">&nbsp;</span>${collection.name}</td>
					        </tr>
					        <c:if test="${ (status.count % 2) == 1}">
					            <c:set value="0" var="modEven"/>
					            <c:set value="1" var="modOdd"/>
					        </c:if>
					        <c:if test="${(status.count % 2) == 0}">
					            <c:set value="1" var="modEven"/>
					            <c:set value="0" var="modOdd"/>
					        </c:if>
				        </c:forEach>
				        
                        <c:if test="${ur:isEmpty(collectionsToMove)}">
                             <c:set value="1" var="modEven"/>
					         <c:set value="0" var="modOdd"/>
                        </c:if>
				        
				        <c:forEach items="${itemsToMove}" varStatus="status" var="item">
				            <c:if test="${ (status.count % 2) == modEven}">
                                <c:set value="even" var="rowType"/>
                                <c:set value="this.className='even'" var="onmouseout"/>
                            </c:if>
                            <c:if test="${ (status.count % 2) == modOdd}">
                                <c:set value="odd" var="rowType"/>
                                <c:set value="this.className='odd'" var="onmouseout"/>
                            </c:if>
					        <tr onmouseout="${onmouseout}" onmouseover="this.className='highlight'" class="${rowType}">
						        <td><span class="packageBtnImg">&nbsp;</span>${item.name}</td>
					        </tr>
				        </c:forEach>
			        </tbody>
		        </table>
		        </div>
		        
		    </td>
		    <td width="100px" align="center" valign="top">
		
		        <button class="ur_button" id="move_button"
			            onclick="javascript:YAHOO.ur.collection.move.moveCollection();"
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
 
                   <c:forEach items="${itemsToMove}" var="item">
	                   <input type="hidden" value="${item.id}" name="itemIds" />
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
                            <c:if test="${fileSystemObject.fileSystemType.type == 'personalCollection'}">
                               <c:set var="canMove" value="${ir:canMoveToFolder(collectionsToMove, fileSystemObject)}"/>
					           <c:if test="${canMove}">
						            <span class="folderBtnImg">&nbsp;</span><a
							            href="javascript:YAHOO.ur.collection.move.getMoveCollection(${fileSystemObject.id});">${fileSystemObject.name}</a>
					           </c:if>
					           <c:if test="${!canMove}">
						            <span class="folderBtnImg">&nbsp;</span><span class="errorMessage">${fileSystemObject.name} [Moving]</span>
					           </c:if>
					       </c:if>
			           	   <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
			           	       <c:set var="beingMoved" value="${ir:isFileToBeMoved(itemsToMove, fileSystemObject)}"/>
					           <c:if test="${!beingMoved}">
						            <span class="packageBtnImg">&nbsp;</span>${fileSystemObject.name}
						       </c:if>
					           <c:if test="${beingMoved}">
						            <span class="packageBtnImg">&nbsp;</span><span class="errorMessage">${fileSystemObject.name} [Moving]</span>
						       </c:if>						       
					       </c:if>					 
                          </urstb:td>
                      </urstb:tr>
                  </urstb:tbody>
              </urstb:table>
              </div>
               
		   </td>
	   </tr>
   </table>

            <!--  end the body tag --> <!--  this is the footer of the page --> 
            <c:import url="/inc/footer.jsp" />
        </div>
    </div>
    <!-- end doc -->
</body>
</html>