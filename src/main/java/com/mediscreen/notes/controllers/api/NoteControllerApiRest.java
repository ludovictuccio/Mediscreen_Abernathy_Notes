package com.mediscreen.notes.controllers.api;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.notes.domain.Note;
import com.mediscreen.notes.services.NoteService;

import io.swagger.annotations.ApiOperation;

@RestController
@Validated
@RequestMapping(value = "/api/note")
public class NoteControllerApiRest {

    private static final Logger LOGGER = LogManager
            .getLogger("NoteControllerApiRest");

    @Autowired
    private NoteService noteService;

    @ApiOperation(value = "GET all the notes for all patients", notes = "Return response 200", response = Note.class)
    @GetMapping("/all")
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @ApiOperation(value = "GET all the patient's notes", notes = "Need param 'patId', the patient's id - Return response 200 or 404 not found if no notes found", response = Note.class)
    @GetMapping
    public List<Note> getAllPatientsNotes(@RequestParam final Long patId) {
        return noteService.getAllPatientNotes(patId);
    }

    @ApiOperation(value = "POST Add a new patient's note", notes = "Need Note body - Return response 201 created or 400 bad request")
    @PostMapping(consumes = { MediaType.ALL_VALUE })
    public ResponseEntity<Note> addNote(@Valid @RequestBody final Note note) {
        Note result = noteService.addNote(note);

        if (result != null) {
            return new ResponseEntity<Note>(HttpStatus.CREATED);
        }
        LOGGER.error("POST request FAILED for: /api/note");
        return new ResponseEntity<Note>(HttpStatus.BAD_REQUEST);
    }

}
