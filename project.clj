(defproject publichouse "0.1.0"
  :main publichouse.core
  :description "Transform HTML to EPUB"
  :dependencies [[clojure "1.2.1"]
                 [nl.siegmann.epublib/epublib-core "3.0-SNAPSHOT"]
                 [enlive "1.0.0"]]
  :repositories
  {"psiegman"
   "https://github.com/psiegman/mvn-repo/raw/master/snapshots"})
               
(deftask ph
  "Using the `profile`, transform `input` HTML into `output` EPUB"
  (bake
   (:require publichouse.core)
   [[profile input output] (:ph *opts*)]
   (publichouse.core/-main profile input output)))

