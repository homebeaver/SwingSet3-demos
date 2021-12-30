SwingSet2 demonstrates some of the abilities of the Swing User Interface
Toolkit by displaying many of the components in a single showcase application.
Use it to try out different components and features provided by Swing.


==================================
TO RUN SWINGSET2 AS AN APPLICATION
==================================

  java -jar SwingSet2.jar


=============================
TO RUN SWINGSET2 AS AN APPLET
=============================

  appletviewer SwingSet2.html

=========================
TO MODIFY/BUILD SWINGSET2
=========================

The full source for the SwingSet2 demo can be found in the "src"
subdirectory. If you wish to play with the source code and try
out your changes, you can compile and run in this "src" directory:

  javac *.java
  
  java SwingSet2

You may notice a difference when running SwingSet from your compiled source
(versus running from the packaged JAR file), in that it won't show the
splash screen. This is expected, as the splash screen is shown using the
java.awt.SplashScreen support, which allows specifying a splash screen
image as an attribute in the JAR's manifest file. If you'd like to see the
splash screen with your own compiled version, you can package your classes
into a JAR and specify the splash screen (as outlined in the java.awt.SplashScreen
documentation) or you can specify the splash screen image on the command line:

java -splash:resources/images/splash.png SwingSet2


Note: These instructions assume that this installation's versions of the java,
appletviewer, and javac commands are in your path.  If they aren't, then you should
either specify the complete path to the commands or update your PATH environment
variable as described in the installation instructions for the
Java(TM) SE Development Kit.

 * Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
