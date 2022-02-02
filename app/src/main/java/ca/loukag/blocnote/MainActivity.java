package ca.loukag.blocnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvListe;
    private AdapterListe adapterListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        if(!new File(this.getFilesDir()+"/data.ser").exists()){
            Note.save(this.getFilesDir().toString());
        }else{
            try {
                FileInputStream fileInputStream = new FileInputStream(this.getFilesDir()+"/data.ser");
                ObjectInputStream objInput = new ObjectInputStream(fileInputStream);
                Note.loadSave((ArrayList<Note>) objInput.readObject());
                objInput.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        rvListe = findViewById(R.id.rvNote);
        rvListe.setHasFixedSize(true);
        rvListe.setLayoutManager(new LinearLayoutManager(this));

        adapterListe = new AdapterListe();

        rvListe.setAdapter(adapterListe);

        ImageButton newNote = (ImageButton) this.findViewById(R.id.btn_new_note);
        Intent intent = new Intent().setClass(this,NoteEditorActivity.class);
        newNote.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Note note = Note.createNote("","");
                note.setTitre("Note #"+note.getId());
                intent.putExtra("note", (Parcelable) note);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterListe.notifyDataSetChanged();
    }
}