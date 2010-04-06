<jsp:directive.page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" />



<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="en_US" scope="session"/>
<rss version="2.0">
   <channel>
      <title><c:out value="${institutionalCollection.name}"/></title>
      
      <c:url var="collectionUrl" value="viewInstitutionalCollection.action">
           <c:param name="collectionId" value="${collectionId}"/>
	  </c:url>
      <link><ir:baseUrl/>${collectionUrl}</link>
      <description><c:out value="${institutionalCollection.description}"/></description>
      <generator><c:out value="${institutionalCollection.repository.name}"/></generator>
      
      <c:forEach var="item" items="${mostRecentSubmissions}">
           <item>
               <c:url var="itemView" value="institutionalPublicationPublicView.action">
			       <c:param name="institutionalItemId" value="${item.id}"/>
			   </c:url>
			   <title><c:out value="${item.versionedInstitutionalItem.currentVersion.item.name}"/></title>
			   <link><ir:baseUrl/>${itemView}</link>
			   <description><c:out value="${ir:getItemDescription(item.versionedInstitutionalItem.currentVersion.item)}"/></description>
               <pubDate><fmt:formatDate pattern="EE, dd MMM yyyy HH:mm:ss Z" value="${item.versionedInstitutionalItem.currentVersion.dateOfDeposit}"/></pubDate>
               <guid  isPermaLink="false">institutional_item_version_id:${item.versionedInstitutionalItem.currentVersion.id}</guid>
          </item>
      </c:forEach>     
   </channel>
</rss>