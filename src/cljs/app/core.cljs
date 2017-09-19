(ns app.core
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [cljs.core.async :as async
    :refer [<! chan close! alts! timeout put!]]
   [goog.dom :as dom]
   [goog.events :as events]
   [goog.string :as gstring]
   [reagent.core :as reagent
    :refer [atom]]
   [api.jokes :as jokes]
   [app.view.page
    :refer [page html5]]
   [app.view.view
    :refer [view]]))

(def scripts [{:src "/js/out/app.js"}
              "main_cljs_fn()"])

(defn static-page []
  (go-loop [in (jokes/resource-chan)
            [val ch] (alts! [in (timeout 2000)])]
    (-> (if (identical? in ch) val (repeat 12 "No Joke!"))
        (page :scripts scripts :title "Jokes" :forkme true)
        (html5))))

(defn activate [dispatcher]
  (let [el (dom/getElement "canvas")
        in (jokes/resource-chan)
        content (atom nil)]
    (go-loop []
      (when-let [event (<! dispatcher)]
        (case (first event)
          :refresh (let [initialize (nil? @content)]
                     (reset! content (<! in))
                     (when initialize
                       (reagent/render [#(view @content)] el))))
        (recur)))))
