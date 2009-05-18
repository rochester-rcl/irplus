<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

<h3>Total subscribers for this collection: ${ur:collectionSize(collection.subscriptions)}</h3>
<div class="dataTable">
    <urstb:table width="50%">
	    <urstb:caption>Subscribers for this collection</urstb:caption>
		<urstb:thead>
		    <urstb:tr>
			    <urstb:td>First Name</urstb:td>
				<urstb:td>Last Name</urstb:td>
				<urstb:td>Actions</urstb:td>
		    </urstb:tr>
	    </urstb:thead>
		<urstb:tbody var="subscriber" 
			 oddRowClass="odd"
			 evenRowClass="even"
			 currentRowClassVar="rowClass"
			 collection="${collection.subscriptions}">
						                
			<urstb:tr cssClass="${rowClass}"
					  onMouseOver="this.className='highlight'"
					  onMouseOut="this.className='${rowClass}'">
					  
					  <urstb:td>${subscriber.user.firstName}</urstb:td>
					  <urstb:td>${subscriber.user.lastName}</urstb:td>
					  <urstb:td><a href="javascript:YAHOO.ur.edit.institution.collection.unsubscribeUser(${subscriber.user.id},${collection.id})">Unsubscribe</a></urstb:td>
			</urstb:tr>
		</urstb:tbody>
	</urstb:table>
</div>