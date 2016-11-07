(ns clojusc.ini-env
  (:require [clojure.string :as string]
            [clojure-ini.core :as ini])
  (:import [clojure.lang Keyword])
  (:refer-clojure :exclude [get]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   INI Convenience Functions / Wrappers   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   ENV Convenience Functions / Wrappers   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn dash->under [str]
  (string/replace str "-" "_"))

(defn under->dash [str]
  (string/replace str "_" "-"))

(defn str->envstr [str]
  (-> str
      (string/upper-case)
      (dash->under)))

(defn keyword->envstr [kwd]
  (-> kwd
      (name)
      (string/upper-case)
      (dash->under)))

(defn envstr->keyword [str]
  (-> str
      (under->dash)
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

(defmulti get-env
  (fn ([& args]
    (mapv class (into [] args)))))

(defmethod get-env [] []
  (envstrs->keywords (System/getenv)))

(defmethod get-env [String] [key]
  (System/getenv (str->envstr key)))

(defmethod get-env [Keyword] [key]
  (System/getenv (keyword->envstr key)))

(defmethod get-env [Keyword Keyword] [section key]
  (System/getenv (section-key->env section key)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Combined Access   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get
  ""
  ([key]
    (or (get-env key)
        (get-env :default key)))
  ([section key]
    (get-env section key)))
