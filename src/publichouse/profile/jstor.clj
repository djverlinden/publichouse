(ns publichouse.profile.jstor
  (:require [net.cgrand.enlive-html :as e]))

;;;; Transformation for jstor.org documents
(defn transform
  "Transform HTML from the Annual Reviews to profile data specification"
  [{html :content}]
  (let [title (e/text (first (e/select html [:h2#articleTitle])))
        author (apply str (interpose " and " (e/texts (e/select html [:span.articleBody_author]))))]
    {:title title
     :author author 
     :sections (vector
                ["Title Page" [{:tag :h1 :content [title]}
                               {:tag :h2 :content [author]}
                               {:tag :h3 :content ["Abstract"]}
                               {:tag :div :content 
                                (doall (e/select html [:div.articleBody_abstract]))}]]
                 ["Article" (e/at
                             (e/select html [(e/lefts [:div.articleBody_abstract])])
                             [:a.jqShowLink] nil)])}))
