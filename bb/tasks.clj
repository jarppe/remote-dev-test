(ns tasks
  (:require [babashka.process :as p]
            [clojure.string :as str]))


(defn instance-ip [instance-name]
  (-> (p/process "gcloud compute instances describe" instance-name "--format=json")
      (p/process "jq -r .networkInterfaces[].accessConfigs[].natIP")
      (p/check)
      :out
      (slurp)
      (str/trim)))


(defn instance-status [instance-name]
  (-> (p/process "gcloud compute instances describe" instance-name "--format=json")
      (p/process "jq -r .status")
      (p/check)
      :out
      (slurp)
      (str/trim)))
