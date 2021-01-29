package com.mediscreen.notes.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Note model class.
 *
 * @author Ludovic Tuccio
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "notes")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field(value = "creationDate")
    private LocalDate creationDate;

    @Field(value = "patId")
    private Long patId;

    @NotBlank(message = "Patient lastname can't be empty")
    @Field(value = "LastName")
    private String lastName;

    @NotBlank(message = "Patient firstName can't be empty")
    @Field(value = "FirstName")
    private String firstName;

    @NotBlank(message = "Text can't be empty")
    @Field(value = "Notes")
    private String note;

    public Note(
            @NotBlank(message = "Patient lastname can't be empty") final String plastName,
            @NotBlank(message = "Patient firstName can't be empty") final String pfirstName,
            @NotBlank(message = "Text can't be empty") final String pnote) {
        super();
        this.lastName = plastName;
        this.firstName = pfirstName;
        this.note = pnote;
    }

}
