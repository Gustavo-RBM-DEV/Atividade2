import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto{

	/** Desconto para proximidade de validade: 25% */
	private static final double DESCONTO = 0.25;
	
	/** Prazo, em dias, para conceder o desconto por proximidade da validade */
	private static final int PRAZO_DESCONTO = 7;
	
	/** Data de validade do produto. Não pode ser anterior à data da criação ou venda do produto. */
	private LocalDate dataDeValidade;
	
	/**
     * Construtor completo. 
     * Causa exceção em caso de valores inválidos para os dados do produto.
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço de compra do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro para a venda (mínimo 0.01)
     * @param validade Data de validade do produto, que deve ser posterior à data atual.
     * @throws IllegalArgumentException em caso dos limites acima serem desrespeitados.
     */
	public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade) {
		
		super(desc, precoCusto, margemLucro);
		
		if (validade.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Data de validade do produto é anterior ao dia de hoje!");
		}
		dataDeValidade = validade;
	}
	
	/**
     * Construtor do produto com margem de lucro padrão (20%). Causa exceção em caso de valores inválidos para os dados do produto.
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço de compra do produto (mínimo 0.01)
     * @param validade Data de validade do produto, que deve ser posterior à data atual.
     * @throws IllegalArgumentException em caso dos limites acima serem desrespeitados.
     */
	public ProdutoPerecivel(String desc, double precoCusto, LocalDate validade) {
		
		super(desc, precoCusto);
		
		if (validade.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Data de validade do produto é anterior ao dia de hoje!");
		}
		dataDeValidade = validade;
	}

	/**
     * Retorna o valor de venda do produto, considerando seu preço de custo, margem de lucro e
     * dias de validade. Se o prazo de validade estiver a menos de 7 dias, será concedido desconto de 25%.
     * @return Valor de venda do produto (double, positivo)
     */
	@Override
	public double valorDeVenda() {
		
		double precoVenda;
		
		if (dataDeValidade.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Data de validade do produto é anterior ao dia de hoje!");
		}
		
		precoVenda = (precoCusto * (1.0 + margemLucro));
				
		if (LocalDate.now().until(dataDeValidade).getDays() <= PRAZO_DESCONTO) {
			precoVenda = precoVenda * (1.0 - DESCONTO);
		}
		
		return precoVenda;
	}
	
	/**
     * Descrição, em string, do produto, contendo sua descrição, o valor de venda e sua data de validade.
     *  @return String com o formato:
     * [NOME]: R$ [VALOR DE VENDA]
     * Válido até [DD/MM/YYYY]
     */
    @Override
    public String toString(){
    	
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        String dados = super.toString();
        dados += "\nVálido até " + formato.format(dataDeValidade);
        
        return dados;
    }
    
    /**
     * Gera uma linha de texto a partir dos dados do produto. Preço e margem de lucro são formatados com 2 casas decimais.
     * Data de validade é formatada no formato dd/mm/aaaa
     * @return Uma string no formato "2;descrição;preçoDeCusto;margemDeLucro;dataDeValidade"
     */
	@Override
    public String gerarDadosTexto() {
    
		String precoCustoFormatado = String.format("%.2f", precoCusto).replaceAll(",", ".");
		String margemLucroFormatada = String.format("%.2f", margemLucro).replaceAll(",", ".");
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String dataFormatada = formatoData.format(dataDeValidade);
		
		return String.format("2;%s;%s;%s;%s", descricao, precoCustoFormatado, margemLucroFormatada, dataFormatada);
		
	}
}
