(defproject clojusc/ini-env "0.1.0-SNAPSHOT"
  :description "Access config data from INI files or the ENV"
  :url "https://github.com/clojusc/ini-env"
  :license
    {:name "Apache License, Version 2.0"
     :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies
    [[org.clojure/clojure "1.8.0"]
     [clojure-ini "0.0.2"]]
  :profiles {
    :dev {
      :source-paths ["dev-resources/src"]
      :repl-options {:init-ns clojusc.ini-env.dev}
      :dependencies [
        [org.clojure/tools.namespace "0.2.11"
         :exclusions [org.clojure/clojure]]]}})
