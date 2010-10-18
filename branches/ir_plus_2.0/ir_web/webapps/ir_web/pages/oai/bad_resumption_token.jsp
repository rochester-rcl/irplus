<jsp:directive.page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" />
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<OAI-PMH xmlns="http://www.openarchives.org/OAI/2.0/" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd">
  <responseDate>${responseDate}</responseDate>
  <request> <ir:baseUrl/>oai2.action</request>  <error code="badResumptionToken"> ${oaiOutput}</error>
</OAI-PMH>
