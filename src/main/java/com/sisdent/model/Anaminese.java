package com.sisdent.model;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "anaminese")
public class Anaminese {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao;

	
    @NotNull
    @Column(name = "fumante")
    private boolean fumante;
	
    
    @NotNull
    @Column(name = "pratica_exercicio")
    private boolean praticaExercicio;
    
   
    @NotNull
    @Column(name = "doenca_hereditaria")
    private boolean doencaHereditaria;
    
    @Size(max = 255)
    @Column(name = "descricao_doenca", length = 255)
    private String descricaoDoenca;
    
    
    @NotNull
    @Column(name = "operacao_recente")
    private boolean operacaoRecente;
    
    @Size(max = 255)
    @Column(name = "descricao_operacao_recente", length = 255)
    private String descricaoOperacaoRecente;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "medicacao_continua")
    private boolean medicacaoContinua;
    
    @Size(max = 255)
    @Column(name = "descricao_medicacao_continua", length = 255)
    private String descricaoMedicacaoContinua;
    

    @NotNull
    @Column(name = "alergia", nullable = false)
    private boolean alergia;
    
    @Size(max = 255)
    @Column(name = "descricao_alergia", length = 255)
    private String descricaoAlergia;
    
    
    @NotNull
    @Column(name = "dst", nullable = false)
    private boolean dst;
    
    @Size(max = 255)
    @Column(name = "descricao_dst", length = 255)
    private String descricaoDst;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "observacao", length = 65535)
    private String observacao;
    
    @OneToOne
	@JoinColumn(name = "codigo_cliente")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "codigo_usuario")
	private Usuario usuario;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = LocalDateTime.now();
	}

	public boolean isFumante() {
		return fumante;
	}

	public void setFumante(boolean fumante) {
		this.fumante = fumante;
	}

	public boolean isPraticaExercicio() {
		return praticaExercicio;
	}

	public void setPraticaExercicio(boolean praticaExercicio) {
		this.praticaExercicio = praticaExercicio;
	}

	public boolean isDoencaHereditaria() {
		return doencaHereditaria;
	}

	public void setDoencaHereditaria(boolean doencaHereditaria) {
		this.doencaHereditaria = doencaHereditaria;
	}

	public String getDescricaoDoenca() {
		return descricaoDoenca;
	}

	public void setDescricaoDoenca(String descricaoDoenca) {
		this.descricaoDoenca = descricaoDoenca;
	}

	public boolean isOperacaoRecente() {
		return operacaoRecente;
	}

	public void setOperacaoRecente(boolean operacaoRecente) {
		this.operacaoRecente = operacaoRecente;
	}

	public String getDescricaoOperacaoRecente() {
		return descricaoOperacaoRecente;
	}

	public void setDescricaoOperacaoRecente(String descricaoOperacaoRecente) {
		this.descricaoOperacaoRecente = descricaoOperacaoRecente;
	}

	public boolean isMedicacaoContinua() {
		return medicacaoContinua;
	}

	public void setMedicacaoContinua(boolean medicacaoContinua) {
		this.medicacaoContinua = medicacaoContinua;
	}

	public String getDescricaoMedicacaoContinua() {
		return descricaoMedicacaoContinua;
	}

	public void setDescricaoMedicacaoContinua(String descricaoMedicacaoContinua) {
		this.descricaoMedicacaoContinua = descricaoMedicacaoContinua;
	}

	public boolean isAlergia() {
		return alergia;
	}

	public void setAlergia(boolean alergia) {
		this.alergia = alergia;
	}

	public String getDescricaoAlergia() {
		return descricaoAlergia;
	}

	public void setDescricaoAlergia(String descricaoAlergia) {
		this.descricaoAlergia = descricaoAlergia;
	}

	public boolean isDst() {
		return dst;
	}

	public void setDst(boolean dst) {
		this.dst = dst;
	}

	public String getDescricaoDst() {
		return descricaoDst;
	}

	public void setDescricaoDst(String descricaoDst) {
		this.descricaoDst = descricaoDst;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isNovo() {
		return codigo == null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Anaminese other = (Anaminese) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	

}
