(ns lib.env)

(defn env [k]
  (aget js/process "env" (str k)))
