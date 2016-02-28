(ns battleship.uuid)

(defn uuid4
  []
  (str (java.util.UUID/randomUUID)))
