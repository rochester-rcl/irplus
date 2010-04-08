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


package edu.ur.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.ur.dao.CriteriaHelper;


public class CriteriaBuilder {
	
	
	/**
	 * Adds the filter information to the specified criteria object
	 * 
	 * @param criteria - criteria to add the filter information to
	 * @param filters - filter information
	 * @return - Criteria with filter information added.
	 */
	public void execute(Criteria criteria, List<CriteriaHelper> criterias) {
		if (criterias != null) {
			for (CriteriaHelper criteriaHelper : criterias) {
				if( criteriaHelper.isSort() || criteriaHelper.isFilter())
				{
				    buildCriteria(criteria, criteriaHelper, false);
				}
			}
		}
		
	}
	
	/**
	 * Only execute with filters only
	 * 
	 * @param criteria
	 * @param criterias
	 */
	public void executeWithFiltersOnly(Criteria criteria, List<CriteriaHelper> criterias)
	{
		if (criterias != null) {
			for (CriteriaHelper criteriaHelper : criterias) {
				if( criteriaHelper.isSort() || criteriaHelper.isFilter())
				{
				    buildCriteria(criteria, criteriaHelper, true);
				}
			}
		}
	}

	/**
	 * Build the criteria for the search.
	 * 
	 * @param criteria
	 * @param criteriaHelper
	 * @param filterOnly
	 */
	private void buildCriteria(Criteria criteria,
			CriteriaHelper criteriaHelper, boolean filterOnly) {
		Criteria currentCriteria = criteria;
		Object value = criteriaHelper.getValue();

		String property = criteriaHelper.getProperty();

		// this is for associated values
		// when the field is something like person.getName.getFirstName
		// the path element would be name
		
		List<String> pathElements = criteriaHelper
					.getAssociationPathElements();
		if (pathElements != null && pathElements.size() > 0) {
			for (String s : pathElements) {
				currentCriteria = criteria.createCriteria(s);
			}
		}
		

		if (criteriaHelper.isFilter()) {
			if (value != null) {
				if (value instanceof String) {
					currentCriteria.add(Restrictions.like(property,
							"%" + value + "%").ignoreCase());
				} else if (value instanceof Integer) {
					currentCriteria.add(Restrictions.eq(property, value));
				} else if (value instanceof Long) {
					currentCriteria.add(Restrictions.eq(property, value));
				}
			}
		}

		if (criteriaHelper.isSort() && !filterOnly) {
			String sortType = criteriaHelper.getSortType();
			if (sortType.equals(CriteriaHelper.ASC)) {
				if( criteriaHelper.isIgnoreCaseOnSort())
				{
				    currentCriteria.addOrder(Order.asc(property).ignoreCase());
				}
				else
				{
					currentCriteria.addOrder(Order.asc(property));
				}
			} else if (sortType.equals(CriteriaHelper.DESC)) {
				if( criteriaHelper.isIgnoreCaseOnSort())
				{
				    currentCriteria.addOrder(Order.desc(property).ignoreCase());
				}
				else
				{
					currentCriteria.addOrder(Order.desc(property));
				}
			}
		}
	}


}
