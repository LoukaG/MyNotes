package ca.loukag.blocnote;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Note implements Parcelable, Serializable {

    //  S T A T I C

    private static ArrayList<Note> noteList;

    /**
     * Note lors de l'installation de l'application. Sera ajouté au note seulement lorsque le fichier de sauvegarde n'existe pas
     */
    static{
        Note.noteList = new ArrayList<>();
        Note.createNote("\uD83D\uDC96 Bienvenue dans MyNotes !","Bienvenue dans MyNotes! \uD83D\uDCDD\n\nPrenez des notes simplement et efficacement. \uD83E\uDD29 \nMerci d'utiliser mon application et je vous souhaite la bienvenue. \uD83D\uDE01\n - Louka Gauthier \uD83D\uDC68\u200D\uD83D\uDCBB");
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    /**
     * Créer une note avec comme date de création celle d'aujourd'hui
     * @param _titre Titre de la note
     * @param _description Description de la note
     * @return Un object Note
     */
    public static Note createNote(String _titre, String _description){
        Note note = new Note(Note.noteList.size(),_titre,_description,LocalDateTime.now());
        Note.noteList.add(note);

        return note;
    }

    /**
     * Sauvegarder la liste de note dans un fichier data.ser
     * @param path Le chemin ou le fichier va être enregistré.
     */
    public static void save(String path){
        try {
            FileOutputStream file = new FileOutputStream(path+"/data.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
            objectOutputStream.writeObject(Note.getNoteList());
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charger une liste de note
     * @param notes La liste de note
     */
    public static void loadSave(@NonNull ArrayList<Note> notes){
        Note.noteList = notes;
    }

    public static void updateNote(Note note){
        Note note2 = Note.getNoteList().get(note.getId());
        note2.setTitre(note.getTitre());
        note2.setDescription(note.getDescription());
        note2.setDateCreation(note.getDateCreation());
    }

    /**
     * Créer une note
     * @param _titre Titre de la note
     * @param _description Description de la note
     * @param _dateCreation Date de création de la note
     * @return Un object Note
     */
    public static Note createNote(String _titre, String _description, LocalDateTime _dateCreation){
        Note note = new Note(Note.noteList.size(),_titre,_description,_dateCreation);
        Note.noteList.add(note);

        return note;
    }

    /**
     * Récupérer une note avec son id
     * @param _id L'id de la note
     * @return La note relié avec l'id
     */
    public static Note getNote(int _id){
        return Note.noteList.get(_id);
    }

    /**
     * Récupérer la liste complète des notes
     * @return Tous les notes
     */
    public static ArrayList<Note> getNoteList(){

        return noteList;
    }

    /**
     * Supprimer une note avec un id
     * @param _id L'id de la note a supprimer
     */
    public static void removeNote(int _id){
        Note.noteList.remove(_id);
    }

    //  O B J E C T

    private int id;
    private String titre, description;
    private LocalDateTime dateCreation;

    /**
     * Créer un objet d'une note
     * @param _titre Le titre de la note
     * @param _description  La description de la note
     * @param _dateCreation La date de création de la note
     */
    private Note(int _id, String _titre, String _description, LocalDateTime _dateCreation){
        this.id = _id;
        this.titre = _titre;
        this.description = _description;
        this.dateCreation = _dateCreation;
        LocalDateTime.now();
    }

    /**
     * Create note with Intent
     * @param in The parcel
     */
    protected Note(Parcel in) {
        id = in.readInt();
        titre = in.readString();
        description = in.readString();
        dateCreation = (LocalDateTime) in.readSerializable();

    }

    /**
     * Récupérer le titre de la note
     * @return Le titre de la note
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Définir le titre de la note
     * @param _titre le nouveau titre de la note
     */
    public void setTitre(String _titre) {
        this.titre = _titre;
    }

    /**
     * Récupérer la description de la note
     * @return La description de la note
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définir la description
     * @param _description La nouvelle description
     */
    public void setDescription(String _description) {
        this.description = _description;
    }

    /**
     * Récupurer la date de création de la note
     * @return La date de création de la note
     */
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    /**
     * Définir la date de création de la note
     * @param _dateCreation La nouvelle date de création de la note
     */
    public void setDateCreation(LocalDateTime _dateCreation) {
        this.dateCreation = _dateCreation;
    }

    public String getStringDateCreation(){
        String[] months = new String[]{"Janv", "Févr", "Mars", "Avr", "Mai", "Juin", "Juill", "Sept", "Oct", "Nov", "Déc"};
        return months[this.getDateCreation().getMonthValue()-1]+" "+this.getDateCreation().getDayOfMonth()+", "+this.getDateCreation().getYear()+", "+this.getDateCreation().getHour()+":"+this.getDateCreation().getMinute();
    }

    /**
     * Récupérer l'id de la note
     * @return L'id de la note
     */
    public int getId(){
        return Note.noteList.indexOf(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(titre);
        parcel.writeString(description);
        parcel.writeSerializable(dateCreation);
    }

    @Override
    public boolean equals(Object o) {
       Note note2 = (Note)o;
       return note2.dateCreation.equals(this.dateCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titre, description, dateCreation);
    }
}
