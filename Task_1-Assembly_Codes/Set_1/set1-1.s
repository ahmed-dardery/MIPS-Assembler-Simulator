.data
msg: .asciiz"calculate summation , multiplication , subtraction of 2 numbers according to the difference between them \nenter 2 numbers\n"
.text
.globl main

main:
la $a0,msg
li $v0,4
syscall

li $v0,5
syscall
addi $s0,$v0,0	#1st number at s0

li $v0,5
syscall
add $s1,$0,$v0	#2nd number at s1

beq $s0,$s1,Equal	
blt $s0,$s1,Less

add $s0,$s0,$s1

j end

Equal:
mul $s0,$s0,$s1

j end


Less:
sub $s0,$s0,$s1

end:
addi $a0,$s0,0
li $v0,1
syscall

li $v0,10
syscall
