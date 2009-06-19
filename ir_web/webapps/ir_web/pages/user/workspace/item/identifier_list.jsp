
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

<!--
  This JSP file displays the identifiers for the item
 -->
 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${itemIdentifiersCount == 0}">
	<%@ include file="/pages/user/workspace/item/item_identifier.jsp" %>
</c:if>

	<c:forEach items="${item.itemIdentifiers}" var="itemIdentifier" varStatus="rowCounter">
    
   	<table id="identifier_table_${rowCounter.count}">

		<tr>
	      <td > 
	      	   <select id="itemForm_identifier" name="identifierIds" />
	      		<c:forEach items="${identifierTypes}" var="identifierType">
	      			<option value = "${identifierType.id}" 
		      			<c:if test="${identifierType.name == itemIdentifier.identifierType.name}">
		      				selected
		      			</c:if>
		      		> ${identifierType.name}</option>
	      		</c:forEach>
	      	   </select>
	      </td>
	      <td>
	      	   <input type="text" id="itemForm_identifier_value" name="identifierValues" value="${itemIdentifier.value}" size="40"/>
	      </td>
	      <td>   
	      	   <input type="button" class="ur_button" id="itemForm_remove" value="Remove Identifer" onclick="javascript:YAHOO.ur.item.metadata.removeIdentifier('identifier_table_${rowCounter.count}');"/>
	      </td>
	    </tr>
	</table>
	</c:forEach> 
