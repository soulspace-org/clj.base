;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;

(ns org.soulspace.clj.string
  (:require [clojure.string :as str]))

;;
;; String functions
;;

;(set! *warn-on-reflection* true)

;;
;; String comparison
;;

(defn gt
  "Greater than string comparison."
  [^String s1 ^String s2]
  (> (.compareTo s1 s2) 0))

(defn ge
  "Greater or equal string comparison."
  [^String s1 ^String s2]
  (>= (.compareTo s1 s2) 0))

(defn lt
  "Less than string comparison."
  [^String s1 ^String s2]
  (< (.compareTo s1 s2) 0))

(defn le
  "Less or equal string comparison."
  [^String s1 ^String s2]
  (<= (.compareTo s1 s2) 0))

(defn eq
  "Equal string comparison."
  [s1 s2]
  (= s1 s2))

(defn ne
  "Not equal string comparison."
  [s1 s2]
  (not= s1 s2))

;;
;; String transformation
;;

(defn substring
  "Returns a substring of string defined by the indices."
  ([begin-idx ^String s]
   (.substring s begin-idx))
  ([begin-idx end-idx ^String s]
   (.substring s begin-idx end-idx)))

(defn upper-case?
  "Returns true if the char is upper case."
  [^Character c]
  (Character/isUpperCase c))

(defn lower-case?
  "Returns true if the char is lower case."
  [^Character c]
  (Character/isLowerCase c))

(defn first-upper-case
  "Returns the string with the first letter converted to upper case."
  [s]
  (str (str/upper-case (substring 0 1 s)) (substring 1 s)))

(defn first-lower-case
  "Returns the string with the first letter converted to lower case."
  [s]
  (str (str/lower-case (substring 0 1 s)) (substring 1 s)))

(defn to-camel-case
  "Converts a string 's' into camel case. Removes occurences of 'c' and converts
  the next character to upper case."
  [c s]
  (loop [chars (seq s) cc-chars []]
    (if (seq chars)
      (if (= (first chars) c)
        (recur (rest (rest chars)) (conj cc-chars (str/upper-case (second chars))))
        (recur (rest chars) (conj cc-chars (str (first chars)))))
      (apply str cc-chars))))

(defn from-camel-case
  "Converts a string 's' from camel case to a lower case string with the spacer character
  'c' inserted in front of intra word uppercase chars. Spacer chars are not inserted into
  upper case abbreviations. The case of the chars is retained.
  
  Examples:
  (from-camel-case \\- \"fromCamelCase\") -> \"from-Camel-Case\"
  (from-camel-case \\- \"getHTTPRequest\") -> \"get-HTTP-Request\"
  "
  [^Character c s]
  (loop [chars (seq s) r-chars [] start? true in-upper? false]
    (if (seq chars)
      (let [current-char (char (first chars))]
        (if (not (or (Character/isDigit current-char) (Character/isLetter current-char)))
          ;; special char or white space, replace with hyphen
          (recur (rest chars) (conj r-chars \-) false false)
          (if (or (lower-case? current-char) (Character/isDigit current-char))
            ;; lower case or digit, don't add spacer char
            (recur (rest chars) (conj r-chars current-char) false false)
            (if start?
              ;; start of word, don't add spacer
              (recur (rest chars) (conj r-chars current-char) false true)
              (if-not (seq (rest chars))
                ;; last char, dont add spacer
                (recur (rest chars) (conj r-chars current-char) false true)
                ;; not the last char of the string
                (if in-upper?
                  (if (upper-case? (fnext chars))
                    ;; in an upper case word and the next char is upper case too
                    ;; don't add spacer here
                    (recur (rest chars) (conj r-chars current-char) false true)
                    ;; in an upper case word but the next char is lower case
                    ;; add a spacer char in front of the last upper case char
                    (recur (rest chars) (conj r-chars c current-char) false true))
                  ;; first upper case char after a lower case char, add spacer char
                  (recur (rest chars) (conj r-chars c current-char) false true)))))))
      (apply str r-chars))))

(defn hyphen-to-camel-case
  "Converts the hyphenized string 's' to a camel case string."
  [s]
  (to-camel-case \- s))

(defn camel-case-to-hyphen
  "Converts the camel case string 's' to a hyphenized string."
  [s]
  (from-camel-case \- s))

(defn underscore-to-camel-case
  "Converts the underscored string 's' to a camel case string."
  [s]
  (to-camel-case \_ s))

(defn camel-case-to-underscore
  "Converts the camel case string 's' to a underscored string."
  [s]
  (from-camel-case \_ s))

(defn to-kebab-case
  "Converts a camel case string 's' to kebab case, which is a lower case,
  hyphenized string.

  Examples:
  (to-kebab-case \"toKebabCase\") => \"to-kebab-case\"
  (to-kebab-case \"getHTTPRequest\") => \"get-http-request\"
  "
  [s]
  (str/lower-case (camel-case-to-hyphen s)))

;;
;; String parsing
;;

(defn parse-number
  "Reads a number from a string. Returns nil if not a number."
  [^String s]
  (if (re-find #"^-?\d+\.?\d*([Ee]\+\d+|[Ee]-\d+|[Ee]\d+)?$" (.trim s))
    (read-string s)))
