package tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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