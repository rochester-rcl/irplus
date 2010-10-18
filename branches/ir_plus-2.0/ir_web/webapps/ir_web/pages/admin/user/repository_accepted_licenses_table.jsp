<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
 
 			         <div class="dataTable">
	                     <urstb:table width="100%">
	                         <urstb:thead>
	                             <urstb:tr>
					                 <urstb:td>License Id</urstb:td>         
	                                 <urstb:td>Name</urstb:td>
	                                 <urstb:td>Text</urstb:td>
	                                 <urstb:td>Date Accepted</urstb:td>
	                             </urstb:tr>
	                         </urstb:thead>
	                         <urstb:tbody
	                             var="userRepositoryLicense" 
	                             oddRowClass="odd"
	                             evenRowClass="even"
	                             currentRowClassVar="rowClass"
	                             collection="${irUser.acceptedLicenses}">
	                             <urstb:tr 
	                                 cssClass="${rowClass}"
	                                 onMouseOver="this.className='highlight'"
	                                 onMouseOut="this.className='${rowClass}'">
	                                 <urstb:td>
		                                 ${userRepositoryLicense.id}
	                                 </urstb:td>
	                                 <urstb:td>
		                                  ${userRepositoryLicense.licenseVersion.license.name}
	                                 </urstb:td>
	                                 <urstb:td>
		                   		          ${userRepositoryLicense.licenseVersion.license.text}
	                                 </urstb:td>
	                                 <urstb:td>
		                   		          ${userRepositoryLicense.dateAccepted}
	                                 </urstb:td>
	                        
	                             </urstb:tr>
	                         </urstb:tbody>
	                     </urstb:table>
                   