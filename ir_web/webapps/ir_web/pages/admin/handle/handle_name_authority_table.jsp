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
	<ur:basicForm method="post" id="handleNameAuthorities" name="myHandleNameAuthorities" >
	             
	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
					<urstb:td><ur:checkbox name="checkAllSetting" 
								value="off" onClick="YAHOO.ur.handle.authority.setCheckboxes();"/></urstb:td>         
	                <urstb:td>Id</urstb:td>
	                <urstb:td>Name</urstb:td>
	                <urstb:td>Base URL</urstb:td>
					<urstb:td>Description</urstb:td>
	            </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="nameAuthority" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${handleNameAuthorities}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        <ur:checkbox name="nameAuthorityIds" value="${nameAuthority.id}"/>
	                        </urstb:td>
	                        <urstb:td>
		                        ${nameAuthority.id}
	                        </urstb:td>
	                        <urstb:td>
			                   <a href="javascript:YAHOO.ur.handle.authority.editHandleNameAuthority(${nameAuthority.id});">${nameAuthority.namingAuthority}</a>
	                        </urstb:td>
	                        <urstb:td>
	                             ${nameAuthority.authorityBaseUrl}
	                        </urstb:td>
	                        <urstb:td>
	                             ${nameAuthority.description}
	                        </urstb:td>
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
		</ur:basicForm>
</div>	



