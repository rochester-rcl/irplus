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

<!--  form fragment for displaying the files that cannot 
	  be shared with other users.
 -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	
	<!--  represents a the share permissions for files -->
	<input type="hidden" id="has_permission" value="${hasSharePermission}"/>

	<input type="hidden" id="file_ids" name="shareFileIds" value="${shareFileIds}"/>
	
	<input type="hidden" id="parent_folder_id" name="parentFolderId" value="${parentFolderId}"/>
	
	The following file(s) cannot be shared with other users.  
	 <div class="clear">&nbsp;</div>
	
	<c:forEach var="file" items="${filesWithNoSharePermission}">
		${file.name} 
		 <div class="clear">&nbsp;</div>
	</c:forEach>
	
	<c:if test="${shareFileIds != ''}">
		Click OK to continue. Else click CANCEL.
	</c:if>

	
	
	
	
  
  	    