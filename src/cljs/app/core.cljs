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
   [taoensso.timbre :as timbre]
   [lib.rflib :as rflib]
   [app.data :as data]
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

(defn static-page []
  (go-loop []
    (let [initial data/state
          state (session/state initial)]
      (-> state
          (page :scripts (scripts initial)
                :title "HackBench"
                :forkme false)
          (html5)))))

(defn activate [initial]
  #_
  (-> (cljs.reader/read-string initial)
      (session/initialize))
  (session/initialize data/state)
  (let [el (dom/getElement "canvas")
        state (session/subscriptions
               (map first data/state))]
    (reagent/render [#(view state)] el)))
