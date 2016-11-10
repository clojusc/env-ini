(ns clojusc.env-ini.util
  "General utility functions."
  (:require [clojure.string :as string]
            [clojure.walk :as walk]
            #?@(:cljs [
            [goog.string :as gstring]
            [goog.string.format]])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   General Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn dash->under [str]
  (string/replace str "-" "_"))

(defn under->dash [str]
  (string/replace str "_" "-"))

(defn get-home
  ""
  []
  #?(:clj (System/getenv "HOME")
     :cljs (aget js/process "env" "HOME")))

(defn expand-home
  ""
  [filename]
  (string/replace-first filename "~" (get-home)))

#?(:cljs
  (defn jsx->clj
    [obj]
    (into {} (map #(vector % (aget data %)) (.keys js/Object data)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   ENV Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
    #?(:clj (format "%s_%s" (keyword->envstr section)
                            (keyword->envstr key)))
    #?(:cljs (gstring/format "%s_%s" (keyword->envstr section)
                                     (keyword->envstr key)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   INI Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn inistr->keyword [str]
  (-> str
      (under->dash)
      (string/lower-case)
      (keyword)))

(defn inistrs->keywords
  [data]
  (let [f (fn [[k v]] (if (string? k) [(inistr->keyword k) v] [k v]))]
    (walk/postwalk (fn [x] (if (map? x) (into {} (map f x)) x)) data)))
