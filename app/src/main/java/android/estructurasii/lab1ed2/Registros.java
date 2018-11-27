package android.estructurasii.lab1ed2;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.Buffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Stack;

public class Registros {
    Stack<String[]> registers;
    String storage = "/storage/emulated/0/misCompresiones/compresiones.txt";

    public Registros(){
        registers = new Stack<>();
    }

    public  void AddRegister(String compress, String original,String algorithm){
        String[] newregister = new String[6];
        newregister[5] = algorithm;
        File original_ = new File(original);
        newregister[0]  = original_.getName();
        long sizeoriginal = original_.length();
        File compress_ = new File(compress);
        newregister[1] = compress_.getName() +":" + compress_.getPath();
        long sizecompress = compress_.length();
        NumberFormat formatter = new DecimalFormat("000.00");
        double razon = ((double) sizecompress/sizeoriginal)*100;
        double factor = ((double)sizeoriginal/sizecompress)*100;
        newregister[2] = formatter.format(razon);
        newregister[3] = formatter.format(factor);
        NumberFormat format = new DecimalFormat("00.00");
        double porcentaje = Math.abs(((double) sizeoriginal-sizecompress)/sizeoriginal)*100;
        newregister[4] = format.format(porcentaje);
        registers.add(newregister);
        File directory = new File("/storage/emulated/0/","misCompresiones");
        if(!directory.exists()){
            directory.mkdirs();
        }
        File directory_ = new File(directory,"compresiones"+".txt");
        try {
            FileWriter writer = new FileWriter(directory_.getPath(),true);
            BufferedWriter writer_ = new BufferedWriter(writer);

            if(directory_.exists()){
                for(String str : newregister){
                    writer_.write(str);
                    writer_.write("ب");
                }
                writer_.newLine();
            }
            writer_.close();
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public Stack<String[]> getRegisters (){

        File registers_ = new File(storage);
        if(registers_.exists()){
            try{
                FileReader reader = new FileReader(registers_);
                BufferedReader reader_= new BufferedReader(reader);
                String line;

                while((line = reader_.readLine())!= null){
                    registers.add(line.split("ب"));
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return registers;
    }



}
