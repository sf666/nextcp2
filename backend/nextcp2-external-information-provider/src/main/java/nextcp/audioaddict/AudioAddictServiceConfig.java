package nextcp.audioaddict;

/**
 * Username and password are optional and needed for "favorites" access.
 * 
 * Token is mandatory and needed for channel access and listening to music
 */
public class AudioAddictServiceConfig {
	public String user = "";
	public String pass = "";
	public String token = "";
	public boolean preferEuropeanServer = true;
}
