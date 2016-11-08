(defproject clojusc/env-ini "0.1.0"
  :description "Access config data from the ENV or INI files"
  :url "https://github.com/clojusc/env-ini"
  :license
    {:name "Apache License, Version 2.0"
     :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies
    [[org.clojure/clojure "1.8.0"]
     [clojure-ini "0.0.2"]]
  :profiles {
    :dev {
      :source-paths ["dev-resources/src"]
      :repl-options {:init-ns clojusc.env-ini.dev}
      :dependencies [
        [org.clojure/tools.namespace "0.2.11"
         :exclusions [org.clojure/clojure]]]}})
