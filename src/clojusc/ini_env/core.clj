(ns clojusc.ini-env.core
  "Combined functions for config INI files and ENV data."
  (:require [clojusc.ini-env.env :as env]
            [clojusc.ini-env.ini :as ini])
  (:refer-clojure :exclude [get]))

(defn get
  ""
  ([data key]
    (or (env/get data key)
        (env/get data :default key)
        (ini/get data :default key)))
  ([data section key]
    (or (env/get data section key)
        (ini/get data section key)))
  ([data env-key section ini-key]
    (or (get data env-key)
        (get data section ini-key))))

(defn load-data
  ""
  [filename & args]
  {:ini (apply #'ini/read (into [filename] args))
   :env (apply #'env/read args)})
