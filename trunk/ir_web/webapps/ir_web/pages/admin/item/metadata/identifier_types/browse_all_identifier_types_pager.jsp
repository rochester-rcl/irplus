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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<table class="pager">
<tr> <td>


	<ur:pager currentPageNumber="${currentPageNumber}" 
	     startPageNumber="${startPageNumber}"  
		 totalHits="${totalHits}"
		 numberOfPagesToShow="${numberOfPagesToShow}"  
		 numberOfResultsToShow="${numberOfResultsToShow}" >	

		<ur:firstPage>
		      <a href="javascript:YAHOO.ur.identifier.type.getIdentifierTypes(0, 1, 1, '${sortType}');">First</a>
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</ur:firstPage>
				
		<ur:previousPage>
			<&nbsp;<a href="javascript:YAHOO.ur.identifier.type.getIdentifierTypes(${rowStart}, ${prevousPageStartPageNumber}, ${currentPageNumber - 1}, '${sortType}');">Previous</a> &nbsp;&nbsp;
			<ur:morePrevious>
				&nbsp;<a href="javascript:YAHOO.ur.identifier.type.getIdentifierTypes(${rowStart}, ${startPageNumberForPreviousSet}, ${startPageNumberForPreviousSet}, '${sortType}');">....</a> &nbsp;&nbsp;			
  		    </ur:morePrevious>		      
			
		</ur:previousPage>
								
		<ur:forEachPage var="pageNumber">
		    <c:if test="${pageNumber != currentPageNumber}">
				 <a href="javascript:YAHOO.ur.identifier.type.getIdentifierTypes(${rowStart}, ${startPageNumber}, ${pageNumber}, '${sortType}');">${pageNumber}</a>&nbsp;&nbsp;
		     </c:if>
										
			 <c:if test="${pageNumber == currentPageNumber}">
			     ${pageNumber}&nbsp;&nbsp;
			 </c:if>
		</ur:forEachPage>								
															
		<ur:nextPage>
	       	<ur:moreNext>
			     <a href="javascript:YAHOO.ur.identifier.type.getIdentifierTypes(${nextSetRowStart}, ${startPageNumberForNextSet}, ${startPageNumberForNextSet}, '${sortType}');">....</a>&nbsp;&nbsp;&nbsp;
	        </ur:moreNext>
		
		     <a href="javascript:YAHOO.ur.identifier.type.getIdentifierTypes(${rowStart}, ${nextPageStartPageNumber}, ${currentPageNumber + 1}, '${sortType}');">Next</a>&nbsp;> &nbsp;&nbsp;
	    </ur:nextPage>

	    <ur:lastPage>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:YAHOO.ur.identifier.type.getIdentifierTypes(${rowstartForLastPage}, ${startPageNumber}, ${currentPageNumber}, '${sortType}');">Last</a> 
		</ur:lastPage>	
				    
	</ur:pager>	

</td> </tr>    
</table>   

				         
				         