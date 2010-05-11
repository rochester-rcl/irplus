<jsp:directive.page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" />
<%@ taglib prefix="ir" uri="ir-tags"%>

<OAI-PMH xmlns="http://www.openarchives.org/OAI/2.0/" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/
         http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd">
  <responseDate>${responseDate}</responseDate>
  <request verb="${verb}" identifier="${identifier}" 
           metadataPrefix="${metadataPrefix}"  resumptionToken="${resumptionToken}"> <ir:baseUrl/>oai2.action</request>
      ${oaiOutput}
</OAI-PMH>
