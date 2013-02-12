(defproject huckster "0.1.0-SNAPSHOT"
  :description "Huckster. Flip Your Domains."
  :url "http://github.com/rboyd/huckster"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.immutant/immutant "0.8.0"]
                 [com.novemberain/monger "1.4.2"]]

  :immutant {:init huckster.core/init
             :context-path "/"}
  )
