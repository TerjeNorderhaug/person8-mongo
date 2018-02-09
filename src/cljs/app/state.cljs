(ns app.state
  (:require
   [camel-snake-kebab.core
    :refer [->kebab-case-keyword]]
   [camel-snake-kebab.extras
    :refer [transform-keys]]))

(defn transform-kebab-keys [m]
  (transform-keys ->kebab-case-keyword m))

(def state
  {:brand "HackBench"
   :modes [{:id "split" :title "Split"}
           {:id "mobile" :title "Mobile"}
           {:id "dashboard" :title "Dashboard"}]
   :mode (case :default
           :split "split"
           nil)
   :stage nil
   :mobile {:stage nil}
   :panes [{:id "about" :title "About"}
           {:id "main" :title "Main"}
           {:id "info" :title "Info"}]
   :pane "info"})
