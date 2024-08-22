package com.medicoLaboSolutions.microservice_note.unit_test.repositories;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicoLaboSolutions.microservice_note.model.Note;
import com.medicoLaboSolutions.microservice_note.repository.NoteRepository;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteTests {

	@Autowired
	private NoteRepository noteRepository;

	private Note noteTest;

	private ObjectMapper mapper = new ObjectMapper();

	private final String FILE_NAME = "src/test/resources/data.json";

	@BeforeAll
	public void setupDataBase() {
		try {
			TypeReference<List<Note>> jacksonTypeReference = new TypeReference<List<Note>>() {};
			String dataJSONToString = FileUtils.readFileToString(new File(FILE_NAME), StandardCharsets.UTF_8);
			List<Note> jacksonList = mapper.readValue(dataJSONToString, jacksonTypeReference);

			noteRepository.insert(jacksonList);

		} catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

	@AfterAll
	public void resetDataBase(){
		noteRepository.deleteAll();
	}
	@BeforeEach
	public void setupTestData(){
		// Given : Setup object for test
		noteTest = new Note("4", "TestEarlyOnset", "note content test");
	}

	@Test
	@DisplayName("Test that saves a note")
	public void givenNoteObject_whenInsert_thenReturnSavedNote(@Autowired MongoTemplate mongoTemplate) {
		// ACT
		Note savedNote = noteRepository.insert(noteTest);

		//ASSERT
		assertThat(savedNote).isNotNull();
		assertThat(savedNote.getPatId()).isEqualTo("4");

	}

	@Test
	@DisplayName("Test that gets a list of notes")
	public void givenPatientId_whenFindByPatId_thenReturnListOfNoteLinkedToThePatient(@Autowired MongoTemplate mongoTemplate){
		//ARRANGE
		String patientTestId = "4";
		// ACT
		List<Note> notesOfPatient = noteRepository.findByPatId(patientTestId);

		// ASSERT
		assertThat(notesOfPatient).isNotEmpty();
		assertThat(notesOfPatient.size()).isBetween(4,5);
	}
}
