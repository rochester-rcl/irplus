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

package edu.ur.ir.repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FolderInfo;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.persistent.CommonPersistent;

/**
 * This represents a repository and institutional information.
 * 
 * @author Nathan Sarr
 *
 */
public class Repository extends CommonPersistent {
	
	/** Default repository id  */
	public static final long DEFAULT_REPOSITORY_ID = 1l;
	
	/** Logger */
	private static final Logger log = Logger.getLogger(Repository.class);
	
	/**  Current location where files should be stored  */
	private FileDatabase fileDatabase;
	
	/** The institution that this repository belongs to. */
	private String institutionName;
	
	/** 
	 * Set of pictures for the institution this is to show the pictures
	 * for the institution.  This is just a set of files so it is up to the 
	 * client to determine if the files are appropriate for this set.
	 */
	private Set<IrFile> pictures = new HashSet<IrFile>();
	
	/**
	 * Set of top level collections that belong to this
	 * institutional repository.
	 *  
	 */
	private Set<InstitutionalCollection> institutionalCollections = new HashSet<InstitutionalCollection>();
	
	/**  Generated unique id. */
	private static final long serialVersionUID = 5333030801530312407L;
	
	/** Folder containing the name index for names of people in the system*/ 
	private FolderInfo nameIndexFolder;
	
	/** Folder containing the index for users in the system*/ 
	private FolderInfo userIndexFolder;
	
	/** Folder containing the index for institutional items in the repository */
	private FolderInfo institutionalItemIndexFolder;
	
	/** Folder containing the index for researcher in the system*/ 
	private FolderInfo researcherIndexFolder;
	
 	/**
     * Default constructor
     */
    public Repository(){}
    
    /**
     * Default public constructor.
     * 
     * @param name - name of the repository
     * @param fileDatabase - database to store file in.
     */
    public Repository(String name, FileDatabase fileDatabase)
    {
    	this.name = name;
    	this.fileDatabase = fileDatabase;
    }
    
	/**
	 * get the institutional collections that belong to this repository.
	 * 
	 * @return an unmodifiable set of collections.
	 */
	public Set<InstitutionalCollection> getInstitutionalCollections() {
		return Collections.unmodifiableSet(new TreeSet<InstitutionalCollection>(institutionalCollections));
	}
	
	/**
	 * Add the ir collection as a collection that is part of this
	 * repository.  Adds the collection as a root collection
	 * in the repository.  If this collection is part of a different
	 * repository, it is removed from it's existing repository.
	 * 
	 * If it exists as a child in a different collection, it is
	 * removed from the existing collection.
	 * 
	 * @param irCollection
	 * @throws DuplicateNameException 
	 */
	public void addInstitutionalCollection(InstitutionalCollection institutionalCollection) throws DuplicateNameException 
	{
		log.debug("adding colleciton " + institutionalCollection);
		
		if( getInstitutionalCollection(institutionalCollection.getName()) != null)
		{
			throw new DuplicateNameException("This repository already has a collection" +
					"with the specified name " + institutionalCollection.getName(), 
					institutionalCollection.getName());
		}
		
		Repository current = institutionalCollection.getRepository();
		if(current != null && !current.equals(this))
		{
			current.removeInstitutionalCollection(institutionalCollection);
		}

		if( institutionalCollection.getParent() != null )
		{
			institutionalCollection.getParent().removeChild(institutionalCollection);	
		}
		
		institutionalCollection.setRepository(this);
		institutionalCollections.add(institutionalCollection);
	}
	
	/**
	 * Create a top level collection in this repository.
	 * 
	 * @param name to give the repository.
	 * @throws DuplicateNameException 
	 * 
	 * @throws IllegalStateExcpetion if the repositories institution has a 
	 * null name or the institution is null.
	 */
	public InstitutionalCollection createInstitutionalCollection(String name) throws DuplicateNameException
	{
		InstitutionalCollection irCollection = new InstitutionalCollection(this, name);
		irCollection.setName(name);
		addInstitutionalCollection(irCollection);
		institutionalCollections.add(irCollection);
		return irCollection;
	}
	
	/**
	 * Remove a collection from the repository.
	 * 
	 * @param irCollection collection to remove
	 * 
	 * @return true if the collection is removed.
	 */
	public boolean removeInstitutionalCollection(InstitutionalCollection irCollection)
	{
	    return institutionalCollections.remove(irCollection);	
	}
	
	/**
	 * Find a collection based on the name.  If
	 * no collection is found a null object is returned.
	 * 
	 * @param name of the collection.
	 * @return the found collection.
	 */
	public InstitutionalCollection getInstitutionalCollection(String name)
	{
		for( InstitutionalCollection c : institutionalCollections)
		{
			if( c.getName().equalsIgnoreCase(name))
			{
				return c;
		    }
		}
	
		return null;
	}
	
