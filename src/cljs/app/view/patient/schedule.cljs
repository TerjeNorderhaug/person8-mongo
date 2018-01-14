(ns app.view.patient.schedule
  (:require
   [reagent.core :as reagent
     :refer [atom]]
   [re-frame.core :as rf]
   [cljsjs.material-ui]
   [cljs-react-material-ui.core :as material
     :refer [get-mui-theme color]]
   [cljs-react-material-ui.reagent :as ui]
   [cljs-react-material-ui.icons :as ic]
   [goog.string :as gstring]))

(defn panel [data]
  (let [time (reagent/atom nil)
        date "Thursday, October 15, 2017"]
   (fn [data]
    [ui/paper {:style {:padding-left "1em"
                       :padding-right "1em"}}
     [:h3
      [ui/font-icon {:class-name "material-icons"
                     :style {:font-size "40"}}
       [ic/action-perm-contact-calendar {:color (color :grey600)
                                         :font-size "8em"}]
       [:span {:style {:margin-left "0.5em"}}
        "Schedule your appointment"]]]
     [ui/paper {:style {:padding "0.5em"}}
      [:h3 "Available spots:"]
      [:h4 "Thursday, October 15, 2017"]
      [:div {:style {:margin-top "1em"}}
       [:button.btn.btn-primary
        {:style {:margin-left "0%" :width "100%"}
         :on-click #(rf/dispatch
                     [:schedule (str @time " on " date)])}
        "Schedule Your Appointment"]]]])))

(defn view [session]
  [ui/mui-theme-provider
   {:mui-theme (get-mui-theme
                {:palette
                 {:primary1-color "#661775"
                  :primary2-color (color :deep-purple700)
                  :primary3-color (color :deep-purple200)
                  :alternate-text-color (color :white) ;; used for appbar text
                  :primary-text-color (color :light-black)}})}
   [ui/paper {:style {:height "auto"}}
    [panel session]]])
