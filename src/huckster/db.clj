(ns huckster.db
  (:require [monger.core :as mg]
            [monger.collection :as mgc]
            [clj-time.format])
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

(defn save-offer
  "Saves a new offer to the database."
  [remote-ip server-addr email offer]
    (let [id (ObjectId.)]
      (mgc/insert "offers" {:_id id :remote-ip remote-ip :server-addr server-addr :email email :offer offer})))
