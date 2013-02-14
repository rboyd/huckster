(ns huckster.core
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [response]]
            [immutant.web :as web]
            [net.cgrand.enlive-html :as html]
            [huckster.db :as db]))

(defn domain-from-hostname [hostname]
  (let [[tld sld & rest] (reverse (clojure.string/split hostname #"\."))]
    (clojure.string/lower-case (str sld "." tld))))

(html/deftemplate index (java.io.StringReader. (slurp "resources/public/index.html"))
  [ctxt]
  [:span#domain] (html/content (domain-from-hostname (:domain ctxt))))

(defn render [t]
  (apply str t))

(def render-to-response
     (comp response render))

(defroutes app-routes
  (GET "/" {:keys [server-name remote-addr]}
       (do
         (db/save-hit remote-addr server-name)
         (render-to-response (index {:domain server-name}))))
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
