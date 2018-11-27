package android.estructurasii.lab1ed2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MisCompresiones extends Fragment {
    RecyclerView myView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          final View myCompress = inflater.inflate(R.layout.fragment_miscompresiones,container,false);
          myView = (RecyclerView) myCompress.findViewById(R.id.myRV);
          myView.setLayoutManager(new LinearLayoutManager(getActivity()));
          Registros registros = new Registros();
          CompresionesAdapter adapter = new CompresionesAdapter(getActivity(),registros.getRegisters());
          myView.setAdapter(adapter);
          return myCompress;
    }
}
