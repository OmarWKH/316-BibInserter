#BibInserter
BibInserter might add a key to your registery: HKEY_CURRENT_USER\SOFTWARE\JavaSoft\Prefs\omarwkh

##Files:
- JAR
	- BibInserter.jar
- Source Code
	-  src/
- Dependency JARs
	- lib/
- JavaDoc
	- docs/
- Gradle Build Tool
	- gradle.build
	- gradle/
	- gradlew
	- gradlew.bat
- Git
	- .git
	- .gitignore
- Instructions
	- README.md

##Dependencies:
- JKeyMaster: Global Hotkeys.
	- com.github.tulskiy:jkeymaster:1.2
- SLF4J - Logging. Used by JKeyMaster
	- org.slf4j:slf4j-jdk14:1.7.13

##How to compile:
- Add source code to your IDE and reference dependency JARs.
or
- Run `gradlew build`. You'll find .class files in build/classes/ and JAR file in build/libs/. For more info: https://docs.gradle.org/current/userguide/gradle_wrapper.html#using_wrapper_scripts

##How to run:
- Run BibInserter.jar.
or
- Run java -jar BibInserter.jar [optional-bib-file-path] to see output.