# Introduction #

The tool works best when you associate the program with .pdf files.
Once this is done you can select a pdf file invoke the context menu and select.
Open with -> PDF-Meta.
The following sections describe how this is done in different desktop systems.


# KDE 4 #
(I do not have an english KDE running so the description is freely translated and buttons may be named different.)

  * Go to the system settings.
  * Go to the advanced tab
  * Search for pdf or select the application->pdf entry in the tree
  * Press "Add" in the programs list
  * Browse to the pdf-meta jar file and select it.
  * Now edit the newly created entry and provide as program name "Metadata Updater"
  * Edit the program command to something like this: "/usr/bin/java -jar pdfmeta\_20100228.jar %F"

If you do not like the multi file edit mode you can also launch the application in "single file edit" mode with the following command.

"/usr/bin/java -jar pdfmeta\_20100228.jar -edit %F"

Then the application will launch in a more compact layout that will allow quicker edit of single files.

# Windows #
Comming soon.