.data
arr: .word 1
msg: .asciiz"loop on string calculating number of lowercase and uppercase characters \nenter the number of character of the string \n"
upperCase: .asciiz"number of uppercase characters is "
lowerCase: .asciiz"number of lowercase characters is "
newLine: .asciiz"\n"

.text

.globl main
main:
#load the word
la $a0,msg
li $v0,4
syscall

li $v0,5
syscall
addi $a1,$v0,2  #2 for new line and null character 

li $v0,8  
la $a0,arr	#a0 = address of word
syscall


#count upper and lower characters

li $s0,0  #counter (i)
li $s1,0  #upper case
li $s2,0  #lower case
li $s3,91 #less than Z char
li $s4,10 #newLine

loop:

	beq $s0,$a1,done

	add $t0,$s0,$a0	#t0=arr[i]

	lb $t1,0($t0)  #load byte from start of t0 at t1

	beq $t1,$s4,done	#if t1=='\n' terminate
	beq $t1,$0,done		#if t1=='\0' terminate

	blt $t1,$s3, upper	#t1 is upper case
	
	addi $s2,$s2,1		#t1 is lower case
	j increament

	upper:
	addi $s1,$s1,1

	increament:
	addi $s0,$s0,1

j loop 

done :
la $a0,lowerCase
li $v0,4
syscall

addi $a0,$s2,0
li $v0,1
syscall

la $a0,newLine
li $v0,4
syscall

la $a0,upperCase
li $v0,4
syscall

addi $a0,$s1,0
li $v0,1
syscall

li $v0,10
syscall