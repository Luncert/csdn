
import { r, rc, ReactType } from '../util/react-helper';
import { Component, Props } from '../com/Component';
import ItemList from '../com/ItemList';
import Avatar from '../com/Avatar';
import Label from '../com/Label';
import Division from '../com/Division';
import Input from '../com/Input';
import Button from '../com/Button';
import Capsule from '../com/Capsule';
import DropBox from '../com/DropBox';
import resource from '../core/Resource';
import acceleratorManager, { KEYS, CombineKey } from '../core/AcceleratorManager';

const Websocket = <any> require('react-websocket')
const url = <any> require('../res/img/author.jpg');
const styles = <any> require('./HomePage.css');

interface Article {
    title: string;
    summary: string;
    avatar: string;
    author: string;
    createTime: string;
    viewCount: number;
}

const LogLevel = {
    Info: 'Info', Warn: 'Warn', Error: 'Error'
}

const LOG_LEVEL = ['Info', 'Warn', 'Error'];

interface Log {
    level: string;
    timestamp: number;
    desc: string;
    detail: string;
}

const SpiderStatus = {
    Starting: 'Starting', Running: 'Running', Stopping: 'Stopping', Stopped: 'Stopped'
}

interface State {
    showFilterBox: boolean;
    filterQuery: string;
    spiderStatus: string;
}

export default class HomePage extends Component {

    props: Props;
    state: State;
    colors: string[] = [];
    logList: Log[];
    articleList: Article[];

    constructor(props: Props) {
        super(props);
        this.state = {
            showFilterBox: false,
            filterQuery: null,
            spiderStatus: SpiderStatus.Stopped
        };
    }

    componentWillMount() {
        // bind CTRL + SHIFT + F
        acceleratorManager.register(
            () => this.setState({showFilterBox: true}),
            new CombineKey(KEYS.F, false, true, true)
        );
        for (let i = 0; i < 2; i++)
            this.colors.push(this.randomColor());
        // 刷新spider状态
        this.refreshSpiderStatus();
        // 获取Articles
        resource.get('article', {maxSize: 100})
            .done((articleList) => {
                this.articleList = articleList;
                this.forceUpdate();
            });
        // 获取Logs
        resource.get('log', {maxSize: 100})
            .done((logList) => {
                for (let item of logList) {
                    item.timestamp = parseInt(item.timestamp);
                }
                this.logList = logList;
                this.forceUpdate();
            });
    }

    private refreshSpiderStatus() {
        resource.get('spider.status')
            .done((rep) =>
                this.setState({spiderStatus: rep}));
    }

    private filterQuery() {
        console.log(this.state.filterQuery);
    }

    private filterLogLevel(index: number) {
        console.log(LOG_LEVEL[index]);
    }

    private changeSpider() {
        const { spiderStatus } = this.state;
        // TODO: change spider
    }

    onWSData(data: any) {
        // TODO: complete receive message
        data = JSON.parse(data);
        if (data.type == 'Article') {
            this.articleList.push(data.content);
            this.forceUpdate();
        }
        else if (data.type == 'Log') {
            data.content.timestamp = parseInt(data.content.timestamp);
            this.logList.push(data.content);
            this.forceUpdate();
        }
    }

