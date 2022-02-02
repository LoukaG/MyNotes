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

    public static String[] months;

    private EditText titre, description;
    private TextView date;
    private ImageButton back, delete;
    private Note note;

    static{
        months = new String[]{"Janv", "Févr", "Mars", "Avr", "Mai", "Juin", "Juill", "Sept", "Oct", "Nov", "Déc"};
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onStart() {
        super.onStart();

        setContentView(R.layout.note_editor);

        titre = (EditText) this.findViewById(R.id.inputTitre);
        description = (EditText) this.findViewById(R.id.inputDescription);
        date = (TextView) this.findViewById(R.id.tvEditorDate);
        delete = (ImageButton) this.findViewById(R.id.btn_delete);
        Intent intent = getIntent();
        note = (Note)intent.getParcelableExtra("note");
        titre.setText(note.getTitre());
        description.setText(note.getDescription());
        date.setText(note.getStringDateCreation());

        back = this.findViewById(R.id.btn_back);

        Intent i = new Intent().setClass(this,MainActivity.class);


        back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(titre.getText().toString().length() >= 3) {
                    note.setTitre(titre.getText().toString());
                    note.setDescription(description.getText().toString());
                    Note.updateNote(note);
                    Note.save(getFilesDir().toString());
                    startActivity(i);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setCancelable(true);
                builder.setTitle("Suppression");

                builder.setMessage("Voulez-vous supprimer la note \""+note.getTitre()+"\" ?");
                builder.setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
