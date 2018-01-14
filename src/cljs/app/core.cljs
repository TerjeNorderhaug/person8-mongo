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
   [api.jokes :as jokes]
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
    (let [initial {:mode "patient"}
          state (session/state initial)]
      (-> state
          (page :scripts (scripts initial)
                :title "WellBE"
                :forkme false)
          (html5)))))

(defn activate [initial]
  (session/initialize initial)
  (let [el (dom/getElement "canvas")
        content (cljs.reader/read-string initial)
        state {:mode (reagent/atom "patient")
               :stage (reagent/atom "checkout")
               :patient (reagent/atom 5)}]
    (reagent/render [#(view state)] el)))
