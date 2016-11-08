(ns clojusc.ini-env.ini
  (:require [clojure.string :as string]
            [clojure.walk :as walk]
            [clojure-ini.core :as ini]
            [clojusc.ini-env.util :as util])
  (:import [clojure.lang Keyword])
  (:refer-clojure :exclude [get read]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   ENV-specific Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn inistr->keyword [str]
  (-> str
      (util/under->dash)
      (string/lower-case)
      (keyword)))

(defn inistrs->keywords
  [data]
  (let [f (fn [[k v]] (if (string? k) [(inistr->keyword k) v] [k v]))]
    (walk/postwalk (fn [x] (if (map? x) (into {} (map f x)) x)) data)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Wrapper for clojure-ini   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn read-ini
  ""
  [filename & {:keys [keywordize?]
               :or {keywordize? true}}]
  (if keywordize?
    (inistrs->keywords (ini/read-ini filename))
    (ini/read-ini filename)))

(def memoized-read-ini (memoize read-ini))

(defn read
  [filename & {:keys [force-reload? keywordize?]
               :or {force-reload? false keywordize? true}
               :as all-args}]
  (let [args (flatten
               (into [filename]
                 (assoc
                   (dissoc all-args :force-reload?)
                   :keywordize? keywordize?)))]
    (if force-reload?
      (apply read-ini args)
      (apply memoized-read-ini args))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   INI Operations Against File   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-ini
  ""
  [filename section key & args]
  (get-in (apply read (into [filename section key] args)) [section key]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   INI Operations Against Data   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get
  ""
  [data section key]
  (get-in data [:ini section key]))
