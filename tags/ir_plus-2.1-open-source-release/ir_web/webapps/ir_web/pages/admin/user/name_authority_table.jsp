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

<!-- Displays the search results for the names
 -->
 
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div>
	<ur:basicForm method="post" id="names" name="myNames" 
	     action="javascript:YAHOO.ur.email.getNames(0)">
	
		 <input type="hidden" id="names_query" name="query" value="${query}"/>
		 <input type="hidden" id="names_itemId" name="userId" value="${irUser.id}"/>
		 <input type="hidden" id="names_rowStart" name="rowStart" value="${rowStart}"/>
		 <input type="hidden" id="number_of_results_to_show" name="numberOfResultsToShow" value="${numberOfResultsToShow}"/>

		<ir:jsPagination totalHits="${totalHits}" 
		    javaScriptFunctionName="YAHOO.ur.email.getNames"
		    rowStart="${rowStart}" 
		    numberOfResultsToShow="${numberOfResultsToShow}" /> 
		    
	    	
		<!-- Table for files and folders  -->            
		<table class="itemFolderTable" width="90%" align="center">
			<thead>
				<tr>
					<th class="thItemFolder" width="17%">Add</th>
					<th class="thItemFolder">Contributors</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="nameAuthority" items="${nameAuthorities}">
				    <tr>
				        
				            <c:if test="${(irUser.personNameAuthority != null) && 
				                          (nameAuthority.id == irUser.personNameAuthority.id)}">
				                 <td class="tdItemFolderLeftBorder"> &nbsp;&nbsp;&nbsp;Added </td>
				                 <td class="tdItemFolderRightBorder">
				                     <ir:authorName personName="${nameAuthority.authoritativeName}" displayDates="true"/> [Authoritative Name]
		                             
		                             <c:forEach var="name" items="${nameAuthority.names}">
				                         <c:if test="${name.id != nameAuthority.authoritativeName.id}">
				                            <br/><br/><ir:authorName personName="${name}" displayDates="false"/>
				                         </c:if>
				                     </c:forEach>
				                 </td>
				            </c:if>
				            <c:if test="${(irUser.personNameAuthority == null) || 
				                          (nameAuthority.id != irUser.personNameAuthority.id)}">
				                <td class="tdItemFolderLeftBorder">&nbsp;<span class="addBtnImg">&nbsp;</span><a href="javascript:YAHOO.ur.email.addName('${irUser.id}','${nameAuthority.id}');">Add</a></td>
				                <td class="tdItemFolderRightBorder">
		                             <ir:authorName personName="${nameAuthority.authoritativeName}" displayDates="true"/> [Authoritative Name]
		                             
		                             <c:forEach var="name" items="${nameAuthority.names}">
				                         <c:if test="${name.id != nameAuthority.authoritativeName.id}">
				                            <br/><br/><ir:authorName personName="${nameAuthority.authoritativeName}" displayDates="false"/>
				                         </c:if>
				                     </c:forEach>
		                         </td>
				            </c:if>
				    </tr>
				</c:forEach>
			</tbody>
		</table>	
		
		<ir:jsPagination totalHits="${totalHits}" 
		    javaScriptFunctionName="YAHOO.ur.email.getNames" 
		    rowStart="${rowStart}" 
		    numberOfResultsToShow="${numberOfResultsToShow}" />
	
    </ur:basicForm>
</div>



				