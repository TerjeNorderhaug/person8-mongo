(ns app.mobile.pane
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

(defmulti pane (fn [{:keys [stage] :as session}]
                  (if stage [@stage])))

(defmethod pane :default [{:keys [] :as session}]
  [ui/card
   [ui/card-text
    [:div "Hello"]]])