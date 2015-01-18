JAVA_EXE = /opt/jdk1.7.0_72/bin
SRC_PATH = src
BUILD_PATH = build
JFLAGS = -g
JC = javac
JE = java
.SUFFIXES: .java .class
.java.class:
	$(JAVA_EXE)/$(JC) $(JFLAGS) -d $(BUILD_PATH) -sourcepath $(SRC_PATH) $*.java

MAIN = jp/co/cyberagent/Main
MAIN_SOURCE := src/$(MAIN).java

.PHONY: clean run

default: classes

run: classes
	$(JAVA_EXE)/$(JE) -cp $(BUILD_PATH) $(MAIN)

classes: $(MAIN_SOURCE:.java=.class)

clean:
	rm -rf build/*
