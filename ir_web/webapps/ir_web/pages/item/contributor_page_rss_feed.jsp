<jsp:directive.page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" />


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<rss version="2.0">
   <channel>
      <title><ir:authorName personName="${personName}" displayDates="true"/></title>
      
      
      <c:url var="viewContributorUrl" value="viewContributorPage.action">
           <c:param name="personNameId" value="${personName.id}"/>
	  </c:url>
      <link><ir:baseUrl/>${viewContributorUrl}</link>
      <description>Contributor Page for: <ir:authorName personName="${personName}" displayDates="true"/></description>
      <generator><c:out value="${repository.name}"/></generator>
      
      <c:forEach var="item" items="${contributorPublications}">
           <item>
               <c:url var="itemView" value="institutionalPublicationPublicView.action">
			       <c:param name="institutionalItemVersionId" value="${item.institutionalItemVersion.id}"/>
			   </c:url>
			   <title><c:out value="${item.institutionalItemVersion.item.name}"/></title>
			   <link><ir:baseUrl/>${itemView}</link>
			   <description><c:out value="${ir:getItemDescription(item.institutionalItemVersion.item)}"/></description>
               <pubDate><fmt:formatDate type="both" dateStyle="long" timeStyle="long" value="${item.institutionalItemVersion.dateOfDeposit}"/></pubDate>
               <guid  isPermaLink="false">institutional_item_version_id:${item.institutionalItemVersion.id}</guid>
          </item>
      </c:forEach>     
   </channel>
</rss>