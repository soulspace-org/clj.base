
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

(ns org.soulspace.clj.namespace-test
  (:require [clojure.test :refer :all]
            [org.soulspace.clj.namespace :refer :all]))

(deftest unqualify-keyword-test
  (testing "Unqualified keyword"
    (is (= :k (unqualify-keyword :k))))
  (testing "Namespace qualified keyword"
    (is (= :k (unqualify-keyword ::k)))))

(deftest unqualify-map-test
  (testing "Unqualified keywords keys"
    (is (= {:k 1 :j 2} (unqualify-map {:k 1 :j 2}))))
  (testing "Namespace qualified keyword keys"
    (is (= {:k 1 :j 2} (unqualify-map {::k 1 ::j 2})))))

