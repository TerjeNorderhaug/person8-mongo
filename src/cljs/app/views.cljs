(ns app.views
  (:require-macros
   [kioo.reagent
    :refer [defsnippet deftemplate snippet]])
  (:require
   [kioo.reagent
    :refer [html-content content append after set-attr do->
            substitute listen unwrap]]
   [kioo.core
    :refer [handle-wrapper]]
   [reagent.core :as reagent
    :refer [atom]]
   [goog.string :as gstring]))

;; Showcasing combining different ways to generate the markup for the page:
;; 1. Hiccup-style templating with elements as inline clojure vectors;
;; 2. Kioo (enlive style) injecting transformations into external template;
;;    The template file is in the resources directory.

(defn view-1 [data]
  [:div.row
   (for [entity data]
     ^{:key (gstring/hashCode entity)}
     [:div.card.col-xs-12.col-sm-6.col-md-4.col-lg-3
      [:div.well
       [:div.entity entity]]])])

(defsnippet card "template.html" [:main :.card]
  [entity]
  {[:.entity] (content entity) })

(defsnippet view-2 "template.html" [:main :.row]
  [data]
  {[:.card] (substitute (map card data)) })

(def view-option {:hiccup view-1 :kioo view-2})

(def view (view-option :hiccup)) ;; Use either of the view-* templates from above

;; Template for the html page:

(defsnippet page "template.html" [:html]
  [data & {:keys [scripts title forkme]}]
  {[:head :title] (if title (content title) identity)
   [:main] (content [view data])
   [:#forkme] (if forkme identity (content nil))
   [:body] (append [:div (for [src scripts]
                           ^{:key (gstring/hashCode (pr-str src))}
                           [:script src])]) })

(defn html5 [data]
  (str "<!DOCTYPE html>\n" data))

(defn test-views []
  (html5 (page ["Chuck Norris eats parentheses for breakfast"])))
