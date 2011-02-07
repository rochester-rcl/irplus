<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>



<div class="dataTable">
    <urstb:table width="100%">
        <urstb:thead>
            <urstb:tr>
                <urstb:td> Name </urstb:td>
                <urstb:td> Description </urstb:td>
                <urstb:td> Action </urstb:td>
            </urstb:tr>
        </urstb:thead>
        <urstb:tbody
            var="group" 
            oddRowClass="odd"
            evenRowClass="even"
            currentRowClassVar="rowClass"
            collection="${groupWorkspace.groups}">
            <urstb:tr 
                cssClass="${rowClass}"
                onMouseOver="this.className='highlight'"
                onMouseOut="this.className='${rowClass}'">
                <urstb:td>
                    ${group.name}
                </urstb:td>
                        
                <urstb:td>
                    ${group.description}
                </urstb:td>

                <urstb:td>
                    <a href="">Edit</a>&nbsp;/&nbsp;<a href="javascript:YAHOO.ur.user.workspace_group.deleteWorkspaceGroup(${group.id})">Delete</a>
                </urstb:td>                        

            </urstb:tr>
        </urstb:tbody>
    </urstb:table>
</div>