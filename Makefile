# the build variables
# source directory
SRC_PATH = src

# build target directory
BUILD_PATH = build

# java environment
# java home, if not set in environment, pass the default value
JAVA_HOME ?= /opt/jdk1.7.0_72

# build flags
JFLAGS ?= -g

# complier program
JC = javac

# execute program
JE = java

.SUFFIXES: .java .class

# build command
.java.class:
	$(JAVA_HOME)/bin/$(JC) $(JFLAGS) -d $(BUILD_PATH) -sourcepath $(SRC_PATH) $*.java

# main source
MAIN = jp/co/cyberagent/Main
MAIN_SOURCE := src/$(MAIN).java

.PHONY: clean run

default:
	@echo "build the source code"
	make classes
	@echo "build successfully"

run:
	make
	@echo "run the minesweeper game"
	$(JAVA_HOME)/bin/$(JE) -cp $(BUILD_PATH) $(MAIN)

classes: $(MAIN_SOURCE:.java=.class)

clean:
	@echo "clean build files"
	$(RM) -r build/*
