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
import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ur.ir.item.ItemFile;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.security.SecurityService;


/**
 * Tag to get group entries for Item file
 * 
 * @author Sharmila Ranganathan
 */
public class FileGroupEntriesTag extends SimpleTagSupport {
	
	/**  Eclipse generated serial id  */
	private static final long serialVersionUID = 5620239685924163067L;

	/** Log for IrAclTag */
	private static final Logger log = LogManager.getLogger(FileGroupEntriesTag.class);

	/** domain object */
    private ItemFile itemFile;
    
	/** Security service  */
	private static SecurityService mySecurityService = null;
    
    /**
     * to get group entries for Item file
     */
    public void doTag() throws JspException {
    	log.debug("do start tag for ACL");
     
		JspFragment body = getJspBody();
		PageContext pageContext = (PageContext) getJspContext();
		
		IrAcl acl = mySecurityService.getAcl(itemFile);
		Set<IrUserGroupAccessControlEntry> fileEntries = new HashSet<IrUserGroupAccessControlEntry>();
		
		if( acl != null )
		{
			fileEntries = acl.getGroupEntries();
		}
		
		pageContext.setAttribute("fileEntries", fileEntries);

		try {
			if( body != null )
			{
			    body.invoke(null);
			}

		} catch (IOException e) {
			throw new JspException(e);
		}
    }
	
    /**
	 * Security service for dealing with secure actions.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		mySecurityService = securityService;
	}

	public ItemFile getItemFile() {
		return itemFile;
	}

	public void setItemFile(ItemFile itemFile) {
		this.itemFile = itemFile;
	}
}
