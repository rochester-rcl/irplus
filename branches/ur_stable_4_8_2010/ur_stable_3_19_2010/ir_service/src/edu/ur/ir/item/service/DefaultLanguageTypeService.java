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


package edu.ur.ir.item.service;

import java.util.List;

import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeDAO;
import edu.ur.ir.item.LanguageTypeService;

/**
 * Language type service class
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultLanguageTypeService implements LanguageTypeService{
	
	/** Language type data access. */
	private LanguageTypeDAO languageTypeDAO;


	public LanguageType get(String name) {
		return languageTypeDAO.findByUniqueName(name);
	}

	public LanguageType get(Long id, boolean lock) {
		return languageTypeDAO.getById(id, lock);
	}

	/**
	 * Get language types order by name
	 * 
	 * @see edu.ur.ir.item.LanguageTypeService#getLanguageTypesOrderByName(int, int, String)
	 */
	public List<LanguageType> getLanguageTypesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return languageTypeDAO.getLanguageTypesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	}

	public Long getLanguageTypesCount() {
		return languageTypeDAO.getCount();
	}

	public void save(LanguageType languageType) {
		languageTypeDAO.makePersistent(languageType);
	}

	public LanguageTypeDAO getLanguageTypeDAO() {
		return languageTypeDAO;
	}

	public void setLanguageTypeDAO(LanguageTypeDAO languageTypeDAO) {
		this.languageTypeDAO = languageTypeDAO;
	}

	@SuppressWarnings("unchecked")
	public List<LanguageType> getAll() {
		return languageTypeDAO.getAll();
	}

	public void delete(LanguageType languageType) {
		languageTypeDAO.makeTransient(languageType);
	}

	
	public LanguageType getByUniqueSystemCode(String uniqueSystemCode) {
		return languageTypeDAO.getByUniqueSystemCode(uniqueSystemCode);
	}
}
