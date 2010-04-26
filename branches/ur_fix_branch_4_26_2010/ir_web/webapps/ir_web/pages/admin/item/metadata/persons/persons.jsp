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

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Manage Person Authoritative Names</title>
        
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>
     
    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	<ur:js src="page-resources/yui/tabview/tabview-min.js"/>
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="pages/js/ur_table.js"/>
    <ur:js src="page-resources/js/admin/persons.js"/>
     
</head>

<body class=" yui-skin-sam">
 
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
    
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <h3>Edit Person Name Authorities</h3>
  
        <div id="bd">  
	        <!--  set up tabs for the workspace -->
	        <div id="person-tabs" class="yui-navset">
	         <ul class="yui-nav">
	             <li class="selected"><a href="#tab1"><em><u>Person Name Authority</u></em></a></li>
	             <li><a href="#tab2"><em><u>Search</u></em></a></li>
	         </ul>
	
	        <!--  first tab -->
	        <div class="yui-content">
	            <div id="tab1">
 
				    <br/>
				    
				    <button id="showPerson" class="ur_button" 
				                               onmouseover="this.className='ur_buttonover';"
				                               onmouseout="this.className='ur_button';">New Person</button> 
			        
                     <br/>
                     <br/>
				     <div id="newPersons"></div>
				</div>

	            <div id="tab2">
	            	<form method="post" id="person_search_form" name="personSearchForm" 
	            	action="javascript:YAHOO.ur.person.searchPerson(0,1,1);">
	            		<br>
						Search Person : <input type="input" name="query" id="search_person_query" size="50"/>
						<button id="search_person" class="ur_button" type="button"
		                       onmouseover="this.className='ur_buttonover';"
		                       onmouseout="this.className='ur_button';"
		                       onclick="javascript:YAHOO.ur.person.searchPerson(0,1,1);">Search</button>
					</form>
					<br/>
					<div id="search_results_div"></div>
				</div>

			</div>
		</div>
      </div>
      <!--  end body div -->		
			      
	      <div id="newPersonDialog" class="hidden">
	          <div class="hd">Person Information</div>
		      <div class="bd">
		          <form id="addPerson" name="newPersonForm" 
		              method="post" 
		              action="user/createPerson.action">
		              <div id="new_person_fields">
		                  <c:import url="person_form_frag.jsp"/>    
		              </div>
		          </form>
		      </div>
	      </div>
	      
	      <div id="deletePersonDialog" class="hidden">
	          <div class="hd">Delete People</div>
		      <div class="bd">
		          <form id="deletePerson" name="deletePerson" method="post" 
		              action="user/deletePerson.action">
		              
		              <div id="deletePersonError" class="errorMessage"> </div>
			          <p>Are you sure you wish to delete the selected people?</p>
			          
		          </form>
		      </div>
	      </div>
 
 	      <div id="deletePersonMessageDialog" class="hidden">
	          <div class="hd">Delete People</div>
		      <div class="bd">
		              <div id="deletePersonMessage" class="errorMessage"></div>
		      </div>
	      </div>
	      
	      <form id="deletePersonForm" name="deletePersonForm">
	          <input type="hidden" id="delete_person_id" name="id" value=""/> 
	      </form>
	      
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  </div>
  <!--  End doc div-->
     


</body>
</html>