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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="messages"/>
<!-- page header - this uses the yahoo page styling -->
<div id="hd">
   
   <div id="header">
       <div id="header_logo">
           <a href="<c:url value="/home.action"/>"><img align="left" alt="Home Page" src="${pageContext.request.contextPath}/page-resources/images/all-images/ir_plus_logo.jpg"/></a>  
       </div>
       <div id="header_links">
           <a href="<c:url value="/help.action"/>">User Help</a> | <a href="<c:url value="/admin-help.action"/>">Administration Help</a> | <a href="<c:url value="/contactUs.action"/>">Contact Us</a> | <a href="<c:url value="/about.action"/>">About</a> | <a href="<c:url value="/privacyPolicy.action"/>">Privacy Policy</a>
           <br>
           <c:url var="poweredByImage" value="/page-resources/images/all-images/poweredby.gif"/>
           <a href="http://www.irplus.org/"><img class="powered_by" alt="Powered By IR Plus" src="${poweredByImage}"></a>
       </div>
   </div>
  
    
    <div id="mainMenu" class="yuimenubar yuimenubarnav">
        <div class="bd">
            <ul class="first-of-type">

            
            <c:if test="${user == null}">
                    <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="<c:url value="viewUserRegistration.action"/>"><span class="userImg">&nbsp;</span><span class="menu_text">Create Account</span></a></li>    
                    <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="<c:url value="/user/workspace.action"/>"><span class="doorInImg">&nbsp;</span><span class="menu_text">Login</span></a></li>    
            </c:if>
            <c:if test="${user != null}">
                    <li class="yuimenubaritem first-of-type"><a class="yuimenubaritemlabel" href="<c:url value="/j_spring_security_logout"/>"><span class="logoutImg">&nbsp;</span><span class="menu_text">Logout</span></a>
                    </li>    
            </c:if>
 
            <!--  user cannot be null -->
            <c:if test="${user != null}">
                <c:if test='${ir:userHasRole("ROLE_ADMIN,ROLE_COLLECTION_ADMIN", "OR")}'>
                   <li class="yuimenubaritem"><a class="yuimenubaritemlabel" href="#administration"><span class="wrenchImg">&nbsp;</span><span class="menu_text">Administration</span></a>
                   <div id="administration" class="yuimenu">
                       <div class="bd">                                        
                            <ul>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewAffiliations.action" />"><span class="menu_text"><fmt:message key="menu.affiliations" /></span></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewPendingApproval.action" />"><span class="menu_text"><fmt:message key="menu.viewPendingApproval" /></span></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewContentTypes.action"/>" ><span class="menu_text"><fmt:message key="menu.viewContentTypes"/></span></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewContributorTypes.action"/>"><span class="menu_text"><fmt:message key="menu.viewContributorTypes" /></span></a></li>
                                
               
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewCopyrightStatements.action"/>"><span class="menu_text">Copyright Statements</span></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewDepartments.action" />"><span class="menu_text"><fmt:message key="menu.departments"/></span></a></li>
                                <li class="yuimenuitem">
                                    <a class="yuimenuitemlabel">Dublin Core Mappings</a>

                                    <div id="dcmapping" class="yuimenu">
                                        <div class="bd">                    
                                            <ul>
                                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewContributorTypeDublinCoreMappings.action" />"><span class="menu_text">Contributor Type DC Mapping</span></a></li>
                                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewIdentifierTypeDublinCoreMappings.action" />"><span class="menu_text">Identifier Type DC Mapping</span></a></li>
                                            </ul>
                                        </div>
                                    </div>                    
                                </li>
                                
				                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewExtentTypes.action"/>"><span class="menu_text"><fmt:message key="menu.viewExtentTypes" /></span></a></li>
                                <c:if test='${ir:userHasRole("ROLE_ADMIN","OR")}'>
                                    <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewExternalAccountTypes.action"/>"><span class="menu_text">External Account Types</span></a></li>
                                </c:if>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewFields.action" />"><span class="menu_text"><fmt:message key="menu.fields"/></span></a></li>
				                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewFileStorage.action"/>"><span class="menu_text">File Storage</span></a></li>
				                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewHandleNameAuthorities.action"/>"><span class="menu_text">Handle Name Authorities</span></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewIdentifierTypes.action"/>"><span class="menu_text"><fmt:message key="menu.viewIdentifierTypes"/></span></a></li>
                                
                                
                                
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewInstitutionalCollections.action" />"><span class="menu_text">Institutional Collections</span></a></li>
                                
                                <li class="yuimenuitem">
                                    <a class="yuimenuitemlabel">IP Address / Statistics</a>

                                    <div id="ipstats" class="yuimenu">
                                        <div class="bd">                    
                                            <ul>
                                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewIgnoreIpAddresses.action" />"><span class="menu_text"><fmt:message key="menu.ignoreIpAddress"/></span></a></li>
                                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewDownloadCountsByIp.action" />"><span class="menu_text">Download Counts By IP Address</span></a></li>
                                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewIgnoreDownloadCountsByIp.action" />"><span class="menu_text">Ignore Download Counts By IP Address</span></a></li>
                                            </ul>
                                        </div>
                                    </div>                    
                                </li>
                                
                                
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewLanguageTypes.action"/>" ><span class="menu_text"><fmt:message key="menu.viewLanguageTypes"/></span></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewTopMediaTypes.action" />"><span class="menu_text"><fmt:message key="menu.viewTopMediaTypes"/></span></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewNewsItems.action"/>"><span class="menu_text"><fmt:message key="menu.viewNewsItem"/></span></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewPersons.action" />"><span class="menu_text">Person Name Authority</span></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewPublishers.action"/>"><span class="menu_text"><fmt:message key="menu.viewPublishers"/></span></a></li>
                                <c:if test='${ir:userHasRole("ROLE_ADMIN","OR")}'>
                                    <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewRepository.action"/>"><span class="menu_text">Repository</span></a></li>
                                </c:if>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewRepositoryLicenses.action"/>"><span class="menu_text"><fmt:message key="menu.viewLicenses"/></span></a></li>
				                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewReviewPendingItems.action"/>"><span class="menu_text">Review Pending Publications</span></a></li>
                                <c:if test='${ir:userHasRole("ROLE_ADMIN", "OR")}'>
                                    <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewRoles.action" />"><span class="menu_text"><fmt:message key="menu.ir_roles"/></span></a></li>
                                </c:if>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewSeries.action"/>"><span class="menu_text"><fmt:message key="menu.viewSeries"/></span></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewSponsors.action"/>"><span class="menu_text"><fmt:message key="menu.viewSponsors"/></span></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewUserGroups.action"/>"><span class="menu_text">User Groups</span></a></li>
                                <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/admin/viewUsers.action" />"><span class="menu_text"><fmt:message key="menu.viewUsers"/></span></a></li>
                            </ul>
                        </div>
                    </div>                
                    </li>
                    </c:if>
                    <c:if test='${ir:userHasRole("ROLE_COLLABORATOR,ROLE_AUTHOR,ROLE_RESEARCHER,ROLE_ADMIN", "OR")}'>
                    <li class="yuimenubaritem"><a class="yuimenubaritemlabel" href="#workspace"><span class="report_editImg">&nbsp;</span><span class="menu_text">Workspace</span></a>
                        <div id="workspace" class="yuimenu">
                            <div class="bd">                                        
                                <ul>
                                    <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/user/workspace.action"/>"><span class="menu_text">Files and Folders</span></a></li>
                                    <c:if test='${ir:userHasRole("ROLE_RESEARCHER", "OR")}' >
                               		    <li class="yuimenuitem"><a class="yuimenuitemlabel" href="<c:url value="/user/viewResearcher.action"/>"><span class="menu_text">Researcher Page</span></a></li>
                                    </c:if>
                                </ul>
                            </div>
                        </div>                                        
                    </li>
                    </c:if> 
                </c:if>
                
                <li class="yuimenubaritemleft"><a class="yuimenubaritemlabel" href="<c:url value="/home.action"/>"><span class="homeImg">&nbsp;</span><span class="menu_text">Home</span></a></li>
                <c:if test="${user != null}">
                    <li class="yuimenubaritemleft"><a class="yuimenubaritemlabel" href="<c:url value="/user/myAccount.action"/>"><span class="userImg">&nbsp;</span><span class="menu_text">My Account: ${user.username}</span></a></li>    
                </c:if>
            </ul>            
        </div>
    </div>
</div>
<!--  end header -->