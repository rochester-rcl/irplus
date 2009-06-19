
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
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title>Preview Publication: ${item.name}</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!--  css styles from yahoo -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/> 
	    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
	    <ur:styleSheet href="page-resources/css/base-ur.css"/>        
	    
	    <ur:styleSheet href="page-resources/css/global.css"/>
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

 	    <ur:js src="page-resources/js/user/preview_publication.js"/> 
 	    
 	</head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">

                <div class="clear">&nbsp;</div>
				<p><strong> Preview Publication :  </strong> <span class="noBorderTableGreyLabel">${personalItem.fullPath}${item.name} </span> </p>

				<table width="735"  align="center"  height="48" >
                  	<tr>                                           
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                          <a href="javascript:YAHOO.ur.item.preview.viewAddFiles();"> Add Files</a>
                      </td>
                      
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                           <a href="javascript:YAHOO.ur.item.preview.viewAddMatadata();">Add Information</a>
                      </td>
                      
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                          <a href="javascript:YAHOO.ur.item.preview.viewContributors();"> Add Contributors</a>
                      </td>
                      
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/bluearrow.jpg">
                          Preview Publication
                      </td> 
                  </tr>
                </table>
				
				<table width="100%">
                  <tr>
                                           
                      <td align="left" width="100">
                          <button class="ur_button" id="saveItemMetadata" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.preview.finishLater();">Finish Later</button>
                      </td>
                      <td align="right">
                          <button class="ur_button" id="goto_previous" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.preview.viewContributors()";>Previous</button>
                      </td> 
                      
                      <c:if test="${institutionalItemId == null}">                           
	                      <td align="right" width="200">
	                          <button class="ur_button" id="goto_next" 
	                               onmouseover="this.className='ur_buttonover';"
	                               onmouseout="this.className='ur_button';"
	                               onclick="javascript:YAHOO.ur.item.preview.submitItem();">Submit to Collection</button>
	                      </td>
	                  </c:if>
                  </tr>
                </table>

            	<br/>
            	
            	<form name="previewForm" method="post">
            		<input type="hidden" name="genericItemId" value="${item.id}"/>
            		<input type="hidden" name="parentCollectionId" value="${parentCollectionId}"/>
            		<input type="hidden" name="institutionalItemId" value="${institutionalItemId}"/>
            	</form>
				
				<!-- Begin - Display the Item preview -->
				
	
				<a href="javascript:YAHOO.ur.item.preview.viewAddFiles();"> Edit Files</a>
				<c:import url="/pages/item/item_files_frag.jsp">
				    <c:param name="isPreview" value="true"/>
				</c:import>
			
				<a href="javascript:YAHOO.ur.item.preview.viewAddMatadata();">Edit information</a>
			    <c:import url="/pages/item/item_metadata_frag.jsp"/>
	
				<!-- End - Display the Item preview -->
       	        
       	    <br/>
       	    <br/>
			<table width="100%">
              <tr>
                  <td align="left" width="100">
                       <button class="ur_button" id="saveItemMetadata" 
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';"
                           onclick="javascript:YAHOO.ur.item.preview.finishLater();">Finish Later</button>
                  </td>
                  <td align="right">
                      <button class="ur_button" id="gotoNext" 
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';"
                           onclick="javascript:YAHOO.ur.item.preview.viewContributors()";>Previous</button>
                  </td>                      
                 
                 <c:if test="${institutionalItemId == null}">      
	                  <td align="right" width="200">
	                      <button class="ur_button" id="goto_next" 
	                           onmouseover="this.className='ur_buttonover';"
	                           onmouseout="this.className='ur_button';"
	                           onclick="javascript:YAHOO.ur.item.preview.submitItem();">Submit to Collection</button>
	                  </td>
				  </c:if>
              </tr>
            </table>
			                
            	
	    </div>
	    <!--  end the body tag --> 
	
	    <!--  this is the footer of the page -->
	    <c:import url="/inc/footer.jsp"/>
	        
	    </div>
	    <!-- end doc -->
    </body>
</html>

    
