package android.estructurasii.lab1ed2.Huffman;

public class BTree<T extends Comparable<T>> {
    public Node<T> root;

    public BTree() {
        root = null;
    }
    public void Insert(T value) {
        Node<T> newNode = new Node<T>(value);
        if(root == null) {
            root = newNode;
        } else {

        }
    }
    public void InsertChild(Node<T> newN, Node<T> root ) {
        if(root != null) {
            if(newN.value.compareTo(root.value)<= 0) {
                if(root.Left == null) {
                    root.Left = newN;
                } else {
                    InsertChild(newN,root.Left);
                }
            } else {
                if(root.value.compareTo(newN.value)>0) {
                    if(root.Right == null) {
                        root.Right = newN;
                    } else {
                        InsertChild(newN,root.Right);
                    }
                }
            }
        }
    }
}
