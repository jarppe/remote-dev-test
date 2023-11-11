(ns hello.index
  (:require ["react-dom/client" :as rdom]
            [helix.core :as hx :refer [defnc $ <>]]
            [helix.dom :as d]
            [helix.hooks :as hooks]))


(defonce root (rdom/createRoot (js/document.getElementById "app")))


(defnc App []
  (let [[clicks set-clicks] (hooks/use-state 0)]
    (<>
     (d/h1 "Hello")
     (d/div "clicks: " (d/b clicks))
     (d/div
      (d/button {:on-click (fn [_] (set-clicks inc))} "+")))))


(defn ^:export start []
  (js/console.log "Here we go again...")
  (.render root ($ App)))
