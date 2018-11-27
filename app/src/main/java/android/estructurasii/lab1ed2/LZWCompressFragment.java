package android.estructurasii.lab1ed2;

import android.app.Activity;
import android.content.Intent;
import android.estructurasii.lab1ed2.Huffman.pathProvider;
import android.estructurasii.lab1ed2.LZW.LZWAlgrth;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LZWCompressFragment extends Fragment {
    private static final int READ_REQUEST_CODE = 42;
    private static final int READ_SELECT_CODE = 43;
   String resolver =  "/storage/emulated/0/Compresiones/";
    Registros register;
    LZWAlgrth Algorithm;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_compresslzw,container,false);
        final Button selectFile = view.findViewById(R.id.buttonlzw);
        final Button selectpath = view.findViewById(R.id.buttonselectpath);
        Algorithm = new LZWAlgrth(true);
        register = new Registros();

        selectFile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);


                intent.addCategory(Intent.CATEGORY_OPENABLE);


                intent.setType("text/plain");

                startActivityForResult(Intent.createChooser(intent,"Seleccione Archivo"), READ_REQUEST_CODE);
            }
        });
        selectpath.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);

                startActivityForResult(Intent.createChooser(intent,"Seleccione Ruta"),READ_SELECT_CODE);

            }
        });
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if(requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri selectedFile = resultData.getData();
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(selectedFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder str = new StringBuilder();
                ArrayList<String> stringsarray = new ArrayList<>();
                String linea;
                while((linea = br.readLine()) != null){

                    str.append(linea);
                    stringsarray.add(linea);

                }
                br.close();
                inputStream.close();
                Algorithm.fillDictionary(str.toString());
                ArrayList<String> resultArray = new ArrayList<>();
                resultArray.add(Algorithm.TabladeDictionary());
                for(int i = 0; i<stringsarray.size();i++){
                    String temp = Algorithm.compress(stringsarray.get(i));
                    temp = ChangeTroubleStrings(temp);
                    resultArray.add(temp);
                }

                // se convierte el Uri a file para obtener el nombre del archivo
                File tempfile = new File(selectedFile.getPath());
                String name = tempfile.getName().replace('.','_'); // nombre de nuevo archivo igual al archivo elegido
                // archivo a escribir
                File newfile = new File(resolver,name+".lzw");

                FileOutputStream outputStream = new FileOutputStream(newfile);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                bufferedWriter.write(resultArray.get(0));
                bufferedWriter.newLine();
                for(int i = 1; i<resultArray.size(); i++){
                    bufferedWriter.write(resultArray.get(i).toCharArray());
                    bufferedWriter.write("ꡐ");
                }
                bufferedWriter.flush();
                bufferedWriter.close();
                Toast.makeText(getContext(),"Guardado en" +resolver+"/"+name+".lzw", Toast.LENGTH_LONG).show();
                pathProvider provider = new pathProvider();
                //se guarda en bitácora
                register.AddRegister(newfile.getPath(),provider.getPath(getContext(),selectedFile),"LZW");


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }







        }
        if(requestCode == READ_SELECT_CODE && resultCode == Activity.RESULT_OK){
            Uri selected = resultData.getData();
            pathProvider provider = new pathProvider();
            // se convierte el Uritree en un path normal
            resolver = provider.getFullPathFromTreeUri(selected,getContext());

        }


    }
    String ChangeTroubleStrings (String linetochange) {
        if (linetochange.contains("\n")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\n"), Matcher.quoteReplacement("ꦃ"));
        }
        if (linetochange.contains("\t")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\t"), Matcher.quoteReplacement("ꦄ"));
        }
        if (linetochange.contains("\r")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\r"), Matcher.quoteReplacement("ﬀ"));
        }
        if (linetochange.contains("\f")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\f"), Matcher.quoteReplacement("ﬆ"));
        }
        if (linetochange.contains("\b")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\b"), Matcher.quoteReplacement("ﬡ"));
        }
        if (linetochange.contains("\"")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\""), Matcher.quoteReplacement("תּ"));
        }
        if (linetochange.contains("\'")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\'"), Matcher.quoteReplacement("פֿ"));
        }

        return linetochange;
    }
}
