<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

	        <div class="dataTable">
	            <urstb:table width="100%">
	                <urstb:thead>
	                    <urstb:tr>
	                        <urstb:td>Id</urstb:td>
					        <urstb:td>Name</urstb:td>
					        <urstb:td>User Name Case Sensitive</urstb:td>
					        <urstb:td>Description</urstb:td>
					        <urstb:td>Action</urstb:td>
	                    </urstb:tr>
	                </urstb:thead>
	                <urstb:tbody
	                    var="externalAccountType" 
	                    oddRowClass="odd"
	                    evenRowClass="even"
	                    currentRowClassVar="rowClass"
	                    collection="${externalAccountTypes}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                        ${externalAccountType.id}
	                        </urstb:td>
	                        <urstb:td>
			                   ${externalAccountType.name}
	                        </urstb:td>
	                        <urstb:td>
			                   ${externalAccountType.userNameCaseSensitive}
	                        </urstb:td>
	                        <urstb:td>
	                             ${externalAccountType.description}
	                        </urstb:td>
	                        <urstb:td>
	                             <a href="javascript:YAHOO.ur.external_account_type.edit(${externalAccountType.id});">Edit</a>/<a href="javascript:YAHOO.ur.external_account_type.deleteAccount(${externalAccountType.id})">Delete</a>
	                        </urstb:td>
	                    </urstb:tr>
	                </urstb:tbody>
	            </urstb:table>
              </div>
              <!--  end table -->