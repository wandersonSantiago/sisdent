Brewer = Brewer || {};

Brewer.Autocomplete = (function() {
	
	function Autocomplete() {
		this.servicoInput = $('.js-nome-servico-input');
		var htmlTemplateAutoComplete = $('#template-autocomplete-servico').html();
		this.template = Handlebars.compile(htmlTemplateAutoComplete);
		
	}
	
	Autocomplete.prototype.iniciar = function() {
		var options = {
			url: function(nome){ return '/servicos?nome=' + nome;
			
			},
			getValue:'nome',
			minCharNumber:3,
			requestDelay:300,
			ajaxSettings:{
				contentType: 'application/json'
			},
			template:{
				type:'custom',
				method: function(nome,servico){
					servico.valorFormatado = Brewer.formatarMoeda(servico.valor);
					return this.template(servico);
				}.bind(this)
				
			}
		};
		
		this.servicoInput.easyAutocomplete(options);
	}
	
		
	return Autocomplete
	
}());

$(function(){
	var autocomplete = new Brewer.Autocomplete();
	autocomplete.iniciar();
})