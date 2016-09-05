(ns app.core
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [cljs.core.async :as async :refer [chan close! alts! timeout put!]]
   [goog.dom :as dom]
   [goog.events :as events]
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom.server :refer [render-to-string]]
   [app.bridge :as bridge]
   [app.views :refer [view page html5]]))

(def scripts [{:src "/js/out/app.js"}
              "main_cljs_fn()"])

(def endpoint {:url "http://api.icndb.com/jokes/random"
               :extract #(get-in % ["value" "joke"]) })

(def jokes-chan
  (memoize #(bridge/open-resource endpoint 12 2)))

(defn static-page []
  (let [out (chan 1)
        in (jokes-chan)]
    (go
      (let [[val ch] (alts! [in (timeout 2000)])]
        (put! out
              (-> (if (identical? in ch) val ["No Joke!"])
                  (page :scripts scripts)
                  (render-to-string)
                  (html5)))))
    out))

(defn activate []
  (let [el (dom/getElement "jokes")
        joke-num 12
        buf-size 3
        jokes-buf (bridge/open-resource endpoint joke-num buf-size :concur (* joke-num buf-size))
        jokes (atom nil)
        user-action (chan)]
    (events/listen el events/EventType.CLICK
                   (partial put! user-action))
    (go-loop [initialized false]
      (when (some? (<! user-action))
        (reset! jokes (<! jokes-buf))
        (when-not initialized
          (reagent/render [#(view @jokes)] el))
        (recur true)))))
