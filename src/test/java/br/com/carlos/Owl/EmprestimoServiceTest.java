package br.com.carlos.Owl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.carlos.Owl.entity.Emprestimo;
import br.com.carlos.Owl.repository.EmprestimoRepository;
import br.com.carlos.Owl.service.EmprestimoService;

@ExtendWith(MockitoExtension.class)
public class EmprestimoServiceTest {

    @InjectMocks
    private EmprestimoService emprestimoService;

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Test
    void cadastrarEmprestimoComSucesso() {

        Emprestimo emprestimo = new Emprestimo();

        
        
    }

}
