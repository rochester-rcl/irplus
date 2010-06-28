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

package edu.ur.ir.user;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.repository.LicenseVersion;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.security.Sid;
import edu.ur.persistent.BasePersistent;
import edu.ur.security.PersistentUser;

/**
 * User in the system.  This holds all of the user data.  
 * 
 * @author Nathan Sarr
 *
 */
public class IrUser extends BasePersistent implements PersistentUser, UserDetails, Sid{
	
	/** Indicates this is a user type for security  */
	public static final String USER_SID_TYPE = "USER_SID_TYPE";
		
	/**  Roles this user currently has */
	private Set<IrRole> roles = new HashSet<IrRole>();
	
	/**  Generated Version Id. */
	private static final long serialVersionUID = 7518030300931490124L;	
	
	/**  Password used by the user.  */
	private String password;
	
	/**  Last name of the user  .*/
	private String lastName;
	
	/** lower case value of the last name */
	private String lowerCaseLastName;
	
	/**  First name of the user .*/
	private String firstName;
	
	/** lower case of the first name */
	private String lowerCaseFirstName;
	
	/** Middle name of the user */
	private String middleName;
	
	/** lower case value of the middle name */
	private String lowerCaseMiddleName;
	
	/**  User name used by the user.*/
	private String username;
	
	/**  Encoding used for the password */
	private String passwordEncoding;
	
	/**  Token given to user when user forgot the password  */
	private String passwordToken;

	/**  Email Used by user.  */
	private UserEmail defaultEmail;
	
	/**  Email Used by user.  */
	private Set<UserEmail> emails = new HashSet<UserEmail>();

	/**  Indicates if the account is expired */
	private boolean accountExpired = false;
	
	/**  indicates if the account is locked */
	private boolean accountLocked = false;
	
	/**  Indicates if the credentials have been expired  */
	private boolean credentialsExpired = false;
	
	/**  Indicates if the password has to be changed  */
	private boolean passwordChangeRequired = false;

	/**  Date the account was created.  */
	private Timestamp createdDate;
	
	/**  Date the account expires. */
	private Date expirationDate;

	/**  Phone number of the user. */
	private String phoneNumber;
	
	/**  Root Folder for this person. */
	private Set<PersonalFolder> rootFolders = new HashSet<PersonalFolder>();
	
	/**  Root files for this person.  */
	private Set<PersonalFile> rootFiles = new HashSet<PersonalFile>();
	
	/** Root personal collections for this user  */
	private Set<PersonalCollection> rootPersonalCollections = new HashSet<PersonalCollection>();

	/** Root personal collections for this user  */
	private Set<PersonalItem> rootPersonalItems = new HashSet<PersonalItem>();
	
	/**Personal folder for indexing personal content */
	private String personalIndexFolder;

	/**  Affiliation for the user  */
	private Affiliation affiliation; 
	
	/** Departments of the user */
	private Set<Department> departments = new HashSet<Department>();
	
	/**  Indicates whether the affiliation is approved */
	private boolean isAffiliationApproved;
	
	/** Holds the set of files until they can be moved into the users own files 
	 *  and folders
	 */
	private Set<SharedInboxFile> sharedInboxFiles = new HashSet<SharedInboxFile>();
	
	/** Name for the user */
	private PersonNameAuthority personNameAuthority;
	
	/** Researcher */
	private Researcher researcher;
	
	/**  Indicates the user registered themselves  */
	private boolean selfRegistered = false;
	
	/** Set of licenses accepted by the user */
	private Set<UserRepositoryLicense> acceptedLicenses = new HashSet<UserRepositoryLicense>();
	
	/** indicates that the users index needs to be rebuilt by either overwriting the current index
	 * or deleting the users old index and rebuilding  */
	private boolean reBuildUserWorkspaceIndex = false;
	
	/** represents an external account that a user can be authenticated against */
	private ExternalUserAccount externalAccount;
	
	
	/**
	 * Default Constructor. 
	 */
	public IrUser(){
		this.createdDate = new Timestamp( new Date().getTime());
	}
	
	/**
	 * Default constructor
	 * 
	 * @param username for the user
	 * @param password password for the user.  It must be 
	 * encrypted prior to passing it to the constructor.
	 */
	public IrUser(String username, String password)
	{
		this();
		setPassword(password);
		setUsername(username);
	}
	
	/**
	 * Get the name based on id.
	 * 
	 * @param id
	 * @return
	 */
	public UserEmail getUserEmail(Long id)
	{
		UserEmail myEmail = null;
		for( UserEmail email : emails)
		{
			if( email.getId().equals(id))
			{
				myEmail = email;
			}
		}
		return myEmail;
	}
	
