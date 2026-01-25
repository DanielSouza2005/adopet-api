package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.dto.DadosDetalhesAbrigo;
import br.com.alura.adopet.api.dto.DadosDetalhesPet;
import br.com.alura.adopet.api.exception.CadastrarAbrigoValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbrigoService {

    @Autowired
    private AbrigoRepository repository;

    @Autowired
    private PetRepository petRepository;

    public List<DadosDetalhesAbrigo> listar() {
        return repository.findAll()
                .stream()
                .map(DadosDetalhesAbrigo::new)
                .toList();
    }

    public void cadastrar(CadastroAbrigoDto cadastroAbrigoDto) {
        boolean nomeJaCadastrado = repository.existsByNome(cadastroAbrigoDto.nome());
        boolean telefoneJaCadastrado = repository.existsByTelefone(cadastroAbrigoDto.telefone());
        boolean emailJaCadastrado = repository.existsByEmail(cadastroAbrigoDto.email());

        if (nomeJaCadastrado) {
            throw new CadastrarAbrigoValidacaoException("Abrigo já cadastrado com esse nome.");
        }

        if (telefoneJaCadastrado) {
            throw new CadastrarAbrigoValidacaoException("Abrigo já cadastrado com esse telefone.");
        }

        if (emailJaCadastrado) {
            throw new CadastrarAbrigoValidacaoException("Abrigo já cadastrado com esse email.");
        }

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

    public void cadastrarPet(String idOuNome, CadastroPetDto cadastroPetDto) {
        Pet pet = petRepository.getReferenceById(cadastroPetDto.idPet());

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
