package com.mediscreen.notes.controllers.api;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.notes.domain.Note;
import com.mediscreen.notes.repository.NoteRepository;
import com.mediscreen.notes.services.NoteService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NoteControllerApiRestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private LocalDate date1 = LocalDate.of(2021, 01, 01);

    private static final String API_URI_GET_ALL_NOTES = "/api/note/all";
    private static final String API_URI_BASE = "/api/note";
    private static final String API_URI_GET_ALL_NOTESDTO = "/api/note/getAllPatientsNoteDto/";

    @BeforeEach
    public void setUpPerTest() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        noteRepository.deleteAll();
    }

    @Test
    @Tag("/api/note/getAllPatientsNoteDto/{patId}")
    @DisplayName("GET all patient's notes DTO - OK 200")
    public void givenNote_whenGetNoteDto_thenReturnOk() throws Exception {
        Note note = new Note();
        note.setPatId(1L);
        noteRepository.save(note);
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get(API_URI_GET_ALL_NOTESDTO + "1").param("patId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Tag("/api/note/getAllPatientsNoteDto/{patId}")
    @DisplayName("GET all patient's notes DTO - ERROR 404")
    public void givenNote_whenGetNoteDtoWithBadId_thenReturnNotFound()
            throws Exception {
        Note note = new Note();
        note.setPatId(1L);
        noteRepository.save(note);
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get(API_URI_GET_ALL_NOTESDTO + "1111")
                        .param("patId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @Tag("/api/note")
    @DisplayName("POST - Add new note - OK - 201")
    public void givenNote_whenAddNewValidNote_thenReturnCreated()
            throws Exception {
        Note note = new Note("6006e44ba2c25a63e0623b30", date1, 1L, "TestNone",
                "a note text");
        noteRepository.save(note);
        this.mockMvc.perform(MockMvcRequestBuilders.post(API_URI_BASE)
                .contentType(MediaType.APPLICATION_JSON).content(
                        "{ \"patId\": 1, \"patientLastname\": \"TestNone\",\"note\": \"A new note text !\"}"))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated()).andReturn();
    }

    @Test
    @Tag("/api/note")
    @DisplayName("POST - Add new note - Error - 400")
    public void givenNote_whenAddInvalidNote_thenReturnBadRequest()
            throws Exception {
        Note note = new Note("6006e44ba2c25a63e0623b30", date1, 1L, "TestNone",
                "a note text");
        noteRepository.save(note);
        this.mockMvc.perform(MockMvcRequestBuilders.post(API_URI_BASE)
                .contentType(MediaType.APPLICATION_JSON).content(
                        "{\"patId\": 4, \"patientLastname\": \"\",\"note\": \"A new note text !\"}"))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @Tag("/api/note/all")
    @DisplayName("GET all notes - OK - 200")
    public void givenZeroNotes_whenGetList_thenReturnOk() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(API_URI_GET_ALL_NOTES)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Tag("/api/note")
    @DisplayName("GET all patient's notes - OK - 200")
    public void givenOneNote_whenGet_thenReturnOk() throws Exception {
        Note note = new Note();
        note.setPatId(1L);
        noteRepository.save(note);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(API_URI_BASE)
                        .param("patId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Tag("/api/note")
    @DisplayName("GET all patient's notes - Error 404")
    public void givenZeroNotes_whenGetPatientsNotes_thenReturnNotfound()
            throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(API_URI_BASE)
                        .param("patId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @Tag("PUT")
    @DisplayName("PUT Update note - OK - 200")
    public void givenNote_whenUpdate_thenReturnOK() throws Exception {
        Note note = new Note("6006e44ba2c25a63e0623b30", date1, 1L, "TestNone",
                "a note text");
        noteRepository.save(note);

        Note noteToUpdate = new Note("6006e44ba2c25a63e0623b30", date1, 1L,
                "TestNone", "a note text - A NEW NOTE TEXT");
        String jsonContent = objectMapper.writeValueAsString(noteToUpdate);
        this.mockMvc
                .perform(MockMvcRequestBuilders.put(API_URI_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .param("id", "6006e44ba2c25a63e0623b30"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Tag("PUT")
    @DisplayName("PUT Update note - ERROR 400 - Bad id")
    public void givenNote_whenUpdateWithBadId_thenReturnBadRequest()
            throws Exception {
        Note note = new Note("6006e44ba2c25a63e0623b30", date1, 1L, "TestNone",
                "a note text");
        noteRepository.save(note);

        Note noteToUpdate = new Note("6006e44ba2c25a63e0623b30", date1, 1L,
                "TestNone", "a note text - A NEW NOTE TEXT");
        String jsonContent = objectMapper.writeValueAsString(noteToUpdate);
        this.mockMvc
                .perform(MockMvcRequestBuilders.put(API_URI_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent).param("id", "badId"))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @Tag("PUT")
    @DisplayName("PUT Update note - ERROR 400 - Text deleted")
    public void givenNote_whenUpdateWithBadTextNote_thenReturnBadRequest()
            throws Exception {
        Note note = new Note("6006e44ba2c25a63e0623b30", date1, 1L, "TestNone",
                "a note text");
        noteRepository.save(note);

        Note noteToUpdate = new Note("6006e44ba2c25a63e0623b30", date1, 1L,
                "TestNone", "");
        String jsonContent = objectMapper.writeValueAsString(noteToUpdate);
        this.mockMvc
                .perform(MockMvcRequestBuilders.put(API_URI_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .param("id", "6006e44ba2c25a63e0623b30"))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest()).andReturn();
    }
}
