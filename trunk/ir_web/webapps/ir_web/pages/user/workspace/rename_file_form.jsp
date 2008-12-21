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

<div id="rename_error_div">       
    <!--  get the error messages from fieldErrors -->
    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
       key="renameFileMessage"/></p>
    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
        key="illegalNameError"/></p> 
</div>

<!--  represents a successful submission -->
<input type="hidden" id="renameForm_success" 
		   value="${fileRenamed}"/>

<input type="hidden" name="personalFileId"  value="${personalFileId}"/>
New File Name: <input type="text" name="newFileName" size="25" value="${newFileName}"/>          
