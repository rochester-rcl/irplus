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
<%@ taglib prefix="ir" uri="ir-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title>Edit Account</title>
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

	    <ur:js src="pages/js/base_path.js"/>
	    <ur:js src="page-resources/js/util/ur_util.js"/>
	    <ur:js src="page-resources/js/menu/main_menu.js"/>
	 	
	 	<ur:js src="pages/js/ur_table.js"/>
 	    <ur:js src="page-resources/js/admin/edit_user.js"/>
        <ur:js src="page-resources/js/user/edit_my_account.js"/>
        <ur:js src="page-resources/js/admin/edit_person.js"/>
  
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body regin of the page -->
            <div id="bd">
            	
            	 <h3> ${user.username}'s account </h3>
		        
		        <!--  set up tabs for editing news -->
		        <div id="user-account-tabs" class="yui-navset">
		            <ul class="yui-nav">
		                <li class="selected"><a href="#tab1"><em>User Information</em></a></li>
		                <li><a href="#tab2"><em>Emails</em></a></li>
		                <li><a href="#tab3"><em>Publication Names</em></a></li>
		            </ul>
		
		            <div class="yui-content">
		            
		                <!--  first tab -->
		                <div id="tab1">
		                  <div class="clear">&nbsp;</div>
		                  
		                  <button class="ur_button" id="show_change_password" 
	                               onmouseover="this.className='ur_buttonover';"
	                               onmouseout="this.className='ur_button';"
	                               >Change Password</button>

	                	  <ur:basicForm id="addUser" name="newUserForm" method="post" 
			            	  action="user/saveMyAccount.action" onSubmit="return  YAHOO.ur.user.account.formValidation();">
			              
			              <input type="hidden" id="user_id" name="userId" value="${irUser.id}"/>
			               
						  <table class="formTable">
						  <tr>
						  <td colspan="2">
					          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
			                       key="userNameError"/></p>
							  <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
			                       key="emailExistError"/></p>			                       
	
				          </td>
				          </tr>    
	
				          <tr>
				          <td>
					          User Name:
						  </td>
						  <td> ${irUser.username}
					     </td>
					     </tr>
	
						  <tr>
						  <td>
					          First Name:
				          </td>
				          <td>
					          <input type="text" class="input"
					              id="newUserForm_first_name" name="irUser.firstName" value="${irUser.firstName}" size="40"/> 
					              
				          </td>
				          </tr>    
	
						  <tr>
						  <td>
				
					          Last Name:
					      </td>
					      <td>
					      		<input type="text" class="input"  
					              id="newUserForm_last_name" name="irUser.lastName" value="${irUser.lastName}" size="40"/>
					      </td>
					      </tr>
	
	
				          <tr>
				            <td>
				              Phone Number:
			                </td>
			                <td>
			               		<input type="text" class="input"  
				              id="newUserForm_phone_number" name="irUser.phoneNumber" value="${irUser.phoneNumber}" size="40"/>
		                      </td>
	                      </tr>
	                      
	                      
	                      <tr>
	                      <td colspan="2" align="center">
								<button class="ur_button"  type="submit"
	                               onmouseover="this.className='ur_buttonover';"
	                               onmouseout="this.className='ur_button';"
	                               >Save Account</button>	                     

	                      </td>
	                      </tr>
	                      </table>
			      
			          </ur:basicForm>
			          
		        </div>
	            <!--  end first tab -->
	                  
	                  
              	 <!--  start second tab -->
               	 <div id="tab2">
			          <br/>
					              <button class="ur_button" id="showEmail" 
	                               onmouseover="this.className='ur_buttonover';"
	                               onmouseout="this.className='ur_button';"
	                               >New Email</button>
					              <button class="ur_button" id="showDeleteEmail" 
	                               onmouseover="this.className='ur_buttonover';"
	                               onmouseout="this.className='ur_button';"
	                               >Delete Email</button>
				      <br/>
				      <br/>
				    <input type="hidden" id="editUserForm_id" value="${irUser.id}">
					<ur:div id="newEmails"></ur:div>
					<div class="clear">&nbsp;</div>
					
				 </div>
	             <!--  end tab 2 -->
	             
				<!--  start third tab -->
               	 <div id="tab3">
			          <br/>
				    
					              <button class="ur_button" id="showPersonName" 
	                               onmouseover="this.className='ur_buttonover';"
	                               onmouseout="this.className='ur_button';"
	                               >New Name</button>
				              
									<button class="ur_button" id="showDeletePersonName" 
	                               onmouseover="this.className='ur_buttonover';"
	                               onmouseout="this.className='ur_button';"
	                               >Delete Name</button>				              
				  
				      <br/>
				      <br/>
					
					<ur:div id="personNames"></ur:div>
					
					<div class="clear">&nbsp;</div>
					
				 </div>
	             <!--  end tab 3 -->	             
	          </div>
	          <!--  end content -->
	       </div>
	       <!--  end tabs -->
	    
	     </div>
	  	<!--  End body div -->
					
		
	     <ur:div id="newEmailDialog" cssClass="hidden">
	          <ur:div cssClass="hd">Email Information</ur:div>
		      <ur:div cssClass="bd">
		          <ur:basicForm id="addEmail" name="newEmailForm" 
		              method="post" 
		              action="admin/createEmail.action">
		             
		             <!--  if editing an id must be passed -->     
		   			 <input type="hidden" id="newEmailForm_id"  name="id" value="${irUser.id}"/>
		              
		              <ur:div id="newEmailDialogFields">
	                   <%@ include file="/pages/admin/user/email_form.jsp" %>
	                  </ur:div>
		              
		          </ur:basicForm>
		      </ur:div>
	      </ur:div>
	      
	      <ur:div id="deleteEmailDialog" cssClass="hidden">
	          <ur:div cssClass="hd">Delete Email</ur:div>
		      <ur:div cssClass="bd">
		          <ur:basicForm id="deleteEmail" name="deleteEmail" method="POST" 
		              action="admin/deleteEmail.action">
		              
		              
		              <ur:div id="deleteEmailError" cssClass="errorMessage"></ur:div>
			          <p>Are you sure you wish to delete the selected emails?</p>
		          </ur:basicForm>
		      </ur:div>
	      </ur:div>

	      <ur:div id="change_password_dialog" cssClass="hidden">
	          <ur:div cssClass="hd">Change password</ur:div>
		      <ur:div cssClass="bd">
		          <ur:basicForm id="change_password_form" name="changePasswordForm" method="POST" 
		              action="user/changePassword.action">
			         
			         <input type="hidden" id="change_password_form_id"  name="userId" value="${irUser.id}"/>
					  <ur:div id="new_password_dialog_fields">
	                  	 <%@ include file="/pages/user/login/change_password_form.jsp" %>
	                  </ur:div>			         
			          
		          </ur:basicForm>
		      </ur:div>
	      </ur:div>

	     <ur:div id="newPersonNameDialog" cssClass="hidden">
	         <ur:div cssClass="hd">Name Information</ur:div>
	         <ur:div cssClass="bd">
	             <ur:basicForm id="addPersonName" name="newPersonNameForm" 
	                       method="post" 
	                       action="user/createPersonName.action">
		           	
		           	<input type="hidden" id="newPersonNameForm_id"
		                   name="id" value=""/>
   	             	<input type="hidden" id="newPersonNameForm_personId"
		                   name="personId" value="${irUser.personNameAuthority.id}"/>
		                   
  					<input type="hidden" id="newPersonNameForm_userId"
		                   name="userId" value="${irUser.id}"/>		                   
		               
		         	<input type="hidden" id="newPersonNameForm_new"
		                   name="newPersonName" value="true"/>
					
					<input type="hidden" id="newPersonNameForm_myName"
		                   name="myName" value="true"/>		                   
		                   	             
	             <ur:div id="personError" cssClass="errorMessage"></ur:div>
		          
		         <label for="newPersonNameFormFirstName">First Name:</label><input type="text" 
		              id="newPersonNameFormFirstName" 
		              name="personName.forename" 
		              value=""/>
		          
		         <div class="clear">&nbsp;</div>
		          
		         <label for="newPersonNameFormLastName">Last Name:</label><input type="text" 
		              id="newPersonNameFormLastName" 
		              name="personName.surname" 
		              value=""/>
		          
		         <div class="clear">&nbsp;</div>
		          
		         <label for="newPersonNameFormNameMiddleName">Middle Name:</label><input type="text" 
		              id="newPersonNameFormMiddleName" 
		              name="personName.middleName" 
		              value=""/>
		          
		         <div class="clear">&nbsp;</div>
		          
		         <label for="newPersonNameFormNameFamilyName">Family Name:</label><input type="text" 
		             id="newPersonNameFormFamilyName" 
		             name="personName.familyName" 
		             value=""/>
		          
		         <div class="clear">&nbsp;</div>
		          
		         <label for="newPersonNameFormInitials">Initials:</label><input type="text" 
		              id="newPersonNameFormInitials" 
		              name="personName.initials" 
		              value=""/>
		          
		         <div class="clear">&nbsp;</div>
		          
		         <label for="newPersonNameFormNumeration">Numeration:</label><input type="text" 
		              id="newPersonNameFormNumeration" 
		              name="personName.numeration" 
		              value=""/>
		          
		         <div class="clear">&nbsp;</div>
	             <label for="newPersonNameFormAuthoritative">Authoritative Name</label><input 
	                   id="newPersonNameFormAuthoritative"
	                   type="checkbox" name="authoritative" value="true"/>
	          </ur:basicForm>
	      </ur:div>
	  </ur:div>
	  
	  <ur:div id="deletePersonNameDialog" cssClass="hidden">
	      <ur:div cssClass="hd">Delete People</ur:div>
	      <ur:div cssClass="bd">
	          <ur:basicForm id="deletePersonName" name="deletePersonName" method="POST" 
	              action="user/deletePersonName.action">
	              
	              
	              <ur:div id="deletePersonNameError" cssClass="errorMessage"></ur:div>
		          <p>Are you sure you wish to delete the selected name(s)?</p>
	          </ur:basicForm>
	      </ur:div>
	  </ur:div>
	
	  <ur:div id="deletePersonNameMessageDialog" cssClass="hidden">
	      <ur:div cssClass="hd">Delete Name</ur:div>
	      <ur:div cssClass="bd">
	              <ur:div id="delete_email_error" cssClass="errorMessage"></ur:div>
	      </ur:div>
	  </ur:div>

     <ur:div id="emailConfirmationDialog" cssClass="hidden">
          <ur:div cssClass="hd">Email Information</ur:div>
	      <ur:div cssClass="bd">
	              <ur:div id="emailConfirmationDialogFields">
                  </ur:div>
	              
	      </ur:div>
      </ur:div>	 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
