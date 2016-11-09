(ns clojusc.env-ini.env
  (:require [clojure.string :as string]
            [clojusc.env-ini.common-env :as common]
            [clojusc.env-ini.util :as util])
  (:refer-clojure :exclude [get read]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Utility functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn jsx->clj
  [obj]
  (into {} (for [k (.keys js/Object obj)] [k (aget obj k)])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Wrapper for aget js/process "env"   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def system-getenv (partial aget js/process "env"))

(defmulti get-env
  (fn ([& args]
    (mapv class (into [] args)))))

(defmethod get-env [String] [key]
  (system-getenv (common/str->envstr key)))

(defmethod get-env [Keyword] [key]
  (system-getenv (common/keyword->envstr key)))

(defmethod get-env [Keyword Keyword] [section key]
  (system-getenv (common/section-key->env section key)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   ENV Reader   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn read-env
  ""
  [& {:keys [keywordize?]
      :or {keywordize? true}}]
  (if keywordize?
    (common/envstrs->keywords (System/getenv))
    (jsx->clj (system-getenv))))

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
    (get-in data [:env (common/section-key->env section key)])))
