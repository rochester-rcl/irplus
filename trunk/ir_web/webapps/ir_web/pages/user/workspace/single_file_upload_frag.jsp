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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<table class="formTable">
    <tr>
        <td colspan="2">
            <input type="hidden" id="file_added" value="${allFilesAdded}"/>
            <!--  get the error messages from fieldErrors -->
            <div id="single_file_upload_error">
                <p class="errorMessage" id="upload_error">
                    <c:forEach var="fileUploadInfo" items="${filesNotAdded}">
                        The name: "${fileUploadInfo.fileName}" already exits as a file or folder in this folder 
                    <br/>
                    </c:forEach> 
                </p>
            </div>
        </td>
    </tr>
    <tr>
        <td class="label">File:</td>
        <td align="left" class="input"><input size="100" id="single_file_upload" name="file"
             type="file"></td>
    </tr>
    <tr>
        <td class="label">Display Name(optional):</td>
        <td align="left" class="input"><input id="user_file_name" 
            name="userFileName" value="${filesNotAdded[0].userFileName}" size="50" type="text"/></td>
    </tr>
    <tr>
        <td class="label">File Description:</td>
        <td align="left" class="input"><textarea id="user_file_description" 
            name="userFileDescription" cols="47" rows="2">${filesNotAdded[0].description}</textarea></td>
    </tr>
</table>