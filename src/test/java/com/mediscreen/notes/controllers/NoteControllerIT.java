package com.mediscreen.notes.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.notes.domain.Note;
import com.mediscreen.notes.repository.NoteRepository;
import com.mediscreen.notes.services.NoteService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NoteControllerIT {

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

    private static final String URI_GET_PATIENT_NOTES_LIST = "/note/list";
    private static final String URI_GET_REDIRECT_PATIENT_LIST = "/patient/list";
    private static final String URI_GET_ADD_NOTE = "/note/add";
    private static final String URI_POST_ADD_VALIDATE = "/note/validate";
    private static final String URI_UPDATE = "/note/update";

    @BeforeEach
    public void setUpPerTest() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        noteRepository.deleteAll();
    }

    @Test
    @Tag("/note/update")
    @DisplayName("Get - Update - OK")
    public void givenNote_whenUpdateWithHisId_thenReturnOk() throws Exception {
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);
        String noteId = noteRepository.findAll().get(0).getId();
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI_UPDATE + "/" + noteId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(
                        MockMvcResultMatchers.model().attributeExists("note"))
                .andExpect(MockMvcResultMatchers.view().name("note/update"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    @Tag("/note/update")
    @DisplayName("Get - Update - Error - Bad id")
    public void givenNote_whenUpdateWithInvalidId_thenReturnBadRequest()
            throws Exception {
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI_UPDATE + "/badId50"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/note/list"))
                .andReturn();
    }

    @Test
    @Tag("/note/add")
    @DisplayName("Get - Add  note- OK")
    public void givenGetMapping_whenAddNote_thenReturnOk() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI_GET_ADD_NOTE)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andReturn();
    }

    @Test
    @Tag("/note/validate")
    @DisplayName("Post - Validate / Add - OK")
    public void givenPatient_whenPostValidate_thenReturnOk() throws Exception {
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);

        Note note2 = new Note("TestNone", "Test", "a note text NEW !");
        String jsonContent = objectMapper.writeValueAsString(note2);
        this.mockMvc
                .perform(MockMvcRequestBuilders.post(URI_POST_ADD_VALIDATE)
                        .contentType(MediaType.ALL).content(jsonContent))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Tag("/note/list")
    @DisplayName("Get - OK - Notes list - Existing note with patId")
    public void givenNoteWithPatId_whenGetIt_thenReturnOk() throws Exception {
        Note note = new Note("TestNone", "Test", "a note text");
        noteRepository.save(note);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI_GET_PATIENT_NOTES_LIST)
                        .contentType(APPLICATION_JSON)
                        .param("lastName", "TestNone")
                        .param("firstName", "Test"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andReturn();
    }

    @Test
    @Tag("/note/list")
    @DisplayName("Get - Error 404 - Notes list - Non-existant note with patId")
    public void givenNonExistantNoteWithPatIdTwo_whenGet_thenReturnNotFound()
            throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI_GET_PATIENT_NOTES_LIST)
                        .contentType(APPLICATION_JSON)
                        .param("lastName", "TestNone")
                        .param("firstName", "Test"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @Tag("/patient/list")
    @DisplayName("Redirection to patient microservice - OK")
    public void redirectionToPatientsList() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get(URI_GET_REDIRECT_PATIENT_LIST))
                .andExpect(status().is3xxRedirection());

    }
}
