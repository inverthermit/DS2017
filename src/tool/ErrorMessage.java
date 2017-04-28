/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class configures all the possible error messages when different 
 * types of error occur.And transmit them to Json Sting by utilizing the
 * function called toJSON.
 * 
 * @author  Group - Alpha Panthers
 * @version 1.1
 */
public class ErrorMessage
{
  public static final String GENERIC_INVALID = "invalid command";
  public static final String GENERIC_MISS_INCORRECT = "missing or incorrect type for command";
  public static final String PUBLISH_BROKEN = "cannot publish resource";
  public static final String PUBLISH_REMOVE_RESOURCE_INCORRECT = "invalid resource";
  public static final String PUBLISH_REMOVE_RESOURCE_MISSING = "missing resource";
  public static final String EXCHANGE_SERVERLIST_MISSING = "missing or invalid server list";
  public static final String REMOVE_RESOURCE_NOT_EXIST = "cannot remove resource";
  public static final String SHARE_BROKEN = "cannot share resource";
  public static final String SHARE_SECRET_INCORRECT = "incorrect secret";
  public static final String SHARE_MISSING = "missing resource and\\/or secret";
  public static final String QUERY_FETCH_EXCHANGE_RESOURCETEMPLATE_MISSING = "missing resourceTemplate";
  public static final String QUERY_FETCH_RESOURCETEMPLATE_INVALID = "invalid resourceTemplate";
  
  public void checkPublish() {}
  
//  public String toJSON()
//  {
//    Gson gson = new GsonBuilder().setPrettyPrinting().create();
//    return gson.toJson(this);
//  }
}