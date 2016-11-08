(ns clojusc.env-ini.util
  (:require [clojure.string :as string]))

(defn dash->under [str]
  (string/replace str "-" "_"))

(defn under->dash [str]
  (string/replace str "_" "-"))
