package com.mediscreen.notes.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotNull(message = "Patient Id can't be empty")
    @Field(value = "patId")
    private Long patId;

    @NotBlank(message = "Patient lastname can't be empty")
    @Field(value = "patientLastname")
    private String patientLastname;

    @NotBlank(message = "Text can't be empty")
    @Field(value = "note")
    private String note;

}
