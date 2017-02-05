#!/bin/bash
curDir=$(pwd)
cd `dirname $0`/dist
#java -Dswing.defaultlaf=com.sun.java.swing.plaf.motif.MotifLookAndFeel image/ImageApp
#java -Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel image/ImageApp
#java -Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel image/ImageApp
#java -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -classpath .:../i18n:../lib/jargs.jar image/ImageAppLauncher --curdir "$curDir" $@
java -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -classpath .:../i18n:../lib/jargs.jar -jar Jaguaribe.jar "$@"
