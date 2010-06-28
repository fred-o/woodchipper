# Introduction

This project was [born out of rage][blog1]; I was fed up with the Java
logging frameworks and decided to do something drastic about it. 

This utility instruments jar files by removing all references to
[Log4J][log4j] logging statements and replacing them with
`System.out.println()` (for `log()` and `warn()`) or
`System.err.println()` (for `error()` and `fatal()`).

## Installation 

You need maven installed.

   > git clone http://github.com/fred-o/woodchipper
   
   > cd woodchipper
   
   > mvn package
   
This will create `woodchipper/target/woodchipper.jar`. Copy this jar
file to wherever its handy.

## Running

To instrument a jar file

   > java -jar woodchipper.jar -i input.jar -o output.jar

## Status

This project is in an early stage and should by no means be considered
stable. Right now it only handles [Log4J][log4j], but 'support' for
[Java Logging][jdklog] and [Commons Logging][commonslog] is planned.

[blog1]:http://mulli.nu/2010/06/22/nolog.html
[log4j]:http://logging.apache.org/log4j/1.2/
[jdklog]:http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html
[commonslog]:http://commons.apache.org/logging/
