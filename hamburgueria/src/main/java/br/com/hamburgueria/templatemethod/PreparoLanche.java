package br.com.hamburgueria.templatemethod;

public abstract class PreparoLanche {

    public final void preparar() {
        tostarPao();
        prepararCarne();
        montarIngredientes();
        embalar();
    }

    protected void tostarPao() {
        System.out.println("Tostando o pao na chapa...");
    }

    protected abstract void prepararCarne();

    protected abstract void montarIngredientes();

    protected void embalar() {
        System.out.println("Embalando o lanche...");
    }
}
