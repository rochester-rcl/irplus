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

<!--  footer  -->
<div id="ft">
 
   <ul id="bottomNav" class="basicNav">
        <li><a href="<c:url value="/help.action"/>">Help</a>&nbsp;|&nbsp;</li>
        <li><a href="<c:url value="/contactUs.action"/>">Contact Us</a>&nbsp;|&nbsp;</li>
        <li><a href="<c:url value="/about.action"/>">About</a>&nbsp;|&nbsp;</li>
        <li><a href="<c:url value="/privacyPolicy.action"/>">Privacy Policy</a></li>
    </ul>
    <div class="copyright"><i>Copyright &copy; UNIVERSITY OF ROCHESTER LIBRARIES. All Rights Reserved</i></div>
   
</div>
<!-- end footer -->