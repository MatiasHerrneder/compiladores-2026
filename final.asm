include macros2.asm	; Inclusión de macros de la cátedra
include number.asm	; Inclusión de macros de la cátedra
.MODEL LARGE		; Modelo de Memoria
.386			; Tipo de Procesador
STACK 200h		; Bytes en el Stack

.DATA
  _a 	dd 	?
  _b 	dd 	?
  _c 	dd 	?
  _x 	dd 	?
  _z 	dd 	?
  _msg 	dd 	?
  _10 	dd 	10.0
  _20 	dd 	20.0
  _123 	dd 	123.0
  _3_14 	dd 	3.14
  _2786 	dd 	2786.0
  _hola_mundo 	db 	"hola mundo", '$'
  _a_y_b_son_iguales 	db 	"a y b son iguales", '$'
  _5 	dd 	5.0
  _2_0 	dd 	2.0
  _condicion_compuesta_verdadera 	db 	"condicion compuesta verdadera", '$'
  _1 	dd 	1.0
  _12 	dd 	12.0
  _comparador 	dd 	?
  _contador   	dd 	?
  _0          	dd 	0.0
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
  @aux12 	dd 	?
  @aux13 	dd 	?
  @aux14 	dd 	?
  @aux15 	dd 	?
  @aux16 	dd 	?
  @aux17 	dd 	?
  @aux18 	dd 	?
  @aux19 	dd 	?
  @aux20 	dd 	?

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
  fstp _c		; Asigna sacando el valor de la FPU
  fld _3_14		; Carga el resultado final de la expresion
  fstp _x		; Asigna sacando el valor de la FPU
  fld _2786		; Carga el resultado final de la expresion
  fstp _z		; Asigna sacando el valor de la FPU
; --- ASIGNACION DE STRING OMITIDA (USO DE LITERAL EN HOJAS) ---
; --- INICIO DE UN BLOQUE IF ---
  fld _a		; Carga operando izquierdo
  fld _b		; Carga operando derecho
  fxch			; Intercambia ST(0) y ST(1) para restar correcto
  fcom			; Compara reales en FPU
  fstsw ax		; Copia registro de estado de FPU a AX
  sahf			; Mueve los bits de AX a FLAGS de CPU
  ffree ST(0)		; Libera espacio FPU
  ffree ST(1)		; Libera espacio FPU
  jne Etiq_Else_1
; --- BLOQUE THEN ---
; --- SENTENCIA SHOW (IMPRESION) ---
  mov dx, OFFSET _a_y_b_son_iguales	; Carga la direccion real de la cadena
  mov ah, 9			; Funcion de DOS para imprimir string
  int 21h			; Interrupcion de sistema

  jmp Etiq_Final_2
Etiq_Else_1:
; --- BLOQUE ELSE ---
; --- SENTENCIA SHOW (IMPRESION) ---
; =================================================
; GENERACION DE LA FUNCION ESPECIAL #IGUALES
; =================================================
  fld _a		; Carga el resultado final de la expresion
  fstp _comparador		; Asigna sacando el valor de la FPU
  fld _0		; Carga el resultado final de la expresion
  fstp _contador		; Asigna sacando el valor de la FPU
; --- INICIO DE UN BLOQUE IF ---
  fld _comparador		; Carga operando izquierdo
  fld _a		; Carga operando derecho
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
  fld _a		; Carga operando derecho
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
  fld _10		; Carga operando derecho
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

; --- INICIO DE UN BLOQUE IF ---
  fld _5		; Carga operando izquierdo a ST(0)
  fld _5		; Carga operando derecho a ST(0), desplazando el anterior
  fadd			; Suma ST(1) + ST(0) y hace pop
  fstp @aux14		; Descarga el resultado en el temporal

  fld _comparador		; Carga operando izquierdo
  fld @aux14		; Carga operando derecho
  fxch			; Intercambia ST(0) y ST(1) para restar correcto
  fcom			; Compara reales en FPU
  fstsw ax		; Copia registro de estado de FPU a AX
  sahf			; Mueve los bits de AX a FLAGS de CPU
  ffree ST(0)		; Libera espacio FPU
  ffree ST(1)		; Libera espacio FPU
  jne Etiq_Else_12
