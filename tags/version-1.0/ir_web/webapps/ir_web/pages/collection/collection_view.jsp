
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title>Institutional Collection: ${institutionalCollection.name}</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!-- Core + Skin CSS -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
        <ur:styleSheet href="page-resources/css/base-ur.css"/>
        <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>

        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/home_page_content_area.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>

        <!-- Dependencies --> 
        <ur:js src="page-resources/yui/utilities/utilities.js"/>
        <ur:js src="page-resources/yui/button/button-min.js"/>
        <ur:js src="page-resources/yui/container/container-min.js"/>
 	    <ur:js src="page-resources/yui/menu/menu-min.js"/>
        
        <!-- Source File -->
        <ur:js src="page-resources/js/menu/main_menu.js"/>
        <ur:js src="pages/js/base_path.js"/>
        <ur:js src="page-resources/js/util/ur_util.js"/>
 	    <ur:js src="page-resources/js/public/collection_view.js"/>
      
        <!--  styling for page specific elements -->
        <style type="text/css">
            .ur_button
            {
                width: 100px;
                margin: 0px;
                padding: 0px;
            }
            
            .ur_buttonover
            {
                width: 100px;
                margin: 0px;
                padding: 0px;
            }
        </style>
    </head>
    
    <body class="yui-skin-sam">
        <input type="hidden" id="current_collection_id" value="${institutionalCollection.id}"/>
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body regin of the page -->
            <div id="bd">
                <div id="collection_top_info">
                    <h3 class="collection_path"><a href="home.action">${repository.name}</a> >
                                <c:forEach var="collection" items="${collectionPath}">
                                    <c:url var="pathCollectionUrl" value="/viewInstitutionalCollection.action">
                                         <c:param name="collectionId" value="${collection.id}"/>
                                    </c:url>
                                    <c:if test="${collection.id != institutionalCollection.id}">
                                        <a href="${pathCollectionUrl}">${collection.name}</a>  >
                                    </c:if>
                                    <c:if test="${collection.id == institutionalCollection.id}">
                                    ${collection.name}
                                    </c:if>
                                </c:forEach>
                                <c:if test="${user != null && ir:userHasRole('ROLE_ADMIN', 'OR')}">
                                    <c:url var="editCollection" value="/admin/viewInstitutionalCollection">
                                        <c:param name="collectionId" value="${institutionalCollection.id}"/>
                                    </c:url>
                                    &nbsp;<a href="${editCollection}">(Edit Collection)</a>
                                </c:if>
                    </h3>
                </div>
                <!--  create the first column -->
                <div class="yui-g">
                <div class="yui-u first">
                   <div class="contentContainer">
                       <div class="contentBoxTitle">
                           <p>Browse/Search</p>
                       </div>
                   
                       <div class="contentBoxContent">
                           <c:url var="searchCollectionItems" value="/searchCollectionItems.action"/>
                           <form method="GET" action="${searchCollectionItems}">
                                <p>Search: <input type="text" name="query" size="50"/>
                                <br/>
                                <br/>
                                <button type="submit" class="ur_button" 
		                               onmouseover="this.className='ur_buttonover';"
	 		                           onmouseout="this.className='ur_button';">Search</button>
                                <input type="hidden" name="collectionId" value="${institutionalCollection.id}"/>
                          
                                <br/>
                                <br/>
	 		                      <c:url var="browseCollectionItems" value="/browseCollectionItems.action">
                                       <c:param name="collectionId" value="${institutionalCollection.id}"/>
                                  </c:url>
	 		                      <a href="${browseCollectionItems}"><strong>Browse All/Search</strong></a>
	 		                      </p>
                           </form>
                           
                       </div>
                   </div>
                   
                   
                   <div class="contentContainer">
                       <div class="contentBoxTitle">
                           <p>About Collection</p>
                       </div>
                   
                       <div class="contentBoxContent">
                           <p>${institutionalCollection.description}</p>
                       </div>
                   </div>
                    
                   <div class="contentContainer">
                        <div class="contentBoxTitle">
                            <p>Sub Collections</p>
                        </div>
                        <div id="institutional_collections" class="contentBoxContent">
                            <c:if test="${!ur:isEmpty(nameOrderedChildren)}">
                            <table class="baseTable">
                            <c:forEach items="${nameOrderedChildren}" var="child" >
                                <c:url var="institutionalCollectionUrl" value="/viewInstitutionalCollection.action">
                                    <c:param name="collectionId" value="${child.id}"/>
                                </c:url>
                                <tr>
                                   <td class="baseTableImage">
                                       <c:if test="${ir:hasThumbnail(child.primaryPicture)}">
                                           <c:url var="url" value="/institutionalCollectionThumbnailDownloader.action">
                                                <c:param name="collectionId" value="${child.id}"/>
                                                <c:param name="irFileId" value="${child.primaryPicture.id}"/>
                                           </c:url>
                                           <img height="66px" width="100px"  src="${url}"/>
                                       </c:if>
                                   </td>
                                   <td>
                                       <p><strong><a href="${institutionalCollectionUrl}">${child.name}</a></strong></p>
                                    </td>
                                </tr>
                            </c:forEach>
                            </table>
                            </c:if>
                            
                            <c:if test="${ur:isEmpty(nameOrderedChildren)}">
                                <p>There are no sub collections</p>
                            </c:if>
                        </div>
                   </div>
                    
                </div>
                <!--  end the first column -->
            
                <div class="yui-u">
                    
                     <div class="contentContainer">
                       <div class="contentBoxTitle">
                           <p>Subscribe</p>
                       </div>
                   
                       <div class="contentBoxContent">
		                    <ur:div id="collection_subscription">
			                    <input type="hidden" name="userId" id="user_id" value="${user.id}">

		                    	<c:if test="${user == null}">
									<p> <a href="<c:url value="/user/workspace.action"/>">Login</a> or <a href="<c:url value="viewUserRegistration.action"/>">Create an Account</a> to subscribe to this collection. </p>
								</c:if>	
							</ur:div>
                       </div>
                   </div>
                    
                    <div class="contentContainer">
                        <div class="contentBoxTitle">
                            <p>Pictures</p>
                        </div>
                   
                        <div class="contentBoxContentPicture" id="collection_picture">
                            <p>Pictures will go here</p>
                        </div>
                    </div>
                    
                     <div class="contentContainer">
                       <div class="contentBoxTitle">
                           <p>Links</p>
                       </div>
                   
                       <div class="contentBoxContent">
                           <p>
                           <br/>
                           <c:forEach var="link" items="${institutionalCollection.links}">
                           <a href="${link.url}">${link.name}</a> <br/><br/>
                           </c:forEach>
                           </p>
                       </div>
                   </div>
                    
                    <div class="contentContainer">
                       <div class="contentBoxTitle">
                           <p>Recent Submissions</p>
                       </div>
                   
                       <div class="contentBoxContent">
                           <p>
                           <c:forEach var="item" items="${mostRecentSubmissions}">
                               <c:url var="itemView" value="/institutionalPublicationPublicView.action">
						           <c:param name="institutionalItemId" value="${item.id}"/>
						       </c:url>
                               <a href="${itemView}">${item.versionedInstitutionalItem.currentVersion.item.name}</a> <c:if test="${item.versionedInstitutionalItem.currentVersion.item.description != null}">- <ur:maxText numChars="100" text="${item.versionedInstitutionalItem.currentVersion.item.description}"/> </c:if> - (<fmt:formatDate pattern="yyyy-MM-dd" value="${item.versionedInstitutionalItem.currentVersion.dateOfDeposit}"/>) <br/><br/>
                           </c:forEach>
                           </p>
                       </div>
                   </div>
                   
                   <div class="contentContainer">
                       <div class="contentBoxTitle">
                           <p>Statistics</p>
                       </div>
                   
                       <div class="contentBoxContent">
						<div id="statistics_div"></div>
                       </div>
                   </div>
                </div>
                <!--  end the second column -->
                
                </div>
                <!--  end the grid -->
                
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
