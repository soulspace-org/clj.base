;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;

(ns org.soulspace.clj.property-replacement)

;;
;; Functions for property replacements in the form of ${property}
;;

; concatenate the tokens matched by the pattern of replace-properties
; if no property is found, replace with
(defn- concat-property-tokens
  [prop-map [_ t1 t2 t3]]
  (str t1 (get prop-map (keyword t2) (str "${" t2 "}")) t3))

(defn replace-properties
  "Replaces properties of the form of '${property}' in strings given as input with values from prop-map."
  ([prop-map input]
   (cond
     (string? input)
     (if-let [tokens (re-seq #"([^$]*)(?:\$\{([^}]*)\}*([^$]*))" input)]
       (reduce str (map (partial concat-property-tokens prop-map) tokens))
       input)
     (coll? input)
     (map (partial replace-properties prop-map) input)
     :default
     input))
  ([prop-map input default]
   (if-not (nil? input)
     (replace-properties prop-map input)
     (replace-properties prop-map default))))

(defn replace-properties-recursive
  "Recursively replaces properties of the form ${property} in strings contained in input until no further replacement is possible."
  ([prop-map input]
   (cond
     (string? input)
     (loop [in input]
       (let [replaced (replace-properties prop-map in)]
         (if-not (= in replaced)
           (recur replaced)
           in)))
     (coll? input)
     (map (partial replace-properties-recursive prop-map) input)
     :default
     input))
  ([prop-map input default]
   (if-not (nil? input)
     (replace-properties-recursive prop-map input)
     (replace-properties-recursive prop-map default))))
