package nextcp.ai;

/**
 * Thrown when the AI provider rejects a request because the configured budget
 * or quota for the API key has been exceeded (e.g. HTTP 429 from Google
 * Gemini). The REST layer maps this exception to a user-friendly message
 * instead of leaking a stack trace to the caller.
 */
public class BudgetExceededException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BudgetExceededException(String message) {
		super(message);
	}

	public BudgetExceededException(String message, Throwable cause) {
		super(message, cause);
	}
}
