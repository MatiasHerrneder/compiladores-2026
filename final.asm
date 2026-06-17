include macros2.asm	; Inclusión de macros de la cátedra
include number.asm	; Inclusión de macros de la cátedra
.MODEL LARGE		; Modelo de Memoria
.386			; Tipo de Procesador
STACK 200h		; Bytes en el Stack

.DATA
  _a 	dd 	?
  _b 	dd 	?
  _w 	dd 	?
  _x 	dd 	?
  _e 	dd 	?
  _f 	dd 	?
  _10 	dd 	10.0
  _20 	dd 	20.0
  _123 	dd 	123.0
  _3 	dd 	3.0
  _5 	dd 	5.0
  _comparador 	dd 	?
  _contador   	dd 	?
  _0          	dd 	0.0
  _1          	dd 	1.0
  @aux1 	dd 	?
  @aux2 	dd 	?
  @aux3 	dd 	?
  @aux4 	dd 	?
  @aux5 	dd 	?
  @aux6 	dd 	?
  @aux7 	dd 	?
  @aux8 	dd 	?
  @aux9 	dd 	?
  @aux10 	dd 	?
  @aux11 	dd 	?

.CODE
START:
  mov AX, @DATA		; Inicializa el segmento de datos
  mov DS, AX
  mov ES, AX

  fld _10		; Carga el resultado final de la expresion
  fstp _a		; Asigna sacando el valor de la FPU
  fld _20		; Carga el resultado final de la expresion
  fstp _b		; Asigna sacando el valor de la FPU
  fld _123		; Carga el resultado final de la expresion
  fstp _e		; Asigna sacando el valor de la FPU
  fld _3		; Carga el resultado final de la expresion
  fstp _x		; Asigna sacando el valor de la FPU
  fld _5		; Carga el resultado final de la expresion
  fstp _f		; Asigna sacando el valor de la FPU
; --- SENTENCIA SHOW (IMPRESION) ---
; =================================================
; GENERACION DE LA FUNCION ESPECIAL #IGUALES
; =================================================
  fld _a		; Carga operando izquierdo a ST(0)
  fld _w		; Carga operando derecho a ST(0), desplazando el anterior
  fadd			; Suma ST(1) + ST(0) y hace pop
  fstp @aux1		; Descarga el resultado en el temporal

  fld @aux1		; Carga operando izquierdo a ST(0)
  fld _b		; Carga operando derecho a ST(0), desplazando el anterior
  fdiv			; Divide ST(1) / ST(0) y hace pop
  fstp @aux2		; Descarga el resultado en el temporal

  fld @aux2		; Carga el resultado final de la expresion
  fstp _comparador		; Asigna sacando el valor de la FPU
  fld _0		; Carga el resultado final de la expresion
  fstp _contador		; Asigna sacando el valor de la FPU
; --- INICIO DE UN BLOQUE IF ---
  fld _comparador		; Carga operando izquierdo
  fld _x		; Carga operando derecho
  fxch			; Intercambia ST(0) y ST(1) para restar correcto
  fcom			; Compara reales en FPU
  fstsw ax		; Copia registro de estado de FPU a AX
  sahf			; Mueve los bits de AX a FLAGS de CPU
  ffree ST(0)		; Libera espacio FPU
  ffree ST(1)		; Libera espacio FPU
  jne Etiq_Else_3
; --- BLOQUE THEN ---
  fld _contador		; Carga operando izquierdo a ST(0)
  fld _1		; Carga operando derecho a ST(0), desplazando el anterior
  fadd			; Suma ST(1) + ST(0) y hace pop
  fstp @aux5		; Descarga el resultado en el temporal

  fld @aux5		; Carga el resultado final de la expresion
  fstp _contador		; Asigna sacando el valor de la FPU
Etiq_Else_3:
; --- FIN DEL BLOQUE IF ---

; --- INICIO DE UN BLOQUE IF ---
  fld _comparador		; Carga operando izquierdo
  fld _e		; Carga operando derecho
  fxch			; Intercambia ST(0) y ST(1) para restar correcto
  fcom			; Compara reales en FPU
  fstsw ax		; Copia registro de estado de FPU a AX
  sahf			; Mueve los bits de AX a FLAGS de CPU
  ffree ST(0)		; Libera espacio FPU
  ffree ST(1)		; Libera espacio FPU
  jne Etiq_Else_6
; --- BLOQUE THEN ---
  fld _contador		; Carga operando izquierdo a ST(0)
  fld _1		; Carga operando derecho a ST(0), desplazando el anterior
  fadd			; Suma ST(1) + ST(0) y hace pop
  fstp @aux8		; Descarga el resultado en el temporal

  fld @aux8		; Carga el resultado final de la expresion
  fstp _contador		; Asigna sacando el valor de la FPU
Etiq_Else_6:
; --- FIN DEL BLOQUE IF ---

; --- INICIO DE UN BLOQUE IF ---
  fld _comparador		; Carga operando izquierdo
  fld _f		; Carga operando derecho
  fxch			; Intercambia ST(0) y ST(1) para restar correcto
  fcom			; Compara reales en FPU
  fstsw ax		; Copia registro de estado de FPU a AX
  sahf			; Mueve los bits de AX a FLAGS de CPU
  ffree ST(0)		; Libera espacio FPU
  ffree ST(1)		; Libera espacio FPU
  jne Etiq_Else_9
; --- BLOQUE THEN ---
  fld _contador		; Carga operando izquierdo a ST(0)
  fld _1		; Carga operando derecho a ST(0), desplazando el anterior
  fadd			; Suma ST(1) + ST(0) y hace pop
  fstp @aux11		; Descarga el resultado en el temporal

  fld @aux11		; Carga el resultado final de la expresion
  fstp _contador		; Asigna sacando el valor de la FPU
Etiq_Else_9:
; --- FIN DEL BLOQUE IF ---

; =================================================
; FIN DE LA FUNCION ESPECIAL #IGUALES
; =================================================

  DisplayFloat _contador, 2	; Invoca macro de impresion para numeros


  mov ax, 4c00h		; Finaliza la ejecucion
  int 21h
END START
