(ns on-key-down.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :as async :refer (<! >! put! chan)]))

(defonce app-state (atom {:text "Hello Chestnut!"}))

(def event-bus (chan))

(defn receive-events []
  (go (loop []
        (let [e (<! event-bus)]
          (println "go-loop received" e)
          (println (e)))
        (recur))))

(defn main []
  (receive-events)  
  (om/root
   (fn [app owner]
     (reify
       om/IRender
       (render [_]
         (dom/section #js {:id "VRE" 
                           :tabIndex 0
                           :onKeyDown #(put! event-bus (fn [] (.preventDefault %)))}
                      ;; In the latest attempt, I tried to pack the
                      ;; function in a closure and pass it along.
                      ;; When it gets to the other side, the object it
                      ;; refers to (this) is no longer there
                      (dom/input nil)))))
   app-state
   {:target (. js/document (getElementById "app"))}))


