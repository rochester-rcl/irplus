<jsp:directive.page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" />
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<OAI-PMH xmlns="http://www.openarchives.org/OAI/2.0/" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd">
  <responseDate>${responseDate}</responseDate>
    <request 
           <c:if test="${verb != null}">
               verb="${verb}"
           </c:if> 
           <c:if test="${identifier != null }">
               identifier="${identifier}" 
           </c:if>
           <c:if test="${metadataPrefix != null }">
                metadataPrefix="${metadataPrefix}" 
           </c:if>
           <c:if test="${resumptionToken != null }"> 
               resumptionToken="${resumptionToken}"
           </c:if>
           <c:if test="${from != null }"> 
               from="${from}"
           </c:if>
           <c:if test="${until != null }"> 
               until="${until}"
           </c:if>
           <c:if test="${set != null }"> 
               set="${set}"
           </c:if>
           > <ir:baseUrl/>oai2.action</request>
  <error code="badVerb">Illegal OAI verb - ${verb}</error>
</OAI-PMH>
