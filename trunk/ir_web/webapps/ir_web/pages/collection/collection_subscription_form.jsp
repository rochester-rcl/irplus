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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
   <c:if test="${user == null}">
       <p> <a href="<c:url value="/user/workspace.action"/>">Login</a> or <a href="<c:url value="viewUserRegistration.action"/>">Create an Account</a> to subscribe to this collection. </p>
	</c:if>
	<c:if test="${user != null}">	
	    <c:if test="${!subscriber}">
		    <table class="formTable">  
			    <tr> 
				    <td align="left" class="label"> Click on the "Subscribe" button to receive e-mails about new submissions [Note: this is a daily digest and only sends e-mails if there is new content]</td> 
			    </tr>  
		        <tr>       
	                <td align="left" class="label">Include all subcollections: <input type="checkbox" name="includeSubCOllections" id="include_sub_collections">  </td>
			    </tr>
			    <tr>
			        <td align="left" class="label" ><button class="ur_button"
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';"
                           onclick="javascript:YAHOO.ur.public.collection.view.subscribe(${user.id});">Subscribe</button> </td>
			    </tr>
		    </table>                       
	     </c:if>
						 
	    <c:if test="${subscriber}">
		    <table class="formTable">  
			    <tr> 
				    <td align="left" class="label"> Click on the "UnSubscribe" button to no longer receive e-mails about new content in this collection.</td> 
			    </tr>  
			    <tr>
			        <td align="left" class="label" ><button class="ur_button"
                           onmouseover="this.className='ur_buttonover';"
                           onmouseout="this.className='ur_button';"
                           onclick="javascript:YAHOO.ur.public.collection.view.unsubscribe(${user.id});">UnSubscribe</button> </td>
			    </tr>
		    </table>                       
	     </c:if>
	 </c:if>
	 
	 <p><img src="<c:url value='/page-resources/images/all-images/feed.jpg'/>" alt="RSS Feed"/>&nbsp;<a href="${collectionRss}">${institutionalCollection.name} Recent Submissions</a></p>
