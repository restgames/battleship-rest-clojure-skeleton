(ns battleship.config
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[battleship started successfully]=-"))
   :middleware identity})
