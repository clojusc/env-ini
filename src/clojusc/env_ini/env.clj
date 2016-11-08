(ns clojusc.env-ini.env
  (:require [clojure.string :as string]
            [clojusc.env-ini.util :as util])
  (:import [clojure.lang Keyword])
  (:refer-clojure :exclude [get read]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   ENV-specific Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Wrapper for System/getenv   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti get-env
  (fn ([& args]
    (mapv class (into [] args)))))

(defmethod get-env [String] [key]
  (System/getenv (str->envstr key)))

(defmethod get-env [Keyword] [key]
  (System/getenv (keyword->envstr key)))

(defmethod get-env [Keyword Keyword] [section key]
  (System/getenv (section-key->env section key)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   ENV Reader   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn read-env
  ""
  [& {:keys [keywordize?]
      :or {keywordize? true}}]
  (if keywordize?
    (envstrs->keywords (System/getenv))
    (System/getenv)))

(def memoized-read-env (memoize read-env))

(defn read
  [& {:keys [force-reload? keywordize?]
      :or {force-reload? false keywordize? true}
      :as all-args}]
  (let [args (flatten (into [] (dissoc all-args :force-reload?)))]
    (if force-reload?
      (apply read-env args)
      (apply memoized-read-env args))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   INI Operations Against Data   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get
  ""
  ([data key]
    (get-in data [:env key]))
  ([data section key]
    (get-in data [:env (section-key->env section key)])))
