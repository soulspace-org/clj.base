
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

(ns org.soulspace.clj.property-replacement-test
  (:require [clojure.test :refer :all]
            [org.soulspace.clj.property-replacement :refer :all]))

(deftest replace-properties-test
  (testing "Simple property replacement"
    (is (= "Value" (replace-properties {:value "Value"} "${value}")))
    (is (= "Howdy Rich!" (replace-properties {:greetings "Howdy" :user "Rich"}
                                             "${greetings} ${user}!")))))

(deftest replace-properties-recursive-test
  (testing "Recursive property replacement with simple properties"
    (is (= "Value" (replace-properties-recursive {:value "Value"} "${value}")))
    (is (= "Howdy Rich!" (replace-properties-recursive {:greetings "Howdy" :user "Rich"}
                                                       "${greetings} ${user}!"))))
  (testing "Recursive property replacement with recursive properties"
    (is (= "Value" (replace-properties-recursive {:value "${value1}" :value1 "Value"}
                                                 "${value}")))

    ; not implemented yet, not sure if this is relevant
    ; (is (= "Value" (replace-properties-recursive {:value "value1"
    ;                                               :value1 "Value"} "${${value}}")))
    ))

