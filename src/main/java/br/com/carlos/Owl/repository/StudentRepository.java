package br.com.carlos.Owl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.carlos.Owl.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByRegistrationNumber(String registrationNumber);
    Student findByName(String name);

    boolean existsByName(String name);
    boolean existsByRegistrationNumber(String registrationNumber);

    void deleteByRegistrationNumber(String registrationNumber);

    List<Student> findByNameContainingIgnoreCase(String name);
}
