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
<input type="hidden" id="newCollectionForm_success" 
		   value="${collectionAdded}"/>
           
<input type="hidden" id="newCollectionForm_new"
    name="newCollection" value="true"/>
           
<input type="hidden" id="newCollectionForm_collectionId"
    name="updateCollectionId" value="${updateCollectionId}"/>
              
<div id="collection_error_div">       
    <!--  get the error messages from fieldErrors -->
    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
       key="personalCollectionAlreadyExists"/></p>
</div>
              
<table class="formTable">
    <tr>
        <td class="label">Collection Name:</td>
        <td align="left" class="input"><input type="text" 
            name="collectionName" size="50" id="collectionName" value="${collectionName}"/></td>
    </tr>
    <tr>
        <td class="label">Collection Description:</td>
        <td align="left" class="input"><textarea cols="45" rows="3" name="collectionDescription" 
            id="collectionDescription">${collectionDescription}</textarea></td>
    </tr>
</table>
