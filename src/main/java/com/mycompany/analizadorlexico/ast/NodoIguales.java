package com.mycompany.analizadorlexico.ast;

import java.util.List;

public class NodoIguales extends NodoExpresion {

    private final NodoExpresion pivot;
    private final List<NodoIdentificador> lista;

    public NodoIguales(NodoExpresion pivot, List<NodoIdentificador> lista) {
        super("#Iguales");
        this.pivot = pivot;
        this.lista = lista;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();

        String resultado = super.graficar(idPadre);

        // conectar pivot
        resultado += pivot.graficar(miId);

        // conectar todas las listas
        for (NodoIdentificador expr : lista) {
           resultado += expr.graficar(miId);
        }

        return resultado;
    }
}