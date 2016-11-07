(ns clojusc.ini-env-test
  (:require [clojure.test :refer :all]
            [clojusc.ini-env :as ini-env]))

(deftest no-op
  (is (= 1 1)))
