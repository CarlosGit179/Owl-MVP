package br.com.carlos.Owl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.carlos.Owl.entity.Loan;
import br.com.carlos.Owl.enums.LoanStatus;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByStudentRegistrationNumber(String registrationNumber);
    List<Loan> findByBookIsbn(String isbn);
    List<Loan> findByStatus(LoanStatus status);

    boolean existsByStudentRegistrationNumberAndStatus(String registrationNumber, LoanStatus status);
    Optional<Loan> findByStudentRegistrationNumberAndStatus(String registrationNumber, LoanStatus status);

}
