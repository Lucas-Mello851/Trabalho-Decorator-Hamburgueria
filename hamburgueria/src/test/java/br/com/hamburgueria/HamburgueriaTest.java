package br.com.hamburgueria;

import br.com.hamburgueria.cardapio.Cardapio;
import br.com.hamburgueria.component.Lanche;
import br.com.hamburgueria.concretecomponent.*;
import br.com.hamburgueria.concretedecorator.*;
import br.com.hamburgueria.factory.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes — Hamburgueria (Decorator + Factory + Singleton)")
class HamburgueriaTest {

    @Test
    @DisplayName("Singleton: duas chamadas getInstance() retornam a mesma instância")
    void testSingletonMesmaInstancia() {
        Cardapio a = Cardapio.getInstance();
        Cardapio b = Cardapio.getInstance();
        assertSame(a, b, "Devem ser a mesma referência de memória");
    }

    @Test
    @DisplayName("Singleton: cardápio contém as 3 fábricas registradas")
    void testSingletonFabricasRegistradas() {
        Cardapio c = Cardapio.getInstance();
        assertEquals(3, c.getFabricas().size());
        assertTrue(c.getFabricas().containsKey("Clássico"));
        assertTrue(c.getFabricas().containsKey("Vegano"));
        assertTrue(c.getFabricas().containsKey("Smash"));
    }

    @Test
    @DisplayName("Singleton: cardápio contém os 5 adicionais registrados")
    void testSingletonAdicionaisRegistrados() {
        Cardapio c = Cardapio.getInstance();
        assertEquals(5, c.getAdicionais().size());
        assertTrue(c.getAdicionais().containsKey("Queijo Cheddar"));
        assertTrue(c.getAdicionais().containsKey("Bacon Crocante"));
    }

    @Test
    @DisplayName("Singleton: tipo inválido lança IllegalArgumentException")
    void testSingletonTipoInvalido() {
        assertThrows(IllegalArgumentException.class,
            () -> Cardapio.getInstance().getFabrica("Inexistente"));
    }

    @Test
    @DisplayName("Factory: ClassicoFactory cria HamburguerClassico com preço R$22,00")
    void testFactoryClassico() {
        Lanche l = new ClassicoFactory().criar();
        assertInstanceOf(HamburguerClassico.class, l);
        assertEquals(22.00, l.getPreco(), 0.001);
        assertTrue(l.getDescricao().contains("Clássico"));
    }

    @Test
    @DisplayName("Factory: VeganoFactory cria HamburguerVegano com preço R$26,00")
    void testFactoryVegano() {
        Lanche l = new VeganoFactory().criar();
        assertInstanceOf(HamburguerVegano.class, l);
        assertEquals(26.00, l.getPreco(), 0.001);
        assertTrue(l.getDescricao().contains("Vegano"));
    }

    @Test
    @DisplayName("Factory: SmashFactory cria HamburguerSmash com preço R$28,00")
    void testFactorySmash() {
        Lanche l = new SmashFactory().criar();
        assertInstanceOf(HamburguerSmash.class, l);
        assertEquals(28.00, l.getPreco(), 0.001);
        assertTrue(l.getDescricao().contains("Smash"));
    }

    @Test
    @DisplayName("Factory via Cardápio: getFabrica cria o lanche correto")
    void testFactoryViaCardapio() {
        Lanche classico = Cardapio.getInstance().getFabrica("Clássico").criar();
        Lanche vegano   = Cardapio.getInstance().getFabrica("Vegano").criar();
        assertEquals(22.00, classico.getPreco(), 0.001);
        assertEquals(26.00, vegano.getPreco(), 0.001);
    }

    @Test
    @DisplayName("Decorator: Queijo adiciona +R$3,00 e altera descrição")
    void testDecoratorQueijo() {
        Lanche l = new Queijo(new HamburguerClassico());
        assertEquals(25.00, l.getPreco(), 0.001);
        assertTrue(l.getDescricao().contains("Queijo Cheddar"));
    }

    @Test
    @DisplayName("Decorator: Bacon adiciona +R$4,00 e altera descrição")
    void testDecoratorBacon() {
        Lanche l = new Bacon(new HamburguerClassico());
        assertEquals(26.00, l.getPreco(), 0.001);
        assertTrue(l.getDescricao().contains("Bacon Crocante"));
    }

    @Test
    @DisplayName("Decorator: Alface adiciona +R$1,00")
    void testDecoratorAlface() {
        Lanche l = new Alface(new HamburguerClassico());
        assertEquals(23.00, l.getPreco(), 0.001);
    }

    @Test
    @DisplayName("Decorator: Tomate adiciona +R$1,00")
    void testDecoratorTomate() {
        Lanche l = new Tomate(new HamburguerClassico());
        assertEquals(23.00, l.getPreco(), 0.001);
    }

    @Test
    @DisplayName("Decorator: MolhoEspecial adiciona +R$2,00")
    void testDecoratorMolho() {
        Lanche l = new MolhoEspecial(new HamburguerClassico());
        assertEquals(24.00, l.getPreco(), 0.001);
    }

    @Test
    @DisplayName("Decorator: Clássico + Queijo + Bacon = R$29,00")
    void testComposicaoClassicoQueijoBacon() {
        Lanche l = new Bacon(new Queijo(new HamburguerClassico()));
        // 22 + 3 + 4 = 29
        assertEquals(29.00, l.getPreco(), 0.001);
        assertTrue(l.getDescricao().contains("Queijo Cheddar"));
        assertTrue(l.getDescricao().contains("Bacon Crocante"));
    }

    @Test
    @DisplayName("Decorator: Smash completo (Queijo + Bacon + Alface + Tomate + Molho) = R$39,00")
    void testSmashCompleto() {
        Lanche l = new HamburguerSmash();
        l = new Queijo(l);
        l = new Bacon(l);
        l = new Alface(l);
        l = new Tomate(l);
        l = new MolhoEspecial(l);
        assertEquals(39.00, l.getPreco(), 0.001);
    }

    @Test
    @DisplayName("Decorator: Duplo Queijo soma o adicional duas vezes")
    void testDuploQueijo() {
        Lanche l = new Queijo(new Queijo(new HamburguerClassico()));
        assertEquals(28.00, l.getPreco(), 0.001);
    }

    @Test
    @DisplayName("Decorator: Vegano + salada + molho = R$30,00")
    void testVeganoSalada() {
        Lanche l = new HamburguerVegano();
        l = new Alface(l);
        l = new Tomate(l);
        l = new MolhoEspecial(l);
        assertEquals(30.00, l.getPreco(), 0.001);
    }

    @Test
    @DisplayName("Decorator: objeto original não é modificado após decoração")
    void testOriginalImutavel() {
        Lanche base = new HamburguerClassico();
        double precoOriginal = base.getPreco();
        Lanche decorado = new Queijo(base);
        decorado.getPreco();
        assertEquals(precoOriginal, base.getPreco(), 0.001);
    }

    @Test
    @DisplayName("Integração: Factory via Singleton + Decorator resulta em preço correto")
    void testIntegracaoCompleta() {
        Lanche l = Cardapio.getInstance().getFabrica("Smash").criar();
        l = new Queijo(l);
        l = new Bacon(l);
        assertEquals(35.00, l.getPreco(), 0.001);
        assertTrue(l.getDescricao().contains("Smash"));
        assertTrue(l.getDescricao().contains("Queijo"));
        assertTrue(l.getDescricao().contains("Bacon"));
    }
}
