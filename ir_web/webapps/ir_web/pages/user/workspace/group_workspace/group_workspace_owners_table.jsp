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
                <urstb:td> Email </urstb:td>
                <urstb:td> Action </urstb:td>
            </urstb:tr>
        </urstb:thead>
        <urstb:tbody
            var="owner" 
            oddRowClass="odd"
            evenRowClass="even"
            currentRowClassVar="rowClass"
            collection="${groupWorkspace.owners}">
            <urstb:tr 
                cssClass="${rowClass}"
                onMouseOver="this.className='highlight'"
                onMouseOut="this.className='${rowClass}'">
                <urstb:td>
                    ${owner.firstName}&nbsp;${owner.lastName}
                </urstb:td>
                        
                <urstb:td>
                    ${owner.defaultEmail.email}
                </urstb:td>

                <urstb:td>
                    <a href="">Add</a> / <a href="">Edit</a>
                </urstb:td>                        

            </urstb:tr>
        </urstb:tbody>
    </urstb:table>
</div>