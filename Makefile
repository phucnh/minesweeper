# the build variables
# source directory
SRC_PATH = src

# build target directory
BUILD_PATH = build

# jar path
JAR_PATH = jar

# jar name
JAR_NAME = minesweeper.jar

# java environment
# java home, if not set in environment, pass the default value
JAVA_HOME ?= /opt/jdk1.7.0_72

# build flags
JFLAGS ?= -g

# complier program
JC = javac

# execute program
JE = java

# make jar command
JAR = jar

# java version
JVM = 1.7

.SUFFIXES: .java .class

# build command
.java.class:
	$(JAVA_HOME)/bin/$(JC) $(JFLAGS) -target $(JVM) -d $(BUILD_PATH) -sourcepath $(SRC_PATH) $*.java

# main source
MAIN = jp/co/cyberagent/Main
MAIN_SOURCE := src/$(MAIN).java

.PHONY: clean run

default:
	@echo create the build directory if not exist
	mkdir -p $(BUILD_PATH)
	@echo "build the source code"
	make classes
	@echo "build successfully"

run:
	make
	@echo "run the minesweeper game"
	$(JAVA_HOME)/bin/$(JE) -cp $(BUILD_PATH) $(MAIN)

run-jar:
	@echo "run executable file"
	$(JAVA_HOME)/bin/$(JE) -jar $(JAR_NAME)

classes: $(MAIN_SOURCE:.java=.class)

clean:
	@echo "clean build and jar files"
	$(RM) -r $(BUILD_PATH)/*
