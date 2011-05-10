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

<!-- This JSP file represent the metadata form
-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
			
	<c:if test="${ThumbnailFilesCount > 0}">
		<strong> Primary Image File: </strong> 
		<select id="itemForm_primary_file" name="primaryFileId" />
	
			<option value = "0"> Select </option>
			
			<c:forEach items="${thumbnailFiles}" var="thumbnailFile">
					<option value = "${thumbnailFile.id}"
					<c:if test="${item.primaryImageFile.id == thumbnailFile.id}">
						selected
					</c:if>
					> ${thumbnailFile.irFile.name}</option>
			</c:forEach>
		</select>
	</c:if>
	
	<div class="clear">&nbsp;</div>     
	       
	<table class="itemMetadataTable" width="100%">
	
		  <tr>
		      <td class="rowBorder">
	              <strong>Articles (A, The, ...)</strong>&nbsp;<strong>Title</strong>
	          </td>
	      </tr>    
	
		  <tr>
		  <td>
		  <input type="text" 
	              id="itemForm_articles" name="itemNameArticles" value="<c:out value='${item.leadingNameArticles}' />" size="15"/> 
	          <input type="text" 
	              id="itemForm_name" name="itemName" value="<c:out value='${item.name}'/>" size="90"/> 
 	           &nbsp;&nbsp;&nbsp;
 	           <input type="button" class="ur_button" id="itemForm_add_title" value="Add Other title" onclick="javascript:YAHOO.ur.item.metadata.addSubTitleRow();"/>
	      </td>
	      </tr> 
	      
	      <tr>
	      <td>
               <!--  this table is built dynamically -->
               <div id="title_forms">
               		
               		<c:forEach items="${item.subTitles}" var="title" varStatus="rowCounter">
				       	<table id="title_table_${rowCounter.count}" class="noPaddingTable" >
				
							<tr>
							 
						      <td>
						      	   <input type="text" id="itemForm_titleArticles" name="subTitleArticles" value="<c:out value='${title.leadingArticles}'/>" size="15"/>&nbsp;<input type="text" id="itemForm_title" name="subTitles" value="<c:out value='${title.title}'/>" size="90"/>
						      </td>
						      <td>   
						      	  &nbsp;&nbsp;&nbsp; <input type="button" class="ur_button" id="itemForm_remove" value="Remove Other Title" onclick="javascript:YAHOO.ur.item.metadata.removeSubTitle('title_table_${rowCounter.count}');"/>
						      </td>
						    </tr>
						</table>
					</c:forEach>  
               		
               </div> 
	      </td>
	      </tr>
          <tr>
			<td class="rowBorder"> 
	            <label  for="itemForm_abstract">Abstract</label>
	        </td>
	      </tr>
	
	      <tr>
	        <td> <textarea 
	              id="itemForm_abstract" name="itemAbstract" rows="10" cols="100"><c:out value='${item.itemAbstract}'/></textarea>
	        </td>
	      </tr>
		  <tr>
		  <td class="rowBorder">
	          <label for="itemForm_title" >Description</label>
	      </td>
	      </tr>    
	
		  <tr>
	      <td>
	          <textarea id="itemForm_desc" name="itemDescription" rows="5" cols="100"><c:out value="${item.description}"/></textarea> 
	      </td>
	      </tr>  

		  <tr>
		  <td class="rowBorder">
	          <label for="itemForm_Submitter" >Submitter</label>
	      </td>
	      </tr>    
	
		  <tr>
	      <td>
	           ${item.owner.firstName}&nbsp;${item.owner.lastName}
	      </td>
	      </tr>  
	      	      	
		  <tr>
		  <td class="rowBorder">
	          <label  for="itemForm_item_type">Publication Type(s)</label>
	      </td>
	      </tr>    
	
		  <tr> 
	      <td> Primary Type: 
	      		<select id="itemForm_item_type" name="contentTypeId" onChange="javascript:YAHOO.ur.item.metadata.saveItemTypeAndUpdateSecondaryTypes();"/>
	      			<option value = "0"> Select </option>
	      		<c:forEach items="${contentTypes}" var="contentType">
	      			<option value = "${contentType.id}"
	      			<c:if test="${item.primaryItemContentType.contentType.id == contentType.id}">
	      				selected
	      			</c:if>
	      			> ${contentType.name}</option>
	      		</c:forEach>
	      		</select>
	      </td>
	      </tr>
	        
	      <tr class="rowBorder">
	        <td>
	          <strong>Secondary Type(s):<strong>
	        </td>
	      </tr>
	      <tr>
	        <td> 
	        	<div id="type_form">
	        	    <c:import url="/pages/user/workspace/item/content_type_list.jsp"/>
	         	 </div>
		    </td>
	      </tr>
	
	      <tr>
	      <td class="rowBorder">
	          <label  for="itemForm_series_report">Series/Report Number</label> 
	      </td>
	      </tr>
	      
	      <tr>
	      <td>
	           <input type="button" class="ur_button" id="itemForm_add_series" value="Add Existing Series" onclick="javascript:YAHOO.ur.item.metadata.getSeries();"/>
	           <input type="button" class="ur_button" id="show_series" value="Create New Series"/>
	      </td>
	      </tr>
	      
	      <tr>
	      <td>
	  
	          <div id="new_series"> </div>
              <!--  this table is built dynamically -->
              <div id="series_forms">
                    <c:import url="/pages/user/workspace/item/series_list.jsp"/>
               </div> 
	      </td>
	      </tr>
	
	      <tr>
	      <td class="rowBorder">
	          <label  for="itemForm_identifier">Identifier(s)</label>
	      </td>
	      </tr>
	
		  <tr>
	      <td>
	           <input type="button" class="ur_button" id="itemForm_add_identifier" value="Add Another Identifier Entry" onclick="javascript:YAHOO.ur.item.metadata.getIdentifiers();"/>
	           <input type="button" class="ur_button" id="show_identifier_type" value="Create New Identifier Type"/>
	      </td>
	      </tr>
	      
	      <tr>
	      <td> 
	      
		         <div id="new_identifier"> </div>
	               <!--  this table is built dynamically -->
	               <div id="identifier_forms">
	                   <c:import url="/pages/user/workspace/item/identifier_list.jsp"/>
	               </div>  
	
	      </td>
	      </tr>

	      <tr>
	      <td class="rowBorder">
	          <label  for="itemForm_extent">Extent(s)</label>
	      </td>
	      </tr>
	
		  <tr>
	      <td>
	           <input type="button" class="ur_button" id="itemForm_add_extent" value="Add Another Extent Type Entry" onclick="javascript:YAHOO.ur.item.metadata.getExtents();"/>
	           <input type="button" class="ur_button" id="show_extent_type" value="Create New Extent Type"/>
	      </td>
	      </tr>
	      
	      <tr>
	          <td> 
	      
		         <div id="new_extent"> </div>
	               <!--  this table is built dynamically -->
	               <div id="extent_forms">
	                   <c:import url="/pages/user/workspace/item/extent_list.jsp"/>
	               </div>  
	
	          </td>
	      </tr>
	      
	      <tr>
			  <td class="rowBorder"> 				          
	              <label  for="itemForm_languages">Language</label>
		       </td>
		  </tr>
	
	      <tr>
		      <td>
		  	      <select id="itemForm_languages" name="languageTypeId"  />
		  	      <option value = "0"> Select </option>
	      		   <c:forEach items="${languages}" var="language">
	       			<option value = "${language.id}"
	      			<c:if test="${item.languageType.id == language.id}">
	      				selected
	      			</c:if>
	      			> ${language.name}</option>
	      		   </c:forEach>
	      	       </select>
	          </td>
	      </tr>
	      
	      <tr>
			  <td class="rowBorder"> 				          
	              <label  for="itemForm_copyright">Copyright Statement</label>
		       </td>
		  </tr>
	
	      <tr>
		      <td>
		  	      <select id="itemForm_copyright" name="copyrightStatementId"  />
		  	      <option value = "0"> Select </option>
	      		   <c:forEach items="${copyrightStatements}" var="copyright">
	       			<option value = "${copyright.id}"
	      			<c:if test="${item.copyrightStatement.id == copyright.id}">
	      				selected
	      			</c:if>
	      			><ur:maxText numChars="140" text="${copyright.name} - ${copyright.text}"/></option>
	      		   </c:forEach>
	      	       </select>
	          </td>
	      </tr>
	
	      <tr>
	      <td class="rowBorder">				  
	          <label  for="itemForm_keywords">Subject Keywords - semicolon(;) separated</label>
	      </td>
	      </tr>
	
	      <tr>
	      <td> <textarea id="itemForm_keywords" name="keywords" rows="5" cols="100"/><c:out value='${item.itemKeywords}'/></textarea>	
	      			
	      </td>
	      </tr>
	      
		  
	        
	      <tr>
	        <td class="rowBorder">
	          <label  for="itemForm_sponsor">Sponsors</label>
	        </td>
	      </tr>

		  <tr>
	      <td>
	           <input type="button" class="ur_button" id="itemForm_add_sponsor" value="Add Another Sponsor Entry" onclick="javascript:YAHOO.ur.item.metadata.getSponsors();"/>
	           <input type="button" class="ur_button" id="show_sponsor" value="Create New Sponsor"/>
	      </td>
	      </tr>
	      
	      <tr>
	      <td> 
	      
		         <div id="new_sponsor"> </div>
	               <!--  this table is built dynamically -->
	               <div id="sponsor_forms">
	                   <c:import url="/pages/user/workspace/item/sponsor_list.jsp"/>
	               </div>  
	
	      </td>
	      </tr>

		  <tr>
		  <td class="rowBorder" colspan="3">
	          <strong>Date this publication was first presented to the public (MM/DD/YYYY)</strong>
	      </td>
	      </tr>    
	
		  <tr>
	      <td>
	          <input type="text" id="itemForm_dateAvailable_month" name="firstAvailableMonth" 
	          		value="<c:if test="${item.firstAvailableDate.month != 0}">${item.firstAvailableDate.month}</c:if>"
				size="2" maxlength ="2"/>
				
	          <input type="text" id="itemForm_dateAvailable_day" name="firstAvailableDay" 
	          		value="<c:if test="${item.firstAvailableDate.day != 0}">${item.firstAvailableDate.day}</c:if>" 
	          	size="2" maxlength ="2"/>
				
	          <input type="text" id="itemForm_dateAvailable_year" name="firstAvailableYear" 
	          		value="<c:if test="${item.firstAvailableDate.year != 0}">${item.firstAvailableDate.year}</c:if>" 
	          		size="4" maxlength ="4"/>
				
	            <button type="button" id="show_available_calendar"><img alt="Calendar" 
				                       src="${pageContext.request.contextPath}/page-resources/images/all-images/calendar.gif"/></button>
	    		
	    		<div id="containerDialog3">
					<div class="hd">Calendar</div>
	
					<div  class="bd">
						 <div id="cal3Container"></div> 
					</div >
				</div >	
	      </td>
	      </tr> 
	      
		  <tr>
		  <td class="rowBorder" colspan="2">
	          <strong>Date this publication can be made available to the public (MM/DD/YYYY)</strong>
	      </td>
	      </tr>    
	
		  <tr>
	      <td>
	          <input type="text" id="itemForm_releaseDate_month" name="releaseMonth"  
	          		value="<c:if test="${releaseMonth != 0}">${releaseMonth}</c:if>" 
	          		size="2" maxlength ="2"/>
				
	          <input type="text" id="itemForm_releaseDate_day" name="releaseDay" 
	          		value="<c:if test="${releaseDay != 0}">${releaseDay}</c:if>" 
	          		size="2" maxlength ="2"/>
				
	          <input type="text" id="itemForm_releaseDate_year" name="releaseYear" 
	          		value="<c:if test="${releaseYear != 0}">${releaseYear}</c:if>" 
	          		size="4" maxlength ="4"/>
				
	    		<button type="button" id="show_release_calendar" title="Show Calendar"><img alt="Calendar" 
				                       src="${pageContext.request.contextPath}/page-resources/images/all-images/calendar.gif"/></button>
	    		
	    		<div id="containerDialog2">
					<div class="hd">Calendar</div>
	
					<div  class="bd">
						 <div id="cal2Container"></div> 
					</div >
				</div >	              
	      </td>
	      </tr> 	      

		  <tr>
		  <td class="rowBorder" colspan="2">
	          <strong>Date this publication was originally created (MM/DD/YYYY) </strong>
	      </td>
	      </tr>    
	
		  <tr>
	      <td>
	          <input type="text" id="itemForm_createdDate_month" name="createdMonth" 
	          		value="<c:if test="${item.originalItemCreationDate.month != 0}">${item.originalItemCreationDate.month}</c:if>" 
	          		size="2" maxlength ="2"/>
				
	          <input type="text" id="itemForm_createdDate_day" name="createdDay" 
	          		value="<c:if test="${item.originalItemCreationDate.day != 0}">${item.originalItemCreationDate.day}</c:if>" 
	          		size="2" maxlength ="2"/>
				
	          <input type="text" id="itemForm_createdDate_year" name="createdYear" 
	          		value="<c:if test="${item.originalItemCreationDate.year != 0}">${item.originalItemCreationDate.year}</c:if>" 
	          		size="4" maxlength ="4"/>
				
	    		<button type="button" id="show_createdDate_calendar" title="Show Calendar"><img alt="Calendar" 
				                       src="${pageContext.request.contextPath}/page-resources/images/all-images/calendar.gif"/></button>
	    		
	    		<div id="containerDialog4">
					<div class="hd">Calendar</div>
	
					<div class="bd">
						 <div id="cal4Container"></div> 
					</div >
				</div >	              
	      </td>
	      </tr> 	      
	      	      
	      <tr>
	        <td class="rowBorder" colspan="2">
	          <label  for="itemForm_externallyPublished">Will this be considered published once submitted, or has it been publicly distributed before?</label>
	        </td>
	       </tr>
	        
	      <tr>
	        <td>
	       		<input type="radio" name="isExternallyPublished"  id="itemForm_isExternallyPublished" value="true" onclick="javascript:YAHOO.ur.item.metadata.showPublisherForm();"
	       		<c:if test="${isExternallyPublished == true}">
	       			checked="true"
	       		</c:if> 
	       		> Yes
	       		<input type="radio" name="isExternallyPublished" id="itemForm_isNotExternallyPublished" value="false" onclick="javascript:YAHOO.ur.item.metadata.hidePublisherForm();"
	       		<c:if test="${isExternallyPublished == false}">
	       			checked="true"
	       		</c:if> 
	       		> No
	       	</td>
	      </tr>
	      <tr>
	      <td>
		      <div id="publisherDiv">
			      <table>
			          <tr>
				          <td>
	          			      <input type="button" class="ur_button" id="show_publisher" value="Add New Publisher"/>
	        		      </td> 	
			      	  </tr>
			          <tr>
			              <td>
			                  <label  for="itemForm_publisher">Publisher</label>
			              </td>
			          </tr>
			          <tr>
			              <td>
			              	<div id="publisher_form">
			              	    <c:import url="/pages/user/workspace/item/publisher_list.jsp"/>
		      	 		    </div>
			              </td>
			          </tr>
			          <tr>
				          <td>
	          			      <input type="button" class="ur_button" id="show_place_of_publication" value="Add New Place Of Publication"/>
	        		      </td> 	
			      	  </tr>
			          <tr>
			              <td>
			                  <label  for="itemForm_place_of_publication">Place Of Publication</label>
			              </td>
			          </tr>
			          <tr>
			              <td>
			              	<div id="place_of_publication_form">
			              	    <c:import url="/pages/user/workspace/item/place_of_publication_list.jsp"/>
		      	 		    </div>
			              </td>
			          </tr>
			          
			          <tr>
			              <td width="230">
			         		 <label  for="itemForm_datePublished">Date Published (MM/DD/YYYY)</label>
			              </td>
			          </tr>
			          <tr>
			              <td width="230">
						
		        				 
		         		<input type="text" id="itemForm_publishedDate_month" name="publishedMonth" 
		         			value="<c:if test="${item.externalPublishedItem.publishedDate.month != 0}">${item.externalPublishedItem.publishedDate.month}</c:if>"
		         			size="2" maxlength ="2"/>
			            
			            <input type="text" id="itemForm_publishedDate_day" name="publishedDay" 
			            	value="<c:if test="${item.externalPublishedItem.publishedDate.day != 0}">${item.externalPublishedItem.publishedDate.day}</c:if>" 
			            	size="2" maxlength ="2"/>
			          
			            <input type="text" id="itemForm_publishedDate_year" name="publishedYear" 
			            	value="<c:if test="${item.externalPublishedItem.publishedDate.year != 0}">${item.externalPublishedItem.publishedDate.year}</c:if>" 
			            	size="4" maxlength ="4"/>
		        				 
		        		<button type="button" id="show" title="Show Calendar"><img alt="Calendar" 
				                       src="${pageContext.request.contextPath}/page-resources/images/all-images/calendar.gif"/></button>
		        		
		        		<div id="containerDialog">
      						<div class="hd">Calendar</div>

    						<div  class="bd">
      							 <div id="cal1Container"></div> 
      						</div>
   						</div>
			        		
				        </td>
			          </tr>
			          <tr>
				         <td> 		
				       		 <label  for="itemForm_citation">Citation</label>
				         </td>  
			          </tr>
			          <tr>
				          <td> 	
				              <textarea id="itemForm_citation" name="citation" rows="10" cols="100">${item.externalPublishedItem.citation}</textarea>	
				         </td>  
			         </tr>
			      </table>
		      	</div>
	      	</td>
	      	</tr>
	
	</table>
      
 