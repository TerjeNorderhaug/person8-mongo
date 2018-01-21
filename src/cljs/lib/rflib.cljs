(ns lib.rflib
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [cljs.core.async :as async
    :refer [<! chan close! alts! timeout put!]]
   [re-frame.core :as rf]))

(defn dispatch-message
  ([ch]
   (go
    (when-let [msg (<! ch)]
      (rf/dispatch msg))))
  ([k ch]
   (go
    (when-let [msg (<! ch)]
      (rf/dispatch [k msg])))))