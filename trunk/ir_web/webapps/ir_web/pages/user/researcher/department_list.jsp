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

<!-- This JSP file helps to display the list of fields
-->

 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>


      	    <select size="10" multiple="multiple" name="departmentIds" />
	      	    <c:forEach items="${departments}" var="department">
	      		    <option value = "${department.id}"
	      			    <c:forEach items="${researcher.user.departments}" var="userDepartment">
	      			        <c:if test="${department.id == userDepartment.id}">
	      				        selected
	      			        </c:if>
	      			    </c:forEach>
	      			    > ${department.name}</option>
	      		</c:forEach>
      	    </select>
          
	
	

