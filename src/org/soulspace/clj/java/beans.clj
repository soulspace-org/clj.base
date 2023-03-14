;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;
(ns org.soulspace.clj.java.beans
  (:refer-clojure :exclude [methods])
  (:require [clojure.string :as str]
            [org.soulspace.clj.string :as sstr]
            [org.soulspace.clj.java.reflection :as r]
            [org.soulspace.clj.java.type-conversion :as tc])
  (:import [java.lang.reflect Method]))

;;
;; Functions for method-based reflective access to Java bean style objects.
;;
;; TODO memoize with core.memoize for performance
;;

;(set! *warn-on-reflection* true)

(def boxing
  {java.lang.Boolean/TYPE java.lang.Boolean
   java.lang.Boolean java.lang.Boolean/TYPE
   java.lang.Byte/TYPE java.lang.Byte
   java.lang.Byte java.lang.Byte/TYPE
   java.lang.Character/TYPE java.lang.Character
   java.lang.Character java.lang.Character/TYPE
   java.lang.Short/TYPE java.lang.Short
   java.lang.Short java.lang.Short/TYPE
   java.lang.Integer/TYPE java.lang.Integer
   java.lang.Integer java.lang.Integer/TYPE
   java.lang.Long/TYPE java.lang.Long
   java.lang.Long java.lang.Long/TYPE
   java.lang.Float/TYPE java.lang.Float
   java.lang.Float java.lang.Float/TYPE
   java.lang.Double/TYPE java.lang.Double
   java.lang.Double java.lang.Double/TYPE})


(defn compatible-type?
  "Checks the compatibility of java types."
  [^Class expected ^Class actual]
  (and (not (nil? expected))
       (not (nil? actual))
       (or (= expected actual)
           (= expected (boxing actual)) ;boxing compatibility (e.g. java.lang.Short <-> short)
           (.isAssignableFrom expected actual)))) ;assignment compatibility

(defn getter?
  "Returns true if the given method is a property getter method."
  ([^Method method]
   (and
     (or
       (str/starts-with? (r/method-name method) "get")
       (str/starts-with? (r/method-name method) "is"))
     (= 0 (count (r/parameter-types method)))))
  ([^Method method property-name]))
    ; TODO check against property name


(defn setter?
  "Returns true if the given method is a property setter method."
  ([^Method method]
   (and
     (str/starts-with? (r/method-name method) "set")
     (= 1 (count (r/parameter-types method)))))
  ([^Method method property-name]))
    ; TODO check against property name


; TODO (defn adder?)
; TODO (defn remover?)

(defn- parameter-type
  "Returns the first parameter type of a method."
  [^Method method]
  (first (r/parameter-types method)))

(defn getter-method
  "Returns the getter method for this property. Works for derived properties too."
  ^Method [^Class cl property]
  (let [pname (sstr/first-upper-case property)]
    (first (filter #(and
                      (or (= (str "get" pname) (r/method-name %))
                          (= (str "is" pname) (r/method-name %)))
                      (nil? (r/parameter-types %)))
                   (r/methods cl)))))

(defn setter-methods
  "Returns a sequence of the setter methods for this property."
  ^Method [^Class cl property]
  (let [pname (str "set" (sstr/first-upper-case property))]
    (filter #(and (= pname (r/method-name %))
                  (setter? %))
            (r/methods cl))))

(defn adder-methods
  "Returns a sequence of the adder methods for this property."
  ^Method [^Class cl property]
  (let [pname (str "add" (sstr/first-upper-case property))]
    (filter #(and (= pname (r/method-name %))
                  (= 1 (count (r/parameter-types %))))
            (r/methods cl))))

(defn remover-methods
  "Returns a sequence of the remover methods for this property."
  ^Method [^Class cl property]
  (let [pname (str "remove" (sstr/first-upper-case property))]
    (filter #(and (= pname (r/method-name %))
                  (= 1 (count (r/parameter-types %))))
            (r/methods cl))))

