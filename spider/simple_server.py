from src import csdn
from flask import Flask
import json

app = Flask(__name__)

shown_offset = None
@app.route('/article', methods=['GET'])
def get_article_list():
    global shown_offset
    shown_offset, article_refs = csdn.get_more_articles('career', shown_offset)
    return json.dumps(article_refs, cls=csdn.AttrsObjectEncoder)

if __name__ == '__main__':
    app.run()