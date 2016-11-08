(ns clojusc.ini-env.dev
  (:require [clojure.pprint :refer [print-table]]
            [clojure.reflect :refer [reflect]]
            [clojure.string :as string]
            [clojure-ini.core :as config-ini]
            [clojusc.ini-env.core :as ini-env]
            [clojusc.ini-env.env :as env]
            [clojusc.ini-env.ini :as ini]
            [clojusc.ini-env.util :as util]))

(defn show-methods
  ""
  [obj]
  (print-table
    (sort-by :name
      (filter :exception-types (:members (reflect obj))))))

