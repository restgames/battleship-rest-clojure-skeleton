(ns battleship.config
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [battleship.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[battleship started successfully using the development profile]=-"))
   :middleware wrap-dev})
