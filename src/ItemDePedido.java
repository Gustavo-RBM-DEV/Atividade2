import java.text.NumberFormat;

public class ItemDePedido {

	private Produto produto;
	private int quantidade;
	private double precoVenda;
	
	public ItemDePedido(Produto produto, int quantidade, double precoVenda) {
		this.produto = produto;
		this.setQuantidade(quantidade);
		this.precoVenda = precoVenda;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public double getPrecoVenda() {
		return precoVenda;
	}
	
	public Produto getProduto() {
		return produto;
	}
	
	@Override
	public String toString() {
		
		NumberFormat moeda = NumberFormat.getCurrencyInstance();
		String itemDePedidoTexto;
		
		itemDePedidoTexto = String.format("PRODUTO: " + produto.descricao);
		itemDePedidoTexto += String.format("\nQUANTIDADE: %02d", quantidade);
		itemDePedidoTexto += String.format("\nPREÇO UNITÁRIO: " + moeda.format(precoVenda));
		return  itemDePedidoTexto;	
	}
	
	@Override
	public boolean equals(Object obj) {
		
		ItemDePedido outroItemDePedido;
		
		if (obj == this) {
			return true;
		}
		if ((obj == null) || (!(obj instanceof ItemDePedido))) {
			return false;
		}
		
		outroItemDePedido = (ItemDePedido) obj;
		return this.produto.equals(outroItemDePedido.produto);
	}
}
