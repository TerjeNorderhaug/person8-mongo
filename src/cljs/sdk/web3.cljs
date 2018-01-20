(ns sdk.web3
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [taoensso.timbre :as timbre]
   [cljsjs.web3]
   [cljs-web3.bzz :as web3-bzz]
   [cljs-web3.core :as web3]
   [cljs-web3.db :as web3-db]
   [cljs-web3.eth :as web3-eth]
   [cljs-web3.evm :as web3-evm]
   [cljs-web3.net :as web3-net]
   [cljs-web3.personal :as web3-personal]
   [cljs-web3.settings :as web3-settings]
   [cljs-web3.shh :as web3-shh]
   [cljs-web3.async.eth :as web3-eth-async]))

(def web3 (web3/create-web3 "http://localhost:8545"))

#_
(web3-eth/accounts web3 #())

(list 1)
