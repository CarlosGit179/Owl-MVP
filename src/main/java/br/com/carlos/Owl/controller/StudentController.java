package br.com.carlos.Owl.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.carlos.Owl.dto.infra.request.StudentRequest;
import br.com.carlos.Owl.dto.infra.response.StudentResponse;
import br.com.carlos.Owl.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Handles requests related to students.
 * <p>
 * Supports registering, updating, deleting, listing, and finding students.
 * </p>
 */
@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Tag(name = "Student", description = "API for managing students")
public class StudentController {

    /**
    * Student service.
    */
    private final StudentService studentService;

    /**
     * Registers a new student.
     *
     * @param request Student data to be registered.
     * @return The registered student.
     */
    @PostMapping
    @Operation(summary = "Register a new student", description = "Registers a new student in the library")
    public StudentResponse register(@RequestBody StudentRequest request) {
        return studentService.register(request);
    }

    /**
     * Updates an existing student.
     *
     * @param registrationNumber Registration number of the student to update.
     * @param request Updated student data.
     * @return The updated student.
     */
    @PutMapping("/{registrationNumber}")
    @Operation(summary = "Update student", description = "Updates the data of an existing student in the library")
    public StudentResponse update(@RequestBody StudentRequest request, @PathVariable String registrationNumber) {
        return studentService.update(registrationNumber, request);
    }

    /**
     * Deletes an existing student.
     *
     * @param registrationNumber Registration number of the student to delete.
     */
    @DeleteMapping("/{registrationNumber}")
    @Operation(summary = "Delete student", description = "Deletes an existing student from the library by registration number")
    public void delete(@PathVariable String registrationNumber) {
        studentService.delete(registrationNumber);
    }

    /**
     * Lists all students.
     *
     * @return A list of students.
     */
    @GetMapping
    @Operation(summary = "List all students", description = "Returns a list of all students registered in the library")
    public List<StudentResponse> list() {
        return studentService.list();
    }

    /**
     * Finds students by name.
     *
     * @param name Name of the student.
     * @return A list of students found.
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "Find students by name", description = "Finds students in the library by name")
    public List<StudentResponse> findByName(@PathVariable String name) {
        return studentService.findByName(name);
    }

    /**
     * Finds a student by registration number.
     *
     * @param registrationNumber Registration number of the student.
     * @return The student found.
     */
    @GetMapping("/registration-number/{registrationNumber}")
    @Operation(summary = "Find student by registration number", description = "Finds a student in the library by registration number")
    public StudentResponse findByRegistrationNumber(@PathVariable String registrationNumber) {
        return studentService.findByRegistrationNumber(registrationNumber);
    }

}
