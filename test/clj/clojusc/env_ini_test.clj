(ns clojusc.env-ini-test
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [clojure.test :refer :all]
            [clojure-ini.core :as config-ini]
            [clojusc.env-ini.core :as env-ini]
            [clojusc.env-ini.ini :as ini])
  (:gen-class))

(defn -main
  [mode & args]
  (case (keyword mode)
    :show-example-ini (pprint (ini/read "example-config.ini"))))
