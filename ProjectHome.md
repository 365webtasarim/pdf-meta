## Motivation for this project ##
There are a lot of PDF editors and authoring tools available so why this project? I was in the need of a quick tool to edit the metadata of existing pdf files. I own an Sony PRS 505 ebook reader and this device uses the metadata, embedded in the PDF files, to display the books in a list. If this metadata is incorrect or not filled at all, this makes it very hard to use them on the ebook reader.

So I needed a quick tool to do exactly this job and I found nothing that worked for Linux. This is why I decided to write my own small program.

The tool is designed to be used on the command line or to be registered as an action in the filemanagers context menu.

## Feature List ##
It is very basic in it's functionality. All it can do is open a PDF and change the metadata of it. It uses the [iText library](http://itextpdf.com/) to read and write PDF files.
For more details about the features see also the [Usage](Usage.md), FileAssociation or ChangeHistory wiki pages.

## Feeback ##
The tool is a first drop and maybe I will improve it when I have some spare time. pdf-meta is released under BSD license and comes with no warranty. If you like the tool or have suggestions drop me a note in [Issue Tracker](http://code.google.com/p/pdf-meta/issues/list) or email me. If you like to participate in any direction you are welcome.

Have fun,

- Bernd Rosstauscher