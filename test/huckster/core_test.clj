(ns huckster.core-test
  (:use clojure.test
        huckster.core))

(deftest test-domain-from-hostname
  (testing "Given a full hostname, return downcased sld.tld domain name"
    (is (= "domain.com" (domain-from-hostname "www.DOMAIN.COM")))))
