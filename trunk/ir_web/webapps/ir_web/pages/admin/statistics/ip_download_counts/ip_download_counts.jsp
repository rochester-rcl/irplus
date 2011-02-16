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
    <title>Ignore Ipaddress range</title>
    
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
	<ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>

    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 </head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
       
       <!--  this is the header of the page -->
       <c:import url="/inc/header.jsp"/>    
       <h3>Downloads by IP Address</h3>
      
        <div id="bd">
            
            <c:if test="${totalHits > 0}">
                <c:import url="ip_download_counts_pager.jsp"/>
                <br/>
            </c:if>
            
 			<div class="dataTable">
							                 
			    <urstb:table width="100%">
				    <urstb:thead>
					    <urstb:tr>
					        <urstb:td>IP Address</urstb:td>
					        
					        <c:url var="sortAscendingUrl" value="/admin/viewDownloadCountsByIp.action">
							    <c:param name="rowStart" value="${rowStart}"/>
								<c:param name="startPageNumber" value="${startPageNumber}"/>
								<c:param name="currentPageNumber" value="${currentPageNumber}"/>	
								<c:param name="sortElement" value="name"/>		
								<c:param name="sortType" value="asc"/>
							</c:url>
					                     
					        <c:url var="sortDescendingUrl" value="/admin/viewDownloadCountsByIp.action">
							    <c:param name="rowStart" value="${rowStart}"/>
								<c:param name="startPageNumber" value="${startPageNumber}"/>
								<c:param name="currentPageNumber" value="${currentPageNumber}"/>	
								<c:param name="sortElement" value="name"/>		
								<c:param name="sortType" value="desc"/>
							</c:url>
					                    
					        <urstb:tdHeadSort  height="33"
					            useHref="true"
					            hrefVar="href"
                                currentSortAction="${sortType}"
                                ascendingSortAction="${sortAscendingUrl}"
                                descendingSortAction="${sortDescendingUrl}">
                                <a href="${href}">Download Count</a>                                              
                                            <urstb:thImgSort
                                                         sortAscendingImage="page-resources/images/all-images/bullet_arrow_down.gif"
                                                         sortDescendingImage="page-resources/images/all-images/bullet_arrow_up.gif"/></urstb:tdHeadSort>
					                   
						</urstb:tr>
			        </urstb:thead>
					<urstb:tbody var="downloadCount" 
						         oddRowClass="odd"
						         evenRowClass="even"
						         currentRowClassVar="rowClass"
						         collection="${downloadCounts}">
				        <urstb:tr cssClass="${rowClass}"
						          onMouseOver="this.className='highlight'"
						          onMouseOut="this.className='${rowClass}'">
						                        
						          
						    <urstb:td>
						     ${downloadCount.ipAddress}                      
						    </urstb:td>
						                        
						    <urstb:td>
						     ${downloadCount.downloadCount}
						    </urstb:td>
						</urstb:tr>
			        </urstb:tbody>
				</urstb:table>
			</div>	           
  
            <c:if test="${totalHits > 0}">
                <c:import url="ip_download_counts_pager.jsp"/>
            </c:if>
	    </div>
        <!--  end body div -->
      
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End doc div-->
  

</body>
</html>