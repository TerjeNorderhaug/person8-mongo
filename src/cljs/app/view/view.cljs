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
   [goog.string :as gstring]))

(defn view [{:keys [mode] :as session}]
  (case (if mode @mode)
    ("patient")
    [patient.root/view session]
    ("provider" nil)
    [:div "Provider"]))
