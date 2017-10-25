WorkshopTracker
===============
Version: 0.1 beta, released 12.09.2017 08:34GMT

Requires
========
- Windows or any Unix/Linux OS distribution (tested with Windows7 and
- Java JRE 1.8 or above installed
- java executable configured in PATH

User instructions:
=================
1. Unarchive WorkshopTracker.zip to any folder with r/w rights
2. Goto this folder
3. You may decide to add Talk input in a text file, or enter (copy paste) to the app directly.
4. On Unix/Linux command line: start app with ./run [inputfile.txt]
   On Windows command line: start app with run.bat [inputfile.txt]
5. Mark end of input sequence with empty line.
6. The tool verifies input by running some basic checks.
7. The tool outputs one possible workshop setup, or an error message if arrangement is not possible.
8. Should you need a different setup, re-run the tool again with the same input.

Input format restrictions:
=========================
1. One talk per line
2. Talk must contain the length in "XXX min" format (without quotes). The only exceptions are "lightning"-s which keywords are automatically interpreted as Talks of 5 mins length.
3. Empty line indicates end of input sequence.

Support/Contact:
===============
Balazs David Molnar
balazs.david.molnar@gmail.com

