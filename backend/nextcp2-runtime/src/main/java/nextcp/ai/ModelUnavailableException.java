package nextcp.ai;

/**
 * Thrown when the AI provider is temporarily unable to serve a request because
 * the model is overloaded or otherwise unavailable (e.g. HTTP 503 from Google
 * Gemini: "This model is currently experiencing high demand"). The REST layer
 * maps this exception to a user-friendly, retryable message instead of leaking
 * a stack trace to the caller.
 */
public class ModelUnavailableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ModelUnavailableException(String message) {
		super(message);
	}

	public ModelUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}
}
