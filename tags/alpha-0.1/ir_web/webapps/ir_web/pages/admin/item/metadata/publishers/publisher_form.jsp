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

<!--  form fragment for dealing with editing publishers
      this form will return with error messages in it if there
      is an issue.
 -->
<%@ taglib prefix="ir" uri="ir-tags"%>

		<!--  represents a successful submission -->
		<input type="hidden" id="newPublisherForm_success" 
		       value="${added}"/>
		       
		<!--  if editing an id must be passed -->     
	    <input type="hidden" id="newPublisherForm_id"
		        name="id" value="${publisher.id}"/>
		               
	    <input type="hidden" id="newPublisherForm_new"
		        name="newPublisher" value="true"/>
		         
		<div id="publisherError">
		    <!--  get the error messages from fieldErrors -->
			<p class="errorMessage"><ir:printError errors="${fieldErrors}" 
			key="publisher.name"/></p>
			<p class="errorMessage"><ir:printError errors="${fieldErrors}" 
			key="publisherAlreadyExists"/></p> 
		</div>
				
		<table class="formTable">    
		    <tr>       
	            <td align="left" class="label">Name:*</td>
	            <td align="left" class="input"><input type="text" 
			    id="newPublisherForm_name" 
			    name="publisher.name" 
			    value="${publisher.name}" size="45"/> </td>
			</tr>
			<tr>
			    <td align="left" class="label">Description:</td>
			    <td align="left" colspan="2" class="input"> <textarea name="publisher.description" 
	   			 id="newPublisherForm_description" cols="42" rows="4">${publisher.description}</textarea>
	            </td>
			</tr>
	    </table>

