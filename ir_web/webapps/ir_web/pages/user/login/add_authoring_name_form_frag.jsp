<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form id="addPersonName" name="newPersonNameForm" 
	                       method="post" 
	                       action="<c:url value="/user/createPersonName.action"/>">
		           	
		           	<input type="hidden" id="newPersonNameForm_id"
		                   name="id" value=""/>
		                   
   	             	<input type="hidden" id="newPersonNameForm_personId"
		                   name="personId" value="${irUser.personNameAuthority.id}"/>
		                   
		             <input type="hidden" id="newPersonNameForm_authorityId"
		                   name="authorityId" value="${personNameAuthority.id}"/>
		                   
  					<input type="hidden" id="newPersonNameForm_userId"
		                   name="addToUserId" value="${irUser.id}"/>		                   
		               
		         	<input type="hidden" id="newPersonNameForm_new"
		                   name="newPersonName" value="true"/>
					
					<input type="hidden" id="newPersonNameForm_myName"
		                   name="myName" value="true"/>		                   
		             
		             <input type="hidden" id="name_added" value="${added}"/>            
	             <div id="personError"class="errorMessage">${added}</div>
		          
		         <label for="newPersonNameFormFirstName">First Name:</label><input type="text" 
		              id="newPersonNameFormFirstName" 
		              name="personName.forename" 
		              value=""/>
		          
		         <div class="clear">&nbsp;</div>
		          
		         <label for="newPersonNameFormLastName">Last Name*:</label><input type="text" 
		              id="newPersonNameFormLastName" 
		              name="personName.surname" 
		              value=""/>
		          
		         <div class="clear">&nbsp;</div>
		          
		         <label for="newPersonNameFormNameMiddleName">Middle Name:</label><input type="text" 
		              id="newPersonNameFormMiddleName" 
		              name="personName.middleName" 
		              value=""/>
		          
		         <div class="clear">&nbsp;</div>
		          
		         <label for="newPersonNameFormNameFamilyName">Family Name:</label><input type="text" 
		             id="newPersonNameFormFamilyName" 
		             name="personName.familyName" 
		             value=""/>
		          
		         <div class="clear">&nbsp;</div>
		          
		         <label for="newPersonNameFormInitials">Initials:</label><input type="text" 
		              id="newPersonNameFormInitials" 
		              name="personName.initials" 
		              value=""/>
		          
		         <div class="clear">&nbsp;</div>
		          
		         <label for="newPersonNameFormNumeration">Numeration:</label><input type="text" 
		              id="newPersonNameFormNumeration" 
		              name="personName.numeration" 
		              value=""/>
		          
		         <div class="clear">&nbsp;</div>
	             <label for="newPersonNameFormAuthoritative">Authoritative Name</label><input 
	                   id="newPersonNameFormAuthoritative"
	                   type="checkbox" name="authoritative" value="true"/>
	          </form>