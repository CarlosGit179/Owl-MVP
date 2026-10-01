package br.com.carlos.Owl.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.carlos.Owl.dto.infra.request.StudentRequest;
import br.com.carlos.Owl.dto.infra.response.StudentResponse;
import br.com.carlos.Owl.entity.Student;
import br.com.carlos.Owl.exception.Students.RegistrationNumberAlreadyExistsException;
import br.com.carlos.Owl.exception.Students.StudentHasActiveLoanException;
import br.com.carlos.Owl.exception.Students.StudentNotFoundException;
import br.com.carlos.Owl.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Applies business rules for student operations.
 * <p>
 * Supports registering, listing, finding, updating, and deleting students.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class StudentService {

    /**
    * Student repository.
    */
    private final StudentRepository studentRepository;

    private final LoanService loanService;

    private StudentResponse toStudentResponse(Student student) {
        return new StudentResponse(student.getName(), student.getRegistrationNumber());
    }

    /**
     * Registers a new student.
     *
     * @param request Student data to be registered.
     * @return The registered student.
     * @throws RegistrationNumberAlreadyExistsException If the registration number is already registered.
     */
    public StudentResponse register(StudentRequest request) {

        if (studentRepository.existsByRegistrationNumber(request.registrationNumber())) {
            throw new RegistrationNumberAlreadyExistsException();
        }

        Student student = new Student();
        student.setName(request.name());
        student.setRegistrationNumber(request.registrationNumber());

        return toStudentResponse(studentRepository.save(student));
    }

    /**
     * Lists all students.
     *
     * @return A list of students.
     */
    public List<StudentResponse> list() {
        return studentRepository.findAll().stream().map(this::toStudentResponse).toList();
    }

    /**
     * Updates an existing student.
     *
     * @param registrationNumber Registration number of the student to update.
     * @param request Updated student data.
     * @return The updated student.
     * @throws StudentNotFoundException If the student is not found.
     */
    public StudentResponse update(String registrationNumber, StudentRequest request) {

        Student student = studentRepository.findByRegistrationNumber(registrationNumber);

        if (student == null) {
            throw new StudentNotFoundException();
        }

        student.setName(request.name());
        student.setRegistrationNumber(request.registrationNumber());

        return toStudentResponse(studentRepository.save(student));
    }

    /**
     * Deletes an existing student.
     *
     * @param registrationNumber Registration number of the student to delete.
     * @throws StudentNotFoundException If the student is not found.
     * @throws StudentHasActiveLoanException If the student has an active loan.
     */
    @Transactional 
    public void delete(String registrationNumber) {

        if (!studentRepository.existsByRegistrationNumber(registrationNumber)) {
            throw new StudentNotFoundException();
        }

        if (loanService.hasActiveLoan(registrationNumber)) {
            throw new StudentHasActiveLoanException();
        }

        studentRepository.deleteByRegistrationNumber(registrationNumber);
    }

    /**
     * Finds a student by registration number.
     *
     * @param registrationNumber Registration number of the student.
     * @return The student found.
     * @throws StudentNotFoundException If the student is not found.
     */
    public StudentResponse findByRegistrationNumber(String registrationNumber) {

        Student student = studentRepository.findByRegistrationNumber(registrationNumber);

        if (student == null) {
            throw new StudentNotFoundException();
        }

        return toStudentResponse(student);
    }

    /**
     * Finds students by name.
     *
     * @param name Name of the student.
     * @return A list of students found.
     */
    public List<StudentResponse> findByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name).stream().map(this::toStudentResponse).toList();
    }

}
