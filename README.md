# Introduction

This project was [born out of rage][blog1]; I was fed up with the Java
logging frameworks and decided to do something drastic about it. 

This utility uses bytecode manipulation to remove all traces of
[Log4J][log4j] from any jar file:

 * `log()` and `warn()` becomes `System.out.println()`
 * `error()` and `fatal()` becomes `System.err.println()` 
 * Other statements are replaced with NOOP operations if
   possible. Note however, that there are some some advanced usage
   scenarios that WoodChipper doesn't know how to handle. But if your
   are only doing basic logging you should be fine.
 * `log4j.properties` is removed

## Installation 

You need maven installed.

    git clone http://github.com/fred-o/woodchipper
    cd woodchipper
    mvn package
   
This will create `woodchipper/target/woodchipper.jar`. Copy this jar
file to wherever its handy.

## Running

To instrument a jar file:

    java -jar woodchipper.jar -i <input.jar> -o <output.jar>

The `-o` argument is optional. If left out, the input jar is modified
in place.

## Status

This project is in an early stage and should by no means be considered
stable. Right now it only handles [Log4J][log4j], but 'support' for
[Java Logging][jdklog] and [Commons Logging][commonslog] is planned.

[blog1]:http://mulli.nu/2010/06/22/nolog.html
[log4j]:http://logging.apache.org/log4j/1.2/
[jdklog]:http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html
[commonslog]:http://commons.apache.org/logging/
