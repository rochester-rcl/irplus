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
<%@ taglib prefix="ir" uri="ir-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
<fmt:setBundle basename="messages"/>
<html>
  <head>
    <title> Edit Researcher: ${researcher.title}</title>
    <c:import url="/inc/meta-frag.jsp"/>
        
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>
    
    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/home_page_content_area.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    <ur:styleSheet href="page-resources/css/tree.css"/>
    
	<ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	<ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/tabview/tabview-min.js"/>
 	<ur:js src="page-resources/yui/treeview/treeview-min.js"/> 
    <ur:js src="page-resources/yui/json/json-min.js"/>
        
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 	<ur:js src="pages/js/ur_table.js"/>
 	
 	<ur:js src="page-resources/js/user/researcher_tabs.js"/>
 	<ur:js src="page-resources/js/user/edit_researcher.js"/>
 	<ur:js src="page-resources/js/user/researcher_folder.js"/>
 	<ur:js src="page-resources/js/user/researcher_links.js"/>
 	
    
    <!--  Style for dialog boxes -->
    <style>
 		#treeDiv { background: #fff; padding:1em; margin-top:1em; }
    </style>
    
    <script type="text/javascript">
    	var myTabs = new YAHOO.widget.TabView("researcher-properties-tabs");
    </script>

  </head>
    
  <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  
            <form id="hidden_researcher_form" name="hiddenResearcherId" method="post">
                <input type="hidden" id="hidden_researcher_id" name="researcherId" value="${researcher.id}"/>
            </form>

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body of the page -->
            <div id="bd">
            
            <h3> Researcher page of ${researcher.user.firstName}&nbsp;${researcher.user.lastName}</h3>
    
            <!--  set up tabs for the researcher -->
	        <div id="researcher-properties-tabs" class="yui-navset">

	            <div id="preview" align="left">
		        	<a href="viewResearcherPage.action?researcherId=${researcher.id}">Preview Researcher Page</a> 
	            </div>	        

		        <div id="publi" align="right">
		        	Researcher Page : 
		        	<input type="radio" name="isPublic" onclick="javascript:YAHOO.ur.edit.researcher.changePublicValue(false);"
	            		<c:if test="${!researcher.public}">
	            			checked
	            		</c:if>
	            	> Hidden </input> 
	            	
	            	<input type="radio" name="isPublic" onclick="javascript:YAHOO.ur.edit.researcher.changePublicValue(true);"
	            		<c:if test="${researcher.public}">
	            			checked
	            		</c:if>
	            	> Public </input>
	            	
	            </div>

	            
	             <ul class="yui-nav">
                     <li class="selected"><a href="#tab1"><em>Personal Information</em></a></li>
                     <li ><a href="#tab2"><em>Research</em></a></li>
                     <li ><a href="#tab3"><em>Pictures</em></a></li>
                     <li ><a href="#tab4"><em>Links</em></a></li>
                 </ul>

                 <div class="yui-content">
                     
                     <!--  first tab -->
                     <div id="tab1">
                         <form id="base_researcher_information" name="researcherInformation" method="post" 
                             action="javascript:YAHOO.ur.edit.researcher.updatePersonalInfo();">
                             
                             <input type="hidden" id="field_table_id" value="${researcherFieldsCount}"/>
                             <input type="hidden" id="department_table_id" value="${researcherDepartmentsCount}"/>
                             <input type="hidden" id="researcherId"   name="researcherId" value="${researcher.id}" />

					        <div class="yui-g">

					        <!--  create the first column -->
					        <div class="yui-u first">
                             
                                <table class = "formTable">
	                                <tr>
	                                    <td class="label">
	                                        <strong>Title</strong>
	                                    </td>
	                               </tr>
	                               <tr>
	                                    <td class="input">
	                                        <input type="text" size="45" id="researcher_title" name="researcher.title" 
	                                               value="${researcher.title}"/>
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <td class="label">
	                                        <strong>Department</strong> 
	                                        &nbsp;&nbsp;&nbsp;<input type="button" class="ur_button" id="researcherForm_add_department" value="Add Department" onclick="javascript:YAHOO.ur.edit.researcher.getDepartments();"/>
	                                        &nbsp;&nbsp;&nbsp;<input type="button" class="ur_button" id="show_department" value="Add New Department"/>
	                                    </td>
	                               </tr>
	                               <tr>
	                                    <td class="input">
	                					   <div id="new_department"> </div>
	                                       <!--  this table is built dynamically -->
							               <div id="department_form">
							                    <c:import url="department_list.jsp"/>
							               </div> 
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <td class="label">
	                                        <strong>Field</strong>
	                                        &nbsp;&nbsp;&nbsp;<input type="button" class="ur_button" id="researcherForm_add_field" value="Add Field" onclick="javascript:YAHOO.ur.edit.researcher.getFields();"/>
	                                        &nbsp;&nbsp;&nbsp;<input type="button" class="ur_button" id="show_field" value="Add New Field"/>
	                                    </td>
	                               </tr>
	                              <tr>
	                              		<td class="input">
	                					   <div id="new_field"> </div>
	                                       <!--  this table is built dynamically -->
							               <div id="field_form">
							                    <c:import url="field_list.jsp"/>
							               </div> 
	                                    </td>
	                                </tr>
	
	                                <tr>
	                                    <td class="label">
	                                        <strong>Campus location</strong>
	                                    </td>
	                               </tr>
	                               <tr>
	                                    <td class="input">
	                                        <input type="text" size="45" name="researcher.campusLocation" id="researcher_location" 
	                                        value="${researcher.campusLocation}" />
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <td class="label">
	                                        <strong>Phone number</strong>
	                                    </td>
	                               </tr>
	                               <tr>
	                                    <td class="input">
	                                        <input type="text" size="45" id="researcher_phone" name="researcher.phoneNumber" 
	                                               value="${researcher.phoneNumber}"/>
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <td class="label">
	                                        <strong>Email</strong>
	                                    </td>
	                               </tr>
	                               <tr>
	                                    <td class="input">
	                                  
	                                  		<c:if test="${researcher.email == null}">
												<c:set var="selectedEmail" value="${researcher.user.defaultEmail.email}"/>
											</c:if>	
	
	
	                                    	<c:if test="${researcher.email != null}">
												<c:set var="selectedEmail" value="${researcher.email}"/>
											</c:if>		
														
	                                           <select id="researcher_email" name="researcher.email" />
													<c:forEach items="${researcher.user.userEmails}" var="email">
														<option value = "${email.email}"
														
														<c:if test="${email.email == selectedEmail}">
															selected
														</c:if>
														
														> ${email.email}</option>
													</c:forEach>
												</select>
	
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <td class="label">
	                                        <strong>Fax</strong>
	                                    </td>
	                               </tr>
	                               <tr>
	                                    <td class="input">
	                                        <input type="text" size="45" id="researcher_fax" name="researcher.fax" 
	                                               value="${researcher.fax}"/>
	                                    </td>
	                                </tr>
	
	                            </table>
			             </div>
			             <!--  end the first column -->
			             
		                <div class="yui-u">
			             
                            <table class="formTable">
                                <tr>
                                     <td class="label">
                                        <strong>Research Interest</strong>
                                    </td>
                               </tr>
                               <tr>
                                     <td class="input">
                                        <textarea rows="11" cols="45" id="researcher_research" 
                                        	name="researcher.researchInterest">${researcher.researchInterest}</textarea>
                                    </td>
                                </tr>

                                <tr>
                                     <td class="label">
                                        <strong>Teaching Interest</strong>
                                    </td>
                                    
                               </tr>
                               <tr>
                                    <td class="input">
                                        <textarea rows="11" cols="45" id="researcher_teaching" 
                                        	name="researcher.teachingInterest">${researcher.teachingInterest}</textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label">
                                        <strong>Search Keywords(comma separated values)</strong>
                                    </td>
                               </tr>
                               <tr>
                                    <td class="input">
                                        <textarea rows="5" cols="45" id="researcher_keywords" 
                                        	name="researcher.keywords">${researcher.keywords}</textarea>
                                    </td>
                                </tr>
                            </table>
                            
		                </div>
		                <!--  end the second column -->
								
	                
		                 </div>
		                 <!--  end the grid -->

                   		<button class="ur_button" id="update_researcher_information" type="submit"
               				onmouseover="this.className='ur_buttonover';"
             				onmouseout="this.className='ur_button';"
               				>Save</button>
                            
                       </form>
	                 
	                 </div>
	                 <!--  end first tab -->
	                  
	                 <!--  start second tab -->
	                 <div id="tab2">
	                 	  <br/>
	                      <div id="files_folders_buttons">
	                     
 		                              <button class="ur_button" 
 		                               onmouseover="this.className='ur_buttonover';"
 		                               onmouseout="this.className='ur_button';"
 		                               id="showFolder"><span class="addFolderBtnImg">&nbsp;</span><fmt:message key="new_folder"/></button> 
	                              
 		                              <button class="ur_button" 
 		                               onmouseover="this.className='ur_buttonover';"
 		                               onmouseout="this.className='ur_button';"
 		                               id="showLink"><img  alt="" class="buttonImg" src="${pageContext.request.contextPath}/page-resources/images/all-images/link_add.gif"/>
 		                               <fmt:message key="new_link"/></button> 
	                              
	                                  <button class="ur_button" id="showAddFile"
	                                  onmouseover="this.className='ur_buttonover';"
 		                              onmouseout="this.className='ur_button';"
 		                              onclick="javascript:YAHOO.ur.edit.researcher.viewAddFiles();"><span class="pageAddBtnImg">&nbsp;</span>Add file</button>
	                              
	                                  <button class="ur_button" id="showAddPublication"
	                                  onmouseover="this.className='ur_buttonover';"
 		                              onmouseout="this.className='ur_button';"
 		                              onclick="javascript:YAHOO.ur.edit.researcher.viewAddPublications();"><span class="packageAddBtnImg">&nbsp;</span>Add Publication</button>
	                              
	                                  <button class="ur_button" id="showDeleteFolder"
	                                  onmouseover="this.className='ur_buttonover';"
 		                              onmouseout="this.className='ur_button';"><span class="deleteBtnImg">&nbsp;</span>Delete</button>
	                             
	                      </div>
	                      

	                      <!--  table of files and folders -->
	                      <div id="newResearcherFolders" >
	                          <ur:basicForm  id="folders" name="myFolders">
	                              <input type="hidden" id="myFolders_parentFolderId" 
	                                   name="parentFolderId" 
	                                   value="${parentFolderId}"/>
	                              <input type="hidden" id="myFolders_showFoldersTab" 
	                                   name="showFoldersTab" 
	                                   value="${showFoldersTab}"/>	                                   
	                          </ur:basicForm>
	                      </div>
	                      <!--  end personal files and folders div -->	                  

	                 </div>                
	                 <!--  end second tab -->
	                 
	                 <!--  Start third tab -->
	                 <div id="tab3">
	                    <br/>
	                    <table>
	                        <tr>
	                            <td>
 		                              <button class="ur_button" 
 		                               onmouseover="this.className='ur_buttonover';"
 		                               onmouseout="this.className='ur_button';"
 		                               id="showUploadPicture">Upload Picture</button> 
	                            </td>
	                         </tr>
	                    </table>
	                    <div id="deletePictureDiv"></div>
	                   
	                   	<div id="researcher_pictures">
	                       		 <c:import url="researcher_pictures_frag.jsp"/>
                        </div>
                       
	                    
                        <!--  end pictures -->
	                 </div>
	                 <!--  End third tab -->
	                 
	                 <!--  Start fourth tab -->
	                 <div id="tab4">
	                     <br/>
                         <br/>
                         <button class="ur_button"
					             onmouseover="this.className='ur_buttonover';"
					             onmouseout="this.className='ur_button';"
					             onclick="javascript:YAHOO.ur.researcher.link.newLinkDialog.showLinkDialog();"
					             id="add_researcher_personal_link_btn">Add Link</button>
					             
                         <br/>
                         <br/>
                         <div id="researcher_personal_links">
                            <c:import url="researcher_personal_links_table_frag.jsp"/>
                         </div>
	                 </div>
	                 <!--  End fourth tab -->
	                 
	             </div>
	             <!--  end content div -->
	             
                
            </div>
            <!--  end the tabs div --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
        
        <!-- Dialog box for uploading pictures -->
        <ur:div id="uploadResearcherPictureDialog" cssClass="hidden">
	        <ur:div cssClass="hd">Picture Upload</ur:div>
		    <ur:div cssClass="bd">
		        <ur:basicForm id="addResearcherPicture" name="pictureUploadForm" 
		            method="post" enctype="multipart/form-data"
		            action="user/uploadResearcherPicture.action">
		              
		            <input type="hidden" id="researcher_id" name="researcherId" value="${researcher.id}"/>
		            <div id="upload_form_fields">
		                <c:import url="/pages/user/researcher/researcher_upload_form_frag.jsp"/>
		             </div>
		        </ur:basicForm>
		    </ur:div>
	     </ur:div>
	     <!--  end upload picture dialog -->

	     <!--  generic error dialog -->   	     
	     <div id="error_dialog_box" class="hidden">
	         <div class="hd">Error</div>
		     <div class="bd">
		          <div id="default_error_dialog_content">
		          </div>
		     </div>
	     </div>
	     <!-- End generic error dialog -->   	     
  
	  <ur:div id="newDepartmentDialog" cssClass="hidden">
	      <ur:div cssClass="hd">Department Information</ur:div>
	      <ur:div cssClass="bd">
	          <ur:basicForm id="addDepartment" name="newDepartmentForm" 
			                    method="post" 
			                    action="user/addDepartment.action">
		          <ur:div id="newDepartmentDialogFields">
		                       <%@ include file="/pages/admin/department/department_form.jsp" %>
		          </ur:div>
		      </ur:basicForm>
	      </ur:div>
	  </ur:div>	  
	  
	  <ur:div id="newFieldDialog" cssClass="hidden">
	      <ur:div cssClass="hd">Field Information</ur:div>
	      <ur:div cssClass="bd">
	          <ur:basicForm id="addField" name="newFieldForm" 
			                    method="post" 
			                    action="user/addField.action">
		          <ur:div id="newFieldDialogFields">
		                       <%@ include file="/pages/admin/field/field_form.jsp" %>
		          </ur:div>
		      </ur:basicForm>
	      </ur:div>
  	  </ur:div>
  
        <div id="newFolderDialog" class="hidden">
            <div class="hd">Folder Information</div>
            <div class="bd">
                  <ur:basicForm id="addFolder" name="newFolderForm" 
                  method="post" action="user/addResearcherFolder.action">
              
                   <input type="hidden" id="newFolderForm_parentFolderId"
                       name="parentFolderId" value="${parentFolderId}"/>
                       
                   <input type="hidden" id="newFolderForm_researcherId"
                       name="researcherId" value="${researcher.id}"/>                       
               
                   <input type="hidden" id="newFolderForm_new"
                       name="newFolder" value="true"/>
              
                   <input type="hidden" id="newfolderForm_folderId"
                       name="updateFolderId" value=""/>
                   
                  <div id="folderNameError" class="errorMessage"></div>
	              
	              <table class="formTable">
	                  <tr>
	                      <td align="left"  class="label"> Folder Name:*</td>
	                      <td align="left" class="input"> <input id="folder" type="text" name="folderName" value="" size="45"/></td>
	                  </tr>
	                  <tr>
	                      <td align="left"  class="label"> Folder Description:</td>
	                      <td align="left" class="input" colspan="2" ><textarea cols="42" rows="4" name="folderDescription"></textarea></td>
	                  </tr>
	              </table>
                 </ur:basicForm>
           </div>
           <!-- end dialog body -->
       </div>
       <!--  end the new folder dialog -->
 
       <!--  move folder dialog -->
      <div id="deleteFileFolderConfirmDialog" class="hidden">
          <div class="hd">Delete?</div>
          <div class="bd">
              <p>Do you want to delete the selected items?</p>
          </div>
      </div>
      <!--  end move folder dialog -->

        <div id="newLinkDialog" class="hidden">
            <div class="hd">Link Information</div>
            <div class="bd">
                  <ur:basicForm id="addLink" name="newLinkForm" 
                  method="post" action="user/addResearcherLink.action">
              
                   <input type="hidden" id="newLinkForm_parentFolderId"
                       name="parentFolderId" value="${parentFolderId}"/>
                       
                   <input type="hidden" id="newLinkForm_researcherId"
                       name="researcherId" value="${researcher.id}"/>                       
               
                   <input type="hidden" id="newLinkForm_new"
                       name="newLink" value="true"/>
              
                   <input type="hidden" id="newLinkForm_linkId"
                       name="updateLinkId" value=""/>
                   
                  <div id="linkNameError" class="errorMessage"></div>
	              
	              <table class="formTable">
	                  <tr>
	                      <td align="left" class="label"> Link Name:</td>
	                      <td align="left" class="input"> <input id="link_name" size="45" type="text" name="linkName" value=""/></td>
	                  </tr>
	                  <tr>
	                      <td align="left" class="label"> Link Description:</td>
	                      <td align="left" class="input"><textarea cols="42" rows=4" name="linkDescription"></textarea></td>
	                  </tr>
	                  <tr>
	                      <td align="left" class="label"> Link URL:</td>
	                      <td align="left" class="input"> <input id="link_url" size="45" type="text" name="linkUrl" value="http://"/></td>
	                  </tr>
	              </table>
                 </ur:basicForm>
           </div>
           <!-- end dialog body -->
       </div>
       <!--  end the new link dialog -->
       
       	<!-- Create a new personal link dialog  -->
	   	<div id="newResearcherPersonalLinkDialog" class="hidden">
	   	
	        <div class="hd">Link Information</div>
		    <div class="bd">
		       <c:url var="addResearcherPersonalLinkAction" 
		                  value="/user/addResearcherPersonalLink.action"/>
		        <form id="add_researcher_personal_link" name="newResearcherPersonalLinkForm" 
		            method="post" action="${addResearcherPersonalLinkAction}">
		              
		             <input type="hidden" id="researcherId" name="researcherId" value="${researcher.id}"/>

              	    <div id="researcher_personal_link_fields">
               		    <%@ include file="add_researcher_personal_link_form.jsp" %>
              	    </div>

		         </form>
		     </div>
		      <!-- end dialog body -->
	     </div>
	     <!--  end the new folder dialog -->
	     
	     
	      <!--  remove link from collection -->   	     
	     <div id="remove_researcher_personal_link_confirm" class="hidden">
	         <div class="hd">Remove Link</div>
		     <div class="bd">
		          <div id="remove_link">
		              <form method="POST" id="remove_researcher_personal_link_form" action="">
		                  <input type="hidden" id="remove_link_name" name="linkName" value=""/>
		                  <input type="hidden" name="researcherId" value="${researcher.id}"/>
		              </form>
		              <p>Are you sure you wish to remove the selected link?</p>
		          </div>
		     </div>
	     </div>
	     <!-- End prompt to delete researcher personal link -->  

    </body>
</html>