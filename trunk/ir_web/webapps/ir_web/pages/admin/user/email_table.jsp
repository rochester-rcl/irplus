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
<%@ taglib prefix="ir" uri="ir-tags"%>

<div class="jmesa">
	<ur:basicForm method="post" id="emails" name="myEmails" >
	 <input type="hidden" name="id" value="${id}"/>
	    <ur:table cssClass="table" 
	        border="0" 
	        cellPadding="0" 
	        cellSpacing="0" 
	        width="0"
	        collection="${emails}"
	        caption="Emails">
	        
	        <ur:tableRow var="email" 
	            totalNumberOfPages="${totalNumberOfPages}" 
	            totalResults="${totalNumberOfResults}"
	            currentPage="${currentPage}"
	            rowStart="${displayRowStart}"
	            rowEnd="${displayRowEnd}"
	            divId="newEmails"
	            javascriptObject="myEmailTable"
	            submitUrl="${pageContext.request.contextPath}/admin/getEmails.action">
	            <ur:tableHeaderToolbar>
	                <ur:simpleTablePager />
	                <ur:maxResultsPicker maxResultsPerPage="${maxResultsPerPage}"
	                                     choices="${maxResultChoices}"/>
	            </ur:tableHeaderToolbar>
	            <ur:column  filter="false">
	                <ur:columnHeader sort="false"><ur:checkbox name="checkAllSetting" 
	                    value="off" 
	                    onClick="YAHOO.ur.email.setCheckboxes();"/>Email Id</ur:columnHeader>
	                <ur:columnContent>
	                    <c:if test="${email.id != email.irUser.defaultEmail.id}">
	                        <ur:checkbox name="emailIds" value="${email.id}"/>
	                    </c:if>
	                    <c:if test="${email.id == email.irUser.defaultEmail.id}">
	                        <strong>[Default Email]</strong>
	                    </c:if>
	                </ur:columnContent>
	               
	            </ur:column>
	            <ur:column  filter="true" sortFilterProperty="id">
	                <ur:columnHeader sort="true"
	                 sortOrder="${nextAvailableSortOrder}">&nbsp;Id&nbsp;</ur:columnHeader>
	                 <ur:columnContent>
	                     ${email.id}
	                 </ur:columnContent>
	            </ur:column >

	            <ur:column filter="true" sortFilterProperty="email">
	                <ur:columnHeader sort="true" sortOrder="${nextAvailableSortOrder}">Email</ur:columnHeader>
	                <ur:columnContent>
	                 	<c:if test="${(email.id != email.irUser.defaultEmail.id) && (email.verified)}">
	                   		<a href="javascript:YAHOO.ur.email.editEmail('${email.irUser.id}',
	                   		'${email.id}','${email.email}','${email.irUser.defaultEmail.id}');">${email.email}</a>
	                   	</c:if> 
	                 	<c:if test="${(email.id == email.irUser.defaultEmail.id) || !(email.verified)}">
	                   		${email.email}
	                   	</c:if> 

	                </ur:columnContent>
	            </ur:column >
	            
	            <ur:column>
	                <ur:columnHeader>Status</ur:columnHeader>
	                <ur:columnContent>
	                   		<c:if test="${email.verified}">
	                       		Verified
	                    	</c:if>
	                   		<c:if test="${!email.verified}">
	                       		Pending verification 
	                    	</c:if>

	                </ur:columnContent>
	            </ur:column >	            
      </ur:tableRow>
	    </ur:table>
    </ur:basicForm>
</div>