/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package model;

import java.net.Socket;
import java.util.ArrayList;

public class ClientModel {
	public String ip;
	public String port;
	public Socket socket;
	public ArrayList<ServerModel> serverList;
	public ArrayList<Resource> resourceList;
	public long timestamp;
}
