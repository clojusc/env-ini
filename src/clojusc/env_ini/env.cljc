(ns clojusc.env-ini.env
  "Clojure(script) ENV support."
  (:require [clojure.string :as string]
            [clojusc.env-ini.system :as system]
            [clojusc.env-ini.util :as util])
  #?(:clj (:import [clojure.lang Keyword]))
  (:refer-clojure :exclude [get read]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Wrapper for getenv   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti get-env
  (fn ([& args]
    (mapv type (into [] args)))))

(defmethod get-env [] [key]
  (system/getenv))

#?(:clj
  (defmethod get-env [String] [key]
    (system/getenv (util/str->envstr key))))

#?(:cljs
  (defmethod get-env [js/String] [key]
    (system/getenv (util/str->envstr key))))

(defmethod get-env [Keyword] [key]
  (system/getenv (util/keyword->envstr key)))

(defmethod get-env [Keyword Keyword] [section key]
  (system/getenv (util/section-key->env section key)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   ENV Reader   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn read-env
  ""
  [& {:keys [keywordize?]
      :or {keywordize? true}}]
  (if keywordize?
    (util/envstrs->keywords (system/getenv))
    (system/getenv)))

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
;;;   ENV Operations Against Data   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get
  ""
  ([data key]
    (get-in data [:env key]))
  ([data section key]
    (get-in data [:env (util/section-key->env section key)])))
