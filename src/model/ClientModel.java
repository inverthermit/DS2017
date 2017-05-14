/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package model;

import java.net.Socket;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;
/**
 * This class is model of clients.
 * It contains ip, port, socket, serverList, resourceList
 * and the time stamp of the client's last query.
 * 
 * @author  Group - Alpha Panthers
 * @version 1.1
 */
public class ClientModel {
	public String ip;
	public String port;
	public Socket socket;
	public SSLSocket sslsocket;
	public ArrayList<ServerModel> serverList;
	public ArrayList<Resource> resourceList;
	public long timestamp;
}
