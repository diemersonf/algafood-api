package com.diemerson.mobilefood.api.controller;

import java.util.List;

import com.diemerson.mobilefood.api.assembler.EstadoInputDesassembler;
import com.diemerson.mobilefood.api.assembler.EstadoModelAssembler;
import com.diemerson.mobilefood.api.model.EstadoModel;
import com.diemerson.mobilefood.api.model.input.EstadoInput;
import com.diemerson.mobilefood.domain.service.CadastroEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.diemerson.mobilefood.domain.model.Estado;
import com.diemerson.mobilefood.domain.repository.EstadoRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/estados")
public class EstadoController {

	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstado;

	@Autowired
	private EstadoModelAssembler estadoModelAssembler;

	@Autowired
	private EstadoInputDesassembler estadoInputDesassembler;
	
	@GetMapping
	public List<EstadoModel> listar() {
		return estadoModelAssembler.toCollectionModel(estadoRepository.findAll());
	}
	
	@GetMapping("/{estadoId}")
	public EstadoModel buscar(@PathVariable Long estadoId) {
		return estadoModelAssembler.toModel(cadastroEstado.buscarOuFalhar(estadoId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
		Estado estado = estadoInputDesassembler.toObjectDomain(estadoInput);

		return estadoModelAssembler.toModel(cadastroEstado.salvar(estado));
	}
	
	@PutMapping("/{estadoId}")
	public EstadoModel atualizar(@PathVariable Long estadoId,
			@RequestBody @Valid EstadoInput estadoInput) {
		Estado estado = cadastroEstado.buscarOuFalhar(estadoId);
		estadoInputDesassembler.copyToDomainObject(estadoInput, estado);
		
		return estadoModelAssembler.toModel(cadastroEstado.salvar(estado));
	}
	
	@DeleteMapping("/{estadoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long estadoId) {
		cadastroEstado.excluir(estadoId);	
	}
	
}
