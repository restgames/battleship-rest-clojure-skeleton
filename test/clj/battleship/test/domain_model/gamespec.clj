(ns battleship.test.domain-model.gamespec
  (:require [battleship.uuid :as uuid]
            [battleship.domain-model.game :as game]
            [clojure.math.numeric-tower :as math])
  (:use midje.sweet))

(defn- create-new-game
  [uuid]
  (game/game
    uuid
    (str
      "0300222200"
      "0300000000"
      "0310000000"
      "0010005000"
      "0010005000"
      "0010044400"
      "0010000000"
      "0000000000"
      "0000000000"
      "0000000000")))

(defn create-game-with-ship-about-to-be-sunk
  [uuid]
  (game/game
    uuid
    [
     0 -3 0 0 2 2 2 2 0 0
     0 -3 0 0 0 0 0 0 0 0
     0  3 1 0 0 0 0 0 0 0
     0  0 1 0 0 0 5 0 0 0
     0  0 1 0 0 0 5 0 0 0
     0  0 1 0 0 4 4 4 0 0
     0  0 1 0 0 0 0 0 0 0
     0  0 0 0 0 0 0 0 0 0
     0  0 0 0 0 0 0 0 0 0
     0  0 0 0 0 0 0 0 0 0]
    -1))

(defn- hit-on
  [position game]
  (let [hit (get (:grid game) position 0)
        new-grid (assoc (:grid game) position (* -1 hit))]
    (game/game (:id game) new-grid (:next-shot game))))

(facts "about `game`"
  (let [game (create-new-game (uuid/uuid4))]
    (facts "about `hit`"
      (fact "it should return a water response if no ship has been hit or sunk"
        (game/hit "A" 1 game) => [:water game]
        (game/hit "C" 1 game) => [:water game]
        (game/hit "D" 1 game) => [:water game]
        (game/hit "A" 2 game) => [:water game]
        (game/hit "A" 3 game) => [:water game])

      (fact "it should return a hit response if a ship has been hit"
        (game/hit "B" 1 game) => [:hit (hit-on 1 game)]
        (game/hit "B" 2 game) => [:hit (hit-on 11 game)])))

  (let [game (create-game-with-ship-about-to-be-sunk (uuid/uuid4))]
    (fact "it should return a sunk response if a ship has been sunk"
      (game/hit "B" 3 game) => [:sunk (hit-on 21 game)])))
