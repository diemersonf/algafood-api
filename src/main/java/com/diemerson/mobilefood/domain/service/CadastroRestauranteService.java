package com.diemerson.mobilefood.domain.service;

import com.diemerson.mobilefood.domain.exception.EntidadeEmUsoException;
import com.diemerson.mobilefood.domain.exception.RestauranteNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.diemerson.mobilefood.domain.model.Cozinha;
import com.diemerson.mobilefood.domain.model.Restaurante;
import com.diemerson.mobilefood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {
	private static final String MSG_RESTAURANTE_EM_USO
			= "Cozinha de código %d não pode ser removida, pois está em uso";

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
		
		restaurante.setCozinha(cozinha);
		
		return restauranteRepository.save(restaurante);
	}
	
	public Restaurante buscarOuFalhar(Long restauranteId) {
		return restauranteRepository.findById(restauranteId)
			.orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}

	public void excluir(Long restauranteId) {
		try {
			restauranteRepository.deleteById(restauranteId);

		} catch (EmptyResultDataAccessException e) {
			throw new RestauranteNaoEncontradoException(restauranteId);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_RESTAURANTE_EM_USO, restauranteId));
		}
	}
	
}
