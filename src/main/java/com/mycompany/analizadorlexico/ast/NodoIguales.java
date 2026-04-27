package com.mycompany.analizadorlexico.ast;

import java.util.List;
import java.util.ArrayList;
/**
 * Representa la función especial #Iguales del Grupo 1.
 *
 * Sintaxis:
 *   #Iguales(expresión_pivot, [lista1], [lista2], ..., [listaN])
 *
 * Semántica: devuelve la cantidad de elementos iguales al pivot
 * encontrados en todas las listas combinadas. Si una lista es vacía,
 * contribuye con cero.
 *
 * Ejemplo:
 *   result := #Iguales(a+w/b, [(d-3)*2, e, f])
 */
public class NodoIguales extends NodoExpresion {

    /** Expresión pivot contra la que se comparan todos los elementos. */
    private final NodoExpresion pivot;
    private final NodoVariable comparador;
    private final NodoVariable contador;

    /**
     * Lista de listas de expresiones.
     * Cada elemento de este List representa una lista [ ... ] del lenguaje.
     * Una lista vacía se representa con una lista Java vacía (no null).
     */
    private final ArrayList<ArrayList<NodoExpresion>> listas;

    public NodoIguales(NodoExpresion pivot, ArrayList<ArrayList<NodoExpresion>> listas) {
        super("IGUALES");
        this.pivot = pivot;
        this.listas = listas;
        this.comparador = new NodoVariable("comparador");
        this.contador = new NodoVariable("contador");
        comparador.setTipoSemantico("INT");
        contador.setTipoSemantico("INT");
    }

    public NodoExpresion getPivot() {
        return pivot;
    }

    public ArrayList<ArrayList<NodoExpresion>> getListas() {
        return listas;
    }

    @Override
    public String getTipoSemantico() { return "INT"; }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_iguales_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();

        resultado.append(miId + " [label=\"#Iguales\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }

        // comp = pivot  → NodoAsignacion
        NodoAsignacion asigComp = new NodoAsignacion(comparador, pivot);
        resultado.append(asigComp.graficar(miId));

        // contador = 0  → NodoAsignacion
        NodoConstanteInt cero = new NodoConstanteInt(0);
        NodoAsignacion asigContador = new NodoAsignacion(contador, cero);
        resultado.append(asigContador.graficar(miId));

        // IF por cada elemento de cada lista
        for (List<NodoExpresion> lista : listas) {
            for (NodoExpresion elemento : lista) {
                
                // Condicion: comp == elemento  → NodoCondicion
                NodoVariable comp = new NodoVariable("comparador");
                comp.setTipoSemantico("INT");
                NodoCondicion condicion = new NodoCondicion(comp, "==", elemento);
                
                NodoConstanteInt uno = new NodoConstanteInt(1);
                // contador = contador + 1  → NodoAsignacion
                NodoVariable contRef1 = new NodoVariable("contador");
                contRef1.setTipoSemantico("INT");
                NodoVariable contRef2 = new NodoVariable("contador");
                contRef2.setTipoSemantico("INT");
                NodoExpresionBinaria suma = new NodoExpresionBinaria(contRef1, "+", uno);
                NodoAsignacion asigInc = new NodoAsignacion(contRef2, suma);

                // Then: lista con la asignacion
                List<NodoSentencia> sentenciasThen = new ArrayList<>();
                sentenciasThen.add(asigInc);

                // IF → NodoIf
                NodoIf ifNodo = new NodoIf(condicion, sentenciasThen, null);
                resultado.append(ifNodo.graficar(miId));
            }
        }

        return resultado.toString();
    }
}
