package com.mediscreen.notes.services;

import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mediscreen.notes.controllers.exceptions.DataNotFoundException;
import com.mediscreen.notes.domain.Note;
import com.mediscreen.notes.repository.NoteRepository;
import com.mediscreen.notes.util.ConstraintsValidator;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger LOGGER = LogManager
            .getLogger("NoteServiceImpl");

    @Autowired
    private NoteRepository noteRepository;

    /**
     * Used while update note method: the number of characters allowed for the
     * user to delete.
     */
    private static final int MAX_SIZE_ALLOWED_TO_DELETE = 10;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> getAllPatientNotes(final Long patId) {
        List<Note> allPatientsNotes = noteRepository
                .findByPatIdOrderByCreationDateDesc(patId);

        if (allPatientsNotes.isEmpty()) {
            throw new DataNotFoundException(
                    "Notes not found for patient id:" + patId);
        }
        return allPatientsNotes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Note addNote(final Note note) {
        if (ConstraintsValidator.checkValidPatient(note) == null) {
            LOGGER.info("Constraints violated");
            return null;
        }
        note.setCreationDate(LocalDate.now());
        return noteRepository.save(note);
    }

    /**
     * {@inheritDoc}
     */
    public boolean updateNote(final Note note, final String id) {
        boolean isUpdated = false;

        Note existingNote = noteRepository.findById(id).orElse(null);

        if (existingNote == null) {
            LOGGER.error("Unknow note with id: {}", id);
            return isUpdated;
        }
        // Fast check that note is added, and note deleted
        // allowed to deleted errors (max 10 characters)
        if (note.getNote().length() < (existingNote.getNote().length()
                - MAX_SIZE_ALLOWED_TO_DELETE)) {
            LOGGER.error("Notes must be added, no deleted");
            return isUpdated;
        }
        existingNote.setNote(note.getNote());
        noteRepository.save(existingNote);
        isUpdated = true;
        return isUpdated;
    }

    /**
     * {@inheritDoc}
     */
    public Note getNote(final String id) {
        return noteRepository.findById(id).orElse(null);
    }

}
