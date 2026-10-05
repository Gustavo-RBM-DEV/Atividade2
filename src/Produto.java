import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Produto implements Comparable<Produto>{
	
	private static final double MARGEM_PADRAO = 0.2;
	private static int ultimoID = 10_000;
	
	protected int idProduto;
	protected String descricao;
	protected double precoCusto;
	protected double margemLucro;
	
	/**
     * Inicializador privado. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	private void init(String desc, double precoCusto, double margemLucro) {
		
		if ((desc.length() >= 3) && (precoCusto > 0.0) && (margemLucro > 0.0)) {
			descricao = desc;
			this.precoCusto = precoCusto;
			this.margemLucro = margemLucro;
			idProduto = ultimoID++;
		} else {
			throw new IllegalArgumentException("Valores inválidos para os dados do produto.");
		}
	}
	
	/**
     * Construtor completo. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	protected Produto(String desc, double precoCusto, double margemLucro) {
		init(desc, precoCusto, margemLucro);
	}
	
	/**
     * Construtor sem margem de lucro - fica considerado o valor padrão de margem de lucro.
     * Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00 
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     */
	protected Produto(String desc, double precoCusto) {
		init(desc, precoCusto, MARGEM_PADRAO);
	}
	
	 /**
     * Retorna o valor de venda do produto, considerando seu preço de custo e margem de lucro.
     * @return Valor de venda do produto (double, positivo)
     */
	public abstract double valorDeVenda();
	
	/**
     * Descrição, em string, do produto, contendo sua descrição e o valor de venda.
     *  @return String com o formato:
     * [NOME]: R$ [VALOR DE VENDA]
     */
    @Override
	public String toString() {
    	
    	NumberFormat moeda = NumberFormat.getCurrencyInstance();
    	
		return String.format("NOME: " + descricao + ": " + moeda.format(valorDeVenda()));
	}
    
    @Override
    /**
     * Retorna o código identificador do produto. É um valor único para cada produto (== chave).
     * @return Inteiro positivo com o identificador do produto.
     */
    public int hashCode(){
        return idProduto;
    }

    /**
     * Igualdade de produtos: caso possuam o mesmo código. 
     * @param obj Outro produto a ser comparado 
     * @return booleano true/false conforme o parâmetro possua o mesmo código identificador deste objeto
     */
    @Override
    public boolean equals(Object obj){
    	
    	if (this == obj) {
    		return true;
    	}
    	if ((obj == null) || (getClass() != obj.getClass())) {
    		return false;
    	}
    	Produto outroProduto = (Produto) obj;
    	return (this.hashCode() == outroProduto.hashCode());
    }
    
    @Override
    /**
     * Comparação padrão do produto: identificador/hash code.
     * Retorna um valor negativo se este produto tem um identificador anterior ao outro produto,
     * valor positivo se o identificador é posterior ao do outro produto. Para o mesmo produto, o
     * retorno é 0.
     * @param outro Produto a ser comparado
     * @return Int de acordo com a regra padrão de Comparable (descrita acima)
     */
    public int compareTo(Produto outro){
    	
    	if (this.idProduto == outro.idProduto)
    		return 0;
    	else if (this.idProduto < outro.idProduto)
    		return -1;
    	else
    		return 1;
    }
    
    /**
     * Cria um produto a partir de uma linha de dados em formato texto. A linha de dados deve estar de acordo com a formatação
     * "tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
     * ou o funcionamento não será garantido. Os tipos são 1, para produto não perecível; e 2, para perecível.
     * @param linha Linha com os dados do produto a ser criado.
     * @return Um produto com os dados recebidos
     */
    static Produto criarDoTexto(String linha) {
     
    	String[] dadosLinha;
    	int tipo;
    	String descricao;
    	double precoCusto, margemLucro;
    	LocalDate dataDeValidade;
    	Produto produto;
    	
    	dadosLinha = linha.split(";");
    	tipo = Integer.parseInt(dadosLinha[0]);
    	descricao = dadosLinha[1];
    	precoCusto = Double.parseDouble(dadosLinha[2].replace(",", "."));
        margemLucro = Double.parseDouble(dadosLinha[3].replace(",", "."));
        if (tipo == 2) {
    		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    		dataDeValidade = LocalDate.parse(dadosLinha[4], formatoData);
    		produto = new ProdutoPerecivel(descricao, precoCusto, margemLucro, dataDeValidade);
    	} else {
    		produto = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
    	}
    	
    	return produto;
    }
    	
    /**
     * Gera uma linha de texto a partir dos dados do produto.
     * @return Uma string no formato "tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
     */
    public abstract String gerarDadosTexto();
}