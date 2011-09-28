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
  
<!-- Form to enter the new password  --> 

<table class="formTable"> 

     <tr>
         <td class="label" colspan="2">Password must be at least 8 characters long</td>
     </tr>
     <tr>
         <td class="label">New Password *:</td>
         <td align="left" class="input"> 
             <input type="password" id="change_password_form_new_password" name="password"  value=""/>
         </td>
     </tr>
     <tr>
         <td class="label">Confirm new password*:</td>
         <td align="left" class="input"> 
              <input type="password"  id="change_password_form_confirm_password"  name="confirmPassword"  value=""/>
         </td>
     </tr>
 
</table>
