;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;
(ns org.soulspace.clj.java.system
  (:require [clojure.string :as str]))

;;
;; Functions for interacting with java.lang.System
;;

;;
;; Environment vars and system properties
;;

(defn get-environment-variable
  "Returns the environment variable named var.
   If a default is specified and the environment variable is not set, the default will be returned."
  ([var]
   (System/getenv (name var)))
  ([var default]
   (let [env (get-environment-variable var)]
     (if (seq? env)
       env
       default))))

(defn set-system-property
  "Sets a system property."
  [property value]
  (System/setProperty property value))

(defn get-system-property
  "Gets a system property.
   If a default is specified and the property is not set, the default will be returned."
  ([property]
   (System/getProperty property))
  ([property default]
   (System/getProperty property default)))

(defn clear-system-property
  "Removes the system propery."
  [property]
  (System/clearProperty property))

;;
;; Proxies
;;

(defn use-system-proxies
  "Tell the JVM that the system proxies should be used."
  []
  (set-system-property "java.net.useSystemProxies" "true"))

(defn set-http-proxy
  "Set an HTTP proxy for the JVM."
  ([host port]
   (set-system-property "http.proxyHost" host)
   (set-system-property "http.proxyPort" port))
  ([host port bypassed-hosts]
   (set-http-proxy host port)
   (set-system-property "http.nonProxyHosts" (join "|" bypassed-hosts))))

(defn set-https-proxy
  "Set an HTTPS proxy for the JVM."
  [host port]
  (set-system-property "https.proxyHost" host)
  (set-system-property "https.proxyPort" port))

(defn set-ftp-proxy
  "Set a FTP proxy for the JVM."
  ([host port]
   (set-system-property "ftp.proxyHost" host)
   (set-system-property "ftp.proxyPort" port))
  ([host port bypassed-hosts]
   (set-https-proxy host port)
   (set-system-property "ftp.nonProxyHosts" (join "|" bypassed-hosts))))

(defn set-socks-proxy
  "Set an SOCKS proxy for the JVM."
  [host port]
  (set-system-property "socksProxyHost" host)
  (set-system-property "socksProxyPort" port))

;;
;; Current time
;;

(defn nano-time
  "Returns the current time in nano seconds."
  []
  (System/nanoTime))

(defn milli-time
  "Returns the current time in milli seconds."
  []
  (System/currentTimeMillis))

;;
;; Operating systems
;;

(defn os-name
  "Returns the value of the os.name property."
  []
  (get-system-property "os.name"))

(defn os-windows?
  "Tests for Windows OS."
  ([]
   (os-windows? (os-name)))
  ([os]
   (str/starts-with? os "Windows")))

(defn os-linux?
  "Tests for Linux OS."
  ([]
   (os-linux? (os-name)))
  ([os]
   (str/starts-with? os "Linux")))

(defn os-mac-os?
  "Tests for Mac OS."
  ([]
   (os-mac-os? (os-name)))
  ([os]
   (str/starts-with? os "Mac OS")))

(defn os-sun-os?
  "Tests for SunOS."
  ([]
   (os-sun-os? (os-name)))
  ([os]
   (str/starts-with? os "SunOS")))

(defn os-freebsd?
  "Tests for FreeBSD OS."
  ([]
   (os-freebsd? (os-name)))
  ([os]
   (str/starts-with? os "FreeBSD")))

(defn os-aix?
  "Tests for AIX OS."
  ([]
   (os-aix? (os-name)))
  ([os]
   (str/starts-with? os "AIX")))

(defn os-unix?
  "Tests for a Unix OS."
  []
  (let [os (os-name)]
    (or (os-linux? os) (os-mac-os? os) (os-sun-os? os)  (os-freebsd? os) (os-aix? os))))

(defn line-separator
  "Returns the line separator of the current system."
  []
  (System/lineSeparator))

;;
;; Exit JVM
;;

(defn exit
  "Terminates the currently running JVM."
  ([]
   (exit 0))
  ([status]
   (System/exit status)))

