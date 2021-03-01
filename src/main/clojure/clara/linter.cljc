(ns clara.linter
  (:require [clojure.string :as clj-str]))


(defn validate-duplicate-result-bindings
  [productions]
  (doseq [p productions
          :let [fact-binding-frequencies (frequencies (into
                                                       []
                                                       (comp (filter :fact-binding)
                                                             (map :fact-binding))
                                                       (:lhs p)))
                cardinalities-set (-> fact-binding-frequencies vals set)]]
    (when-not (every? #(<= % 1) cardinalities-set)
      (let [duplicate-bindings (->> fact-binding-frequencies
                                    (filter (fn [[k v]] (> v 1)))
                                    (map first))
            duplicates (->> duplicate-bindings
                            (map name)
                            (clj-str/join ","))]
        (throw (ex-info (str "Duplicate fact-binding in rule or query " (:name p) ", binding names: " duplicates)
                        {:production p
                         :duplicate-fact-bindings (set duplicate-bindings)}))))))

(defn validate-productions
  [productions]
  (validate-duplicate-result-bindings productions)
  productions)
