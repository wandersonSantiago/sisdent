var Brewer = Brewer || {};

Brewer.CategoriaCadastroRapido = (function() {
	
	function CategoriaCadastroRapido() {
		this.modal = $('#modalCadastroRapidoCategoria');
		this.botaoSalvar = this.modal.find('.js-modal-cadastro-categoria-salvar-btn');
		this.form = this.modal.find('form');
		this.url = this.form.attr('action');
		this.inputNomeCategoria = $('#nomeCategoria');
		this.containerMensagemErro = $('.js-mensagem-cadastro-rapido-categoria');
	}
	
	CategoriaCadastroRapido.prototype.iniciar = function() {
		this.form.on('submit', function(event) { event.preventDefault() });
		this.modal.on('shown.bs.modal', onModalShow.bind(this));
		this.modal.on('hide.bs.modal', onModalClose.bind(this))
		this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this));
	}
	
	function onModalShow() {
		this.inputNomeCategoria.focus();
	}
	
	function onModalClose() {
		this.inputNomeCategoria.val('');
		this.containerMensagemErro.addClass('hidden');
		this.form.find('.form-group').removeClass('has-error');
	}
	
	function onBotaoSalvarClick() {
		var nomeCategoria = this.inputNomeCategoria.val().trim();
		$.ajax({
			url: this.url,
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ nome: nomeCategoria }),
			error: onErroSalvandoCategoria.bind(this),
			success: onCategoriaSalvo.bind(this)
		});
	}
	
	function onErroSalvandoCategoria(obj) {
		var mensagemErro = obj.responseText;
		this.containerMensagemErro.removeClass('hidden');
		this.containerMensagemErro.html('<span>' + mensagemErro + '</span>');
		this.form.find('.form-group').addClass('has-error');
	}
	
	function onCategoriaSalvo(categoria) {
		var comboCategoria = $('#categoria');
		comboCategoria.append('<option value=' + categoria.codigo + '>' + categoria.nome + '</option>');
		comboCategoria.val(categoria.codigo);
		this.modal.modal('hide');
	}
	
	return CategoriaCadastroRapido;
	
}());

$(function() {
	var categoriaCadastroRapido = new Brewer.CategoriaCadastroRapido();
	categoriaCadastroRapido.iniciar();
});
