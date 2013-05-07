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
        <title>Welcome</title>
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

	    <ur:js src="page-resources/js/admin/review_item.js"/> 
 	    
 	</head>
    
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">

                <div class="clear">&nbsp;</div>
            	<form name="previewForm" method="post">
            		<input type="hidden" name="reviewableItemId" value="${reviewableItem.id}"/>
            	</form>
				
				<!-- Begin - Display the Item preview -->
				
				<table class="noBorderTable" width="100%">
					<tr>
						<td colspan="2" class="noBorderTabletd">
							<label for="preview" class="noBorderTableLabel">${reviewableItem.item.fullName}   </label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<table class="greyBorderTable">
								<c:import url="/pages/item/item_files_frag.jsp">
				                        <c:param name="isPreview" value="true"/>
				                </c:import>
							</table>
						</td>
					</tr>
			   </table>
              
               <c:url value="editItemMetadata.action" var="editItemMetadata">
			        <c:param name="genericItemId" value="${reviewableItem.item.id}"/>
			     	<c:param name="reviewableItemId" value="${reviewableItem.id}"/>
			 	</c:url>

				<strong> <a href="${editItemMetadata}">Edit metadata</a> </strong>
				<c:import url="/pages/item/item_metadata_frag.jsp"/>
				
			<table width="100%">
              <tr>
                  <td align="left" width="200">
                       <button class="ur_button"  
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';"
                           onclick="javascript:YAHOO.ur.review.item.viewReviewPendingItems();">Back to Pending items</button>
                  </td>
                  <td align="right">
                      <button class="ur_button" 
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';"
                           onclick="javascript:YAHOO.ur.review.item.acceptItem()";>Accept</button>
                  </td>                      
                  
                  <td align="right" width="200">
                      <button class="ur_button" id="showRejection" 
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';">Reject</button>
                  </td>
              </tr>
            </table>
			                
            	
	    </div>
	    <!--  end the body tag --> 
	
	    <!--  this is the footer of the page -->
	    <c:import url="/inc/footer.jsp"/>
	        
	    </div>
	    <!-- end doc -->
	    
        <div id="rejectionDialog" class="hidden">
            <div class="hd">Reject the item</div>
            <div class="bd">
                  <ur:basicForm name="rejectionForm" 
                  method="post" action="admin/rejectReviewableItem.action">
              
                   <input type="hidden" name="reviewableItemId" value="${reviewableItem.id}"/>

	              
	              <table class="formTable">
	                  <tr>
	                      <td class="label"> Reason for rejection:*</td>
	                      <td align="left" class="input"> <textarea id="rejectionForm_reason" cols="42" rows="4" name="reason"></textarea></td>
	                  </tr>
	                 
	              </table>
                 </ur:basicForm>
           </div>
           <!-- end dialog body -->
       </div>
       <!--  end the new link dialog -->	    
    </body>
</html>

    
