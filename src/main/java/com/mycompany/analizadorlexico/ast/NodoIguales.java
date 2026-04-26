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

        // Nodo pivot
        String idPivot = "nodo_pivot_" + System.identityHashCode(this);
        resultado.append(idPivot + " [label=\"pivot\"]\n");
        resultado.append(miId + " -- " + idPivot + "\n");
        resultado.append(this.pivot.graficar(idPivot));

        // Nodos para cada lista
        int indice = 0;
        for (List<NodoExpresion> lista : this.listas) {
            String idLista = "nodo_lista_" + indice + "_" + System.identityHashCode(this);
            resultado.append(idLista + " [label=\"Lista " + indice + "\"]\n");
            resultado.append(miId + " -- " + idLista + "\n");

            if (lista.isEmpty()) {
                String idVacia = "nodo_vacia_" + indice + "_" + System.identityHashCode(this);
                resultado.append(idVacia + " [label=\"[]\"]\n");
                resultado.append(idLista + " -- " + idVacia + "\n");
            } else {
                for (NodoExpresion elemento : lista) {
                    resultado.append(elemento.graficar(idLista));
                }
            }
            indice++;
        }

        return resultado.toString();
    }
}