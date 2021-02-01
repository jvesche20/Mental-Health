import string
from string import punctuation
import nltk
from nltk.corpus import stopwords
from nltk.corpus import words
from nltk.tokenize import sent_tokenize, word_tokenize
import sys

def cleanResults(dis):
	allwords = []

	allwords = dis.split(',')
	noPunct = []
	for word in allwords:
	    np =  [w for w in word if not w in punctuation] 
	    noPunct.append(''.join(np))

	stop_words = set(stopwords.words('english'))

	noStop = []
	for word in noPunct:
	    if word not in stop_words and len(word) > 2:
	        noStop.append(word)

	noDup = []
	for word in noStop:
	    if word not in noDup:
	        noDup.append(word)

	real = []

	for word in noDup:
	    if word in words.words():
	        real.append(word)

	text = ' '.join(real)
	read = word_tokenize(text)

	values = nltk.pos_tag(read)

	goodtags = []
	for tag in values:
	    if tag[1] == 'NN' or tag[1] == 'JJ' or tag[1] == 'VBG':
	        goodtags.append(tag)
        
	goodwords = []
	for t in goodtags:
	    if t[0] not in goodwords:
	        goodwords.append(t[0])

	return goodwords


res = cleanResults(sys.argv[len(sys.argv)-1])

if res != None:
	ws = ' '.join(res)
	sys.stdout.write(ws)




