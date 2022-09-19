package com.mediscreen.notes.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mediscreen.notes.domain.Note;
import com.mediscreen.notes.domain.dto.NoteDto;

@Component
public class EntityToDtoConverter {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Method used to convert Note entity to Note DTO.
     *
     * @param note
     * @return patientDto
     */
    public NoteDto convertNoteToDto(final Note note) {
        NoteDto noteDto = modelMapper.map(note, NoteDto.class);
        noteDto.setNote(note.getNote());
        return noteDto;
    }
}
