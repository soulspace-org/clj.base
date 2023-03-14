;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;
(ns org.soulspace.clj.java.net
  (:require [clojure.java.io :as io]))

;;
;; Functions for network access
;;

(defn url-content
  "Read the content from an URL ."
  ([url]
    (slurp url))
  ([url timeout]
    (let [conn (.openConnection (io/as-url url))]
      (.setConnectTimeout conn timeout)
      (.connect conn)
      (slurp (.getInputStream conn)))))

(defn test-url
  "Test an URL, returns true if content is available."
  ([url]
   (test-url url 500))
  ([url timeout]
   (try
     (let [conn (.openConnection url)]
       (.setConnectTimeout conn timeout)
       (.connect conn)
       (contains? #{200} (.getResponseCode conn)))
     (catch Exception _ false))))
