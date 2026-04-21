export default class Stack<TData> {

    private _topNode: Node<TData> | undefined = undefined;
    private _count: number = 0;

    public count(): number {
        return this._count;
    }

    public isEmpty(): boolean {
        return this._topNode === undefined;
    }

    public push(value: TData): void {
        // create a new Node and add it to the top
        let node = new Node<TData>(value, this._topNode);
        this._topNode = node;
        this._count++;
    }

    public pop(): TData {
        if (!this._topNode) {
            throw new Error('Cannot pop from an empty stack');
        }

        // remove the top node from the stack.
        // the node at the top now is the one before it
        let poppedNode  = this._topNode;
        this._topNode = poppedNode.previous;
        this._count--;
        return poppedNode.data;
    }

    public peek(): TData {
        if (!this._topNode) {
            throw new Error('Cannot peek an empty stack');
        }
        return this._topNode.data;
    }

}

class Node<T> {

    previous: Node<T> | undefined;
    data: T;

    constructor (data: T, previous: Node<T> | undefined) {
        this.previous = previous;
        this.data = data;
    }

}
