package com.medicoLaboSolutions.microservice_note.unit_test.services;

import com.medicoLaboSolutions.microservice_note.exceptions.NoteNotFoundException;
import com.medicoLaboSolutions.microservice_note.model.Note;
import com.medicoLaboSolutions.microservice_note.repository.NoteRepository;
import com.medicoLaboSolutions.microservice_note.service.NoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class NoteServiceTests {

    @Autowired
    private NoteService noteService;
    @MockBean
    private NoteRepository noteRepository;

    @Test
    public void givenNoteService_whenFindPatientListOfNotes_thenCorrectMethodMustBeCalled (){
        //ARRANGE
        String patientId = "4";
        List<Note> mockResult = new ArrayList<>();
        mockResult.add(new Note("4", "TestEarlyOnset","contentTest"));

        when(noteRepository.findByPatId(any(String.class))).thenReturn(mockResult);

        //ACT
        List<Note> result = noteService.findPatientListOfNotes(patientId);

        //ASSERT
        assertThat(result.size()).isEqualTo(1);
        verify(noteRepository, times(1)).findByPatId(anyString());

    }

    @Test
    public void givenEmptyListNote_whenFindPatientListOfNotes_thenReturnCorrectException (){
        //ARRANGE
        String patientId = "4";
        List<Note> mockResult = new ArrayList<>();

        String expectedMessage = "Invalid Note : No note exists with the Id 4. Please repeat your request with another id";

        when(noteRepository.findByPatId(any(String.class))).thenReturn(mockResult);

        //ACT
        Exception exception = assertThrows(NoteNotFoundException.class, () -> {noteService.findPatientListOfNotes(patientId);});

        //ASSERT
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    public void givenNoteService_whenAddNewNote_thenCorrectMethodMustBeCalled (){
        //ARRANGE
        Note noteTest = new Note("4", "TestEarlyOnset","contentTest");

        when(noteRepository.insert(any(Note.class))).thenReturn(noteTest);

        //ACT
        Note result = noteService.addNewNote(noteTest);

        //ASSERT
        assertThat(result.getNote()).isEqualTo("contentTest");
        verify(noteRepository, times(1)).insert(any(Note.class));
    }
}
