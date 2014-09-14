GeneticSorting
==============

A genetic programming (GP) framework for developing sorting algorithms.

It is able to generate simple iterative sorting algorithms, that can later executed through the
provided interpreter.

You can find an example in "testprogram.gp", that is a Bubble Sort, written in the language used
by this framework for writing sorting programs.


Execution instructions
----------------------

You can execute the program for generating sorting algorithms, by running the script "run_gp.sh".
It takes as parameter a path for the output file.
 
You can use a previously generated program to sort sequences, by running the script "run_interpreter".
It takes the path to the file containing the program to interpret. Once launched, it takes as input
a list of integers, all on the same line and separated by space. It outputs the same integers sorted
from the smallest to the greatest.

Building instructions
---------------------
To compile the program starting from the source code, you can use the provided ant script. 
It requires Apache Ant.