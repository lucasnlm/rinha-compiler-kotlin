<div align="center">

![banner]

[<img src="https://img.shields.io/badge/Discord-7289DA?style=for-the-badge&logo=discord&logoColor=white">](https://discord.gg/e8EzgPscCw)

</div>

## Rinha Interpreter

### About

Rinha interpreter is an interpreter for the "Rinha de Compiladores".
You can read more about it [here](https://github.com/aripiprazole/rinha-de-compiler).

### How to build

This project was built using [Kotlin Native](https://kotlinlang.org/docs/native-overview.html).
So, you can edit / build it using IntelliJ IDEA.

To build the project, you can use the following command:

```bash
./gradlew nativeBinaries
```

### How to use

To run the interpreter, you can use the following command:

```bash
./rinha files/print.json # or any other file
```

It will return:

```
Hello world
Time: 0ms
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
