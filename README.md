# Public House
Transform web pages into EPUB documents.

## Description

E-readers (Amazon Kindle, B&N Nook, etc.) are great for reading largely
text documents. In fact, the underlying formats of these readers are
basically HTML, the standard markup language of the web. Most web
pages, however, come with extra junk we don't need to see when reading
the content: headers, footers, sidebars, ads, etc. Public House is a
utility to strip these unnecessary components out of the HTML and
generate EPUB documents (which can be read natively on many e-readers
and serve as point of conversion most others).

The primary goal of Public House is to make it easy to read academic
journals on e-readers. PDFs are great for representing how a printed
page would look, but can be hard to manipulate properly on the small
sceens of the e-readers and force users to give up useful features
such as text sizing and dictionary look ups. Most academic publishers
provide HTML versions of papers as well, presenting an opportunity to
transform these documents into a e-reader native format.

Public House automates this transformation process through the use of
"profiles." A profile is a recipe for turning a web page into an EPUB
document. Profiles strip away unnecessary elements and correct for
unusual formatting or structure from the publisher. After transforming
the document, it is written into an EPUB document. Users can then load
this document on their e-readers using software such as
[Calibre](http://calibre-ebook.com). 

Current profiles include:

* `cjo`: Cambridge Journals Online, which publishes many academic
journals

## Installation

Public House is currently available as a command line utility (OS X,
Linux, etc). A graphic interface is planned, but not yet implemented. 
There is currently a bug preventing releasing Public House as a
standalone utility, but adventurous users can clone this repository
and use [cake](http://github.com/flatland/cake) to run the utility.

    $ git clone https://github.com/markmfredrickson/publichouse.git
    $ cd publichouse/test/examples
    $ ./fetch-examples.sh
    # paths are relative to the repository root
    $ cake ph cjo test/examples/besley-ryenal-querol-apsr/action/main.html test.epub

## Writing Profiles

To write a profile you will need to know a little about
[Clojure](http://clojure.org) and
HTML. I also suggest you learn about
[Enlive](https://github.com/cgrand/enlive), 
as it is a useful tool for transforming the source HTML.

A profile is a Clojure namespace under `publichouse.profile`,
e.g. `publichouse.profile.cjo`, with a `transform`
function. `transform` takes as a single argument a map with `:content`
and `:directory` keys. The content is a list of XML, loaded from the
supplied input file name. The directory is the location of this file,
which can be useful when rewriting image links or other relative
files.

The `transform` form function returns a map with the following keys:

* `:title`: A string for the title field
* `:author`:  A string for the author field or a pair of strings for
first and last name. _Warning_: This field will eventually be a
sequence of names to accommodate multiple authors.
* `:sections`: a list of pairs where the first item is the pair is the
title and the second item is HTML for the section. If you want section
to contain their own titles, make sure to prepend an `h1` element with
the section title. Titles are otherwise only used in the table of
contents, so they can be different than in text titles.

You can use whatever means you see fit to generate the HTML (again XML
represented as maps), but I have found
[Enlive](https://github.com/cgrand/enlive) to be the best tool
for extracting and manipulating HTML in Clojure.


## FAQ

### Are there alternative ways to get web pages onto e-readers?

*Yes*. First, you could use an EPUB editor like
[Sigil](http://code.google.com/p/sigil/) to edit a web page and turn it
into an e-book. This has the advantage of being a graphical interface
like MS Word. I prototyped several transformations this way. 
If you are converting many documents, you may find this tedious.

There is a Firefox plugin called
[GrabMyBooks](http://www.grabmybooks.com/) that allows you to turn
entire web pages or just selections of a page into an EPUB document. It
strips away much of the formatting (for better or worse) in the
process.

There are various PDF to EPUB utilities. I have not tried them, so I
can't speak to their effectiveness.

### Can I use Public House to generate documents for my Kindle?

*Yes*. While Kindles do not read EPUB natively, you can use
[Calibre](http://calibre-ebook.com) to convert to `.mobi`
format. Calibre is also useful for organizing and loading documents on
Kindles and other e-readers.

### Can I contribute a profile?

Certainly. Fork this repository, create your profile, and send me a
pull request. For more details on writing profiles, see above. For
details on forking and sending pull requests see the [github help
pages](http://help.github.com/).
