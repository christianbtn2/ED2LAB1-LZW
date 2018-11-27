package android.estructurasii.lab1ed2.Huffman;

import java.util.Comparator;

public class HuffListComparator implements Comparator<Node<HuffNode>> {
    @Override
    public int compare(Node<HuffNode> o1, Node<HuffNode> o2) {
        return Integer.compare(o1.value.getFrecuency(), o2.value.getFrecuency());
    }
}
