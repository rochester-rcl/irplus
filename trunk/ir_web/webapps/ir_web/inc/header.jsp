<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>

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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="messages"/>
<!-- page header - this uses the yahoo page styling -->
<div id="hd">
   
   <div id="header">
       <div id="header_logo">
           <a href="<c:url value="/home.action"/>"><img align="left" alt="irplus" src="${pageContext.request.contextPath}/page-resources/images/all-images/ir_plus_logo.jpg"/></a>  
       </div>
       <div id="header_links">
           <a href="">Help</a> | <a href="">Contact Us</a> | <a href="">About</a> | <a href="">Privacy Policy</a>
        
       </div>
   </div>
  
    
    <div id="mainMenu" class="yuimenubar yuimenubarnav">
        <div class="bd">
            <ul class="first-of-type">

            
            <c:if test="${user == null}">
                    <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="<c:url value="/user/workspace.action"/>"><span class="doorInImg">&nbsp;</span>Login</a>
                    </li>    
            </c:if>
            <c:if test="${user != null}">
                    <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="<c:url value="/j_spring_security_logout"/>"><span class="logoutImg">&nbsp;</span>Logout</a>
                    </li>    
            </c:if>
 
            <!--  user cannot be null -->
            <c:if test="${user != null}">
                <c:if test='${ir:userHasRole("ROLE_ADMIN,ROLE_COLLECTION_ADMIN", "OR")}'>
                   <li class="yuimenubaritem"><a class="yuimenubaritemlabel" href="<c:url value="/admin/adminHome.action"/>"><span class="wrenchImg">&nbsp;</span>Administration</a>
                   <div id="administration" class="yuimenu">
                       <div class="bd">                                        
                            <ul>
                                <c:if test='${ir:userHasRole("ROLE_ADMIN","OR")}'>
                                    <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewRepository.action" />" >Repository</a></li>
                                </c:if>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewInstitutionalCollections.action" />" >Institutional Collections</a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewUserGroups.action" />" >User Groups</a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewContentTypes.action"/>" ><fmt:message key="menu.viewContentTypes"/></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewLanguageTypes.action"/>" ><fmt:message key="menu.viewLanguageTypes" /></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewIdentifierTypes.action"/>"><fmt:message key="menu.viewIdentifierTypes" /></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewLicenses.action"/>"><fmt:message key="menu.viewLicenses" /></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewNewsItems.action"/>"><fmt:message key="menu.viewNewsItem" /></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewContributorTypes.action"/>"><fmt:message key="menu.viewContributorTypes" /></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewSponsors.action"/>"><fmt:message key="menu.viewSponsors" /></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewPublishers.action"/>"><fmt:message key="menu.viewPublishers" /></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewSeries.action"/>"><fmt:message key="menu.viewSeries" /></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewPersons.action" />">Person Name Authority</a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewUsers.action" />"><fmt:message key="menu.viewUsers" /></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewPendingApproval.action" />"><fmt:message key="menu.viewPendingApproval" /></a></li>
                                <c:if test='${ir:userHasRole("ROLE_ADMIN", "OR")}'>
                                    <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewRoles.action" />"><fmt:message key="menu.ir_roles" /></a></li>
                                </c:if>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewTopMediaTypes.action" />"><fmt:message key="menu.viewTopMediaTypes" /></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewAffiliations.action" />"><fmt:message key="menu.affiliations" /></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewDepartments.action" />"><fmt:message key="menu.departments" /></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewFields.action" />"><fmt:message key="menu.fields" /></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewIgnoreIpAddresses.action" />"><fmt:message key="menu.ignoreIpAddress" /></a></li>
				                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewExtentTypes.action"/>"><fmt:message key="menu.viewExtentTypes" /></a></li>
				                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewReviewPendingItems.action"/>">Review Pending Publications</a></li>
				                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewHandleNameAuthorities.action"/>">Handle Name Authorities</a></li>

                            </ul>
                        </div>
                    </div>                
                    </li>
                    </c:if>
                    <c:if test='${ir:userHasRole("ROLE_COLLABORATOR,ROLE_AUTHOR,ROLE_RESEARCHER,ROLE_ADMIN", "OR")}'>
                    <li class="yuimenubaritem"><a class="yuimenubaritemlabel" href="<c:url value="/user/workspace.action"/>"><span class="report_editImg">&nbsp;</span>Workspace</a>
                        <div id="workspace" class="yuimenu">
                            <div class="bd">                                        
                                <ul>
                                    <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/user/workspace.action"/>">Files and Folders</a></li>
                                    <c:if test='${ir:userHasRole("ROLE_RESEARCHER", "OR")}' >
                               		    <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/user/viewResearcher.action"/>">Researcher Page</a></li>
                                    </c:if>
                                </ul>
                            </div>
                        </div>                                        
                    </li>
                    </c:if> 
                </c:if>
                
                <c:if test="${user != null}">
                    <li class="yuimenubaritemleft"><a class="yuimenubaritemlabel" href="<c:url value="/home.action"/>"><span class="userImg">&nbsp;</span>Welcome ${user.username}</a>
                        <div id="welcome" class="yuimenu">
                            <div class="bd">                                        
                                <ul>
                                    <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/user/myAccount.action"/>">My Account</a></li>
                                </ul>
                            </div>
                        </div>
                    </li>    
                </c:if>
            </ul>            
        </div>
    </div>
</div>
<!--  end header -->