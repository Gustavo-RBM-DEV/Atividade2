import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Pedido implements Comparable<Pedido> {

	private static int ultimoID = 1;
	
	/** Quantidade máxima de itens de um pedido */
	private static final int MAX_ITENS_DE_PEDIDO = 10;
	
	/** Porcentagem de desconto para pagamentos à vista */
	private static final double DESCONTO_PG_A_VISTA = 0.15;
	
	private int idPedido;
	
	/** Vetor para armazenar os itens do pedido */
	private ItemDePedido[] itensDePedido;
	
	/** Data de criação do pedido */
	private LocalDate dataPedido;
	
	/** Indica a quantidade total de itens no pedido até o momento */
	private int quantItensDePedido = 0;
	
	/** Indica a forma de pagamento do pedido sendo: 1, pagamento à vista; 2, pagamento parcelado */
	private int formaDePagamento;
	
	/** Construtor do pedido.
	 *  Deve criar o vetor de itens do pedido, 
	 *  armazenar a data e a forma de pagamento informadas para o pedido. 
	 */  
	public Pedido(LocalDate dataPedido, int formaDePagamento) {
		
		idPedido = ultimoID++;
		itensDePedido = new ItemDePedido[MAX_ITENS_DE_PEDIDO];
		quantItensDePedido = 0;
		this.dataPedido = dataPedido;
		this.formaDePagamento = formaDePagamento;
	}
	
	public ItemDePedido[] getItensDoPedido() {
		return itensDePedido;
	}
	
	public ItemDePedido existeNoPedido(Produto produto) {
		
		ItemDePedido itemDePedidoProcurado = new ItemDePedido(produto, 0, 0.1);
		for (int i = 0; i < quantItensDePedido; i++) {
			if (itensDePedido[i].equals(itemDePedidoProcurado)) {
				return itensDePedido[i];
			}
		}
		return null;
	}
	
	/**
     * Inclui produtos no pedido. Se necessário, aumenta a quantidade de itens armazenados no pedido até o momento.
     * Caso o produto já exista no pedido, sua quantidade é atualizada.
     * Caso contrário, um novo item de pedido é criado, desde que haja espaço disponível.
     * @param novo O produto a ser incluído no pedido
     * @param quantidade A quantidade do produto a ser incluída no pedido
     * @return true/false indicando se a inclusão do produto no pedido foi realizada com sucesso.
     */
	public boolean incluirProduto(Produto novo, int quantidade) {
		
		ItemDePedido itemDePedido = existeNoPedido(novo);
		
		if (itemDePedido != null) {
			itemDePedido.setQuantidade(quantidade + itemDePedido.getQuantidade());
			return true;
		}
		else if (quantItensDePedido < MAX_ITENS_DE_PEDIDO) {
			itensDePedido[quantItensDePedido++] = new ItemDePedido(novo, quantidade, novo.valorDeVenda());
			return true;
		}
		return false;
	}
	
	/**
     * Calcula e retorna o valor final do pedido (soma do valor de venda de todos os itens do pedido).
     * Caso a forma de pagamento do pedido seja à vista, aplica o desconto correspondente.
     * @return Valor final do pedido (double)
     */
	public double valorFinal() {
		
		double valorPedido = 0;
		BigDecimal valorPedidoBD;
		
		for (int i = 0; i < quantItensDePedido; i++) {
			valorPedido += itensDePedido[i].getQuantidade() * itensDePedido[i].getPrecoVenda();
		}
		
		if (formaDePagamento == 1) {
			valorPedido = valorPedido * (1.0 - DESCONTO_PG_A_VISTA);
		}
		
		valorPedidoBD = new BigDecimal(Double.toString(valorPedido));

		valorPedidoBD = valorPedidoBD.setScale(2, RoundingMode.HALF_UP);

        return valorPedidoBD.doubleValue();
	}
	
	/**
     * Representação, em String, do pedido.
     * Contém um cabeçalho com sua data e o número de itens de pedido no pedido.
     * Depois, em cada linha, a descrição de cada item do pedido.
     * Ao final, mostra a forma de pagamento, o percentual de desconto (se for o caso) e o valor a ser pago pelo pedido.
     * Exemplo:
     * ID do pedido: 3600
     * Data do pedido: 25/08/2025
     * Pedido com 2 itens.
     * Itens de pedido no pedido:
     * NOME: Iogurte
     * Válido até: 29/08/2025
     * QUANTIDADE: 2
     * PREÇO UNITÁRIO: R$ 8.00
     * NOME: Guardanapos
     * QUANTIDADE: 1
     * PREÇO UNITÁRIO: R$ 2.75
     * Pedido pago à vista. Percentual de desconto: 15,00%
     * Valor total do pedido: R$ 15.44 
     * @return Uma string contendo dados do pedido conforme especificado (cabeçalho, detalhes, forma de pagamento,
     * percentual de desconto - se for o caso - e valor a pagar)
     */
	@Override
	public String toString() {
		
		StringBuilder stringPedido = new StringBuilder();
		
		stringPedido.append("==============================\n");
		stringPedido.append("ID do pedido: " + idPedido + "\n");
		
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		stringPedido.append("Data do pedido: " + formatoData.format(dataPedido) + "\n");
		
		stringPedido.append("Pedido com " + quantItensDePedido + " itens.\n");
		stringPedido.append("Itens de pedido no pedido:\n");
		for (int i = 0; i < quantItensDePedido; i++ ) {
			stringPedido.append(itensDePedido[i].toString() + "\n");
		}
		
		stringPedido.append("Pedido pago ");
		if (formaDePagamento == 1) {
			stringPedido.append("à vista. Percentual de desconto: " + String.format("%.2f", DESCONTO_PG_A_VISTA * 100) + "%\n");
		} else {
			stringPedido.append("parcelado.\n");
		}
		
		stringPedido.append("Valor total do pedido: R$ " + String.format("%.2f", valorFinal()));
		
		return stringPedido.toString();
	}
	
	@Override
    /**
     * Retorna o código identificador do pedido. É um valor único para cada pedido (== chave).
     * @return Inteiro positivo com o identificador do pedido.
     */
    public int hashCode(){
        return idPedido;
    }
	
	/**
     * Igualdade de pedidos: caso possuam o mesmo código. 
     * @param obj Outro pedido a ser comparado 
     * @return booleano true/false conforme o parâmetro possua o código igual ou não a este pedido.
     */
    @Override
    public boolean equals(Object obj) {
    	
    	if (obj == this) {
			return true;
		}
		if ((obj == null) || (!(obj instanceof Pedido))) {
			return false;
		}
		
        Pedido outro = (Pedido)obj;
        return this.hashCode() == outro.hashCode();
    }

	@Override
	public int compareTo(Pedido outroPedido) {
		
		return (this.hashCode() - outroPedido.hashCode());
	}
}