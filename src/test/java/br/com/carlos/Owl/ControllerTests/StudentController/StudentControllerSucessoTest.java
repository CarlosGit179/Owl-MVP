package br.com.carlos.Owl.ControllerTests.StudentController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.carlos.Owl.controller.StudentController;
import br.com.carlos.Owl.dto.infra.request.StudentRequest;
import br.com.carlos.Owl.dto.infra.response.StudentResponse;
import br.com.carlos.Owl.repository.UserRepository;
import br.com.carlos.Owl.security.TokenService;
import br.com.carlos.Owl.service.StudentService;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StudentControllerSucessoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    //==================================================
    // TESTS - REGISTER
    //==================================================

    // Success
    @Test
    void registerStudentSuccessfully() throws Exception {

        StudentResponse response = new StudentResponse("Carlos", "123456");

        when(studentService.register(any(StudentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON).content("""
                {
                    "name": "Carlos",
                    "registrationNumber": "123456"
                }
                """)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Carlos"))
                .andExpect(jsonPath("$.registrationNumber").value("123456"));

        verify(studentService).register(any(StudentRequest.class));
    }

    //==================================================
    // TESTS - UPDATE
    //==================================================

    // Success
    @Test
    void updateStudentSuccessfully() throws Exception {

        StudentResponse response = new StudentResponse("Carlos", "123456");

        when(studentService.update(eq("123456"), any(StudentRequest.class))).thenReturn(response);

        mockMvc.perform(put("/students/123456").contentType(MediaType.APPLICATION_JSON).content("""
                {
                    "name": "Carlos",
                    "registrationNumber": "123456"
                }
                """)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Carlos"))
                .andExpect(jsonPath("$.registrationNumber").value("123456"));

        verify(studentService).update(eq("123456"), any(StudentRequest.class));
    }

    //==================================================
    // TESTS - DELETE
    //==================================================

    // Success
    @Test
    void deleteStudentSuccessfully() throws Exception {

        mockMvc.perform(delete("/students/123456")).andExpect(status().isOk()).andExpect(content().string(""));

        verify(studentService).delete("123456");
    }

    //==================================================
    // TESTS - LIST
    //==================================================

    // Success
    @Test
    void listStudentsSuccessfully() throws Exception {

        StudentResponse student1 = new StudentResponse("Carlos", "123456");

        StudentResponse student2 = new StudentResponse("Maria", "654321");

        List<StudentResponse> students = List.of(student1, student2);

        when(studentService.list()).thenReturn(students);

        mockMvc.perform(get("/students")).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Carlos"))
                .andExpect(jsonPath("$[0].registrationNumber").value("123456"))
                .andExpect(jsonPath("$[1].name").value("Maria"))
                .andExpect(jsonPath("$[1].registrationNumber").value("654321"));

        verify(studentService).list();
    }

    //==================================================
    // TESTS - FIND BY NAME
    //==================================================

    // Success
    @Test
    void findStudentByNameSuccessfully() throws Exception {

        StudentResponse student = new StudentResponse("Carlos", "123456");

        List<StudentResponse> students = List.of(student);

        when(studentService.findByName("Carlos")).thenReturn(students);

        mockMvc.perform(get("/students/name/Carlos")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Carlos"))
                .andExpect(jsonPath("$[0].registrationNumber").value("123456"));

        verify(studentService).findByName("Carlos");
    }

    //==================================================
    // TESTS - FIND BY REGISTRATION NUMBER
    //==================================================

    // Success
    @Test
    void findStudentByRegistrationNumberSuccessfully() throws Exception {

        StudentResponse student = new StudentResponse("Carlos", "123456");

        when(studentService.findByRegistrationNumber("123456")).thenReturn(student);

        mockMvc.perform(get("/students/registration-number/123456")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carlos"))
                .andExpect(jsonPath("$.registrationNumber").value("123456"));

        verify(studentService).findByRegistrationNumber("123456");
    }
}