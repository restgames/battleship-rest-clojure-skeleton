(ns battleship.infrastructure.persistence.redis
  (:require [taoensso.carmine :as car :refer (wcar)]))

(defn save-game!
  [game]
  (let [uuid (:id game)
        serialized-game {:id (:id game) :grid (:grid game) :next-shot (:next-shot game)}]
    (car/wcar {} (car/set uuid serialized-game)))
  game)

(defn of-id
  [uuid]
  (car/wcar {} (car/get uuid)))

(defn finish-game!
  [game]
  (car/wcar {} (car/del (:id game))))