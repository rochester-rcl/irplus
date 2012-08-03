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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<fmt:setBundle basename="messages"/>


<html>

<head>
    <title>Auto Share Folder(s) With User</title>
    
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>

    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
	<ur:styleSheet href="page-resources/css/home_page_content_area.css"/>    

    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	

 	<ur:js src="pages/js/base_path.js" />
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/util/wait_dialog.js" />
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 	<ur:js src="page-resources/js/user/auto_share_folder.js"/>
    
</head>

<body  class="yui-skin-sam">
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
      
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <div id="bd">
			
			<h3>Auto Share Folder: ${personalFolder.name}</h3>
			<p>Will automatically share files added to the folder and files currently existing in the folder</p>
	
	        <div class="yui-content">
	            <div class="yui-g">
		                <br/>
						<ur:basicForm id="showWorkspace" name="backToWorkspace" method="POST" action="user/workspace.action">
						    <c:if test="${personalFolder.parent != null }">
							    <input type="hidden" name="parentFolderId" value="${personalFolder.parent.id}">
						    </c:if>
						    <c:if test="${personalFolder.parent == null }">
							    <input type="hidden" name="parentFolderId" value="0">
						    </c:if>
							
							<button class="ur_button" type="submit"
				                   onmouseover="this.className='ur_buttonover';"
				                   onmouseout="this.className='ur_button';"
				                   id="backToWorkspace"><span class="arrowBtnImg">&nbsp;</span> Back to workspace </button>
				           
			            </ur:basicForm>
			            
				        <div class="yui-u first">
				            
				           <div class="contentContainer">
				               <div class="contentBoxTitle">
				                   <p>User(s) to auto-share folder with</p>
				               </div>
				           
				               <div class="contentBoxContent">
				               		<p>
								          <form id="addInvite" name="newInviteForm" 
								          onsubmit="javascript: return YAHOO.ur.auto_share.validateAutoShareForm();"
								          method="post" 
								          action="<c:url value="/user/autoShareFolderWithUsers.action"/>">
								              <table class="formTable">
		
											  <input type="hidden" id="share_file_ids"
								               		name="shareFileIds" value="${shareFileIds}"/>
											  <input type="hidden" name="personalFolderId" value="${personalFolder.id}"/>						               		
								               		
								              <tr>
								              <td colspan="2">  <div id="inviteUserError" class="errorMessage"></div> </td>
									          </tr>
									          <tr>
									              <td><input type="checkbox" name="includeSubFolders" value="true"/> </td>
									              <td>Apply permissions to existing sub folders and files</td>
									          </tr>
									          <tr> 
									              <td></td>
									              <td> Separate Emails by semicolons (;) </td>
									             
									          </tr>
									          <tr> 
									          <td>Email(s)</td>
									          <td> <textarea name="emails" id="newInviteForm_inviteEmail" cols="45" rows="5"></textarea></td>
									          </tr>
		
											  <tr>
											  <td>Message</td>
											  <td>
								              <textarea name="inviteMessage" id="newInviteForm_inviteMessage" cols="45" rows="8"></textarea>
								              </td>
											  </tr>
											  
									          <c:forEach var="classTypePermission" items="${classTypePermissions}">
												  <tr>
												  <td>  <input type="checkbox" name="selectedPermissions" id="${classTypePermission.name}" value="${classTypePermission.id}" 
												      onclick="YAHOO.ur.auto_share.autoCheckPermission(this, selectedPermissions);"/> </td>
										          <td> ${classTypePermission.description}</td>
												  </tr>
			           						  </c:forEach>
											  
											  <tr>
											  <td colspan="2" align="center"> 
												  <button class="ur_button" id="inviteUser" type="submit"
												           
							                               onmouseover="this.className='ur_buttonover';"
							                               onmouseout="this.className='ur_button';"
							                               >Auto Share</button>
                              				  </td>
											  </tr>
											  </table>
								          </form>
							  		 </p>
			                     </div>
			                </div>
			                   
			             </div>
			             <!--  end the first column -->
		            
		                <div class="yui-u">
		                    
		                    <div class="contentContainer">
		                        <div class="contentBoxTitle">
		                            <p>Auto sharing with</p>
		                        </div>
		                   
		                        <div class="contentBoxContent">
		                        <p>
		                            <c:if test="${empty personalFolder.autoShareInfos && empty personalFolder.folderInviteInfos}">
						     		Not auto-sharing with anyone
						     		</c:if>
						     		<c:if test="${!empty personalFolder.autoShareInfos || !empty personalFolder.folderInviteInfos}">
						     		    <c:forEach var="autoShareInfo" items="${personalFolder.autoShareInfos}">
						     		        ${autoShareInfo.collaborator.firstName}&nbsp;${autoShareInfo.collaborator.lastName}
						     		        &nbsp;<a href="javascript:YAHOO.ur.auto_share.editPermissions(${autoShareInfo.id})">Edit</a>&nbsp;<a href="javascript:YAHOO.ur.auto_share.unshareFolder(${autoShareInfo.id}, '<c:url value="/user/removeAutoShareFolder.action"/>');">Remove Auto Share</a><br/>
						     		    </c:forEach>
						     		    <c:forEach var="invite" items="${ personalFolder.folderInviteInfos}">
						     		        ${invite.email}&nbsp; [to be shared]&nbsp; <a href="javascript:YAHOO.ur.auto_share.unshareFolder(${invite.id}, '<c:url value="/user/removeAutoShareFolderInvite.action"/>');">Remove Auto Share</a><br/>
						     		    </c:forEach>
						     		</c:if> 
						     	</p>
		                        </div>
		                    </div>
		                </div>
		                <!--  end the second column -->
	                
	                </div>
	                <!--  end the grid -->
	        </div>
	        <!-- end content div -->
	   
	  </div>
      <!--  end body div -->
      
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->
  
  <!--  start unshare dialog [ this will handle both unsharing an existing user and a user yet to register ]-->
  <div id="unshareFolderConfirmDialog" class="hidden">
      <div class="hd">Delete?</div>
	  <div class="bd">
	      <form id="unshareConfirmationForm" 
	            name="unshareForm" 
		        method="post" action="">
		       <input type="hidden" id="unshareFormAutoShareInfoId" name="folderAutoShareInfoId">
		       <input type="hidden" id="unshareFormParentFolderId" name="personalFolderId" value="${personalFolderId}" >
		            	
			   <p>Do you you want to stop auto sharing with selected user?</p>
			   
			   <table class="formTable">
			       <tr>
			           <td>
			               Include sub folders:
			           </td>
			           <td>
			               <input type="checkbox" name="includeSubFolders" value="true"/>
			           </td>
			       </tr>
			       <tr>
			           <td>
			               Remove Permissions on Files:
			           </td>
			           <td>
			               <input type="checkbox" name="includeSubFiles" checked="checked" value="true"/> 
			           </td>
			       </tr>
			   </table>
			</form>
		</div>
	</div>
	<!--  end unshare dialog -->
	
	<div id="editFolderPermissionsDialog" class="hidden">
        <div class="hd">Edit Permissions</div>
        <div class="bd">
            <form id="editPermissions" name="editPermissionsForm" 
		                    method="post" action="<c:url value="/user/editPermissions.action"/>">
		       <div id="editPermissionsDialogFields">
		           <c:import url="edit_folder_auto_share_permissions_form.jsp"/>
	           </div>
	       </form>
        </div>
    </div>
	
	
	<!--  wait div -->
 	<div id="wait_dialog_box" class="hidden">
 	    <div class="hd">Processing...</div>
 		<div class="bd">
 		    <c:url var="wait" value="/page-resources/images/all-images/ajax-loader.gif"/>
 		    <p><img src="${wait}"></img></p>
 		</div>
 	</div>       
</body>
</html>