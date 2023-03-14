;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;
(ns org.soulspace.clj.java.beaninfo
  (:require [org.soulspace.clj.java.type-conversion :as tc])
  (:import [java.beans Introspector]))

;;
;; Functions to set Java bean properties via property descriptors
;;

(defn property-descriptor
  "Returns the property descriptor for the property with the given name."
  [inst prop-name]
  (first (filter
           #(= prop-name (.getName %)) 
           (.getPropertyDescriptors (Introspector/getBeanInfo (class inst))))))

(defn get-property-class
  "Returns the class of a property by the given property write method."
  [write-method]
  (first (.getParameterTypes write-method)))

(defn set-property!
  "Sets the property on the given instance to value."
  [inst prop value]
  (let [pd (property-descriptor inst prop)]
    (if (nil? pd) (throw (IllegalArgumentException. (str "No such property " prop))))
    (let [write-method (.getWriteMethod pd)
          dest-class (get-property-class write-method)]
      (.invoke write-method inst (into-array [(tc/coerce dest-class value)])))))

(defn set-properties!
  "Sets the properties given in the map to the instance."
  [inst prop-map]
  (doseq [[k v] prop-map] (set-property! inst (name k) v))) 
