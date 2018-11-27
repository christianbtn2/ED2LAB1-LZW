package android.estructurasii.lab1ed2.Huffman;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Huffman {
    // variables Globales
    LinkedList<Node<HuffNode>> charList;
    private static HuffListComparator compare = new HuffListComparator();
    HashMap<String,Integer> data;
    BTree<HuffNode> ATree;
    /**
     * Constructor de la Clase
     */
    public Huffman() {
        charList = new LinkedList<>();
        ATree = new BTree<>();
        data = new HashMap<String,Integer>();
    }
    /**
     * Crea la tabla de frecuencia de caracteres
     * @param line, texto leido desde el archivo
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void FillTable (String line) {
        for(char c :line.toCharArray()) {
            if(data.containsKey(String.valueOf(c))== false) {
                int counter = 0;
                for(int i=0; i<line.length();i++) {
                    if(line.charAt(i) == c) {
                        counter++;
                    }
                }
                data.put(String.valueOf(c), counter);
            }
        }
        //Se crea una lista de nodos que contengan el caracter y su frecuencia
        ArrayList<String> keys = new ArrayList<String>(data.keySet());
        ArrayList<Integer> values = new ArrayList<Integer>(data.values());
        for(int i = 0; i<data.size(); i++) {
            charList.add(new Node<HuffNode>(new HuffNode(keys.get(i),values.get(i))));
        }
        // Se ejecuta el Algoritmo de Huffman
        Algorithm();
    }
    /**
     * Ejecuta el Algoritmo de Huffman
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Algorithm() {
        charList.sort(compare);
        while(charList.size() >1) {
            Node<HuffNode> first = charList.removeFirst();
            Node<HuffNode> second = charList.removeFirst();
            int sumFrec = first.value.getFrecuency() + second.value.getFrecuency();
            Node<HuffNode>sumOf = new Node<HuffNode>(new HuffNode("xyz",sumFrec),second,first);
            charList.add(sumOf);
            charList.sort(compare);
        }
        ATree.root = charList.removeFirst();

    }
    /**
     * Genera una cadena que contiene los caracteres
     * y sus frecuencias
     * @return Cadena ordenada entre caracteres y frecuencias
     */
    public String Entities() {
        NumberFormat formatter = new DecimalFormat("000000");
        StringBuilder resultado = new StringBuilder();
        resultado.append(formatter.format(data.size()));
        StringBuilder freq = new StringBuilder();
        for(int i = 0; i<data.size();i++) {
            if(i == data.size()-1) {
                freq.append(data.keySet().toArray()[i]);
            } else {
                freq.append(data.keySet().toArray()[i]+"~");
            }
        }
        resultado.append(freq.toString());
        StringBuilder values = new StringBuilder();
        for(int i = 0; i<data.size();i++) {
            if(i == data.size()-1) {
                values.append(formatter.format(data.values().toArray()[i]));
            } else {
                values.append(formatter.format(data.values().toArray()[i])+",");
            }
        }
        resultado.append(values.toString());
        return resultado.toString();
    }

    /**
     * Comprime un texto a través del algoritmo de Huffman
     * @param line Texto a comprimir
     * @return Texto Huffman
     */
    public String Compress(String line) {
        StringBuilder resultado = new StringBuilder();
        HashMap<String,String>Prefixtable = new HashMap<String,String>();
        CodingPrefix(ATree.root,Prefixtable,"");
        for (char c:line.toCharArray()) {
            resultado.append(Prefixtable.get(String.valueOf(c)));
        }
        return resultado.toString();


    }
    /**
     * Método recursivo que genera códigos frefijos através del
     * árbol creado con Huffman
     * @param node Nodo actual del recorrido
     * @param Prefixtable Tabla donde se almacenan los códigos de los caracteres
     * @param code Secuencia de codigo que se sigue
     */
    private void CodingPrefix(Node<HuffNode>node,HashMap<String,String>Prefixtable,String code) {
        if(node != null) {
            node.value.setBincode(code);
            CodingPrefix(node.Left,Prefixtable,code.concat("0"));
            CodingPrefix(node.Right,Prefixtable,code.concat("1"));
            if(node.isLeaf()) {
                Prefixtable.put(node.value.getCaracter(), node.value.getBincode());
            }

        }
    }
    /**
     * Este método descomprime una cadena en binario hacia caracteres legibles
     * @param text Linea de texto a descomprimir
     * @return cadena original
     */
    public String Decompress(String text) {
        HashMap<String,String>normalTable = new HashMap<String,String>();
        CodingPrefix(ATree.root,normalTable,"");
        HashMap<String,String>NormalTable = new HashMap<String,String>();
        NormalTable = BinTable(normalTable);
        StringBuilder controltext = new StringBuilder();
        StringBuilder resultado = new StringBuilder();
        for(char c:text.toCharArray()) {
            controltext.append(String.valueOf(c));
            if(NormalTable.containsKey(controltext.toString())) {
                resultado.append(NormalTable.get(controltext.toString()));
                controltext.setLength(0);
            }
        }
        return resultado.toString();
    }
    /**
     * Crea la tabla de frecuencias y caracteres al leer un archivo comprimido
     * @param table cadena que contiene frecuencias y caracteres
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void DecomTable(String table) {
        int format = 6;
        int entities = Integer.parseInt(table.substring(0, format));
        int newrange = format+(entities*2)-1;
        String characters = table.substring(format, newrange);
        String[] charactrs = characters.split("~");
        String frecuencies = table.substring(newrange,newrange+(format*entities)+(entities-1));
        String[] frecuencs = frecuencies.split(",");
        // Llena una lista de nodos con valores caracter y frecuencia
        for(int i= 0; i<charactrs.length; i++) {
            charList.add(new Node<HuffNode>(new HuffNode(charactrs[i],Integer.parseInt(frecuencs[i]))));
        }
        // Ejecuta el algoritmo de Huffman
        Algorithm();
    }
    /**
     * Invierte los valores de una tabla binario,cadena
     * que permite descomprimir una cadena
     * @param table Diccionario original
     * @return Diccionario invertido
     */
    public HashMap<String,String> BinTable(HashMap<String,String> table){
        ArrayList<String> keys = new ArrayList<String>(table.keySet());
        ArrayList<String> values = new ArrayList<String>(table.values());
        HashMap<String,String>normalTable = new HashMap<String,String>();
        for(int i = 0; i<keys.size();i++) {
            normalTable.put(values.get(i), keys.get(i));
        }
        return normalTable;
    }

    /**
     * convierte una cadena en binario a una cadena en ASCII
     * @param txt cadena en binario
     * @return cadena en ASCII
     * @throws UnsupportedEncodingException
     */
    public String ConvertToASCII(String txt) throws UnsupportedEncodingException {
        int size = txt.length();
        int iterator = size;
        int counter = 0;
        while(iterator%7!=0) {
            iterator = iterator+1;
            counter++;
        }
        StringBuilder Binstring = new StringBuilder(txt);
        for(int i = 0;i<counter;i++) {
            Binstring.append("0");
        }
        ArrayList<Byte> byter = new ArrayList<>();
        String newtxt = Binstring.toString();
        int minCentinel =0;
        int maxCentinel = 7;
        int flag = newtxt.length()/7;
        for(int i = 0; i<flag;i++) {
            String byte_ = newtxt.substring(minCentinel,maxCentinel);
            byter.add(Byte.parseByte(byte_,2));
            minCentinel = minCentinel+7;
            maxCentinel = maxCentinel+7;
        }
        byte[] byter_ = new byte[(byter.size())];
        for(int i = 0; i<byter.size(); i++) {
            byter_[i] = byter.get(i);
        }
        String newstring = new String(byter_,"US-ASCII");
        return newstring;
    }

    public String ConvertToBinary(String ascii) throws UnsupportedEncodingException {
        int format = 6;
        String toRead = ascii.substring(0,format);
        int size = Integer.parseInt(toRead);
        String ascii_ = ascii.substring(format, ascii.length());
        byte[] asciiBytes = ascii_.getBytes("US-ASCII");
        StringBuilder asciiResult = new StringBuilder();
        for(int i = 0; i<asciiBytes.length;i++) {
            String binaryByte = Integer.toBinaryString(asciiBytes[i]);
            int size_ = binaryByte.length();
            if(size_ == 7) {
                asciiResult.append(binaryByte);
            } else {
                int centinel = 7-size_;
                for(int j = 0; j<centinel; j++)
                {
                    asciiResult.append("0");
                }
                asciiResult.append(binaryByte);
            }

        }
        String result = asciiResult.toString().substring(0, size);
            return result;
    }
}
