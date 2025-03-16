import sys
import re
from nltk.stem import SnowballStemmer
from nltk.corpus import stopwords
import nltk

nltk.download('stopwords')
nltk.download('punkt')

stemmer = SnowballStemmer("russian")
stop_words = set(stopwords.words('russian'))

def validate_theme(theme_text):
    keywords = ["разработка", "реализация", "исследование"]
    words = nltk.word_tokenize(theme_text.lower())
    words = [word for word in words if word not in stop_words and word.isalnum()]
    stemmed_words = [stemmer.stem(word) for word in words]

    for keyword in keywords:
        stemmed_keyword = stemmer.stem(keyword)
        if stemmed_keyword in stemmed_words:
            return True
    return False


if __name__ == "__main__":
    theme_text = sys.argv[1]
    result = validate_theme(theme_text)
    print("valid" if result else "invalid")