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
    push y@prompt
    call _printf
    pop ebx
    push y
    push scanf_format
    call _scanf
    pop ebx
    pop ebx
    push dword [x]
push dword 20
push dword 3
    push dword [y]
    pop ebx
    pop eax
    add eax, ebx
    push eax
    pop ebx
    pop eax
    imul eax, ebx
    push eax
    pop ebx
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
y@prompt: db 'Input y: ', 0

section .bss
x: resd 1
y: resd 1
