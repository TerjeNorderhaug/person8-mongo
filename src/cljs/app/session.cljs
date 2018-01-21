(ns app.session
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [cljs.core.async :as async
    :refer [<!]]
   [reagent.core :as reagent]
   [re-frame.core :as rf]
   [taoensso.timbre :as timbre]
   [cljs-http.client :as http]))

(defonce event-queue (async/chan))

(defonce event-mult (async/mult event-queue))

(defn dispatch [event]
  (async/put! event-queue event))

(defn state [initial]
  (->> initial
       (map #(vector (first %)(reagent/atom (second %))))
       (into {})))

(defn subscriptions [ks]
  (into {} (map #(vector % (rf/subscribe [%])) ks)))

(defn reg-property [name]
  (rf/reg-event-db name
   (fn [db [_ value]]
     (assoc db name value)))
  (rf/reg-sub name
   (fn [db]
     (get db name))))

(defn initialize [initial]

  (rf/reg-event-db
   :initialize
   (fn [db _] initial))

  (rf/reg-event-db
   :update
   (fn [db [_ path f]]
     (update-in db path f)))

  (rf/reg-event-db
   :patient
   (fn [db [_ id stage]]
     {:pre [(string? stage)]}
     (assoc db
            :patient id
            :stage stage)))

  (reg-property :providers)
  (reg-property :waiting)
  (reg-property :mode)
  (reg-property :stage)
  (reg-property :pane)

  (rf/reg-sub
   :itinerary
   (fn [db]
     (:itinerary db)))

  (rf/reg-sub :patient :patient)
  (rf/reg-sub :panes :panes)

  (rf/reg-event-db ;; should be fx
                   :pay
                   (fn [db [_ amount]]
                     (http/post "/api/exonum/pay"
                                {:json-params {:from nil
                                               :to nil
                                               :amount 16}})
                     (assoc db :stage "payed")))

  (rf/dispatch-sync [:initialize]))
