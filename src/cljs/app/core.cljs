(ns app.core
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [cljs.core.async :as async
    :refer [<! chan close! alts! timeout put!]]
   [goog.dom :as dom]
   [reagent.core :as reagent
    :refer [atom]]
   [re-frame.core :as rf]
   [app.session :as session]
   [app.view.page
    :refer [page html5]]
   [app.view.view
    :refer [view]]))

(defn scripts [initial]
  [{:src "/js/out/app.js"}
   (str "main_cljs_fn("
        (pr-str (pr-str initial))
        ")")])

(def default-state
  {:mode "split"
   :stage "checkout"
   :patient 5
   :itinerary {:items [{:label "1"
                        :description "Examination and consultation"
                        :cost "5"}
                       {:label "2"
                        :description "Prescription (Prednisone 5mg)"
                        :cost "8"}]}})

(defn static-page []
  (go-loop []
    (let [initial default-state
          state (session/state initial)]
      (-> state
          (page :scripts (scripts initial)
                :title "WellBE"
                :forkme false)
          (html5)))))

(defn activate [initial]
  #_
  (-> (cljs.reader/read-string initial)
      (session/initialize))
  (session/initialize default-state)
  (let [el (dom/getElement "canvas")
        state {:mode (rf/subscribe [:mode])
               :stage (rf/subscribe [:stage])
               :itinerary (rf/subscribe [:itinerary])}]
    (reagent/render [#(view state)] el)))
