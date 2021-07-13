package br.com.rafaelmattos.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.rafaelmattos.cursomc.domain.Categoria;
import br.com.rafaelmattos.cursomc.dto.CategoriaDTO;
import br.com.rafaelmattos.cursomc.repositories.CategoriaRepository;
import br.com.rafaelmattos.cursomc.services.exceptions.DataIntegrityException;
import br.com.rafaelmattos.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired // instanciar o repositorio
	private CategoriaRepository repo;

	// buscar no banco de dados com base no id //1
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		//Um função que estância uma exceção (utilizou uma expressão lambda)
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	// Inserir //2
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	// Atualizar //3
	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repo.save(obj);
	}
	
	// Deletar //4
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria que possui produtos");
		}
	}
	
	//Listar categoria //5
	public List<Categoria> findAll() {
		return repo.findAll();
	}
	
	//Paginação //6
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	//Metodo auxiliar que apartir de uma Categoria instancia o DTO
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
	
//	private void updateData (Categoria newObj, Categoria obj) {
//		newObj.setNome(obj.getNome());
//	}
	
}
