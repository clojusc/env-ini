(defproject clojusc/env-ini "0.2.0-SNAPSHOT"
  :description "Access config data from the ENV or INI files"
  :url "https://github.com/clojusc/env-ini"
  :license
    {:name "Apache License, Version 2.0"
     :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies
    [[org.clojure/clojure "1.8.0"]
     [org.clojure/clojurescript "1.8.51"]
     [clojure-ini "0.0.2"]]
  :source-paths ["src/cljs"]
  :plugins
    [[lein-cljsbuild "1.1.4"]
     [lein-npm "0.6.2"]]
  :npm
    {:dependencies
      [[better-require "0.0.3"]]}
  :cljsbuild {
    :builds [
    {:id "env-ini"
     :source-paths ["src/cljs"]
     :compiler
       {:main "env-ini.core"
        :asset-path "js/out"
        :output-to "resources/public/js/env-ini.js"
        :output-dir "resources/public/js/out"}}]}
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
      ^{:doc "Start a Node.js-based Clojurescript REPL"}
      ["trampoline" "run" "-m" "clojure.main"
       "dev-resources/src/clj/clojusc/env_ini/browser-dev.clj"]
     }
  :profiles {
    :dev {
      :source-paths ["dev-resources/src/clj"]
      :repl-options {:init-ns clojusc.env-ini.dev}
      :dependencies [
        [org.clojure/tools.namespace "0.2.11"
         :exclusions [org.clojure/clojure]]]
      :plugins [
        [lein-figwheel "0.5.4-7"]
        [figwheel-sidecar "0.5.8"]]}})
