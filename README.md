# datavyuFX
[DatavyuFX](http://datavyu.org/) is an open-source research tool that integrates and displays all kinds of data, letting you discover the big picture while remaining connected with raw data. Datavyu will let you build and organize interpretations, and will assist with analysis.

## Download and Use DatavyuFX
You can find binaries of Datavyu available for Windows and OSX on [the Datavyu.org download page](http://datavyu.org/download/).

## Development Requirements

To get started with DatavyuFX development, you will need to download and install a few development tools. Datavyu is primarily written in Java, along with a little Ruby (via JRuby) for additional scripting tasks. So the list of toys you will need to download:

* [Java JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 
* [Maven 3.0.5](http://maven.apache.org/)
* [Git](http://git-scm.com/)

## Datavyu OSX And Windows builds

To build and package DatavyuFX, use the following commands:

	git clone https://github.com/databrary/datavyuFX.git
	cd datavyuFX
	export MAVEN_OPTS="-Xmx256M"
	mvn clean -U -Dmaven.test.skip=true jfx:native

## Build and Running DatavyuFX as Javafx Application through Maven in Intellij

Follow these steps for the setup.

1. Next to the run button click on the drop-down menu item 'Edit configuration'
2. In the dialog select on the left-hand side 'maven' and then '+'
3. Fill out working directory: 'C:/Users/Florian/integration/datavyuFX' (yours is different)
4. Add the command line: -Dmaven.test.skip=true clean compile jfx:run (for now we exclude tests)

Note, this setup will not run with the debugger in Intellij.


## Debugging DatavyuFX as Java Application in Intellij

Follow these steps for the setup.

1. Next to the run button click on the drop-down menu item 'Edit configuration'
2. In the dialog select on the left-hand side select 'Defaults' and 'Application' then '+'
3. Fill out the working directory 'C:/Users/Florian/integration/datavyuFX' (yours is different)
4. Add as main class 'org.datavyuFX.DatavyuFX'

## Deployment Information
pre_version.txt, version.txt

## More Information

See the [wiki](https://github.com/databrary/datavyu/wiki) for more information on how to code and contribute improvements to DatavyuFX.

A list of features and fixes that need implementing for DatavyuFX can be found [here](http://datavyu.org/bugs).