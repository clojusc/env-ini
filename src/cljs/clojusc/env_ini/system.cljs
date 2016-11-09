(ns clojusc.env-ini.system
  "Clojurescript system support."
  (:require [clojusc.env-ini.util :as util]))

(def aget-env (partial aget js/process "env"))

(defn getenv
  ([]
    (util/jsx->clj (aget-env)))
  ([key]
    (aget-env key)))
