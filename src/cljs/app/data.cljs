(ns app.data
  (:require
   [camel-snake-kebab.core
    :refer [->kebab-case-keyword]]
   [camel-snake-kebab.extras
    :refer [transform-keys]]))

(defn transform-kebab-keys [m]
  (transform-keys ->kebab-case-keyword m))

(def state
  {:mode "split"
   :stage "diagnostic"
   :panes [[:id "about" :title "About"]
           {:id "demo" :title "Demo"}
           {:id "team" :title "Team"}
           {:id "tech" :title "Tech"}]
   :pane "demo"})
