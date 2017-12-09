(ns app.session
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [cljs.core.async :as async
    :refer [<!]]
   [reagent.core :as reagent]))

(defonce event-queue (async/chan))

(defn dispatch [event]
  (async/put! event-queue event))

(defn dispatcher [dispatch-map]
  (go-loop []
    (when-let [event (<! event-queue)]
      (when-let [f (get dispatch-map (first event))]
        (apply f (rest event)))
      (recur))))

(defn state [initial]
  (->> initial
       (map #(vector (first %)(reagent/atom (second %))))
       (into {})))
