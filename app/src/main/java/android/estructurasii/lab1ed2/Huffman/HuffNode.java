package android.estructurasii.lab1ed2.Huffman;

import java.text.Collator;

public class HuffNode implements Comparable<HuffNode> {
    private String caracter;
    private int frecuency;
    private String bincode;
    public String getCaracter() {
        return caracter;
    }
    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }
    public int getFrecuency() {
        return frecuency;
    }
    public void setFrecuency(int frecuency) {
        this.frecuency = frecuency;
    }
    public String getBincode() {
        return bincode;
    }
    public void setBincode(String bincode) {
        this.bincode = bincode;
    }
    public HuffNode(String caracter, int frecuency) {
        this.caracter = caracter;
        this.frecuency = frecuency;
        bincode = "";
    }

    @Override
    /**
     * Comparador entre caracteres, utilizado para ordenarlos seg�n su frecuencia
     */
    public int compareTo(HuffNode o) {
        if(this.frecuency > o.getFrecuency()) {
            return 1;
        } else if(this.frecuency == o.getFrecuency()) {
            return Collator.getInstance().compare(this.getCaracter(), o.getCaracter());

        } else {
            return -1;
        }

    }
}
