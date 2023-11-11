(ns user
  (:require [integrant.repl :as igr]
            [integrant.repl.state :as state]))


;;
;; Integrant repl setup:
;;


(igr/set-prep!
 (fn [] ((requiring-resolve 'hello.server.system/prepare-system))))


#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn start []
  (igr/init))


#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn stop []
  (igr/halt))


#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn reset []
  (igr/reset))


(defn system [] state/system)
