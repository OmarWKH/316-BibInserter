# BibInserter
A tool to search through entries in a .bib file and insert their key for citation. Homework for SWE316 in KFUPM.

BibInserter might add a key to your registery: HKEY_CURRENT_USER\SOFTWARE\JavaSoft\Prefs\omarwkh

## Dependencies:
- JKeyMaster: Global Hotkeys.
	- com.github.tulskiy:jkeymaster:1.2
- SLF4J - Logging. Used by JKeyMaster
	- org.slf4j:slf4j-jdk14:1.7.13

## Javadocs
https://omarwkh.github.io/316-BibInserter/

## How to compile?
- Run `gradlew build`. You'll find .class files in build/classes/ and JAR file in build/libs/. For more info: https://docs.gradle.org/current/userguide/gradle_wrapper.html#using_wrapper_scripts

## How to run?
- Run BibInserter.jar.
- To see output: Run java -jar BibInserter.jar [optional-bib-file-path].

## To-Do
- using hotstrings
- changing hotkeys/hotstrings
- proper search window size (now it's maximized)
- move preferences to a file instead of registery
- don't override user's clipboard content
- multiple .bib files?
- file reading errors?
- file content errors?
- reading/searching efficiency?
