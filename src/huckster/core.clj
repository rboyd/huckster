(ns huckster.core
  (:require [immutant.web :as web]
            [huckster.db :as db]))

(defn make-handler  [sub-context]
  (fn [{:keys [context path-info] :as request}]
    (db/save-hit (:remote-addr request) (:server-name request))
    {:status 200
     :content-type "text/plain"
     :body (pr-str {:mounted-sub-context sub-context
                    :request-context context
                    :request-path-info path-info})}))

(defn init []
  ;; responds to /foo/
  (web/start "/" (make-handler "/"))
  ;; responds to /foo/bar/
  (web/start "/bar" (make-handler "/bar")))
