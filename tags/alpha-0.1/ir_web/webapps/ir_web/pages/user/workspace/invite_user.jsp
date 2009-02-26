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

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<fmt:setBundle basename="messages"/>


<html>

<head>
    <title>Invite user</title>
    
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
 	<ur:js src="page-resources/yui/tabview/tabview-min.js"/>

 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="page-resources/js/user/invite_user.js"/>
    <ur:js src="page-resources/js/user/email_search.js"/>
    
 	<script type="text/javascript">
       	var myTabs = new YAHOO.widget.TabView("invite-user-tabs");
    </script>    
    
</head>



<body  class="yui-skin-sam">
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
      
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <div id="bd">
			
			<h3> Invite user </h3>
	        
	        <!--  set up tabs for editing news -->
	        <div id="invite-user-tabs" class="yui-navset">
	            <ul class="yui-nav">
	                <li class="selected"><a href="#tab1"><em>Invite User</em></a></li>
	                <li><a href="#tab2"><em>Search For Users To Share With</em></a></li>
	            </ul>
	
	            <div class="yui-content">
	                <!--  first tab -->
	                <div id="tab1">  
			        <!--  create the first column -->
			        <div class="yui-g">
		                <br/>
						<ur:basicForm id="showWorkspace" name="backToWorkspace" method="POST" action="user/workspace.action">
							<input type="hidden" name="parentFolderId" value="${parentFolderId}">
						
							
							<button class="ur_button" type="submit"
				                   onmouseover="this.className='ur_buttonover';"
				                   onmouseout="this.className='ur_button';"
				                   id="backToWorkspace"><span class="arrowBtnImg">&nbsp;</span> Back to workspace </button>
				           
			            </ur:basicForm>
			            
				        <div class="yui-u first">
				            
				           <div class="contentContainer">
				               <div class="contentBoxTitle">
				                   <p>User to share the file with</p>
				               </div>
				           
				               <div class="contentBoxContent">
				               		<p>
								          <ur:basicForm id="addInvite" name="newInviteForm" method="POST" action="user/addInviteUser.action">
								              <table class="formTable">
		
											  <input type="hidden" id="share_file_ids"
								               		name="shareFileIds" value="${shareFileIds}"/>
											  <input type="hidden" name="parentFolderId" value="${parentFolderId}"/>						               		
								               		
								              <tr>
								              <td colspan="2">  <ur:div id="inviteUserError" cssClass="errorMessage"></ur:div> </td>
									          </tr>
									          
									          <tr> 
									          <td> <label class="label" for="newUserForm_middle_name">Email</label>  </td>
									          <td> <input type="text" class="input" name="email" id="newInviteForm_email" value="" size="40"/>  </td>
									          </tr>
		
											  <tr>
											  <td>Message</td>
											  <td>
								              <textarea name="inviteMessage" id="newInviteForm_inviteMessage" cols="37" rows="8"></textarea>
								              </td>
											  </tr>
											  
									          <c:forEach var="classTypePermission" items="${classTypePermissions}">
												  <tr>
												  <td>  <input type="checkbox" name="selectedPermissions" id="${classTypePermission.name}" value="${classTypePermission.id}" 
												      onclick="YAHOO.ur.invite.autoCheckPermission(this, selectedPermissions);"/> </td>
										          <td> ${classTypePermission.description}</td>
												  </tr>
			           						  </c:forEach>
											  
											  <tr>
											  <td colspan="2" align="center"> 
												  <button class="ur_button" id="inviteUser" type="button"
							                               onmouseover="this.className='ur_buttonover';"
							                               onmouseout="this.className='ur_button';"
							                               >Invite User</button>
                              				  </td>
											  </tr>
											  </table>
								          </ur:basicForm>
							  		 </p>
			                     </div>
			                </div>
			                   
			             </div>
			             <!--  end the first column -->
		            
		                <div class="yui-u">
		                    
		                    <div class="contentContainer">
		                        <div class="contentBoxTitle">
		                            <p>File(s) selected to share</p>
		                        </div>
		                   
		                        <div class="contentBoxContent">
						     		<ur:div id="newCollaborators"></ur:div>
		                        </div>
		                    </div>
		                </div>
		                <!--  end the second column -->
	                
	                </div>
	                <!--  end the grid -->

		        </div>
	            <!--  end first tab -->
	                  
	                  
              	 <!--  start second tab -->
               	 <div id="tab2">
                     <ur:basicForm id="emailSearchForm" name="emailSearchForm" action="javascript:YAHOO.ur.invite.search.executeEmailSearch(0,1,1);" >
                         
                         <table class="formTable">
                             <tr>
                                 <td class="label">
                                     Search:
                                 </td>
                                 <td class="input">
                                     <input type="text" name="query" size="50"
                                         value="${query}"/>
                                 </td>
                                 <td>
                                     <button class="ur_button" type="button"
                                      onmouseover="this.className='ur_buttonover';"
	                                  onmouseout="this.className='ur_button';"
                                      onclick="YAHOO.ur.invite.search.executeEmailSearch(0,1,1)"><span class="magnifierBtnImg">&nbsp;</span>Search</button>
                                 </td>
                                 
                             </tr>
                         </table>
                     </ur:basicForm>

                     <!--  location where search results will be placed -->
                     <div id="search_results">
                     </div>
				 </div>
	             <!--  end tab 2 -->
	             <div class="clear">&nbsp;</div>
	          </div>
	          <!--  end content -->

	       </div>
	       <!--  end tabs -->

	        <div id="editPermissionsDialog" class="hidden">
                <ur:div cssClass="hd">Edit Permissions</ur:div>
                <ur:div cssClass="bd">
                    <ur:basicForm id="editPermissions" name="editPermissionsForm" 
		                    method="post" action="user/editPermissions.action">
		            	<ur:div id="editPermissionsDialogFields">
		            	    <c:import url="edit_permissions_form.jsp"/>
	                  	</ur:div>
				          
	                </ur:basicForm>
                </ur:div>
            </div>

			<!--  start unshare dialog -->
			<div id="unshareFileConfirmDialog" class="hidden">
			     <div class="hd">Delete?</div>
			     <div class="bd">
			        <ur:basicForm id="unshareConfirmationForm" name="unshareForm" 
		                    method="post" action="user/deleteCollaborator.action">
		            	<input type="hidden" id="unshareForm_fileCollaboratorId" name="fileCollaboratorId">
		            	<input type="hidden" id="unshareForm_personalFileId" name="personalFileId">
		            	<input type="hidden" id="unshareForm_parentFolderId" name="parentFolderId" value="${parentFolderId}" >
                        <input type="hidden" id="unshareForm_share_file_ids" name="shareFileIds" value="${shareFileIds}"/>
		            	
			         <p>Do you want to unshare the file for the selected user?</p>
			         </ur:basicForm>
			     </div>
			</div>
			<!--  end unshare dialog -->


			<!--  start unshare pending invitee dialog -->
			<div id="unsharePendingInviteeConfirmDialog" class="hidden" >
			     <div class="hd">Delete?</div>
			     <div class="bd">
			        <ur:basicForm id="unsharePendingInviteeConfirmationForm" name="unsharePendingInviteeForm" 
		                    method="post" action="user/deletePendingInvitee.action">
		            	<input type="hidden" id="unsharePendingInviteeForm_inviteInfoId" name="inviteInfoId">
		            	<input type="hidden" id="unsharePendingInviteeForm_personalFileId" name="personalFileId">
		            	<input type="hidden" id="unsharePendingInviteeForm_share_file_ids" name="shareFileIds" value="${shareFileIds}"/>
			         <p>Do you want to unshare the file?</p>
			         </ur:basicForm>
			     </div>
			</div>
			<!--  end unshare pending invitee dialog -->

		   <!--  start remove file dialog -->
			<div id="removeFileConfirmDialog" class="hidden" >
			     <div class="hd">Remove?</div>
			     <div class="bd">
			        <ur:basicForm id="remove_file_form" name="removeFileForm" 
		                    method="post" action="user/removeFile.action">
		            	<input type="hidden" id="remove_file_form_personal_file_id" name="personalFileId">
		            	<input type="hidden" id="removeFileForm_share_file_ids" name="shareFileIds" value="${shareFileIds}"/>
			         <p>Do you want to remove the file?</p>
			         </ur:basicForm>
			     </div>
			</div>
			<!--  end remove file  dialog -->			
						      
      </div>
      <!--  end body div -->
      
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->
  
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