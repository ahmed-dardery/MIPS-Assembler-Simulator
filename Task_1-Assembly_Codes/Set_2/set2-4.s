.data

odd_message  : .asciiz "Odd Number    "
even_message : .asciiz "Even Number   "
input        : .ascii  "Enter Number: "

.text

.globl main
isOdd:
	addi $sp,$sp,-4 # "push" operations pre-decrement the stack pointer  
	sw $ra, 0($sp)  # Save the current return address on the stack
	
	jal isEven # is even result stored in $s1
	nor $s1, $s1, $s0 #negate the result (a ~| 0) = NOT(a | 0) = NOT(a) 
	
	lw $ra , 0($sp) # Restore the old return address
	addi $sp,$sp,4  # "pop" operations post-increment the stack pointer
	jr $ra
	
isEven:
	addi $sp,$sp,-4 # "push" operations pre-decrement the stack pointer  
	sw $ra, 0($sp)  # Save the current return address on the stack
	
	li $t1,2 # load 2
	div $s1,$t1 # n/2 -> hi = n%2
	mfhi $s1 # s1 = n%2
	nor $s1, $s1, $s0 #negate the result (a ~| 0) = NOT(a | 0) = NOT(a) 
	
	lw $ra , 0($sp) # "push" operations pre-decrement the stack pointer  
	addi $sp,$sp,4  # "pop" operations post-increment the stack pointer
	jr $ra
main:
	li $s0,0 # $s0 = 0
	
	la $a0, input #reload byte space to primary address
    li $v0,4 # print string
    syscall
	
	li $v0, 5 #read n
	syscall
	addi $s1, $v0, 0 # s1 = n
	
	jal isOdd #goto isOdd Function and its result stored in $s1
	
	beq $s1,$s0,PRINT_EVEN # if $s1 = 0 then the number is even
	
	la $a0, odd_message #reload byte space to primary address
    li $v0,4 # print string
    syscall
	j end

PRINT_EVEN:
	la $a0, even_message #reload byte space to primary address
    li $v0,4 # print string
    syscall
	j end	
end:
li  $v0, 10
	syscall
.end main