package nextcp.audioaddict;

/**
 * Username and password are optional and is needed for "favorites" access.
 *
 * Token is mandatory and needed for channel access and listening to music
 */
public class AudioAddictServiceConfig {
	public boolean enabled = false;
	public String user = "";
	public String pass = "";
	public boolean preferEuropeanServer = true;
}
