<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



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

<!--  form fragment for dealing with editing content types
      this form will return with error messages in it if there
      is an issue.
 -->

<input type="hidden" id="contributorTypeDcMappingForm_id" name="id" value="${contributorTypeDublinCoreMapping.id}"/>
		               
<input type="hidden" id="newContributorTypeDublinCoreMapping_new" name="update" value="${update}"/>
<input type="hidden" id="newContributorTypeDublinCoreMapping_success" name="added" value="${added}"/>
		              
<div id="error" class="errorMessage">${message}</div>
<table class="formTable">    
    <tr>       
	    <td align="left" class="label">Contributor Type:*</td>
		<td align="left" class="input">
		    <select name="contributorTypeId">
		        <c:forEach items="${contributorTypes}" var="contributorType">
		            <option 
		                <c:if test="${contributorTypeDublinCoreMapping.contributorType.id == contributorType.id}">
		                  selected="true"
		                </c:if>
		                value="${contributorType.id}">${contributorType.name}</option>
		        </c:forEach>
		    </select> 
		</td>
	</tr>
	<tr>       
	    <td align="left" class="label">Dublin Core Term:*</td>
		<td align="left" class="input">
		    <select name="dublinCoreTermId">
		        <c:forEach items="${dublinCoreTerms}" var="dublinCoreTerm">
		            <option value="${dublinCoreTerm.id}"
		             <c:if test="${contributorTypeDublinCoreMapping.dublinCoreTerm.id == dublinCoreTerm.id}">
		                  selected="true"
		              </c:if>
		            >${dublinCoreTerm.name}</option>
		        </c:forEach>
		    </select> 
		</td>
	</tr>
</table>