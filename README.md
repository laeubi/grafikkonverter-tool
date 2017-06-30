# grafikkonverter-tool-osgi
Das Tool ermöglicht es verschiedene Grafikformate zu laden und mittels z.B. dem Floyd-Steinberg Verfahren in Bilder mit geringerer Farbtiefe zu konvertieren. Diese können dann in verschiedene Sourcecode-Formate konvertiert werden.

Außerdem ist es möglich das Bild zu beschneiden, rotieren, und skalieren, sowie eine (Graustufen)Farbanpassung vor dem Berechnen anzuwenden.
Als Ausgabe stehen verschiedene C-Compiler zur Verfügung (BMP2C) oder Assembler (AVR Studio BMP2ASM oder so), außerdem reine Textformate oder wieder Binäre Formate (RAW, JPEG, ...)
Für die meisten Formate ist es zudem möglich zu wählen, ob die Ausgabe Horizontal oder Vertikal erfolgen soll, ob die Farbtabelle mit ausgegeben werden soll, oder das ergebnis LSB oder MSB first gespeichert werden soll.

#Laufzeitumgebung
Das Programm ist in Java geschrieben, is also unter Windows, Linux, Mac,... lauffähig, benötigt wird nur eine Java Runtime Umgebung ab Version 1.7

# Mehr Dateiformate
Mehr Dateiformate werden durch Installtion der Advanced Image API
unterstützt: https://github.com/jai-imageio/jai-imageio-core
