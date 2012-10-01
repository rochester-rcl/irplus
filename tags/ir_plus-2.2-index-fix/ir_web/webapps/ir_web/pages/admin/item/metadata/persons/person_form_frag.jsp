<jsp:directive.page contentType="application/javascript; charset=UTF-8"/>

<input type="hidden" id="newPersonForm_id" name="id" value="${personNameAuthority.id}"/>
<input type="hidden" id="newPersonForm_auth_id" name="personNameId" value=""/>
<input type="hidden" id="new_person" name="newPerson" value="true"/>
<input type="hidden" id="name_added" value="${added}"/>
		              
<div id="personError" class="errorMessage">
    
</div>
			          
<table class="formTable">    
    <tr>       
	    <td align="left" class="label">First Name:</td>
		<td align="left" class="input">
		    <input type="text" id="person_first_name" 
				   name="personName.forename" 
				   value="${personName.forename}" size="45"/> 
        </td>
	</tr>
	<tr>
	    <td align="left" class="label">Last Name:*</td>
		<td align="left" class="input">
		    <input type="text" id="person_last_name" 
		        name="personName.surname" 
				value="${personName.surname}" size="45"/> 
		</td>
	</tr>
	<tr>       
	    <td align="left" class="label">Middle Name:</td>
		<td align="left" class="input"><input type="text" 
		    id="person_middle_name" 
			name="personName.middleName" 
			value="${personName.middleName}" size="45"/> 
		</td>
	</tr>
	<tr>
	    <td align="left" class="label">Family Name:</td>
		<td align="left" class="input">
		    <input type="text" 
		        id="person_family_name" 
		 	    name="personName.familyName" 
			    value="${personName.familyName}" size="45"/> 
		</td>
	</tr>
	<tr>       
	    <td align="left" class="label">Initials:</td>
		<td align="left" class="input">
		    <input type="text" 
				id="person_initials" 
				name="personName.initials" 
				value="${personName.initials}" size="45"/> 
		</td>
	</tr>
	<tr>
	    <td align="left" class="label">Numeration:</td>
		<td align="left" class="input">
		    <input type="text" 
			    id="person_numeration" 
				name="personName.numeration" 
				value="${personName.numeration}" size="45"/> </td>
	</tr>
	<tr>       
	    <td align="left" class="label">Birth Year:</td>
		<td align="left" class="label">
		    Year:<input  type="text" 
		         id="person_birthdate_year" 
		         name="birthYear" 
		         size="4" 
		         maxlength ="4"
		         value="${personNameAuthority.birthDate.year}"/> </td>
	</tr>
	<tr>       
	    <td align="left" class="label">Death Year:</td>
		<td align="left" class="label">
		    Year:<input type="text" 
		         id="person_deathdate_year" 
		         name="deathYear" 
		         size="4" 
		         value="${personNameAuthority.deathDate.year}"
		         maxlength ="4"/>
		</td>
	</tr>
</table>
