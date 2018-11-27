package android.estructurasii.lab1ed2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;

public class CompresionesAdapter extends RecyclerView.Adapter<CompresionesAdapter.CompresionesViewHolder> {
    private Context myContext;
    Stack<String[]> rgs_;
    private static ArrayList<String[]> rgs;

    public CompresionesAdapter(Context thecontext, Stack<String[]> registers){
        myContext = thecontext;
        rgs_ = registers;
        rgs = new ArrayList<>();
        int size = rgs_.size();
        for(int i = 0; i <size;i++){
            rgs.add(rgs_.pop());
        }
    }
    @NonNull
    @Override
    public CompresionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater Inflator = LayoutInflater.from(myContext);
        View view = Inflator.inflate(R.layout.cardlayoutmc,null);
        CompresionesViewHolder holder = new CompresionesViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull CompresionesViewHolder holder, int position) {
        String [] current = rgs.get(position);
        String first = "Nombre de Archivo original:" + current[0];
        holder.textView1.setText(first);
        String second = "Nombre y dirección Archivo comprimido:" + current[1];
        holder.textView2.setText(second);
        String third = "Razón de Compresión:" + current[2];
        holder.textView03.setText(third);
        String fourth = "Factor de Compresión:" + current[3];
        holder.textView4.setText(fourth);
        String fifth = "Porcentaje de Compresión:" + current[4] +"%";
        holder.textView5.setText(fifth);
        String sixth = "Algoritmo:" + current[5];
        holder.texTView6.setText(sixth);
    }

    @Override
    public int getItemCount() {
        return rgs.size();
    }

    public static class CompresionesViewHolder extends RecyclerView.ViewHolder{
        public TextView textView1;
        public TextView textView2;
        public TextView textView03;
        public TextView textView4;
        public TextView textView5;
        public TextView texTView6;

        public CompresionesViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.TextView01);
            textView2 = itemView.findViewById(R.id.TextView02);
            textView03 = itemView.findViewById(R.id.TextView03);
            textView4 = itemView.findViewById(R.id.TextView04);
            textView5 = itemView.findViewById(R.id.TextView05);
            texTView6 = itemView.findViewById(R.id.TextView06);
        }
    }
}
