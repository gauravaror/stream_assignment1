#!/bin/bash
javac -classpath  stdlib.jar Morris.java Hash.java CMS.java CMS_default.java CMS_conservative.java CMS_Morris.java TestCMS.java Stream.java
#java -cp stdlib.jar:./ TestCMS 100 1000 Stocks/processed/aame.us.txt.processed aame
#java -cp stdlib.jar:./ TestCMS 32 200 aame.us.txt.processed query.file
java -cp stdlib.jar:./ TestCMS 1 2000000 10000000 0.3
echo "Running on all of the data"
#java -cp stdlib.jar:./ TestCMS 1 200 data query.file
