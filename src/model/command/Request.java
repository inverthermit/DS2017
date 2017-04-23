/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 *  
 *  This class is a parent class of all of server commands classes. It
 *  contains a concrete function that its children will inherit and a abstract
 *  function that its children will override.
 *  
 */
package model.command;

import java.util.Arrays;

import com.google.gson.Gson;


import model.Resource;

public abstract class Request {

	/**
     * This function is used to transmit the current Resource instance to a
     * JSON string. For instance, if there is a Publish instance, it will be
     * transmitted to the string "{"command":"PUBLISH","relay":true,
     * "resourceTemplate":{"name":"Unimelb website 6667","description":
     * "The main page for the University of Melbournee","tags":["web","html"],
     * "uri":"http://www.unimelb1.edu.au","channel":"","owner":"","EZserver":
     * [],"resourceSize":0}}"
     * 
     * @return A JSON string contains all of information of the current object
     */
	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	/**
     * This function is abstract one and it is used to transmit a JSON string
     * to a target object. Some instances will override it.
     * 
     * @param  A JSON string contains all of information of the target instance.
     */
	public abstract void fromJSON(String json);

}
