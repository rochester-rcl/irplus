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
 <br> <br>
	<!-- Table for files and folders  -->            
	<table class="itemFolderTable" width="90%" align="center">
		<thead>
			<tr>
				<th class="thItemFolder" width="17%">Remove</th>
				<th class="thItemFolder">Authoritative Name</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${irUser.personNameAuthority != null}">
				<tr>
				<td class="tdItemContributorLeftBorder"> <a href="javascript:YAHOO.ur.email.removeName(${irUser.id});">Remove</a>
				</td>
				<td class="tdItemContributorRightBorder"> ${irUser.personNameAuthority.authoritativeName.forename} ${irUser.personNameAuthority.authoritativeName.surname}
					[Authoritative name]
				</td>
				<c:forEach  var="name" items="${irUser.personNameAuthority.names}">
					<c:if test="${name.id != irUser.personNameAuthority.authoritativeName.id}">
						<tr>
						<td></td>
						<td class="tdItemContributorRightBorder"> ${name.forename} ${name.surname}</td>
						</tr>
					</c:if>
				</c:forEach>
			</c:if>
		</tbody>
	</table>	
