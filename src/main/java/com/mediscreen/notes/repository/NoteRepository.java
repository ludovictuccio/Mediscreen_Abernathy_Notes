package com.mediscreen.notes.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mediscreen.notes.domain.Note;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findByPatIdOrderByCreationDateDesc(Long patId);

    List<Note> findByLastNameAndFirstName(String lastName, String firstName);

}
