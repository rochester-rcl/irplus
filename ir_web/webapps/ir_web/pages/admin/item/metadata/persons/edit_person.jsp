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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

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
 	<ur:js src="page-resources/js/util/wait_dialog.js" />
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
        
        <c:url var="viewPersons" value="/admin/viewPersons.action"/>
      
        <h3>Edit Person Names > <a href="${viewPersons}">Person Name Authority</a></h3>
  
        <div id="bd">
            <br/>    
		    <button id="showPersonName" class="ur_button" 
 		            onmouseover="this.className='ur_buttonover';"
 		            onmouseout="this.className='ur_button';">New Person Name</button> 
	            
	        <button id="showDeletePersonName" class="ur_button" 
 		            onmouseover="this.className='ur_buttonover';"
 		            onmouseout="this.className='ur_button';">Delete</button>
	        <br/>
	        <br/>
	         
	         <div id="personNames"></div>

			<div>
				<c:if test="${personNameAuthority.id != null}">
					<h3>Identifier Type Mappings</h3>
					<c:url value="/admin/editPersonNameAuthorityIdentifier.action"
						var="newIdentifierTypeMapperUrl">
						<c:param name="personNameAuthorityId"
							value="${personNameAuthority.id}" />
					</c:url>
					<a href="${newIdentifierTypeMapperUrl}">New Identifier</a>
					<br />
					<br />
					<div class="dataTable">

						<urstb:table width="100%">
							<urstb:thead>
								<urstb:tr>
									<urstb:td>Id</urstb:td>
									<urstb:td>Identifier Type</urstb:td>
									<urstb:td>Value</urstb:td>
									<urstb:td>Action</urstb:td>
								</urstb:tr>
							</urstb:thead>
							<urstb:tbody var="ident" oddRowClass="odd" evenRowClass="even"
								currentRowClassVar="rowClass"
								collection="${personNameAuthority.identifiers}">
								<urstb:tr cssClass="${rowClass}"
									onMouseOver="this.className='highlight'"
									onMouseOut="this.className='${rowClass}'">
									<urstb:td>
		                       ${ident.id}
	                        </urstb:td>
									<urstb:td>
			                   ${ident.personNameAuthorityIdentifierType.name}
	                        </urstb:td>
									<urstb:td>
			                   ${ident.value} 
	                        </urstb:td>
									<urstb:td>
										<c:url
											value="/admin/editPersonNameAuthorityIdentifier.action"
											var="editUrl">
											<c:param name="id" value="${ident.id}" />
										</c:url>
										<a href="${editUrl}">Edit</a> /<a
											href="javascript:YAHOO.ur.person.names.deleteIdentifierMapping(${ident.id});">Delete</a>
									</urstb:td>

								</urstb:tr>
							</urstb:tbody>
						</urstb:table>
					</div>
				</c:if>

			</div>

			<div id="newPersonNameDialog" class="hidden">
	             <div class="hd">Name Information</div>
		         <div class="bd">
		             <form id="addPersonName" name="newPersonNameForm" 
		                       method="post" 
		                       action="user/createPersonName.action">
		              
		             <input type="hidden" id="newPersonNameForm_id"
		                   name="id" value=""/>
		           
		             <input type="hidden" id="newPersonNameForm_personId"
		                   name="personId" value="${personId}"/>
		               
		             <input type="hidden" id="newPersonNameForm_new"
		                   name="newPersonName" value="true"/>
		              
		             <div id="personError" class="errorMessage"></div>

			           <table class="formTable">    
						    <tr>       
					            <td align="left" class="label">First Name:</td>
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
		          </form>
		      </div>
	      </div>
	  </div>
      <!--  end body div -->
      
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  </div>
  <!--  End  context div-->
	      
	      <div id="deletePersonNameDialog" class="hidden">
	          <div class="hd">Delete People</div>
		      <div class="bd">
		          <form id="deletePersonName" name="deletePersonName" method="post" 
		              action="user/deletePersonName.action">
		              
		              
		              <div id="deletePersonNameError" class="errorMessage"></div>
			          <p>Are you sure you wish to delete the selected people?</p>
		          </form>
		      </div>
	      </div>

	      <div id="deletePersonNameMessageDialog" class="hidden">
	          <div class="hd">Delete Name</div>
		      <div class="bd">
		              <div id="deletePersonNameMessage" class="errorMessage"></div>
		      </div>
	      </div>
	      
	      
	 <div id="deleteIdentifierDialog" class="hidden">
	    <div class="hd">Delete Identifier</div>
	    <div class="bd">
		    <p>Are you sure you wish to delete the selected Identifier?</p>
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