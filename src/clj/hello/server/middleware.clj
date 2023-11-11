(ns hello.server.middleware
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [ring.util.http-response :as resp]))


(set! *warn-on-reflection* true)


(defn exception-middleware [handler]
  (fn [req]
    (try
      (handler req)
      (catch Exception e
        (let [{:keys [type response]} (ex-data e)]
          (if (= type ::resp/response)
            response
            (do (log/errorf e "unexpected exception: method=%s, uri=%s"
                            (-> req :request-method (name) (str/upper-case))
                            (-> req :uri))
                (resp/internal-server-error {:message "Unexpected exception"}))))))))
