(ns publichouse.core
  (:use [clojure.string :only [split]])
  (:require [net.cgrand.enlive-html :as e]
            [clojure.java.io :as io]))

; from https://bitbucket.org/tebeka/fs/src/0df7925bf36c/src/fs.clj
(defn- dirname
  "Return directory name of path.\n\t(dirname \"a/b/c\") -> \"/a/b\"."
  [path]
  (.getParent (io/as-file path)))

(defn load-html
  "Load an HTML file and parse via Enlive. Returns a map with :content and
:directory (to find images, etc.)"
  [path]
  {:directory (dirname path)
   :content (e/html-resource (io/as-file path))})
