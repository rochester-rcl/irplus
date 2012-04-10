
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<h3>Project Page:&nbsp;<a href="${groupWorkspaceUrl}">${groupWorkspaceProjectPage.groupWorkspace.name}</a>
                   <input type="radio" id="group_workspace_project_page_off" name="isPublic" onclick="javascript:YAHOO.ur.edit.group_workspace_project_page.confirmPrivateDialog.showDialog();"
	               <c:if test="${!groupWorkspaceProjectPage.pagePublic}">
	                   checked
	               </c:if>
	               /> <c:if test="${!groupWorkspaceProjectPage.pagePublic}"><span class="errorMessage">OFF</span></c:if> <c:if test="${groupWorkspaceProjectPage.pagePublic}">OFF</c:if>
	               &nbsp;    	
	               <input type="radio" id="group_workspace_project_page_on" name="isPublic" onclick="javascript:YAHOO.ur.edit.group_workspace_project_page.confirmPublicDialog.showDialog();"
	                   <c:if test="${groupWorkspaceProjectPage.pagePublic}">
	            			checked
	                   </c:if>
	                /><c:if test="${groupWorkspaceProjectPage.pagePublic}"><span class="greenMessage">ON</span></c:if> <c:if test="${!groupWorkspaceProjectPage.pagePublic}">ON</c:if>
</h3>