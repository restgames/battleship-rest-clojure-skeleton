(ns battleship.infrastructure.representation.dto
  (require [clojure.string :as str]
           [clojure.core.match :refer [match]]))

(defn game-to-dto
  [game]
  {:gameId (:id game) :grid (str/join (map str (:grid game)))})

(defn shot-to-dto
  [shot]
  {:letter (:letter shot) :number (:number shot)})

(defn hit-to-dto
  [hit]
  (match [hit]
    [:water] {:result 0}
    [:hit] {:result 1}
    [:sunk] {:result 2}))