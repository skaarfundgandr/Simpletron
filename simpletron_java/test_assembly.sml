; Test assembly file with .sml extension
; This should be compiled with -c flag

VARIABLE sum
VARIABLE counter

sum 0
counter 5

loop: LOADM counter
      WRITE counter
      LOADM sum
      ADDM counter
      STORE sum
      LOADM counter
      SUBI 1
      STORE counter
      JZ end
      JMP loop

end: LOADM sum
     WRITE sum
     HALT
