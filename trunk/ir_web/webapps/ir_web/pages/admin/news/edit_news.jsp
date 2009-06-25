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

    <title>Edit News</title>
    
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
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
 	<ur:js src="page-resources/yui/editor/editor-min.js"/>
    <ur:js src="page-resources/yui/tabview/tabview-min.js"/>
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 	<ur:js src="page-resources/js/admin/edit_news.js"/>
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
    
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <h3>Edit News</h3>
    
        <div id="bd">
            <!--  set up tabs for editing news -->
	        <div id="edit-news-tabs" class="yui-navset">
	            <ul class="yui-nav">
                    <li class="selected"><a href="#tab1"><em>News Information & Article</em></a></li>
                    <li><a href="#tab2"><em>News Pictures</em></a></li>
                </ul>

                
                <div class="yui-content">
                    <!--  first tab -->
                    <div id="tab1">
                        <c:url var="saveFormUrl" value="/admin/saveNewsItem.action"/>
                        <form method="post" action="${saveFormUrl}">
                            <input type="hidden" name="id" value="${newsItem.id}"/>
                            <table class="formTable">
                                <tr>
                                    <td class="label">
                                        <strong>Name:</strong>
                                    </td>
                                    <td class="input">
                                        <input type="text" size="75" name="newsItem.name" 
                                               value="${newsItem.name}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label">
                                        <strong>Short Description:</strong>
                                    </td>
                                    <td class="input">
                                        <textarea name="newsItem.description" rows="5" cols="72">${newsItem.description}</textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label">
                                        <strong>Date Available(MM/dd/YYYY):</strong>
                                    </td>
                                    <td class="input">
			                            <input type="text" id="newNewsItemForm_dateAvailable" 
			                                   name="newsItem.dateAvailable" value=""/>
			                        </td>
			                    </tr>
			                    <tr>       
			                        <td class="label">
			                            <strong>Date Removed(MM/dd/YYYY):</strong>
			                        </td>
			                        <td class="input">   
			                            <input type="text" id="newNewsItemForm_dateRemoved" 
			                             name="newsItem.dateRemoved" value=""/>	
			                        </td>
			                    </tr>	
                                <tr>
                                    <td class="label">
                                        <strong>Article</strong>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="input" colspan="2">
                                        <textarea name="article" id="msgpost" rows="20" cols="75"> ${article} </textarea>
                                    </td>
                                </tr>
                                <tr>
                                   <td>
                                       <input type="submit" value="save"/>
                                   </td>
                                </tr>
                     
                            </table>
                       </form>
                   </div>
	               <!--  end first tab -->
	                  
	                  
	               <!--  start second tab -->
	               <div id="tab2">
	                  
 		              <button class="ur_button" 
 		                      onmouseover="this.className='ur_buttonover';"
 		                      onmouseout="this.className='ur_button';"
 		                               id="showUploadPicture">Upload Picture</button> 
	                      <br/>
	                     

	                      
	                      <div id="news_item_pictures">
	                          <c:import url="/pages/admin/news/news_pictures_frag.jsp"/>
                          </div>
                        <!--  end pictures -->
	               </div>
	               <!--  end tab 2 -->
	          </div>
	          <!--  end content -->
	       </div>
	       <!--  end tabs -->
            
           
        </div>
        <!--  end body div -->
        


      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  </div>
  <!--  End  doc div-->

  <!-- Dialog box for delete confirmation -->
  <div id="deletePictureDialog" >
      <div class="hd">Delete Picture</div>
	  <div class="bd">
	     <c:url var="deleteUrl" value="/admin/deleteNewsPicture.action"/>
	      <form id="deleteNewsPictureForm" name="pictureUploadForm" 
		                              method="post"  action="${deleteUrl}">
		      <input type="hidden" id="delete_news_item_id" name="newsItemId" value=""/>
		      <input type="hidden" id="delete_picture_id" name="pictureId" value=""/>
		      <input type="hidden" id="delete_primary_news_picture" name="primaryNewsPicture" value=""/>
		      <p>Do you want to delete the selected picture?</p>
		  </form>
	   </div>
   </div>
        
        <!-- Dialog box for uploading pictures -->
        <div id="uploadNewsPictureDialog" >
	            <div class="hd">Picture Upload</div>
		        <div class="bd">
		            <c:url var="uploadUrl" value="/admin/uploadNewsPicture.action"/>
		            <form id="addNewsPicture" name="pictureUploadForm" 
		                 method="post" enctype="multipart/form-data"
		                 action="${uploadUrl}">
		                 <input type="hidden" id="news_item_id" name="newsItemId" value="${newsItem.id}"/>
		                 <div id="upload_form_fields">
		                 <c:import url="/pages/admin/news/news_upload_form_frag.jsp"/>
		                 </div>
		             </form>
		         </div>
	     </div>
	     <!--  end upload picture dialog -->
</body>
</html>