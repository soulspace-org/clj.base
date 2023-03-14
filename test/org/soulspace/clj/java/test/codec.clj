;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;

(ns org.soulspace.clj.java.test.codec
  (:require [clojure.test :refer :all]
            [org.soulspace.clj.java.codec :refer :all]))

(def s1 "Hello World!")

(deftest base64-test
  (is (= s1 (String. (base64->bytes (bytes->base64 (.getBytes s1)))))))

(deftest hex-test
  (is (= s1 (String. (hex->bytes (bytes->hex (.getBytes s1)))))))

