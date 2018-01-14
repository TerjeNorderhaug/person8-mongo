(ns app.session
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [cljs.core.async :as async
    :refer [<!]]
   [reagent.core :as reagent]
   [re-frame.core :as rf]))

(defonce event-queue (async/chan))

(defonce event-mult (async/mult event-queue))

(defn dispatch [event]
  (async/put! event-queue event))

(defn dispatcher [dispatch-map]
  (let [in (async/tap event-mult (async/chan))]
    (go-loop []
      (when-let [event (<! in)]
        (when-let [f (get dispatch-map (first event))]
          (apply f (rest event)))
        (recur)))))

(defn state [initial]
  (->> initial
       (map #(vector (first %)(reagent/atom (second %))))
       (into {})))

#_
(defn reg-event-handler [k f]
  (let [in (->> (async/chan 1 (filter #(= k (first %))))
                (async/tap event-mult))]
    (go-loop []
      (when-let [event (<! in)]
        (apply f (rest event))
        (recur)))))

(defn initialize [initial]

  (rf/reg-event-db
   :initialize
   (fn [db _] initial))

  (rf/reg-event-db
   :patient
   (fn [db [_ id stage]]
     {:pre [(string? stage)]}
     (assoc db :mode "patient" :patient id :stage stage)))

  (rf/reg-sub
   :mode
   (fn [db]
     (:mode db)))

  (rf/reg-sub
   :stage
   (fn [db]
     (:stage db)))

  (rf/reg-event-db ;; should be fx
   :pay
   (fn [db [_ amount]]
     (assoc db :stage "payed")))

  (rf/dispatch [:initialize]))
