package com.mycompany.analizadorlexico.ast;

import com.mycompany.analizadorlexico.COLUMNA;
import com.mycompany.analizadorlexico.TablaSimbolos;
import java.util.List;
import java.util.Map;

public class NodoPrograma extends Nodo {
    //private final NodoDecVar decVar;
    private final List<NodoSentencia> sentencias;
    private TablaSimbolos tablaSimbolos;

    public NodoPrograma(/*NodoDecVar decVar, */List<NodoSentencia> sentencias) {
        super("PGM");
        //this.decVar = decVar;
        this.sentencias = sentencias;
    }

    public void setTablaSimbolos(TablaSimbolos tablaSimbolos) {
        this.tablaSimbolos = tablaSimbolos;
    }

    public String graficar() {
        // Acá se dispara la invocación a los métodos graficar() de los nodos.
        // Como un NodoPrograma no tiene padre, se inicia pasando null.
        return this.graficar(null);
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_programa";

        StringBuilder resultado = new StringBuilder();
        resultado.append("graph G {");

        resultado.append(miId + " [label=\"Programa\"]\n");
        
        //resultado.append(decVar.graficar(miId));

        for (NodoSentencia sentencia : this.sentencias) {
            resultado.append(sentencia.graficar(miId));
        }

        resultado.append("}");

        return resultado.toString();
    }

    public String generaAssembler() {
        if (tablaSimbolos == null) {
            throw new IllegalStateException("NodoPrograma no tiene acceso a la tabla de simbolos.");
        }

        GeneradorAssemblerContext contexto = new GeneradorAssemblerContext(tablaSimbolos);
        StringBuilder codigoAsm = new StringBuilder();

        for (NodoSentencia sentencia : sentencias) {
            sentencia.generarAssembler(codigoAsm, contexto);
        }

        StringBuilder asm = new StringBuilder();
        asm.append(".MODEL LARGE\n");
        asm.append(".386\n");
        asm.append(".STACK 200h\n\n");
        asm.append(".DATA\n\n");
        asm.append(generaData(contexto));
        asm.append("\n.CODE\n\n");
        asm.append("MOV AX,@DATA\n");
        asm.append("MOV DS,AX\n");
        asm.append("MOV ES,AX\n\n");
        asm.append(codigoAsm);
        asm.append("\nMOV AX,4C00h\n");
        asm.append("INT 21h\n\n");
        asm.append("END\n");
        return asm.toString();
    }

    private String generaData(GeneradorAssemblerContext contexto) {
        StringBuilder dataAsm = new StringBuilder();

        for (Map<COLUMNA, String> fila : tablaSimbolos.getSymtabla()) {
            String nombre = fila.get(COLUMNA.NOMBRE);
            String token = fila.get(COLUMNA.TOKEN);
            String tipo = fila.get(COLUMNA.TIPO);
            String valor = fila.get(COLUMNA.VALOR);

            if (nombre == null || token == null) {
                continue;
            }

            switch (token) {
                case "ID":
                    if ("STRING".equals(tipo)) {
                        dataAsm.append(nombre).append(" db 255 dup ('$')\n");
                    } else {
                        dataAsm.append(nombre).append(" dd ?\n");
                    }
                    break;
                case "ASM_AUX":
                    dataAsm.append(nombre).append(" dd ?\n");
                    break;
                case "INT_CONST":
                    dataAsm.append(contexto.nombreConstanteInt(Integer.parseInt(valor)))
                           .append(" dd ")
                           .append(valor)
                           .append(".0\n");
                    break;
                case "FLOAT_CONST":
                    dataAsm.append(contexto.nombreConstanteFloat(Float.parseFloat(valor)))
                           .append(" dd ")
                           .append(valor)
                           .append("\n");
                    break;
                case "HEX_CONST":
                    dataAsm.append(contexto.nombreConstanteHex(nombre.substring(2)))
                           .append(" dd ")
                           .append(valor)
                           .append(".0\n");
                    break;
                case "STRING_CONST":
                    dataAsm.append(contexto.nombreConstanteString(quitarComillas(valor)))
                           .append(" db ")
                           .append(valor)
                           .append(",'$'\n");
                    break;
                default:
                    break;
            }
        }

        return dataAsm.toString();
    }

    private String quitarComillas(String valor) {
        if (valor == null) {
            return "";
        }
        if (valor.length() >= 2 && valor.startsWith("\"") && valor.endsWith("\"")) {
            return valor.substring(1, valor.length() - 1);
        }
        return valor;
    }
}
