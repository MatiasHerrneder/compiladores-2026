package com.mycompany.analizadorlexico.ast;

import java.io.PrintWriter;
import com.mycompany.analizadorlexico.GeneradorCodigo;

public abstract class NodoSentencia extends Nodo {
    
    public NodoSentencia(String tipo) {
        super(tipo);
    }

    // Define que cualquier sentencia DEBE saber cómo traducirse a Assembler
    public abstract void generarASM(PrintWriter pw, GeneradorCodigo gc);
}