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

}
