package com.mediscreen.notes.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mediscreen.notes.controllers.exceptions.DataNotFoundException;
import com.mediscreen.notes.domain.Note;
import com.mediscreen.notes.domain.dto.NoteDto;
import com.mediscreen.notes.repository.NoteRepository;

@SpringBootTest
public class NoteServiceTest {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    private LocalDate date1 = LocalDate.of(2021, 01, 01);
    private LocalDate date2 = LocalDate.of(2020, 01, 01);
    private LocalDate date3 = LocalDate.of(2019, 01, 01);

    @BeforeEach
    public void setUpPerTest() {
        noteRepository.deleteAll();
    }

    @Test
    @Tag("getAllPatientsNoteDto")
    @DisplayName("getAllPatientsNoteDto - OK - Existing patient's id")
    public void givenPatienWithTwoNotes_whenGetAllPatientsNotesDto_thenReturnTwoSizeList() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);
        Note note2 = new Note("TestNone", "Test", "a note text 2");
        noteRepository.save(note2);

        // WHEN
        List<NoteDto> result = noteService.getAllPatientsNoteDto("TestNone",
                "Test");

        // THEN
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getNote()).isEqualTo("a note text");
        assertThat(result.get(1).getNote()).isEqualTo("a note text 2");
    }

    @Test
    @Tag("getNote")
    @DisplayName("getNote - OK - Existing id")
    public void givenNote_whenGetById_thenReturnNote() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);
        String noteId = noteRepository.findAll().get(0).getId();

        // WHEN
        Note result = noteService.getNote(noteId);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getLastName()).isEqualTo("TestNone");
        assertThat(result.getFirstName()).isEqualTo("Test");
        assertThat(noteRepository.findAll().size()).isEqualTo(1);
        assertThat(noteRepository.findById(noteId).get().getNote())
                .isEqualTo("a note text");
    }

    @Test
    @Tag("getNote")
    @DisplayName("getNote - ERROR - Bad note's id")
    public void givenInvalidNoteId_whenGetById_thenReturnNull() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);
        String noteId = noteRepository.findAll().get(0).getId();

        // WHEN
        Note result = noteService.getNote("6006e");

        // THEN
        assertThat(result).isNull();
    }

    @Test
    @Tag("updateNote")
    @DisplayName("updateNote - OK")
    public void givenNote_whenUpdateWithValidId_thenReturnUpdated() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);
        String noteId = noteRepository.findAll().get(0).getId();
        Note noteToUpdate = new Note("TestNone", "Test", "NEW NOTE");

        // WHEN
        boolean result = noteService.updateNote(noteToUpdate, note.getId());

        // THEN
        assertThat(result).isTrue();
        assertThat(noteRepository.findAll().size()).isEqualTo(1);
        assertThat(noteRepository.findById(noteId).get().getNote())
                .isEqualTo("NEW NOTE");
    }

    @Test
    @Tag("updateNote")
    @DisplayName("updateNote - Error - More than 10 characters deleted")
    public void givenNote_whenUpdateWithInvalidId_thenReturnFalse() {
        // GIVEN
        Note note = new Note("TestNone", "Test",
                "a note text ------------------ other");
        noteRepository.save(note);
        String noteId = noteRepository.findAll().get(0).getId();
        Note noteToUpdate = new Note("TestNone", "Test", "NEW NOTE");

        // WHEN
        boolean result = noteService.updateNote(noteToUpdate, note.getId());

        // THEN
        assertThat(result).isFalse();
        assertThat(noteRepository.findAll().size()).isEqualTo(1);
        assertThat(noteRepository.findById(noteId).get().getNote())
                .isEqualTo("a note text ------------------ other");
    }

    @Test
    @Tag("getNote")
    @DisplayName("getNote - OK - Valid note's id")
    public void givenNote_whenGetWithHisId_thenReturnNote() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);
        assertThat(noteRepository.findAll().size()).isEqualTo(1);
        String noteId = noteRepository.findAll().get(0).getId();

        // WHEN
        Note result = noteService.getNote(noteId);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getLastName()).isEqualTo("TestNone");
        assertThat(noteRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @Tag("getNote")
    @DisplayName("getNote - ERROR - Invalid note's id")
    public void givenNote_whenGetWithBadId_thenReturnNote() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);
        String noteId = noteRepository.findAll().get(0).getId();
        assertThat(noteRepository.findAll().size()).isEqualTo(1);

        // WHEN
        Note result = noteService.getNote("6");

        // THEN
        assertThat(result).isNull();
    }

    @Test
    @Tag("addNote")
    @DisplayName("addNote - OK")
    public void givenExistingNote_whenAddNewValidNote_thenReturnOk() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);
        assertThat(noteRepository.findAll().size()).isEqualTo(1);

        Note newNote = new Note();
        newNote.setLastName("TestNone");
        newNote.setFirstName("Test");
        newNote.setNote("a new note text");

        // WHEN
        Note result = noteService.addNote(newNote);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getLastName()).isEqualTo("TestNone");
        assertThat(noteRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @Tag("addNote")
    @DisplayName("addNote - ERROR - Empty firstname")
    public void givenEmptyFirstName_whenAddNewValidNote_thenReturnOk() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);
        assertThat(noteRepository.findAll().size()).isEqualTo(1);

        Note newNote = new Note();
        newNote.setLastName("TestNone");
        newNote.setNote("a new note text");

        // WHEN
        Note result = noteService.addNote(newNote);

        // THEN
        assertThat(result).isNull();
        assertThat(noteRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @Tag("addNote")
    @DisplayName("addNote - ERROR - Empty patient lastname")
    public void givenEmptyLastname_whenAddNewValidNote_thenReturnOk() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);
        assertThat(noteRepository.findAll().size()).isEqualTo(1);

        Note newNote = new Note();
        newNote.setFirstName("Test");
        newNote.setNote("a new note text");

        // WHEN
        Note result = noteService.addNote(newNote);

        // THEN
        assertThat(result).isNull();
        assertThat(noteRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @Tag("addNote")
    @DisplayName("addNote - ERROR - Empty patient note text")
    public void givenEmptyNoteText_whenAddNewValidNote_thenReturnOk() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);
        assertThat(noteRepository.findAll().size()).isEqualTo(1);

        Note newNote = new Note();
        newNote.setLastName("TestNone");
        newNote.setFirstName("Test");

        // WHEN
        Note result = noteService.addNote(newNote);

        // THEN
        assertThat(result).isNull();
        assertThat(noteRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @Tag("getAllPatientNotes")
    @DisplayName("getAllPatientNotes - OK - Patient 2")
    public void givenThreeNotes_whenPatiensNotesForPatientTwo_thenReturnOne() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        Note note2 = new Note("TestBorderline", "Test", "a note text");
        Note note3 = new Note("TestInDanger", "Test", "a note text");
        noteRepository.save(note);
        noteRepository.save(note2);
        noteRepository.save(note3);

        // WHEN
        List<Note> result = noteService.getAllPatientNotes("TestBorderline",
                "Test");

        // THEN
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @Tag("getAllPatientNotes")
    @DisplayName("getAllPatientNotes - Error - Patient without notes")
    public void givenPatientWithoutNotes_whenGet_thenReturnException() {

        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> {
                    noteService.getAllPatientNotes("UNKNOW", "Test");
                });
    }

    @Test
    @Tag("getAllNotes")
    @DisplayName("getAllNotes - OK - 3")
    public void givenThreeNotes_whenGetAllNotes_thenReturnThree() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        Note note2 = new Note("TestBorderline", "Test", "a note text");
        Note note3 = new Note("TestInDanger", "Test", "a note text");
        noteRepository.save(note);
        noteRepository.save(note2);
        noteRepository.save(note3);

        // WHEN
        List<Note> result = noteService.getAllNotes();

        // THEN
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @Tag("getAllNotes")
    @DisplayName("getAllNotes - OK - 0")
    public void givenZeroNotes_whenGetAllNotes_thenReturnEmptyList() {
        // GIVEN

        // WHEN
        List<Note> result = noteService.getAllNotes();

        // THEN
        assertThat(result.size()).isEqualTo(0);
    }
}
