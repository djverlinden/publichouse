(ns publichouse.profile.ar
  (:require [net.cgrand.enlive-html :as e]))

;;;; Transformation for Annual Reviews.org pages

;;; The AR documents are arranged in "boxes" with a title and text
(defn box->section
  [fragment]
  (let [title (e/text (first (e/select fragment [:div.yui3-hd])))]
    [title
     (conj (e/select fragment [:div.yui3-bd]) {:tag :h1 :content [title]})]))

(defn transform
  "Transform HTML from the Annual Reviews to profile data specification"
  [{html :content}]
  (let [title (e/text (first (e/select html [:h1.arttitle])))
        author (e/text (first (e/select html [:div.artAuthors :div.contrib])))]
    {:title title
     :author author 
     :sections (concat
                [["Title Page" [{:tag :h1 :content [title]}
                                {:tag :h2 :content [author]}]]]
                (map box->section (e/select html [:div.simpleBox]))
                [["References" (first (e/select html [:div#referencesTab :ul]))]])}))

