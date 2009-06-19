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

<!--This JSP file helps to add another row of extent 
 -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<ur:styleSheet href="page-resources/css/global.css"/>

<table id="sponsor_table_i">

	<tr>
      <td > 
      	   <select id="itemForm_sponsor" name="sponsorIds" />
      	   <option value = "0"> Select </option>
      		<c:forEach items="${sponsors}" var="sponsor">
      			<option value = "${sponsor.id}"> ${sponsor.name}</option>
      		</c:forEach>
      	   </select>
      </td>
      <td>
      	   <input type="text" id="itemForm_sponsor_description" name="sponsorDescriptions" size="40"/>
      </td>
      <td>   
      	   <input type="button" class="ur_button" id="itemForm_remove" value="Remove Sponsor" onclick="javascript:YAHOO.ur.item.metadata.removeSponsor('sponsor_table_i');"/>
      </td>
      </tr>
</table>