	/**
	 * Get the user email based on email.
	 * 
	 * @param email
	 * @return
	 */
	public UserEmail getUserEmail(String email)
	{
		UserEmail myEmail = null;
		for( UserEmail userEmail : emails)
		{
			if( userEmail.getEmail().equals(email))
			{
				myEmail = userEmail;
				break;
			}
		}
		return myEmail;
	}


	/**
	 * Unmodifiable Set of emails of the user.
	 * 
	 * @return
	 */
	public Set<UserEmail> getUserEmails() {
		return Collections.unmodifiableSet(emails);
	}

	/**
	 * Set the emails used by the user.
	 * 
	 * @param names
	 */
	public void setUserEmails(Set<UserEmail> userEmails) {
		this.emails = userEmails;
	}

	/**
	 * Add user email
	 * 
	 * @param userEmail
	 */
	public void addUserEmail(UserEmail userEmail, boolean isDefaultEmail)
	{
		if( emails.contains(userEmail))
		{
			return;
		}
		
		if( ((userEmail.getIrUser()) != null) && (!userEmail.getIrUser().equals(this))) 
		{
			userEmail.getIrUser().removeUserEmail(userEmail);
		}
		
		userEmail.setIrUser(this);
		emails.add(userEmail);
		
		if( isDefaultEmail)
		{
		    setDefaultEmail(userEmail);
		}
		
	}
	
	/**
	 * Set the email with the specified id as the default email. If
	 * a email with the specified id is not found, no change is made.
	 *  
	 * @param id
	 * @return true if the default email has been changed
	 */
	public boolean changeDefaultEmail(Long id)
	{
		for(UserEmail email: emails)
		{
			if(email.getId().equals(id))
			{
				setDefaultEmail(email);
				return true;
			}
		}
		return false;
	}

	/**
	 * Change the default email to the specified email.
	 * 
	 * @param email to set the email to.
	 * @return
	 */
	public void changeDefaultEmail(UserEmail email)
	{
		if(!emails.contains(email))
		{
			addUserEmail(email, true);
		}
		else
		{
		    setDefaultEmail(email);
		}
	}

	/**
	 * Remove the email from the set.
	 * 
	 * @param userEmail
	 * @return true if the user email is removed.
	 */
	public boolean removeUserEmail(UserEmail userEmail)
	{
		if(userEmail.equals(this.defaultEmail))
		{
			defaultEmail = null;
		}
		
		if( emails.contains(userEmail))
		{
			userEmail.setIrUser(null);
		}
		return emails.remove(userEmail);
	}

	/**
	 * Default email for the user
	 * 
	 * @return
	 */
	public UserEmail getDefaultEmail() {
		return defaultEmail;
	}

	/**
	 * Set the default email for the user.
	 * 
	 * @param email
	 */
	public void setDefaultEmail(UserEmail email) {
		this.defaultEmail = email;
	}
	
	/**
	 * Get the password for the user.
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the password for the user.  This 
	 * does not encyrpt the password.  The 
	 * password must be encrypted prior to setting
	 * it on the person.
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns true if the account has expired.
	 * 
	 * @return
	 */
	public boolean getAccountExpired() {
		return accountExpired;
	}

	/**
	 * Set the account as expired.
	 * 
	 * @param accountExpired
	 */
	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	/**
	 * Returns true if the account is locked.
	 * 
	 * @return
	 */
	public boolean getAccountLocked() {
		return accountLocked;
	}

	/**
	 * Set the account as locked.
	 * 
	 * @param accountLocked
	 */
	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	/**
	 * Determine if the credentials have expired.
	 * 
	 * @return
	 */
	public boolean getCredentialsExpired() {
		return credentialsExpired;
	}

	/**
	 * Set the credentials as expired.
	 * 
	 * @param credentialsExpired
	 */
	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	/**
	 * Return true if the account is enabled.
	 * 
	 * @return
	 */
	public boolean getEnabled() {
		return !getAccountExpired() && !getAccountLocked() && !getCredentialsExpired();
	}

