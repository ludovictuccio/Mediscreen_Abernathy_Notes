package com.mediscreen.notes.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mediscreen.notes.domain.Note;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    // @Query("SELECT n FROM Note n WHERE n.patId = ? ORDER BY DESC
    // creationDate")
    List<Note> findByPatIdOrderByCreationDateDesc(final Long patId);

    Note findFirstNoteByPatId(final Long patId);

}
