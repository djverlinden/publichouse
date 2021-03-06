(ns publichouse.core
  (:gen-class :main true)
  (:use [clojure.string :only [split]])
  (:require [net.cgrand.enlive-html :as e]
            [clojure.java.io :as io])
  (:import [java.io FileOutputStream ByteArrayInputStream]
           [nl.siegmann.epublib.epub EpubWriter]
           [nl.siegmann.epublib.domain Book Author Resource]))

;;; I am indebted to
;;; http://srid.github.com/blog/2011/09/generating-epub-in-clojure/
;;; for tips on setting up and using epublib

;;; from https://bitbucket.org/tebeka/fs/src/0df7925bf36c/src/fs.clj
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

;;;; Profile interface
;;; A profile is a transform of an Enlive html document into a well
;;; formed data structure for use in the ebook. The output is a map
;;; with the follwoing keys:

;;; :title => A string for the title field
;;; :author => A string for the author field or a pair of strings
;;; :sections => a list or vectors of pairs where the first item in
;;;              the pair is the title and the second item is HTML

(defn make-author
  "Handles different author specifications"
  [a]
  (if (coll? a)
    (Author. (first a) (second a))
    (let [exploded (split a #" ")
          lname (last exploded)
          fname (apply str (interpose " " (butlast exploded)))]
      (Author. fname lname))))

(defn make-ebook
  [profile-data]
  (let [book (Book.)]
    (doto (.getMetadata book)
      (.addTitle (profile-data :title))
      (.addAuthor (make-author (profile-data :author))))
    (doseq [section (profile-data :sections)]
      (.addSection
       book
       (first section)
       (Resource. 
        (-> (apply str (e/emit* (second section))) 
            (.getBytes)
            (ByteArrayInputStream.))
        (str (gensym) ".html"))))
    book))

(defn write-ebook
  "Actually write out an ebook to file"
  [ebook file-name]
  (.write (EpubWriter.) ebook (FileOutputStream. file-name)))

(defn -main
  "Entry point for command line interface"
  [profile input output]
  ;; start with error checking inputs
  (let [profile-ns (symbol (str "publichouse.profile." profile))
        input-file (io/as-file input)]
    (try
      (require profile-ns)
      (catch java.io.FileNotFoundException _
        (do
          (println "No profile found matching:" profile)
          (System/exit 1))))
    (when (not (.exists input-file))
      (println "Input file not found")
      (System/exit 2))

    ;; all error checking out of the way, time to make the ebook
    (println "Starting transformation using profile" profile)
    (-> (load-html input) ((ns-resolve profile-ns 'transform)) (make-ebook) (write-ebook output))
    (println "Transformation complete.")))
