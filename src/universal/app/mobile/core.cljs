(ns app.mobile.core
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
   [app.mobile.toolbar
    :refer [toolbar]]
   [app.mobile.pane
    :refer [pane]]))

(defmethod pane ["intro"] [session]
  [:div "Intro"])

(defn view [{:keys [stage] :as session}]
  [ui/mui-theme-provider
   {:mui-theme (get-mui-theme
                {:font-size 16
                 :palette
                 {:primary1-color (color :blue-grey900)
                  :primary2-color (color :indigo700)
                  :primary3-color (color :indigo200)
                  :alternate-text-color (color :white) ;; used for appbar text
                  :primary-text-color (color :light-black)}})}
   [:div
    [toolbar session]
    [pane session]]])
