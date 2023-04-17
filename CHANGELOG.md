ChangeLog
=========

Version 0.9.1
-------------
* moved file.clj to clj.java as it uses Java IO

Version 0.9.0
-------------
* fixed a bug in file/base-name
* fixed docstring issues in file
* fixed a bug in namespace/call-by-name
* took steps to make clj.base compatible with clojurescript
  * removed clj.java namespaces from clj.base again to reduce the dependencies on JVM classes
  * moved file namespace to clj.java, because it depends on the JVM

Version 0.8.4
-------------
* Added array-type function to create type hints for non primitive arrays
* Integrated the namespaces of clj.java into clj.base because they don't have
  any external dependencies, too
* Added type hints on many functions

Version 0.8.3
-------------
* added atom?, ref?, agent? and reftype? methods to test for reference types

Version 0.8.2
-------------
* fixed from-camel-case to handle digits, whitespace and special chars

Version 0.8.1
-------------
* merged file-search into file namespace
* enhanced from-camel-case, added to-kebab-case
* added unqualify-keyword and unqualify-map
* renamed unittest folder to test
* fixed function definition order

Version 0.8.0
------------- 
* added Leiningen project file
* renamed to clj.base to make the library compatible with clojars
* updated project files accordingly
* made the code compliant to current Clojure coding practices (e.g. require over use)
* updated comments and documentation in the code
* moved some Java specific namespaces to clj.java (codec, message-digest)
* moved some CLojure only code from clj.app to clj.base
* updated Clojure version to 1.10.1

Version 0.6.0
-------------
* added system properties support
* added JVM proxy settings
* added path normalization for file paths (java.io)
* added codec for base64 and hex coding of byte arrays
* added message digests
* now requires JavaSE 7

Version 0.5.0
-------------
* added camel case string functions
* added copyright header in source files
* moved namespace related functions to 'namespace' namespace
* replaced str-length with count
* updated module files

Version 0.4.0
-------------
* added absolute-file and canonical-file
* fixed file search
* code cleanups
* updated module files

Version 0.3.0
-------------
* added convenience in file functions
* added unit tests for string functions
* added doc strings
* added license.txt
* refactored file functions
* updated module files
* initial github import
* initial git import
