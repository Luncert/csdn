import json
import sys
from contextlib import closing
from requests import get, head
import threading
from unit_test import test

# https://lol.qq.com/v/v2/detail.shtml?docid=662166179888672302&tagGroup=

class AttrsObject(object):
    def __init__(self, json_data):
        for name in self.get_attr_names():
            if name in json_data:
                setattr(self, name, json_data[name])
    
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

class VideoRefDetail(AttrsObject):
    ATTR_NAMES = ['guid', 'iBiz', 'iDocID', 'iFrom', 'iIsRedirect', 'iSubType', 'iTime',
                'iTotalPlay', 'iType', 'iVideoId', 'sAuthor', 'sCoverList', 'sCoverMap',
                'sCreated', 'sDesc', 'sExt1', 'sExt2', 'sExt3', 'sIMG', 'sIdxTime',
                'sRedirectURL', 'sTagIds', 'sTitle', 'sUrl', 'sVID']
    def get_attr_names(self):
        return VideoRefDetail.ATTR_NAMES

class VideoRefDetails(object):
    def __init__(self, page, page_size, total_page, video_ref_details):
        self.page = page
        self.page_size = page_size
        self.total_page = total_page
        self.video_ref_details = video_ref_details
    
    def __str__(self):
        return 'VideoRefDetails(page={}, page_size={}, total_page={}, video_ref_details_num={}' \
            .format(self.page, self.page_size, self.total_page, len(self.video_ref_details))

# source: https://lol.qq.com/v/v2/videoDetail.shtml?tagGroup=new - video-list-detail.js getVideoList2
def get_newest_video_list(page, list_num=28, s_idx_time='sIdxTime'):
    '''
    @author Luncert

    @desc 获取最新视频列表
    
    parameters:
    - page: int 
    - list_num: int (optional)
    - s_idx_time: string (optional)

    return: VideoRefDetails
    '''
    url = 'https://apps.game.qq.com/wmp/v3.1/?p0=3&p1=searchKeywordsList&\
        page={}&pagesize={}&order={}&type=&id=&r0=jsonp&source=web_pc'\
        .format(page, list_num, s_idx_time)
    rep = get(url)
    if rep.status_code == 200:
        raw = rep.content.decode('utf-8')
        i, j = raw.find('{'), raw.rfind('}')
        raw = raw[i : j + 1]
        e = json.loads(raw)

        # assert e['status'] == 0
        msg = e['msg']
        video_ref_details = []
        for item in msg['result']:
            video_ref_details.append(VideoRefDetail(item))
        return VideoRefDetails(msg['page'], msg['pagesize'], msg['totalpage'], video_ref_details)
    else: print('get_newest_video_list failed, status code: %d' % rep.status_code)

class VideoDetail(AttrsObject):
    ATTR_NAMES = ['authorID', 'avatar', 'iCanCmt', 'iComment', 'iDocID', 'iIsRedirect', 'iSourceId',
                'iTime', 'iTotalPlay', 'likeTotal', 'sAuthor', 'sCreated', 'sDesc', 'sIMG',
                'sIdxTime', 'sOriginID', 'sRedirectURL', 'sSource', 'sTagIds', 'sTitle', 'sUrl',
                'sVID', 'type', 'uuid']
    def get_attr_names(self):
        return VideoDetail.ATTR_NAMES
    
# source: https://lol.qq.com/v/v2/js/common.js - getVideoDetail
def get_video_detail(doc_id):
    '''
    @author Luncert

    @desc 获取视频详细信息
    
    parameters:
    - doc_id: string

    return: VideoDetail
    '''
    url = 'https://apps.game.qq.com/cmc/zmMcnContentInfo?type=1&docid={}&source=web_pc'\
        .format(doc_id)
    rep = get(url)
    if rep.status_code == 200:
        raw = rep.content.decode('utf-8')
        e = json.loads(raw)
        if not e['msg'] == 'OK':
            raise Exception('get_video_detail failed, msg: {}, status: {}'.format(e['msg'], e['status']))

        result = e['data']['result']
        return VideoDetail(result)
    else: print('get_video_source failed, status code: %d' % rep.status_code)

# source: https://lol.qq.com/v/v2/js/video-detail.js - L180 'player = new Txplayer({'
# source: https://vm.gtimg.cn/tencentvideo/txp/js/txplayer.js - L28 d
def get_video_source(video_id):
    url = 'http://vv.video.qq.com/getinfo?otype=json&appver=3.2.19.333&platform=11&defnpayver=1&vid={}' \
        .format(video_id)
    rep = get(url)
    if rep.status_code == 200:
        raw = rep.content.decode('utf-8')
        i, j = raw.find('{'), raw.rfind('}')
        raw = raw[i : j + 1]
        video_json = json.loads(raw)
        
        fn_pre = video_json['vl']['vi'][0]['lnk']  # vid?
        # title = video_json['vl']['vi'][0]['ti']  # 标题
        host = video_json['vl']['vi'][0]['ul']['ui'][0]['url']  # 视频下载host
        streams = video_json['fl']['fi']  # 流列表
        seg_cnt = video_json['vl']['vi'][0]['cl']['fc']
        if seg_cnt == 0:
            seg_cnt = 1

        # best_quality = streams[-1]['name']
        # name = streams[-1]['cname']

        part_format_id = streams[-1]['id']

        part_urls = []
        for _ in range(1, seg_cnt + 1):
            # filename = fn_pre + '.p' + str(
            # part_format_id % 10000) + '.' + str(part) + '.mp4'
            filename = fn_pre + '.mp4'
            key_api = "http://vv.video.qq.com/getkey?otype=json&platform=11&format={}&vid={}&filename={}&appver=3.2.19.333".format(
                part_format_id, video_id, filename)
            part_info = get(key_api).text
            key_json = json.loads(part_info[13:-1])
            if key_json.get('key') is None:
                vkey = video_json['vl']['vi'][0]['fvkey']
                url = '{}{}?vkey={}'.format(video_json['vl']['vi'][0]['ul']['ui'][0]['url'], fn_pre + '.mp4', vkey)
            else:
                vkey = key_json['key']
                url = '{}{}?vkey={}'.format(host, filename, vkey)

            if not vkey:
                print(key_json['msg'])
                print("break")
                break
            part_urls.append(url)

        return part_urls
    else: print('get_video_source failed, status code: %d' % rep.status_code)

