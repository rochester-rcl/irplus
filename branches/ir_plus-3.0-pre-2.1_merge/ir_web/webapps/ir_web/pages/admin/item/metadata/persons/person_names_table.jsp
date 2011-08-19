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

<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>


<div class="dataTable">
	<ur:basicForm method="post" id="people" name="myPersonNames" >
	    <input type="hidden" name="personId" value="${personNameAuthority.id}"/>
	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
					<urstb:td><ur:checkbox name="checkAllSetting" 
								value="off" onClick="YAHOO.ur.person.names.setCheckboxes();"/></urstb:td>         
	                <urstb:td>Id</urstb:td>
	                <urstb:td>First Name</urstb:td>
	                <urstb:td>Last Name</urstb:td>
 	                <urstb:td>Middle Name</urstb:td>
	                <urstb:td>Family Name</urstb:td>
 	                <urstb:td>Initials</urstb:td>
 	                <urstb:td>Numeration</urstb:td>
	                </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="personName" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${personNames}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
			                    <c:if test="${personName.id != personNameAuthority.authoritativeName.id}">
			                        <ur:checkbox name="personNameIds" value="${personName.id}"/>
			                    </c:if>
			                    <c:if test="${personName.id == personNameAuthority.authoritativeName.id}">
			                        <strong>[Authoritative Name]</strong>
			                    </c:if>
	                        </urstb:td>
	                        <urstb:td>
		                        ${personName.id}
	                        </urstb:td>
	                        <urstb:td>
			                   ${personName.forename}
	                        </urstb:td>
	                        <urstb:td>
	                        <a href="javascript:YAHOO.ur.person.names.editPersonName('${personName.personNameAuthority.id}',
			                   '${personName.id}',
			                   '${personName.surname}', 
			                   '${personName.forename}', 
			                   '${personName.middleName}', 
			                   '${personName.familyName}', 
			                   '${personName.initials}',
			                   '${personName.numeration}',
			                   '${personNameAuthority.authoritativeName.id}');">
		                   		${personName.surname}</a>
	                        </urstb:td>
	                        <urstb:td>
	                             ${personName.middleName}
	                        </urstb:td>
	                        <urstb:td>
	                             ${personName.familyName}
	                        </urstb:td>	                        
	                        <urstb:td>
		                   		${personName.initials}
	                        </urstb:td>
	                        <urstb:td>
	                             ${personName.numeration}
	                        </urstb:td>
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>	
		</ur:basicForm>
	</div>







