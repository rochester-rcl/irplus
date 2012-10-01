<jsp:directive.page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" />


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

<researcher>
    <!--  only show the researcher if the researcher page is public -->
    <c:if test="${researcher != null && (researcher.isPublic || user.id == researcher.user.id)}">
    
        <c:if test="${researcher.user.personNameAuthority != null}">
            <c:url var="contributorUrl" value="viewContributorPage.action">
                <c:param name="personNameId" value="${researcher.user.personNameAuthority.authoritativeName.id}"/>
            </c:url>
            <contributor-url><ir:baseUrl/><c:out value="${contributorUrl}"/></contributor-url>
        </c:if>

        <title><c:out value="${researcher.title}"/></title>
        <departments>
        <c:if test="${!ur:isEmpty(researcher.user.departments)}">
		    <c:forEach items="${researcher.user.departments}" var="department">
		        <department>
		            <name><c:out value="${department.name}"/></name>
		        </department>
		     </c:forEach>
		</c:if>
		</departments>
		
		<fields>
		<c:if test="${!ur:isEmpty(researcher.fields)}">
		     <c:forEach items="${researcher.fields}" var="field">
		         <field>
		             <name><c:out value="${field.name}"/></name>
		         </field>
		     </c:forEach>
		</c:if>
		</fields>
		
		<campus_location><c:out value="${researcher.campusLocation}"/></campus_location>
		<phone_number><c:out value="${researcher.phoneNumber}"/></phone_number>
		<email><c:out value="${researcher.email}"/></email>
		<fax><c:out value="${researcher.fax}"/></fax>
		
		<personal_links>
		<c:if test="${ur:collectionSize(researcher.personalLinks) > 0}">
		<c:forEach var="link" items="${researcher.personalLinks}">
		    <personal_link>
		        <name><c:out value="${link.name}"/></name>
		        <description><c:out value="${link.description}"/></description>
		        <url><c:out value="${link.url}"/></url>
		    </personal_link>
		</c:forEach>
		</c:if>
		</personal_links>
		
		<research_interests>
		    <c:out value="${researcher.researchInterest}"/>
		</research_interests>
		<teaching_interests>
		    <c:out value="${researcher.teachingInterest}"/>
		</teaching_interests>
		
		<ir:drawResearcherXmlFolder researcher="${researcher}"/>
    </c:if>
</researcher>