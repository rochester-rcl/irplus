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
  This JSP file displays the extents for the item
 -->
 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<c:if test="${itemExtentsCount == 0}">
	<%@ include file="/pages/user/workspace/item/item_extent.jsp" %>
</c:if>

	<c:forEach items="${item.itemExtents}" var="itemExtent" varStatus="rowCounter">
    
   	<table id="extent_table_${rowCounter.count}">

		<tr>
	      <td > 
	      	   <select id="itemForm_extent" name="extentIds" />
	      		<c:forEach items="${extentTypes}" var="extentType">
	      			<option value = "${extentType.id}" 
		      			<c:if test="${extentType.name == itemExtent.extentType.name}">
		      				selected
		      			</c:if>
		      		> <ur:maxText numChars="70" text="${extentType.name}" /></option>
	      		</c:forEach>
	      	   </select>
	      </td>
	      <td>
	      	   <input type="text" id="itemForm_extent_value" name="extentValues" value="${itemExtent.value}" size="40"/>
	      </td>
	      <td>   
	      	   <input type="button" class="ur_button" id="itemForm_remove" value="Remove Extent" onclick="javascript:YAHOO.ur.item.metadata.removeExtent('extent_table_${rowCounter.count}');"/>
	      </td>
	    </tr>
	</table>
	</c:forEach> 
