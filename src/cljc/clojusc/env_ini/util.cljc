(ns clojusc.env-ini.util
  (:require [clojure.string :as string]))

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
  (.replaceFirst filename "^~" (get-home)))