; --- BLOQUE THEN ---
  fld _contador		; Carga operando izquierdo a ST(0)
  fld _1		; Carga operando derecho a ST(0), desplazando el anterior
  fadd			; Suma ST(1) + ST(0) y hace pop
  fstp @aux15		; Descarga el resultado en el temporal

  fld @aux15		; Carga el resultado final de la expresion
  fstp _contador		; Asigna sacando el valor de la FPU
Etiq_Else_12:
; --- FIN DEL BLOQUE IF ---

; =================================================
; FIN DE LA FUNCION ESPECIAL #IGUALES
; =================================================

  DisplayFloat _contador, 2	; Invoca macro de impresion para numeros

Etiq_Final_2:
; --- FIN DEL BLOQUE IF ---

; --- INICIO DE UN BLOQUE IF ---
; --- EVALUACION AND (PARTE 1) ---
; --- EVALUACION DE NOT ---
  fld _a		; Carga operando izquierdo
  fld _b		; Carga operando derecho
  fxch			; Intercambia ST(0) y ST(1) para restar correcto
  fcom			; Compara reales en FPU
  fstsw ax		; Copia registro de estado de FPU a AX
  sahf			; Mueve los bits de AX a FLAGS de CPU
  ffree ST(0)		; Libera espacio FPU
  ffree ST(1)		; Libera espacio FPU
  je Etiq_Falso_And_18
; --- EVALUACION AND (PARTE 2) ---
  fld _x		; Carga operando izquierdo
  fld _2_0		; Carga operando derecho
  fxch			; Intercambia ST(0) y ST(1) para restar correcto
  fcom			; Compara reales en FPU
  fstsw ax		; Copia registro de estado de FPU a AX
  sahf			; Mueve los bits de AX a FLAGS de CPU
  ffree ST(0)		; Libera espacio FPU
  ffree ST(1)		; Libera espacio FPU
  jbe Etiq_Falso_And_18
Etiq_Falso_And_18:
  nop			; Punto de salida del falso cortocircuito Etiq_Else_16
; --- BLOQUE THEN ---
; --- SENTENCIA SHOW (IMPRESION) ---
  mov dx, OFFSET _condicion_compuesta_verdadera	; Carga la direccion real de la cadena
  mov ah, 9			; Funcion de DOS para imprimir string
  int 21h			; Interrupcion de sistema

Etiq_Else_16:
; --- FIN DEL BLOQUE IF ---

; --- SENTENCIA SHOW (IMPRESION) ---
  DisplayFloat _z, 2	; Invoca macro de impresion para numeros

; --- INICIO DE UN BUCLE REPEAT ---
Etiq_Inicio_Repeat_19:
  fld _a		; Carga operando izquierdo a ST(0)
  fld _1		; Carga operando derecho a ST(0), desplazando el anterior
  fsub			; Resta ST(1) - ST(0) y hace pop
  fstp @aux20		; Descarga el resultado en el temporal

  fld @aux20		; Carga el resultado final de la expresion
  fstp _a		; Asigna sacando el valor de la FPU
; --- SENTENCIA SHOW (IMPRESION) ---
  DisplayFloat _a, 2	; Invoca macro de impresion para numeros

; --- EVALUACION DE CONDICION UNTIL ---
  fld _a		; Carga operando izquierdo
  fld _12		; Carga operando derecho
  fxch			; Intercambia ST(0) y ST(1) para restar correcto
  fcom			; Compara reales en FPU
  fstsw ax		; Copia registro de estado de FPU a AX
  sahf			; Mueve los bits de AX a FLAGS de CPU
  ffree ST(0)		; Libera espacio FPU
  ffree ST(1)		; Libera espacio FPU
  jae Etiq_Inicio_Repeat_19
; --- FIN DEL BUCLE REPEAT ---

; --- SENTENCIA SHOW (IMPRESION) ---
  DisplayFloat _a, 2	; Invoca macro de impresion para numeros

; --- SENTENCIA SHOW (IMPRESION) ---
  mov dx, OFFSET _hola_mundo	; Carga la direccion real de la cadena
  mov ah, 9			; Funcion de DOS para imprimir string
  int 21h			; Interrupcion de sistema


  mov ax, 4c00h		; Finaliza la ejecucion
  int 21h
END START
