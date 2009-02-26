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
<%@ taglib prefix="ir" uri="ir-tags"%>

<input type="hidden" id="edit_permission_collection_form_collection_id" name="collectionId" value="${collection.id}">

<table class="formTable">
        
         <c:if test="${collection.publiclyViewable}">
			<tr>
			    <td align="left">
			  	  <input type="radio" name="updateChildrenPermission" value="false" > Set the collection as private </input>  
				</td>
			</tr>
			<tr>
				<td align="left">
				  	  <input type="radio" name="updateChildrenPermission" value="true"> Set the collection as well as the Publications and files in this collection as private </input>
			  	</td>
    	  	</tr> 
	   	</c:if> 

         <c:if test="${!collection.publiclyViewable}">
			<tr>
			    <td align="left">
			  	  <input type="radio" name="updateChildrenPermission" value="false" > Set the collection as public </input> 
				</td>
			</tr>
			<tr>
				<td align="left">
			  	  <input type="radio" name="updateChildrenPermission" value="true"> Set the collection as well as the Publications and files in this collection as public </input> 
			  	</td>
    	  	</tr> 
	     </c:if> 

	
</table>