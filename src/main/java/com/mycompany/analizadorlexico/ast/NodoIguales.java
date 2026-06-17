package com.mycompany.analizadorlexico.ast;

import java.util.List;

import com.mycompany.analizadorlexico.GeneradorCodigo;

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
    public String graficarConVariable (NodoVariable variable, String id) {
        StringBuilder resultado = this.graficarSentencias();
        resultado.append(variable.graficar(id));
        resultado.append(this.graficarContador(id));
        return resultado.toString();
    }

    @Override
    public String generarASM(java.io.PrintWriter pw, GeneradorCodigo gc) {
        pw.println("; =================================================");
        pw.println("; GENERACION DE LA FUNCION ESPECIAL #IGUALES");
        pw.println("; =================================================");

        // 1. Inicializar el comparador con el valor del pivot (comparador := pivot)
        NodoAsignacion asigComp = new NodoAsignacion(this.comparador, this.pivot);
        asigComp.generarASM(pw, gc);

        // 2. Inicializar el contador en cero (contador := 0)
        NodoConstanteInt cero = new NodoConstanteInt(0);
        NodoAsignacion asigContador = new NodoAsignacion(this.contador, cero);
        asigContador.generarASM(pw, gc);

        // 3. Iterar por cada elemento de cada lista emulando un IF inline
        for (ArrayList<NodoExpresion> lista : this.listas) {
            for (NodoExpresion elemento : lista) {
                
                // Condicion: comparador == elemento
                NodoVariable compRef = new NodoVariable("comparador");
                compRef.setTipoSemantico("INT");
                NodoCondicion cond = new NodoCondicion(compRef, "==", elemento);
                
                // Incremento: contador := contador + 1
                NodoConstanteInt uno = new NodoConstanteInt(1);
                NodoVariable contRef1 = new NodoVariable("contador");
                contRef1.setTipoSemantico("INT");
                NodoVariable contRef2 = new NodoVariable("contador");
                contRef2.setTipoSemantico("INT");
                
                NodoExpresionBinaria suma = new NodoExpresionBinaria(contRef1, "+", uno);
                NodoAsignacion asigInc = new NodoAsignacion(contRef2, suma);

                // Armamos el bloque de sentencias del Then para el incremento
                java.util.List<NodoSentencia> sentenciasThen = new java.util.ArrayList<>();
                sentenciasThen.add(asigInc);

                // Instanciamos el NodoIf emulado en caliente y hacemos que escupa su ASM
                NodoIf ifEmulado = new NodoIf(cond, sentenciasThen, null);
                ifEmulado.generarASM(pw, gc);
            }
        }

        pw.println("; =================================================");
        pw.println("; FIN DE LA FUNCION ESPECIAL #IGUALES");
        pw.println("; =================================================");
        pw.println();

        // 4. Retornamos el nombre de la variable "_contador" de tu TdeS
        // para que la expresion o asignacion que llamo a #Iguales sepa de donde leer el total
        return "_contador";
    }

    @Override
    protected String graficar(String idPadre) {
        StringBuilder resultado = this.graficarSentencias();
        resultado.append(this.graficarContador(idPadre));

        return resultado.toString();
    }

    private StringBuilder graficarSentencias () {
        String idPrograma = "nodo_programa";
        //final String miId = "nodo_iguales_" + System.identityHashCode(this);
        

        //resultado.append(miId + " [label=\"#Iguales\"]\n");
        //if (idPadre != null) {
        //    resultado.append(idPadre + " -- " + miId + "\n");
        //}

        StringBuilder resultado = new StringBuilder();
        // comp = pivot  → NodoAsignacion
        NodoAsignacion asigComp = new NodoAsignacion(comparador, pivot);
        resultado.append(asigComp.graficar(idPrograma));

        // contador = 0  → NodoAsignacion
        NodoConstanteInt cero = new NodoConstanteInt(0);
        NodoAsignacion asigContador = new NodoAsignacion(contador, cero);
        resultado.append(asigContador.graficar(idPrograma));

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
                resultado.append(ifNodo.graficar(idPrograma));
            }
        }
        return resultado;
    }

    private String graficarContador(String idPadre) {
        NodoVariable contador = new NodoVariable("contador");
        contador.setTipoSemantico("INT");
        return contador.graficar(idPadre);
    }
}
