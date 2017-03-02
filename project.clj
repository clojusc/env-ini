(defproject clojusc/env-ini "0.3.0"
  :description "Clojure(script) functions for accessing config data from the ENV or INI files"
  :url "https://github.com/clojusc/env-ini"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [
    [org.clojure/clojure "1.8.0"]
    [org.clojure/clojurescript "1.9.495"]
    [clojure-ini "0.0.2"]
    [clojusc/cljs-tools "0.1.2"]]
  :plugins [
    [lein-cljsbuild "1.1.5"]
    [lein-npm "0.6.2"]]
  :npm {
    :dependencies [
      [ini "1.3.4"]]}
  :clean-targets ^{:protect false}
    ["resources/public/js"
     "target"]
  :cljsbuild {
    :builds [{
      :id "env-ini"
      :compiler {
        :main "env-ini.core"
        :output-to "resources/public/js/env_ini.js"
        :output-dir "resources/public/js"}} {
      :id "node"
      :compiler {
        :target :nodejs
        :output-to "target/node/env_ini.js"
        :output-dir "target/node"}}]}
  :aliases {
    "rhino-repl"
      ^{:doc "Start a Rhino-based Clojurescript REPL"}
      ["trampoline" "run" "-m" "clojure.main"
       "dev-resources/src/clj/clojusc/env_ini/rhino-dev.clj"]
    "node-repl"
      ^{:doc "Start a Node.js-based Clojurescript REPL"}
      ["trampoline" "run" "-m" "clojure.main"
       "dev-resources/src/clj/clojusc/env_ini/node-dev.clj"]
    "browser-repl"
      ^{:doc "Start a browser-based Clojurescript REPL"}
      ["trampoline" "run" "-m" "clojure.main"
       "dev-resources/src/clj/clojusc/env_ini/browser-dev.clj"]}
  :profiles {
    :uberjar {
      :aot :all
      :source-paths ["src" "test/clj"]}
    :test {
      :plugins [
        [jonase/eastwood "0.2.3" :exclusions [org.clojure/clojure]]
        [lein-kibit "0.1.3" :exclusions [org.clojure/clojure]]]
      :test-selectors {
        :default :unit
        :unit :unit
        :system :system
        :integration :integration}
      :source-paths ["test/clj"]}
    :dev {
      :source-paths ["dev-resources/src/clj"]
      :repl-options {
        :init-ns clojusc.env-ini.dev}
      :dependencies [
        [org.clojure/tools.namespace "0.2.11"
         :exclusions [org.clojure/clojure]]]}})
