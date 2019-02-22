from unit_test import test
from requests import get
from bs4 import BeautifulSoup
import json

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36'
}

class AttrsObjectEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, AttrsObject):
            ret = {}
            for name in obj.get_attr_names():
                if hasattr(obj, name):
                    value = getattr(obj, name)
                    ret[name] = value
            return ret
        return json.JSONEncoder.default(self, obj)

class AttrsObject(object):
    def __init__(self, json_data=None):
        if json_data:
            for name in self.get_attr_names():
                if name in json_data:
                    setattr(self, name, json_data[name])
        else:
            for name in self.get_attr_names():
                setattr(self, name, None)
    
    def get_attr_names(self):
        return []
    
    def __str__(self):
        ret = self.__class__.__name__ + '('
        for name in self.get_attr_names():
            if hasattr(self, name):
                value = getattr(self, name)
                if isinstance(value, str):
                    ret += '%s="%s",' % (name, value)
                else:
                    ret += '%s=%s,' % (name, value)
        if ret[-1] == ',': ret = ret[:-1] + ')'
        else: ret += ')'
        return ret

class Article(AttrsObject):
    ATTR_NAMES = ['title', 'time', 'author', 'read_count', 'tags',
                'copyright', 'content']
    def get_attr_names(self):
        return Article.ATTR_NAMES

class ArticleRef(AttrsObject):
    ATTR_NAMES = ['avatar', 'cache_time', 'category', 'category_id', 'comments',
                'commits', 'created_at', 'desc', 'downs', 'id', 'isexpert',
                'nickname', 'quality_score', 'shown_offset', 'shown_time',
                'sourcetype', 'strategy', 'strategy_id', 'sub_title', 'summary',
                'tag', 'title', 'type', 'url', 'user_name', 'user_url', 'views']
    def get_attr_names(self):
        return ArticleRef.ATTR_NAMES

CATEGORY = ['career', 'web', 'arch', 'lang', 'db', 'game', 'mobile', 'ops', 'ai',
    'sec', 'cloud', 'engineering', 'iot', 'fund', 'avi', 'other']

def get_article(url):
    rep = get(url, headers=headers)
    assert(rep.status_code == 200)

    article = Article()
    soup = BeautifulSoup(rep.content, 'lxml')

    # 获取标题
    article.title = soup.find(class_='article-title-box')\
        .find(class_='title-article').text

    # 获取相关信息：创建时间、作者、阅读数、标签
    info = soup.find(class_='article-info-box')\
        .find(class_='article-bar-top')
    article.time = info.find(class_='time').text
    article.author = info.find(class_='follow-nickName').text
    article.read_count = info.find(class_='read-count').text
    article.tags = []
    tags_box = info.find(class_='tags-box')
    if tags_box:
        for item in tags_box.find_all('a'):
            article.tags.append(item.text.strip())

    # 版权
    copy_right = soup.find(class_='article-copyright')
    if copy_right:
        article.copyright = copy_right.text.strip()

    # TODO: cast html to markdown
    article.content = soup.find(id='content_views').find_all('p')

    return article

def get_more_articles(category, shown_offset=None):
    if shown_offset is None:
        url = 'https://www.csdn.net/nav/{}'.format(category)
        rep = get(url, headers=headers)
        assert(rep.status_code == 200)
        soup = BeautifulSoup(rep.content, 'lxml')
        shown_offset = int(soup.find(id='feedlist_id')['shown-offset'])

    url = 'https://www.csdn.net/api/articles?type=more&category={}&shown_offset={}'\
        .format(category, shown_offset)
    rep = get(url, headers=headers)
    assert(rep.status_code == 200)

    data = json.loads(rep.content)
    assert(data['status'] == 'true')

    article_refs = []
    for item in data['articles']:
        article_refs.append(ArticleRef(item))
    # ignore: last_view_time, message, no_wartchers
    return (data['shown_offset'], article_refs)    

@test
def test_get_article():
    url = 'https://blog.csdn.net/duozhishidai/article/details/87867635'
    article = get_article(url)
    print(article)

@test
def test_get_more_articles():
    category = 'career'
    shown_offset, article_refs = get_more_articles(category, shown_offset=None)
    print(shown_offset, article_refs)
    with open('tmp', 'wb') as f:
        f.write(article_refs[0].__str__().encode('utf-8'))
