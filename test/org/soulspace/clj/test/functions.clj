(ns org.soulspace.clj.test.functions
  (:require [clojure.test :refer :all]
          [org.soulspace.clj.function :refer :all]))

(deftest not-nil?-test
  (is (not-nil? 1))
  (is (not-nil? true))
  (is (not-nil? false))
  (is (not-nil? ""))
  (is (not-nil? []))
  (is (not (not-nil? nil))))

(def my-var 0)
(def my-atom (atom 1))
(def my-ref (ref 2))
(def my-agent (agent 3))

(deftest atom?-test
  (is (atom? my-atom))
  (is (not (atom? my-var)))
  (is (not (atom? my-ref)))
  (is (not (atom? my-agent)))
  (is (not (atom? nil))))

(deftest ref?-test
  (is (ref? my-ref))
  (is (not (ref? my-var)))
  (is (not (ref? my-atom)))
  (is (not (ref? my-agent)))
  (is (not (ref? nil))))

(deftest agent?-test
  (is (agent? my-agent))
  (is (not (agent? my-var)))
  (is (not (agent? my-atom)))
  (is (not (agent? my-ref)))
  (is (not (agent? nil))))

(deftest reftype?-test
  (is (reftype? my-atom))
  (is (reftype? my-ref))
  (is (reftype? my-agent))
  (is (not (reftype? my-var)))
  (is (not (reftype? nil))))
