(ns clojusc.ini-env.dev
  (:require [clojure.pprint :refer [print-table]]
            [clojure.reflect :refer [reflect]]
            [clojure.string :as string]
            [clojusc.ini-env :as ini-env]))

(defn show-methods
  ""
  [obj]
  (print-table
    (sort-by :name
      (filter :exception-types (:members (reflect obj))))))

