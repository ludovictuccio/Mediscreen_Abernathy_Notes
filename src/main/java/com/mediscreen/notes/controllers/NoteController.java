package com.mediscreen.notes.controllers;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mediscreen.notes.domain.Note;
import com.mediscreen.notes.services.NoteService;

import io.swagger.annotations.ApiOperation;

@Controller
public class NoteController {

    private static final Logger LOGGER = LogManager.getLogger("NoteController");

    @Autowired
    private NoteService noteService;

    @ApiOperation(value = "GET list of all patient's notes", notes = "THYMELEAF - Return response 200")
    @GetMapping("/note/list")
    public String list(@RequestParam("patId") final Long patId,
            final Model model) {
        model.addAttribute("notes", noteService.getAllPatientNotes(patId));
        return "note/list";
    }

    @ApiOperation(value = "GET patients list", notes = "THYMELEAF - Return to patient microservice - For search an other patient")
    @GetMapping("/patient/list")
    public ModelAndView returnAllPatientsList(final ModelMap model) {
        return new ModelAndView("redirect:http://localhost:8081/patient/list",
                model);
    }

//    @GetMapping("/patient/medicalRecord/{patId}")
//    public ModelAndView returnMedicalRecordPage(
//            @PathVariable("patId") final String patId, final ModelMap model) {
//        LOGGER.info("controller model and map");
//        model.addAttribute("patId", patId);
//        return new ModelAndView(
//                "redirect:http://localhost:8081/patient/medicalRecord", model);
//    }

    @ApiOperation(value = "ADD a Note (get)", notes = "THYMELEAF - Add new patient's note")
    @GetMapping("/note/add")
    public String addNote(final Model model) {
        model.addAttribute("note", new Note());
        return "note/add";
    }

    @ApiOperation(value = "VALIDATE add Note (post)", notes = "THYMELEAF - Validate - save/add the new patient's note")
    @PostMapping("/note/validate")
    public String validate(@Valid final Note note, final BindingResult result,
            final Model model) {

        if (!result.hasErrors()) {
            Note noteToAdd = noteService.addNote(note);
            if (noteToAdd != null) {
                model.addAttribute("note", noteToAdd);
                return "redirect:/note/list?patId=" + note.getPatId();
            } else {
                LOGGER.error("POST request FAILED for: /note/validate");
                LOGGER.error(
                        "The user id must be the good association with patient's lastName. Please check your entries");
                return "note/add";
            }
        }
        LOGGER.error("POST request FAILED for: /note/validate");
        return "note/add";
    }

}
