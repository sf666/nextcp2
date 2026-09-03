package codegen.model;

/**
 * One state variable or action argument as it is remembered in the service model.
 *
 * The java type is what the generated code needs; the upnp type is kept for reference and is only
 * known once a real device announced the element (a model seeded from already generated code has
 * no way to tell a ui1 from a ui4).
 */
public class VariableModel {

	private final String name;
	private final String javaType;
	private final String upnpType;

	public VariableModel(String name, String javaType, String upnpType) {
		this.name = name;
		this.javaType = javaType;
		this.upnpType = upnpType;
	}

	public String getName() {
		return name;
	}

	public String getJavaType() {
		return javaType;
	}

	public String getUpnpType() {
		return upnpType;
	}

	/**
	 * Keeps whatever knows more: an element that a device announced carries its upnp type, one that
	 * was recovered from generated code does not.
	 */
	public VariableModel mergedWith(VariableModel other) {
		if (other == null) {
			return this;
		}
		return upnpType != null ? this : new VariableModel(name, javaType, other.upnpType);
	}
}
