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
    <title>Edit People</title>
    
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>
	<ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>   
        
    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
	<ur:js src="pages/js/ur_table.js"/>
    <ur:js src="page-resources/js/admin/edit_person.js"/>
</head>

<body class=" yui-skin-sam">

    <!--  this form is used to capture the person id being edited -->
    <form name ="personData">
        <input type="hidden" name="personId" id="person_id" value="${personId}"/>
    </form>
 
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
    
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <h3>Edit Person Names</h3>
  
        <div id="bd">    
	        <table>
	            <tr>
					<td>
		                <button id="showPersonName" class="ur_button" 
 		                               onmouseover="this.className='ur_buttonover';"
 		                               onmouseout="this.className='ur_button';">New Person Name</button> 
	                </td>
	                <td>
	                    <button id="showDeletePersonName" class="ur_button" 
 		                               onmouseover="this.className='ur_buttonover';"
 		                               onmouseout="this.className='ur_button';">Delete</button>
	                </td>		            

	             </tr>
	         </table>
	         <ur:div id="personNames"></ur:div>
	      
	         <ur:div id="newPersonNameDialog" cssClass="hidden">
	             <ur:div cssClass="hd">Name Information</ur:div>
		         <ur:div cssClass="bd">
		             <ur:basicForm id="addPersonName" name="newPersonNameForm" 
		                       method="post" 
		                       action="user/createPersonName.action">
		              
		             <input type="hidden" id="newPersonNameForm_id"
		                   name="id" value=""/>
		           
		             <input type="hidden" id="newPersonNameForm_personId"
		                   name="personId" value="${personId}"/>
		               
		             <input type="hidden" id="newPersonNameForm_new"
		                   name="newPersonName" value="true"/>
		              
		             <ur:div id="personError" cssClass="errorMessage"></ur:div>

			           <table class="formTable">    
						    <tr>       
					            <td align="left" class="label">First Name:*</td>
					            <td align="left" class="input"><input type="text" 
							    id="newPersonNameFormFirstName" 
			              			name="personName.forename" 
			              			value="" size="45"/> </td>
							</tr>
							<tr>
							    <td align="left" class="label">Last Name:*</td>
					            <td align="left" class="input"><input type="text" 
							    id="newPersonNameFormLastName" 
			             			 name="personName.surname"
				         			 value="" size="45"/> </td>
							</tr>
							<tr>       
					            <td align="left" class="label">Middle Name:</td>
					            <td align="left" class="input"><input type="text" 
							    id="newPersonNameFormMiddleName" 
			              			name="personName.middleName" 
				          			value="" size="45"/> </td>
							</tr>
							<tr>
							    <td align="left" class="label">Family Name:</td>
					            <td align="left" class="input"><input type="text" 
							    id="newPersonNameFormFamilyName" 
			          			   name="personName.familyName"  
				          			value="" size="45"/> </td>
							</tr>
						    <tr>       
					            <td align="left" class="label">Initials:</td>
					            <td align="left" class="input"><input type="text" 
							    id="newPersonNameFormInitials" 
			             			name="personName.initials"
				          			value="" size="45"/> </td>
							</tr>
							<tr>
							    <td align="left" class="label">Numeration:</td>
					            <td align="left" class="input"><input type="text" 
							    id="newPersonNameFormNumeration" 
			              			name="personName.numeration" 
				          			 value="" size="45"/> </td>
							</tr>
							<tr>
							    <td align="left" class="label">Authoritative Name:</td>
					            <td align="left" class="input"><input type="checkbox"
							    id="newPersonNameFormAuthoritative"
		                   			 name="authoritative" value="true" size="45"/> </td>
							</tr>	
						</table>		          
		          </ur:basicForm>
		      </ur:div>
	      </ur:div>
	      
	      <ur:div id="deletePersonNameDialog" cssClass="hidden">
	          <ur:div cssClass="hd">Delete People</ur:div>
		      <ur:div cssClass="bd">
		          <ur:basicForm id="deletePersonName" name="deletePersonName" method="POST" 
		              action="user/deletePersonName.action">
		              
		              
		              <ur:div id="deletePersonNameError" cssClass="errorMessage"></ur:div>
			          <p>Are you sure you wish to delete the selected people?</p>
		          </ur:basicForm>
		      </ur:div>
	      </ur:div>

	      <ur:div id="deletePersonNameMessageDialog" cssClass="hidden">
	          <ur:div cssClass="hd">Delete Name</ur:div>
		      <ur:div cssClass="bd">
		              <ur:div id="deletePersonNameMessage" cssClass="errorMessage"></ur:div>
		      </ur:div>
	      </ur:div>
	      

      </div>
      <!--  end body div -->
      
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  </div>
  <!--  End  context div-->

</body>
</html>