#!/bin/bash

if [ $# -eq '0' ]
    then echo 'usage: run_interpreter.sh <program>'
    else java -jar ./bin/artifacts/GeneticSortingInterpreter_jar/GeneticSorting.jar $1
fi


