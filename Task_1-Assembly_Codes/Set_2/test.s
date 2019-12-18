.data
array: .space 1024
.text

.globl main
main:
	li $v0, 5 #read n
	syscall
	
	addi $s5, $v0, 0 
	
	la $a0, array
	la $s3, array
	li $a1, 1024
	li $v0, 8
	syscall
	
	la $a0, array #reload array
    move $a0,$s3 # primary address = t0 address (load pointer)
    li $v0,4 # print
    syscall
		 
	addi $s0, $0, 0 #s0 = i = 0
	addi $s1, $0, 0 #s0 = caps = 0
	addi $s2, $0, 0 #s0 = smalls = 0

Loop:
	beq $s0, $s5 end #if( i == n) break
	#get index of i
	sll $t0, $s0, 2 #$t0 = i*4 (byte offset)
	add $t0, $t0, $s3 #address of array[i]
	lw $t1, ($t0)
	#check if small or cap
	srl $t1, $t1, 5 #shift to right by 5 bits
	addi $t2, $0, 3
	addi $s0, $s0, 1 # i = i+1
	
	beq $t2, $t1  incSmall
	addi $s1, $s1, 1
	j Loop
	
incSmall:
	addi $s2, $s2, 1
	j Loop

end:
	li $v0, 1
	addi $a0, $s2, 0
	syscall
	addi $a0, $s1, 0
	syscall
	li $v0, 10
	syscall
.end main
