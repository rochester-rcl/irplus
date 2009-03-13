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
    <title>Manage User</title>
    
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>    
    <ur:styleSheet href="page-resources/css/base-ur.css"/>
    
    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    <ur:styleSheet href="page-resources/yui/tabview/assets/skins/sam/tabview.css"/>
    <ur:styleSheet href="page-resources/yui/tabview/assets/border_tabs.css"/>    

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

    <script type="text/javascript">
       var myTabs = new YAHOO.widget.TabView("edit-user-tabs");
    </script>
     
</head>

<body class=" yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
    
    <!--  this is the header of the page -->
    <c:import url="/inc/header.jsp"/>
    
    <h3>User Information</h3>
  
    <div id="bd">
            
        <ur:basicForm id="show_users" name="backToUsers" method="POST" action="admin/viewUsers.action">
        
        	<button class="ur_button" type="submit"
		                   onmouseover="this.className='ur_buttonover';"
		                   onmouseout="this.className='ur_button';"
		                   id="backToWorkspace"><span class="arrowBtnImg">&nbsp;</span> Back to Users</button>
		</ur:basicForm>
		
		<div class="clear">&nbsp;</div>            
        
        <strong> File system size : </strong> <ir:fileSizeDisplay sizeInBytes="${fileSystemSize}"/>
         
        <div class="clear">&nbsp;</div>    
         
        <!--  set up tabs for editing news -->
        <div id="edit-user-tabs" class="yui-navset">
            <ul class="yui-nav">
                <li class="selected"><a href="#tab1"><em>User Information</em></a></li>
                <li><a href="#tab2"><em>Emails</em></a></li>
                <li><a href="#tab3"><em>Authoritative name</em></a></li>
            </ul>

            
            <div class="yui-content">
            
                <!--  first tab -->
                <div id="tab1">
					<ur:basicForm id="editUser" name="editUserForm" method="post" 
		             	 action="admin/updateUser.action">
	                	
	                	<input type="hidden" id="editUserForm_id" name="id" value="${irUser.id}"/>   
	                  	
	                  	<div class="clear">&nbsp;</div>
	                  	
	                  	<ur:div id="editUserDialogFields">
	                      	 <%@ include file="/pages/admin/user/edit_user_form.jsp" %>
	                  	</ur:div>
		         	 </ur:basicForm>    
					 
					 <div class="clear">&nbsp;</div>
					 
					 <button id="save_user" class="ur_button" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.email.saveEditUser()";>Save</button>		         	       
          
		        </div>
	            <!--  end first tab -->
	                  
	                  
              	 <!--  start second tab -->
               	 <div id="tab2">
         		 
         		 <br/>
			                <button id="showEmail" class="ur_button" 
	 		                               onmouseover="this.className='ur_buttonover';"
	 		                               onmouseout="this.className='ur_button';">New Email</button> 
		                    <button id="showDeleteEmail" class="ur_button" 
	 		                               onmouseover="this.className='ur_buttonover';"
	 		                               onmouseout="this.className='ur_button';">Delete</button>
			      
				  <br/>
				  <br/>
				  <ur:div id="newEmails"></ur:div>
				  
				  
				
				 </div>
	             <!--  end tab 2 -->
	             
                 <!--  Start third tab -->
                 <div id="tab3">
                     <table width="100%">
                         <tr>
                             <td width= "50%"></td>
                             
                             <td width= "50%">
								<ur:basicForm  id="search_form" name="searchForm"  
								     method="POST" action="javascript:YAHOO.ur.email.handleSearchFormSubmit();">
					             	Search For Name 
					             	<input type="text" id="search_query" name="query" size="30"/>
					             	<input type="hidden" id="search_userId" name="userId" value="${irUser.id}"/>
					             	<input type="hidden" id="number_of_results_to_show" name="numberOfResultsToShow" value="10"/>
					             	<input type="button" id="search_button" value="Search">	                                  
								</ur:basicForm>	 
                             </td>
                         </tr>

                         <tr>
                             <td><br/><br/></td>
                         </tr>
                         <tr>
                             <td valign="top">
		                       	<!--  user authoritative name -->
		                       	<div id="user_auth_name" >
		                       		<%@ include file="/pages/admin/user/user_authoritative_name.jsp" %>
		                        </div> 								
                             </td>
                             <td valign="top">
 								<!--  table of names -->
	                      		<ur:div id="newNames"></ur:div>
	                      		<!--  end table of names div -->
                             </td>
                         </tr>
                     </table>
                 </div>
                 <!--  End third tab -->	             
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
		              method="post"  action="admin/createEmail.action">
	                 
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
   <!--  end doc div -->    


</body>
</html>