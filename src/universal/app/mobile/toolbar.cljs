(ns app.mobile.toolbar
  (:require
   [goog.string :as gstring]
   [reagent.core :as reagent
     :refer [atom]]
   [re-frame.core :as rf]
   [taoensso.timbre :as timbre]
   [cljsjs.material-ui]
   [cljs-react-material-ui.core :as material
     :refer [get-mui-theme color]]
   [cljs-react-material-ui.reagent :as ui]
   [cljs-react-material-ui.icons :as ic]))

(defn toolbar [{:as session}]
  (let [open (atom false)]
    (fn [{:keys [brand tab] :as session}]
      [ui/app-bar
       {:on-left-icon-button-touch-tap #(reset! open true)
        :on-right-icon-button-touch-tap #()
        :title (reagent/as-element
                [:div (if brand @brand)])}
       (into [ui/drawer {:open @open
                         :onClose #(reset! open false)}]
             (for [{:keys [title id] :as item} (:options @tab)]
               ^{:key id}
               [ui/menu-item
                {:on-click #(do
                              (reset! open false)
                              (rf/dispatch [:change-tab id]))}
                title]))])))
