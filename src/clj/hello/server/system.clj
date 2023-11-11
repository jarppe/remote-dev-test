(ns hello.server.system
  (:require [clojure.tools.logging :as log]
            [integrant.core :as ig]
            [hello.server.http-server :as http-server]
            [hello.server.http-routing :as http-routing]))


(defn components [mode]
  {::http-routing/routing {:mode mode}
   ::http-server/server   {:routing (ig/ref ::http-routing/routing)}})


(defn prepare-system []
  ; Use virtual threads for all async tasks:
  (doto (java.util.concurrent.Executors/newVirtualThreadPerTaskExecutor)
    (set-agent-send-executor!)
    (set-agent-send-off-executor!))
  ; Start service:
  (let [prop-mode (System/getenv "MODE")
        mode      (case prop-mode
                    "dev" :dev
                    "prod" :prod
                    (throw (ex-info "environment variable $MODE required to have value \"dev\" or \"prod\"" {:mode prop-mode})))]
    (log/infof "MODE: %s - JDK %s version %s, clojure version %s"
               mode
               (System/getProperty "java.vendor")
               (System/getProperty "java.version")
               (clojure-version))
    (components mode)))


(defn start-system []
  (log/info "starting")
  (let [system (-> (prepare-system)
                   (ig/init))]
    (log/info "running")
    system))



(defn stop-system [system]
  (ig/halt! system))

