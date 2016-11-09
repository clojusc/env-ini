(ns clojusc.env-ini.system
  "Clojure system support.")

(defn getenv
  ([]
    (System/getenv))
  ([key]
    (System/getenv key)))
