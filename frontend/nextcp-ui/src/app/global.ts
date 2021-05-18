export function isAssigned(object: any): boolean {
  if (typeof object !== 'undefined') {
    return true;
  }
  return false;
}

export const delay = (ms: number) => new Promise(_ => setTimeout(_, ms));

export function debounce<T extends (...args: any[]) => any>(
  ms: number,
  callback: T
): (...args: Parameters<T>) => Promise<ReturnType<T>> {
  let timer: NodeJS.Timeout | undefined;

  return (...args: Parameters<T>) => {
    if (timer) {
      clearTimeout(timer);
    }
    return new Promise<ReturnType<T>>((resolve) => {
      timer = setTimeout(() => {
        const returnValue = callback(...args) as ReturnType<T>;
        resolve(returnValue);
      }, ms);
    })
  };
}