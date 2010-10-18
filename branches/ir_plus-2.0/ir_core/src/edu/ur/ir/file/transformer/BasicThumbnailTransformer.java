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

package edu.ur.ir.file.transformer;

/**
 * Interface for creating basic thumbnails.
 * 
 * @author Nathan Sarr
 *
 */
public interface BasicThumbnailTransformer extends FileTransformer{
	
	/**
	 * Direction that the image shold be set when setting
	 * the maximum or minimum.
	 * 
	 * VERTICAL = 0
     * HORIZONTAL = 1
     * 
	 * @param direction
	 */
	public void setDirection(int direction);
	
	/**
	 * Get the direction where the maximim size will be set
	 * on.
	 * 
	 * @return
	 */
	public int getDirection();
	
	/**
	 * Set the maximum size.
	 * 
	 * @param size
	 */
	public void setSize(int size);
	
	/**
	 * Get the maximum size that the thumbnail
	 * can have.
	 * 
	 * @return
	 */
	public int getSize();

}
