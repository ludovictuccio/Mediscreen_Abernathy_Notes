package com.mediscreen.notes.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mediscreen.notes.domain.Note;

@SpringBootTest
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository historyRepository;

    private LocalDate date1 = LocalDate.of(2021, 01, 01);
    private LocalDate date2 = LocalDate.of(2020, 01, 01);
    private LocalDate date3 = LocalDate.of(2019, 01, 01);

    @BeforeEach
    public void setUpPerTest() {
        historyRepository.deleteAll();
    }

    @Test
    @Tag("findAll")
    @DisplayName("findAll - OK")
    public void givenThreeNotes_whenFindAll_thenReturnThree() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        Note note2 = new Note("TestBorderline", "Test", "a note text");
        Note note3 = new Note("TestInDanger", "Test", "a note text");
        historyRepository.save(note);
        historyRepository.save(note2);
        historyRepository.save(note3);

        // WHEN
        List<Note> result = historyRepository.findAll();

        // THEN
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @Tag("findByPatIdOrderByCreationDateDesc")
    @DisplayName("findByPatIdOrderByCreationDateDesc - OK")
    public void givenThreeNotes_whenFindByPatIdTwo_thenReturnOne() {
        // GIVEN
        Note note = new Note("TestNone", "Test", "a note text");
        Note note2 = new Note("TestBorderline", "Test", "a note text");
        note2.setPatId(2L);
        Note note3 = new Note("TestInDanger", "Test", "a note text");
        historyRepository.save(note);
        historyRepository.save(note2);
        historyRepository.save(note3);

        // WHEN
        List<Note> result = historyRepository
                .findByPatIdOrderByCreationDateDesc(2L);

        // THEN
        assertThat(result.size()).isEqualTo(1);
    }

}
