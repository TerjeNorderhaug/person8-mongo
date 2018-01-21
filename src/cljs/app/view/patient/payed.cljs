(ns app.view.patient.payed
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

(defn view [session]
  [:div.alert.alert-success
   {:role "alert"}
   "Thank you for paying for the visit with WELL tokens"])

(defmethod pane "payed" [session]
  [view session])
