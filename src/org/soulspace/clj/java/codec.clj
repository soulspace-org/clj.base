;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;
(ns org.soulspace.clj.java.codec
  (:import [java.util Base64]))

;;
;; Functions to encode/decode to Hex and Base64
;;

;(set! *warn-on-reflection* true)

(defn bytes->base64
  "Encode the bytes as Base64 string."
  ^String [^bytes bytes]
  (let [encoder (java.util.Base64/getEncoder)]
    (.encodeToString encoder bytes)))

(defn base64->bytes
  "Parse a Base64 string into bytes."
  ^bytes [^String s]
  (let [decoder (java.util.Base64/getDecoder)]
    (.decode decoder s)))

(defn bytes->hex
  "Encode the bytes as hexadecimal string."
  ^String [^bytes bytes]
  (let [hex [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9 \a \b \c \d \e \f]]
    (letfn [(byte->hex [b]
      (let [v (bit-and b 0xFF)]
        [(hex (bit-shift-right v 4)) (hex (bit-and v 0x0F))]))]
      (apply str (mapcat byte->hex bytes)))))

(defn string->hex
  ^String [^String s]
  (bytes->hex (.getBytes s)))

(defn hex->bytes
  "Parse a hexadecimal string into bytes."
  ^bytes [^String s]
  (letfn [(hex->byte [c1 c2]
            (unchecked-byte
              (+ (bit-shift-left (Character/digit c1 16) 4)
                 (Character/digit c2 16))))]
    (into-array Byte/TYPE (map #(apply hex->byte %) (partition 2 s)))))

(defn hex->string
  ^String [^String s]
  (String. (hex->bytes s)))
