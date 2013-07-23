package edu.ur.ir.item;

import java.util.LinkedList;

/**
 * Utility Class to determine which files can be played
 * by the player for the user interface.
 * 
 * @author Nathan Sarr
 *
 */
public class PlayerUtil {
	
	private static LinkedList<String> extensions = new LinkedList<String>();	
	
	static{
		extensions.add("mp4");
		extensions.add("m4v");
		extensions.add("f4v"); 
		extensions.add("mov");
		extensions.add("flv");
		extensions.add("webm");
		extensions.add("aac");
		extensions.add("m4a");
		extensions.add("f4a");
		extensions.add("mp3");
		extensions.add("ogg");
		extensions.add("oga");
	}
	
	/**
	 * Determine if the player can play the given extension.
	 * 
	 * @param extension 
	 * @return
	 */
	public static boolean canPlay(String extension){
		return extensions.contains(extension);
	}

}
