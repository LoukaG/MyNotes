package ca.loukag.blocnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterListe extends RecyclerView.Adapter<AdapterListe.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_note,parent,false);
        return new ViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitre.setText(Note.getNote(position).getTitre());
        holder.tvDescription.setText(Note.getNote(position).getDescription());
        holder.tvDate.setText(Note.getNote(position).getStringDateCreation());

    }

    @Override
    public int getItemCount() {
        return Note.getNoteList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitre, tvDescription, tvDate;
        private AdapterListe adapter;
        public ViewHolder(@NonNull View itemView, AdapterListe adapter) {
            super(itemView);
            this.tvTitre = itemView.findViewById(R.id.tvTitre);
            this.tvDescription = itemView.findViewById(R.id.tvDescription);
            this.tvDate = itemView.findViewById(R.id.tvDate);
            this.adapter = adapter;

            itemView.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View view) {
                    Note note = Note.getNote(getLayoutPosition());
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Suppression");

                    builder.setMessage("Voulez-vous supprimer la note \""+note.getTitre()+"\" ?");
                    builder.setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Note.removeNote(note.getId());
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(itemView.getContext(),"La note a été supprimer.",Toast.LENGTH_SHORT).show();
                                    Note.save(view.getContext().getFilesDir().toString());
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Note note = Note.getNote(getLayoutPosition());
                    Intent intent = new Intent().setClass(view.getContext(),NoteEditorActivity.class);
                    intent.putExtra("note", (Parcelable) note);
                    view.getContext().startActivity(intent);
                }
            });

        }
    }
}
