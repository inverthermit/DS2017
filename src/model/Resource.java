/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package model;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import model.command.Share;
import com.google.gson.Gson;

/**
 * This class is implemented to develop a robust Resource template class
 * including all of attributes such as name, description and uri. It also
 * can transmit a Resource object to a JSON string by using the function - 
 * toJSON and transmit a JSON string to a Resource object by utilizing the
 * function called fromJSON. The objective of the class is to make Resource
 * instances stored, looked up and transmitted more efficiently.
 * 
 * @author  Group - Alpha Panthers
 * @version 1.1
 */
public class Resource {
	/** All of attributes of one resource and initial them. */
	public String name = "";
	public String description = "";
	public String[] tags = {};
	public String uri = "";
	public String channel = "";
	public String owner = "";
	public String EZserver = "null";
	public long resourceSize;
	
	/** Default constructor of the class Resource. */
	public Resource() {

	}

	// Tim: this shouldn't be a Constructor, it actually finish the job of deep
	// copy of java object. Change function name to copyOf/clone
	// Refer to
	// http://stackoverflow.com/questions/64036/how-do-you-make-a-deep-copy-of-an-object-in-java
	// Use serialization code in the page.
	
	/**
     * Construct a Resource. It contains all of information which a resource
     * should be had.
     *
     * @param obj  a Resource object.
     */
	public Resource(Resource obj) {
		this.name = obj.name;
		this.description = obj.description;
		this.tags = obj.tags;
		this.uri = obj.uri;
		this.channel = obj.channel;
		this.owner = obj.owner;
		this.EZserver = obj.EZserver;
		this.resourceSize = obj.resourceSize;
	}

	/**
     * This is a setter() function to set the name for a resource instance.
     * 
     * @param a String type name
     */
	public void setName(String name) {
		this.name = name;
	}

	/**
     * This is a setter() function to set the description for a resource
     * instance.
     * 
     * @param a String type description
     */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
     * This is a setter() function to set the tags for a resource instance.
     * 
     * @param a String[] type tags
     */
	public void setTags(String[] tags) {
		this.tags = tags;
	}

	/**
     * This is a setter() function to set the uri for a resource instance.
     * 
     * @param a String type uri
     */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
     * This is a setter() function to set the channel for a resource instance.
     * 
     * @param a String type channel
     */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
     * This is a setter() function to set the owner for a resource instance.
     * 
     * @param a String type owner
     */
	public void setOwner(String owner) {
		this.owner = owner;
	}


	/**
     * This is a setter() function to set the EZserver for a resource instance.
     * 
     * @param a String[] type EZserver
     */

	public void setEZserver(String eZserver) {
		EZserver = eZserver;
	}

	/**
     * This function is used to transmit the current Resource instance to a JSON
     * string.
     * 
     * @return A JSON string contains all of information of the current object
     */
	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	/**
     * This function used to transmit a JSON string to a Resource object.
     * It accepts a JSON string and set all of instance variables that the
     * JSON contains to the target object. 
     * 
     * @param  A JSON string contains all of attributes of a Resource instance.
     */
	public void fromJSON(String json) {
		Gson gson = new Gson();
		Resource obj = gson.fromJson(json, Resource.class);
		this.name = obj.name;
		this.description = obj.description;
		this.tags = obj.tags;
		this.uri = obj.uri;
		this.channel = obj.channel;
		this.owner = obj.owner;
		this.EZserver = obj.EZserver;
		this.resourceSize = obj.resourceSize;
	}
	
	/**
	 * check the uri is valid or not
	 */
	public boolean isUriVaild(){
		try {
			URI uri = new URI(this.uri);
			return true;
		} catch (URISyntaxException e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * check the uri is valid or not for share
	 */
	public boolean isUriShare(){
		try {
			URI uri = new URI(this.uri);
			if(uri.isAbsolute() && uri.getScheme().equals("file") ){
				return true;
			} else {
				return false;
			}
		} catch (URISyntaxException e) {
				//e.printStackTrace();
				return false;
		}
	}
	
	public boolean isUriPublish(){
		try {
			URI uri = new URI(this.uri);
			if(!uri.getScheme().equals("file") ){
				return true;
			} else {
				return false;
			}
		} catch (URISyntaxException e) {
				//e.printStackTrace();
				return false;
		}
	}
	
	/**
	 * check the owner is "*" or not
	 */
	public boolean isOwnerValid(){
		if(this.owner.equals("*")){
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isArgValid(){
		if(isStringValid(this.name) && isStringValid(this.channel) && isStringValid(this.owner)&&
				isStringValid(this.uri)&&isStringValid(this.description)&&
				isStringValid(this.EZserver) && isTagsValid()){
			return true;
		} else {
		return false;
		}
	}
	
	public boolean isStringValid(String str){
		if(str.length()>=1){
			if(str.equals("\0")||str.charAt(0)==' ' || str.charAt(str.length()-1)==' '){
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	public boolean isTagsValid(){
		boolean flag = true;
		for(int i =0 ; i<this.tags.length;i++){
			if(!isStringValid(tags[i])){
				flag = false;
			}
		}
		return flag;
	}
	
	
}
