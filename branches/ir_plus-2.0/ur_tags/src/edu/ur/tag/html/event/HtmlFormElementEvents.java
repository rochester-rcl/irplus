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


package edu.ur.tag.html.event;

/**
 * Only valid in form elements.
 * 
 * @author Nathan Sarr
 *
 */
public interface HtmlFormElementEvents {
	
	/**
	 * Script to be run when the element changes
	 * 
	 * @return
	 */
	public String getOnChange();
	
	/**
	 * Script to be run when the element changes
	 * 
	 * @param onChange
	 */
	public void setOnChange(String onChange);
	
	/**
	 *Script to be run when the form is submitted
	 * 
	 * @return
	 */
	public String getOnSubmit();
	
	/**
	 * Script to be run when the form is submitted
	 * 
	 * @param onSubmit
	 */
	public void setOnSubmit(String onSubmit);
	
	/**
	 * Script to be run when the form is reset
	 * 
	 * @return
	 */
	public String getOnReset();
	
	/**
	 * Script to be run when the form is reset
	 * 
	 * @param onReset
	 */
	public void setOnReset(String onReset);
	
	/**
	 * Script to be run when the element is selected
	 * 
	 * @return
	 */
	public String getOnSelect();
	
	/**
	 * Script to be run when the element is selected
	 * 
	 * @param onSelect
	 */
	public void setOnSelect(String onSelect);
	
	/**
	 * Script to be run when the element loses focus
	 * 
	 * @return
	 */
	public String getOnBlur();
	
	/**
	 * Script to be run when the element loses focus
	 * 
	 * @param onBulur
	 */
	public void setOnBlur(String onBulur);
	
	/**
	 * @return
	 */
	public String getOnFocus();
	
	/**
	 * @param onFocus
	 */
	public void setOnFocus(String onFocus);
	
	/**
	 * For all non empty attributes, returns
	 * the attributes in the form attribute="value"
	 * 
	 * @return
	 */
	public StringBuffer getAttributes();

}
