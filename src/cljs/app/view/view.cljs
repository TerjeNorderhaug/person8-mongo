(ns app.view.view
  (:require-macros
   [kioo.reagent
    :refer [defsnippet deftemplate snippet]])
  (:require
   [kioo.reagent
    :refer [html-content content append after set-attr do->
            substitute listen unwrap]]
   [reagent.core :as reagent
    :refer [atom]]
   [app.view.patient.root :as patient.root]
   [app.view.provider.root :as provider.root]))

(defn split-view [session]
  [:div
    [:div {:style {:width "30%"
                   :float "left"}}
      [:div.phone
        [:div.phone-screen
          [patient.root/view session]]]]
    [:div {:style {:width "60%"
                   :height "100%" ;; should be screen height
                   :border-left "thin solid gray"
                   :float "right"}}
      [provider.root/view session]]])

(defn view [{:keys [mode] :as session}]
  (case (if mode @mode)
    ("patient")
    [patient.root/view session]
    ("provider")
    [provider.root/view session]
    ("split")
    [split-view session]
    (nil)
    [:div "Waiting"]))
