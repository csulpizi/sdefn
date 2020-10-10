(ns cawfn.examples.example
  (:require [clojure.string :refer [split]]
            [cawfn.core :refer [cawfn]])
  (:import java.util.Date))

(def order-count (atom 0))

(cawfn order-record
       "Print order details and return an order record object"
       [& {:required-keys [customer item] :optional-keys [comments qty] :or {qty 1}}]
       (let [date (str (Date.))]
         (println customer "ordered" qty "of" item "on" date)
         (swap! order-count inc)
         {:order-number @order-count
          :name customer
          :item item
          :qty qty
          :comments comments
          :date date}))

(defn order-bear
  "This successfully compiles."
  [customer]
  (order-record :customer customer :item :bear :comments "Extra soft, please"))

(println (order-bear "Alice"))
;;==> Alice ordered 1 of :bear on Sat Jun 06 16:09:10 EDT 2020
;;==> {:order-number 1, :name Alice, :item :bear, :qty 1, :comments Extra soft, please, :date Sat Jun 06 16:09:33 EDT 2020}

(defn order-bananas
  "This successfully compiles."
  [customer qty]
  (order-record :customer customer :item :banana :qty qty))

(println (order-bananas "Bob" 4))
;;==> Bob ordered 4 of :banana on Sat Jun 06 16:09:33 EDT 2020
;;==> {:order-number 2, :name Bob, :item :banana, :qty 4, :comments nil, :date Sat Jun 06 16:09:33 EDT 2020}

(defn order-nothing
  "This fails to compile because it doesn't specify an item."
  [customer]
  (order-record :customer customer))

;;==> Syntax error macroexpanding order-record at (example.clj:42:3).
;;==> Missing parameters: item. You must provide all required parameters.

(defn order-balloons
  "This fails to compile because it doesn't know what qyt means."
  [customer item qty]
  (order-record :customer customer :item :balloons :qyt qty))

;;==> Syntax error macroexpanding order-record at (example.clj:50:5).
;;==> Unknown keys: qyt. These parameters are not defined in the called function. Check that this parameter is spelled right, or add the parameter to the called function.
