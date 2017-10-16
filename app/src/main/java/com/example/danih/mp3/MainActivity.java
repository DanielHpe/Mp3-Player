package com.example.danih.mp3;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private ListView listaMusicas;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaMusicas = (ListView) findViewById(R.id.listViewMusic);
        String path = Environment.getExternalStorageDirectory().toString() + "/Download";
        File directory = new File(path);
        final ArrayList<File> mySongs = findSongs(directory);
        items = new String[mySongs.size()];

        for(int i = 0; i < mySongs.size(); i++){
            items[i] = mySongs.get(i).getName().toString();
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.sound_layout,
                R.id.textModel,
                items
        );

        listaMusicas.setAdapter(adaptador);

        listaMusicas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class).putExtra("pos",
                        position).putExtra("song", mySongs);
                startActivity(intent);
            }
        });
    }

    public ArrayList<File> findSongs(File root){
        ArrayList<File> adicionado = new ArrayList<>();
        File files[] = root.listFiles();
        for(File singleFiles : files){
            if(singleFiles.isDirectory() && !singleFiles.isHidden()){
                adicionado.addAll(findSongs(singleFiles));
            }else{
                if(singleFiles.getName().endsWith(".mp3")){
                    adicionado.add(singleFiles);
                }
            }
        }
        return adicionado;
    }

    public void toast(String name){
        Toast.makeText(getApplicationContext(), name , Toast.LENGTH_LONG).show();
    }

}
