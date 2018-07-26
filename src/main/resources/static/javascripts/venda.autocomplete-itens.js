Brewer = Brewer || {};

Brewer.Autocomplete = (function() {
	
	function Autocomplete() {
		this.servicoInput = $('.js-nome-servico-input');
		var htmlTemplateAutoComplete = $('#template-autocomplete-servico').html();
		this.template = Handlebars.compile(htmlTemplateAutoComplete);
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}
	
	Autocomplete.prototype.iniciar = function() {
		var options = {
			url: function(nome) {
				return this.servicoInput.data('url') + '?nome=' + nome;
			}.bind(this),
			getValue: 'nome',
			minCharNumber: 3,
			requestDelay: 300,
			ajaxSettings: {
				contentType: 'application/json'
			},
			template: {
				type: 'custom',
				method: template.bind(this)
			},
			list: {
				onChooseEvent: onItemSelecionado.bind(this)
			}
		};
		
		this.servicoInput.easyAutocomplete(options);
	}
	
	function onItemSelecionado() {
		this.emitter.trigger('item-selecionado', this.servicoInput.getSelectedItemData());
		this.servicoInput.val('');
		this.servicoInput.focus();
	}
	
	function template(nome, servico) {
		servico.valorFormatado = Brewer.formatarMoeda(servico.valor);
		return this.template(servico);
	}
	
	return Autocomplete
	
}());
