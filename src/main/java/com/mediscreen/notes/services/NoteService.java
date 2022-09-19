package com.mediscreen.notes.services;

import java.util.List;

import com.mediscreen.notes.domain.Note;
import com.mediscreen.notes.domain.dto.NoteDto;

public interface NoteService {

    /**
     * Method used to get all patient's notes.
     *
     * @param lastName
     * @param firstName
     * @return all patient's notes
     */
    List<Note> getAllPatientNotes(String lastName, String firstName);

    /**
     * Method used to get all notes.
     *
     * @return all notes
     */
    List<Note> getAllNotes();

    /**
     * Method used to add a new patient's note.
     *
     * @param note
     * @return note
     */
    Note addNote(Note note);

    /**
     * Method used to update a note, using his id.
     *
     * @param note
     * @param id   the note's id
     * @return boolean isUpdated
     */
    boolean updateNote(Note note, String id);

    /**
     * Method used to get a note with his id.
     *
     * @param id
     * @return note or null
     */
    Note getNote(String id);

    /**
     * Method used to get all patient's NoteDto list.
     *
     * @param lastName
     * @param firstName
     * @return NoteDto list
     */
    List<NoteDto> getAllPatientsNoteDto(String lastName, String firstName);
}
