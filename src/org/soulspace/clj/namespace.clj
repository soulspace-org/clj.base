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

(ns org.soulspace.clj.namespace
  "Functions to help working with namespaces."
  (:require [clojure.string :as str]
            [org.soulspace.clj.string :as sstr]))

;;;;
;;;; Functions to help working with namespaces
;;;;

;;
;; Strip namespace from namespace qualified keywords 
;;

(defn unqualify-keyword
  "Strips the namespace of keyword 'k' and returns it as an unqualified keyword."
  [k]
  (keyword (name k)))

(defn unqualify-map
  "Returns a copy of the given map m, in which all qualified keys have been replaced with their unqualified variant."
  [m]
  (into {} (map (fn [[k v]] [(if (qualified-keyword? k)
                               (unqualify-keyword k)
                               k)
                               v]) m)))

;;
;; Namespace/path conversions 
;;

(defn ns-to-path
  "Converts a namespace 'nsp' into a path."
  [nsp]
  (str/replace nsp \. \/))

(defn path-to-ns
  "Converts a 'path' into a namespace."
  [path]
  (str/replace (file/normalize-path path) \/ \.))

(defn ns-to-filename
  "Converts a namespace 'nsp' into a filename."
  [nsp]
  (str/replace nsp \- \_))

(defn filename-to-ns
  "Converts a filename into a namespace."
  [filename]
  (str/replace filename \_ \-))

(comment
  (defn ns-to-file
    "Converts a namespace into a fileneame."
    [nsp]
    (str/replace (str/replace nsp \- \_) \. \/))

  (defn file-to-ns
    "Converts a filename into a namespace."
    [filename]
    (str/replace (str/replace filename \_ \-) \/ \.))
  )

(defn symbol-name
  "Converts s to hyphened clojure symbol name."
  [s]
  (str/lower-case (sstr/camel-case-to-hyphen s)))

(defn record-name
  "Converts s to camel cased clojure record name."
  [s]
  (sstr/first-upper-case (sstr/hyphen-to-camel-case s)))

(defn call-by-name
  "Resolves a function by name 's' and calls it."
  ([^String s]
   (when-let [func (ns-resolve *ns* (symbol s))]
     (when (fn? func) (func))))
  ([^String s & args]
   (when-let [func (ns-resolve *ns* (symbol s))]
     (when (fn? func) (apply func args)))))

(defn call-by-ns-name
  "Resolves a function by name 's' in the given namespace 'nsp' and calls it."
  ([^String nsp ^String s]
   (when-let [func (ns-resolve (symbol nsp) (symbol s))]
     (when (fn? func) (func))))
  ([^String nsp ^String s & args]
   (when-let [func (ns-resolve (symbol nsp) (symbol s))]
     (when (fn? func) (apply func args)))))
