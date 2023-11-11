(ns hello.server.main
  (:require [hello.server.system :as system])
  (:gen-class))


(defn -main [& _]
  (try
    (system/start-system)
    (catch Throwable e
      (.println System/err "unexpected error while starting server")
      (.printStackTrace e System/err)
      (System/exit 1))))
