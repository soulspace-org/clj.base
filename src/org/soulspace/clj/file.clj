;;;;
;;;;   Copyright (c) Ludger Solbach. All rights reserved.
;;;;
;;;;   The use and distribution terms for this software are covered by the
;;;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;;;   which can be found in the file license.txt at the root of this distribution.
;;;;   By using this software in any fashion, you are agreeing to be bound by
;;;;   the terms of this license.
;;;;
;;;;   You must not remove this notice, or any other, from this software.
;;;;

(ns org.soulspace.clj.file
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [org.soulspace.clj.string :as sstr]))

;;;
;;; Functions for working with files
;;;

;;
;; File predicates
;;

(defn exists?
  "Returns true, if the given file exists."
  [file]
  (let [file (io/as-file file)]
    (and (not (nil? file)) (.exists file))))

(defn is-dir?
  "Returns true, if the given file exists and is a directory."
  [file]
  (let [file (io/as-file file)]
    (and (exists? file) (.isDirectory file))))

(defn is-file?
  "Returns true, if the given file exists and is a directory."
  [file]
  (let [file (io/as-file file)]
    (and (exists? file) (.isFile file))))

(defn readable?
  "Returns true, if the given file is readable."
  [file]
  (let [file (io/as-file file)]
    (and (exists? file) (.canRead file))))

(defn writeable?
  "Returns true, if the given file is writeable."
  [file]
  (let [file (io/as-file file)]
    (and (exists? file) (.canWrite file))))

(defn executable?
  "Returns true, if the given file is executable."
  [file]
  (let [file (io/as-file file)]
    (and (exists? file) (.canExecute file))))

;;
;; File name and path functions
;;

(defn- list-files [file]
  (let [file (io/as-file file)]
    (seq (.listFiles file))))

(defn- list-paths [file]
  (let [file (io/as-file file)]
    (seq (.list file))))

(defn file-name
  "Returns the name of the file."
  [file]
  (let [file (io/as-file file)]
    (.getName file)))

(defn base-name
  "Returns the name of the file."
  [file]
  (let [file-name (.getName (io/as-file file))]
    (sstr/substring 0 (str/last-index-of file-name \.) file-name)))

(defn parent-path
  "Returns the parent path for the file if it exists."
  [file]
  (let [file (io/as-file file)]
    (.getParent file)))

(defn parent-dir
  "Returns the parent dir of the file if it exists."
  [file]
  (let [file (io/as-file file)]
    (.getParentFile file)))

(defn path
  "Returns the path of the file."
  [file]
  (let [file (io/as-file file)]
    (.getPath file)))

(defn normalize-path
  "Returns the normalized path (unix convention) of the path."
  [path]
  (str/replace path \\ \/))

(defn normalized-path
  "Returns the normalized path (unix convention) of the file."
  [file]
  (str/replace (path file) \\ \/))

(defn absolute-path
  "Returns the absolute path of the file."
  [file]
  (let [file (io/as-file file)]
    (.getAbsolutePath file)))

(defn absolute-file
  "Returns the file as absolute file."
  [file]
  (let [file (io/as-file file)]
    (.getAbsoluteFile file)))

(defn canonical-path
  "Returns the canonical path of the file."
  [file]
  (let [file (io/as-file file)]
    (.getCanonicalPath file)))

(defn canonical-file
  "Returns the canonical file of the file."
  [file]
  (let [file (io/as-file file)]
    (.getCanonicalFile file)))

(defn relative-path
  "Returns the path of the file relative to the base-path."
  [base-path file]
  (let [cpath (canonical-path file)
        cbase-path (canonical-path (io/as-file base-path))]
    (if (str/starts-with? cpath cbase-path)
      (if (str/ends-with? cbase-path "/")
        (sstr/substring (count cbase-path) cpath)
        (sstr/substring (+ (count cbase-path) 1) cpath))
      (path file))))

;;
;; Searching, listing and matching
;;

(defn has-extension?
  "Returns true if the path of the file ends with the given extension."
  [ext file]
  (and (exists? file) (str/ends-with? (path file) (str "." ext))))

(defn matches?
  "Returns true if the path of the file matches the given pattern."
  [pattern file]
  (and (exists? file) (re-matches pattern (normalized-path (path file)))))

(defn files
  "Returns a sequence of the files in a directory given as file.
   If the given file is not a directory, it is returned as only file in the sequence."
  [file]
  (when (exists? file)
    (if (is-dir? file)
      (let [files (list-files file)]
        files)
      [file])))

