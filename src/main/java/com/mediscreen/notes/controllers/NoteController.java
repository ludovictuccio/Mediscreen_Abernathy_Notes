package com.mediscreen.notes.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mediscreen.notes.services.NoteService;

import io.swagger.annotations.ApiOperation;

@Controller
//@Validated
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

}
