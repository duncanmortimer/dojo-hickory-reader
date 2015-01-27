(ns dojo-hickory-reader.core
  (:require [clj-http.lite.client :as client]
            [hickory.core :as hickory]
            [hickory.select :as s]))

(defn read-url [url]
  (let [response (client/get url)]
    (when (= (response :status) 200)
      (response :body))))

(defn parse-to-hickory [url]
  (-> url
      read-url
      hickory/parse
      hickory/as-hickory))

(defn select-head [hickory-document]
  (s/select (s/tag :head) hickory-document))

(defn select-body [hickory-document]
  (s/select (s/tag :body) hickory-document))



(defn count-elements [hickory-document]
  (->> hickory-document
       (tree-seq :content :content)
       (filter #(= (% :type) :element))
       count))










