public class ProdutoNaoPerecivel extends Produto{

	/**
     * Construtor completo. Causa exceção em caso de valores inválidos para os dados do produto.
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço de compra do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro para a venda (mínimo 0.01)
     * @throws IllegalArgumentException em caso dos limites acima serem desrespeitados.
     */
	public ProdutoNaoPerecivel(String desc, double precoCusto, double margemLucro) {
		super(desc, precoCusto, margemLucro);
	}
	
	/**
     * Construtor do produto com margem de lucro padrão (20%). Causa exceção em caso de valores inválidos para os dados do produto.
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço de compra do produto (mínimo 0.01)
     * @throws IllegalArgumentException em caso dos limites acima serem desrespeitados.
     */
	public ProdutoNaoPerecivel(String desc, double precoCusto) {
		super(desc, precoCusto);
	}

	/**
     * Retorna o valor de venda do produto, considerando seu preço de custo e margem de lucro
     * @return Valor de venda do produto (double, positivo)
     */
	@Override
	public double valorDeVenda() {
		return (precoCusto * (1.0 + margemLucro));
	}

	/**
     * Gera uma linha de texto a partir dos dados do produto. Preço e margem de lucro são formatados com 2 casas decimais.
     * @return Uma string no formato "1;descrição;preçoDeCusto;margemDeLucro"
     */
	@Override
    public String gerarDadosTexto() {
    	
		String precoCustoFormatado = String.format("%.2f", precoCusto).replaceAll(",", ".");
		String margemLucroFormatada = String.format("%.2f", margemLucro).replaceAll(",", ".");
		
		return String.format("1;%s;%s;%s", descricao, precoCustoFormatado, margemLucroFormatada);
    }
}
