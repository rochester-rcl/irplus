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

<input type="hidden" id="newLinkForm_new" name="newLink" value="true"/>
<input type="hidden" id="newLinkForm_linkId" name="linkId" value="${link.id}"/>
<div id="linkNameError" class="errorMessage"></div>
	              
<table class="formTable">
    <tr>
	    <td align="left" class="label"> Link Name:</td>
	    <td align="left" class="input"> <input id="link_name" size="45" type="text" name="linkName" value="${link.name}"/></td>
	</tr>
	<tr>
	    <td align="left" class="label"> Link Description:</td>
	    <td align="left" class="input"><textarea cols="42" rows=4" name="linkDescription">${link.description}</textarea></td>
	</tr>
	<tr>
	    <td align="left" class="label"> Link URL:</td>
	    <td align="left" class="input"> 
	    <c:if test="${link.id == null }">
	        <input id="link_url" size="45" type="text" name="linkUrl" value="http://"/>
	    </c:if>
	    <c:if test="${link.id != null }">
	        <input id="link_url" size="45" type="text" name="linkUrl" value="${link.url}"/>
	    </c:if>
	    </td>
	</tr>
</table>