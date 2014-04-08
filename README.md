#**_ImgurIR_**
written by Kim Bradley & Michael Church  
for CSCI 4330: AI and the Web, DOSHI, Spring 2014

##Overview
**Imgur Comment Generated Information Retrieval** is a system that allows a user to search for [Imgur](http://imgur.com/) images based on user generated comments using and a user interface for quering the database and displaying results. The system also includes a web crawler to mine comment data and save it to the database. It implements _web-content mining_ and _modern information retrieval_, which are concepts we have learned in class.

>#####Web Crawler that traverses Imgur:
>  1. Scrapes comments
>  2. Tokenizes words
>  3. Removes stopwords
>  4. Stems each tokenized word
>  5. Calculates frequencies
>  6. Stores links and stem frequencies

>#####User Query Interface:
>  1. Search with user inputed queries
>  2. Retrieve top image links from database
>  3. Display images in order of relevance

##Usage
`<to be implimented>`
##Evaluation
`<to be implimented>`
