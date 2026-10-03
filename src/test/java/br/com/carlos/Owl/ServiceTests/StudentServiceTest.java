package br.com.carlos.Owl.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.carlos.Owl.dto.infra.request.StudentRequest;
import br.com.carlos.Owl.dto.infra.response.StudentResponse;
import br.com.carlos.Owl.entity.Student;
import br.com.carlos.Owl.exception.Students.RegistrationNumberAlreadyExistsException;
import br.com.carlos.Owl.exception.Students.StudentHasActiveLoanException;
import br.com.carlos.Owl.exception.Students.StudentNotFoundException;
import br.com.carlos.Owl.repository.StudentRepository;
import br.com.carlos.Owl.service.LoanService;
import br.com.carlos.Owl.service.StudentService;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private LoanService loanService;

    //==================================================
    // TESTS - REGISTER
    //==================================================

    // Success

    @Test
    void registerStudentSuccessfully() {

        StudentRequest request = new StudentRequest("Carlos", "123456");

        when(studentRepository.existsByRegistrationNumber(request.registrationNumber())).thenReturn(false);

        Student savedStudent = new Student();
        savedStudent.setName("Carlos");
        savedStudent.setRegistrationNumber("123456");

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        StudentResponse result = studentService.register(request);

        assertNotNull(result);
        assertEquals("Carlos", result.name());
        assertEquals("123456", result.registrationNumber());

        verify(studentRepository).save(any(Student.class));
    }

    // Errors

    @Test
    void registerShouldThrowExceptionWhenRegistrationNumberAlreadyExists() {

        StudentRequest request = new StudentRequest("Carlos", "123456");

        when(studentRepository.existsByRegistrationNumber("123456")).thenReturn(true);

        RegistrationNumberAlreadyExistsException exception = assertThrows(
                RegistrationNumberAlreadyExistsException.class, () -> studentService.register(request));

        assertEquals("Registration number already exists.", exception.getMessage());
    }

    //==================================================
    // TESTS - LIST
    //==================================================

    // Success

    @Test
    void listStudentsSuccessfully() {

        Student student1 = new Student();
        student1.setName("João da Silva");
        student1.setRegistrationNumber("123456");

        Student student2 = new Student();
        student2.setName("Maria Souza");
        student2.setRegistrationNumber("654321");

        List<Student> students = List.of(student1, student2);

        when(studentRepository.findAll()).thenReturn(students);

        List<StudentResponse> result = studentService.list();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("João da Silva", result.get(0).name());
        assertEquals("Maria Souza", result.get(1).name());

        verify(studentRepository).findAll();
    }

    //==================================================
    // TESTS - UPDATE
    //==================================================

    // Success

    @Test
    void updateStudentSuccessfully() {

        Student student = new Student();
        student.setName("Carlos Emanoel");
        student.setRegistrationNumber("123456");

        StudentRequest updatedStudent = new StudentRequest("João da Silva", "654321");

        when(studentRepository.findByRegistrationNumber("654321")).thenReturn(student);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponse result = studentService.update("654321", updatedStudent);

        assertNotNull(result);
        assertEquals("João da Silva", result.name());
        assertEquals("654321", result.registrationNumber());

        verify(studentRepository).findByRegistrationNumber("654321");
        verify(studentRepository).save(any(Student.class));
    }

    // Errors

    @Test
    void updateShouldThrowExceptionWhenStudentIsNotFound() {

        StudentRequest updatedStudent = new StudentRequest("João da Silva", "789");

        when(studentRepository.findByRegistrationNumber("123")).thenReturn(null);

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class,
                () -> studentService.update("123", updatedStudent));

        assertEquals("Student not found.", exception.getMessage());

        verify(studentRepository).findByRegistrationNumber("123");
    }

    //==================================================
    // TESTS - DELETE
    //==================================================

    // Success

    @Test
    void deleteStudentSuccessfully() {

        when(studentRepository.existsByRegistrationNumber("123456")).thenReturn(true);

        when(loanService.hasActiveLoan("123456")).thenReturn(false);

        studentService.delete("123456");

        verify(studentRepository).existsByRegistrationNumber("123456");
        verify(loanService).hasActiveLoan("123456");
        verify(studentRepository).deleteByRegistrationNumber("123456");
    }

    // Errors

    @Test
    void deleteShouldThrowExceptionWhenStudentIsNotFound() {

        when(studentRepository.existsByRegistrationNumber("123456")).thenReturn(false);

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class,
                () -> studentService.delete("123456"));

        assertEquals("Student not found.", exception.getMessage());

        verify(studentRepository).existsByRegistrationNumber("123456");
    }

    @Test
    void deleteShouldThrowExceptionWhenStudentHasActiveLoan() {

        when(studentRepository.existsByRegistrationNumber("123456")).thenReturn(true);

        when(loanService.hasActiveLoan("123456")).thenReturn(true);

        StudentHasActiveLoanException exception = assertThrows(StudentHasActiveLoanException.class,
                () -> studentService.delete("123456"));

        assertEquals("The student already has an active Loan.", exception.getMessage());

        verify(studentRepository).existsByRegistrationNumber("123456");

        verify(loanService).hasActiveLoan("123456");

        verify(studentRepository, never()).deleteByRegistrationNumber("123456");
    }

    //==================================================
    // TESTS - FIND BY REGISTRATION NUMBER
    //==================================================

    // Success

    @Test
    void findStudentByRegistrationNumberSuccessfully() {

        Student student = new Student();
        student.setName("João da Silva");
        student.setRegistrationNumber("123456");

        when(studentRepository.findByRegistrationNumber("123456")).thenReturn(student);

        StudentResponse result = studentService.findByRegistrationNumber("123456");

        assertNotNull(result);
        assertEquals("João da Silva", result.name());
        assertEquals("123456", result.registrationNumber());

        verify(studentRepository).findByRegistrationNumber("123456");
    }

    // Errors

    @Test
    void findStudentByRegistrationNumberShouldThrowExceptionWhenNotFound() {

        when(studentRepository.findByRegistrationNumber("123")).thenReturn(null);

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class,
                () -> studentService.findByRegistrationNumber("123"));

        assertEquals("Student not found.", exception.getMessage());

        verify(studentRepository).findByRegistrationNumber("123");
    }

    //==================================================
    // TESTS - FIND BY NAME
    //==================================================

    // Success

    @Test
    void findStudentByNameSuccessfully() {

        Student student = new Student();
        student.setName("Maria da Silva");
        student.setRegistrationNumber("123");

        when(studentRepository.findByNameContainingIgnoreCase("maria")).thenReturn(List.of(student));

        List<StudentResponse> result = studentService.findByName("maria");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Maria da Silva", result.get(0).name());
        assertEquals("123", result.get(0).registrationNumber());

        verify(studentRepository).findByNameContainingIgnoreCase("maria");
    }
}