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

