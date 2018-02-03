(ns app.session
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [cljs.core.async :as async
    :refer [<!]]
   [reagent.core :as reagent]
   [re-frame.core :as rf
    :refer [reg-sub]]
   [lib.rflib :as rflib
    :refer [reg-property]]
   #_[re-frame.http-fx]
   [taoensso.timbre :as timbre]
   [cljs-http.client :as http]))

(def interceptors [#_(when ^boolean js/goog.DEBUG debug)
                   rf/trim-v])

(defn state [initial]
  (->> initial
       (map #(vector (first %)(reagent/atom (second %))))
       (into {})))

(defn subscriptions [ks]
  (into {} (map #(vector % (rf/subscribe [%])) ks)))

(defn initialize [initial]

  (rf/reg-event-db
   :initialize
   (fn [db _] initial))

  (rf/reg-event-db
   :update
   (fn [db [_ path f]]
     (update-in db path f)))

  (rf/reg-event-db
   :assign
   (fn [db [_ path value]]
     (timbre/debug "Assign:" path value)
     (assoc-in db path value)))

  (rf/reg-event-db
   :patient
   (fn [db [_ id stage]]
     {:pre [(string? stage)]}
     (assoc db
            :patient id
            :stage stage)))

  (reg-property :mode)
  (reg-property :stage)
  (reg-property :pane)

  (rf/reg-sub :panes :panes)

  (rf/reg-event-db ;; should be fx
                   :pay
                   (fn [db [_ amount]]
                     (http/post "/api/exonum/pay"
                                {:json-params {:from nil
                                               :to nil
                                               :amount 16}})
                     (assoc db :stage "payed")))

  (rf/reg-event-db
    :diagnostic/analyze ;; should be fx
    (fn [db [_ desc]]
      (timbre/debug "Analyze:" desc)
         ;; FIX: use fx
      (go-loop [result (<! (http/get "/api/infermedica/analysis"
                                    {:query-params {"desc" (str desc)}}))]
        (timbre/debug "Analysis =>" result)
        (when (:success result)
          (rf/dispatch [:analysis (:body result)])))
      (assoc db :description desc)))

  (rf/reg-event-db
   :diagnostic/diagnose ;; should be fx
   (fn [db [_ arg]]
     (timbre/debug "Diagnose:" arg)
     ;; FIX: use fx
     (go-loop [defaults {:sex "male" :age "30"}
               result (<! (http/post "/api/infermedica/diagnosis"
                                      {:json-params (merge defaults arg)}))]
       (timbre/debug "Diagnose =>" result)
       (when (:success result)
         (rf/dispatch [:diagnosis (:body result)])))
     db))

  (rf/dispatch-sync [:initialize]))
