#!/bin/bash

if [ $# -eq '0' ]
    then echo 'usage: run_gp.sh <out-file>'
    else java -jar ./bin/artifacts/GeneticSorting_jar/GeneticSorting.jar $1
fi


