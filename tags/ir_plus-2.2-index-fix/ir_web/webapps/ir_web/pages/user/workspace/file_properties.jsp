
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ir" uri="ir-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>


<html>

<head>
    <title>Properties for file ${personalFile.versionedFile.nameWithExtension}</title>
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>

    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <!--  required imports -->
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
    <ur:js src="page-resources/yui/menu/menu-min.js"/>
    
 	<!--  base path information -->
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="page-resources/js/user/file_properties.js"/>
</head>

<body id="body" class="yui-skin-sam">

    
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">  
 
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
  
        <!--  this is the body regin of the page -->
        <div id="bd">
        
             <div id="file_properties">
                 <c:import url="file_properties_inner_page.jsp"/>
             </div>
             <!-- end the file properties div -->
            
	    </div>
	    <!--  end body tag -->
	  
	  <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
      
    </div>
    <!--  end doc tag -->
	          
	<!-- Dialog box for uploading a file -->
    <div id="versionedFileUploadDialog" class="hidden">
	    <div class="hd">New Version Upload</div>
		<div class="bd">
		           
		<form id="versionedFileUploadForm" name="versionedFileUploadForm" 
		    method="post" enctype="multipart/form-data"
		    action="user/singleFileUpload.action">
		                 
		    <div id="version_upload_form_fields">
		        <c:import url="/pages/user/workspace/upload_new_file_version.jsp"/>
		    </div>
		</form>
		</div>
	</div>
	<!--  end file upload dialog -->  
	          
	<div id="error_dialog_box" class="hidden">
	    <div class="hd">Error</div>
		<div class="bd">
		    <div id="default_error_dialog_content">
		    </div>
		 </div>
	 </div>
	            
	 <!--  invite confirmation dialog -->
	 <div id="inviteConfirmDialog" class="hidden">
	     <div class="hd">Invite?</div>
		 <div class="bd">

 		     <form id="invite_files_form" name="inviteFilesForm" 
		         method="post" enctype="multipart/form-data"
		        action="user/viewInviteUser.action">		          
			    <div id="invite_form_fields">
			        <c:import url="/pages/user/workspace/invite_files_confirmation.jsp"/>
			    </div>
		    </form>
		              
         </div>
	 </div>
	 <!--  end invite confirmation dialog -->

	 <!--  new publication version dialog -->
	 <div id="newPublicationVersionConfirmation" class="hidden">
	     <div class="hd">Create new version?</div>
		 <div class="bd">
		     <form id="new_version_form" name="newVersionForm" 
		         method="post" enctype="multipart/form-data">		
		         
		         <input type="hidden" name="itemId"/>          
		     </form>		          
		     <p>This publication version is published to Institutional Collection. 
		        To publish to more collections choose 'Submit publication' link.
		        <div class="clear">&nbsp;</div>
		        If you want to edit this publication, a new version will be created.
		        Do you want to create new version for editing?
		     </p>
		 </div>
	 </div>
	 <!--  end new publication version dialog -->

	 <!--  Rename file dialog -->
	 <div id="renameFileDialog" class="hidden">
	     <div class="hd">Rename file</div>
		 <div class="bd">
		     <form id="rename_form" name="renameForm" 
		         method="post" enctype="multipart/form-data">		
		         <p>

	              	<div id="renameFileDialogFields">
	              	    <c:import url="/pages/user/workspace/rename_file_form.jsp"/>
	              	</div>		             
		         </p>
		     </form>		          
		 </div>
	 </div>
	 <!--  end rename file dialog -->

	 <!--  Change owner dialog -->
	 <div id="changeOwnerDialog" class="hidden">
	     <div class="hd">Change owner</div>
		 <div class="bd">
		     <form id="owner_form" name="changeOwnerForm" 
		         method="post" enctype="multipart/form-data">
		         
		        <input type="hidden" name="personalFileId" value="${personalFileId}"/>        		
				New Owner :
				<select name="newOwnerId">
					<option value="${personalFile.versionedFile.owner.id}"> ${personalFile.versionedFile.owner.firstName}&nbsp;${personalFile.versionedFile.owner.lastName} </option>
					<c:forEach var="collaborator" items="${personalFile.versionedFile.collaborators}">
						<option value="${collaborator.collaborator.id}">${collaborator.collaborator.firstName}&nbsp;${collaborator.collaborator.lastName}</option>
					</c:forEach>
				</select>
		     </form>		          
		 </div>
	 </div>
	 <!--  Change owner dialog -->
	 
</body>
</html>