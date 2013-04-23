(defproject huckster "0.1.0-SNAPSHOT"
  :description "Huckster. Flip Your Domains."
  :url "http://github.com/rboyd/huckster"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.novemberain/monger "1.4.2"]
                 [compojure "1.1.5"]
                 [enlive "1.0.1"]
                 [clj-http "0.6.4"]
                 [clj-time "0.4.4"]
                 [org.clojure/data.json "0.2.1"] 
                 [ring/ring-jetty-adapter "1.2.0-beta2"]
                 [environ "0.4.0"]]
  :main huckster.core)
