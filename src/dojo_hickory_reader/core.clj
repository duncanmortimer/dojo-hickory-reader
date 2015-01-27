(ns dojo-hickory-reader.core
  (:require [clj-http.lite.client :as client]
            [hickory.core :as hickory]))

(defn read-url [url]
  (let [response (client/get url)]
    (when (= (response :status) 200)
      (response :body))))
