package nextcp.ai;

/**
 * Thrown when the configured AI provider endpoint cannot be reached at all
 * (e.g. connection refused, connect timeout, unknown host, no route to host).
 * This typically means the base URL is wrong or the AI server (OpenWebUI,
 * Ollama, a self-hosted gateway, ...) is currently not running. The REST layer
 * maps this exception to a user-friendly, retryable message instead of leaking
 * a raw connection stack trace to the caller.
 */
public class ServiceUnreachableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceUnreachableException(String message) {
		super(message);
	}

	public ServiceUnreachableException(String message, Throwable cause) {
		super(message, cause);
	}
}
