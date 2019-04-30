(ns util.request
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [cljs.core.async
    :refer [<!]]
   [util.lib :as lib]
   [taoensso.timbre :as timbre]
   [re-frame.core :as rf]))

;; dispatch to remote procedure calls on the server
;; for easy integration of server-side sdks.
;; For now depending on pubnub

(rf/reg-fx
 :request
 (fn [arg]
   (timbre/debug "Request fx:" arg)
   (if (lib/node?)
     (timbre/warn "Ignored request:" arg)
     (rf/dispatch [:pubnub/publish {:channel "demo"
                                    :message {:request arg}}]))))

(rf/reg-event-fx
 :request
 (fn [{:keys [db] :as cofx} [_ & [arg]]]
   (timbre/debug "Request event:" arg)
   {:request arg}))