	/**
	 * Date the user was created.
	 * 
	 * @return
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Set the date the user was created.
	 * 
	 * @param createdDate
	 */
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Get the date the user account expired.
	 * 
	 * @return
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Set the expiration date of the account.
	 * 
	 * @param expirationDate
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += username == null ? 0 : username.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof IrUser)) return false;

		final IrUser other = (IrUser) o;

		if( ( username != null && !username.equals(other.getUsername()) ) ||
			( username == null && other.getUsername() != null ) ) return false;

		return true;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[User id = ");
		sb.append(getId());
		sb.append(" User name = ");
		sb.append(getUsername());
		sb.append(" First name = ");
		sb.append(getFirstName());
		sb.append(" Last name = ");
		sb.append(getLastName());
		sb.append(" account expired = ");
		sb.append(accountExpired);
		sb.append(" account locked = ");
		sb.append(accountLocked);
		sb.append("credentials expired = ");
		sb.append(credentialsExpired);
		sb.append("]");
		
		return  sb.toString();
	}

	/**
	 * Get the granted authorities for this user.
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#getAuthorities()
	 */
	public Collection<GrantedAuthority> getAuthorities() {
		return new LinkedList<GrantedAuthority>(getRoles());
	}

	/**
	 * Return true if the account is not expired.
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#isAccountNonExpired()
	 */
	public boolean isAccountNonExpired() {
		return !getAccountExpired();
	}

	/**
	 * Return true if the account is not locked.
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#isAccountNonLocked()
	 */
	public boolean isAccountNonLocked() {
		return !getAccountLocked();
	}

	/**
	 * Return true if the credentials have not expried.
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	public boolean isCredentialsNonExpired() {
		return !getCredentialsExpired();
	}

	/**
	 * Return true if the account is enabled.
	 * 
	 * @see org.acegisecurity.userdetails.UserDetails#isEnabled()
	 */
	public boolean isEnabled() {
		return getEnabled();
	}

	/**
	 * Get the roles this user currently has.
	 * 
	 * @return an unmodifiable set of roles
	 */
	public Set<IrRole> getRoles() {
		return Collections.unmodifiableSet(roles);
	}
	
	/**
	 * Get a role by name for this user
	 * 
	 * @param role
	 * @return the role or null if no role found.
	 */
	public IrRole getRole(String roleName)
	{
		for(IrRole myRole : roles)
		{
			if(myRole.getName().equals(roleName))
			{
				return myRole;
			}
		}
		return null;
	}
	
	/**
	 * Get a role for this user by id.
	 * 
	 * @param id - unique id of the role
	 * @return the role if found otherwise null.
	 */
	public IrRole getRole(Long id)
	{
		for(IrRole role : roles)
		{
			if( role.getId().equals(id))
			{
				return role;
			}
		}
		return null;
	}
	
	/**
	 * Remove the role from this user with the specified role.
	 * 
	 * @param name - name of the role to remove
	 * @return true if the role is removed
	 */
	public boolean removeRole(String name)
	{
		IrRole role = getRole(name);
		return roles.remove(role);
	}
	
	/**
	 * Add a role to this user.
	 * 
	 * @param irRole
	 */
	public void addRole(IrRole irRole)
	{
		roles.add(irRole);
	}
	
	/**
	 * Remove a role from this user.
	 * 
	 * @param irRole
	 * @return true if the role is removed.
	 */
	public boolean removeRole(IrRole irRole)
	{
		return roles.remove(irRole);
	}
	
	/**
	 * Determine if the user has the specified role.
	 * 
	 * @param irRole
	 * @return
	 */
	public boolean hasRole(IrRole irRole)
	{
		return roles.contains(irRole);
	}
	
	/**
	 * Determine if the user has the specified role.
	 * 
	 * @param name - of the role
	 * @return true if the user has the role.
	 */
	public boolean hasRole(String name)
	{
		return getRole(name) != null;
	}
	
	/**
	 * Returns true if the user has the role.
	 * 
	 * @param id - unique id of the role
	 * @return true if the user has the specified role.
	 */
	public boolean hasRole(Long id)
	{
		return getRole(id) != null;
	}
	
	/**
	 * Remove a folder from this persons set of root personal folders.
	 * 
	 * @param folder
	 * @return true if the folder is removed.
	 * 
	 */
	public boolean removeRootFolder(PersonalFolder folder)
	{
		return rootFolders.remove(folder);
	}
	
	/**
	 * Remove the personal file from this user.
	 * 
	 * @param personlFile
	 * @return true if the file is removed.
	 */
	public boolean removeRootFile(PersonalFile personalFile)
	{
		return rootFiles.remove(personalFile);
	}

	/**
	 * Set the roles this user currently has.
	 * 
	 * @param roles
	 */
	void setRoles(Set<IrRole> roles) {
		this.roles = roles;
	}
	
	/**
	 * @see edu.ur.security.User#getUsername()
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Set the suer name.
	 * 
	 * @param username
	 */
	public void setUsername(String username)
	{
		if( username != null )
		{
			this.username = username.toLowerCase();
		}
		else
		{
			this.username = null;
		}
		
		
	}

	/**
	 * Encoding used for the password
	 * 
	 * @return
	 */
	public String getPasswordEncoding() {
		return passwordEncoding;
	}

	/**
	 * Encoding used for the password.
	 * 
	 * @param passwordEncoding
	 */
	public void setPasswordEncoding(String passwordEncoding) {
		this.passwordEncoding = passwordEncoding;
	}
		
	/**
	 * Creates the root folder by name if it does not exist.  The 
	 * name should not be null or the name of an existing root
	 * folder for this user.
	 * 
	 * @param name of root folder to create.
	 * @return Created Folder if it does not already exist
	 * @throws DuplicateNameException 
	 * 
	 * @throws IllegalArgumentException if the name of the folder 
	 * already exists or the name is null.
	 */
	public PersonalFolder createRootFolder(String name) throws DuplicateNameException, IllegalFileSystemNameException
	{
		if( name == null)
		{
			throw new IllegalArgumentException("Name cannot be null");
		}
		
		if( !isVaildPersonalFileSystemName(name))
		{
			throw new DuplicateNameException("A file or folder with name " + name +
			" already exists ", name );
        }
		
		PersonalFolder f = new PersonalFolder(this, name);
		rootFolders.add(f);
		return f;
	}
	
	/**
	 * Adds an existing folder to the root of this person.
	 * If the folder was a child of an existing folder.  It
	 * is removed from its parents list.
	 * 
	 * @param folder - to add as a root
	 * @throws DuplicateNameException 
	 */
	public void addRootFolder(PersonalFolder folder) throws DuplicateNameException
	{
		if( !isVaildPersonalFileSystemName(folder.getName()))
        {
        	throw new DuplicateNameException("A file or folder with name " + folder.getName() +
        			" already exists in this folder", folder.getName());
        }
		
		PersonalFolder parent = folder.getParent();
		if(parent != null)
		{
		    parent.removeChild(folder);
		}
		rootFolders.add(folder);
	}

	/**
	 * Adds an existing collection to the root of this person.
	 * If the collection was a child of an existing collection .  It
	 * is removed from its parents list.
	 * 
	 * @param collection - to add as a root
	 */
	public void addRootCollection(PersonalCollection collection) throws DuplicateNameException
	{
		if(getRootPersonalCollection(collection.getName()) != null)
		{
			throw new DuplicateNameException("Collection with name " + collection.getName() +
			" already exists in this collection", collection.getName() );
	    }
		
		PersonalCollection parent = collection.getParent();
		
		if( parent != null )
		{
		    parent.removeChild(collection);
		}
		
		collection.setOwner(this);
		rootPersonalCollections.add(collection);
	}

	/**
	 * Set of root folders for this person.
	 * 
	 * @return Unmodifiable set of root folders
	 */
	public Set<PersonalFolder> getRootFolders() {
		return Collections.unmodifiableSet(rootFolders);
	}

	/**
	 * The set of root folders owned by this user.
	 * 
	 * @param rootFolders
	 */
	void setRootFolders(Set<PersonalFolder> rootFolders) {
		this.rootFolders = rootFolders;
	}
	
	/**
	 * Get a personal root folder by name.  The comparison
	 * is case insensitive.
	 * 
	 * @param name - name of the folder to return
	 * @return The folder if found otherwise null.
	 */
	public PersonalFolder getRootFolder(String name)
	{
		for(PersonalFolder f: rootFolders )
		{
			if( f.getName().equalsIgnoreCase(name))
			{
				return f;
			}
		}
		return null;
	}
	
		
	/**
	 * Get a personal versioned file by name.
	 * 
	 * @param name
	 * @return the found file
	 */
	public PersonalFile getRootFile(String nameWithExtension)
	{
		for(PersonalFile pvf: rootFiles )
		{
			if( pvf.getVersionedFile().getNameWithExtension().equalsIgnoreCase(nameWithExtension))
			{
				return pvf;
			}
		}
		return null;
	}
	
	/**
	 * Adds an existing file to the root of this person.
	 * If the file was a child of an existing folder.  It
	 * is removed from the old folders file list.
	 * 
	 * @param file - to add as a root file
	 */
	public void addRootFile(PersonalFile file) throws DuplicateNameException
	{
		if( !isVaildPersonalFileSystemName(file.getVersionedFile().getNameWithExtension()))
        {
        	throw new DuplicateNameException("A file or folder with name " + file.getName() +
        			" already exists in this folder", file.getName());
        }
		
		PersonalFolder folder = file.getPersonalFolder();
		if( folder != null )
		{
			folder.removePersonalFile(file);
			file.setPersonalFolder(null);
			
			IrUser current = folder.getOwner();
			if(current != null && !current.equals(this))
			{
				current.removeRootFile(file);
			}
		}
		
		rootFiles.add(file);
		
		SharedInboxFile inboxFile = this.getSharedInboxFile(file.getVersionedFile());
        if ( inboxFile != null )
        {
        	this.removeFromSharedFileInbox(inboxFile);
        }
	}
	
	/**
	 * Adds an existing item to the root of this person.
	 * If the item was a child of an existing collection.  It
	 * is removed from the old collection file list.
	 * 
	 * @param item - to add as a root item
	 */
	public void addRootItem(PersonalItem item) 
	{
	
		PersonalCollection collection = item.getPersonalCollection();
		if( collection != null )
		{
			collection.removePersonalItem(item);
			item.setPersonalCollection(null);
		}
		
		rootPersonalItems.add(item);
	}
	
	/**
	 * Create a root personal file for this user.  If the file exists as an 
	 * inbox file, the inbox file is removed.
	 * 
	 * @param vf the versioned file to add to the root.	
	 * @return the created personal file
	 * 
	 * @throws DuplicateNameException - if the file name already exists as a root file
	 */
	public PersonalFile createRootFile(VersionedFile vf)throws DuplicateNameException
	{
        if( !isVaildPersonalFileSystemName(vf.getNameWithExtension()) )
        {
        	throw new DuplicateNameException("A file or folder with name " + vf.getName() +
        			" already exists in this folder", vf.getName());
        }
        
        SharedInboxFile inboxFile = getSharedInboxFile(vf);
        if ( inboxFile != null )
        {
        	removeFromSharedFileInbox(inboxFile);
        }
		PersonalFile pvf = new PersonalFile(this, vf);
		rootFiles.add(pvf);
		return pvf;
		
	}

	/**
	 * Returns an unmodifiable set of root folders.
	 * 
	 * @return
	 */
	public Set<PersonalFile> getRootFiles() {
		return Collections.unmodifiableSet(rootFiles);
	}

	/**
	 * Set the root personal files for this user.
	 * 
	 * @param rootFiles
	 */
	void setRootFiles(Set<PersonalFile> rootFiles) {
		this.rootFiles = rootFiles;
	}
	
	/**
	 * Get root personal file by name.
	 * 
	 * @param name - name of the personal file to return
	 * @return The personal file if found otherwise null.
	 */
	public PersonalFile getRootPersonalFile(String name)
	{
		for(PersonalFile personalFile: rootFiles )
		{
			if( personalFile.getName().equals(name))
			{
				return personalFile;
			}
		}
		return null;
	}
	
	/**
	 * Get root personal file by id.
	 * 
	 * @param id - id of the personal file to return
	 * @return The personal file if found otherwise null.
	 */
	public PersonalFile getRootPersonalFile(Long id)
	{
		for(PersonalFile personalFile: rootFiles )
		{
			if( personalFile.getId().equals(id))
			{
				return personalFile;
			}
		}
		return null;
	}

	
	/**
	 * Creates the root personal collection by name if it does not exist.  The 
	 * name should not be null or the name of an existing root
	 * personal collection for this user.
	 * 
	 * @param name of root folder to create.
	 * @return Created Folder if it does not already exist
	 * 
	 * @throws DuplicateNameException if the name of the personal collection 
	 * already exists or the name is null.
	 */
	public PersonalCollection createRootPersonalCollection(String name) throws DuplicateNameException
	{
		if( name == null)
		{
			throw new IllegalArgumentException("Name cannot be null");
		}
		
		PersonalCollection collection = null;
		if(getRootPersonalCollection(name) == null)
		{
		    collection = new PersonalCollection(this, name);
		    rootPersonalCollections.add(collection);
		}
		else
		{
			throw new DuplicateNameException("Personal collection with name " + name +
					" already exists ", name );
		}
		
		return collection;
	}

	/**
	 * Set of root personal collections for this user.
	 * 
	 * @return Unmodifiable set of root personal collections
	 */
	public Set<PersonalCollection> getRootPersonalCollections() {
		return Collections.unmodifiableSet(rootPersonalCollections);
	}

	/**
	 * The set of root personal collections owned by this user.
	 * 
	 * @param rootPersonalCollections
	 */
	void setRootPersonalCollections(Set<PersonalCollection> rootPersonalCollections) {
		this.rootPersonalCollections = rootPersonalCollections;
	}
	
	/**
	 * Get root personal collection by name.
	 * 
	 * @param name - name of the personal collection to return
	 * @return The personal collection if found otherwise null.
	 */
	public PersonalCollection getRootPersonalCollection(String name)
	{
		for(PersonalCollection collection: rootPersonalCollections )
		{
			if( collection.getName().equals(name))
			{
				return collection;
			}
		}
		return null;
	}
	
	/**
	 * Get root personal collection by id.
	 * 
	 * @param name - name of the personal collection to return
	 * @return The personal collection if found otherwise null.
	 */
	public PersonalCollection getRootPersonalCollection(Long id)
	{
		for(PersonalCollection collection: rootPersonalCollections )
		{
			if( collection.getId().equals(id))
			{
				return collection;
			}
		}
		return null;
	}
	
	/**
	 * Remove the personal collection from the set of root personal collections.
	 * 
	 * @param personalCollection
	 * @return true if the root personal collection is removed.
	 */
	public boolean removeRootPersonalCollection(PersonalCollection personalCollection)
	{
		return rootPersonalCollections.remove(personalCollection);
	}

	/**
	 * Get the root personal items.
	 * 
	 * @return root set of personal items
	 */
	public Set<PersonalItem> getRootPersonalItems() {
		return Collections.unmodifiableSet(rootPersonalItems);
	}

	/**
	 * Set the root personal items.
	 * 
	 * @param rootPersonalItems
	 */
	void setRootPersonalItems(Set<PersonalItem> rootPersonalItems) {
		this.rootPersonalItems = rootPersonalItems;
	}
	
	/**
	 * Creates the root item if it does not exist.  The 
	 * name should not be null or the name of an existing root
	 * personal item for this user.
	 * 
	 * @param versionedItem versionedItem to add to this user.
	 * @return Created personal if it does not already exist
	 * 
	 * @throws IllegalArgumentException if the name of the personal versionedItem
	 * already exists or the name is null.
	 */
	public PersonalItem createRootPersonalItem(VersionedItem versionedItem)
	{
		PersonalItem personalItem = null;
	    personalItem = new PersonalItem(this, versionedItem);
	    rootPersonalItems.add(personalItem);
		
		return personalItem;
	}
	
	/**
	 * Get root personal item by name.
	 * 
	 * @param name - name of the item to return
	 * @return The personal item if found otherwise null.
	 */
	public PersonalItem getRootPersonalItem(String name)
	{
		for(PersonalItem personalItem: rootPersonalItems )
		{
			if( personalItem.getName().equals(name))
			{
				return personalItem;
			}
		}
		return null;
	}
	
	/**
	 * Get root personal item by id.
	 * 
	 * @param name - name of the personal item to return
	 * @return The personal item if found otherwise null.
	 */
	public PersonalItem getRootPersonalItem(Long id)
	{
		for(PersonalItem personalItem: rootPersonalItems )
		{
			if( personalItem.getId().equals(id))
			{
				return personalItem;
			}
		}
		return null;
	}
	
	/**
	 * Add a department to this researcher.
	 * 
	 * @param department
	 * 
	 */
	public void addDepartment(Department department)
	{
		departments.add(department);
	}


	/**
	 * Remove the personal item from the set of root personal items.
	 * 
	 * @param personalItem
	 * @return true if the root personal item is removed.
	 */
	public boolean removeRootPersonalItem(PersonalItem personalItem)
	{
		return rootPersonalItems.remove(personalItem);
	}

	/**
	 * Get phone number of the user
	 * 
	 * @return phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Set phone number of the user
	 * 
	 * @param phoneNumber phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Get token given for forgot password
	 * 
	 * @return token
	 */
	public String getPasswordToken() {
		return passwordToken;
	}

	/**
	 * Set token when user forgot the password
	 * 
	 * @param passwordToken token
	 */
	public void setPasswordToken(String passwordToken) {
		this.passwordToken = passwordToken;
	}

	/**
	 * Return true if password has to be changed forcefully.
	 * 
	 * @return
	 */
	public boolean getPasswordChangeRequired() {
		return passwordChangeRequired;
	}

	/**
	 * Set true to force password change, else set false
	 * 
	 * @param forcePasswordChange 
	 */
	public void setPasswordChangeRequired(boolean passwordChangeRequired) {
		this.passwordChangeRequired = passwordChangeRequired;
	}

	/**
	 * Location of Folder which holds an index of this users workspace information.
	 * 
	 * @return the folder which is used to store this users 
	 * indexed information.
	 */
	public String getPersonalIndexFolder() {
		return personalIndexFolder;
	}

	/**
	 * Index which is used to store this users personal workspace information.
	 * 
	 * @param personalIndexFolder
	 */
	public void setPersonalIndexFolder(String personalIndexFolder) {
		this.personalIndexFolder = personalIndexFolder;
	}

	/**
	 * Get the affiliation for the user
	 * 
	 * @return
	 */
	public Affiliation getAffiliation() {
		return affiliation;
	}

	/**
	 * Set the affiliation for the user
	 * 
	 * @param affiliation
	 */
	public void setAffiliation(Affiliation affiliation) {
		this.affiliation = affiliation;
	}

	/**
	 * Last name of the user.
	 * 
	 * @return last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Set the last name of the user
	 * 
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
		
		if( this.lastName == null)
		{
			this.lowerCaseLastName = null;
		}
		else
		{
			this.lowerCaseLastName = this.lastName.toLowerCase();
		}
	}

	/**
	 * Get the first name of the user
	 * 
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Set the first name of the user.
	 * 
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
		if( this.firstName == null)
		{
			this.lowerCaseFirstName = null;
		}
		else
		{
			this.lowerCaseFirstName = this.firstName.toLowerCase();
		}
		
	}

	/**
	 * Returns true if the affiliation has been approved
	 * 
	 * @return
	 */
	public boolean isAffiliationApproved() {
		return isAffiliationApproved;
	}

	/**
	 * Set to true if the affiliation is true.
	 * 
	 * @param isAffiliationApproved
	 */
	public void setAffiliationApproved(boolean isAffiliationApproved) {
		this.isAffiliationApproved = isAffiliationApproved;
	}

	/**
	 * The users departments
	 * 
	 * @return an unmodifiable set of departments
	 */
	public Set<Department> getDepartments() {
		return Collections.unmodifiableSet(departments);
	}

	/**
	 * Set the researchers department.
	 * 
	 * @param department
	 */
	void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}
	
	/**
	 * Remove a department from this researcher.
	 * 
	 * @param department
	 * @return true if the department is removed.
	 */
	public boolean removeDepartment(Department department)
	{
		return departments.remove(department);
	}

	/**
	 * Remove all departments
	 */
	public void removeAllDepartments() {
		departments.clear();
	}

	
	/**
	 * Get the sid type.  This is for security.
	 * 
	 * @see edu.ur.ir.security.Sid#getSidType()
	 */
	public String getSidType() {
		return USER_SID_TYPE;
	}
	
	/**
	 * Get the accepted license by the license version.
	 * 
	 * @param licenseVersion - the license version that should be accepted
	 * @return the found user repository license or null if it is not found.
	 */
	public UserRepositoryLicense getAcceptedLicense(LicenseVersion licenseVersion)
	{
		for(UserRepositoryLicense license: acceptedLicenses )
		{
			if( license.getLicenseVersion().equals(licenseVersion))
			{
				return license;
			}
		}
		
		return null;
	}
	
	/**
	 * Create a new accepted license and return it for this user.  It is added
	 * to the list of the users accepted license.  If the user already has the
	 * license version, the existing user repository license is returned rather
	 * than creating a new one.
	 * 
	 * @param licenseVersion - the license version accepted.
	 * @return the created user repository license.
	 */
	public UserRepositoryLicense addAcceptedLicense(LicenseVersion licenseVersion)
	{
		UserRepositoryLicense license = getAcceptedLicense(licenseVersion);
		
		if( license == null)
		{
			license = new UserRepositoryLicense(licenseVersion, this);
			acceptedLicenses.add(license);
		}
		
		return license;
		
	}
	
	/**
	 * Remove an accepted license from a user.
	 * 
	 * @param license - license to remove
	 * @return true if the license is removed.
	 */
	public boolean removeAcceptedLicense(UserRepositoryLicense license)
	{
	    return acceptedLicenses.remove(license);
	}
	
    /**
     * Adds a file to the shared file in-box.
     * 
     * @param sharingUser - user who shared the file
     * @param versionedFile - versioned file being shared
     * 
     * @return the created shared inbox file
     *
     * @throws FileSharingException - if a user tries to share a file with themselves
     */
    public SharedInboxFile addToSharedFileInbox(IrUser sharingUser, VersionedFile versionedFile) 
        throws FileSharingException
    {
    	if( sharingUser.equals(this))
    	{
    		throw new FileSharingException("a user cannot share a file with themselves");
    	}
    	SharedInboxFile inboxFile = new SharedInboxFile(sharingUser, this, versionedFile);
    	sharedInboxFiles.add(inboxFile);
    	return inboxFile;
    }
    
    /**
     * Returns the found in-box file or null if in-box file not found by id.
     * 
     * @param id - id of the in-box file
     * @return the found in-box file or null
     */
    public SharedInboxFile getSharedInboxFile(Long id)
    {
    	for(SharedInboxFile inboxFile: sharedInboxFiles)
    	{
    		if( inboxFile.getId().equals(id))
    		{
    			return inboxFile;
    		}
    	}
    	return null;
    }
    
    /**
     * Returns the found in-box file or null if in-box file not found.
     * 
     * @param vf - versioned file the in-box file contains
     * @return the found in-box file or null
     */
    public SharedInboxFile getSharedInboxFile(VersionedFile vf)
    {
    	for(SharedInboxFile inboxFile: sharedInboxFiles)
    	{
    		if( inboxFile.getVersionedFile().equals(vf))
    		{
    			return inboxFile;
    		}
    	}
    	return null;
    }
    
    
    /**
     * Remove the personal in-box file from the shared file in-box.
     * 
     * @param personalInboxFile
     * @return true if the file is removed
     */
    public boolean removeFromSharedFileInbox(SharedInboxFile personalInboxFile)
    {
    	return sharedInboxFiles.remove(personalInboxFile);
    }

	/**
	 * Shared file in-box receives the files
	 * when a file is shared. Returns an unmodifiable set
	 * 
	 * @return
	 */
	public Set<SharedInboxFile> getSharedInboxFiles() {
		return Collections.unmodifiableSet(sharedInboxFiles);
	}

	/**
	 * The shared file in-box.
	 * 
	 * @param sharedFileInbox
	 */
	void setSharedInboxFiles (Set<SharedInboxFile> sharedFileInbox) {
		this.sharedInboxFiles = sharedFileInbox;
	}

	/**
	 * Get Authoritative Name for the user
	 * 
	 * @return Authoritative name for the user
	 */
	public PersonNameAuthority getPersonNameAuthority() {
		return personNameAuthority;
	}

	/**
	 * Set Authoritative Name for the user
	 * 
	 * @param personNameAuthority Authoritative Name for the user
	 */
	public void setPersonNameAuthority(PersonNameAuthority personNameAuthority) {
		this.personNameAuthority = personNameAuthority;
	}
	
	/**
	 * Create researcher information for the user
	 * 
	 * @return researcher information
	 */
	public Researcher createResearcher() { 
		
		Researcher researcher = new Researcher(this);
		this.researcher = researcher;
		
		return researcher;
	}

	/**
	 * Get researcher
	 * 
	 * @return researcher
	 */
	public Researcher getResearcher() {
		return researcher;
	}

	/**
	 * Set researcher
	 * 
	 * @param researcher
	 */
	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}
	
	/**
	 * Returns true if the name is ok to add to a file or folder
	 * 
	 * @param name of the file or folder.  If it is a file, it should contain
	 * the extension.
	 * 
	 * @return true if the name does not exist.  This is case insensitive.
	 */
	private boolean isVaildPersonalFileSystemName(String name)
	{
		boolean ok = false;
		if( getRootFolder(name) == null && getRootFile(name) == null)
		{
			ok = true;
		}
		return ok;
	}
	
	/**
	 * Get email having specified token
	 * 
	 * @param token
	 * @return
	 */
	public UserEmail getEmailByToken(String token) {
		
		UserEmail userEmail = null;
		
		if (token != null) {
			for (UserEmail email:emails) {
				if (token.equals(email.getToken())) {
					userEmail = email;
					break;
				}
			}
		}		
		
		return userEmail;
	}


	public boolean isSelfRegistered() {
		return selfRegistered;
	}

	public void setSelfRegistered(boolean selfRegistered) {
		this.selfRegistered = selfRegistered;
	}

	/**
	 * Returns the a set of unmodifiable licenses.
	 * 
	 * @return the set of licenses
	 */
	public Set<UserRepositoryLicense> getAcceptedLicenses() {
		return Collections.unmodifiableSet(acceptedLicenses);
	}

	/**
	 * Set of accepted licenses.
	 * 
	 * @param acceptedLicenses
	 */
	void setAcceptedLicenses(Set<UserRepositoryLicense> acceptedLicenses) {
		this.acceptedLicenses = acceptedLicenses;
	}

	/**
	 * Get the middle name of the user.
	 * 
	 * @return - middle name of the user
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * Set the middle name of the user.
	 * 
	 * @param middleName
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
		if( this.middleName == null)
		{
			this.lowerCaseMiddleName = null;
		}
		else
		{
			this.lowerCaseMiddleName = this.middleName.toLowerCase();
		}
	}

	/**
	 * Get the lower case value of the last name.
	 * 
	 * @return
	 */
	public String getLowerCaseLastName() {
		return lowerCaseLastName;
	}

	/**
	 * Get the lower case value of the first name.
	 * 
	 * @return
	 */
	public String getLowerCaseFirstName() {
		return lowerCaseFirstName;
	}

	/**
	 * Get the lower case middle name value.
	 * 
	 * @return
	 */
	public String getLowerCaseMiddleName() {
		return lowerCaseMiddleName;
	}

	/**
	 * Indicates the user workspace should be rebuilt.
	 * 
	 * @return
	 */
	public boolean getReBuildUserWorkspaceIndex() {
		return reBuildUserWorkspaceIndex;
	}

	/**
	 * Indicates the user workspace should be rebuilt.
	 * 
	 * @param reBuildUserIndex
	 */
	public void setReBuildUserWorkspaceIndex(boolean reBuildUserIndex) {
		this.reBuildUserWorkspaceIndex = reBuildUserIndex;
	}

	/**
	 * Get the users external account.
	 * 
	 * @return the users external account
	 */
	public ExternalUserAccount getExternalAccount() {
		return externalAccount;
	}
	
	/**
	 * Set the external user account to null
	 */
	public void deleteExternalUserAccount()
	{
		externalAccount = null;
	}
	
	/**
	 * Creates the external user account and sets it as the current external account.  
	 * 
	 * @param externalUserAccountName - name to use
	 * @param externalAccountType - account type
	 */
	public void createExternalUserAccount(String externalUserAccountName, ExternalAccountType externalAccountType )
	{
		externalAccount = new ExternalUserAccount(this,externalUserAccountName, externalAccountType );
	}
	
}
