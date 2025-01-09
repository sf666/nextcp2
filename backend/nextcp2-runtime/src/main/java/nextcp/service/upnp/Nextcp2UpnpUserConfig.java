package nextcp.service.upnp;


public class Nextcp2UpnpUserConfig {

	private String upnpBindAddress = null;
	
	
	protected String getUpnpBindAddress() {
		return upnpBindAddress;
	}

	
	protected void setUpnpBindAddress(String upnpBindAddress) {
		this.upnpBindAddress = upnpBindAddress;
	}

	public Nextcp2UpnpUserConfig(String upnpBindAddress) {
		super();
		this.upnpBindAddress = upnpBindAddress;
	}

	public Nextcp2UpnpUserConfig() {
	}

}
