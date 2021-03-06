<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>


<div class="dataTable">
    <urstb:table width="100%">
	    <urstb:thead>
	        <urstb:tr>
	            <urstb:td>Id</urstb:td>
                <urstb:td>Name</urstb:td>
                <urstb:td>Path</urstb:td>
                <urstb:td>Description</urstb:td>
                <urstb:td>Edit</urstb:td>
	        </urstb:tr>
	    </urstb:thead>
	    <urstb:tbody
	        var="fileDatabase" 
	        oddRowClass="odd"
	        evenRowClass="even"
	        currentRowClassVar="rowClass"
	        collection="${fileServer.fileDatabases}">
	                       
	        <urstb:tr 
	            cssClass="${rowClass}"
	            onMouseOver="this.className='highlight'"
	            onMouseOut="this.className='${rowClass}'">
	                            
	            <urstb:td>
		            ${fileDatabase.id}
	            </urstb:td>
	            <urstb:td>
	                <c:url var="fileDatabaseUrl" value="/admin/viewFileDatabase.action">
	                    <c:param name="fileServerId" value="${fileServer.id}"/>
	                    <c:param name="fileDatabaseId" value="${fileDatabase.id}"/>
	                </c:url>
			        <a href="${fileDatabaseUrl}">${fileDatabase.name}</a>
	            </urstb:td>
	            <urstb:td>
			        ${fileDatabase.fullPath}
	            </urstb:td>
	            <urstb:td>
			        ${fileDatabase.description}
	            </urstb:td>
	            <urstb:td>
			        <a href="javascript:YAHOO.ur.file.database.edit(${fileServer.id},${fileDatabase.id})">Edit</a>&nbsp;/&nbsp;<a href="javascript:YAHOO.ur.file.database.deleteFileDatabase(${fileDatabase.id})">Delete</a>
	            </urstb:td>
	        </urstb:tr>
	    </urstb:tbody>
	</urstb:table>
</div>