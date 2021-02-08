#!/usr/bin/env python
# coding: utf-8

# In[4]:


#python Regular expression operations package
#https://docs.python.org/2/library/re.html#re.sub
import re


# In[5]:


#The spacy document has everything from install to process.
#Here is the link:https://spacy.io/usage
import spacy


# In[6]:


nlp = spacy.load("en_core_web_sm")


# In[7]:


from collections import OrderedDict


# In[8]:


from spacy.lang.en.stop_words import STOP_WORDS


# In[9]:


import csv


# In[10]:


#if you use Jupyter Notbook, then better upload the .csv to the same directory as this python
#Then just change the path to let the file open
f=open('/Users/riochen/3DisordersRecord.csv')
csv_f=csv.reader(f)
features=[]
for row in csv_f:
    features.append(row[1])
print (features)


# In[34]:


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
            type = 'Key'
        else:
            type = 'Mod'
        keywords.append({
            'WordType': type,
            'Word': token.lemma_
        })


        #lemmas.append(token.text)

print(len(keywords))
#print(keywords)


# In[29]:


#try to print out 'Mod' type only
# def search(Mod):
#     for word in keywords:
#         if word['WordType'] == Mod:
#             return word

# search("Mod")
#next(item for item in keywords if item["WordType"] == "Mod")


# In[33]:


#keywords = [{'WordType': 'Mod', 'Word': 'diagnostic'}, {'WordType': 'Key', 'Word': 'feature'}, {'WordType': 'Key', 'Word': 'substancemedication'}]
def search(Mod):
    return [word for word in keywords if word['WordType'] == Mod]

for i in search("Key"):
    print("{}  {}".format(*i.values()))
    


# In[ ]:


for dic in dataList:
    for key in dic:
        print(dic[key])


# In[16]:


import io
import csv
def toCsv(listOfDict):
  keys = listOfDict[0].keys()
  file = io.StringIO()
  
  dict_writer = csv.DictWriter(file, keys)
  dict_writer.writeheader()
  dict_writer.writerows(listOfDict)
  return file.getvalue()

print(toCsv(keywords))


# In[17]:


with open("OutputKeyMod.csv", "w") as text_file:
    text_file.write(toCsv(keywords))


# This part is left for you. The keywords contains both Mod and Key, and has 391 in total.
# Find a way to connect the keywords to the access directly, then generate or update the DB.
# This is the link that I've found so far:
# https://datatofish.com/how-to-connect-python-to-ms-access-database-using-pyodbc/

# In[ ]:




