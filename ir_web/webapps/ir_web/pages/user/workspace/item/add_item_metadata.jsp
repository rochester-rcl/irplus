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
        <title>Adding Information to Publication: ${item.name}</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!--  css styles from yahoo -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/> 
	    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
	    <ur:styleSheet href="page-resources/yui/calendar/assets/skins/sam/calendar.css"/> 
	    <ur:styleSheet href="page-resources/css/base-ur.css"/>        
 
        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>

		<!--  Style for dialog boxes -->
	    <style>
	        #containerDialog .bd {padding:0;}  
			#cal1Container {border:none;padding:1em} 
			#containerDialog .bd:after {content:".";display:block;clear:left;height:0;visibility:hidden;} 

	        #containerDialog2 .bd {padding:0;}  
			#cal2Container {border:none;padding:1em} 
			#containerDialog2 .bd:after {content:".";display:block;clear:left;height:0;visibility:hidden;} 

	        #containerDialog3 .bd {padding:0;}  
			#cal3Container {border:none;padding:1em} 
			#containerDialog3 .bd:after {content:".";display:block;clear:left;height:0;visibility:hidden;} 
			

	    </style>
	    
        <!-- Dependencies --> 
    	<ur:js src="page-resources/yui/utilities/utilities.js"/>
        <ur:js src="page-resources/yui/button/button-min.js"/>
	    <ur:js src="page-resources/yui/container/container-min.js"/>
	    <ur:js src="page-resources/yui/menu/menu-min.js"/>
	    <ur:js src="page-resources/yui/calendar/calendar-min.js"/>

        <!-- Source File -->
        <ur:js src="pages/js/base_path.js"/>        
        <ur:js src="page-resources/js/util/ur_util.js"/>
        <ur:js src="page-resources/js/menu/main_menu.js"/>
 	    <ur:js src="page-resources/js/user/add_item_metadata.js"/>
 	    
 	</head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">

                <div class="clear">&nbsp;</div>
				<p><strong> Adding Information to Publication :  </strong> <span class="noBorderTableGreyLabel"><c:out value='${personalItem.fullPath}'/><c:out value='${item.name}'/> </span> </p>

				<table width="735"  align="center"  height="48" >
                  	<tr>                                           
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                          <a href="javascript:YAHOO.ur.item.metadata.showAddFiles();"> Add Files</a>
                      </td>
                      
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/bluearrow.jpg">
                          Add Information
                      </td>
                      
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                          &nbsp;&nbsp;&nbsp; <a href="javascript:YAHOO.ur.item.metadata.saveMetadataAndViewContributors();"> Add Contributors</a>
                      </td>
                      
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                          <a href="javascript:YAHOO.ur.item.metadata.gotoPreview();"> Preview Publication </a>
                      </td> 
                  </tr>
                </table>
				
				<table width="100%">
                  <tr>
                                           
                      <td align="left" width="100">
                          <button class="ur_button" id="saveItemMetadata" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.metadata.saveItemMetadata();">Finish Later</button>
                      </td>
                      <td align="right">
                          <button class="ur_button" id="goto_previous" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.metadata.showAddFiles()";>Previous</button>
                      </td>                      
                      <td align="right" width="100">
                          <button class="ur_button" id="gotoNext" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.metadata.saveMetadataAndViewContributors();">Next</button>
                      </td>
                  </tr>
                </table>            	
				
				<!-- Begin - Display the Item files preview -->
				
				<table class="noBorderTable" width="100%">
					<tr>
						<td width="100" class="noBorderTabletd">
						</td>
						<td class="noBorderTabletd">
							<table class="greyTable">
								<c:forEach items="${itemObjects}" var="object">
								<tr>
									<td width="10" class="noBorderTabletd">
										
									</td>
									<td class="bottomBorder">
										<c:if test="${object.type == 'FILE'}">
											<ir:fileTypeImg cssClass="tableImg" irFile="${object.irFile}"/> <ur:maxText numChars="40" text="${object.irFile.name}"></ur:maxText>
										</c:if>
										<c:if test="${object.type == 'URL'}">
											<img  alt="" class="tableImg" src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/>
    										<ur:maxText numChars="40" text="${object.name}"></ur:maxText>
										</c:if>
									</td>
									<td class="bottomBorder">
										${object.description}
									</td>
									<td width="10" class="noBorderTabletd">
										
									</td>
								</tr>
								</c:forEach>
							</table>
						</td>
					</tr>
				</table>
				<!-- End - Display the Item files preview -->
       	        
       	        <ur:basicForm id="add_item_metadata" name="itemForm" enctype="multipart/form-data"  method="post">
					<input type="hidden" id="series_table_id" value="${reportsCount}"/>
					<input type="hidden" id="identifier_table_id" value="${itemIdentifiersCount}"/>
					<input type="hidden" id="extent_table_id" value="${itemExtentsCount}"/>
					<input type="hidden" id="sponsor_table_id" value="${itemSponsorsCount}"/>
					<input type="hidden" id="title_table_id" value="${subTitlesCount}"/>
					<input type="hidden" name="genericItemId" value="${genericItemId}"/>
					<input type="hidden" name="parentCollectionId" value="${parentCollectionId}"/>
					<input type="hidden" name="institutionalItemId" value="${institutionalItemId}"/>
					       	        
       	        	<ur:div id="item_metadata_form">
       	        	    <c:import url="item_metadata_form.jsp"/>
					</ur:div>
				</ur:basicForm>
			   
			
			<div class="clear">&nbsp;</div>
				
			<table width="100%">
              <tr>
                  <td align="left" width="100">
                       <button class="ur_button" id="saveItemMetadata" 
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';"
                           onclick="javascript:YAHOO.ur.item.metadata.saveItemMetadata();">Finish Later</button>
                  </td>
                  <td align="right">
                      <button class="ur_button" id="gotoNext" 
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';"
                           onclick="javascript:YAHOO.ur.item.metadata.showAddFiles()";>Previous</button>
                  </td>                      
                  
                  <td align="right" width="100">
                      <button class="ur_button" id="gotoNext" 
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';"
                           onclick="javascript:YAHOO.ur.item.metadata.saveMetadataAndViewContributors();">Next</button>
                  </td>
              </tr>
            </table>
			     
	           
		<div id="newSeriesDialog" class="hidden">
                <div class="hd">Series Information</div>
                <div class="bd">
                    <form id="addSeries" name="newSeriesForm" 
		                    method="post" 
		                    action="<c:url value="/user/addSeries.action"/> ">
	                  <div id="newSeriesDialogFields">
	                      <c:import url="series_form.jsp"/>
	                  </div>
	                </form>
                </div>
        </div>
 
 		<div id="newSponsorDialog" class="hidden">
                <div class="hd">Sponsor Information</div>
                <div class="bd">
                    <form id="addSponsor" name="newSponsorForm" 
		                    method="post" 
		                    action="<c:url value="/user/addSponsor.action"/>">
	                  <div id="newSponsorDialogFields">
	                       <c:import url="/pages/admin/item/metadata/sponsors/sponsor_form.jsp"/>
	                  </div>
	                </form>
                </div>
         </div>

        <div id="newIdentifierTypeDialog" class="hidden">
            <div class="hd">Identifier Type Information</div>
	        <div class="bd">
	            <form id="addIdentifierType" name="newIdentifierType" method="POST" 
	                  action="<c:url value="/user/addIdentifierType.action"/>">
	              
	                <input type="hidden" id="newIdentifierTypeForm_id"
	                    name="id" value=""/>
	               
	                <input type="hidden" id="newIdentifierType_new"
	                    name="newIdentifierType" value="true"/>
	              
	                <div id="identifierTypeError"class="errorMessage"></div>

				<table class="formTable">    
				    <tr>
					    <td align="left" class="label">Name:*</td>
			            <td align="left" class="input"><input type="text" 
					         id="newIdentifierTypeForm_name" name="identifierType.name" value=""
					        size="45"/> </td>
			         </tr>
			         <tr>
			             <td align="left" class="label">Description:</td>
			             <td colspan="2" align="left" class="input"><textarea id="newIdentifierTypeForm_description"
				                  name="identifierType.description" cols="42" rows="4"></textarea></td>
			         </tr>
			     </table>
	            </form>
	        </div>
         </div>
         
        <div id="newExtentTypeDialog" class="hidden">
            <div class="hd">Extent Type Information</div>
	        <div class="bd">
	            <form id="addExtentType" name="newExtentType" method="POST" 
	                  action="<c:url value="/user/addExtentType.action"/>">
	              
	                <input type="hidden" id="newExtentTypeForm_id"
	                    name="id" value=""/>
	               
	                <input type="hidden" id="newExtentType_new"
	                    name="newExtentType" value="true"/>
	              
	                <div id="extentTypeError" class="errorMessage"></div>

				<table class="formTable">    
				    <tr>
					    <td align="left" class="label">Name:*</td>
			            <td align="left" class="input"><input type="text" 
					         id="newExtentTypeForm_name" name="extentType.name" value=""
					        size="45"/> </td>
			         </tr>
			         <tr>
			             <td align="left" class="label">Description:</td>
			             <td colspan="2" align="left" class="input"><textarea id="newExtentTypeForm_description"
				                  name="extentType.description" cols="42" rows="4"></textarea></td>
			         </tr>
			     </table>
	            </form>
	        </div>
         </div>         
      
	        <div id="newPublisherDialog" class="hidden">
                <div class="hd">Publisher Information</div>
                <div class="bd">
                    <form id="addPublisher" name="newPublisherForm" 
		                    method="post" 
		                    action="<c:url value="/user/addPublisher.action"/>">
	                  <div id="newPublisherDialogFields">
	                       <c:import url="/pages/admin/item/metadata/publishers/publisher_form.jsp"/>
	                  </div>
	                </form>
                </div>
            </div>
            	
	    </div>
	    <!--  end the body tag --> 
	
	    <!--  this is the footer of the page -->
	    <c:import url="/inc/footer.jsp"/>
	        
	    </div>
	    <!-- end doc -->
    </body>
</html>

    
