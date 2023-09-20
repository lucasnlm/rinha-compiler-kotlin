<div align="center">

![banner]

[<img src="https://img.shields.io/badge/Discord-7289DA?style=for-the-badge&logo=discord&logoColor=white">](https://discord.gg/e8EzgPscCw)

</div>

## Kotlin Rinha Interpreter

### About

Rinha interpreter is an interpreter for the "Rinha de Compiladores".
You can read more about it [here](https://github.com/aripiprazole/rinha-de-compiler).

### How to build

This project was built using [Kotlin Native](https://kotlinlang.org/docs/native-overview.html) and [better-parse](https://github.com/h0tk3y/better-parse) to implement the Rinha grammar.

To build the project, you can use the following command:

```bash
./gradlew nativeBinaries
```

You can edit / build it using IntelliJ IDEA.

### How to use

To run the interpreter, you can use the following command:

```bash
./rinhak files/print.rinha # or print.json to use the AST file
```

You can also run the interpreter directly using REPL:

```bash
./rinhak repl
```

### Help

You can use the following command to see the help:

```bash
❯ ./rinhak help
Kotlin Rinha Interpreter
  Usage: rinhak [options] [source file]
  Options:
      help          Show this help message and exit.
      repl          Run the REPL.
      <file.json>   Run from AST file. E.g: rinhak test.json
      <file.rinha>  Run from Rinha file. E.g: rinhak test.rinha
```

### Tests

To run the tests, you can use the following command:

```bash
./gradlew allTests
```

[banner]: ./img/banner.png

## Docker

You can also use the docker image to run the interpreter.

```bash
docker build -t "rinha:Dockerfile" .
docker run -it rinha:Dockerfile

# rinhak will run the json AST of rinha generated code
# rinha should already be available to use too.
./rinhak files/print.json
```

You can also call `rinha file.rinha` to generate a new Json.
