#!/usr/bin/env python
# coding: utf-8

# In[41]:


#python Regular expression operations package
#https://docs.python.org/2/library/re.html#re.sub
import re


# In[1]:


#The spacy document has everything from install to process.
#Here is the link:https://spacy.io/usage
import spacy


# In[2]:


nlp = spacy.load("en_core_web_sm")


# In[3]:


from collections import OrderedDict


# In[5]:


from spacy.lang.en.stop_words import STOP_WORDS


# In[6]:


import csv


# In[64]:


#if you use Jupyter Notbook, then better upload the .csv to the same directory as this python
#Then just change the path to let the file open
f=open('/Users/riochen/3DisordersRecord.csv')
csv_f=csv.reader(f)
features=[]
for row in csv_f:
    features.append(row[1])
print (features)


# In[65]:


text=''.join(features)
#Regex to filter the redundency words up to standard instead of removing them
#https://stackoverflow.com/questions/51976328/best-way-to-remove-xad-in-python
text = text.replace('\xad', '')
text = text.replace('\u00ad', '')
text = text.replace('\N{SOFT HYPHEN}', '')
text = text.replace('/','')
text = text.replace(').',' ')


# add more stopwords from this step:
'''
from collections import Counter
word_freq=Counter()
for word in words:
    word_freq[word] += 1
common_words = word_freq.most_common(50)
print(common_words)
'''
#We got:
'''
[('depressive', 23), ('traumatic', 14), ('events', 11), ('physical', 9), ('substance', 8), ('sexual', 8), ('medication', 7), ('onset', 7), ('PTSD', 7), ('persistent', 6), ('dissociative', 6), ('abuse', 5), ('withdrawal', 5), ('exposure', 5), ('individuals', 5), ('negative', 5), ('major', 4), ('mood', 4), ('childhood', 4), ('behavior', 4), ('violence', 4), ('violent', 4), ('injury', 4), ('trauma', 4), ('de\xadpressive', 3), ('drug', 3), ('physiological', 3), ('clinical', 3), ('independent', 3), ('substantial', 3), ('distress', 3), ('drugs', 3), ('judgment', 3), ('essential', 3), ('previously', 3), ('recurrent', 3), ('hyperactivity', 3), ('excessive', 3), ('motor', 3), ('child', 3), ('important', 3), ('present', 3), ('setting', 3), ('typically', 3), ('activities', 3), ('fear', 3), ('states', 3), ('arousal', 3), ('war', 3), ('assault', 3)]
'''
# see: https://stackoverflow.com/a/51627002/9047811
nlp.Defaults.stop_words |= {'e.g.', 'Criterion','disorder','event','symptoms','individual','use','time',
    'period','diagnosis','history','long','\'', 'Bl','years','days','behaves',
    'non',
    '-','job','age','event','symptoms'}

for word in nlp.Defaults.stop_words:
    lex = nlp.vocab[word]
    lex.is_stop = True

#start the nlp text processing    
doc=nlp(text)
candidate_pos = ['NOUN', 'PROPN', 'ADJ','ADV']

#diminish the duplication
words=set()

keywords = []

#remove text data that not the English words:
    #remove all aphabet
    #remove or fix every words with)'/x'and '/xad'
for token in doc:
    if re.match(r'([A-Z])|([A-Z]\d)|(\w+\-)', token.text):
        continue
    #process the Lemmatization, see https://spacy.io/usage/adding-languages#lemmatizer
    if token.lemma_ in words:
        continue
    words.add(token.lemma_)
    #Get our key and mod   
    if token.pos_ in candidate_pos and not token.is_stop:
        if token.pos_ == 'NOUN' or token.pos_ == 'PROPN': 
            type = 'KEY'
        else:
            type = 'MOD'
        keywords.append({
            'type': type,
            'word': token.lemma_
        })


        #lemmas.append(token.text)
print(len(keywords))
print(keywords)


# This part is left for you. The keywords contains both Mod and Key, and has 391 in total.
# Find a way to connect the keywords to the access directly, then generate or update the DB.
# This is the link that I've found so far:
# https://datatofish.com/how-to-connect-python-to-ms-access-database-using-pyodbc/

# In[62]:


#https://stackoverflow.com/a/1047591/9047811
#unfinished
import pyodbc

def mdb_connect(db_file, user='admin', password = '', old_driver=False):
    driver_ver = '*.mdb'
    if not old_driver:
        driver_ver += ', *.accdb'

    odbc_conn_str = ('DRIVER={Microsoft Access Driver (%s)}'
                     ';DBQ=%s;UID=%s;PWD=%s' %
                     (driver_ver, db_file, user, password))

    return pyodbc.connect(odbc_conn_str)


# In[ ]:




