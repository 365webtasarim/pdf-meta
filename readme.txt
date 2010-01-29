The homepage can be found at: http://code.google.com/p/pdf-meta/

Motivation for this project
---------------------------
There are a lot of PDF editors and authoring tools available so why this project? 
I was in the need of a quick tool to edit the metadata of existing pdf files. 
I own an Sony PRS 505 ebook reader and this device uses the metadata embedded in 
the PDF files to display the books in a list. If this metadata is incorrect or not 
at all this makes it very hard to use them on the ebook reader.

So I needed a quick tool to do exactly this job an I found nothing that worked 
and is available for Linux. This is why I decided to write my one tool.

The tool is designed to be used on the command line or to be registered as 
an action in the filemanagers context menu.

It is very basic in it's functionality. All it can do is open a PDF and change the metadata of it.

Usage:
------
To start the tool you need Java 1.5
Launch the tool with 

java -jar pdfmeta_20100124.jar to launch the command line version  
or java -jar pdfmeta_20100124.jar -cli to launch the command line version of the tool.

(Note that the date may change from release to release)
In windows you can double click the jar file in explorer if 
Java is registered correctly to .jar files.

If you get OutOfMemory exceptions when loading big pdf files, increase
the VM memory by adding -Xmx512m to the java command.

e.g java -Xmx512m -jar pdfmeta_20100124.jar  

License:
--------
pdfmeta is distributed under BSD license. See license.txt for details.

Have fun

	- Bernd Rosstauscher