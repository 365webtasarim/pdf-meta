# Program Usage: #

To start the tool you need Java 1.6 installed on your computer.

If you do not have it installed goto http://www.java.com and download it from there.

Launch the tool with

`java -jar pdfmeta_20100124.jar`

to launch the graphical version or

`java -jar pdfmeta_20100124.jar -cli`

to launch the command line version of the tool. (Note that the date in the above jar file may change from release to release)

In Microsoft Windows you can double click the jar file in explorer if
Java is registered correctly to .jar files.

If you like a more compact editor that is directly registered to edit single pdf files you can launch the gui version with the -edit option like this:

`java -jar pdfmeta_20100124.jar -edit /path/to/pdf/file.pdf`

This will load the file directly into the editor in a compact view.

If you get "Out Of Memory" exceptions when loading big pdf files, increase
the VM memory by adding -Xmx512m to the java command.

`java -Xmx512m -jar pdfmeta_20100124.jar`