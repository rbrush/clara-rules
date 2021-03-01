(ns clara.test-linter
  (:require [clara.rules :refer :all]
            [clojure.test :refer :all]
            [clara.rules.testfacts :refer :all]
            [clara.rules.dsl :as dsl]
            [clara.tools.testing-utils :refer [assert-ex-data]])
  (:import [clara.rules.testfacts
            Cold Hot]))


(deftest duplicate-fact-binding-test
  (assert-ex-data {:duplicate-fact-bindings #{:?weather}}
                  (let [rule (dsl/parse-rule [[?weather <- Hot]
                                              [?weather <- Cold]]
                                             (println "Hello world"))]
                    (mk-session [rule] :cache false))))
