;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;

(ns org.soulspace.clj.cli
  (:require [clojure.string :as str]))

;;
;; Functions to define and parse command line options
;; supports multiple indications of the same option, which org.clojure/tools.cli does not at the moment.
;;

; example spec entry
(comment
  (def spec-entry-format
    {:name "define"
     :option "--define"
     :short "-D"
     :doc "Define a var"
     :parse-fn identity
     :multi false
     :default nil}))

(defn flag-spec?
  "Tests if the string is a flag, which starts with the string '--[no-]'."
  [arg]
  (str/starts-with? arg "--[no-]"))

(defn long-option?
  "Tests if the string is a long option, which starts with the string '--'."
  [arg]
  (str/starts-with? arg "--"))

(defn option?
  "Tests if the string is an option, which starts with the character '-'."
  [arg]
  (str/starts-with? arg "-"))

(defn matches-option?
  "Returns true, if the arg starts with an option switch of this spec."
  [arg spec]
  (if (long-option? arg)
    (and (not (nil? (:long spec))) (str/starts-with? arg (:long spec)))
    (and (not (nil? (:short spec))) (str/starts-with? arg (:short spec)))))

(defn option-name
  "Returns the name of the option."
  [opt]
  (str/replace opt #"^--\[no\]-|^--no-|^--|^-" ""))

(defn build-specs
  "Build option specifications from the option definitions."
  [option-defs]
  (if (seq option-defs)
    (loop [defs option-defs specs []]
      (if (seq defs)
        (let [[opt short doc & options] (first defs)
              spec (merge {:name (option-name opt) :long opt :short short :doc doc :parse-fn identity :multi true}
                          (apply hash-map options))]
          (recur (rest defs) (conj specs spec)))
        specs))))

(defn doc-for-spec
  "Returns the documentation for the specification."
  [{:keys [long short doc default]}]
  [(str/join ", " [short long])
   (or (str doc "."))
   (or (str "Default: " default) "")])

(defn doc-for-specs
  "Returns the documentation for the specifications."
  [specs]
  (str/join "\n" (map #(str/join "\t" (doc-for-spec %)) specs)))

(defn doc-options
  [option-defs]
  (doc-for-specs (build-specs option-defs)))

(defn default-option-map
  "Returns an option map initalized with the default values."
  [specs]
  (reduce (fn [map spec] (assoc map (keyword (:name spec)) (:default spec))) {} (filter #(:default %) specs)))

(defn add-result
  "Add the value for an option to the option map. Handles multiple values per option."
  [option-map [spec value]]
  (let [option-key (keyword (:name spec))
        multi (true? (:multi spec))]
    (if-let [old-value (option-map option-key)]
      (cond
        (and multi (coll? old-value)) (assoc option-map option-key (conj old-value value))
        multi (assoc option-map option-key [old-value value]))
      (assoc option-map option-key value))))

(defn parse-option-arg
  "Returns the option spec and the parsed value in a vector, if a matching option is found."
  [specs arg]
  (if-let [spec (first (filter (partial matches-option? arg) specs))]
    (if (long-option? arg)
      [spec ((:parse-fn spec) (str/replace arg (re-pattern (str "^" (:long spec))) ""))]
      [spec ((:parse-fn spec) (str/replace arg (re-pattern (str "^" (:short spec))) ""))])))

(defn parse-option-args
  "Parses a sequence of options."
  [specs option-args]
  (loop [args option-args option-map (default-option-map specs)]
    (if-let [arg (first args)]
      (if-let [result (parse-option-arg specs arg)]
        (recur (rest args) (add-result option-map result)) ; find option value and assoc [option value] to option map
        (recur (rest args) option-map))
      option-map)))

(defn parse-args
  "Parses the args sequence into a vector of options and arguments."
  [args option-definitions]
  (if-let [specs (build-specs option-definitions)]
    [(filter (complement option?) args) (parse-option-args specs (filter option? args))]))
