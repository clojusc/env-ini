(ns clojusc.ini-env-test
  (:require [clojure.test :refer :all]
            [clojusc.ini-env.core :as ini-env]))

(deftest no-op
  (is (= 1 1)))
