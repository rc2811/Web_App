CONTEXT =       ../webapps/ROOT
LIBDIR  =       $(CONTEXT)/WEB-INF/lib
CLSDIR  =       $(CONTEXT)/WEB-INF/classes
CLASSES =       Servlet.class BCrypt.class

all:    $(CLASSES)

clean:
	/bin/rm -f $(CLASSES) $(CLSDIR)/* $(LIBDIR)/*

      install:        $(CLASSES)
	#install -m600 $(JARS) $(LIBDIR)
	install -m600 $(CLASSES) $(CLSDIR)


      Servlet.class:   Servlet.java
	javac -cp .:/usr/lib/tomcat7/tomcat-7.0.29/lib/servlet-api.jar Servlet.java

BCrypt.class: BCrypt.java
	javac BCrypt.java
