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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<fmt:setBundle basename="messages"/>

<html>
<head>
        
   <title>Institutional Collections</title>
   <c:import url="/inc/meta-frag.jsp"/>
        
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
 	<ur:js src="page-resources/yui/tabview/tabview-min.js"/>
 	
    
    <ur:js src="pages/js/base_path.js"/>
    <ur:js src="page-resources/js/util/ur_util.js"/>
    <ur:js src="page-resources/js/menu/main_menu.js"/>  
	<ur:js src="pages/js/ur_table.js"/>
    <ur:js src="page-resources/js/admin/institutional_collection.js"/>
    
    
    <!--  Style for dialog boxes -->
    <style>
 
        /* this is a simple fix for geco based browsers
         * this does have side affects if scroll bars are used.
         * in geco based browsers see cursor fix on yahoo
         * http://developer.yahoo.com/yui/container/
         */
        .mask 
        {
            overflow:visible; /* or overflow:hidden */
        }
    </style>
        

</head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">
                <h3>Institutional Collection Administration</h3>
                <c:url var="browseCollections" value="/admin/viewInstitutionalCollections.action">
                    <c:param name="parentCollectionId" value="${parentCollectionId}"/>
                </c:url>
                <c:url var="searchCollections" value="/admin/searchInstitutionalCollections.action"/>
                
                <!--  set up tabs for the workspace -->
                <div id="collection-tabs" class="yui-navset">
	                <ul class="yui-nav">
	                 <c:if test='${viewType == "browse"}'>
		                    <li class="selected"><a href="${browseCollections}"><em>Browse</em></a></li>
		                    <li><a href="#tab2"><em>Search</em></a></li>
		               </c:if>
		               <c:if test='${viewType == "search"}'>
		                    <li><a href="${browseCollections}"><em>Browse</em></a></li>
		                    <li class="selected"><a href="#tab2"><em>Search</em></a></li>
		               </c:if>
	                </ul>
	                
	                
	                <div class="yui-content">
	                    <!--  first tab -->
	                    <div id="tab1">
                            <c:import url="institutional_collections_table.jsp"/>
                        </div>
                        <!--  end first tab -->
                        
                        <!--  Second tab -->
                        <div id="tab2">
                            <form method="post" 
                                  id="collection_search_form" 
                                  name="collectionSearchForm" 
                                  action="${searchCollections}" >
	            		
	            		     <br/>
						         Search Collections : <input type="text" name="query" size="50"/>
						         <button id="search_user" class="ur_button" type="submit"
		                                 onmouseover="this.className='ur_buttonover';"
		                                 onmouseout="this.className='ur_button';">Search</button>
					        </form>
					        <br/>
	                        <br/>
	                        <c:if test='${viewType == "search" && !searchInit}'>
					            <c:import url="institutional_collection_search_table.jsp"/>
					        </c:if>
                        </div>
                    </div>
                    <!--  end tabs -->
                    
                </div>  
                <!--  tabs -->    
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
  
        <div id="newCollectionDialog" class="hidden">
	        <div class="hd">New Collection</div>
		    <div class="bd">
		        <form name="newCollectionForm" method="post" 
		                  action="<c:url value="/admin/createInstitutionalCollection.action"/>">
		                  <input type="hidden" id="newCollectionForm_parentCollectionId" 
		                      name="parentCollectionId" value="${parentCollectionId}"/>
		               
		                  <div id="collectionNameError" class="errorMessage"></div>
		                  <table class="formTable">
		                      <tr>
		                          <td class="label">Collection Name:</td>
		                          <td class="input"><input type="text" size="50" name="collectionName" value=""/></td>
		                      </tr>
		                  </table>
		        </form>
		    </div>
	    </div> 
	    
	    <div id="deleteCollectionDialog" class="hidden">
	        <div class="hd">Delete Collection?</div>
		    <div class="bd">
		         Are you sure you wish to delete the selected collections?
		    </div>
	    </div>   
    </body>
</html>