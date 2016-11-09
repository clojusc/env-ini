(ns clojusc.env-ini.browser
  (:require [clojure.browser.repl :as repl]
            [clojure.pprint :refer [print-table]]
            [clojure.reflect :refer [reflect]]
            [clojure.string :as string]
            [clojure-ini.core :as config-ini]
            [clojusc.env-ini.core :as env-ini]
            [clojusc.env-ini.env :as env]
            [clojusc.env-ini.ini :as ini]
            [clojusc.env-ini.util :as util]))

(repl/connect "http://localhost:9000/repl")

(enable-console-print!)

(println "Hello world!")
