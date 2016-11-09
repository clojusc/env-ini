(ns clojusc.env-ini.common-env
  "ENV-specific Utility Functions."
  (:require [clojure.string :as string]
            [clojusc.env-ini.util :as util])
  (:refer-clojure :exclude [get read]))

(defn str->envstr [str]
  (-> str
      (string/upper-case)
      (util/dash->under)))

(defn keyword->envstr [kwd]
  (-> kwd
      (name)
      (string/upper-case)
      (util/dash->under)))

(defn envstr->keyword [str]
  (-> str
      (util/under->dash)
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
    (format "%s_%s" (keyword->envstr section)
                    (keyword->envstr key))))