	/**
	 * Get the name of the institution.
	 * 
	 * @return institution name
	 */
	public String getInstitutionName() {
		return institutionName;
	}

	/**
	 * Set the institution name.
	 * 
	 * @param institutionName
	 */
	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	/**
	 * Set the collections that belong to this institutional
	 * repository.
	 * 
	 * @param collections
	 */
	void setInstitutionalCollections(Set<InstitutionalCollection> irCollections) {
		this.institutionalCollections = irCollections;
	}
	
	/**
	 * Hash code for the repository
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		return value;
	}
	
	/**
	 * Get the default file database
	 * 
	 * @return
	 */
	public FileDatabase getFileDatabase() {
		return fileDatabase;
	}

	/**
	 * Set the default file database storage
	 * 
	 * @param defaultFileDatabase
	 */
	void setFileDatabase(FileDatabase fileDatabase) {
		this.fileDatabase = fileDatabase;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Repository)) return false;

		final Repository other = (Repository) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		return true;
	}
	
	/**
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Repository name = ");
		sb.append(name);
		sb.append(" id = ");
		sb.append(id);
		sb.append(" description = ");
		sb.append(description);
		sb.append(" institutionName = ");
		sb.append(institutionName);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Returns an unmodifiable set of pictures for the institution.
	 * 
	 * @return 
	 */
	public Set<IrFile> getPictures() {
		return Collections.unmodifiableSet(pictures);
	}
	
	/**
	 * Add a new picture to the set.
	 * 
	 * @param file
	 */
	public void addPicture(IrFile file)
	{
		pictures.add(file);
	}
	
	/**
	 * Remove a picture from the set.
	 * 
	 * @param file
	 */
	public boolean removePicture(IrFile file)
	{
		return pictures.remove(file);
	}
	
	/**
	 * Remove the institutional picture for the specified file.
	 * 
	 * @param id - unique id of irFile
	 * @return true if the picture is removed.
	 */
	public boolean removePicture(Long id)
	{
		IrFile f = getPicture(id);
		if( f != null)
		{
			return removePicture(f);
		}
		return false;
	}
	
	/**
	 * Remove the institutional picture for the specified file.
	 * 
	 * @param id - unique name of irFile
	 * @return true if the picture is removed.
	 */
	public boolean removePicture(String name)
	{
		IrFile f = getPicture(name);
		if( f != null)
		{
			return removePicture(f);
		}
		return false;
	}
	
	/**
	 * Get an institutional picture by id.
	 * 
	 * @param id - unique id of the institutional picture
	 * @return the IrFile if it is found otherwise null.
	 */
	public IrFile getPicture(Long id)
	{
		for( IrFile f : pictures)
		{
			if( id.equals(f.getId()))
			{
				return f;
			}
		}
		return null;
	}
	
	/**
	 * Get the institutional picture by it's unique name.
	 * 
	 * @param uniqueName - unique name of the ir file.
	 * @return the IrFile if it is found otherwise null.
	 */
	public IrFile getPicture(String uniqueName)
	{
		for( IrFile f : pictures)
		{
			if( uniqueName.equals(f.getName()))
			{
				return f;
			}
		}
		return null;
	}

	/**
	 * Set of pictures for the institution.
	 * 
	 * @param institutionalPictures
	 */
	void setPictures(Set<IrFile> pictures) {
		this.pictures = pictures;
	}

	/**
	 * Get the folder containing the name indexes
	 * 
	 * @return
	 */
	public FolderInfo getNameIndexFolder() {
		return nameIndexFolder;
	}

	/**
	 * Set the folder containing the person name index information
	 * @param nameIndexFolder
	 */
	public void setNameIndexFolder(FolderInfo nameIndexFolder) {
		this.nameIndexFolder = nameIndexFolder;
	}

	/**
	 * Get the user index folder
	 * 
	 * @return
	 */
	public FolderInfo getUserIndexFolder() {
		return userIndexFolder;
	}

	/**
	 * Set the folder containing the user index
	 * 
	 * @param userIndexFolder
	 */
	public void setUserIndexFolder(FolderInfo userIndexFolder) {
		this.userIndexFolder = userIndexFolder;
	}

	/**
	 * Get the institutional item index folder
	 * 
	 * @return
	 */
	public FolderInfo getInstitutionalItemIndexFolder() {
		return institutionalItemIndexFolder;
	}

	/**
	 * Set the institutional item index folder.
	 * 
	 * @param institutionalItemIndexFolder
	 */
	public void setInstitutionalItemIndexFolder(
			FolderInfo institutionalItemIndexFolder) {
		this.institutionalItemIndexFolder = institutionalItemIndexFolder;
	}

	public void setResearcherIndexFolder(FolderInfo researcherIndexFolder) {
		this.researcherIndexFolder = researcherIndexFolder;
	}

	public FolderInfo getResearcherIndexFolder() {
		return researcherIndexFolder;
	}
}
