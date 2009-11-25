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

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<!--  images used by the page -->
<html>
    <head>
        <title>Add Contributor(s): ${item.fullName}</title>
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
        <ur:js src="page-resources/js/util/wait_dialog.js" />
        <ur:js src="page-resources/js/menu/main_menu.js"/>
  		<ur:js src="page-resources/js/user/add_contributor.js"/>
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body region of the page -->
            <div id="bd">
            
            	<div class="clear">&nbsp;</div>
				<p><strong> Add Contributors to Publication :  </strong> <span class="noBorderTableGreyLabel">${personalItem.fullPath}${item.fullName} </span> </p>
				
				<table width="735"  align="center"  height="48" >
                  	<tr>
                                           
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                     
                          <a href="javascript:YAHOO.ur.item.contributor.viewAddFiles();">Add Files</a>
                      </td>
                      
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                          <a href="javascript:YAHOO.ur.item.contributor.viewAddMetadata();"> Add Information </a>
                      </td>
                      
                       <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/bluearrow.jpg">
                          Add Contributors
                      </td>
                      
                      <td width="150" align="center" background="${pageContext.request.contextPath}/page-resources/images/all-images/greyarrow.jpg">
                          <a href="javascript:YAHOO.ur.item.contributor.gotoPreview();"> Preview Publication </a>
                      </td>                      
                  </tr>
                </table>
                
				<table width="100%">
                  <tr>
                      <td align="left" width="100">
                          <button class="ur_button" id="saveItem" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.contributor.finishLater();">Finish Later</button>
                      </td>
                      <td align="right">
                          <button class="ur_button" id="goto_previous" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.contributor.viewAddMetadata()";>Previous</button>
                      </td>                      
                      <td align="right" width="100">
                          <button class="ur_button" id="goto_next" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.contributor.gotoPreview();">Next</button>
                      </td>                      
                  </tr>
                </table>
                
				<br/>                               
                     
                     
                <table width="100%">
                    <tr>
                        <td align="left" width="50%">          
				            <form  id="search_form" name="searchForm"  
				               method="POST" action="javascript:YAHOO.ur.item.contributor.handleSearchFormSubmit();">
	             	           First Search For Name 
	             	            <input type="text" id="search_query" name="query" size="30"/>
	             	            <input type="hidden" id="search_itemId" name="genericItemId" value="${item.id}"/>
	             	            <input type="hidden" id="search_pcId" name="parentCollectionId" value="${parentCollectionId}"/>
	             	            <input type="hidden" id="search_iItemId" name="institutionalItemId" value="${institutionalItemId}"/>
	             	            <button id="search_button" class="ur_button" 
		 		                    onmouseover="this.className='ur_buttonover';"
		 		                    onmouseout="this.className='ur_button';">Search</button>
	             	                         
				            </form>	 
				        </td>
				  
				        <td>
				            if you can't find the name then
				
				            <button class="ur_button" id="new_person" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               >Add New Person</button>                    
			            </td>
			         </tr>
			     </table>
				
				
       	        <div class="yui-g">
			        <div class="yui-u first">
			        
		       			 <!--  table of names -->
	                      <div id="newNames">
								<form method="post" id="names" name="myNames">
								
									 <input type="hidden" id="names_query" name="query" value="${query}"/>
									 <input type="hidden" id="names_itemId" name="genericItemId" value="${item.id}"/>
							
								</form>	                      
	                      </div>
	                      <!--  end table of names div -->
		       
		             </div>
		             <!--  end the first column -->
            
        	        <div class="yui-u">
	        	      <form id="contributors_form" name="contributors_form"  method="POST" action="user/getContributors.action">
                              
                          <input type="hidden" id="contributors_form_itemId" name="genericItemId" value="${item.id}"/>
                          <input type="hidden" id="contributors_form_parent_collection_id" name="parentCollectionId" value="${parentCollectionId}"/>
                          <input type="hidden" id="contributors_form_inst_item_id" name="institutionalItemId" value="${institutionalItemId}"/>
	                    	
	                      <!--  Table of selected names -->
	                      <div id="item_contributors" >
	                      </div>
                  		  <!--  end table of selected names div -->
                      </form>
            	    </div>
                	<!--  end the second column -->
                </div>
                <!--  end the grid -->
				
				<br/>
				
				<table width="100%" >
                  <tr>
                      <td align="left" width="100">
                          <button class="ur_button" id="saveItem" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.contributor.finishLater();">Finish Later</button>
                      </td>
                      <td align="right">
                          <button class="ur_button" id="goto_previous" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.contributor.viewAddMetadata()";>Previous</button>
                      </td>                      
                      <td align="right" width="100">
                          <button class="ur_button" id="goto_next" 
                               onmouseover="this.className='ur_buttonover';"
                               onmouseout="this.className='ur_button';"
                               onclick="javascript:YAHOO.ur.item.contributor.gotoPreview();">Next</button>
                      </td> 
                  </tr>
                </table>

                
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
        
        <div id="newPersonDialog" class="hidden">
	        <div class="hd">Person Information</div>
		    <div class="bd">
		        <form id="add_person" name="newPersonForm" 
		              method="post" 
		              action="user/addNewPerson.action">
		              
   	            <input type="hidden" id="person_id"
		                   name="personId" value="${user.personNameAuthority.id}"/>
		
  				<input type="hidden" id="user_id"
		                   name="addToUserId" value="${user.id}"/>		                   
		              
		        <div id="personError" class="errorMessage"></div>
			          
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
					    <td align="left" class="label">Birth Year:</td>
					    <td align="left" class="label">
				          		Year:<input  type="text" id="person_birthdate_year" name="birthYear" size="4" maxlength ="4"/> </td>
					</tr>
					<tr>       
					    <td align="left" class="label">Death Year:</td>
						<td align="left" class="label">

				          	Year:<input type="text" id="person_deathdate_year" name="deathYear" size="4" maxlength ="4"/>
				       	</td>
					</tr>
					<tr>
					    <td align="left" class="label">My Name:</td>
					    <td align="left" class="input"><input type="checkbox" 
							          id="person_myName" name="myName" value="true"/>	
						</td>
					</tr>
			    </table>
		        </form>
		        </div>
	        </div>


	        <div id="newPersonNameDialog" class="hidden">
	            <div class="hd">Name Information</div>
		        <div class="bd">
		            <form id="addPersonName" name="newPersonNameForm" 
		                       method="post" 
		                       action="user/addNewPersonName.action">
		              
		            <input type="hidden" id="newPersonNameForm_personId"
		                   name="personId" />
		              
		            <div id="personNameError" class="errorMessage"></div>
			          
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
		        </form>
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

    
