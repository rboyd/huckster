(ns huckster.stats
  (:require [clj-time.coerce]
            [clj-time.format]
            [monger.collection :as mgc]
            [huckster.core]))

(defn date-from-hit
  "Given a hit record, returns a YYYY-MM-DD date string."
  [hit]
  (-> hit
      :_id
      .getTime
      clj-time.coerce/from-long
      (->> (clj-time.format/unparse (clj-time.format/formatters :date)))))

(defn update-counts [a b]
  (let [b-date (first (keys b))
        b-domain (first (keys (b b-date)))]
    (if (contains? a b-date)
      (if (contains? (a b-date) b-domain)
        (update-in a [b-date b-domain] inc)
        (assoc-in a [b-date b-domain] 1))
      (merge a b))))

(defn map-from-hit [hit]
  {(date-from-hit hit)
   {(-> hit :server-addr huckster.core/domain-from-hostname)
    1}})

(defn hits-by-date []
  (let [hits        (mgc/find-maps "hits")
        mapped-hits (map map-from-hit hits)]
    (reduce update-counts mapped-hits)))

(defn sorted-by-value [m]
  (sort-by val > m))

(defn top-hits-for-date [hits-by-date date]
  (sorted-by-value (hits-by-date date)))

(defn top-hits-total [hits-by-date]
  (let [dates (keys hits-by-date)
        all-dates (map #(hits-by-date %) dates)
        union (reduce #(merge-with + %1 %2) all-dates)]
    (sorted-by-value union)))
