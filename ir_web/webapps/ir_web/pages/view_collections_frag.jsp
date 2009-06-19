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
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<c:if test="${!ur:isEmpty(institutionalCollections)}">
    <table class="baseTable">
    <c:forEach items="${institutionalCollections}" var="institutionalCollection" >
       
        <c:url var="institutionalCollectionUrl" value="/viewInstitutionalCollection.action">
             <c:param name="collectionId" value="${institutionalCollection.id}"/>
        </c:url>
       
        <tr>
            <td class="baseTableImage">
                <ir:transformUrl systemCode="PRIMARY_THUMBNAIL" download="true" irFile="${institutionalCollection.primaryPicture}" var="url"/>
                <c:if test="${url != null}">
                    <a href="${institutionalCollectionUrl}"><img height="66px" width="100px" src="${url}"/></a>
                </c:if>
                
            </td>
            <td>
                <p><strong><a href="${institutionalCollectionUrl}">${institutionalCollection.name}</a></strong> <ur:maxText numChars="100" text="${institutionalCollection.description}"></ur:maxText></p>
            </td>
        </tr>
       
    </c:forEach>
      
    </table>
</c:if>
<c:if test="${ur:isEmpty(institutionalCollections)}">
    <p>There are no institutional collections</p>
</c:if>