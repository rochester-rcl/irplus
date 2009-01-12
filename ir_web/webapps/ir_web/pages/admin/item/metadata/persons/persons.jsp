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
 
				    <table>
				        <tr>
							<td>
				                <button id="showPerson" class="ur_button" 
				                               onmouseover="this.className='ur_buttonover';"
				                               onmouseout="this.className='ur_button';">New Person</button> 
			                </td>
			                <td>
			                    <button id="showDeletePerson" class="ur_button" 
				                               onmouseover="this.className='ur_buttonover';"
				                               onmouseout="this.className='ur_button';">Delete</button>
			                </td>	            
				          </tr>
				      </table>

				      <ur:div id="newPersons"></ur:div>
				</div>

	            <div id="tab2">
	            	<ur:basicForm method="post" id="person_search_form" name="personSearchForm" action="javascript:YAHOO.ur.person.searchPerson(0,1,1);">
	            		<br>
						Search Person : <input type="input" name="query" size="50"/>
						<button id="search_person" class="ur_button" type="button"
		                       onmouseover="this.className='ur_buttonover';"
		                       onmouseout="this.className='ur_button';"
		                       onclick="javascript:YAHOO.ur.person.searchPerson(0,1,1);">Search</button>
					</ur:basicForm>
					
					<div id="search_results_div"></div>
				</div>

			</div>
		</div>
      </div>
      <!--  end body div -->		
			      
	      <ur:div id="newPersonDialog" cssClass="hidden">
	          <ur:div cssClass="hd">Person Information</ur:div>
		      <ur:div cssClass="bd">
		          <ur:basicForm id="addPerson" name="newPersonForm" 
		              method="post" 
		              action="user/createPerson.action">
		              
		              <input type="hidden" id="newPersonForm_id"
		               name="id" value=""/>
		               
		              <input type="hidden" id="newPersonForm_auth_id"
		               name="personNameId" value=""/>
		               
		              <input type="hidden" id="new_person"
		               name="newPerson" value="true"/>
		              
		              <ur:div id="personError" cssClass="errorMessage"></ur:div>
			          
			           <table class="formTable">    
						    <tr>       
					            <td align="left" class="label">First Name:*</td>
					            <td align="left" class="input"><input type="text" 
							    id="person_first_name" 
							          name="personName.forename" 
							          value="" size="45"/> </td>
							</tr>
							<tr>
							    <td align="left" class="label">Last Name:*</td>
					            <td align="left" class="input"><input type="text" 
							    id="person_last_name" 
				         			 name="personName.surname" 
				         			 value="" size="45"/> </td>
							</tr>
							<tr>       
					            <td align="left" class="label">Middle Name:</td>
					            <td align="left" class="input"><input type="text" 
							    id="person_middle_name" 
				          			name="personName.middleName" 
				          			value="" size="45"/> </td>
							</tr>
							<tr>
							    <td align="left" class="label">Family Name:</td>
					            <td align="left" class="input"><input type="text" 
							    id="person_family_name" 
				          			name="personName.familyName" 
				          			value="" size="45"/> </td>
							</tr>
						    <tr>       
					            <td align="left" class="label">Initials:</td>
					            <td align="left" class="input"><input type="text" 
							    id="person_initials" 
				          			name="personName.initials" 
				          			value="" size="45"/> </td>
							</tr>
							<tr>
							    <td align="left" class="label">Numeration:</td>
					            <td align="left" class="input"><input type="text" 
							    id="person_numeration" 
				         			 name="personName.numeration" 
				          			 value="" size="45"/> </td>
							</tr>
							<tr>       
					            <td align="left" class="label">Birth Date:</td>
					            <td align="left" class="label">
					            Month:<input  type="text" id="person_birthdate_month" name="birthMonth" size="2" maxlength ="2"/>
				          
				          		Day:<input  type="text" id="person_birthdate_day" name="birthDay" size="2" maxlength ="2"/>
				          
				          		Year:<input  type="text" id="person_birthdate_year" name="birthYear" size="4" maxlength ="4"/> </td>
							</tr>
							<tr>       
					            <td align="left" class="label">Death Date:</td>
							    <td align="left" class="label">

					            Month:<input type="text" id="person_deathdate_month" name="deathMonth" size="2" maxlength ="2"/>
				          
				          		Day:<input type="text" id="person_deathdate_day" name="deathDay" size="2" maxlength ="2"/>
				          
				          		Year:<input type="text" id="person_deathdate_year" name="deathYear" size="4" maxlength ="4"/>
				          		</td>
							</tr>
				          
			           </table>
		          </ur:basicForm>
		      </ur:div>
	      </ur:div>
	      
	      <ur:div id="deletePersonDialog" cssClass="hidden">
	          <ur:div cssClass="hd">Delete People</ur:div>
		      <ur:div cssClass="bd">
		          <ur:basicForm id="deletePerson" name="deletePerson" method="POST" 
		              action="user/deletePerson.action">
		              
		              
		              <ur:div id="deletePersonError" cssClass="errorMessage"> </ur:div>
			          <p>Are you sure you wish to delete the selected people?</p>
			          
		          </ur:basicForm>
		      </ur:div>
	      </ur:div>
 
 	      <ur:div id="deletePersonMessageDialog" cssClass="hidden">
	          <ur:div cssClass="hd">Delete People</ur:div>
		      <ur:div cssClass="bd">
		              <ur:div id="deletePersonMessage" cssClass="errorMessage"></ur:div>
		      </ur:div>
	      </ur:div>
      

  
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  </div>
  <!--  End doc div-->
     


</body>
</html>