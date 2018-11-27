package android.estructurasii.lab1ed2.Huffman;

public class Node<T> {
    public Node<T> Right;
    public Node<T> Left;
    public T value;

    public Node(T value, Node<T> right, Node<T> left) {
        this.Right = right;
        this.Left = left;
        this.value = value;
    }
    public Node(T value) {
        this.value = value;
        this.Right = null;
        this.Left = null;
    }

    public boolean isEmpty() {
        return this.value == null;
    }
    public boolean isLeaf() {
        return this.Right == null && this.Left == null;
    }
}
