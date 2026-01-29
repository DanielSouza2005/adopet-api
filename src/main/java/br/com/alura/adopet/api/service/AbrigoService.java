package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.dto.DadosDetalhesAbrigo;
import br.com.alura.adopet.api.dto.DadosDetalhesPet;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.validation.abrigo.ValidacaoCadastroAbrigo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbrigoService {

    @Autowired
    private AbrigoRepository repository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    List<ValidacaoCadastroAbrigo> validacoesCadastroAbrigo;

    public List<DadosDetalhesAbrigo> listar() {
        return repository.findAll()
                .stream()
                .map(DadosDetalhesAbrigo::new)
                .toList();
    }

    public void cadastrar(CadastroAbrigoDto cadastroAbrigoDto) {
        validacoesCadastroAbrigo.forEach(v -> v.validar(cadastroAbrigoDto));

        Abrigo abrigo = new Abrigo();
        abrigo.setEmail(cadastroAbrigoDto.email());
        abrigo.setNome(cadastroAbrigoDto.nome());
        abrigo.setTelefone(cadastroAbrigoDto.telefone());
        repository.save(abrigo);
    }

    public List<DadosDetalhesPet> listarPets(String idOuNome) {
        try {
            Long id = Long.parseLong(idOuNome);
            return repository.getReferenceById(id).getPets()
                    .stream()
                    .map(DadosDetalhesPet::new)
                    .toList();
        } catch (NumberFormatException e) {
            return repository.findByNome(idOuNome).getPets()
                    .stream()
                    .map(DadosDetalhesPet::new)
                    .toList();
        }
    }

    public void cadastrarPet(String idOuNome, Long petId) {
        Pet pet = petRepository.getReferenceById(petId);

        try {
            Long id = Long.parseLong(idOuNome);
            Abrigo abrigo = repository.getReferenceById(id);
            pet.setAbrigo(abrigo);
            pet.setAdotado(false);
            abrigo.getPets().add(pet);
            repository.save(abrigo);
        } catch (NumberFormatException nfe) {
            Abrigo abrigo = repository.findByNome(idOuNome);
            pet.setAbrigo(abrigo);
            pet.setAdotado(false);
            abrigo.getPets().add(pet);
            repository.save(abrigo);
        }
    }
}
