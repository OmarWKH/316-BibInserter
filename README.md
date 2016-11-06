#BibInserter
A tool to search through entries in a .bib file and insert their key for citation.

BibInserter might add a key to your registery: HKEY_CURRENT_USER\SOFTWARE\JavaSoft\Prefs\omarwkh

##Dependencies:
- JKeyMaster: Global Hotkeys.
	- com.github.tulskiy:jkeymaster:1.2
- SLF4J - Logging. Used by JKeyMaster
	- org.slf4j:slf4j-jdk14:1.7.13

##How to compile:
- Run `gradlew build`. You'll find .class files in build/classes/ and JAR file in build/libs/. For more info: https://docs.gradle.org/current/userguide/gradle_wrapper.html#using_wrapper_scripts

##How to run:
- Run BibInserter.jar.
or
- Run java -jar BibInserter.jar [optional-bib-file-path] to see output.
