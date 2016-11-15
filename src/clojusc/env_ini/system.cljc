(ns clojusc.env-ini.system
  "Clojure(script) system support."
  (:require [clojusc.cljs-tools.core :as tools]))

#?(:cljs
  (def aget-env (partial aget js/process "env")))

(defn getenv
  ([]
    #?(:cljs (tools/jsx->clj (aget-env) :nested? false :check? false))
    #?(:clj (System/getenv)))
  ([key]
    #?(:cljs (aget-env key))
    #?(:clj (System/getenv key))))

