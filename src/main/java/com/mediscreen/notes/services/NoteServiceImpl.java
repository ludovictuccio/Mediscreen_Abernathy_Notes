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

    @Autowired
    private ConstraintsValidator constraintsValidator;

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
        if (constraintsValidator.checkExistingPatient(note.getPatId(),
                note.getPatientLastname()) == false) {
            LOGGER.info(
                    "This patient doesn't exists. Please verify patient's id et lastname.");
            return null;
        }
        note.setCreationDate(LocalDate.now());
        note.setLastUpdateDate(null);
        return noteRepository.save(note);
    }

}
