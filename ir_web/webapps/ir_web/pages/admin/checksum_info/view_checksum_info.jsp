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

<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Checksum Information</title>
    
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>

    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <!--  required imports -->
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="pages/js/ur_table.js"/>
   
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
      
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        
        <h3><a href="<c:url value="/admin/viewFileInfoChecksums.action"/>">All Checksums</a> &gt; Checksum Information</h3>
        
        <div id="bd">
            <div class="dataTable">
            <table class="table">
                  <tr class="even" onmouseout="this.className='even'" onmouseover="this.className='highlight'">
                      <td><strong>Checksum Id:&nbsp;</strong></td>
                      <td> ${fileInfoChecksum.id}</td>
                  </tr>
                  
                  <tr class="odd" onmouseout="this.className='odd'" onmouseover="this.className='highlight'">
                      <td><strong>File Location:&nbsp;</strong></td>
                      <td> ${fileInfoChecksum.fileInfo.fullPath}</td>
                  </tr>
                  <tr class="even" onmouseout="this.className='even'" onmouseover="this.className='highlight'">
                      <td><strong>File Extension:&nbsp;</strong></td>
                      <td> ${fileInfoChecksum.fileInfo.extension}</td>
                  </tr>
                  <tr class="odd" onmouseout="this.className='odd'" onmouseover="this.className='highlight'">
                      <td><strong>Check Passed:&nbsp;</strong></td>
                      <td> ${fileInfoChecksum.reCalculatedPassed}</td>
                  </tr>
                  <tr class="even" onmouseout="this.className='even'" onmouseover="this.className='highlight'">
                      <td><strong>Checksum:&nbsp;</strong></td>
                      <td> ${fileInfoChecksum.checksum}</td>
                  </tr>
                  <tr class="odd" onmouseout="this.className='odd'" onmouseover="this.className='highlight'">
                      <td><strong>Last Calculated Checksum:&nbsp;</strong></td>
                      <td> ${fileInfoChecksum.reCalculatedValue}</td>
                  </tr>
                  
                  <tr class="even" onmouseout="this.className='even'" onmouseover="this.className='highlight'">
                      <td><strong>Date Calculated:&nbsp;</strong></td>
                      <td> ${fileInfoChecksum.dateCalculated}</td>
                  </tr>
                  <tr class="odd" onmouseout="this.className='odd'" onmouseover="this.className='highlight'">
                      <td><strong>Date Re-Calculated:&nbsp;</strong></td>
                      <td> ${fileInfoChecksum.dateReCalculated}</td>
                  </tr>
                  <tr class="even" onmouseout="this.className='even'" onmouseover="this.className='highlight'">
                      <td><strong>Date Last Check Passed:&nbsp;</strong></td>
                      <td> ${fileInfoChecksum.dateLastCheckPassed}</td>
                  </tr>
                  <tr class="odd" onmouseout="this.className='odd'" onmouseover="this.className='highlight'">
                      <td><strong>Re Calculate Checksum:&nbsp;</strong></td>
                      <td> ${fileInfoChecksum.reCalculateChecksum}</td>
                  </tr>
                  <tr class="even" onmouseout="this.className='even'" onmouseover="this.className='highlight'">
                      <td><strong>Algorithm:&nbsp;</strong></td>
                      <td> ${fileInfoChecksum.algorithmType}</td>
                  </tr>
                  <tr class="odd" onmouseout="this.className='odd'" onmouseover="this.className='highlight'">
                      <td><strong>File Id:&nbsp;</strong></td>
                      <td> ${fileInfoChecksum.fileInfo.id}</td>
                  </tr>
              </table>
              </div>
        </div>
        
        <!--  allow administrator to reset the checksum -->
        <c:if test="${!fileInfoChecksum.reCalculatedPassed}">
            <c:url var="resetChecksumUrl" value="/admin/resetFileInfoChecksum.action"/>
            <!--  end body div -->
            <form method="post" action="${resetChecksumUrl}">
                <input type="hidden" name="checksumId" value="${fileInfoChecksum.id}"/>
                Reset Reason:<input type="text" name="notes" size="70"/> <br/><br/>
                <input type="submit" value="Reset Checksum"/>
            </form> <br/><br/>
        </c:if>
        
        <c:url var="checkFileInfoChecksumUrl" value="/admin/checkFileInfoChecksum.action"/>
        <form method="post" action="${checkFileInfoChecksumUrl}">
                <input type="hidden" name="checksumId" value="${fileInfoChecksum.id}"/>
                <input type="submit" value="Check Now"/>
        </form>
        
        <h3>Reset History</h3>
          <div class="dataTable">
            <urstb:table width="100%">
	            <urstb:thead>
		            <urstb:tr>
		                <urstb:td>Reset history Id</urstb:td>
			            <urstb:td>Original checksum</urstb:td>
			            <urstb:td>New Checksum</urstb:td>
			            <urstb:td>User Id</urstb:td>
			            <urstb:td>Date Reset</urstb:td>
			            <urstb:td>Notes</urstb:td>
		            </urstb:tr>
	            </urstb:thead>
	               <urstb:tbody var="resetRecord" oddRowClass="odd"
		                        evenRowClass="even" currentRowClassVar="rowClass"
		                        collection="${fileInfoChecksum.resetHistory}">
		           <urstb:tr cssClass="${rowClass}"
			                 onMouseOver="this.className='highlight'"
			                  onMouseOut="this.className='${rowClass}'">

			           <urstb:td>
			              ${resetRecord.id}
			           </urstb:td>
			           <urstb:td>
			              ${resetRecord.originalChecksum}
			           </urstb:td>
			           <urstb:td>
			             ${resetRecord.newChecksum}
			           </urstb:td>
			           <urstb:td>
			              <c:url var="viewUserUrl" value="/admin/userEditView.action">
			                  <c:param name="id" value="${resetRecord.userId}"/>
			              </c:url>
			             <a href="${viewUserUrl}">${resetRecord.userId} - view user</a>
			           </urstb:td>
			            <urstb:td>
			             ${resetRecord.dateReset}
			           </urstb:td>
			           <urstb:td>
			             ${resetRecord.notes}
			           </urstb:td>
		           </urstb:tr>
	           </urstb:tbody>
           </urstb:table>
        </div>
        
        
        <h3>Repository File</h3>
                <div class="dataTable">
            <urstb:table width="100%">
	            <urstb:thead>
		            <urstb:tr>
		                <urstb:td>File Id</urstb:td>
			            <urstb:td>File Name</urstb:td>
			            <urstb:td>Owner</urstb:td>
		            </urstb:tr>
	            </urstb:thead>
	               <urstb:tbody var="irFile" oddRowClass="odd"
		                        evenRowClass="even" currentRowClassVar="rowClass"
		                        collection="${irFiles}">
		           <urstb:tr cssClass="${rowClass}"
			                 onMouseOver="this.className='highlight'"
			                  onMouseOut="this.className='${rowClass}'">

			           <urstb:td>
			              ${irFile.id}
			           </urstb:td>
			           <urstb:td>
			              ${irFile.nameWithExtension}
			           </urstb:td>
			           <urstb:td>
			           
			              <c:url var="viewUserUrl" value="/admin/userEditView.action">
			                  <c:param name="id" value="${irFile.owner.id}"/>
			              </c:url>
			              <a href="${viewUserUrl}">${irFile.owner.firstName}&nbsp;${irFile.owner.lastName}</a>
			           </urstb:td>
		           </urstb:tr>
	           </urstb:tbody>
           </urstb:table>
        </div>
        
        <h3>Institutional Items containing File</h3>
        <div class="dataTable">
            <urstb:table width="100%">
	            <urstb:thead>
		            <urstb:tr>
			            <urstb:td>Id</urstb:td>
			            <urstb:td>Name</urstb:td>
		            </urstb:tr>
	           </urstb:thead>
	           <urstb:tbody var="institutionalItem" oddRowClass="odd"
		                        evenRowClass="even" currentRowClassVar="rowClass"
		                        collection="${institutionalItems}">
		           <urstb:tr cssClass="${rowClass}"
			                 onMouseOver="this.className='highlight'"
			                  onMouseOut="this.className='${rowClass}'">

			           <urstb:td>
			               ${institutionalItem.id}
			           </urstb:td>
			       
			           <urstb:td>
			               <c:url var="itemView" value="/institutionalPublicationPublicView.action">
						       <c:param name="institutionalItemId" value="${institutionalItem.id}"/>
						   </c:url>
						   <a href="${itemView}">${institutionalItem.name}</a><br/>
			           </urstb:td>
			
			
		           </urstb:tr>
	           </urstb:tbody>
           </urstb:table>
        </div>
        
        
        <h3>Personal Files Containing File</h3>
        <div class="dataTable">
            <urstb:table width="100%">
	            <urstb:thead>
		            <urstb:tr>
		                <urstb:td>Personal File Id</urstb:td>
			            <urstb:td>File Name</urstb:td>
			            <urstb:td>Owner</urstb:td>
			            <urstb:td>Location</urstb:td>
		            </urstb:tr>
	            </urstb:thead>
	               <urstb:tbody var="personalFile" oddRowClass="odd"
		                        evenRowClass="even" currentRowClassVar="rowClass"
		                        collection="${personalFiles}">
		           <urstb:tr cssClass="${rowClass}"
			                 onMouseOver="this.className='highlight'"
			                  onMouseOut="this.className='${rowClass}'">

			           <urstb:td>
			              ${personalFile.id}
			           </urstb:td>
			           <urstb:td>
			              ${personalFile.name}
			           </urstb:td>
			           <urstb:td>
			              <c:url var="viewUserUrl" value="/admin/userEditView.action">
			                  <c:param name="id" value="${personalFile.owner.id}"/>
			              </c:url>
			              <a href="${viewUserUrl}">${personalFile.owner.firstName}&nbsp;${personalFile.owner.lastName}</a>
			           </urstb:td>
			           <urstb:td>
			              ${personalFile.fullPath}
			           </urstb:td>
		           </urstb:tr>
	           </urstb:tbody>
           </urstb:table>
        </div>
        
        <h3>Researcher Files Containing File</h3>
        <div class="dataTable">
            <urstb:table width="100%">
	            <urstb:thead>
		            <urstb:tr>
		                <urstb:td>Researcher File Id</urstb:td>
			            <urstb:td>File Name</urstb:td>
			            <urstb:td>Researcher</urstb:td>
			            <urstb:td>Location</urstb:td>
		            </urstb:tr>
	            </urstb:thead>
	               <urstb:tbody var="researcherFile" oddRowClass="odd"
		                        evenRowClass="even" currentRowClassVar="rowClass"
		                        collection="${researcherFiles}">
		           <urstb:tr cssClass="${rowClass}"
			                 onMouseOver="this.className='highlight'"
			                  onMouseOut="this.className='${rowClass}'">

			           <urstb:td>
			              ${researcherFile.id}
			           </urstb:td>
			           <urstb:td>
			              ${personalFile.name}
			           </urstb:td>
			           <urstb:td>
			              <c:url var="viewUserUrl" value="/admin/userEditView.action">
			                  <c:param name="id" value="${researcherFile.researcher.user.id}"/>
			              </c:url>
			              <a href="${viewUserUrl}">${researcherFile.researcher.user.firstName}&nbsp;${researcherFile.researcher.user.lastName}</a>
			           </urstb:td>
			           <urstb:td>
			              ${researcherFile.fullPath}
			           </urstb:td>
		           </urstb:tr>
	           </urstb:tbody>
           </urstb:table>
        </div>
        
        <h3>Personal Items containing File</h3>
        <div class="dataTable">
            <urstb:table width="100%">
	            <urstb:thead>
		            <urstb:tr>
			            <urstb:td>Id</urstb:td>
			            <urstb:td>Name</urstb:td>
			            <urstb:td>Owner</urstb:td>
			            <urstb:td>Path</urstb:td>
		            </urstb:tr>
	           </urstb:thead>
	           <urstb:tbody var="personalItem" oddRowClass="odd"
		                        evenRowClass="even" currentRowClassVar="rowClass"
		                        collection="${personalItems}">
		           <urstb:tr cssClass="${rowClass}"
			                 onMouseOver="this.className='highlight'"
			                  onMouseOut="this.className='${rowClass}'">

			           <urstb:td>
			               ${personalItem.id}
			           </urstb:td>
			       
			           <urstb:td>
						   ${personalItem.name}
			           </urstb:td>
			           
			           <urstb:td>
			             <c:url var="viewUserUrl" value="/admin/userEditView.action">
			                  <c:param name="id" value="${personalItem.owner.id}"/>
			              </c:url>
			              <a href="${viewUserUrl}"> ${personalItem.owner.firstName}&nbsp;${personalItem.owner.lastName}</a>
			           </urstb:td>
			           
			           <urstb:td>
						   ${personalItem.path}
			           </urstb:td>
			
			
		           </urstb:tr>
	           </urstb:tbody>
           </urstb:table>
        </div>
        <!--  this is the footer of the page -->
        <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->
  


</body>
</html>