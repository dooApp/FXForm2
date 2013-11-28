FXForm 2
========

**Stop coding forms: FXForm 2 can do it for you!**

About
-----

FXForm2 is a library providing automatic JavaFX 2.0 form generation.

How does it work?
-----------------

1. Write your model bean
2. Generate your form using FXForm2
3. Style it using CSS, skins and resource bundles!

Quick start
-----------
Add FXForm to your project dependencies:
  
    <dependency>
        <groupId>com.dooapp.fxform2</groupId>
        <artifactId>core</artifactId>
        <version>2.2.4</version> <!-- Note: For JavaFX 8, use 8.0.1 -->
    </dependency>

Get your FXForm...

    FXForm<MyBean> fxForm = new FXForm<MyBean>(myBean);

...and add it to your scene!

See [Get started](https://github.com/dooApp/FXForm2/wiki/Get-started).

Features
--------

Main features include:

* Automatic form generation and binding to bean properties
* CSS support
* Bean Validation handling (JSR 303)
* Fields reordering and filtering
* Tooltips
* Localization
* Custom factories

Key benefits
------------
* Don't waste time coding forms, focus on styling
* Less code and improved quality
* Easy to use and to customize

Links
-----

A video by Betrand Goetzmann with [Grezi](https://bitbucket.org/bgoetzmann/grezi/wiki/Home) and FXForm2: [http://screenr.com/GvDs]

JavaFX [Third Party Tools and Utilities](http://www.oracle.com/technetwork/java/javafx/community/3rd-party-1844355.html)

[Screenshots](http://infiltrea.com/index.php/visuels) of Infiltrea, a real-world application using FXForm2 - all forms you can see there are based on FXForm2

Licensing
---------

FXForm2 is licensed under the Lesser GPL license.

Build status
------------
[![Build Status](https://buildhive.cloudbees.com/job/dooApp/job/FXForm2/badge/icon)](https://buildhive.cloudbees.com/job/dooApp/job/FXForm2/)
