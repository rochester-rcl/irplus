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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>



<div class="dataTable">
	<ur:basicForm method="post" id="myEmails" name="myEmails" >           
	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
					<urstb:td><ur:checkbox name="checkAllSetting" 
	                    value="off" 
	                    onClick="YAHOO.ur.email.setCheckboxes();"/>&nbsp;Email Type</urstb:td>         
	                <urstb:td>Id</urstb:td>
	                <urstb:td>Email</urstb:td>
	                <urstb:td>Status</urstb:td>
	            </urstb:tr>
	        </urstb:thead>
	        <urstb:tbody
	                var="email" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${emails}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        <c:if test="${email.id != email.irUser.defaultEmail.id}">
	                                <ur:checkbox name="emailIds" value="${email.id}"/>
	                            </c:if>
	                            <c:if test="${email.id == email.irUser.defaultEmail.id}">
	                                <strong>[Default Email]</strong>
	                            </c:if>
	                        </urstb:td>
	                        <urstb:td>
		                         ${email.id}
	                        </urstb:td>
	                        <urstb:td>
		                   		<c:if test="${(email.id != email.irUser.defaultEmail.id) && (email.verified)}">
	                   		        <a href="javascript:YAHOO.ur.email.editEmail('${email.irUser.id}',
	                   		            '${email.id}','${email.email}','${email.irUser.defaultEmail.id}');">${email.email}</a>
	                           	</c:if> 
	                 	        <c:if test="${(email.id == email.irUser.defaultEmail.id) || !(email.verified)}">
	                   		        ${email.email}
	                   	        </c:if> 
	                        </urstb:td>
	                        <urstb:td>
		                   		<c:if test="${email.verified}">
	                       		    Verified
	                    	    </c:if>
	                   		    <c:if test="${!email.verified}">
	                       		     Pending verification 
	                    	     </c:if>
	                        </urstb:td>
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
	    </ur:basicForm>
</div>	


















