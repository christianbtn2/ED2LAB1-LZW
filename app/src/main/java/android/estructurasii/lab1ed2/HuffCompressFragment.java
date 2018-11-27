package android.estructurasii.lab1ed2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.estructurasii.lab1ed2.Huffman.Huffman;
import android.estructurasii.lab1ed2.Huffman.pathProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HuffCompressFragment extends Fragment {

    private static final int READ_REQUEST_CODE = 42;
    Huffman Algorithm;
    TextView fileview;
    Registros register;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_compress,container,false);
       final Button bcompress = view.findViewById(R.id.button);
        fileview = view.findViewById(R.id.textdecompress);
        Algorithm = new Huffman();
        register = new Registros();
        bcompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);


                intent.addCategory(Intent.CATEGORY_OPENABLE);


                intent.setType("text/plain");

                startActivityForResult(Intent.createChooser(intent,"Seleccione Archivo"), READ_REQUEST_CODE);

            }
        });
        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri uri_ = resultData.getData();
            try {
                InputStream input = getContext().getContentResolver().openInputStream(uri_);
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                StringBuilder str = new StringBuilder();
                ArrayList<String> stringsarray = new ArrayList<>();
                String linea;
                while((linea = br.readLine()) != null){

                    str.append(linea);
                    stringsarray.add(linea);

                }
                br.close();
                input.close();

                Algorithm.FillTable(str.toString());
                ArrayList<String> resultArray = new ArrayList<>();
                resultArray.add(Algorithm.Entities());
                for(int i = 0; i<stringsarray.size();i++){
                    resultArray.add(Algorithm.Compress(stringsarray.get(i)));
                }
                int size = 0;
                String[] resultsAscii = new String[resultArray.size()-1];
                for(int i = 1; i<resultArray.size(); i++) {
                    size = resultArray.get(i).length();
                    String Ascii = Algorithm.ConvertToASCII(resultArray.get(i));
                    Ascii = ChangeTroubleStrings(Ascii);
                    NumberFormat formatter = new DecimalFormat("000000");
                    StringBuilder Asciistring = new StringBuilder(formatter.format(size));
                    Asciistring.append(Ascii);
                    resultsAscii[i-1] = Asciistring.toString();
                }
                File storage = new File(Environment.getExternalStorageDirectory(),"Compresion");
                if(!storage.exists()){
                    storage.mkdirs();
                }

                File tempfile = new File(uri_.getPath());
                File path = new File(storage,tempfile.getName()+".huff");
                FileOutputStream outputStream = new FileOutputStream(path);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                bufferedWriter.write(resultArray.get(0));
                bufferedWriter.newLine();
                for(int i = 0; i<resultsAscii.length; i++) {
                    bufferedWriter.write(resultsAscii[i].toCharArray());
                    bufferedWriter.write("σ");
                }
                bufferedWriter.flush();
                bufferedWriter.close();
                Toast.makeText(getContext(),"Guardado en compresiones"+"/"+tempfile.getName()+".huff", Toast.LENGTH_LONG).show();
                pathProvider pathProvider_ = new pathProvider();
                register.AddRegister(path.getPath(),pathProvider_.getPath(getContext(),uri_),"Huffman");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String ChangeTroubleStrings (String linetochange) {
        if (linetochange.contains("\n")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\n"), Matcher.quoteReplacement("ε"));
        }
        if (linetochange.contains("\t")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\t"), Matcher.quoteReplacement("Φ"));
        }
        if (linetochange.contains("\r")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\r"), Matcher.quoteReplacement("η"));
        }
        if (linetochange.contains("\f")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\f"), Matcher.quoteReplacement("θ"));
        }
        if (linetochange.contains("\b")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\b"), Matcher.quoteReplacement("μ"));
        }
        if (linetochange.contains("\"")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\""), Matcher.quoteReplacement("φ"));
        }
        if (linetochange.contains("\'")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\'"), Matcher.quoteReplacement("λ"));
        }

        return linetochange;
    }





}
