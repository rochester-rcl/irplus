<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<h3>${researcher.user.firstName}&nbsp;${researcher.user.lastName}&nbsp;Researcher page (<a href="viewResearcherPage.action?researcherId=${researcher.id}">Preview Page</a>) &nbsp;: 
    <input type="radio" id="researcher_page_off" name="isPublic" onclick="javascript:YAHOO.ur.edit.researcher.confirmPrivateDialog.showDialog();"
	    <c:if test="${!researcher.public}">
	        checked
	    </c:if>
	/> <c:if test="${!researcher.public}"><span class="errorMessage">OFF</span></c:if> <c:if test="${researcher.public}">OFF</c:if>
	        &nbsp;    	
	      <input type="radio" id="researcher_page_on" name="isPublic" onclick="javascript:YAHOO.ur.edit.researcher.confirmPublicDialog.showDialog();"
	          <c:if test="${researcher.public}">
	            			checked
	          </c:if>
	/><c:if test="${researcher.public}"><span class="greenMessage">ON</span></c:if> <c:if test="${!researcher.public}">ON</c:if>
</h3>