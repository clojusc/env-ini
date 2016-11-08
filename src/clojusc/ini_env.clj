(ns clojusc.ini-env
  (:require [clojure.string :as string]
            [clojure-ini.core :as ini])
  (:import [clojure.lang Keyword])
  (:refer-clojure :exclude [get]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   INI Convenience Functions / Wrappers   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn -read-ini
  ""
  [args]
  (apply ini/read-ini args))

(def -memoized-read-ini (memoize -read-ini))

(defn read-ini
  [filename & {:keys [force-reload? keywordize?]
               :or {force-reload? false keywordize? true}
               :as all-args}]
  (let [args (flatten (into [filename] (dissoc all-args :force-reload?)))]
    (if force-reload?
      (-read-ini args)
      (-memoized-read-ini args))))

(defn get-ini
  ""
  [data section key]
  (get-in data [section key]))

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

(defn -read-env
  ""
  [& {:keys [keywordize?]
      :or {keywordize? true}}]
  (if keywordize?
    (envstrs->keywords (System/getenv))
    (System/getenv)))

(def -memoized-read-env (memoize -read-env))

(defn read-env
  [& {:keys [force-reload? keywordize?]
      :or {force-reload? false keywordize? true}
      :as all-args}]
  (let [args (flatten (into [] (dissoc all-args :force-reload?)))]
    (if force-reload?
      (apply -read-env args)
      (apply -memoized-read-env args))))

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
;;;   Combined Access   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get
  ""
  ([data key]
    (or (get-env data key)
        (get-env data :default key)
        (get-ini data :default key)))
  ([data section key]
    (or (get-env data section key)
        (get-ini data section key))))

(defn load-data
  ""
  [filename & args]
  {:ini (apply read-ini (into [filename] args))
   :env (apply read-env args)})
