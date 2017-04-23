package tool;
/**
 * Created by Tim Luo on 2017/3/27.
 */
public class Config {
	public static final int MAX_CONNECTION = 10;
	public static int HEARTBEAT_INTERVAL = 10000;//ms
	public static int CONNECTION_LIMIT_INTERVAL = 0;//minimum
	public static int CONNECTION_TIMEOUT = 20000;
	public static final int TRUNK_SIZE = 1024*1024;
	public static final int DEFAULT_PORT = 10000;
	public static final String DEFAULT_ADVERTISED_HOSTNAME = "ez.server.org";
}
