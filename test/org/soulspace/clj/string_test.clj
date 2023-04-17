;;;;
;;;;   Copyright (c) Ludger Solbach. All rights reserved.
;;;;
;;;;   The use and distribution terms for this software are covered by the
;;;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;;;   which can be found in the file license.txt at the root of this distribution.
;;;;   By using this software in any fashion, you are agreeing to be bound by
;;;;   the terms of this license.
;;;;
;;;;   You must not remove this notice, or any other, from this software.
;;;;

(ns org.soulspace.clj.string-test
  (:require [clojure.test :refer :all]
            [org.soulspace.clj.string :refer :all]))

(deftest gt-test
  (is (true? (gt "b" "a")))
  (is (true? (gt "ab" "aa")))
  (is (false? (gt "a" "a")))
  (is (false? (gt "aa" "aa")))
  (is (false? (gt "a" "b")))
  (is (false? (gt "aa" "ab"))))

(deftest ge-test
  (is (true? (ge "b" "a")))
  (is (true? (ge "ab" "aa")))
  (is (true? (ge "a" "a")))
  (is (true? (ge "aa" "aa")))
  (is (false? (ge "a" "b")))
  (is (false? (ge "aa" "ab"))))

(deftest lt-test
  (is (false? (lt "b" "a")))
  (is (false? (lt "ab" "aa")))
  (is (false? (lt "a" "a")))
  (is (false? (lt "aa" "aa")))
  (is (true? (lt "a" "b")))
  (is (true? (lt "aa" "ab"))))

(deftest le-test
  (is (false? (le "b" "a")))
  (is (false? (le "ab" "aa")))
  (is (true? (le "a" "a")))
  (is (true? (le "aa" "aa")))
  (is (true? (le "a" "b")))
  (is (true? (le "aa" "ab"))))

(deftest substring-test
  (is (= (substring 0 1 "clojure") "c"))
  (is (= (substring 2 5 "clojure") "oju")))

(deftest first-upper-case-test
  (is (= (first-upper-case "heLLo") "HeLLo")))

(deftest first-lower-case-test
  (is (= (first-lower-case "HeLLo") "heLLo")))

(deftest hyphen-to-camel-case-test
  (is (= (hyphen-to-camel-case "hyphen-to-camel-case") "hyphenToCamelCase")))

(deftest camel-case-to-hyphen-test
  (is (= (camel-case-to-hyphen "camelCaseToHyphen") "camel-Case-To-Hyphen")))

(deftest underscore-to-camel-case-test
  (is (= (underscore-to-camel-case "underscore2camel_case") "underscore2camelCase"))
  (is (= (underscore-to-camel-case "underscore_to_camel_case") "underscoreToCamelCase")))

(deftest camel-case-to-underscore-test
  (is (= (camel-case-to-underscore "camelCase2Underscore") "camel_Case2_Underscore"))
  (is (= (camel-case-to-underscore "camelCaseToUnderscore") "camel_Case_To_Underscore")))

(deftest kebab-case-test
  (is (= (to-kebab-case "toKebabCase") "to-kebab-case"))
  (is (= (to-kebab-case "getHTTPRequest") "get-http-request"))
  (is (= (to-kebab-case "RDF") "rdf"))
  (is (= (to-kebab-case "table-column") "table-column"))
  (is (= (to-kebab-case "table column") "table-column"))
  (is (= (to-kebab-case "table_column") "table-column"))
  (is (= (to-kebab-case "_table_column") "-table-column"))
  (is (= (to-kebab-case "table_123") "table-123")))

