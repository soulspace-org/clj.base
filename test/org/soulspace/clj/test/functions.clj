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

(ns org.soulspace.clj.test.functions
  (:require [clojure.test :refer :all]
            [org.soulspace.clj.core :refer :all]))

(def my-var 0)
(def my-atom (atom 1))
(def my-ref (ref 2))
(def my-agent (agent 3))

(deftest not-nil?-test
  (is (not-nil? 1))
  (is (not-nil? true))
  (is (not-nil? false))
  (is (not-nil? ""))
  (is (not-nil? []))
  (is (not (not-nil? nil))))

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
