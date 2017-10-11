(defproject clojusc/env-ini "0.4.0-SNAPSHOT"
  :description "Clojure(script) functions for accessing config data from the ENV or INI files"
  :url "https://github.com/clojusc/env-ini"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :exclusions [org.clojure/clojure]
  :dependencies [
    [org.clojure/clojure "1.8.0"]
    [org.clojure/clojurescript "1.9.946"]
    [clojure-ini "0.0.2"]
    [clojusc/cljs-tools "0.2.0"]]
  :plugins [
    [lein-cljsbuild "1.1.7"]
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
  :profiles {
    :ubercompile {
      :aot :all
      :source-paths ["src" "test/clj"]}
    :test {
      :plugins [
        [jonase/eastwood "0.2.4"]
        [lein-ancient "0.6.12"]
        [lein-bikeshed "0.4.1"]
        [lein-kibit "0.1.5"]
        [venantius/yagni "0.1.4"]]
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
         :exclusions [org.clojure/clojure]]]}}
  :aliases {
    "check-deps" [
      "with-profile" "+test" "ancient" "check" ":all"]
    "kibit" [
      "with-profile" "+test" "do"
        ["shell" "echo" "== Kibit =="]
        ["kibit"]]
    "outlaw" [
      "with-profile" "+test"
      "eastwood" "{:namespaces [:source-paths] :source-paths [\"src\"]}"]
    "lint" [
      "with-profile" "+test" "do"
        ["check"] ["kibit"] ["outlaw"]]
    "build" ["with-profile" "+test" "do"
      ["check-deps"]
      ["lint"]
      ["test"]
      ["compile"]
      ["with-profile" "+ubercompile" "compile"]
      ["clean"]
      ["uberjar"]]})
