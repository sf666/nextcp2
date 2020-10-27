export function isAssigned(object: any): boolean {
    if (typeof object !== 'undefined') {
        return true;
    }
    return false;
}

export const delay = (ms: number) => new Promise(_ => setTimeout(_, ms));