(ns dojo-hickory-reader.core
  (:require [clj-http.lite.client :as client]
            [hickory.core :as hickory]
            [hickory.select :as s]
            [incanter.charts :as charts]
            [incanter.core :as core]))

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

(defn elements [hickory-document]
  (->> hickory-document
       (tree-seq :content :content)
       (filter (complement string?))
       (filter #(= (:type %) :element))))

(defn tags [hickory-document]
  (map :tag (elements hickory-document)))

(defn count-elements [hickory-document]
  (count (elements hickory-document)))

(defn unique-elements [hickory-document]
  (into #{} (tags hickory-document)))

(defn count-element-types [hickory-document]
  (frequencies (tags hickory-document)))

(defn transpose [assoc-seq]
  (apply map vector assoc-seq))

(defn display-tag-frequencies
  [hickory-document]
  (core/view
   (let [freqs (count-element-types hickory-document)
         [tags counts] (transpose (reverse (sort-by second freqs)))]
     (charts/bar-chart tags counts))))
