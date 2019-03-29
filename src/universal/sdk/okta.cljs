(ns sdk.okta
  (:require
   [taoensso.timbre :as timbre]
   [re-frame.core :as rf]
   [mount.core :as mount
    :refer [defstate]]
   [util.lib :as lib]
   [util.env :as env
    :refer [env]]))

(def auth-js "https://ok1static.oktacdn.com/assets/js/sdk/okta-auth-js/2.0.1/okta-auth-js.min.js")

;; https://developer.okta.com/code/javascript/okta_sign-in_widget/

(def client-credentials
  {:url "https://in-progress.okta.com"
   :clientId "0oab4exampleR4Jbi0h7"
   :redirectUri "http://localhost:3333"})

(defn get-with-redirect [client]
  (timbre/debug "Okta get with redirect")
  (.getWithRedirect (.token client
                        #js{:responseType "id_token"})))


(defn store-id [client id]
  (timbre/debug "okta store id" id))

(defn parse-from-url [client token]
  (.then (.parseFromUrl token)
         #(store-id client %1)))


(defn login [client token]
  (timbre/debug "Okta login" token)
  (cond
    token (timbre/info "Okta idToken=" token)
    js/location.hash (do (timbre/warn "Okta location.hash" js/location.hash)
                       (parse-from-url client token))
    true (get-with-redirect client)))


(defn new-client []
  (timbre/debug "New Okta Client")
  (let [client (new js/OktaAuth (clj->js client-credentials))
        token (.get (.-tokenManager client) "idToken")]
    (.then token
           #(login client %1))
    client))

#_
(defstate auth-client
  :start (new-client))

(defn custom-sign-in []
  (let [js-custom-sign-in (if (exists? js/customSignIn)
                            js/customSignIn)]
    (timbre/debug "Okta Custom Sign In" js-custom-sign-in)
    (if js-custom-sign-in
      (js-custom-sign-in))))

(defstate do-signin
  :start (if-not (lib/node?)
           (custom-sign-in)))
