<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>


<div class="dataTable">
	<form method="POST" id="fileServers" name="fileServers" >
	    <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
	                <urstb:td>Id</urstb:td>
                    <urstb:td>Name</urstb:td>
                    <urstb:td>Description</urstb:td>
                    <urstb:td>Edit</urstb:td>
	            </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="fileServer" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${fileServers}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        ${fileServer.id}
	                        </urstb:td>
	                        <urstb:td>
	                           <c:url var="viewFileServer" value="/admin/viewFileServer.action">
	                               <c:param name="fileServerId" value="${fileServer.id}"/>
	                           </c:url>
			                   <a href="${viewFileServer}">${fileServer.name}</a>
	                        </urstb:td>
	                        <urstb:td>
			                   ${fileServer.description}
	                        </urstb:td>
	                        <urstb:td>
			                   <a href="javascript:YAHOO.ur.file.server.edit(${fileServer.id})">Edit</a>/<a href="javascript:YAHOO.ur.file.server.deleteFileServer(${fileServer.id})">Delete</a>
	                        </urstb:td>
	                    </urstb:tr>
	        </urstb:tbody>
	    </urstb:table>
    </form>
</div>	

