(ns publichouse.test.profile.cjo
  (:use clojure.test
        publichouse.core
        publichouse.profile.cjo))

; requires you run test/examples/fetch-examples.sh
; it might be smart to make this script into a Cake task or function

(def besley-ryenal-querol
  "test/examples/besley-ryenal-querol-apsr/action/displayFulltext?type=6&fid=8368205&jid=PSR&volumeId=105&issueId=03&aid=8368204&fulltextType=RA&fileId=S0003055411000281")

(deftest test-transform
  (let [cjo (transform (load-html besley-ryenal-querol))]
    (is (map? cjo))
    (is (= (cjo :title) "Do Democracies Select More Educated Leaders?"))
    (is (= (cjo :author "TIMOTHY BESLEY and MARTA REYNAL-QUEROL")))))
