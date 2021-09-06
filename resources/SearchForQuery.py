# -*- coding: utf-8 -*-
"""
Created on Thu Nov 28 11:58:22 2019

@author: Diana
"""

#https://github.com/tensorflow/hub/blob/v0.10.0/tensorflow_hub/module_v2.py#L50-L108

import gensim
import pandas as pd
import re
import pickle
import tensorflow as tf
import tensorflow_hub as hub


import wiki
import preprocessData as pData


tf.compat.v1.disable_v2_behavior()

#extract data bases of videos
def extractData():
    for i in range(3):
        dataframe = pd.read_excel("LabeledTranscripts.xlsx", usecols = [i])
        yield dataframe.values

videosName,transcripts,labels =extractData()

documents = []
documentsWithLabels = [[],[],[]]
preprocessedDocumentsList = []

#Process all transcripts
nrOfTranscriptsToProcess = len(videosName)
for i in range(nrOfTranscriptsToProcess):
    transcript = transcripts[i][0]
    videoName = videosName[i][0]
    label = labels[i][0]
    if transcript !=  "" and len(transcript)>6000:      #keep only valid transcripts for preprocessing
        preprocessedTranscript = pData.preprocess(transcript)   

        #keep all preprocessed transcripts in a container for each label
        documentsWithLabels[label].append((preprocessedTranscript,videoName))
        
        #keep all preprocessed transcripts together
        documents.append([preprocessedTranscript, videoName, label])
    
        #keep all words from transcripts
        preprocessedDocumentsList.append(preprocessedTranscript.split())

noOfClusters = 3
preprocessedListForDictionary = []

for i in range(noOfClusters):
    preprocessedListForDictionary.append([])

#append transcripts words in a list
index = 0
for transcript, videoName, label in documents:
    preprocessedListForDictionary[label].append(preprocessedDocumentsList[index])
    index += 1

# Creating the dictionaries for the LSI models
dictionary = []
for i in range(noOfClusters):
    dictionary.append(gensim.corpora.Dictionary(preprocessedListForDictionary[i]))


bow_corpus = [None] * noOfClusters  
lsi = [None] * noOfClusters
indexList = [None] * noOfClusters


from gensim import models
from gensim import similarities

# Creating the LSI models
for i in range(noOfClusters):
    if( len(dictionary[i]) != 0):
        #we have to create a bag of words( BoW )
        bow_corpus[i] = [dictionary[i].doc2bow(doc) for doc in preprocessedListForDictionary[i]]
        
        #we will transform it in a tf-idf vector
        tfidf = models.TfidfModel(bow_corpus[i]) 
        corpus_tfidf = tfidf[bow_corpus[i]]

        lsi[i] = models.LsiModel(corpus = corpus_tfidf, id2word=dictionary[i], num_topics=5)
        #we will compute a similarity matrix, which it will help us later, for query
        indexList[i] = similarities.MatrixSimilarity(lsi[i][corpus_tfidf])

        print(indexList[i])
        print(lsi[i].print_topics(num_topics= 5 , num_words=10))
     
#load the model
embed = hub.load("https://tfhub.dev/google/nnlm-es-dim128-with-normalization/2")
clf = pickle.load(open("alex_model.sav", 'rb'))

import time

#Function that returns the result for a query
def resultsForQuery(query, queryToken):
    start_time = time.time()
    ans = wiki.wikipedia_search(query)     #search a sequence on wiki pages
    if(len(ans["itemList"]) != 0 ):             #if we have a result
            queryWiki =  (ans["itemList"][0]["description"])       #assign as query this sequence
    else:   
            queryWiki = query                  #else assign just the query
    # ! Time
    search_time = round((time.time()-start_time)*100)
    with tf.compat.v1.Session() as session:
            session.run([tf.compat.v1.global_variables_initializer(), tf.compat.v1.tables_initializer()])
            embeddedQueryWiki = session.run(embed([queryWiki]))
    # ! Time
    sess_time =round((time.time()-start_time)*100)
    #make a predict for the query for its cluster
    queryClusterWiki = clf.predict(embeddedQueryWiki)[0]
        
    #search in assigned container
    cluster = queryClusterWiki
    # ! Time
    predict_time = round((time.time()-start_time)*100)

    print("cluster")
    print(cluster)
    
    #transform in a bow corpus the query
    vec_bow = dictionary[cluster].doc2bow(pData.singularizeQuery(query))
    # convert the query to LSI space
    vec_lsi = lsi[cluster][vec_bow]  
    
    # perform a similarity query against the corpus
    sims = indexList[cluster][vec_lsi]  
        
    sims = sorted(enumerate(sims), key=lambda item: -item[1])
    # ! Time
    sims_time = round((time.time()-start_time)*100)
    
    #print top 10 results
    for s in sims[:10]:
        print(queryToken+s, documentsWithLabels[cluster][s[0]][1])

while(True):
    query = input()
    whiteSpaceRegex = '\\s'
    millis = int(round(time.time() * 1000))
    resultForQuery(query, queryToken)
    print('python query part took ', int(round(time.time() * 1000))-millis, ' seconds')
    print('eoq '+queryToken,flush=True)







