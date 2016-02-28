(ns battleship.routes.api
  (:require [liberator.core :refer [defresource resource]]
            [compojure.core :refer :all]
            [battleship.infrastructure.persistence.redis :as redis]
            [battleship.infrastructure.representation.dto :as dto]
            [battleship.domain-model.game :as game]
            [battleship.uuid :as uuid]))

(def create-game
  (comp
    (fn [dto] {:game dto})
    dto/game-to-dto
    redis/save-game!
    game/game))

(defresource create-game-resource []
  :available-media-types ["application/json"]
  :allowed-methods [:post]
  :handle-created :game
  :post! (fn [_]
           (create-game
             (uuid/uuid4)
             (str "0300222200"
                  "0300000000"
                  "0310000000"
                  "0010005000"
                  "0010005000"
                  "0010044400"
                  "0010000000"
                  "0000000000"
                  "0000000000"
                  "0000000000"))))

(def shot
  (comp
    (fn [dto] {:shot dto})
    dto/shot-to-dto
    game/shot
    redis/save-game!
    game/prepare-shot
    redis/of-id))

(defresource shot-resource [id]
  :available-media-types ["application/json"]
  :allowed-methods [:post]
  :handle-created :shot
  :post! (fn [_]
           (shot id)))

(defresource shot-result-resource [id result]
  :available-media-types ["application/json"]
  :allowed-methods [:post]
  :handle-created {:result 0})

(def receive-shot
  (comp
    (fn [dto] {:result dto})
    (fn [[result game]] (dto/hit-to-dto result))
    (fn [[result game]] [result (redis/save-game! game)])
    (partial apply game/hit)
    (fn [id letter number] (vector letter number (redis/of-id id)))))

(defresource receive-shot-resource [id letter number]
  :available-media-types ["application/json"]
  :allowed-methods [:post]
  :handle-created :result
  :post! (fn [_] (receive-shot id letter number)))

(defresource delete-game-resource [id]
  :available-media-types ["application/json"]
  :allowed-methods [:delete]
  :delete! (fn [_] (redis/finish-game! id)))

(defroutes api-routes
  (ANY "/battleship/game" [] (create-game-resource))
  (ANY "/battleship/game/:id/shot" [id] (shot-resource id))
  (ANY "/battleship/game/:id/shot-result/:result" [id result] (shot-result-resource id result))
  (ANY "/battleship/game/:id/receive-shot/:letter/:number" [id letter number]
    (receive-shot-resource id letter (read-string number)))
  (ANY "/battleship/game/:id" [id] (delete-game-resource id)))
