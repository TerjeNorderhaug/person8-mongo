(ns app.view.patient.selector
  (:require
   [reagent.core :as reagent
     :refer [atom]]
   [re-frame.core :as rf]
   [cljsjs.material-ui]
   [cljs-react-material-ui.core :as material
     :refer [get-mui-theme color]]
   [cljs-react-material-ui.reagent :as ui]
   [cljs-react-material-ui.icons :as ic]
   [goog.string :as gstring]
   [app.view.patient.pane
    :refer [pane]]))

(defn view [{:keys [providers] :as session}]
  [:div
   (into
    [:div]
    (for [{:keys [name location avatar
                  user-id partner-id provider-id
                  textprofile clinicname
                  language education
                  experience residency cost]
           :as provider}
          (if providers (:providers @providers))]
      [ui/card
       [ui/card-header
        {:title name
         :subtitle clinicname
         :avatar (str "https://well-api.joinwell.com/"
                      avatar)}]
       [ui/card-text
        [ui/list
         [ui/list-item {:primary-text location
                        :left-icon (ic/maps-place)}]
         [ui/list-item {:primary-text language
                        :left-icon (ic/action-language)}]
         [ui/list-item {:primary-text education
                        :left-icon (ic/social-school)}]
         #_
         [ui/list-item {:primary-text experience
                        :left-icon (ic/action-work)}]
         [ui/list-item {:primary-text residency
                        :left-icon (ic/maps-local-hospital)}]
         [ui/list-item {:primary-text cost
                        :left-icon (ic/action-payment)}]]]
       [ui/card-actions
        [ui/raised-button {:label "Check In"
                           :icon (ic/action-book)
                           :primary true
                           :href (str "/#patient/"
                                      provider-id
                                      "/visit")}]]
       [ui/card-text textprofile]]))])

(defmethod pane "schedule" [session]
  [view session])
