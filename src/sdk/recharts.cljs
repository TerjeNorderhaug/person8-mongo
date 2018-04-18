(ns sdk.recharts
  (:require
   #_ [cljsjs.recharts]
   [reagent.core :as reagent]
   [taoensso.timbre :as timbre]))

#_
[cljsjs/recharts "1.0.0-beta.10-3"
    :exclusions [cljsjs/react
                 cljsjs/react-dom]]

;;; ## cljsjs.recharts fails when loading on node per April 2018
;;; ## For now require cljsjs.recharts in app.start (only for browser)
;;; ## and avoid calls to recharts on server

(def recharts (if (exists? js/Recharts) js/Recharts))

(def adapt reagent/adapt-react-class)

(def ResponsiveContainer (if recharts (adapt recharts.ResponsiveContainer)))

;(def XAxis (reagent/adapt-react-class (aget js/Recharts "XAxis")))
;(def YAxis (reagent/adapt-react-class (aget js/Recharts "YAxis")))
;(def CartesianGrid (reagent/adapt-react-class (aget js/Recharts "CartesianGrid")))
;(def Tooltip (reagent/adapt-react-class (aget js/Recharts "Tooltip")))
;(def Legend (reagent/adapt-react-class (aget js/Recharts "Legend")))

;(def BarChart (reagent/adapt-react-class (aget js/Recharts "BarChart")))
;(def Bar (reagent/adapt-react-class (aget js/Recharts "Bar")))

(def PieChart (if recharts (adapt recharts.PieChart)))
(def Pie (if recharts (adapt recharts.Pie)))
(def Legend (if recharts (adapt recharts.Legend)))
(def Cell (if recharts (adapt recharts.Cell)))

(defn responsive-container [{:as attributes} & content]
  (if ResponsiveContainer
    (into [ResponsiveContainer attributes] content)
    [:div "ResponsiveContainer"]))
