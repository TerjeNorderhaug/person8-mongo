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

;; https://github.com/okta/okta-auth-js#api-reference

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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; SIGN IN widget
;; https://developer.okta.com/code/javascript/okta_sign-in_widget/

(def default-client-id "0oab4exampleR4Jbi0h7")
(def test1-client-id "0oaehotqz5N8l2Ake356")

#_
(def default-widget-config ; sign on works and returns user profile but general
   {:baseUrl "https://in-progress.okta.com"})

#_
(def default-widget-config ;; gets token but not profile
     {:baseUrl "https://in-progress.okta.com"
      :clientId test1-client-id
      :redirectUri "http://localhost:5000/implicit/callback"})

(def default-widget-config
       {:baseUrl "https://in-progress.okta.com"
        :clientId test1-client-id
        :redirectUri "http://localhost:5000/implicit/callback"
        :authParams  {;:issuer "default"
                      ; :scopes ["openid" "email" "profile" "address" "phone"]
                      ;:responseType ["id_token" "token"]
                      :display "page"}})

(defn show-widget [widget-container]
  {:pre [(string? widget-container)]}
  (timbre/info "Show okta widget")
  (let [sign-in (new js/OktaSignIn (clj->js default-widget-config))
        token (.-token sign-in)]
    (if (not (.hasTokensInUrl token))
      (.renderEl sign-in #js{:el widget-container}

               (fn [res]
                 (timbre/info "Signed on")
                 (timbre/debug "Okto ->" (js-keys res)(js->clj res))
                 (rf/dispatch [:mobile :user
                               (assoc (js->clj (.-user res) :keywordize-keys true)
                                      :status (or (.-status res) true))])
                 (let [token-manager (.-tokenManager sign-in)]
                   (.add token-manager "my_id_token" res))
                 (timbre/debug "Hide widget")
                 (.hide sign-in)
                 #_
                 (.setCookieAndRedirect (.-session res) "https://in-progress.okta.com/app/UserHome"))
               (fn [err]
                (timbre/error "Okto ->" (js->clj err))))
      (.parseTokensFromUrl token
                           (fn [res]
                             (timbre/debug "Okto:" (js-keys res)(js->clj res)))))))



(defn custom-sign-in []
  (if true
    (show-widget "#widget-container")
    (let [js-custom-sign-in (if (exists? js/customSignIn)
                              js/customSignIn)]
      (timbre/debug "Okta Custom Sign In" js-custom-sign-in)
      (if js-custom-sign-in
        (js-custom-sign-in)))))

(defstate do-signin
  :start (if-not (lib/node?)
           (custom-sign-in)))
