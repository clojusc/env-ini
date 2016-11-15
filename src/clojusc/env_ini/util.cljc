(ns clojusc.env-ini.util
  "General utility functions."
  (:require [clojure.string :as string]
            [clojure.walk :as walk]
            [clojusc.cljs-tools.core :as tools]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   ENV Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn str->envstr [str]
  (-> str
      (string/upper-case)
      (tools/dash->under)))

(defn keyword->envstr [kwd]
  (-> kwd
      (name)
      (string/upper-case)
      (tools/dash->under)))

(defn envstr->keyword [str]
  (-> str
      (tools/under->dash)
      (string/lower-case)
      (keyword)))

(defn envstrs->keywords [data]
  (zipmap
    (map envstr->keyword (keys data))
    (vals data)))

(defn section-key->env
  ""
  ([key]
    (keyword->envstr key))
  ([section key]
    (tools/format "%s_%s" (keyword->envstr section)
                          (keyword->envstr key))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   INI Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn inistr->keyword [str]
  (-> str
      (tools/under->dash)
      (string/lower-case)
      (keyword)))

(defn inistrs->keywords
  [data]
  (let [f (fn [[k v]] (if (string? k) [(inistr->keyword k) v] [k v]))]
    (walk/postwalk (fn [x] (if (map? x) (into {} (map f x)) x)) data)))
