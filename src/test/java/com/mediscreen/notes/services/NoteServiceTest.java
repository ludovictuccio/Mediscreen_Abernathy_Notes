package com.mediscreen.notes.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mediscreen.notes.controllers.exceptions.DataNotFoundException;
import com.mediscreen.notes.domain.Note;
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

    @Test
    @Tag("getAllPatientNotes")
    @DisplayName("getAllPatientNotes - OK - Patient 2")
    public void givenThreeNotes_whenPatiensNotesForPatientTwo_thenReturnOne() {
        // GIVEN
        Note note = new Note("6006e44ba2c25a63e0623b30", date1, date1, 1L,
                "TestNone", "a note text");
        Note note2 = new Note("546465456451", date2, date2, 2L,
                "TestBorderline", "a note text");
        Note note3 = new Note("4568468451656", date3, date3, 3L, "TestInDanger",
                "a note text");
        noteRepository.save(note);
        noteRepository.save(note2);
        noteRepository.save(note3);

        // WHEN
        List<Note> result = noteService.getAllPatientNotes(2L);

        // THEN
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @Tag("getAllPatientNotes")
    @DisplayName("getAllPatientNotes - Error - Patient without notes")
    public void givenPatientWithoutNotes_whenGet_thenReturnException() {

        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> {
                    noteService.getAllPatientNotes(2L);
                });
    }

    @Test
    @Tag("getAllNotes")
    @DisplayName("getAllNotes - OK - 3")
    public void givenThreeNotes_whenGetAllNotes_thenReturnThree() {
        // GIVEN
        Note note = new Note("6006e44ba2c25a63e0623b30", date1, date1, 1L,
                "TestNone", "a note text");
        Note note2 = new Note("546465456451", date2, date2, 2L,
                "TestBorderline", "a note text");
        Note note3 = new Note("4568468451656", date3, date3, 3L, "TestInDanger",
                "a note text");
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
