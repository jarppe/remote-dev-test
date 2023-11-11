(ns hello.server.http-server
  (:require [clojure.tools.logging :as log]
            [integrant.core :as ig]
            [metosin.nima-ring.server :as nima]))


(set! *warn-on-reflection* true)


(defmethod ig/init-key ::server [_ {:keys [routing]}]
  (let [host (or (System/getenv "HOST") "0.0.0.0")
        port (parse-long (or (System/getenv "PORT") "8080"))]
    (log/infof "starting http server %s:%s..." host port)
    (nima/create-server routing {:host host
                                 :port port})))


(defmethod ig/halt-key! ::server [_ server]
  (log/debug "closing http server")
  (nima/shutdown server))
