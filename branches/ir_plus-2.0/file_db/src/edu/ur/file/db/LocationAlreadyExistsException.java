package edu.ur.file.db;

/**
 * Exception to handle when a location already exists
 * @author NathanS
 *
 */
public class LocationAlreadyExistsException extends Exception{

	/** eclipse generated exception  */
	private static final long serialVersionUID = -389272733094827143L;
	
	   /** Name that was duplicated */
    private String location;
    
    /**
     * Message and the name that was duplicated.
     *
     * @param message
     * @param name
     */
    public LocationAlreadyExistsException(String message, String location)
    {
        super(message);
        this.location = location;
    }

    /**
     * Location that already exists
     *
     * @return
     */
    public String getLocation()
    {
        return location;
    }

}
