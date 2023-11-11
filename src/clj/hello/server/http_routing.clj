(ns hello.server.http-routing
  (:require [integrant.core :as ig]
            [metosin.nima-ring.static-content :as sc]))


(defmethod ig/init-key ::routing [_ {:keys [mode]}]
  [[:get "/api/ping" (constantly {:status 200
                                  :body   {:server "Hello"}})]
   (when (= mode :dev)
     [:service "/" (sc/static-files-service "public" {:index "index.html"})])])
     