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

<!--  images used by the page -->
<html>
    <head>
        <title>Welcome</title>
        <c:import url="/inc/meta-frag.jsp"/>

	    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>
	
	    <ur:styleSheet href="page-resources/css/main_menu.css"/>
	    <ur:styleSheet href="page-resources/css/global.css"/>
	    <ur:styleSheet href="page-resources/css/home_page_content_area.css"/>
	    <ur:styleSheet href="page-resources/css/tables.css"/>
        

        <!-- Dependencies --> 
    	<ur:js src="page-resources/yui/utilities/utilities.js"/>
        <ur:js src="page-resources/yui/button/button-min.js"/>
	    <ur:js src="page-resources/yui/container/container-min.js"/>
	    <ur:js src="page-resources/yui/menu/menu-min.js"/>
	    
        <!-- Source File -->
        <ur:js src="pages/js/base_path.js"/>
        <ur:js src="page-resources/js/util/ur_util.js"/>
        <ur:js src="page-resources/js/menu/main_menu.js"/>

  		<ur:js src="page-resources/js/user/submit_item_to_institutional_collection.js"/>
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">
            
				<br/>
                                           
                <button class="ur_button" id="finish_later" 
                    onmouseover="this.className='ur_buttonover';"
                    onmouseout="this.className='ur_button';"
                    onclick="javascript:YAHOO.ur.item.collection.submitPublication();">Submit</button>
                
                <button class="ur_button" id="goto_next" 
                    onmouseover="this.className='ur_buttonover';"
                    onmouseout="this.className='ur_button';"
                    onclick="javascript:YAHOO.ur.item.collection.cancel();">Cancel</button>
				
				<br/>
				
				<h3>Submit item: ${item.name}</h3>
				
       	        <div class="yui-g">
			        <div class="yui-u first">
			        	<form  id="all_collections_form" name="allCollectionsForm"  
				     		method="POST" action="javascript:YAHOO.ur.item.contributor.handleSearchFormSubmit();">
			        		
			        		<input type="hidden" id="all_collections_form_itemId" name="genericItemId" value="${item.id}"/>
			        		<input type="hidden" id="all_collections_form_parentId" name="parentCollectionId" value="${parentCollectionId}"/>
			        		<input type="hidden" id="all_collections_form_institutional_parentId" name="parentInstitutionalCollectionId" value="${parentInstitutionalCollectionId}"/>
			        		<input type="hidden" id="all_collections_form_collectionIds" name="selectedCollectionIds" value="${selectedCollectionIds}"/>
			        		
		       			    <!--  table of institutional collections  -->
	                        <div id="all_collections"></div>
	                        <!--  end table of institutional collections div -->
						</form>	                     
		       
		             </div>
		             <!--  end the first column -->
            
        	        <div class="yui-u">
        	              Selected Collections
        	              <br/>
        	              <br/>
		        	      <form id="selected_collections_form" name="selectedCollectionsForm"  method="POST" action="user/getContributors.action">
	                          <input type="hidden" id="selected_collections_form_itemId" name="genericItemId" value="${item.id}"/>
	                               	
	                               	<!--  Table of selected institutional collections -->
	                               	
	                               	<div id="selected_collections" >
	                               	    <c:import url="selected_collections_table.jsp"/>
	                                </div>
	                  				<!--  end table of selected institutional collections div -->
	                      </form>
	                          
	                     
            	    </div>
                	<!--  end the second column -->
                
                
                
                </div>
                <!--  end the grid -->
				<br/>
	                      <button class="ur_button" id="finish_later" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.collection.submitPublication();">Submit</button>
                          <button class="ur_button" id="goto_next" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.collection.cancel();">Cancel</button>

            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
