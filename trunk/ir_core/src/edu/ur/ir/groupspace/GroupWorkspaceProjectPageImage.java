/**  
   Copyright 2008-2012 University of Rochester

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


package edu.ur.ir.groupspace;

import edu.ur.ir.file.IrFile;
import edu.ur.order.Orderable;
import edu.ur.persistent.BasePersistent;

/**
 * A picture for the group project page
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceProjectPageImage extends BasePersistent implements Orderable{
	
	// eclipse generated id
	private static final long serialVersionUID = -3233523513827689568L;
	
	// determines the order of the picture 
	private int imageOrder;
	
	// the group project page this picture belongs to
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;
	
	// image file
	private IrFile imageFile;
	
	/**
	 * Default constructor
	 */
	GroupWorkspaceProjectPageImage(){}
	
	/**
	 * Constructor 
	 * 
	 * @param groupWorkspaceProjectPage - group workspace project page
	 * @param imageFile image file
	 */
	GroupWorkspaceProjectPageImage(GroupWorkspaceProjectPage groupWorkspaceProjectPage, IrFile imageFile )
	{
		this.setGroupWorkspaceProjectPage(groupWorkspaceProjectPage);
		this.setImageFile(imageFile);
	}

	/**
	 * Get the group project page.
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPage getGroupWorkspaceProjectPage() {
		return groupWorkspaceProjectPage;
	}

	/**
	 * Set the group project page.
	 * 
	 * @param groupWorkspaceProjectPage
	 */
	void setGroupWorkspaceProjectPage(GroupWorkspaceProjectPage groupWorkspaceProjectPage) {
		this.groupWorkspaceProjectPage = groupWorkspaceProjectPage;
	}

	/**
	 * Get the order value for this image.
	 * 
	 * @see edu.ur.order.Orderable#getOrder()
	 */
	public int getOrder() {
		return imageOrder;
	}
	
	
	/**
	 * Set the order.
	 * 
	 * @param order
	 */
	public void setOrder(int order)
	{
		this.imageOrder = order;
	}

	/**
	 * Set the order for this image.
	 * 
	 * @param order
	 */
	public void setImageOrder(int imageOrder) {
		this.imageOrder = imageOrder;
	}
	
	/**
	 * Get the image order value.
	 * 
	 * @return
	 */
	public int getImageOrder()
	{
		return imageOrder;
	}
	
	/**
	 * Get the image file.
	 * 
	 * @return
	 */
	public IrFile getImageFile() {
		return imageFile;
	}

	/**
	 * Set the image file.
	 * 
	 * @param imageFile
	 */
	void setImageFile(IrFile imageFile) {
		this.imageFile = imageFile;
	}

}
