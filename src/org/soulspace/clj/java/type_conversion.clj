;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;
(ns org.soulspace.clj.java.type-conversion)

;;
;; Functions for the coercion of Java types
;;

;(set! *warn-on-reflection* true)

(defmulti coerce
  "Coerce clojure data types to java data types"
  (fn [dest-class obj] [dest-class (class obj)]))

; to Boolean
(defmethod coerce [Boolean/TYPE Boolean] [_ ^Boolean obj]
  obj)

; to Byte
(defmethod coerce [Byte/TYPE Byte] [_ ^Byte obj]
  (Byte. (.byteValue obj)))
(defmethod coerce [Byte/TYPE Character] [_ obj]
  (Byte. (.byteValue obj)))
(defmethod coerce [Byte/TYPE Short] [_ ^Short obj]
  (Byte. (.byteValue obj)))
(defmethod coerce [Byte/TYPE Integer] [_ ^Integer obj]
  (Byte. (.byteValue obj)))
(defmethod coerce [Byte/TYPE Long] [_ ^Long obj]
  (Byte. (.byteValue obj)))
(defmethod coerce [Byte/TYPE Float] [_ ^Float obj]
  (Byte. (.byteValue obj)))
(defmethod coerce [Byte/TYPE Double] [_ ^Double obj]
  (Byte. (.byteValue obj)))

; to Character
(defmethod coerce [Character/TYPE Byte] [_ obj]
  (Character. (.charValue obj)))
(defmethod coerce [Character/TYPE Character] [_ ^Character obj]
  (Character. (.charValue obj)))
(defmethod coerce [Character/TYPE Short] [_ obj]
  (Character. (.charValue obj)))
(defmethod coerce [Character/TYPE Integer] [_ obj]
  (Character. (.charValue obj)))
(defmethod coerce [Character/TYPE Long] [_ obj]
  (Character. (.charValue obj)))

; to Short
(defmethod coerce [Short/TYPE Byte] [_ ^Byte obj]
  (Short. (.shortValue obj)))
(defmethod coerce [Short/TYPE Character] [_ obj]
  (Short. (.shortValue obj)))
(defmethod coerce [Short/TYPE Short] [_ ^Short obj]
  (Short. (.shortValue obj)))
(defmethod coerce [Short/TYPE Integer] [_ ^Integer obj]
  (Short. (.shortValue obj)))
(defmethod coerce [Short/TYPE Long] [_ ^Long obj]
  (Short. (.shortValue obj)))
(defmethod coerce [Short/TYPE Float] [_ ^Float obj]
  (Short. (.shortValue obj)))
(defmethod coerce [Short/TYPE Double] [_ ^Double obj]
  (Short. (.shortValue obj)))

; to Integer
(defmethod coerce [Integer/TYPE Byte] [_ ^Byte obj]
  (Integer. (.intValue obj)))
(defmethod coerce [Integer/TYPE Character] [_  obj]
  (Integer. (.intValue obj)))
(defmethod coerce [Integer/TYPE Short] [_ ^Short obj]
  (Integer. (.intValue obj)))
(defmethod coerce [Integer/TYPE Integer] [_ ^Integer obj]
  (Integer. (.intValue obj)))
(defmethod coerce [Integer/TYPE Long] [_ ^Long obj]
  (Integer. (.intValue obj)))
(defmethod coerce [Integer/TYPE Float] [_ ^Float obj]
  (Integer. (.intValue obj)))
(defmethod coerce [Integer/TYPE Double] [_ ^Double obj]
  (Integer. (.intValue obj)))

; to Long
(defmethod coerce [Long/TYPE Byte] [_ ^Byte obj]
  (Long. (.longValue obj)))
(defmethod coerce [Long/TYPE Character] [_ obj]
  (Long. (.longValue obj)))
(defmethod coerce [Long/TYPE Short] [_ ^Short obj]
  (Long. (.longValue obj)))
(defmethod coerce [Long/TYPE Integer] [_ ^Integer obj]
  (Long. (.longValue obj)))
(defmethod coerce [Long/TYPE Long] [_ ^Long obj]
  (Long. (.longValue obj)))
(defmethod coerce [Long/TYPE Float] [_ ^Float obj]
  (Long. (.longValue obj)))
(defmethod coerce [Long/TYPE Double] [_ ^Double obj]
  (Long. (.longValue obj)))

; to File
(defmethod coerce [java.io.File String] [_ ^String str] 
  (java.io.File. str))

; to List
(defmethod coerce [java.util.List clojure.lang.PersistentVector] [_ ^java.util.List v]
  (java.util.ArrayList. v))

; to Set
(defmethod coerce [java.util.Set clojure.lang.IPersistentSet] [_ ^java.util.Set s]
  (java.util.HashSet. s))

; to Map
(defmethod coerce [java.util.Map clojure.lang.IPersistentMap] [_ ^java.util.Map m]
  (java.util.HashMap. m))

(defmethod coerce :default [dest-class obj] 
   ;(println (str (.getSimpleName dest-class) " " (type obj)))
   (cast dest-class obj))
