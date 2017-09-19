(ns app.view.page
  (:require-macros
   [kioo.reagent
    :refer [defsnippet deftemplate snippet]])
  (:require
   [kioo.reagent
    :refer [html-content content append after set-attr do->
            substitute listen unwrap]]
   [reagent.core :as reagent
    :refer [atom]]
   [reagent.dom.server
    :refer [render-to-string]]
   [goog.string :as gstring]
   [app.view.view
    :refer [view]]))

(defsnippet page "template.html" [:html]
  [data & {:keys [scripts title forkme]}]
  {[:head :title] (if title (content title) identity)
   [:nav :.navbar-brand] (if title (content title) identity)
   [:main] (content [view data])
   [:.refresh-activator] (set-attr :href "#refresh")
   [:#forkme] (if forkme identity (content nil))
   [:body] (append [:div (for [src scripts]
                           ^{:key (gstring/hashCode (pr-str src))}
                           [:script src])])})

(defn html5 [content]
  (->> (render-to-string content)
       (str "<!DOCTYPE html>\n")))

(defn test-views []
  (html5 (page ["Chuck Norris eats parentheses for breakfast"])))
