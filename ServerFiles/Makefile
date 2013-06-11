CONTEXT =       ../webapps/ROOT
CLSDIR  =       $(CONTEXT)/WEB-INF/classes
CLASSES =       Servlet.class BCrypt.class Command.class

all:    $(CLASSES)

clean:
	/bin/rm -f $(CLASSES) $(CLSDIR)/*

      install:        $(CLASSES)
	install -m600 $(CLASSES) $(CLSDIR)


      Servlet.class:   Servlet.java
	javac -cp .:/usr/lib/tomcat7/tomcat-7.0.29/lib/servlet-api.jar Servlet.java

BCrypt.class: BCrypt.java
	javac BCrypt.java

Command.class: Command.java
	javac Command.java
