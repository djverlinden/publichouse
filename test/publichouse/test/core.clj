(ns publichouse.test.core
  (:use clojure.test
        publichouse.core))

; requires you run test/examples/fetch-examples.sh
; it might be smart to make this script into a Cake task or function

(def besley-ryenal-querol
  "test/examples/besley-ryenal-querol-apsr/action/displayFulltext?type=6&fid=8368205&jid=PSR&volumeId=105&issueId=03&aid=8368204&fulltextType=RA&fileId=S0003055411000281")

(deftest load-file-as-enlive-data
  (let [brq (load-html besley-ryenal-querol)]
    (is (= (brq :directory) "test/examples/besley-ryenal-querol-apsr/action"))
    (is (not (string? (brq :content))))
    (is (seq? (brq :content))))) 

(deftest making-authors
  (let [a1 (make-author "Mark M. Fredrickson")
        a2 (make-author ["Mark M." "Fredrickson"])]
    (is (= (class a1) nl.siegmann.epublib.domain.Author))
    (is (= (class a2) nl.siegmann.epublib.domain.Author))
    (is (= a1 a2))))

(def test-profile-data
  {:title "Test EPUB Book"
   :author "Mark M. Fredrickson" ; could also be ["Mark M."
                                 ; "Fredrickson"]
   ;; Enlive style HTML is a little obtuse... Having a Hiccup ->
   ;; Enlive interface would be nice. 
   :sections [["In which the book starts" [{:tag :h2 :content ["A subtitle"]} {:tag :p :content ["Some text"]}]]
              ["Conclusion" [{:tag :h2 :content ["Another subtitle"]} {:tag :p :content ["Some text"]}]]]})
              

(deftest making-an-ebook
  (is (= (class (make-ebook test-profile-data)) nl.siegmann.epublib.domain.Book))
  (is (= (count (.getContents (make-ebook test-profile-data))) 2)))

