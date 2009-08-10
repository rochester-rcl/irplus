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
 	<ur:js src="page-resources/js/util/wait_dialog.js" />
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
            
        <form id="show_users" name="backToUsers" method="POST" action="<c:url value="/admin/viewUsers.action"/>">
        
        	<button class="ur_button" type="submit"
		                   onmouseover="this.className='ur_buttonover';"
		                   onmouseout="this.className='ur_button';"
		                   id="backToWorkspace"><span class="arrowBtnImg">&nbsp;</span> Back to Users</button>
		</form>
		
		<br/>

        <!--  set up tabs for editing users -->
        <div id="edit-user-tabs" class="yui-navset">
            <ul class="yui-nav">
                <li class="selected"><a href="#tab1"><em>User Information</em></a></li>
                <li><a href="#tab2"><em>Emails</em></a></li>
                <li><a href="#tab3"><em>Authoritative name</em></a></li>
                <li><a href="#tab4"><em>Accepted Repository Licenses</em></a></li>
            </ul>

            
            <div class="yui-content">
            
                <!--  first tab -->
                <div id="tab1">
                    <c:url var="reIndexUrl" value="/admin/reIndexUserWorkspace.action">
                        <c:param name="id" value="${irUser.id}"/>
                    </c:url>
                    <a href="${reIndexUrl}">Re Index User Workspace</a>  
                    <br/>
                    <strong> File system size : </strong> <ir:fileSizeDisplay sizeInBytes="${fileSystemSize}"/>
					
					<form id="editUser" name="editUserForm" method="post" 
		             	 action="<c:url value="/admin/updateUser.action"/>">
	                	
	                	<input type="hidden" id="editUserForm_id" name="id" value="${irUser.id}"/>   
	                  	
	                  	<div id="editUserDialogFields">
	                  	    <c:import url="edit_user_form.jsp"/>
	                  	</div>
		         	 </form>    
	                 <br/>
					 
					 <strong>General Information</strong>
					 <br/>
					 <br/>
					 Date Account Created: <fmt:formatDate pattern="MM/dd/yyyy HH:mm:ss" value="${irUser.createdDate}"/>
					 <br/>
					 <br/>
					 Personal Index Folder Location: ${irUser.personalIndexFolder}
					 <br/>
					 <br/>
					 Self Registered: ${irUser.selfRegistered}
					 <br/>
					 <br/>
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
			      
				      <br/>
				       <br/>
				       <div id="newEmails"></div>
				 </div>
	             <!--  end tab 2 -->
	             
                 <!--  Start third tab -->
                 <div id="tab3">
                     <table width="100%">
                         <tr>
                             <td width= "50%"></td>
                             
                             <td width= "50%">
								<form  id="search_form" name="searchForm"  
								     method="POST" action="javascript:YAHOO.ur.email.handleSearchFormSubmit();">
					             	Search For Name 
					             	<input type="text" id="search_query" name="query" size="30"/>
					             	<input type="hidden" id="search_userId" name="userId" value="${irUser.id}"/>
					             	<input type="hidden" id="number_of_results_to_show" name="numberOfResultsToShow" value="10"/>
					             	<input type="button" id="search_button" value="Search">	                                  
								</form>	 
                             </td>
                         </tr>

                         <tr>
                             <td><br/><br/></td>
                         </tr>
                         <tr>
                             <td valign="top">
		                       	<!--  user authoritative name -->
		                       	<div id="user_auth_name" >
		                       	    <c:import url="/pages/admin/user/user_authoritative_name.jsp"/>
		                        </div> 								
                             </td>
                             <td valign="top">
 								<!--  table of names -->
	                      		<div id="newNames"></div>
	                      		<!--  end table of names div -->
                             </td>
                         </tr>
                     </table>
                 </div>
                 <!--  End third tab -->
                 
                 <!--  start 4th tab -->
               	 <div id="tab4">
         		     <h3>Accepted Repository Submission Licenses</h3>
			         <c:import url="repository_accepted_licenses_table.jsp"/>
         		     
				 </div>
                 	             
	          </div>
	          <!--  end content -->
	       </div>
	       <!--  end tabs -->
	    
	     </div>
	  	<!--  End body div -->
	      
	  <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
   
   
   </div>  
   <!--  end doc div -->   
	<div id="newEmailDialog" class="hidden">
	      <div class="hd">Email Information</div>
		  <div class="bd">
		      <form id="addEmail" name="newEmailForm" 
		              method="post"  action="admin/createEmail.action">
	                 
	            <!--  if editing an id must be passed -->     
		   		<input type="hidden" id="newEmailForm_id"  name="id" value="${irUser.id}"/>
	   			 
		        <div id="newEmailDialogFields">
		            <c:import url="/pages/admin/user/email_form.jsp"/>
	            </div>
		    </form>
	    </div>
	</div>
	      
    <div id="deleteEmailDialog" class="hidden">
	    <div class="hd">Delete Email</div>
		<div class="bd">
		    <form id="deleteEmail" name="deleteEmail" method="POST" 
		              action="admin/deleteEmail.action">
		              
		        <input type="hidden" name="emailId" id="deleteEmailId" value=""/>
		        <div id="deleteEmailError" class="errorMessage"></div>
			    <p>Are you sure you wish to delete the selected emails?</p>
		    </form>
	    </div>
	</div>

    <div id="emailConfirmationDialog" class="hidden">
	    <div class="hd">Email Information</div>
		<div class="bd">
		    <div id="emailConfirmationDialogFields"> </div>
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