(defproject org.soulspace.clj/clj.base "0.9.0-SNAPSHOT"
  :description "The clj.base library provides functionality for Clojure with no external dependencies."
  :url "https://github.com/soulspace-org/clj.base"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  ; use deps.edn dependencies
  :plugins [[lein-tools-deps "0.4.5"]]
  :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
  :lein-tools-deps/config {:config-files [:install :user :project]}

  :test-paths ["test"]
  :scm {:name "git" :url "https://github.com/soulspace-org/clj.base"}
  :deploy-repositories [["clojars" {:sign-releases false :url "https://clojars.org/repo"}]])
