package edu.ur.ir.institution;

import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;

/**
 * Represents a subscription to an institutional collection.
 * 
 * @author Nathan Sarr
 *
 */
public class InstitutionalCollectionSubscription extends BasePersistent{
	
	/** Eclipse generated id. */
	private static final long serialVersionUID = -5788429914128301455L;

	/**  Institutional collection this user is subscribed to  */
	private InstitutionalCollection institutionalCollection;
	
	/**  The user subscribed */
	private IrUser user;
	
	/**
	 * Package protected constructor
	 */
	InstitutionalCollectionSubscription(){}
	
	/**
	 * Creates a subscription with the institutional collection and user.
	 * 
	 * @param institutionalCollection - institutional collection to subscribe to
	 * @param user - user who is subscribed.
	 */
	public InstitutionalCollectionSubscription(InstitutionalCollection institutionalCollection, IrUser user)
	{
		setInstitutionalCollection(institutionalCollection);
		setUser(user);
	}

	/**
	 * Get the institutional collection the user is subscribed to.
	 * 
	 * @return
	 */
	public InstitutionalCollection getInstitutionalCollection() {
		return institutionalCollection;
	}

	/**
	 * Set the institutional collection for the subscription.
	 * 
	 * @param institutionalCollection
	 */
	void setInstitutionalCollection(
			InstitutionalCollection institutionalCollection) {
		this.institutionalCollection = institutionalCollection;
	}

	/**
	 * Get the user.
	 * 
	 * @return
	 */
	public IrUser getUser() {
		return user;
	}

	/**
	 * Set the user.
	 * 
	 * @param user
	 */
	void setUser(IrUser user) {
		this.user = user;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int hashCode = 0;
		hashCode += user == null ? 0 : user.hashCode();
		hashCode += institutionalCollection == null ? 0 : institutionalCollection.hashCode();
		return hashCode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if( this == o) return true;
		if (!(o instanceof InstitutionalCollectionSubscription)) return false;

		final InstitutionalCollectionSubscription other = (InstitutionalCollectionSubscription) o;
		
		if( ( user != null && !user.equals(other.getUser()) ) ||
			( user == null && other.getUser() != null ) ) return false;
		
		if( ( institutionalCollection != null && !institutionalCollection.equals(other.getInstitutionalCollection()) ) &&
			( institutionalCollection == null && !institutionalCollection.equals(other.getInstitutionalCollection()) ) ) return false; 
		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" institutional collection = ");
		sb.append(institutionalCollection);
		sb.append(" user = ");
		sb.append(user);
		sb.append(" ]");
		return sb.toString();
	}
	
	
	
	

}
