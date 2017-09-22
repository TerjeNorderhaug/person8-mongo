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
  (go-loop [in (jokes/resource-chan)
            [val ch] (alts! [in (timeout 2000)])]
    (-> (if (identical? in ch) val (repeat 12 "No Joke!"))
        (page :scripts (scripts val) :title "Jokes" :forkme true)
        (html5))))

(defn activate [initial]
  (let [el (dom/getElement "canvas")
        content (atom (cljs.reader/read-string initial))]
    (reagent/render [#(view @content)] el)
    (let [in (jokes/resource-chan)]
      (session/dispatcher
       {:refresh #(go (reset! content (<! in)))}))))
