<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />


<!--  
   Copyright 2008-2010 University of Rochester

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
        <title>${researcher.user.firstName}&nbsp;${researcher.user.lastName}</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!-- Core + Skin CSS -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
        <ur:styleSheet href="page-resources/css/base-ur.css"/>
        <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>
        <ur:styleSheet href="page-resources/yui/treeview/assets/skins/sam/treeview.css"/>          

        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/home_page_content_area.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>
        <ur:styleSheet href="page-resources/css/tree.css"/>

        <!-- Dependencies --> 
        <ur:js src="page-resources/yui/utilities/utilities.js"/>
        <ur:js src="page-resources/yui/container/container-min.js"/>
        <ur:js src="page-resources/yui/menu/menu-min.js"/>
        <ur:js src="page-resources/yui/json/json-min.js"/>
        <ur:js src="page-resources/yui/treeview/treeview-min.js"/> 
        
        
        <!-- Source File -->
        <ur:js src="page-resources/js/menu/main_menu.js"/>
        <ur:js src="pages/js/base_path.js"/>
        <ur:js src="page-resources/js/util/ur_util.js"/>
 	    <ur:js src="page-resources/js/public/researcher_page_view.js"/> 
      
        <!--  styling for page specific elements -->
        <style type="text/css">
            .ur_button
            {
                width: 100px;
                margin: 0px;
                padding: 0px;
            }
            
            .ur_buttonover
            {
                width: 100px;
                margin: 0px;
                padding: 0px;
            }
            
		#treeDiv { background: #fff; padding:1em; margin-top:1em; }

        </style>
        
            
    
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body regin of the page -->
            <div id="bd">
 				
				 <input type="hidden" id="researcher_id" value="${researcher.id}"/>
				  <c:if test="${researcher != null && (!researcher.public)}">
	                	<h3 class="errorMessage">The researcher page of ${researcher.user.firstName}&nbsp;${researcher.user.lastName} is private.</h3>
                  </c:if>

                <c:if test="${researcher == null}">
                	<br/>
                	
                	The researcher page is not available.
                </c:if>
				 
				<c:if test="${researcher != null && (researcher.public || user.id == researcher.user.id)}">
	            	<table width="100%">
	            	<tr> <td width="9%">
		            		<img src="${pageContext.request.contextPath}/page-resources/images/all-images/researcher.jpg" height="50" width="55"/>
	            		</td> 
	            		<td>
		                    <h4> <font color="grey" ><i> The Researcher page of </i> </font> </h4>
		                    <font size="+2" > ${researcher.user.firstName}&nbsp;${researcher.user.lastName}  </font>
		                </td> 
		                <td align="right" valign="bottom">
							<c:if test="${user != null && user.id == researcher.user.id}">	
							    <c:url var="editResearcherPageUrl" value="/user/viewResearcher.action">
					        	    <c:param name="researcherId" value="${researcher.id}"/>
					            </c:url>
						        <a href="${editResearcherPageUrl}">Edit Researcher Page</a> 
					        </c:if>	                
		                </td>
	                </tr>
	                </table>

		            	                
	                <!--  create the first column -->
	                <div class="yui-g">
	                <div class="yui-u first">
	                   
	                   
	                   <div class="contentContainer">
	                       <div class="contentBoxTitle">
	                           <p>${researcher.user.firstName}&nbsp;${researcher.user.lastName}  </p>
	                       </div>
	                   
	                       <div class="contentBoxContent">
								<div id="researcher_picture" >
	                            </div>
	                            <div> 
					               <p>
					                <c:if test="${researcher.user.personNameAuthority != null}">
					                    <c:url var="contributorUrl" value="/viewContributorPage.action">
									        <c:param name="personNameId" value="${researcher.user.personNameAuthority.authoritativeName.id}"/>
									    </c:url>
					                    All work in: <a href="${contributorUrl}"> ${repository.name}</a>
					                </c:if>
								   </p>
									
								   <p> <strong> Researcher Information </strong>  <br/><br/>
								      <c:if test="${!ir:isStringEmpty(researcher.campusLocation)}">
		                              <strong>Title:</strong><br/><br/>${researcher.title} <br/><br/>
		                              </c:if>
		                              <c:if test="${!ur:isEmpty(researcher.user.departments)}">
		                                  <strong>Department(s):</strong><br/><br/>
		                                  <c:forEach items="${researcher.user.departments}" var="department">
		                                      ${department.name}<br/>
		                                  </c:forEach>
		                                  <br/>
		                              </c:if>
		                              <c:if test="${!ur:isEmpty(researcher.fields)}">
		                                  <strong>Field(s):</strong><br/><br/>
		                                  <c:forEach items="${researcher.fields}" var="field">
		                                      ${field.name}<br/>
		                                  </c:forEach>
		                                  <br/><br/>
		                              </c:if>
		                              <c:if test="${!ir:isStringEmpty(researcher.campusLocation)}">
		                              <strong>Location:</strong> <br/><br/>${researcher.campusLocation} <br/><br/>
		                              </c:if>
		                              <c:if test="${!ir:isStringEmpty(researcher.phoneNumber)}">
		                              <strong>Phone:</strong><br/><br/> ${researcher.phoneNumber} <br/><br/>
		                              </c:if>
		                              <c:if test="${!ir:isStringEmpty(researcher.email)}">
		                              <strong>Email:</strong><br/><br/> ${researcher.email} <br/><br/>
		                              </c:if>
		                              <c:if test="${!ir:isStringEmpty(researcher.fax)}">
		                           	  <strong>Fax:</strong> <br/><br/>${researcher.fax} <br/><br/>
		                           	  </c:if>
		                           </p>
		                           
		                           <c:if test="${ur:collectionSize(researcher.personalLinks) > 0}">
		                           <p> <strong> Links </strong>  <br/><br/>
		                               <c:forEach var="link" items="${researcher.personalLinks}">
		                                   <a href="${link.url}">${link.name}</a> <c:if test="${link.description != null}"> <br> ${link.description} </c:if><br/><br/>
		                               </c:forEach>
		                           </p>
		                           </c:if>
		                           
		                           <c:if test="${!ir:isStringEmpty(researcher.researchInterest)}">
		                           <p> <strong> Research Interests </strong>  <br/> ${researcher.researchInterest}</p>
		                           </c:if>
		                           
		                           <c:if test="${!ir:isStringEmpty(researcher.teachingInterest)}">
		                           <p> <strong> Teaching Interests </strong>  <br/> ${researcher.teachingInterest}</p>
		                           </c:if>
								</div>																												
	                       </div>
	                   </div>
	                   
	                   
	                    
	                </div>
	                <!--  end the first column -->
	            
	                <div class="yui-u">
	                    
	                     <div class="contentContainer">
	                       <div class="contentBoxTitle">
	                           <p>Research</p>
	                       </div>
	                   
	                       <div class="contentBoxContent"> 
	                           <div id="treeDiv"><ir:drawResearcherFolder researcher="${researcher}"/></div>
	                       </div>
	                     </div>
	                    
	                </div>
	                <!--  end the second column -->
	                
	                </div>
	                <!--  end the grid -->
                </c:if>
                
               
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
