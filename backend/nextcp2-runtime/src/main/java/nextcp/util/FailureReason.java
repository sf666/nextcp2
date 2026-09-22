package nextcp.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import nextcp.upnp.GenActionException;
import nextcp.upnp.UpnpErrorDescriptionHandler;

/**
 * What the user is told when something failed.
 *
 * A failure reaches the UI through three different worlds and each used to phrase itself its own way:
 * the UPnP stack throws {@link GenActionException} carrying a device's SOAP fault, UMS answers its
 * extended API with its own wording inside such a fault, and nextcp itself throws
 * {@link BackendException} or a Spring {@link ResponseStatusException}. Every REST service had grown
 * its own little extractor for this - four of them, plus thirteen call sites doing the XML step by
 * hand - and they disagreed: one returned the whole SOAP envelope, one the bare text, one the class
 * name, one an empty string. What the user saw depended on which path the failure happened to take,
 * and "entry already in Playlist." was lost on the way more than once.
 *
 * One question, one answer: {@link #of(Throwable)} names the reason, {@link #describe} puts it behind
 * what the user was trying to do. The device's own words win over our wrapping, because they are the
 * only part that says anything new.
 */
public final class FailureReason {

	/** Extraction is idempotent, so it is safe wherever a reason may or may not still be a fault body. */
	private static final UpnpErrorDescriptionHandler FAULT = new UpnpErrorDescriptionHandler();

	/** Long enough for a device's sentence, short enough for a toast. */
	private static final int MAX_LENGTH = 320;

	/** A cause chain is walked at most this far, so a self-referencing one cannot spin. */
	private static final int MAX_DEPTH = 12;

	private FailureReason() {
	}

	/**
	 * The most specific reason this failure carries.
	 *
	 * The chain is walked to the bottom: an outer exception says what nextcp was doing, the inner one
	 * what the device said, and only the latter tells the user something they did not already know.
	 * Falls back to the outermost message, and finally to the exception's own name, so the answer is
	 * never empty.
	 */
	public static String of(Throwable failure) {
		String fromDevice = null;
		String firstAnyway = null;

		Throwable current = failure;
		for (int depth = 0; current != null && depth < MAX_DEPTH; depth++) {
			String reason = reasonOfFrame(current);
			if (StringUtils.isNotBlank(reason)) {
				if (current instanceof GenActionException) {
					fromDevice = reason;
				} else if (firstAnyway == null) {
					firstAnyway = reason;
				}
			}
			current = current.getCause() == current ? null : current.getCause();
		}

		String reason = StringUtils.defaultIfBlank(fromDevice, firstAnyway);
		if (StringUtils.isBlank(reason)) {
			return failure == null ? "" : failure.getClass().getSimpleName();
		}
		return shorten(normalize(reason));
	}

	/**
	 * "Cannot play the folder : 501 : entry already in Playlist." - what was attempted, then why it
	 * did not happen.
	 */
	public static String describe(String attempted, Throwable failure) {
		String reason = of(failure);
		if (StringUtils.isBlank(attempted)) {
			return reason;
		}
		return StringUtils.isBlank(reason) ? attempted : attempted + " : " + reason;
	}

	/** Cuts an over-long reason on a word boundary; the full text is in the log either way. */
	public static String shorten(String reason) {
		if (reason == null || reason.length() <= MAX_LENGTH) {
			return reason;
		}
		return StringUtils.abbreviate(reason, MAX_LENGTH);
	}

	private static String reasonOfFrame(Throwable frame) {
		if (frame instanceof GenActionException genAction) {
			if (genAction.errorCode == GenActionException.ACTION_NOT_SUPPORTED) {
				// Nothing failed here - the device never offered this. Said as such, because "action
				// GetWebStreamIcyOrder of service UmsExtendedServices" is our vocabulary, not the user's.
				return "This media server does not offer that function.";
			}
			if (StringUtils.isNotBlank(genAction.deviceReason)) {
				// The device said something of its own. Everything we wrapped around it - which
				// device, which action, the UPnP code - the user already knows or cannot act on,
				// and it pushed the one new sentence out of sight at the end of the toast.
				return genAction.deviceReason;
			}
			// Nothing more precise available : the wrapped sentence is all there is. Run it through
			// the extractor anyway, since a device that answered outside ActionCallback still gets
			// its envelope unwrapped here.
			return FAULT.extractErrorText(genAction.description);
		}
		if (frame instanceof BackendException backend) {
			return backend.getDescription();
		}
		if (frame instanceof ResponseStatusException status) {
			// Not getMessage() : that reads "417 EXPECTATION_FAILED \"…\"" and carries the wrapping
			// into the toast.
			return status.getReason();
		}
		return frame.getMessage();
	}

	/** One line, single spaces - a fault body can arrive wrapped across several. */
	private static String normalize(String reason) {
		return StringUtils.normalizeSpace(reason);
	}
}
