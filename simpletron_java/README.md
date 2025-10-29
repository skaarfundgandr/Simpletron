# JAVA SIMPLETRON

## How to run

Prerequisites: Java 11+ and Maven

1. Build the project:

  ```sh
  mvn clean package
  ```

2. Run the built jar:

  ```sh
  java -jar target/*.jar <filename>.<extension> <arguments>
  ```

  Or specify the exact artifact:

  ```sh
  java -jar target/<artifact>-<version>.jar <filename>.<extension> <arguments>
  ```

## Notes

- Arguments are optional.
- Two filetypes are supported `.sml` which assumes that the program is already precompiled and `.cml` which is a high level simpletron program.
- Accepted arguments:

  - `-s` runs the program in sequential mode.

  - `-c` force compiles the program.

  - `-d` dumps memory and registers after execution.
