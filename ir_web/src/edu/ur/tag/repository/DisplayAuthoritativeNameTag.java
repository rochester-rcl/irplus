/**  
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
*/  


package edu.ur.tag.repository;

import java.io.IOException;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.user.IrUser;
import edu.ur.tag.TagUtil;

/**
 * To display the names in indented manner for adding authoritative name to user
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DisplayAuthoritativeNameTag extends SimpleTagSupport{
	
	/** Name authority to display */
	private PersonNameAuthority personNameAuthority;
	
	/** User being edited */
	private IrUser user;
	
	/** Add image */
	private String addImg = "page-resources/images/all-images/add.gif";
	
	/**
	 * Displays the authoritative name and the other names indented to it
	 */
	public void doTag() throws JspException, IOException {

		
		
		PageContext context = (PageContext) this.getJspContext();
		JspWriter out = context.getOut();

		Set<PersonName> names = personNameAuthority.getNames();
		Long authoritativeId = personNameAuthority.getAuthoritativeName().getId();

		out.write("<tr>");

		if (names.size() == 1) {
			out.write("<td class=\"tdItemFolderLeftBorder\">");
		} else {
			out.write("<td class=\"tdItemContributorLeftBorder\">");
		}
		
		if (user.getPersonNameAuthority() != null && personNameAuthority.getId().equals(user.getPersonNameAuthority().getId())) {
			out.write("&nbsp;&nbsp;Added");
		} else {
			try {
				out.write(" <img class=\"tableImg\" alt=\"\" src=\"");
				out.print(TagUtil.fixRelativePath(addImg, context));
				out.write("\"/> <a href=\"javascript:YAHOO.ur.email.addName('" + user.getId() + "','" + personNameAuthority.getId() + "');\"> Add</a>");
			} catch (Exception e) {
			       throw new JspException(e);
			}				
		}

		out.write("</td>");
		
		if (names.size() == 1) {
			out.write("<td class=\"tdItemFolderRightBorder\">");
		} else {
			out.write("<td class=\"tdItemContributorRightBorder\">");
		}

		out.write(personNameAuthority.getAuthoritativeName().getForename() + " " + personNameAuthority.getAuthoritativeName().getMiddleName()
				+ " " + personNameAuthority.getAuthoritativeName().getSurname());
		out.write("&nbsp;&nbsp;&nbsp;[Authoritative Name]");
		

		out.write("</td>");
		out.write("</tr>");
		
		if (names.size() > 1) {

			PersonName[] nameArray = names.toArray(new PersonName[0]);

			boolean hasAuthoritativeNamePassed = false;
			
			for (int i = 0; i < nameArray.length; i++) {
				
				if (!nameArray[i].getId().equals(authoritativeId)) {
					
					out.write("<tr>");
					if ((i == nameArray.length - 1) || ( !hasAuthoritativeNamePassed && (i == nameArray.length - 2) ) ) {
						out.write("<td class=\"tdItemFolderLeftBorder\">");
					} else {
						out.write("<td class=\"tdItemContributorLeftBorder\">");
					}
					
					out.write("</td>");

					if ((i == nameArray.length - 1) || ( !hasAuthoritativeNamePassed && (i == nameArray.length - 2) ) ) {
						out.write("<td class=\"tdItemFolderRightBorder\">");
					} else {
						out.write("<td class=\"tdItemContributorRightBorder\">");
					}

					out.write(nameArray[i].getForename() + " " + nameArray[i].getMiddleName()
							+ " " + nameArray[i].getSurname());
					out.write("</td> ");
					out.write("</tr>");
				} else {
					hasAuthoritativeNamePassed = true;
				}
				
			}
			
		}
	}

	public PersonNameAuthority getPersonNameAuthority() {
		return personNameAuthority;
	}

	public void setPersonNameAuthority(PersonNameAuthority personNameAuthority) {
		this.personNameAuthority = personNameAuthority;
	}

	public String getAddImg() {
		return addImg;
	}

	public void setAddImg(String addImg) {
		this.addImg = addImg;
	}

	public IrUser getUser() {
		return user;
	}

	public void setUser(IrUser user) {
		this.user = user;
	}
	
}
