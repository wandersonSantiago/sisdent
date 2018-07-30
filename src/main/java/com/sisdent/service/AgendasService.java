package com.sisdent.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sisdent.model.Agenda;
import com.sisdent.model.Cliente;
import com.sisdent.repository.Agendas;
import com.sisdent.security.UsuarioSistema;
import com.sisdent.service.exception.ImpossivelExcluirEntidadeException;

@Service
public class AgendasService {

	@Autowired
	private Agendas agendas;
	
	
	@Transactional
	public  Agenda salvar(Agenda agenda, UsuarioSistema user) {
		/*Agenda a = findOne(agenda.getCodigo());
		if(a != null) {
			agenda.setCodigo(a.getCodigo());
		}*/
		agenda.setUsuario(user.getUsuario());
		agenda.setDataCriacao(LocalDateTime.now());
		return agendas.save(agenda);
	}
	
	@Transactional
	public void excluir(Agenda agenda) {
		try {
			agendas.delete(agenda);
			agendas.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar agenda. Já foi usada em alguma venda.");
		}
	}

	public Agenda findOne(Long codigo) {
		return agendas.findById(codigo).get();
	}

	public Agenda findByCliente(Cliente cliente) {
		return agendas.findByCliente(cliente);
	}

	public List<Agenda> buscarSomenteDoDia() {
		return agendas.findByDataLessThanEqualAndDataGreaterThanEqual(LocalDate.now(),LocalDate.now());
	}

	public List<Agenda> buscarSomenteDoDiaPorCliente(Cliente cliente) {
		return agendas.findByClienteAndDataLessThanEqualAndDataGreaterThanEqual(cliente,LocalDate.now(),LocalDate.now());
	}
	
}
