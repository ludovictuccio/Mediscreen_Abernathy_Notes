package com.mediscreen.notes.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mediscreen.notes.domain.Note;
import com.mediscreen.notes.domain.dto.NoteDto;

@SpringBootTest
public class EntityToDtoConverterTest {

    @Autowired
    private EntityToDtoConverter entityToDtoConverter;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @Tag("convertNoteToDto")
    @DisplayName("convertNoteToDto - OK")
    public void givenNote_whenConvertToDto_thenReturnDto() {
        // GIVEN
        Note note = new Note();
        note.setNote("a note !");

        // WHEN
        NoteDto result = entityToDtoConverter.convertNoteToDto(note);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getNote()).isEqualTo("a note !");
    }

}