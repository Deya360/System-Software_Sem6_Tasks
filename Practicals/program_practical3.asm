section .text
    global _main
    extern _printf
    extern _scanf

_main:
    push x@prompt
    call _printf
    pop ebx
    push x
    push scanf_format
    call _scanf
    pop ebx
    pop ebx
    push dword 4
    mov ebx, dword [x]
    pop eax
    add eax, ebx
    push eax
    push message
    call _printf
    pop ebx
    pop ebx
    ret

section .rdata
message: db 'Result is %d', 10, 0
scanf_format: db '%d', 0
x@prompt: db 'Input x: ', 0

section .bss
x: resd 1
