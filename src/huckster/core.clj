(ns huckster.core
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [immutant.web :as web]
            [net.cgrand.enlive-html :as html]
            [huckster.db :as db]))

(defroutes app-routes
  (let [index (html/html-resource (java.io.StringReader. (slurp "resources/public/index.html")))]
    
  
    (GET "/" [] (resp/resource-response "index.html" {:root "public"})))
  (route/resources "/"))

(defn make-handler  [sub-context]
  (fn [{:keys [context path-info] :as request}]
    (db/save-hit (:remote-addr request) (:server-name request))
    {:status 200
     :content-type "text/plain"
     :body (pr-str {:mounted-sub-context sub-context
                    :request-context context
                    :request-path-info path-info})}))

(defn init []
  (web/start "/" app-routes))
