package com.mediscreen.notes.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.mediscreen.notes.domain.Note;
import com.mediscreen.notes.repository.NoteRepository;

/**
 * This class is used to valid constraints entities.
 *
 * @author Ludovic Tuccio
 */
public class ConstraintsValidator {

    private static final Logger LOGGER = LogManager
            .getLogger("ConstraintsValidator");

    @Autowired
    private NoteRepository noteRepository;

    /**
     * Method used to validate Note constraints.
     *
     * @param note
     */
    public static Note checkValidPatient(final Note note) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Note>> constraintViolations = validator
                .validate(note);
        if (constraintViolations.size() > 0) {
            LOGGER.error(
                    "ERROR: a constraint was violated. Please check the informations entered.");
            for (ConstraintViolation<Note> contraintes : constraintViolations) {
                LOGGER.error(contraintes.getRootBeanClass().getSimpleName()
                        + "." + contraintes.getPropertyPath() + " "
                        + contraintes.getMessage());
            }
            return null;
        }
        return note;
    }

    /**
     * Method used to check if the association between patient's id and his
     * lastname entered is correct.
     *
     * @param patId
     * @param lastName
     * @return boolean isExistingPatient
     */
    public boolean checkExistingPatient(final Long patId,
            final String lastName) {
        boolean isExistingPatient = false;

        Note note = noteRepository.findFirstNoteByPatId(patId);
        if (note == null) {
            LOGGER.info("Please check existing patient id");
            return isExistingPatient;
        }
        if (!note.getPatientLastname().toUpperCase()
                .equals(lastName.toUpperCase())) {
            return isExistingPatient;
        }
        isExistingPatient = true;
        return isExistingPatient;
    }

}
