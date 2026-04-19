package br.com.hamburgueria.main;

import br.com.hamburgueria.cardapio.Cardapio;
import br.com.hamburgueria.component.Lanche;
import br.com.hamburgueria.concretedecorator.*;

public class Main {

    public static void main(String[] args) {

        Cardapio cardapio = Cardapio.getInstance();
        cardapio.exibirCardapio();

        Cardapio outraRef = Cardapio.getInstance();
        System.out.println("Mesma instância do Cardápio? " + (cardapio == outraRef));

        System.out.println("\n");
        System.out.println("  PEDIDOS DO DIA");
        System.out.println("\n");

        Lanche p1 = cardapio.getFabrica("Clássico").criar();
        exibir("Pedido 1 — Clássico puro", p1);

        Lanche p2 = cardapio.getFabrica("Clássico").criar();
        p2 = new Queijo(p2);
        p2 = new Bacon(p2);
        exibir("Pedido 2 — Clássico com Queijo e Bacon", p2);

        Lanche p3 = cardapio.getFabrica("Smash").criar();
        p3 = new Queijo(p3);
        p3 = new Queijo(p3);   // duplo queijo!
        p3 = new Bacon(p3);
        p3 = new MolhoEspecial(p3);
        exibir("Pedido 3 — Smash Duplo Queijo + Bacon + Molho", p3);

        Lanche p4 = cardapio.getFabrica("Vegano").criar();
        p4 = new Alface(p4);
        p4 = new Tomate(p4);
        p4 = new MolhoEspecial(p4);
        exibir("Pedido 4 — Vegano com Salada + Molho", p4);

        Lanche p5 = cardapio.getFabrica("Smash").criar();
        p5 = new Queijo(p5);
        p5 = new Bacon(p5);
        p5 = new Alface(p5);
        p5 = new Tomate(p5);
        p5 = new MolhoEspecial(p5);
        exibir("Pedido 5 — Smash Completo", p5);

        System.out.println(" ");
    }

    private static void exibir(String titulo, Lanche lanche) {
        System.out.println(" " + titulo);
        System.out.println("  " + lanche.getDescricao());
        System.out.printf("  Total: R$ %.2f%n%n", lanche.getPreco());
    }
}
