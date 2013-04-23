(ns huckster.piwik
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [environ.core :refer [env]]))

(defn get-sites
  "Gets a map of all sites registered with piwik."
  []
  (let [url    (env :piwik-url)
        token  (env :piwik-token)
        query  {:module "API"
                :method "SitesManager.getAllSites"
                :format "JSON"}]
    (-> (client/get url {:query-params (merge query {:token_auth token})})
        :body
        json/read-str)))

(defn sites-to-ids
  "Map of name -> id."
  [sites]
  (reduce merge (map #(identity {((second %) "name") (first %)}) sites)))

(def site-map (atom (sites-to-ids (get-sites))))

(defn emit-script
  "Given a piwik hostname and a site id, generates the tracking code."
  [name]
  (let [piwik-host (env :piwik-host)
        site-id    (@site-map name)]
    (-> (slurp "resources/templates/piwik/tracker.js")
        (clojure.string/replace #"\{PIWIK_HOST\}" piwik-host)
        (clojure.string/replace #"\{SITE_ID\}" site-id))))

(defn add-site
  "Add a site to Piwik via the API."
  [domain]
  (let [url    (env :piwik-url)
        token  (env :piwik-token)
        query  {:module "API"
                :method "SitesManager.addSite"
                :format "JSON"
                :siteName domain
                :urls [(str "http://" domain "/")]}]
    (client/post url {:query-params (merge query {:token_auth token})})))
