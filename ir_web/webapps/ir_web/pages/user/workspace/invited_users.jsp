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

<div class="jmesa">
	<input type="hidden" id="collaborators_share_file_ids"	name="shareFileIds" value="${shareFileIds}"/>
	<div class="clear">&nbsp;</div>
	<table class="formTable">
	
		<c:forEach var="personalFile" items="${filesToShare}" >
		
			<tr>
			<td colspan="3">
				<ir:fileTypeImg cssClass="tableImg" versionedFile="${personalFile.versionedFile}"/> ${personalFile.name}&nbsp;  
				<a href="javascript:YAHOO.ur.invite.removeFile('${personalFile.id}', '${shareFileIds}');">Remove</a>  
			</td>
			</tr>
		
			<c:forEach var="fileCollaborator" items="${personalFile.versionedFile.collaborators}" >	  
				<tr>
					<td>
			                    ${fileCollaborator.collaborator.firstName} &nbsp;${fileCollaborator.collaborator.lastName} (${fileCollaborator.collaborator.defaultEmail.email})
			        </td>
			        <td>
		 						<a href="javascript:YAHOO.ur.invite.unshareFile('${fileCollaborator.id}',
			                           '${personalFile.id}', '${shareFileIds}');">Unshare</a>
			         </td>
			         <td>       
			                   <a href="javascript:YAHOO.ur.invite.editPermissions('${fileCollaborator.id}');">Edit</a>
			        </td>
			    </tr>
			</c:forEach>
			
			<c:forEach var="invite" items="${personalFile.versionedFile.invitees}" >	  
				<tr>
					<td>      ${invite.email} - [to be shared]    </td>
					<td>		<a href="javascript:YAHOO.ur.invite.unsharePendingInvitee('${invite.id}',
			                           '${personalFile.id}', '${shareFileIds}');">UnShare</a>
			        </td>
			    </tr>
			</c:forEach>
		    <div class="clear">&nbsp;</div>          
		</c:forEach>
	</table>
  <div class="clear">&nbsp;</div>
</div>