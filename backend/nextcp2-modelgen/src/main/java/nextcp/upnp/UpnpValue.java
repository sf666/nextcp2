package nextcp.upnp;

import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.meta.ActionArgument;
import org.jupnp.model.types.Base64Datatype;
import org.jupnp.model.types.Datatype;
import org.jupnp.model.types.UnsignedVariableInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts UPnP values whose declared datatype is only known at runtime.
 *
 * The generated classes carry the datatype a device announced when the code generator saw it. That
 * is a snapshot: a firmware update can turn a ui4 into a string, and a plain cast then throws and
 * the value is lost without anyone noticing. jUPnP has already converted an incoming value using
 * the datatype of the device that is talking to us right now, so the value itself is authoritative
 * and the generated type is only a hint - these helpers convert towards the hint instead of
 * insisting on it.
 *
 * Outgoing values work the other way round: the device tells us what it expects, so the argument's
 * datatype decides how the value is handed to jUPnP.
 */
public final class UpnpValue {

	private static final Logger log = LoggerFactory.getLogger(UpnpValue.class.getName());

	private UpnpValue() {
	}

	public static String toText(Object value) {
		return value == null ? null : value.toString();
	}

	/**
	 * Action outputs of type string were always handed out as "" instead of null.
	 */
	public static String toTextOrEmpty(Object value) {
		return value == null ? "" : value.toString();
	}

	public static Long toLong(Object value) {
		Number number = toNumber(value);
		return number == null ? null : number.longValue();
	}

	public static Integer toInteger(Object value) {
		Number number = toNumber(value);
		return number == null ? null : number.intValue();
	}

	public static Boolean toBoolean(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Boolean b) {
			return b;
		}
		if (value instanceof Number n) {
			return n.longValue() != 0;
		}
		String text = value.toString().trim();
		if (text.isEmpty()) {
			return null;
		}
		// The UDA allows 0/1, true/false and yes/no for the boolean datatype.
		switch (text.toLowerCase()) {
			case "1", "true", "yes":
				return Boolean.TRUE;
			case "0", "false", "no":
				return Boolean.FALSE;
			default:
				log.warn("cannot read '{}' as a boolean.", text);
				return null;
		}
	}

	public static byte[] toBytes(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof byte[] bytes) {
			return bytes;
		}
		try {
			return new Base64Datatype().valueOf(value.toString());
		} catch (Exception e) {
			log.warn("cannot read '{}' as base64 encoded data.", value, e);
			return null;
		}
	}

	/**
	 * Prepares a value for {@link ActionInvocation#setInput(String, Object)} using the datatype the
	 * device declared for that argument, so an argument the device announces as a string is sent as
	 * a string even when the generated code holds a number.
	 */
	public static Object forInput(ActionInvocation<?> invocation, String argumentName, Object value) {
		if (value == null) {
			return null;
		}
		ActionArgument<?> argument = invocation.getAction() != null ? invocation.getAction().getInputArgument(argumentName) : null;
		if (argument == null) {
			return value;
		}
		Datatype<?> datatype = argument.getDatatype();
		if (datatype.isHandlingJavaType(value.getClass())) {
			return value;
		}
		try {
			return datatype.valueOf(value.toString());
		} catch (Exception e) {
			log.warn("cannot convert '{}' to the datatype {} announced for argument '{}'.", value, datatype, argumentName, e);
			return value;
		}
	}

	private static Number toNumber(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof UnsignedVariableInteger unsigned) {
			return unsigned.getValue();
		}
		if (value instanceof Number n) {
			return n;
		}
		String text = value.toString().trim();
		if (text.isEmpty()) {
			return null;
		}
		try {
			return Long.valueOf(text);
		} catch (NumberFormatException e) {
			log.warn("cannot read '{}' as a number.", text);
			return null;
		}
	}
}
