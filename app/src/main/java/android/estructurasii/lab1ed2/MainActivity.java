package android.estructurasii.lab1ed2;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState== null){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HuffCompressFragment()).commit();
        Toast.makeText(this,"Compresión Huffman",Toast.LENGTH_LONG).show();
        navigationView.setCheckedItem(R.id.nav_compresion);}

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_compresion:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HuffCompressFragment()).commit();
                break;
            case R.id.nav_decompression:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HuffDecompressFragment()).commit();
                break;

            case R.id.bitacora:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MisCompresiones()).commit();
                Toast.makeText(this,"Mis Compresiones",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_compressLZW:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LZWCompressFragment()).commit();
                Toast.makeText(this,"Compresión LZW",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_decompressLZW:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LZWDecompressFragment()).commit();
                Toast.makeText(this,"Descompresión LZW",Toast.LENGTH_LONG).show();
                break;

        }
        mLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mLayout.isDrawerOpen(GravityCompat.START)){
            mLayout.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }


    }
}
