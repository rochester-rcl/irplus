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

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>


<html>
    <head>
        <title>Add Files To Publication: ${item.fullName}</title>
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

		<!--  Style for dialog boxes -->
	    <style>
	        .statusBar { background-image:url(/page-resources/images/all-images/status_bar_add_files.gif)}
	        .button_height { height: 28px; }
	    </style>
	    
        <!-- Source File -->
        <ur:js src="page-resources/yui/menu/menu-min.js"/>
        <ur:js src="page-resources/js/menu/main_menu.js"/>
             
        <!--  base path information -->
        <ur:js src="page-resources/js/util/base_path.jsp"/>
        <ur:js src="page-resources/js/util/ur_util.js"/>
  		<ur:js src="page-resources/js/user/add_item.js"/>
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">
            
            	<div class="clear">&nbsp;</div>
				<p><strong> Adding Files To Publication :  </strong> <span class="noBorderTableGreyLabel">${personalItem.fullPath}${item.fullName} </span> </p>
				
				<table width="735"  align="center"  height="48" >
                  	<tr>
                                           
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/bluearrow.jpg">
                          Add Files
                      </td>
                      
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                          <a href="javascript:YAHOO.ur.item.viewMetadata();"> Add Information </a>
                      </td>
                      
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                         <a href="javascript:YAHOO.ur.item.viewContributors();"> Add Contributors</a>
                      </td>

                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                          <a href="javascript:YAHOO.ur.item.previewItem();"> Preview Publication </a>
                      </td>                        
                  </tr>
                </table>
                
				<table width="100%">
                  <tr>
                                           
                      <td align="left" >
                          <button class="ur_button" id="saveItem" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.finishLater();">Finish Later</button>
                      </td>
                      
                      <td align="right" >
                          <button class="ur_button" id="gotoNext" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.viewMetadata()";>Next</button>
                      </td>
                  </tr>
                </table>
                

                   <table width="100%">
                    <tr>
                        <td valign="top" width="470px">
                         <!--  table of files and folders -->
	                      <div id="newPersonalFolders" class="float_left">
	                          <ur:basicForm  id="folders" name="myFolders"  method="POST" action="user/getPersonalFolders.action">
	                              <input type="hidden" id="myFolders_parentFolderId" 
	                                   name="parentFolderId" 
	                                   value="${parentFolderId}"/>
	                              <input type="hidden" id="myFolders_itemId" 
	                                   name="genericItemId" 
	                                   value="${item.id}"/>
	                              <input type="hidden" id="myFolders_parentCollectionId" 
	                                   name="parentCollectionId" 
	                                   value="${parentCollectionId}"/>
	                              <input type="hidden" id="myFolders_institutionalItemId" 
	                                   name="institutionalItemId" 
	                                   value="${institutionalItemId}"/>

	                          </ur:basicForm>
	                      </div>
	                      <!--  end personal files and folders div -->
                        </td>
                        <td width="10px">
                        </td>
                        <td width="470px" valign="top">
                         <button class="ur_button" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               id="showLink"><img  alt="" class="buttonImg" src="${pageContext.request.contextPath}/page-resources/images/all-images/link_add.gif"/>
                               Add New Link</button> 
	                           <br/>
	                           <br/>
        	        	<!--  Table of selected files -->
                    	<div id="newSelectedFiles" >
	                          <ur:basicForm  id="files" name="myFiles"  method="POST" action="user/getSelectedFiles.action">
	                              <input type="hidden" id="myFiles_itemId" 
	                                   name="genericItemId" 
	                                   value="${item.id}"/>
	                              <input type="hidden" id="myFiles_parentCollectionId" 
	                                   name="parentCollectionId" 
	                                   value="${parentCollectionId}"/>
	                              <input type="hidden" id="myFiles_institutionalItemId" 
	                                   name="institutionalItemId" 
	                                   value="${institutionalItemId}"/>

	                          </ur:basicForm>
	                      </div>
	                      <!--  end table of selected files div -->
                        </td>
                    </tr>
                </table>
       	        
	
				
				<div class="clear">&nbsp;</div>
				
				<table width="100%">
                  <tr>
                      <td align="left" >
                          <button class="ur_button" id="saveItem" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.finishLater();">Finish Later</button>
                      </td>
                      
                      <td align="right">
                          <button class="ur_button" id="gotoNext" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.viewMetadata();">Next</button>
                      </td>
                  </tr>
                </table>
        
                
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->

        <div id="newLinkDialog" class="hidden">
            <div class="hd">Link Information</div>
            <div class="bd">
                  <ur:basicForm id="addLink" name="newLinkForm" 
                  method="post" action="user/addItemLink.action">
              
                   <input type="hidden" id="newLinkForm_itemId"
                       name="genericItemId" value="${item.id}"/>      
                    
                   <input type="hidden" id="newLinkForm_itemId"
                       name="institutionalItemId" value="${institutionalItemId}"/>                 
               
                   <input type="hidden" id="newLinkForm_new"
                       name="newLink" value="true"/>
              
                   <input type="hidden" id="newLinkForm_linkId"
                       name="updateLinkId" value=""/>
                   
                  <div id="linkNameError" class="errorMessage"></div>
	              
	              <table class="formTable">
	                  <tr>
	                      <td class="label"> Link Name:*</td>
	                      <td align="left" class="input"> <input id="link_name" size="45" type="text" name="linkName" value=""/></td>
	                  </tr>
	                  <tr>
	                      <td class="label"> Link Description:</td>
	                      <td align="left" class="input"><textarea cols="42" rows="4" name="linkDescription"></textarea></td>
	                  </tr>
	                  <tr>
	                      <td class="label"> Link URL:*</td>
	                      <td align="left" class="input"> <input id="link_url" size="45" type="text" name="linkUrl" value="http://"/></td>
	                  </tr>
	              </table>
                 </ur:basicForm>
           </div>
           <!-- end dialog body -->
       </div>
       <!--  end the new link dialog -->
 
         <div id="addFileErrorDialog" class="hidden">
            <div class="hd">Add file to Item</div>
            <div class="bd">
 				<div id="fileNameErrorDiv"> </div>
            </div>
           <!-- end dialog body -->
       </div>
       <!--  end the file error dialog -->
                
    </body>
</html>

    
