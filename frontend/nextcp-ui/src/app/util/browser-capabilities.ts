/**
 * Native browser feature detection.
 */

/**
 * Whether the browser supports the CSS `backdrop-filter` property
 * (including the legacy `-webkit-` prefixed variant). Evaluated once at
 * module load; the result is constant for the lifetime of the session.
 */
export const supportsBackdropFilter: boolean =
  typeof CSS !== 'undefined' &&
  typeof CSS.supports === 'function' &&
  (CSS.supports('backdrop-filter', 'blur(1px)') ||
    CSS.supports('-webkit-backdrop-filter', 'blur(1px)'));
