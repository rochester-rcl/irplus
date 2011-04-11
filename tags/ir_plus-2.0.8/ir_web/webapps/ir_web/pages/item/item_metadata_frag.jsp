<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />


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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table class="greyBorderBlueBgTable">
    <tr>
        <c:if test="${!ir:isStringEmpty(item.itemAbstract) || !ir:isStringEmpty(item.description) || !ur:isEmpty(item.subTitles)}">
	    <td width="60%" valign="top" class="greyBorderTableTd">
		    <table class="noPaddingTable" width="100%" align="left">
		       <c:if test="${!ur:isEmpty(item.subTitles)}">
		            <tr>
					    <td><label for="description" class="previewLabel"> Other Titles </label> </td>
					</tr> 
					<tr>
					    <td>
						    <c:forEach items="${item.subTitles}" var="otherTitle">
							    ${otherTitle.fullTitle}<br/>
							</c:forEach>
						</td>
					</tr>
			    </c:if>
		        <c:if test="${!ir:isStringEmpty(item.description)}">
			        <tr>
				        <td>	<label for="description" class="previewLabel"> Description </label> </td>
				    </tr> 
				
				    <tr>
				        <td>	${item.description} </td>
				    </tr>
				</c:if>
				<c:if test="${!ir:isStringEmpty(item.itemAbstract)}">
				    <tr>
				        <td>	<label for="abstract" class="previewLabel"> Abstract </label> </td>
				    </tr> 
				    <tr>
				        <td>	${item.itemAbstract} </td>
				    </tr>
				</c:if>
		    </table>
		</td>
		</c:if>
		<td valign="top" class="greyBorderTableTd">
		    <table width="100%">
		        
		        <c:if test="${!ur:isEmpty(item.contributors)}">
			        <tr> 
			            <td class="previewLabel">Contributor(s):</td>
				    </tr>
				
				    <c:forEach items="${item.contributors}" var="itemContributor">
				        <tr>
				            <td>
				                <c:url var="contributorUrl" value="/viewContributorPage.action">
						            <c:param name="personNameId" value="${itemContributor.contributor.personName.id}"/>
						        </c:url>						                             
						        <a href="${contributorUrl}"> <ir:authorName personName="${itemContributor.contributor.personName}" displayDates="true"/></a> - ${itemContributor.contributor.contributorType.name}
					        </td>
					    </tr>
				    </c:forEach>
				</c:if>
				
				<c:if test="${item.primaryItemContentType != null}">
				    <tr>
				        <td class="previewLabel"> Primary Item Type:  </td>
				    </tr>
				    <tr>
				        <td> ${item.primaryItemContentType.contentType.name} </td>											
				    </tr>
				</c:if>
				<c:if test="${!ur:isEmpty(item.allNonPrimaryItemContentTypes)}">
				    <tr>
				        <td class="previewLabel"> Secondary Item Type(s): </td>
				    </tr>
				    <c:forEach items="${item.allNonPrimaryItemContentTypes}" var="itemContentType">
				        <tr>
				            <td>${itemContentType.contentType.name} </td>
				        </tr>
				    </c:forEach>
				</c:if>
				<c:if test="${!ur:isEmpty(item.itemReports)}">
				    <tr>
				        <td class="previewLabel"> Series/Report Number:</td>
				    </tr>
										
				    <c:forEach items="${item.itemReports}" var="itemReport">
				       <tr>
				           <td>
					          ${itemReport.series.name} / ${itemReport.reportNumber}
					       </td>
				       </tr>
				    </c:forEach>
				</c:if>
				
				<c:if test="${!ur:isEmpty(item.itemIdentifiers)}">										
				    <tr>
				        <td class="previewLabel"> Identifiers: </td>
				    </tr>
				    <c:forEach items="${item.itemIdentifiers}" var="itemIdentifier">
				        <tr>
				            <td> ${itemIdentifier.identifierType.name}&nbsp;${itemIdentifier.value}</td>
				        </tr>
				    </c:forEach>	
				</c:if>	
				
				<c:if test="${item.languageType != null}">								
				    <tr>
				        <td class="previewLabel"> Language:</td>
				    </tr>
				    <tr>
				        <td> ${item.languageType.name} </td>
				    </tr>
				</c:if>	
				
				<c:if test="${!ir:isStringEmpty(item.itemKeywords)}">	
				    <tr>
				        <td class="previewLabel"> Subject Keywords: </td>
				    </tr>
				    <tr>
				        <td> ${item.itemKeywords}</td>
				    </tr>
				</c:if>
				<c:if test="${!ur:isEmpty(item.itemSponsors)}">	
				    <tr>
				        <td class="previewLabel"> Sponsor - Description:</td>
				    </tr>
				    <c:forEach items="${item.itemSponsors}" var="itemSponsor">
				         <c:url var="sponsorUrl" value="/viewSponsorPage.action">
			                 <c:param name="sponsorId" value="${itemSponsor.sponsor.id}"/>
						</c:url>	
				        <tr>
				           <td>  <a href="${sponsorUrl}">${itemSponsor.sponsor.name}</a> - ${itemSponsor.description} </td>
				        </tr>
				    </c:forEach>
				</c:if>
				<c:if test="${ item.firstAvailableDate != null && (item.firstAvailableDate.month != 0 || 
				              item.firstAvailableDate.day != 0 || 
				              item.firstAvailableDate.year != 0) }">
				    <tr>
				        <td class="previewLabel"> First presented to the public:</td>
				    </tr>
				    <tr>
				        <td>${ir:getSlashedDate(item.firstAvailableDate)}</td>
				    </tr>
				</c:if>
				
				<c:if test="${ item.originalItemCreationDate != null && (item.originalItemCreationDate.month != 0 ||
				              item.originalItemCreationDate.day != 0 ||
				              item.originalItemCreationDate.year != 0)}">						
				<tr>
				    <td class="previewLabel">Originally created:</td>
				</tr>
				<tr>
				    <td>${ir:getSlashedDate(item.originalItemCreationDate)}</td>
				</tr>
				
				</c:if>
				
				<c:if test="${item.releaseDate != null}">					
				<tr>
				    <td class="previewLabel"> Date will be made available to public:</td>
				</tr>
				<tr>
				    <td>
					    ${item.releaseDate} &nbsp;&nbsp;
					</td>
				</tr>
				</c:if>
										
				<c:if test="${item.externalPublishedItem != null}">
				
					
				<c:if test="${item.externalPublishedItem != null && (item.externalPublishedItem.publishedDate.month != 0 ||
				              item.externalPublishedItem.publishedDate.day != 0 ||
				              item.externalPublishedItem.publishedDate.year != 0)}">
				     <tr>
				        <td class="previewLabel">Original Publication Date:</td>
				    </tr>	
				    <tr>
				        <td>${ir:getSlashedDate(item.externalPublishedItem.publishedDate)}</td>
				    </tr>					
				</c:if>

                <c:if test="${item.externalPublishedItem.publisher != null}">
                <tr>
				    <td class="previewLabel">Previously Published By:</td>
				</tr>
						
				<tr>
				    <td>${item.externalPublishedItem.publisher.name} </td>
				</tr>	
				</c:if>	
				 <c:if test="${item.externalPublishedItem.citation != null}">
                 <tr>
				    <td class="previewLabel">Citation:</td>
				</tr>
				<tr>
				    <td>${item.externalPublishedItem.citation} </td>
			    </tr>
			    </c:if>
			</c:if>									

                <c:if test="${!ur:isEmpty(item.itemExtents)}">	
				    <tr>
				        <td class="previewLabel"> Extents:</td>
				    </tr>
				    <c:forEach items="${item.itemExtents}" var="itemExtent">
				        <tr>
				           <td>  ${itemExtent.extentType.name} - ${itemExtent.value} </td>
				        </tr>
				    </c:forEach>
				</c:if>

			   
			    <c:if test="${institutionalItemVersion != null}">
			    <tr>	
			        <td class="previewLabel">License Grantor / Date Granted:  </td>
				</tr>
				<tr>
				    <td>
				        
					    <c:url var="viewLicense" value="viewItemRepositoryLicense.action">
						     <c:param name="versionedLicenseId" value="${institutionalItemVersion.repositoryLicense.licenseVersion.versionedLicense.id}"/>
							 <c:param name="version" value="${institutionalItemVersion.repositoryLicense.licenseVersion.versionNumber}"/>
						</c:url>
						${institutionalItemVersion.repositoryLicense.grantedByUser.firstName}&nbsp;${institutionalItemVersion.repositoryLicense.grantedByUser.lastName} 
						/ ${institutionalItemVersion.repositoryLicense.dateGranted} ( <a href="${viewLicense}">View License</a> )
						
					</td>
				</tr>
				<tr>
				    <td class="previewLabel">Date Deposited</td>
			    </tr>
				<tr>
				    <td>
					    ${institutionalItemVersion.dateOfDeposit}
					</td>
				</tr>
				    <c:if test="${institutionalItemVersion.dateLastModified != institutionalItemVersion.dateOfDeposit}">
				    <tr>
				        <td class="previewLabel">Date Last Updated</td>
			        </tr>
				    <tr>
				        <td>
					    ${institutionalItemVersion.dateLastModified}
					    </td>
				    </tr>
				    <c:if test="${user != null && institutionalItem.owner == user || (ir:userHasRole('ROLE_ADMIN', '')) }">
		                   <c:if test="${institutionalItemVersion.lastModifiedNote != null}">
			                    <tr>
				                   <td class="previewLabel">Last Updated Note</td>
			                    </tr>
				                <tr>
				                    <td>
					                 ${institutionalItemVersion.lastModifiedNote}
					                </td>
				                </tr>
				            </c:if>
				                <tr>
				                    <td class="previewLabel">Last Updated By</td>
			                    </tr>
				                <tr>
				                    <td>
					                 ${institutionalItemVersion.lastModifiedBy.firstName}&nbsp; ${institutionalItemVersion.lastModifiedBy.lastName}
					                </td>
				                </tr>
				           </c:if>
				        </c:if>	
				    </c:if>				
				
				<tr>
				    <td class="previewLabel">Submitter:</td>
			    </tr>
				<tr>
				    <td>
					    ${item.owner.firstName}&nbsp;${item.owner.lastName}
					</td>
				</tr>				
  			</table>
  			
        </td>
	</tr>
	<c:if test="${item.copyrightStatement != null}">
	<tr>
	    <td colspan="2" class="greyBorderTableTd">
  			 <p>Copyright &copy; ${item.copyrightStatement.text}</p>
	    </td>
	</tr>
	</c:if>
</table>