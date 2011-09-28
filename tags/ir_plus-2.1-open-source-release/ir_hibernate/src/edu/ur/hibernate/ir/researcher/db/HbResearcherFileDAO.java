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

package edu.ur.hibernate.ir.researcher.db;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFileDAO;



/**
 * Hibernate implementation of researcher file data access and storage.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbResearcherFileDAO implements ResearcherFileDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = 5004513542989394032L;

	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<ResearcherFile> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbResearcherFileDAO() {
		hbCrudDAO = new HbCrudDAO<ResearcherFile>(ResearcherFile.class);
	}
	
	/**
	 * Set the session factory.  
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
    {
        hbCrudDAO.setSessionFactory(sessionFactory);
    }
	
	/**
	 * Get a count of the IrFiles
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("irFileCount"));
	}
	
	/**
	 * Return all ResearcherFile
	 */
	public List<ResearcherFile> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Return ResearcherFile by id
	 */
	public ResearcherFile getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ResearcherFile entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ResearcherFile entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get the files for researcher id and ir file id .
	 * 
	 * @param researcherId
	 * @param irFileId
	 * 
	 * @return the found files
	 */
	public ResearcherFile getFileForResearcherWithSpecifiedIrFile(Long researcherId, Long irFileId) {
		Object[] values = {researcherId, irFileId};
		return (ResearcherFile)
		(HbHelper.getUnique(hbCrudDAO.getHibernateTemplate()
			.findByNamedQuery("getFileWithSpecifiedResearcherIdAndIrFileID", values)));
		
	}

	/**
	 * Get the files with specified ir file id .
	 * 
	 * @param irFileId
	 * 
	 * @return the found files
	 */
	public Long getFileWithSpecifiedIrFile(Long irFileId) {
		
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate()
			.findByNamedQuery("getResearcherFilesWithIrFileId", irFileId));
		
	}
	
	/**
	 * Get the files for researcher id and folder id .
	 * 
	 * @param researcherId
	 * @param folderId
	 * 
	 * @return the found files
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherFile> getFilesInAFolderForResearcher(Long researcherId, Long folderId) {
		Object[] values = {researcherId, folderId};
		return (List<ResearcherFile>) (hbCrudDAO.getHibernateTemplate().findByNamedQuery("getFilesInAFolderForResearcher", values));
	}
	
	/**
	 * Get the root files 
	 * 
	 * @param researcherId
	 * 
	 * @return the found files
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherFile> getRootFiles(Long researcherId) {
		
		return (List<ResearcherFile>) (hbCrudDAO.getHibernateTemplate().
				findByNamedQuery("getRootResearcherFiles", researcherId));

	}
	
	/**
	 * Find the specified files for the given researcher.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFileDAO#getFiles(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherFile> getFiles(final Long researcherId, final List<Long> fileIds) {
		List<ResearcherFile> foundFiles = new LinkedList<ResearcherFile>();
		if( fileIds.size() > 0 )
		{
		   foundFiles = 
			    (List<ResearcherFile>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
                public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                    Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                    criteria.createCriteria("researcher").add(Restrictions.idEq(researcherId));
                    criteria.add(Restrictions.in("id", fileIds));
                    return criteria.list();
                }
            });
		}
		return foundFiles;
	}

	/**
	 * Get a count of the researcher files containing this irFile
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFileDAO#getResearcherFileCount(Long)
	 */
	public Long getResearcherFileCount(Long irFileId) {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getResearcherFileCount", irFileId));
	}

}
