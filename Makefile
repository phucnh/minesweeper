# the build variables
# source directory
SRC_PATH = src

# build target directory
BUILD_PATH = build

# library directory
LIB_PATH = lib

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

# main source
PACKAGE = jp/co/cyberagent
MAIN = $(PACKAGE)/Main
MAIN_SOURCE := src/$(MAIN).java

.SUFFIXES: .java .class

# build command
.java.class:
	$(JAVA_HOME)/bin/$(JC) $(JFLAGS) \
				-target $(JVM) \
				-d $(BUILD_PATH) \
				-sourcepath $(SRC_PATH) $*.java

.PHONY: clean run run-jar

# build the source code
default:
	@echo create the build directory if not exist
	mkdir -p $(BUILD_PATH)
	@echo "build the source code"
	make classes
	@echo "build successfully"

# run from source code
run:
	make
	@echo "run the minesweeper game"
	$(JAVA_HOME)/bin/$(JE) -cp $(BUILD_PATH) $(MAIN)

# run the jar file
run-jar:
	@echo "run executable file"
	$(JAVA_HOME)/bin/$(JE) -jar $(JAR_NAME)

# test source path
TEST_SOURCE_PATH := test/src

# define test source for each components
# logic
TEST_SOURCE_LOGIC := $(TEST_SOURCE_PATH)/$(PACKAGE)/test/logic/*.java
# components
TEST_SOURCE_COMPONENTS := $(TEST_SOURCE_PATH)/$(PACKAGE)/test/components/*.java
# view
TEST_SOURCE_VIEW := $(TEST_SOURCE_PATH)/$(PACKAGE)/test/view/*.java

# test source code
TEST_SOURCE := $(TEST_SOURCE_LOGIC) \
				$(TEST_SOURCE_COMPONENTS) \
				$(TEST_SOURCE_VIEW)

# lib class path
LIB_CLASS_PATH := $(LIB_PATH)/junit-4.12.jar:$(LIB_PATH)/mockito-all-1.10.19.jar:$(LIB_PATH)/hamcrest-core-1.3.jar

# test build class path
TEST_BUILD_CLASS_PATH := $(LIB_CLASS_PATH):$(BUILD_PATH)

# test run class path
TEST_RUN_CLASS_PATH := $(LIB_CLASS_PATH):$(BUILD_PATH)/test:$(BUILD_PATH)

# test run command
TEST_CMD = $(JAVA_HOME)/bin/$(JE) -cp $(TEST_RUN_CLASS_PATH)

# test runner
TEST_RUNNER := org.junit.runner.JUnitCore

# controller files
CONTROLLER_TEST := $(subst /,.,$(PACKAGE)/test/logic/ConsoleGameControllerTest)

# view files
VIEW_TEST := $(subst /,.,$(PACKAGE)/test/view/ConsoleViewTest)

# component files
BOARD_TEST := $(subst /,.,$(PACKAGE)/test/components/BoardTest)
NUMBER_SQUARE_TEST := $(subst /,.,$(PACKAGE)/test/components/NumberSquareTest)
SQUARE_TEST := $(subst /,.,$(PACKAGE)/test/components/SquareTest)

COMPONENT_TEST := $(BOARD_TEST) $(NUMBER_SQUARE_TEST) $(SQUARE_TEST)

testdir:
	[ -d $(BUILD_PATH) ]

# build test source
build-test: testdir
	make
	@echo "create the build test folder"
	mkdir -p build/test
	@echo "compile the build source"
	$(JAVA_HOME)/bin/$(JC) -d $(BUILD_PATH)/test \
						-cp $(TEST_BUILD_CLASS_PATH) $(TEST_SOURCE)

# test all source code
test: testdir
	make build-test
	make test-controller test-view test-components

# test controller
test-controller:
	$(TEST_CMD) $(TEST_RUNNER) $(CONTROLLER_TEST)

# test view
test-view:
	$(TEST_CMD) $(TEST_RUNNER) $(VIEW_TEST)

# test components board
test-components-board:
	$(TEST_CMD) $(TEST_RUNNER) $(BOARD_TEST)

# test component square
test-components-square:
	$(TEST_CMD) $(TEST_RUNNER) $(SQUARE_TEST)

# test component num square
test-components-num-square:
	$(TEST_CMD) $(TEST_RUNNER) $(NUMBER_SQUARE_TEST)

# test all components
test-components:
	make test-components-board test-components-square test-components-num-square

classes: $(MAIN_SOURCE:.java=.class)

# clean build source and test file
clean:
	@echo "clean build and jar files"
	$(RM) -r $(BUILD_PATH)/*
