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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mediscreen.notes.domain.Note;
import com.mediscreen.notes.domain.dto.NoteDto;
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

    @ApiOperation(value = "GET all the notes list", notes = "Return response 200", response = Note.class)
    @GetMapping("/all")
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @ApiOperation(value = "GET all the patient's notes list", notes = "Need param 'lastName' and 'firstName' - Return response 200 or 404 not found if no notes found", response = Note.class)
    @GetMapping
    public List<Note> getAllPatientsNotes(@RequestParam final String lastName,
            @RequestParam final String firstName) {
        return noteService.getAllPatientNotes(lastName, firstName);
    }

    @ApiOperation(value = "POST Add a new patient's note", notes = "Need Note body (with 'lastName', 'firstName', 'note') - Return response 201 created or 400 bad request")
    @PostMapping(consumes = { MediaType.ALL_VALUE })
    public ResponseEntity<Note> addNote(@Valid @RequestBody final Note note) {
        Note result = noteService.addNote(note);

        if (result != null) {
            return new ResponseEntity<Note>(HttpStatus.CREATED);
        }
        LOGGER.error("POST request FAILED for: /api/note");
        return new ResponseEntity<Note>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "PUT Update a note", notes = "Need Note body (with 'lastName', 'firstName', 'note') & param String 'id' with note's id - Return response 200 Ok or 400 bad request")
    @PutMapping
    public ResponseEntity<Boolean> updateNote(@RequestBody final Note note,
            @RequestParam final String id) {
        boolean result = noteService.updateNote(note, id);
        if (result == true) {
            return new ResponseEntity<Boolean>(HttpStatus.OK);
        }
        LOGGER.error("PUT request FAILED for: /api/note");
        return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "GET All patient's notes DTO", notes = "Need param String 'lastName' & String 'firstName' - Return response 200 or 404 not found if patient not found")
    @GetMapping("/getAllPatientsNoteDto")
    public List<NoteDto> getAllPatientsNoteDto(
            @RequestParam final String lastName,
            @RequestParam final String firstName) {
        return noteService.getAllPatientsNoteDto(lastName, firstName);
    }

}
