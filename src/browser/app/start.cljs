(ns app.start
  (:require
   [app.core :as app]
   [app.router :as router]))

(enable-console-print!)

(defn ^:export main [initial]
  (app/activate (cljs.reader/read-string initial))
  (router/hook-browser-navigation!))

(set! js/main-cljs-fn main)
