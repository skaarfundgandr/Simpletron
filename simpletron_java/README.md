# JAVA SIMPLETRON

### How to run

Prerequisites: Java 8+ and Maven

1. Build the project:
    ```
    mvn clean package
    ```
2. Run the built jar:
    ```
    java -jar target/*.jar <filename>.<extension> <arguments>
    ```
    Or specify the exact artifact:
    ```
    java -jar target/<artifact>-<version>.jar <filename>.<extension> <arguments>
    ```
### Notes

- Arguments are optional.
- Two filetypes are supported `.sml` which assumes that the program is already precompiled and `.cml` which is a high level simpletron program.
- Currently the arguments that are accepted is `-s` which runs the program in sequential mode and `-c` which force compiles the program.
