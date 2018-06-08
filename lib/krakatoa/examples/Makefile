
#
# Compiler/Linker flags specific to Linux
#

CC = cc
LD = ld 
CFLAGS = -O -fwritable-strings -Wall -Wno-parentheses -Wno-unused -Wno-return-type -Wno-implicit-in
PICFLAGS = -fpic
SOFLAGS = -G


# Makefile section common to all platforms
#

CLASSES = Circle.class Env.class JDBC.class LO.class 

JARS = Env.jar Circle.jar JDBC.jar LO.jar 

LIBSO = circle.so

TARGETS =${CLASSES} ${JARS} ${LIBSO}

.SUFFIXES: .java .class .c .o .jar

.java.class: 
	javac $<

.class.jar:
	jar -cf `echo $< | sed -e "s/\.class/\.jar/"` $<

all: $(TARGETS)
	rm -f *.class

circle.so: circle.c
	$(CC) $(CFLAGS) $(PICFLAGS) -I${INFORMIXDIR}/incl/public -c circle.c
	$(LD) $(SOFLAGS) -o circle.so circle.o
	chmod 755 circle.so

clean::
	rm -f ${LIBSO} *.o *.class  ${JARS} *.so
