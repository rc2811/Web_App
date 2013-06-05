CONTEXT =       ../webapps/ROOT
LIBDIR  =       $(CONTEXT)/WEB-INF/lib
CLSDIR  =       $(CONTEXT)/WEB-INF/classes
JARS    =       servlets.jar
CLASSES =       Servlet.class

all:    $(JARS) $(CLASSES)

clean:
	/bin/rm -f $(JARS) $(CLASSES) $(CLSDIR)/* $(LIBDIR)/*

      install:        $(JARS) # $(CLASSES)
	#install -m600 $(JARS) $(LIBDIR)
	install -m600 $(CLASSES) $(CLSDIR)

      servlets.jar:      $(CLASSES)
	jar cf servlets.jar $(CLASSES)


      Servlet.class:   Servlet.java
	javac -cp .:/usr/lib/tomcat7/tomcat-7.0.29/lib/servlet-api.jar Servlet.java
