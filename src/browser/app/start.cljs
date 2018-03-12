(ns app.start
  (:require
   [app.core :as app]
   [app.routes :as routes]))

(enable-console-print!)

(defn ^:export main [initial]
  (app/activate (cljs.reader/read-string initial))
  (routes/hook-browser-navigation!))

(set! js/main-cljs-fn main)
