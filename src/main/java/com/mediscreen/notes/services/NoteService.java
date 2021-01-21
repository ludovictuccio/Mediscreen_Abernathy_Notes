package com.mediscreen.notes.services;

import java.util.List;

import com.mediscreen.notes.domain.Note;

public interface NoteService {

    /**
     * Method used to get all patient's notes.
     *
     * @param patId
     * @return all patient's notes
     */
    List<Note> getAllPatientNotes(final Long patId);

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
    Note addNote(final Note note);

    /**
     * Method used to update a note, using his id.
     *
     * @param note
     * @param id   the note's id
     * @return boolean isUpdated
     */
    boolean updateNote(final Note note, final String id);

    /**
     * Method used to get a note with his id.
     *
     * @param id
     * @return note or null
     */
    Note getNote(final String id);
}
