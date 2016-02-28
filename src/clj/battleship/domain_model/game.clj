(ns battleship.domain-model.game
  (:require [clojure.string :as str]
            [clojure.math.numeric-tower :as math]
            [clojure.core.match :refer [match]]))

(defrecord Game [id grid next-shot])

(defrecord Shot [letter number])

(def ^{:private true} ships {:1 -4 :2 -8 :3 -9 :4 -12 :5 -10})

(defn- parse-grid
  [grid]
  (vec
    (map
      (comp read-string str)
      (char-array grid))))

(defn game
  ([id grid]
    (->Game id (parse-grid grid) -1))
  ([id grid next-shot]
    (->Game id grid next-shot)))

(defn prepare-shot
  [game]
  (->Game (:id game) (:grid game) (inc (:next-shot game))))

(defn shot
  [game]
  (let [next-shot (:next-shot game)
        letter (* (/ next-shot 10) 10)
        number (mod next-shot 10)]
    (->Shot (str (char (+ 65 letter))) (inc number))))

(defn- to-position
  [letter number game]
  (let [x (- (int (first (char-array (str/upper-case letter)))) 65)
        y (* (dec number) 10)]
    [(+ x y) game]))

(defn- hit-on
  [position ship game-record]
  (let [new-grid (assoc (:grid game-record) position (* -1 ship))]
    (game (:id game-record) new-grid (:next-shot game-record))))

(defn- sum-grid-values-of
  [ship grid]
  (let [reducer (fn [acc value]
                  (cond
                    (= ship (math/abs value)) (+ acc value)
                    :else acc))]
    (reduce reducer 0 grid)))

(defn- replace-hit-for-sunk-if-ship-has-been-sunk
  [[ship game]]
  (match [ship game]
    [:water _] [:water game]
    :else (let [sum (sum-grid-values-of ship (:grid game))
                result (if (= sum (get ships (keyword (str ship)))) :sunk :hit)]
            [result game])))

(defn- receive-shot
  [[position game]]
  (let [grid (:grid game)
        ship (math/abs (get grid position 0))]
    (cond
      (not (zero? ship)) [ship (hit-on position ship game)]
      :else [:water game])))

(def hit
  (comp
    replace-hit-for-sunk-if-ship-has-been-sunk
    receive-shot
    to-position))