<jsp:directive.page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" />


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="en_US" scope="session"/>
<rss version="2.0">
   <channel>
      <title>Person Name Not Found for id ${personNameId}</title>
      
   </channel>
</rss>