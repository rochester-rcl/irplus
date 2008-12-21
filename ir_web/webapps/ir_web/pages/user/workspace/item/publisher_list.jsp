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

<!-- This JSP file display the publishers for the item
-->
 
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

	<select id="itemForm_publisher" name="publisherId" />
   		<option value = "0"> Select </option>
  		<c:forEach items="${publishers}" var="publisher">
  			<option value = "${publisher.id}"
  			<c:if test="${item.externalPublishedItem.publisher.id == publisher.id}">
  				selected
  			</c:if>
  			> ${publisher.name}</option>
  		</c:forEach>
   </select>

