package android.estructurasii.lab1ed2;

import android.app.Activity;
import android.content.Intent;
import android.estructurasii.lab1ed2.Huffman.pathProvider;
import android.estructurasii.lab1ed2.LZW.LZWAlgrth;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class LZWDecompressFragment extends Fragment {
    private static final int READ_REQUEST_CODE = 42;
    private static final int READ_SELECT_CODE = 43;
    String solver =  "/storage/emulated/0/Descompresiones/";
    String NameToSave = "";
    LZWAlgrth Algorithm;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         final View view = inflater.inflate(R.layout.fragment_decompresslzw,container,false);
         final Button btnruta = view.findViewById(R.id.buttonselectpath_);
         final Button selectFile = view.findViewById(R.id.buttonlzw_);
         final EditText NombreArchivo = view.findViewById(R.id.edNombre);
         Algorithm = new LZWAlgrth(false);
         btnruta.setOnClickListener(new View.OnClickListener(){

             @Override
             public void onClick(View v) {
                 Intent intent = new Intent();
                 intent.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);

                 startActivityForResult(Intent.createChooser(intent,"Seleccione Ruta"),READ_SELECT_CODE);
             }
         });
         selectFile.setOnClickListener(new View.OnClickListener(){

             @Override
             public void onClick(View v) {
                 if(!NombreArchivo.getText().toString().isEmpty()){
                     NameToSave = NombreArchivo.getText().toString();
                     Intent intent = new Intent();
                     intent.setAction(Intent.ACTION_OPEN_DOCUMENT);


                     intent.addCategory(Intent.CATEGORY_OPENABLE);


                     intent.setType("*/*");

                     startActivityForResult(Intent.createChooser(intent,"Seleccione Archivo"), READ_REQUEST_CODE);
                 } else{
                     Toast.makeText(getContext(), "Debe Ingresar Nombre de Archivo", Toast.LENGTH_LONG).show();

                 }
             }
         });
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedFile = resultData.getData();
            try {
                InputStream input = getContext().getContentResolver().openInputStream(selectedFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String caracteres = br.readLine();
                ArrayList<String> stringsarray = new ArrayList<>();
                Algorithm.ReBuildDictionary(caracteres);
                StringBuilder str = new StringBuilder();
                String linea;
                while((linea = br.readLine()) != null){

                    linea = RevertChanges(linea);
                    str.append(linea);
                }
                String[] lines = str.toString().split("ꡐ");
                for(int i = 0; i<lines.length; i++){
                    stringsarray.add(lines[i]);
                }
                String[] results = new String[stringsarray.size()];
                for(int i = 0; i<stringsarray.size();i++){
                    results[i] = Algorithm.deCompress(stringsarray.get(i));
                }
                // archivo a escribir
                // nombre de nuevo archivo igual al archivo elegido
                File newfile = new File(solver, NameToSave + ".txt");
                FileOutputStream outputStream = new FileOutputStream(newfile);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                for(int i = 0; i<stringsarray.size();i++){
                    bufferedWriter.write(results[i]);
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();
                bufferedWriter.close();


                Toast.makeText(getContext(), "Guardado en" + solver +"/"+ NameToSave + ".txt", Toast.LENGTH_LONG).show();
                //se guarda en bitácora
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        if (requestCode == READ_SELECT_CODE && resultCode == Activity.RESULT_OK) {
            Uri selected = resultData.getData();
            pathProvider provider = new pathProvider();
            // se convierte el Uritree en un path normal
            solver = provider.getFullPathFromTreeUri(selected, getContext());

        }
    }
    private String RevertChanges(String revert){
        if(revert.contains("ꦃ")) {
            revert = revert.replaceAll(Pattern.quote("ꦃ"), Matcher.quoteReplacement("\n"));
        }
        if(revert.contains("ꦄ")) {
            revert = revert.replaceAll(Pattern.quote("ꦄ"), Matcher.quoteReplacement("\r"));
        }
        if(revert.contains("ﬀ")) {
            revert = revert.replaceAll(Pattern.quote("ﬀ"), Matcher.quoteReplacement("\t"));
        }
        if(revert.contains("ﬆ")) {
            revert = revert.replaceAll(Pattern.quote("ﬆ"), Matcher.quoteReplacement("\f"));
        }
        if(revert.contains("ﬡ")) {
            revert = revert.replaceAll(Pattern.quote("ﬡ"), Matcher.quoteReplacement("\b"));
        }
        if(revert.contains("תּ")) {
            revert = revert.replaceAll(Pattern.quote("תּ"), Matcher.quoteReplacement("\""));
        }
        if(revert.contains("פֿ")){
            revert = revert.replaceAll(Pattern.quote("פֿ"), Matcher.quoteReplacement("\'"));
        }

        return revert;
    }
}
