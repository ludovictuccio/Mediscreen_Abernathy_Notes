package com.mediscreen.notes.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mediscreen.notes.controllers.exceptions.DataNotFoundException;
import com.mediscreen.notes.domain.Note;
import com.mediscreen.notes.repository.NoteRepository;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger LOGGER = LogManager
            .getLogger("NoteServiceImpl");

    @Autowired
    private NoteRepository noteRepository;

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

}
