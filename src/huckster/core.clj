(ns huckster.core
  (:use compojure.core)
  (:require [huckster.db :as db]
            [immutant.web :as web]
            [huckster.alert :as alert]
            [huckster.piwik :as piwik]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [net.cgrand.enlive-html :as html]
            [ring.util.response :refer [response]]
            [ring.middleware.params :refer [wrap-params]]))

(defn domain-from-hostname [hostname]
  (let [[tld sld & rest] (reverse (clojure.string/split hostname #"\."))]
    (clojure.string/lower-case (str sld "." tld))))

(html/deftemplate index (java.io.StringReader. (slurp "resources/public/index.html"))
  [ctxt]
  [:span#domain] (html/content (domain-from-hostname (:domain ctxt))))

(defn render [t]
  (apply str t))

(defroutes app-routes
  (GET "/" {:keys [server-name remote-addr]}
       (let [resp   (render (index {:domain server-name}))
             domain (domain-from-hostname server-name)]
         (db/save-hit remote-addr server-name)
         (-> (if (contains? @piwik/site-map domain)
               (clojure.string/replace resp #"</body>" (str (piwik/emit-script domain) "</body>"))
               resp)
             response)))
  (POST "/offers" {:keys [server-name remote-addr params]}
        (let [domain server-name
              email (params "email")
              offer (params "offer")]
          (do
            (db/save-offer remote-addr server-name email offer)
            (if-not (clojure.string/blank? offer)
              (alert/sms (str email " offered " offer " for " domain)))
            (response "Thank you."))))
  (route/resources "/"))

(defn init []
  (web/start "/" (wrap-params app-routes)))
