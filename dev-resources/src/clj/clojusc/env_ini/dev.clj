(ns clojusc.env-ini.dev
  (:require [clojure.pprint :refer [print-table]]
            [clojure.reflect :refer [reflect]]
            [clojure.string :as string]
            [clojure-ini.core :as config-ini]
            [clojusc.env-ini.core :as env-ini]
            [clojusc.env-ini.env :as env]
            [clojusc.env-ini.ini :as ini]
            [clojusc.env-ini.system :as system]
            [clojusc.env-ini.util :as util]
            [clojusc.cljs-tools :as tools]))

(defn show-methods
  ""
  [obj]
  (print-table
    (sort-by :name
      (filter :exception-types (:members (reflect obj))))))

