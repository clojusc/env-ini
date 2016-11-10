(ns clojusc.env-ini.ini
  "Clojurescript INI support."
  (:require [clojure.string :as string]
            [clojure.walk :as walk]
            ;[clojure-ini.core :as ini]
            [clojusc.env-ini.util :as util])
  (:refer-clojure :exclude [get read]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Wrapper for clojure-ini   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn read-ini
  ""
  [filename & {:keys [keywordize?]
               :or {keywordize? true}}]
  ; (if keywordize?
  ;   (common/inistrs->keywords (ini/read-ini filename))
  ;   (ini/read-ini filename)))
  {})

(def memoized-read-ini (memoize read-ini))

(defn read
  [filename & {:keys [force-reload? keywordize?]
               :or {force-reload? false keywordize? true}
               :as all-args}]
  (let [args (flatten
               (into [(util/expand-home filename)]
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
