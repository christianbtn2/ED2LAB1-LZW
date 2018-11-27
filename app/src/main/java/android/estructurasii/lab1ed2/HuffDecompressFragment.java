package android.estructurasii.lab1ed2;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.estructurasii.lab1ed2.Huffman.Huffman;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class HuffDecompressFragment extends Fragment {
    private static final int WRITE_REQUEST_CODE = 43;
    private static int Action = 0;
    private String NameToSave = "";
    private String pathsinDocto = "";
    Huffman Algorithm;
    TextView fileview;
    Button btnRuta;
    Button btnSave;
    TextView Ruta;
    EditText NombreArchivo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_decompress,container,false);
        final Button debcompress = view.findViewById(R.id.button1);
        final Button btnRuta = view.findViewById(R.id.btnRutaSv);
        final EditText NombreArchivo = view.findViewById(R.id.etNombre);
        fileview = view.findViewById(R.id.textcompress);
        Ruta = view.findViewById(R.id.tvRuta);
        Algorithm = new Huffman();
        debcompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Action = 1;
                if (!NombreArchivo.getText().toString().isEmpty() && !Ruta.getText().toString().isEmpty())
                {
                    NameToSave = NombreArchivo.getText().toString();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);


                    intent.addCategory(Intent.CATEGORY_OPENABLE);


                    intent.setType("*/*");

                    startActivityForResult(Intent.createChooser(intent,"Seleccione Archivo"), WRITE_REQUEST_CODE);
                }
                else
                {
                    Toast.makeText(getContext(), "Debe Ingresar Nombre de Archivo y Seleccionar Ruta", Toast.LENGTH_LONG).show();
                }


            }
        });

        btnRuta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vw) {
                Action = 2;
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                intent2.setType("*/*");
                startActivityForResult(Intent.createChooser(intent2,"Seleccione Ruta"), WRITE_REQUEST_CODE);
            }
        });

        //btnSave.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View v) {
             //   Action = 3;
             //   if (NombreArchivo.getText().toString().isEmpty())
             //   {
             //       Toast.makeText(getContext(), "Debe Ingresar Nombre de Archivo", Toast.LENGTH_LONG).show();
             //   }
             //   else
             //   {

             //   }
           // }
       // });

        return view;
    }

    @TargetApi(VERSION_CODES.N)
    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (Action == 1)
        {
            if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
                Uri uri_ = resultData.getData();
                try {
                    InputStream input = getContext().getContentResolver().openInputStream(uri_);
                    BufferedReader br = new BufferedReader(new InputStreamReader(input));
                    String caracteres = br.readLine();
                    ArrayList<String> stringsarray = new ArrayList<>();
                    Algorithm.DecomTable(caracteres);
                    StringBuilder str = new StringBuilder();
                    String linea;
                    while((linea = br.readLine()) != null){

                        linea = RevertChanges(linea);
                        str.append(linea);
                    }
                    br.close();
                    input.close();
                    String[] lines = str.toString().split("σ");
                    for(int i = 0; i<lines.length; i++){
                        stringsarray.add(lines[i]);
                    }


                    String[] resultsBinary = new String[stringsarray.size()];
                    for(int i = 0; i<stringsarray.size();i++) {
                        resultsBinary[i] = Algorithm.ConvertToBinary(stringsarray.get(i));
                    }
                    ArrayList<String> resultArray = new ArrayList<>();
                    for(int i = 0; i<resultsBinary.length;i++){
                        resultArray.add(Algorithm.Decompress(resultsBinary[i]));
                    }
                    File storage = new File(Environment.getExternalStorageDirectory(),"Descompresión");
                    if(!storage.exists()){
                        storage.mkdirs();
                    }
                    //File path = new File(storage,"Documents"+".txt");
                    //Agregado
                    File storage2 = new File(pathsinDocto.toString(), "");
                    if(!storage2.exists()){
                        storage2.mkdirs();
                    }
                    File path = new File(pathsinDocto.toString(),NameToSave+".txt");
                    //Ruta.setText(path.getPath().toString());
                    //
                    FileOutputStream outputStream = new FileOutputStream(pathsinDocto.toString()+NameToSave+".txt");
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                    for(int i = 0; i<resultArray.size();i++){
                        bufferedWriter.write(resultArray.get(i));
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    //Toast.makeText(getContext(),"Guardado en Descompresiones"+"/"+"Documents"+".txt", Toast.LENGTH_LONG).show();
                    //Agregado
                    Toast.makeText(getContext(),"Guardado en: "+pathsinDocto.toString()+NameToSave+".txt", Toast.LENGTH_LONG).show();
                    //

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        else if (Action == 2)
        {
            if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
                Uri uri_ = resultData.getData();
                try {
                    pathsinDocto = getPath(getContext(), uri_);
                    //String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    // Prueba
                    String[] split = pathsinDocto.split("/");
                    String pathsindocto = "";
                    int i = 0;
                    for (i=0;i<split.length-1;i++)
                    {
                        pathsindocto = pathsindocto + split[i].toString()+"/";
                    }
                    pathsinDocto = pathsindocto.toString();
                    // Fin Prueba
                    Ruta.setText(pathsinDocto);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }

    }

    private String RevertChanges(String revert){
        if(revert.contains("ε")) {
            revert = revert.replaceAll(Pattern.quote("ε"), Matcher.quoteReplacement("\n"));
        }
        if(revert.contains("η")) {
            revert = revert.replaceAll(Pattern.quote("η"), Matcher.quoteReplacement("\r"));
        }
        if(revert.contains("Φ")) {
            revert = revert.replaceAll(Pattern.quote("Φ"), Matcher.quoteReplacement("\t"));
        }
        if(revert.contains("θ")) {
            revert = revert.replaceAll(Pattern.quote("θ"), Matcher.quoteReplacement("\f"));
        }
        if(revert.contains("μ")) {
            revert = revert.replaceAll(Pattern.quote("μ"), Matcher.quoteReplacement("\b"));
        }
        if(revert.contains("φ")) {
            revert = revert.replaceAll(Pattern.quote("φ"), Matcher.quoteReplacement("\""));
        }
        if(revert.contains("λ")){
            revert = revert.replaceAll(Pattern.quote("λ"), Matcher.quoteReplacement("\'"));
        }

        return revert;
    }



    private String ChangeTroubleStrings (String linetochange){
        if(linetochange.contains("\n")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\n"), Matcher.quoteReplacement("ε"));
        }
        if(linetochange.contains("\t")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\t"), Matcher.quoteReplacement("Φ"));
        }
        if(linetochange.contains("\r")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\r"), Matcher.quoteReplacement("η"));
        }
        if(linetochange.contains("\f")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\f"), Matcher.quoteReplacement("θ"));
        }
        if(linetochange.contains("\b")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\b"), Matcher.quoteReplacement("μ"));
        }
        if(linetochange.contains("\"")) {
            linetochange = linetochange.replaceAll(Pattern.quote("\""), Matcher.quoteReplacement("φ"));
        }
        if(linetochange.contains("\'")){
            linetochange = linetochange.replaceAll(Pattern.quote("\'"), Matcher.quoteReplacement("λ"));
        }

        return linetochange;

    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }










    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


}
