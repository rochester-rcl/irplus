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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ir" uri="ir-tags"%>


<table >
<tr>
<td>		
<h3>Pictures</h3>
</td>
</tr>

<tr>
<td>
<c:if test="${numberOfResearcherPictures > 0 || researcher.primaryPicture != null}">
<table class="outerBorderTable" align="center">
<tr>
<td>
<table width="100%" class="spaceTable">
	<thead>
		<tr>
			<th class="thItemFolder" colspan="5">Pictures</th>
		</tr>
	</thead>
	<tbody>

		<c:if test="${researcher.primaryPicture != null }">
				<tr>
					<td width="180" align="left" >
						<table align="center" >
						<tr>
						<td>
						      <ir:transformUrl var="url" irFile="${researcher.primaryPicture}"  systemCode="PRIMARY_THUMBNAIL" download="true"/>
				              <c:if test="${url != null}">
				                  <img src="${url}"/>
				              </c:if>		
						 </td>
						 </tr>
		
						 <tr>
						 <td>
							<button class="ur_button" 
								onmouseover="this.className='ur_buttonover';"
								onmouseout="this.className='ur_button';"
								id="setDefaultPicture">Primary Picture</button>
						</td>
						</tr>
						 
						<tr>
						<td>
							<button class="ur_button"
								onmouseover="this.className='ur_buttonover';"
								onmouseout="this.className='ur_button';"
								onclick="javascript:YAHOO.ur.edit.researcher.confirmPictureDelete(${researcher.id}, ${researcher.primaryPicture.id}, true);"
								id="deleteResearcherPictureConfirm">Delete Picture&nbsp;&nbsp;</button>
						</td>
						</tr>
						</table>
					</td>
		</c:if>			
			
		<c:forEach var="irFile" items="${researcher.pictures}" varStatus="status">
			
			<c:if test="${researcher.primaryPicture != null }">
				<c:set var="count" value="${status.count + 1}" scope="page"/>
				<c:set var="primaryPictureCount" value="1" scope="page"/>
			</c:if>

			<c:if test="${status.count == 1 && researcher.primaryPicture == null}">
				<tr>
			</c:if>


			<td width="180" >
				<table align="center" >
				<tr>
				<td>
				    <ir:transformUrl var="url" irFile="${irFile}"  systemCode="PRIMARY_THUMBNAIL" download="true"/>
				    <c:if test="${url != null}">
				        <img src="${url}"/>
				    </c:if>			    
				    				   
				 </td>
				 </tr>

				 <tr>
				 <td>
					<button class="ur_button"
						onmouseover="this.className='ur_buttonover';"
						onmouseout="this.className='ur_button';"
						onclick="javascript:YAHOO.ur.edit.researcher.setAsDefaultPicture(${researcher.id}, ${irFile.id});"
						id="setDefaultPicture">Set as Primary</button>
				</td>
				</tr>
				 
				<tr>
				<td>
					<button class="ur_button"
						onmouseover="this.className='ur_buttonover';"
						onmouseout="this.className='ur_button';"
						onclick="javascript:YAHOO.ur.edit.researcher.confirmPictureDelete(${researcher.id}, ${irFile.id}, false);"
						id="deleteResearcherPictureConfirm">Delete Picture&nbsp;</button>
				</td>
				</tr>
				</table>
			</td>
			
			<c:if test="${ ((status.count + primaryPictureCount) % 5) == 0}">
				</tr>
				<tr>
			</c:if>
						
			<c:if test="${ (numberOfResearcherPictures+ primaryPictureCount) == (status.count + primaryPictureCount)}">
				</tr>
			</c:if>								
		</c:forEach>
		
		
	</tbody>

</table>
</td>
</tr>

</table>
</c:if>
<c:if test="${numberOfResearcherPictures <= 0 && researcher.primaryPicture == null}">
    There are currently no researcher pictures.
</c:if>

</td>
</tr>

</table>