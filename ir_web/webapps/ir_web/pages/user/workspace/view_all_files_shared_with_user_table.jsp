<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>



<div class="dataTable">
    <urstb:table width="100%">
        <urstb:thead>
            <urstb:tr>
                <urstb:td> File Name </urstb:td>
                <urstb:td> File Owner </urstb:td>
                <urstb:td> Path </urstb:td>
            </urstb:tr>
        </urstb:thead>
        <urstb:tbody
            var="personalFile" 
            oddRowClass="odd"
            evenRowClass="even"
            currentRowClassVar="rowClass"
            collection="${personalFiles}">
            <urstb:tr 
                cssClass="${rowClass}"
                onMouseOver="this.className='highlight'"
                onMouseOut="this.className='${rowClass}'">
                <urstb:td>
                    ${personalFile.name}
                </urstb:td>
                <urstb:td>
                    ${personalFile.versionedFile.owner.firstName}&nbsp;${personalFile.versionedFile.owner.lastName}
                </urstb:td>       
                <urstb:td>
                    <c:if test="${!empty personalFile.personalFolder}">
                    
                        <c:url var="viewWorkspace" value="/user/workspace.action">
                            <c:param name="parentFolderId" value="${personalFile.personalFolder.id}"/>
                        </c:url>
                    </c:if>
                    <c:if test="${empty personalFile.personalFolder}">
                        <c:url var="viewWorkspace" value="/user/workspace.action">
                            <c:param name="parentFolderId" value="0"/>
                        </c:url>
                    </c:if>
                    <a href="${viewWorkspace}">${personalFile.fullPath}</a>
                </urstb:td>
            </urstb:tr>
        </urstb:tbody>
    </urstb:table>
</div>