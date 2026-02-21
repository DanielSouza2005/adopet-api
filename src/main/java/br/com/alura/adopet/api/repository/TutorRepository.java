package br.com.alura.adopet.api.repository;

import br.com.alura.adopet.api.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    boolean existsByTelefoneOrEmail(String telefone, String email);
    boolean existsByTelefoneAndIdNot(String telefone, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

}
