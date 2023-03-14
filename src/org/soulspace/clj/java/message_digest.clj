;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;
(ns org.soulspace.clj.java.message-digest
  (:require [clojure.java.io :as io])
  (:import [java.nio.file Files Path Paths]
           [java.security MessageDigest]))

;;
;; Functions to create message digests
;;

;; definition copied from clojure.java.io because it's private
(def ^{:doc "Type object for a Java primitive byte array."
       :private true}
  byte-array-type (class (make-array Byte/TYPE 0)))

;; Algorithms as defined by http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html
;; MD2 not included here because it is considerated insecure.
(def algorithms
  {:md5 "MD5"
   :sha-1 "SHA-1"
   :sha-256 "SHA-256"
   :sha-384 "SHA-384"
   :sha-512 "SHA-512"})

;; Multi method for conversion to byte[]
(defmulti get-bytes type)

(defmethod get-bytes byte-array-type [val] val)
(defmethod get-bytes String [val]
  (.getBytes val "UTF-8"))
(defmethod get-bytes java.nio.file.Path [val]
  (Files/readAllBytes val))
(defmethod get-bytes java.io.File [val]
  (Files/readAllBytes (Paths/get (.getAbsolutePath val))))

(defn message-digest
  "Returns a message digest instance configured with the given algorithm."
  [algorithm]
  (MessageDigest/getInstance algorithm))

(defn reset
  "Resets the message digest."
  [md]
  (.reset md)
  md)

(defn update
  "Updates the message digest."
  [md val]
  (.update md val)
  md)

(defn digest
  "Returns the digest of the message digest."
  ([md]
   (.digest md))
  ([md val]
   (.digest md val)))

(defn get-digest
  "Returns the digest of the val calculated by the given algorithm."
  [algo-key val]
  (-> (message-digest (algorithms algo-key))
    (reset)
    (digest (get-bytes val))))

(defn md5-digest
  "Calculates a message digest with the MD5 algorithm.
   The argument can be a file, a string or a byte array."
  [val]
  (get-digest :md5 val))

(defn sha-1-digest
  "Calculates a message digest with the SHA-1 algorithm.
   The argument can be a path, a file, a string or a byte array."
  [val]
  (get-digest :sha-1 val))

(defn sha-256-digest
  "Calculates a message digest with the SHA-256 algorithm.
   The argument can be a path, a file, a string or a byte array."
  [val]
  (get-digest :sha-256 val))

(defn sha-384-digest
  "Calculates a message digest with the SHA-384 algorithm.
   The argument can be a path, a file, a string or a byte array."
  [val]
  (get-digest :sha-384 val))

(defn sha-512-digest
  "Calculates a message digest with the SHA-512 algorithm.
   The argument can be a path, a file, a string or a byte array."
  [val]
  (get-digest :sha-512 val))
