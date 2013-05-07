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

<jsp:directive.page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" />

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<contributor>

    <!--  output the person name information -->
    <person_name authority="true" id="<c:out value='${personName.id}'/>">
        <first_name><c:out value="${personName.forename}"/></first_name>
        <middle_name><c:out value="${personName.middleName}"/></middle_name>
        <last_name><c:out value="${personName.surname}"/></last_name>
    </person_name>
    <birth_year>${personName.personNameAuthority.birthDate.year}</birth_year>
    <death_year>${personName.personNameAuthority.deathDate.year}</death_year>
    <total_publications><c:out value="${publicationsCount}"/></total_publications>
    <total_downloads><c:out value="${totalDownloads}"/></total_downloads>
    
    <!--  output the researcher page url if one exists -->
    <c:if test="${researcher != null && researcher.isPublic}">
        <c:url value="viewResearcherPage.action" var="viewResearcherPageUrl">
	        <c:param name="researcherId" value="${researcher.id}"/>
	    </c:url>
	    <researcher_page>
	        <url><ir:baseUrl/><c:out value="${viewResearcherPageUrl}"/></url>
	    </researcher_page>
	</c:if>

	<!--  list of all publications -->
	<publications>
	 <c:forEach var="contributorPublication" items="${contributorPublications}">
	     <!--  the base url will be placed on this so no need to add an initial forward slash (/) -->
	     <c:url var="itemViewUrl" value="institutionalPublicationPublicView.action">
	          <c:param name="institutionalItemVersionId" value="${contributorPublication.institutionalItemVersion.id}"/>
	     </c:url>
	     <publication>        
	             <title><c:out value="${contributorPublication.institutionalItemVersion.item.name}"/></title>
	             <abstract><c:out value="${contributorPublication.institutionalItemVersion.item.itemAbstract}"/></abstract>
	             <description><c:out value="${contributorPublication.institutionalItemVersion.item.description}"/></description>
	             <url><ir:baseUrl/><c:out value="${itemViewUrl}"/></url>
	             <date_published>
	                 <month><c:out value="${contributorPublication.institutionalItemVersion.item.externalPublishedItem.publishedDate.month}"/></month>
	                 <day><c:out value="${contributorPublication.institutionalItemVersion.item.externalPublishedItem.publishedDate.day}"/></day>
	                 <year><c:out value="${contributorPublication.institutionalItemVersion.item.externalPublishedItem.publishedDate.year}"/></year>
	             </date_published>
	             <publication_version><c:out value="${contributorPublication.institutionalItemVersion.versionNumber}"/></publication_version>
	             <publication_id><c:out value="${contributorPublication.institutionalItemVersion.id}"/></publication_id>
	             <download_count><c:out value="${contributorPublication.downloadsCount}"/></download_count>
	             <contributors>
	                 <c:forEach var="itemContributor" items="${contributorPublication.institutionalItemVersion.item.contributors}">
	                     <contributor>
	                         <first_name><c:out value="${itemContributor.contributor.personName.forename}"/></first_name>
	                          <middle_name><c:out value="${itemContributor.contributor.personName.middleName}"/></middle_name>
	                         <last_name><c:out value="${itemContributor.contributor.personName.surname}"/></last_name>
	                         <contribution_type><c:out value="${itemContributor.contributor.contributorType.name}"/></contribution_type>
	                     </contributor>
	                 </c:forEach>
	             </contributors>
	     </publication>        
	       
	 </c:forEach>
      </publications>
</contributor>