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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
<fmt:setBundle basename="messages"/>
<html>
  <head>
    <title>Admin - Edit Institutional Collection: ${collection.name}</title>
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

 	<ur:js src="page-resources/js/admin/edit_institutional_collection.js"/>
 	<ur:js src="page-resources/js/admin/add_group_to_collection.js"/>
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 	
 	
    
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
            
            
            <!--  this is the body of the page -->
            <div id="bd">
            <form id="hidden_collection_form" name="hiddenCollectionId">
                <input type="hidden" id="hidden_collection_id" value="${collection.id}"/>
            </form>

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            <!--  path to this collection -->
            <div id="path">
                <h3>Edit Collection: <a href="admin/viewInstitutionalCollections.action">${collection.repository.name}</a> >
                    <c:forEach var="collection" items="${collectionPath}">
                        <c:url var="pathCollectionUrl" value="/admin/viewInstitutionalCollections.action">
                            <c:param name="parentCollectionId" value="${collection.id}"/>
                        </c:url>
                        <a href="${pathCollectionUrl}">${collection.name}</a> >
                    </c:forEach>
                    ${collection.name}
                </h3>
            </div>
            
            
            <!--  set up tabs for the workspace -->
	        <div id="collection-properties-tabs" class="yui-navset">
	             <ul class="yui-nav">
                     <li class="selected"><a href="#tab1"><em>Collection Information</em></a></li>
                     <li><a href="#tab2"><em>Images</em></a></li>
                     <li ><a href="#tab3"><em>Permissions &amp; Groups</em></a></li>
                     <li><a href="#tab4"><em>Subscribers</em></a></li>
                     <li><a href="#tab5"><em>Statistics</em></a></li>
                     <li><a href="#tab6"><em>Department Links</em></a></li>
                 </ul>

                 <div class="yui-content">
                     <!--  first tab -->
                     <div id="tab1">
                        
                         <form id="base_collection_information" name="collectionInformation" method="post" 
                             action="javascript:YAHOO.ur.edit.institution.collection.updateCollection();">
                            <input type="hidden" id="collectionId" 
                                  name="collectionId" value="${collection.id}" />
                            <table class="formTable">
                                <tr>
                                    <td class="label">
                                        <strong>Name:</strong>
                                    </td>
                                    <td class="input">
                                        <input type="text" size="75" id="collection_name" name="collectionName" 
                                               value="<c:out value='${collection.name}'/>"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label">
                                        <strong>About this Collection</strong>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="input" colspan="2">
                                        <textarea name="collectionDescription" id="collection_description" 
                                        rows="20" cols="75"><c:out value='${collection.description}'/></textarea>
                                    </td>
                                </tr>
                                <tr>
                                   <td>
                                       <input type="submit" id="update_base_collection_data" value="save"/>
                                   </td>
                                </tr>
                            </table>
                       </form>
	                 </div>
	                 <!--  end first tab -->
	                  
	                  
	                 <!--  start second tab -->
	                 <div id="tab2">
	                    
	                   <br/>
 		               <button class="ur_button" 
 		                       onmouseover="this.className='ur_buttonover';"
 		                       onmouseout="this.className='ur_button';"
 		                       id="showUploadPicture">Upload Picture</button> 
	                       
	                    
	                    <div id="deletePictureDiv"></div>
	                    <div id="collection_pictures">
	                        <c:import url="collection_pictures_frag.jsp"/>
                        </div>
                        <!--  end pictures -->
	                 </div>                
	                 <!--  end second tab -->
	                 
	                 <!--  Start third tab -->
	                 <div id="tab3">
	                     
                          <br/>
                         <div id="edit_view_permission" align="left">
                            <c:import url="collection_permission_frag.jsp"/>
                         </div>
                          <br/>
                          
                         
                         <c:url var="addGroupsUrl" value="/admin/addGroupsToCollection.action">
                             <c:param name="collectionId" value="${collection.id}"/>
                         </c:url>
                         <a href="${addGroupsUrl}">Add Groups To Collection</a>
                         <br/>
                         <br/>
                         <div id="current_collection_groups_div">
                             <c:import url="collection_group_permissions_frag.jsp"></c:import>
                         </div>
	                 </div>
	                 <!--  End third tab -->
	                 
	                 <!--  Start forth tab -->
	                 <div id="tab4">
	                 	<br>
	                 	<div id="collection_subscribers">
						    <c:import url="collection_subscription_table.jsp"/>
						</div>	                
	                 
	                 </div>
	                 <!--  End forth tab -->
	                 
	                 <!--  Start fifth tab -->
	                 <div id="tab5">
	                 </div>
	                 <!--  End fifth tab -->
	                 
	                 <!--  Start sixth tab -->
	                 <div id="tab6">
	                     <br/>
                         <br/>
                         <button class="ur_button"
					             onmouseover="this.className='ur_buttonover';"
					             onmouseout="this.className='ur_button';"
					             onclick="javascript:YAHOO.ur.edit.institution.collection.newLinkDialog.showLinkDialog();"
					             id="add_collection_link_btn">Add Link</button>
				
                         <br/>
                         <br/>
                         <div id="collection_links">
                            <c:import url="collection_links_table_frag.jsp"/>
                         </div>
	                 
	                 </div>
	                 <!--  End sixth tab -->
	                 
	                 <!--  Start seventh tab -->
	                 <div id="tab7">
	                 </div>
	                 <!--  End seventh tab -->
	                 
	             </div>
	             <!--  end content div -->
	             
                
            </div>
            <!--  end the tabs div --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        </div>
        
      </div>
      <!-- end doc -->
        
        <!-- Dialog box for uploading pictures -->
        <ur:div id="uploadCollectionPictureDialog" cssClass="hidden">
	        <ur:div cssClass="hd">Picture Upload</ur:div>
		    <ur:div cssClass="bd">
		        <ur:basicForm id="addCollectionPicture" name="pictureUploadForm" 
		            method="post" enctype="multipart/form-data"
		            action="admin/uploadCollectionPicture.action">
		              
		            <input type="hidden" id="collection_id" name="collectionId" value="${collection.id}"/>
		            <div id="upload_form_fields">
		                <c:import url="collection_upload_form_frag.jsp"/>
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
	     
	     <!--  permissions for a group on a collection -->   	     
	     <div id="edit_group_permissions" class="hidden">
	         <div class="hd">Edit Permissions For Group</div>
		     <div class="bd">
		          <div id="group_permissions">
		              <c:url var="submitAction" 
		                  value="/admin/updateEntryPermissionsToGroup.action"/>
		              <form method="POST" name="permissionsCollectionForm" id="permissions_for_collection_form" action="${submitAction}">
		                  <input type="hidden" id="group_permissions_group_id" name="groupId" value=""/>
		                  <input type="hidden" name="collectionId" value="${collection.id}"/>
		                  <div id="permissions_for_group">
		                      <c:import url="collection_permissions_form_frag.jsp"/>
		                  </div>
		              </form>
		          </div>
             </div>
	     </div>
	     <!-- End permissions for a group on a collection -->   	     
   	     
	   
	   	<!--  remove grop from a collection -->   	     
	     <div id="remove_group_confirm" class="hidden">
	         <div class="hd">Remove Group Permissions</div>
		     <div class="bd">
		          <div id="group_permissions">
		              <form method="POST" id="remove_group_from_collection_form" action="">
		                  <input type="hidden" id="remove_group_id" name="groupId" value=""/>
		                  <input type="hidden" name="collectionId" value="${collection.id}"/>
		              </form>
		              <p>Are you sure you wish to remove the selected group?</p>
		          </div>
		     </div>
	     </div>
	     <!-- End remove group from a collection -->   	     

	   	<!-- Create a new link dialog  -->
	   	<div id="newLinkDialog" class="hidden">
	   	
	        <div class="hd">Link Information</div>
		    <div class="bd">
		       <c:url var="addLinkAction" 
		                  value="/admin/addCollectionLink.action"/>
		        <form id="add_link" name="newLinkForm" 
		            method="post" action="${addLinkAction}">
		              
		             <input type="hidden" id="collectionId" name="collectionId" value="${collection.id}"/>

              	    <div id="new_link_fields">
              	        <c:import url="add_collection_link_form.jsp"/>
              	    </div>

		         </form>
		     </div>
		      <!-- end dialog body -->
	     </div>
	     <!--  end the new folder dialog -->
	     
	     
	      <!--  remove link from collection -->   	     
	     <div id="remove_link_confirm" class="hidden">
	         <div class="hd">Remove Link</div>
		     <div class="bd">
		          <div id="remove_link">
		              <form method="POST" id="remove_link_collection_form" action="">
		                  <input type="hidden" id="remove_link_id" name="linkId" value=""/>
		                  <input type="hidden" name="collectionId" value="${collection.id}"/>
		              </form>
		              <p>Are you sure you wish to remove the selected link?</p>
		          </div>
		     </div>
	     </div>
	     <!-- End remove link from collection -->   	 

	     <!--  edit collection permission -->   	     
	     <div id="edit_permission_dialog" class="hidden">
	         <div class="hd">Edit Permission</div>
		     <div class="bd">
		          <form method="POST" id="edit_permission_collection_form" name="editPermissionCollectionForm" >
			      	<div id="edit_permission">
			        </div>
			     </form>
			     
		     </div>
	     </div>
	     <!-- End permissions for a collection -->   	 

    </body>
</html>