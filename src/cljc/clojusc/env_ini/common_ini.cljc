(ns clojusc.env-ini.common-ini
  "INI-specific Utility Functions"
  (:require [clojure.string :as string]
            [clojure.walk :as walk]
            [clojusc.env-ini.util :as util]))

(defn inistr->keyword [str]
  (-> str
      (util/under->dash)
      (string/lower-case)
      (keyword)))

(defn inistrs->keywords
  [data]
  (let [f (fn [[k v]] (if (string? k) [(inistr->keyword k) v] [k v]))]
    (walk/postwalk (fn [x] (if (map? x) (into {} (map f x)) x)) data)))
