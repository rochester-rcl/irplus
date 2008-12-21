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

<%@ taglib prefix="ir" uri="ir-tags"%>

<!--  represents a successful submission -->
<input type="hidden" id="researcher_personal_link_form_success"  value="${linkAdded}"/>
		   		               
<input type="hidden" id="researcher_personal_link_form_new" name="newLink" value="true"/>
      
<input type="hidden" id="researcher_personal_link_id"  name="linkId" value="${linkId}"/>

<div id="researcher_personal_link_error_div">       
    <!--  get the error messages from fieldErrors -->
    <p class="errorMessage"><ir:printError errors="${fieldErrors}"  key="linkNameAlreadyExists"/></p>
</div>
    
<table class="formTable">

     <tr>
         <td class="label">Link Name:</td>
         <td align="left" class="input"> 
             <input id="researcher_personal_link_name" size="50" type="text" name="linkName" value="${linkName}"/>
         </td>
     </tr>
     <tr>
         <td class="label"> Link URL:</td>
         <td align="left" class="input"> 
             <input id="researcher_personal_link_url" size="50" type="text" name="linkUrl" value="${linkUrl}"/>
         </td>
     </tr>
     <tr>
         <td class="label"> Link Description:</td>
         <td align="left" class="input"><textarea cols="45" rows="3" id="researcher_personal_link_description" 
             name="linkDescription">${linkDescription}</textarea></td>
     </tr>
 </table>