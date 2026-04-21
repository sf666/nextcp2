package devicedriver.util;

import java.nio.CharBuffer;

/**
 * Utility methods for CharBuffer operations. Prerequisite: The buffer must
 * be flipped before use (flip() has been called).
 */
public class CharBufferUtil {

	private CharBufferUtil() {
	}

	/**
	 * Returns the first index of the searched character.
	 *
	 * @param c Character to search for
	 * @param buffer Flipped CharBuffer
	 * @return Index of the first occurrence, or -1 if not found
	 */
	public static int getFirstIndexOf(char c, CharBuffer buffer) {
		if (buffer == null) {
			return -1;
		}

		for (int i = 0; i < buffer.limit(); i++) {
			if (buffer.get(i) == c) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the last index of the searched character.
	 *
	 * @param c Character to search for
	 * @param buffer Flipped CharBuffer
	 * @return Index of the last occurrence, or -1 if not found
	 */
	public static int getLastIndexOf(char c, CharBuffer buffer) {
		if (buffer == null) {
			return -1;
		}

		for (int i = buffer.limit() - 1; i >= 0; i--) {
			if (buffer.get(i) == c) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Checks whether the buffer ends with the specified character.
	 *
	 * @param c Character to search for
	 * @param buffer Flipped CharBuffer
	 * @return true if the buffer ends with the character
	 */
	public static boolean endsWith(char c, CharBuffer buffer) {
		if (buffer == null || buffer.limit() == 0) {
			return false;
		}

		return buffer.get(buffer.limit() - 1) == c;
	}

	/**
	 * Checks whether the buffer ends with the specified string.
	 *
	 * @param chars String to search for
	 * @param buffer Flipped CharBuffer
	 * @return true if the buffer ends with the string
	 */
	public static boolean endsWith(String chars, CharBuffer buffer) {
		if (buffer == null || chars == null) {
			return false;
		}

		// Fix: Guard against a buffer that is too short
		if (buffer.limit() < chars.length()) {
			return false;
		}

		int comparePos = buffer.limit() - chars.length();

		// Fix: < chars.length() instead of < chars.length() - 1
		for (int i = 0; i < chars.length(); i++) {
			if (chars.charAt(i) != buffer.get(comparePos + i)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks whether the buffer starts with the specified character.
	 *
	 * @param c Character to search for
	 * @param buffer Flipped CharBuffer
	 * @return true if the buffer starts with the character
	 */
	public static boolean startsWith(char c, CharBuffer buffer) {
		if (buffer == null || buffer.limit() == 0) {
			return false;
		}

		return buffer.get(0) == c;
	}

	/**
	 * Checks whether the buffer starts with the specified string.
	 *
	 * @param request String to search for
	 * @param buffer Flipped CharBuffer
	 * @return true if the buffer starts with the string
	 */
	public static boolean startsWith(String request, CharBuffer buffer) {
		if (buffer == null || request == null) {
			return false;
		}

		// Fix: Guard against a buffer that is too short
		if (buffer.limit() < request.length()) {
			return false;
		}

		// Fix: < request.length() instead of < request.length() - 1
		for (int i = 0; i < request.length(); i++) {
			if (request.charAt(i) != buffer.get(i)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Splits the buffer using the specified regex.
	 *
	 * @param buffer Flipped CharBuffer
	 * @param regex Delimiter as regex
	 * @return Array of split strings, empty array for null input
	 */
	public static String[] split(CharBuffer buffer, String regex) {
		if (buffer == null || regex == null) {
			return new String[0];
		}

		return buffer.toString().split(regex);
	}
}