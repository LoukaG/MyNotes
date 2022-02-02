package ca.loukag.blocnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class NoteEditorActivity extends AppCompatActivity {

    private EditText titre, description;
    private TextView date;
    private ImageButton back, delete;
    private Note note;

    @Override
    protected void onStart() {
        super.onStart();

        setContentView(R.layout.note_editor);

        //Charger les inputs de la page
        titre = (EditText) this.findViewById(R.id.inputTitre);
        description = (EditText) this.findViewById(R.id.inputDescription);
        date = (TextView) this.findViewById(R.id.tvEditorDate);
        delete = (ImageButton) this.findViewById(R.id.btn_delete);
        Intent intent = getIntent();
        note = (Note)intent.getParcelableExtra("note");

        //Définir les inputs avec les valeurs de la note
        titre.setText(note.getTitre());
        description.setText(note.getDescription());
        date.setText(note.getStringDateCreation());

        back = this.findViewById(R.id.btn_back);

        Intent i = new Intent().setClass(this,MainActivity.class);

        //Clique bouton retour
        back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //Le titre doit avoir au minimum 3 caractères
                if(titre.getText().toString().length() < 3) return;


                //Sauvegarder et retourner vers la page d'acceuil
                note.setTitre(titre.getText().toString());
                note.setDescription(description.getText().toString());
                Note.updateNote(note);
                Note.save(getFilesDir().toString());
                startActivity(i);
            }
        });


        //Quand l'utilisateur clique sur le bouton supprimer
        delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //Dialogue de confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setCancelable(true);
                builder.setTitle("Suppression");

                builder.setMessage("Voulez-vous supprimer la note \""+note.getTitre()+"\" ?");
                builder.setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Confirmation. Suppression et retour vers la page d'acceuil
                                Note.removeNote(note.getId());
                                Note.save(getFilesDir().toString());
                                startActivity(i);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(note.getId() == -1) return;
        if(titre.getText().toString().length() >= 3)
            note.setTitre(titre.getText().toString());
        note.setDescription(description.getText().toString());
        Note.updateNote(note);
        Note.save(this.getFilesDir().toString());
    }
}
