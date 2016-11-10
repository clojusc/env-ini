(ns clojusc.env-ini.system
  "Clojure(script) system support."
  #?(:cljs (:require [clojusc.env-ini.util :as util])))

#?(:cljs
  (def aget-env (partial aget js/process "env")))

(defn getenv
  ([]
    #?(:cljs (util/jsx->clj (aget-env)))
    #?(:clj (System/getenv)))
  ([key]
    #?(:cljs (aget-env key))
    #?(:clj (System/getenv key))))