; TODO add value as parameter and return the setter based on the type of the value
(defn setter-method
  "Returns the setter method for this property. Works for derived properties too."
  (^Method [^Class cl property]
   (first (setter-methods cl property)))
  (^Method [^Class cl property param-type]
    ;(println param-type "->" (map  #(compatible-type? (parameter-type %) param-type) (setter-methods cl property)))
   (if-let [setter (first (filter #(compatible-type? (parameter-type %) param-type) (setter-methods cl property)))]
     setter
     (setter-method cl property))))

; TODO add value as parameter and return the adder based on the type of the value
(defn adder-method
  "Returns the add method for this multivalued property."
  (^Method [^Class cl property]
   (first (adder-methods cl property)))
  (^Method [^Class cl property param-type]
   (if-let [adder (first (filter #(compatible-type? (parameter-type %) param-type) (adder-methods cl property)))]
     adder
     (adder-method cl property))))

; TODO add value as parameter and return the remover based on the type of the value
(defn remover-method
  "Returns the remove method for this multivalued property."
  (^Method [^Class cl property]
   (first (remover-methods cl property)))
  (^Method [^Class cl property param-type]
   (if-let [remover (first (filter #(compatible-type? (parameter-type %) param-type) (remover-methods cl property)))]
     remover
     (remover-method cl property))))

(defn has-get-method?
  [^Class cl property]
  (getter-method cl property))

(defn has-set-method?
  [^Class cl property]
  (setter-method cl property))

(defn has-add-method?
  [^Class cl property]
  (adder-method cl property))

(defn has-remove-method?
  [^Class cl property]
  (remover-method cl property))

(defn get-property
  "Returns the value of this property."
  [obj property]
  (if-let [property-get (getter-method (class obj) property)]
    (.invoke property-get obj nil)
    (throw (IllegalArgumentException. (str "No such property " property ".")))))

(defn set-property!
  "Sets the value of this property."
  [obj property value]
  (if-let [property-set (setter-method (class obj) property (type value))]
    (let [param-type (first (r/parameter-types property-set))]
      (.invoke property-set obj (into-array [(tc/coerce param-type value)])))
    (throw (IllegalArgumentException. (str "No compatible setter for property " property " found.")))))

(defn add-property!
  "Adds the value to the property collection."
  [obj property value]
  (if-let [property-add (adder-method (class obj) property (type value))]
    (let [param-type (first (r/parameter-types property-add))]
      (.invoke property-add obj (into-array [(tc/coerce param-type value)])))
    (throw (IllegalArgumentException. (str "No compatible adder for property " property " found.")))))

(defn remove-property!
  "Removes the value to the property collection."
  [obj property value]
  (if-let [property-remove (remover-method (class obj) property (type value))]
    (let [param-type (first (r/parameter-types property-remove))]
      (.invoke property-remove obj (into-array [value])))
    (throw (IllegalArgumentException. (str "No compatible remover for property " property " found.")))))

(defn set-properties!
  "Sets the properties given in the map to the instance"
  [obj prop-map]
  (doseq [[k v] prop-map] (set-property! obj (name k) v))
  obj)

(defn add-properties!
  "Adds the properties given in the map to the instance"
  [obj prop-map]
  (doseq [[k v] prop-map]
    (if (coll? v)
      (doseq [value v]
        (add-property! obj (name k) value))
      (add-property! obj (name k) v))))

(defn remove-properties!
  "Removes the properties given in the map to the instance"
  [obj prop-map]
  (doseq [[k v] prop-map]
    (if (coll? v)
      (doseq [value v]
        (remove-property! obj (name k) value))
      (remove-property! obj (name k) v))))

(defn init-properties!
  ([obj set-props]
   (set-properties! obj set-props))
  ([obj set-props add-props]
   (set-properties! obj set-props)
   (add-properties! obj add-props)
   obj))

; TODO works for symbols and strings but not for class names yet
(defn create
  "Creates an instance of the given class"
  [cl & args]
  (clojure.lang.Reflector/invokeConstructor
    (Class/forName (name cl)) (into-array Object args)))

(defn create-bean
  "Creates an instance of the given bean and initializes it with the given data."
  ([bean-class set-props]
   (init-properties! (create bean-class) set-props))
  ([bean-class set-props add-props]
   (init-properties! (create bean-class) set-props add-props)))
