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
   [goog.string :as gstring]))

;; Showcasing combining different ways to generate the markup for the page:
;; 1. Hiccup-style templating with elements as inline clojure vectors;
;; 2. Kioo (enlive style) injecting transformations into external template;
;;    The template file is in the resources directory.

(defn hiccup-view [data]
  [:div.row
   (for [[ix entity] (map-indexed vector data)]
     ^{:key (gstring/hashCode (str entity ix))}
     [:div.card.col-xs-12.col-sm-6.col-md-4.col-lg-3
      [:div.well
       [:div.entity entity]]])])

(defsnippet card "template.html" [:main :.card]
  [entity]
  {[:.entity] (content entity)})

(defsnippet kioo-view "template.html" [:main :.row]
  [data]
  {[:.card] (substitute (map card data))})

(def view kioo-view) ;; Use either kioo-view or hiccup-view
