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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Upload files</title>
    
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>
    <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>
        
    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <ur:js src="page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"/>    
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
    
     <!-- Source File -->
    <ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="pages/js/base_path.js"/>
    <ur:js src="page-resources/js/user/upload_files.js"/>
</head>

<body>
 
 <body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
      
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <h3>Upload file(s) to: <c:if test="${folderId > 0}">${personalFolder.fullPath}</c:if><c:if test="${folderId <=0 }">${user.username}</c:if></h3>
  
        <div id="bd">  
            <c:url var="myFoldersUrl" value="/user/workspace.action"/>
            
            <c:forEach var="fileUploadInfo" items="${filesNotAdded}">
                <p class="errorMessage">
                    The file: "${fileUploadInfo.fileName}" already exits in this folder <br/>
                </p>
            </c:forEach>

            <c:forEach var="illegalFileName" items="${illegalFileNames}">
                <p class="errorMessage">
                    The file name: "${illegalFileName.fileName}" contains unsupported special characters. <br/>
                </p>
            </c:forEach>
            
            <!--  this form is for the cancel action -->
            <form method="get" action="${myFoldersUrl}" name="cancelAddFilesForm">
	            <input type="hidden" value="${folderId}" name="parentFolderId"/>
	        </form>
            
	        <ur:basicForm id="fileInfo" name="newFiles" enctype="multipart/form-data" 
	            action="user/uploadFiles.action">
	           <input type="hidden" id="file_upload_table_id" value="1"/>
	
	          <button type="button" class="ur_button" 
	              onmouseover="this.className='ur_buttonover';"
 		          onmouseout="this.className='ur_button';"
 		          onclick="javascript:YAHOO.ur.file.upload.addFileSets(1, false)">Add Another</button>
	          
	          <input type="submit" class="ur_button" 
	              onmouseover="this.className='ur_buttonover';"
 		          onmouseout="this.className='ur_button';"  value="Upload Files"/>
	          
	          <button type="button" class="ur_button" 
	                 onmouseover="this.className='ur_buttonover';"
 		             onmouseout="this.className='ur_button';"
 		             onclick="javascript:document.cancelAddFilesForm.submit();">Cancel</button>
	           <input type="hidden" name="folderId" value="${folderId}"/>
               
               
               
               <table class="formTable, uploadFile" id="fileTable">
	           </table>
               <!--  this table is built dynamically -->
               <div id="file_forms">
               </div>
     
	           <button type="button" class="ur_button" 
	               onmouseover="this.className='ur_buttonover';"
 		           onmouseout="this.className='ur_button';"
 		           onclick="javascript:YAHOO.ur.file.upload.addFileSets(1, false)">Add Another</button>

	           <input type="submit" class="ur_button" 
	               onmouseover="this.className='ur_buttonover';"
 		           onmouseout="this.className='ur_button';"  value="Upload Files"/>
	           
	           <button type="button" class="ur_button" 
	               onmouseover="this.className='ur_buttonover';"
 		           onmouseout="this.className='ur_button';"
 		           onclick="javascript:document.cancelAddFilesForm.submit();">Cancel</button>
	        
	         </ur:basicForm>
             <!--  end body div -->
      </div>
      <!--  end body div -->
      
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->
</body>
</html>