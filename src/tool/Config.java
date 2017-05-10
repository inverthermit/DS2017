/** Course: COMP90015 2017-SM1 Distributed Systems
 *  Project: Project1-EZShare Resource Sharing Network
 *  Group Name: Alpha Panthers
 */
package tool;

/**
 * This class sets the configurations of the system.
 * 
 * @author  Group - Alpha Panthers
 * @version 3.1
 */
public class Config {
	public static final int MAX_CONNECTION = 10;
	public static int HEARTBEAT_INTERVAL = 100000;//ms
	public static int CONNECTION_LIMIT_INTERVAL = 0;//minimum
	public static int CONNECTION_TIMEOUT = 100000000;
	public static final int TRUNK_SIZE = 1024*1024;
	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final int DEFAULT_PORT = 10001;
	public static final String DEFAULT_ADVERTISED_HOSTNAME = "ez.server.org";
}
