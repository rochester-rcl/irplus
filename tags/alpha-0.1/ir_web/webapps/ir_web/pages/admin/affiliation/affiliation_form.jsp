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

<!--  form fragment for dealing with editing affiliations
      this form will return with error messages in it if there
      is an issue.
 -->
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


		<!--  represents a successful submission -->
		<input type="hidden" id="newAffiliationForm_success" 
		       value="${added}"/>
		       
		<!--  if editing an id must be passed -->     
	    <input type="hidden" id="newAffiliationForm_id"
		        name="id" value="${affiliation.id}"/>
		               
	    <input type="hidden" id="newAffiliationForm_new"
		        name="newAffiliation" value="true"/>
		
		<div id="affiliation_error_div">       
	        <!--  get the error messages from fieldErrors -->
		    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		       key="affiliation.name"/></p>
		    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		        key="affiliationAlreadyExists"/></p> 
		</div>
		
		<table class="formTable">    
		    <tr>
			    <td align="left" class="label">Name:*</td>
	            <td align="left" class="input"><input type="text" 
			        id="newAffiliationForm_name" 
			        name="affiliation.name" 
			        size="45"
			        value="${affiliation.name}"/> </td>
	         </tr>
	         <tr>
	             <td align="left" class="label">Description:</td>
	             <td colspan="2" align="left" class="input"><textarea name="affiliation.description" 
	    id="newAffiliationForm_description" cols="42" rows="4">${affiliation.description}</textarea></td>
	         </tr>
	         <tr>
	             <td class="label" align="left" colspan="2"><strong> User Type Permissions:</strong></td>
	         </tr>
	         <tr>
	             <td colspan="2" align="left"><input type="checkbox" 
	             id="newAffiliationForm_author"  
	             name="author" 
	             onclick="YAHOO.ur.affiliation.autoCheckPermission(this);"
	             <c:if test="${affiliation.author}">checked="checked"</c:if>
	             value="true"/> Assign Author permission </td>
	         </tr>
		     <tr>
	             <td colspan="2" align="left"> <input type="checkbox" id="newAffiliationForm_researcher" 
	                 name="researcher" 
	                 onclick="YAHOO.ur.affiliation.autoCheckPermission(this);"
	                 <c:if test="${affiliation.researcher}">checked="checked"</c:if>
	             value="true"/>
	    Assign Researcher permission</td>
	         </tr>
	    
		     <tr>
	             <td colspan="2" align="left"> 
	    <input type="checkbox" id="newAffiliationForm_needsApproval" name="needsApproval" 
	        <c:if test="${affiliation.needsApproval}">checked="checked"</c:if>
	        value="true"/>
	    Needs Approval by admin
	             </td>
	         </tr>
	     </table>
	    