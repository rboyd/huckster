(ns huckster.stats-test
  (:use clojure.test
        huckster.stats))

(deftest test-update-counts
  (testing "Merges hits, accumulating counters."
    (let [hit            {"2013-02-16" {"test.com" 1}}
          expected-merge {"2013-02-16" {"test.com" 2}}]
      (is (= expected-merge (update-counts hit hit))))))
