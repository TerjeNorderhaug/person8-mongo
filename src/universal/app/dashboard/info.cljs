(ns app.dashboard.info
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
   [util.lib :as lib
    :refer [pp-element pp-str]]
   [app.dashboard.pane
    :refer [pane]]))

(defn view [session]
  [:div
    [:code {:style {:overflow-wrap :break-all
                    :max-width "100%"}}
      (pp-str session)]])
