.data
msg: .asciiz"nested loop on array and change arr[i*2] to i-j\nenter tow numbers\n"
space: .asciiz" "
newLine: .asciiz"\n"
.text
.globl main

main:
la $a0,msg
li $v0,4
syscall

li $v0,5
syscall

addi $s0,$v0,0	#a

li $v0,5
syscall

addi $s1,$v0,0	#b

addi $a0,$s0,0
sll $a0,$a0,3

li $v0,9	#dynamically allocate memory of size a0
syscall

addi $s3,$v0,0	#address of allocated memory in s3

li $t0,0	#i

firstLoop:
beq $t0,$s0,end
li $t1,0	#j

	secondLoop:
	beq $t1,$s1,endOfSecondLoop
	
	
	addi $a0,$t0,0
	
	sll $t3,$t0,3	#t3 = t0*8
	add $t3,$t3,$s3	#t3=arr[t3]
	
	
	sub $t3,$t0,$t1	#t3= t0-t1
	
	addi $a1,$t3,0
	
	jal trace
	
	addi $t1,$t1,1	#t1++
	j secondLoop

endOfSecondLoop:

syscall

addi $t0,$t0,1
j firstLoop

end:
li $v0,10
syscall


trace:
li $v0,1
syscall

la $a0,space

li $v0,4
syscall

addi $a0,$a1,0

li $v0,1
syscall

la $a0,newLine

li $v0,4
syscall

jr $ra

