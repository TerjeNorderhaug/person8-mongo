(ns app.session
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [cljs.core.async :as async
    :refer [<!]]
   [reagent.core :as reagent]
   [re-frame.core :as rf]
   [cljs-http.client :as http]))

(defonce event-queue (async/chan))

(defonce event-mult (async/mult event-queue))

(defn dispatch [event]
  (async/put! event-queue event))

(defn state [initial]
  (->> initial
       (map #(vector (first %)(reagent/atom (second %))))
       (into {})))

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

  (rf/reg-sub
   :itinerary
   (fn [db]
     (:itinerary db)))

  (rf/reg-event-db ;; should be fx
   :pay
   (fn [db [_ amount]]
     (http/post "/api/exonum/pay"
                {:json-params {:from nil
                               :to nil
                               :amount 16}})
     (assoc db :stage "payed")))

  (rf/dispatch-sync [:initialize]))
