package edu.ur.dspace.load;

/**
 * This class will help get the name and articles
 *
 * @author Nathan Sarr
 *
 */
public class LeadingTitleArticleUtil {
	
	public String[] leadingArticles =  { "a", "an", "de", "the", "ye", "d’" };
	
	/**
	 * Return the name without the leading articles.  Returns
	 * the original name if there 
	 * 
	 * @return
	 */
	public String getName(String fullName)
	{
		// split on white space
		int firstSpaceIndex = fullName.trim().indexOf(" ");
		
		// no spaces
		if(firstSpaceIndex != -1 )
		{
			String firstWord = fullName.substring(0, firstSpaceIndex).trim();
		    String remainder = fullName.substring(firstSpaceIndex).trim();
		    for(String s : leadingArticles)
		    {
		    	if(s.equalsIgnoreCase(firstWord))
		    	{
		    		return remainder;
		    	}
		    }
		}
		return fullName;
	}
	
	/**
	 * Returns the leading articles or an empty string "" if no leading articles found.
	 * 
	 * @param fullName
	 * @return
	 */
	public String getLeadingArticle(String fullName)
	{
		// split on white space
		int firstSpaceIndex = fullName.trim().indexOf(" ");
		
		if(firstSpaceIndex != -1 )
		{
			String firstWord = fullName.substring(0, firstSpaceIndex).trim();
		    for(String s : leadingArticles)
		    {
		    	if(s.equalsIgnoreCase(firstWord))
		    	{
		    		return firstWord;
		    	}
		    }
		}
		return "";
	}
	
	public static void main (String[] args)
	{
		LeadingTitleArticleUtil util = new LeadingTitleArticleUtil();
		System.out.println("big test articles = " + util.getLeadingArticle("big test"));
		System.out.println("big test name= " + util.getName("big test"));

		System.out.println("The big test articles = " + util.getLeadingArticle("The big test"));
		System.out.println("The big test name = " + util.getName("The big test"));
		
		System.out.println("A big test articles = " + util.getLeadingArticle("A big test"));
		System.out.println("A big test name = " + util.getName("A big test"));
		
		System.out.println("An interesting test artciles= " + util.getLeadingArticle("An interesting test"));
		System.out.println("An interesting test name = " + util.getName("An interesting test"));

		System.out.println("d’ interesting test articles = " + util.getLeadingArticle("d’ interesting test"));
		System.out.println("d’ interesting test name = " + util.getName("d’ interesting test"));

		System.out.println("ye interesting test articles = " + util.getLeadingArticle("ye interesting test"));
		System.out.println("ye interesting test name = " + util.getName("ye interesting test"));

		System.out.println("DE interesting test articles = " + util.getLeadingArticle("DE interesting test"));
		System.out.println("DE interesting test name = " + util.getName("DE interesting test"));
		
		

	}

}