(defn files-by-extension
  "Returns a sequence of the files with the extension ext in a directory given as file.
   If the given file is not a directory, it is returned as only file in the sequence."
  [ext file]
  (filter (partial has-extension? ext) (files file)))

(defn all-files
  "Returns a sequence of the files in a directory given as file and its sub directories.
   If the given file is not a directory, it is returned as only file in the sequence."
  [file]
  (when (exists? file)
    (if (is-dir? file)
      (let [files (conj [] file)]
        (concat files (flatten (map all-files (list-files file)))))
      [file])))

(defn all-files-by-extension
  "Returns a sequence of the files with the extension ext in a directory given as file and its sub directories.
   If the given file is not a directory, it is returned as only file in the sequence."
  [ext file]
  (filter (partial has-extension? ext) (all-files file)))

(defn all-files-by-pattern
  "Returns a sequence of the files that match the pattern in a directory given as file and its sub directories.
   If the given file is not a directory, it is returned as only file in the sequence."
  [pattern file]
  (filter (partial matches? pattern) (all-files file)))

;;
;; Creation and deletion
;;

(defn create-dir
  "Creates a directory including missing parent directories."
  [file]
  (when-not (exists? file)
    (.mkdirs file)))

(defn delete-file
  "Deletes the file."
  [file]
  (let [file (io/as-file file)]
    (when (exists? file)
      (.delete file))))

(defn delete-dir
  "Deletes the directory and any subdirectories."
  [file]
  (let [file (io/as-file file)]
    (doseq [f (reverse (all-files file))]
      (delete-file f))))

;;
;; Functions to build search paths and search files
;;

(defn split-path
  "Split a path string with the given separator or ':' as default."
  ([paths]
   (split-path ":" paths))
  ([sep paths]
   (str/split paths (re-pattern sep))))

(defn build-path
  "Build a path by joining the given files with the separator or ':' as default."
  ([files]
   (build-path ":" files))
  ([sep files]
   (str/join sep (map path files))))

; convert ** -> ('filename pattern'|/)+  convert * -> ('filename pattern')+
(defn path-pattern
  "Convert ant style path patterns to regex path patterns."
  [ant-pattern]
  (let [file-regex "\\w|\\d|–"]
    (str/replace (str/replace ant-pattern "**" (str "(?:" file-regex "|/)+")) "*" (str "(?:" file-regex ")+"))))

(defn build-searchpath
  "Creates a sequence containing the directories to search."
  [pathnames]
  (if (coll? pathnames)
    (map io/as-file pathnames)
    (map io/as-file (split-path pathnames))))

(defn build-absolute-path
  "Returns the absolute path of the file defined by the given directory, filename (and extension)."
  ([dir filename]
   (str (absolute-path dir) "/" filename))
  ([dir filename extension]
   (str (absolute-path dir) "/" filename "." extension)))

(defn existing-files
  "Returns all existing files (with the specified extension) in the given directories."
  ([dirs]
   (filter exists? (flatten (map all-files dirs))))
  ([ext dirs]
   (filter exists? (flatten (map #(all-files-by-extension ext %) dirs)))))

(defn existing-files-by-pattern
  "Returns all existing files matching the specified pattern in the given directories."
  [pattern dirs]
  (filter exists? (flatten (map #(all-files-by-pattern pattern %) dirs))))

(defn existing-files-on-path
  "Returns all existing files (with the specified extension) on the given path."
  ([dir-path]
   (existing-files (map io/as-file (split-path dir-path))))
  ([ext dir-path]
   (existing-files ext (map io/as-file (split-path dir-path)))))

(defn directory-searcher
  "Returns a function that takes a directory and returns the file specified by filename (and extension)."
  ([filename]
   (fn [dir]
     (let [abs-name (build-absolute-path dir filename)]
       (io/as-file abs-name))))
  ([filename extension]
   (fn [dir]
     (let [abs-name (build-absolute-path dir filename extension)]
       (io/as-file abs-name)))))

(defn locate-file
  "Returns the first existing file on the search path for the specified filename (and extension)."
  ([searchpath filename]
   (first (filter exists? (map (directory-searcher filename) searchpath))))
  ([searchpath filename extension]
   (first (filter exists? (map (directory-searcher filename extension) searchpath)))))

(defn file-locator
  "Returns a function that locates a file by name on the search path."
  ([searchpath]
   (fn [filename]
     (locate-file searchpath filename)))
  ([searchpath ext]
   (fn [filename]
     (locate-file searchpath filename ext))))
