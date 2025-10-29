; Sample program - adds two numbers
; Variables: a, b, c

VARIABLE a
VARIABLE b
VARIABLE c

; Initialize variables

; Jump to start label
start: READ a
       READ b
       LOADM a
       ADDM b
       STORE c
       WRITE c
       HALT
