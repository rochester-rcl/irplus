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

     Collection is 
	       <c:if test="${collection.publiclyViewable}">
		         <strong>Publicly Viewable.</strong>
	       </c:if> 
	       
	       
	       <c:if test="${!collection.publiclyViewable}">
		   		<strong>Private.</strong>
	       </c:if>
	       
	       &nbsp;
 	       <button class="ur_button" type="button"
	               onmouseover="this.className='ur_buttonover';"
	               onmouseout="this.className='ur_button';"
	               onclick="javascript:YAHOO.ur.edit.institution.collection.showPermissionDialog();">Make
	               <c:if test="${collection.publiclyViewable}">
		              Private
	               </c:if> 
	       
	       
	              <c:if test="${!collection.publiclyViewable}">
		   		      Public
	               </c:if>
	      </button> 