    render() {
        const { showFilterBox, spiderStatus } = this.state;

        let key = 0;
        // 生成article列表
        let articles = [];
        if (this.articleList) {
            for (let article of this.articleList) {
                article = <Article> article;
                articles.push(
                    rc('div', key++, null,
                        rc(Label, 0, {size: 18, color: 'black', bold: true, href: null}, article.title),
                        rc(Label, 1, null, article.summary),
                        rc(Avatar, 2, {src: article.avatar, size: 20}),
                        rc(Label, 3, {size: 12, color: 'rgb(10, 10, 10)'}, article.author),
                        rc(Label, 4, {size: 12}, article.createTime),
                        rc(Label, 5, {size: 12, style: {float: 'right'}}, '阅读量：' + article.viewCount),
                    )
                );
                articles.push(rc(Division, key++, {length: '100%'}));
            }
            if (articles.length > 0) articles.pop();
        }

        key = 0;
        // 生成log列表
        let logs = [];
        if (this.logList) {
            for (let log of this.logList) {
                log = <Log> log;
                let formattedTime = this.parseTimestamp(log.timestamp);
                logs.push(
                    rc('div', key++, null,
                        rc(Label, 0, {fill: true, type: log.level.toLowerCase()}, log.level),
                        rc(Label, 1, {color: 'black', size: 13}, formattedTime),
                        rc(Label, 2, {style: {display: 'block'}}, log.desc),
                        rc(Label, 3, {style: {display: 'block'}}, log.detail),
                    )
                );
                logs.push(rc(Division, key++, {length: '100%'}));
            }
            if (logs.length > 0) logs.pop();
        }

        const articleNum = this.articleList ? this.articleList.length : 0;
        const logNum = this.logList ? this.logList.length : 0;
        return r('div', {className: styles.root },
            rc(Websocket, 'websocket', {
                url: 'ws://localhost:8080/websocket',
                onMessage: this.onWSData.bind(this) }),
            // left side
            rc('div', 'leftSide', { className: styles.listBox },
                rc('div', 'statistics', { className: styles.statistics },
                    rc(Capsule, 0, { name: '总数据量', value: articleNum, color: this.colors[0] }),
                ),
                rc(ItemList, 1, { width: '100%', height: 'calc(100% - 30px)', scrollToBottom: true }, articles)
            ),
            // right side
            rc('div', 'rightSide', { className: styles.listBox },
                rc('div', 'statistics', { className: styles.statistics },
                    rc(Capsule, 0, { name: '总数据量', value: logNum, color: this.colors[0] }),
                    rc(DropBox, 1, {
                        name: 'Level筛选', selected: 0,
                        items: LOG_LEVEL, style: { float: 'right' },
                        color: this.colors[1], onChange: this.filterLogLevel.bind(this) })
                ),
                rc(ItemList, 1, { width: '100%', height: 'calc(100% - 30px)', scrollToBottom: true }, logs)
            ),
            // filter box
            showFilterBox && rc('div', 'filterWrapper', {
                    className: styles.filterWrapper,
                    onClick: () => this.setState({ showFilterBox: false })
                },
                rc('div', 'filter', {
                        className: styles.filter,
                        onClick: (e: any) => e.stopPropagation()
                    },
                    rc(Input, 0, {
                        focus: true,
                        multiLine: true,
                        backgroundColor: 'rgba(0, 0, 0, 0)',
                        onChange: (value: string) => this.setState({filterQuery: value}),
                        style: {
                            width: 'calc(100% - 25px)',
                            color: 'white',
                            borderBottom: '1px solid rgb(200, 200, 200)'
                        }
                    }),
                    rc(Button, 1, {
                        type: 'search',
                        onClick: this.filterQuery.bind(this),
                        style: { borderRadius: '5px', color: 'white' } })
                )
            ),
            // spider status
            rc('div', 'spiderStataus', {className: styles.spiderStatus},
                rc(Label, 'name', {color: 'red', size: 18}, 'CSDN Spider'),
                rc(Label, 'status', null, spiderStatus),
                rc(Button, 'control', {
                    type: spiderStatus == SpiderStatus.Running ? 'pause-circle' : 
                            (spiderStatus == SpiderStatus.Stopped) ? 'play-circle' : 'disable',
                    onClick: this.changeSpider.bind(this),
                    style: {verticalAlign: 'middle', borderRadius: '50%'}})
            )
        );
    }

    /**
     * 将时间戳转变为格式化字符串
     * @param timestamp 
     */
    private parseTimestamp(timestamp: number) {
        let date = new Date(timestamp);
        return date.getFullYear() + '/' + date.getMonth() + '/' + date.getDate()
            + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
    }
    
    private randomColor() {
        let clr = '#';
        const v = '0123456789abcdef';
        for (let i = 0, limit = 6; i < limit; i++) {
            clr += v[Math.round(Math.random() * 16)];
        }
        return clr;
    }

}