def save_video_multi_threads(url, target):
    def print_process():
        info = ''
        for id in range(len(process)):
            info += '(%d - %0.2f%%)' % (id, float(process[id] / part_size * 100))
        print(info, end='\r')

    def save(id, url, _range):
        headers = {
            'Accept-Encoding': 'identity;q=1, *;q=0',
            'chrome-proxy': 'frfr',
            'Range': _range,
            'Referer': 'http://ugcws.video.gtimg.com/uwMRJfz-r5jAYaQXGdGnC2_ppdhgmrDlPaRvaV7F2Ic/k0840fhbeat.mp4?vkey=311E5EEB906A0C94BA75BEEDC0E2B034974396BF6432570DCD32D902E1A99C6E527F56F5AB52BBB3695BB546E9C1A792C33E24C3FDE63C7DA268CBB11EDDBDB6184515FF87F3FB72FDFBD149620F0FD2FED5F09C91173837A8D89AA71BA2F568012A83683C730542',
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36'
        }
    
        with closing(get(url, headers=headers, stream=True, verify=False)) as rep:
            chunk_size = 1024
            # HTTP/206 “Partial Content”
            if rep.status_code == 200 or rep.status_code == 206:
                for data in rep.iter_content(chunk_size=chunk_size):
                    parts[id].append(data) 
                    process[id] += len(data)
                    print_process()
            else: raise Exception('save_video failed, status code: %d' % rep.status_code)

    rep = head(url)
    total_size = int(rep.headers['Content-Length'])
    part_size = 1000000
    part_num = int(total_size / part_size) + (0 if total_size % part_size is 0 else 1)
    parts = [[] for _ in range(part_num)]
    process = [0 for _ in range(part_num)]

    print('total size: %d, part size: %d, part num: %d' % (total_size, part_size, part_num))

    threads = []
    for i in range(part_num):
        _range = 'bytes=%d-' % (i * part_size)
        t = threading.Thread(target=save, args=(i, url, _range))
        t.start()
        threads.append(t)
    for t in threads:
        t.join()

def save_video(url, target):
    headers = {
        'Accept-Encoding': 'identity;q=1, *;q=0',
        'chrome-proxy': 'frfr',
        'Range': 'bytes=0-',
        'Referer': 'http://ugcws.video.gtimg.com/uwMRJfz-r5jAYaQXGdGnC2_ppdhgmrDlPaRvaV7F2Ic/k0840fhbeat.mp4?vkey=311E5EEB906A0C94BA75BEEDC0E2B034974396BF6432570DCD32D902E1A99C6E527F56F5AB52BBB3695BB546E9C1A792C33E24C3FDE63C7DA268CBB11EDDBDB6184515FF87F3FB72FDFBD149620F0FD2FED5F09C91173837A8D89AA71BA2F568012A83683C730542',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36'
    }
    rep = head(url)
    size = 0
    total_size = int(rep.headers['Content-Length'])
    with closing(get(url, headers=headers, stream=True, verify=False)) as rep:
        chunk_size = 1024
        if rep.status_code == 200 or rep.status_code == 206:
            with open(target, 'wb') as f:
                for data in rep.iter_content(chunk_size=chunk_size):
                    f.write(data)
                    size += len(data)
                    print('downloding: %0.2f%%' % float(size / total_size * 100), end='\r')
        else: raise Exception('save_video failed, status code: %d' % rep.status_code)
                    
@test
def test_get_newest_video_list():
    page = 1
    details = get_newest_video_list(page)
    print('page: %d, page_size: %d, total_page: %d' % (details.page, details.page_size, details.total_page))
    for video_ref_detail in details.video_ref_details:
        print(video_ref_detail)

@test
def test_get_video_detail():
    doc_id = '662166179888672302'
    video_detail = get_video_detail(doc_id)
    print(video_detail.sVID)

@test
def test_get_video_source():
    vid = 'k0840fhbeat'
    urls = get_video_source(vid)
    print(urls)

@test
def test_save_video():
    url = 'https://ugcws.video.gtimg.com/uwMRJfz-r5jAYaQXGdGnC2_ppdhgmrDlPaRvaV7F2Ic/k0840fhbeat.mp4?vkey=9C27B193280368963AF0B56684311965E1D66E7BF977B2CEA3BDAB1643E2AFDA094F2F6DBEB65A63666B2CCC1AC32A79678A5B2781F06B32D47426394E79D4D4929A98CEFE6323AAEF03F9ADA59F5289C1E7D24004C0B6459DE9915B55F78F76B417823CD77172BC'
    save_video(url, 'k0840fhbeat.mp4')

@test
def main_test():
    page = 1
    details = get_newest_video_list(page)
    for video_ref_detail in details.video_ref_details:
        video_detail = get_video_detail(video_ref_detail.iDocID)
        urls = get_video_source(video_detail.sVID)
        for i in range(len(urls)):
            target = video_detail.sVID
            target = target[:-4] + '-' + str(i) + target[-4:]
            save_video(urls[i], target)