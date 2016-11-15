(ns clojusc.env-ini.ini
  "Clojure(script) INI support."
  (:require [clojure.string :as string]
            [clojure.walk :as walk]
            #?(:clj [clojure-ini.core :as ini])
            [clojusc.cljs-tools :as tools]
            [clojusc.env-ini.util :as util])
  #?(:clj (:import [clojure.lang Keyword]))
  (:refer-clojure :exclude [get read]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Wrapper for clojure-ini   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

#?(:cljs
  (defn wrap-nodejs-ini
    ""
    [filename]
    (let [fs (js/require "fs")
          ini (js/require "ini")]
      (->> "utf-8"
           (.readFileSync fs filename)
           (.parse ini)
           (tools/jsx->clj)))))

(defn read-as-keywords
  ""
  [filename]
  #?(:clj (util/inistrs->keywords (ini/read-ini filename)))
  #?(:cljs (util/inistrs->keywords (wrap-nodejs-ini filename))))

(defn read-as-is
  ""
  [filename]
  #?(:clj (ini/read-ini filename))
  #?(:cljs (wrap-nodejs-ini filename)))

(defn read-ini
  ""
  [filename & {:keys [keywordize?]
               :or {keywordize? true}}]
  (if keywordize?
    (read-as-keywords filename)
    (read-as-is filename)))

(def memoized-read-ini (memoize read-ini))

(defn read
  [filename & {:keys [force-reload? keywordize?]
               :or {force-reload? false keywordize? true}
               :as all-args}]
  (let [args (flatten
               (into [(tools/expand-home filename)]
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
