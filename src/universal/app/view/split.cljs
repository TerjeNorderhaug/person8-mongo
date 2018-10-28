(ns app.view.split
  (:require-macros
   [kioo.reagent
    :refer [defsnippet deftemplate snippet]])
  (:require
   [kioo.reagent
    :refer [html-content content append after set-attr do->
            substitute listen unwrap]]
   [reagent.core :as reagent
    :refer [atom]]
   [app.mobile.core :as mobile]
   [app.dashboard.core :as dashboard]))

(defn view [session]
  [:div.row
    [:div.col-auto
      [:div.phone
        [:div.phone-screen
         (case :iframe
           :iframe
           [:iframe
            {:style {:width "100%"
                     :height "100%"
                     :border "none"}
             :src "/mobile"}
            "Not Supported"]
           :inline
           [mobile/view session])]]]
    [:div.col
     {:style {:height "100vh"
              :border-left "thin solid gray"}}
     [dashboard/view session]]])
