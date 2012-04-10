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

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import edu.ur.hibernate.HbCrudDAO;
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
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getFileWithSpecifiedResearcherIdAndIrFileID");
		q.setLong("researcherId", researcherId);
		q.setLong("fileId", irFileId);
		return (ResearcherFile) q.uniqueResult();
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
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getFilesInAFolderForResearcher");
		q.setLong("researcherId", researcherId);
		q.setLong("parentId", folderId);
		return (List<ResearcherFile>) q.list();
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
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getRootResearcherFiles");
		q.setLong("researcherId", researcherId);
		return  (List<ResearcherFile>) q.list();
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
		    Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
		    criteria.createCriteria("researcher").add(Restrictions.idEq(researcherId));
            criteria.add(Restrictions.in("id", fileIds));
            foundFiles = criteria.list();
		}
		return foundFiles;
	}

	/**
	 * Get a count of the researcher files containing this irFile
	 * 
	 * @see edu.ur.ir.researcher.ResearcherFileDAO#getResearcherFileCount(Long)
	 */
	public Long getResearcherFileCount(Long irFileId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getResearcherFileCount");
		q.setLong("fileId", irFileId);
		return (Long)q.uniqueResult();
	}

}
