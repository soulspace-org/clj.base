clj.base
========
The clj.base library provides functionality for Clojure with no external dependencies.

org.soulspace.clj contains the following namespaces:
* cli - functions to define and parse command line options
* file - functions for working with files, search paths and file search
* core - general functions to supplement clojure.core
* namespace - functions to help working with namespaces
* property-replacement - functions for property replacements in the form of ${property}
* string - functions to supplement clojure.string

org.soulspace.clj.java contains namespaces building on classes of the JDK:
* beaninfo - functions to set Java bean properties via property descriptors
* beans- functions for method-based reflective access to Java bean style objects
* codec- functions to encode/decode to Hex and Base64
* i18n - functions for internationalization via Java resource bundles
* message-digest - functions to create message digests
* net - functions for network access
* properties - function to create Java Properties from a Clojure map
* reflection - functions for reflective introspection of Java classes
* system - functions for interacting with java.lang.System
* type-conversion - Java type coercion/conversion functions


Usage
-----
Add the dependency: 

[![Clojars Project](https://img.shields.io/clojars/v/org.soulspace.clj/clj.base.svg)](https://clojars.org/org.soulspace.clj/clj.base)

Copyright
---------
© 2012-2021 Ludger Solbach

License
-------
[Eclipse Public License 1.0](http://www.eclipse.org/legal/epl-v10.html)

Code Repository
---------------
[CljBase on GitHub](https://github.com/soulspace-org/clj.base)
