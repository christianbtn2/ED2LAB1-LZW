package android.estructurasii.lab1ed2.LZW;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZWAlgrth {
    HashMap<String,Integer> dictionary;
    HashMap<Integer,String> dictionary_;
    /**
     * Constructor de la clase LZW
     * @param activacion, boolean que indica cual diccionario se utilizar�
     */
    public LZWAlgrth(boolean activacion) {
        if(activacion == true) {
            dictionary = new HashMap<String,Integer>();
        }else {
            dictionary_ = new HashMap<Integer,String>();
        }


    }
    /**
     * Comprime una cadena legible, atrav�s del algoritmo LZW
     * @param uncompressed, cadena legible
     * @return cadena compresa en formato Unicode
     * @throws UnsupportedEncodingException, si no se puede convertir a Unicode
     */
    public String compress(String uncompressed) throws UnsupportedEncodingException {

        List<Integer> part = partialcompress(uncompressed);
        String respuesta = "";
        for(int i = 0; i<part.size(); i++) {
            respuesta = respuesta + TurningintoUcode(part.get(i));
        }
        byte[] result = respuesta.getBytes(Charset.forName("Unicode"));
        respuesta = new String(result,"Unicode");

        return respuesta;
    }
    /**
     * Esta funci�n utiliza el algoritmo LZW para convertir los caracteres en los valores
     * del diccionario creado, para su codificaci�n en Unicode
     * @param uncompressed, cadena legible
     * @return Lista de c�digos enteros los cuales representan los valores del diccionario
     */
    private List<Integer> partialcompress(String uncompressed) {
        String s = "";
        List<Integer> result = new ArrayList<Integer>();
        for (char c : uncompressed.toCharArray()) {
            String sc = s + c;
            if (dictionary.containsKey(sc))
                s = sc;
            else {
                result.add(dictionary.get(s));
                // agregar wc al dictionary
                dictionary.put(sc, dictionary.size()+1);
                s = "" + c;
            }
        }

        // obtiene el c�digo del ultimo w
        if (!s.equals(""))
            result.add(dictionary.get(s));



        return result;
    }
    /**
     * Convierte los datos del diccionario necesarios para poder
     * reconstruirlo al descomprimir
     * @return una cadena que contiene los valores y series de un caracter del archivo
     */
    public String TabladeDictionary() {
        NumberFormat formatter = new DecimalFormat("000000");
        ArrayList<String> characters = new ArrayList<String>(dictionary.keySet());
        StringBuilder tableresult = new StringBuilder();
        tableresult.append(formatter.format(characters.size()));
        for(int i = 0; i<characters.size(); i++) {
            tableresult.append(characters.get(i));
        }
        for(int i =0; i<dictionary.size(); i++) {
            tableresult.append(formatter.format(dictionary.values().toArray()[i]));
        }

        return tableresult.toString();
    }
    /***
     * Llena el diccionario utilizado para la codificaci�n
     * @param uncompressed, recibe todo el archivo a comprimir con el objetivo
     * de obtener todas las series de un caracter diferentes
     */
    public void fillDictionary(String uncompressed) {
        int j = 0;
        ArrayList<String> chars = new ArrayList<>();
        for(int i = 0; i<uncompressed.length();i++) {
            if(!chars.contains(String.valueOf(uncompressed.charAt(i)))){

                chars.add(String.valueOf(uncompressed.charAt(i)));
            }

        }
        Collections.sort(chars);
        for(int i =0; i<chars.size();i++){
            j = j+1;
            dictionary.put(chars.get(i),j);
        }

    }
    /**
     * Rearma el diccionario al leer el archivo compreso
     * @param data, cadena que contiene una tabla de caracteres y su valores
     * para recrear el diccionario utilizado en la decodificaci�n
     */
    public void ReBuildDictionary(String data) {
        int format = 6;
        int size = Integer.parseInt(data.substring(0, format));
        String characters = data.substring(format,format + size);
        int flag = format + size;
        String numbers = data.substring(flag,data.length());
        ArrayList<String> character = new ArrayList<String>();
        for(int i = 0; i<characters.toCharArray().length; i++) {
            character.add(String.valueOf(characters.charAt(i)));
        }
        int min = 0;
        ArrayList<Integer> number = new ArrayList<Integer>();
        int limit = numbers.length()/6;
        for(int i = 0; i<limit;i++) {
            number.add(Integer.parseInt(numbers.substring(min, format)));
            min +=6;
            format+=6;
        }

        for(int i = 0; i<size;i++) {
            dictionary_.put(number.get(i), character.get(i));

        }

    }
    /**
     * Convierte un entero en su representaci�n char
     * Unicode
     * @param number, entero a convertir
     * @return elemento en char
     */
    private char TurningintoUcode(int number) {

            return (char) number;

    }
    /**
     * Convierte un char Unicode en su representaci�n
     * entera
     * @param code, char Unicode
     * @return entero equivalente
     */
    private int TurningfromUcode(char code) {

            return (int) code;


    }
    /**
     * Funci�n que convierte los valores codificados por el algoritmo
     * a su equivalente en cadena
     * @param codes Lista de representaci�n en c�digos
     * @return Cadena equivalente a los c�digos de la lista
     */
    private String Partialdecompress(List<Integer>codes) {

        String s = "" + dictionary_.get(codes.remove(0));
        StringBuffer result = new StringBuffer(s);
        for (int k : codes) {
            String entry;
            if (dictionary_.containsKey(k))
                entry = dictionary_.get(k);
             else if (k == dictionary_.size()+1)
                entry = s + s.charAt(0);
             else
                throw new IllegalArgumentException("error en compresi�n: " + k);

            result.append(entry);

            // Agrega w+entry[0] a dictionary_
            dictionary_.put(dictionary_.size()+1, s + entry.charAt(0));
            s = entry;
        }
        return result.toString();
    }
    /**
     * Funci�n que retorna un string legible, a partir de una cadena en
     * Unicode
     * @param encoded, cadena en formato Unicode
     * @return cadena legible equivalente
     */
    public String deCompress(String encoded) {
        List<Integer> codes = new ArrayList<Integer>();
        for(int i = 0; i<encoded.length(); i++) {
            codes.add(TurningfromUcode(encoded.charAt(i)));
        }
        String result = Partialdecompress(codes);

        return result;
    }
}
