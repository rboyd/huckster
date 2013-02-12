(ns huckster.db
  (:require [monger.core :as mg]
            [monger.collection :as mgc])
  (:import [org.bson.types ObjectId]))

(mg/connect!)
(mg/set-db! (mg/get-db "huckster"))

(defn get-domains
  "Gets a list of domains from the database."
  []
  (->
   (mgc/find-maps "domains")))

(defn save-hit
  "Saves a new hit to the database."
  [remote-ip server-addr]
    (let [id (ObjectId.)]
      (mgc/insert "hits" {:_id id :remote-ip remote-ip :server-addr server-addr})))
