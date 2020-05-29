section .text
    global _main
    extern _printf
_main:
push dword 2
push dword 8
    pop ebx
    pop eax
    add eax, ebx
    push eax
push dword 10
    pop ebx
    pop eax
    imul eax, ebx
    push eax
push dword 5
    pop ebx
    pop eax
    sub eax, ebx
    push eax
    push message
    call _printf
    pop ebx
    pop ebx
    ret
message:
    db 'Result is %d', 10, 0
