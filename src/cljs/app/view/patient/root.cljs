(ns app.view.patient.root
  (:require
   [goog.string :as gstring]
   [reagent.core :as reagent
     :refer [atom]]
   [re-frame.core :as rf]
   [cljsjs.material-ui]
   [cljs-react-material-ui.core :as material
     :refer [get-mui-theme color]]
   [cljs-react-material-ui.reagent :as ui]
   [cljs-react-material-ui.icons :as ic]
   [app.view.patient.pane
    :refer [pane]]
   [app.view.patient.diagnostic :as diagnostic]
   [app.view.patient.selector :as selector]
   [app.view.patient.visit :as visit]
   [app.view.patient.checkout :as checkout]
   [app.view.patient.payed :as payed]))

(defn toolbar [session]
  [ui/app-bar
   {:on-left-icon-button-touch-tap #()
    :on-right-icon-button-touch-tap #()
    :title (reagent/as-element
            [:div "WellBE"])}])

(defn view [{:keys [stage] :as session}]
  [ui/mui-theme-provider
   {:mui-theme (get-mui-theme
                {:palette
                 {:primary1-color "#9DCFE1"
                  :primary2-color (color :deep-blue700)
                  :primary3-color (color :deep-blue200)
                  :alternate-text-color (color :white) ;; used for appbar text
                  :primary-text-color (color :light-black)}})}
   [:div
    [toolbar session]
    [pane session]]])
