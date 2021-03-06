
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
import resource, { Page } from '../core/Resource';
import acceleratorManager, { KEYS, CombineKey } from '../core/AcceleratorManager';
import Loader from '../com/Loader';
import TopPanel from '../com/TopPanel';
import { Row } from '../com/Layout';
import RawText from '../com/RawText';

const Websocket = <any> require('react-websocket')
const styles = <any> require('./HomePage.css');

const LOG_LEVEL = ['Info', 'Warn', 'Error'];

const SpiderStatus = {
    Starting: 'Starting', Running: 'Running', Stopping: 'Stopping', Stopped: 'Stopped'
}

const PAGE_SIZE = 40;

interface Article {
    title: string;
    summary: string;
    avatar: string;
    author: string;
    createTime: string;
    readCount: number;
    content: string;
}

interface Log {
    level: string;
    timestamp: number;
    desc: string;
    detail: string;
}

interface State {
    showFilterBox: boolean;
    filterQuery: string;
    spiderStatus: string;

    logCount: number;
    articleCount: number;

    loadingLogs: boolean;
    loadingArticles: boolean;

    detailContent: ReactType;
}

/**
 * TODO: 控制列表数目，提高性能
 */
export default class HomePage extends Component {

    props: Props;
    state: State;

    logPage = 0;
    logTotalPage = 0;
    logList: Log[] = [];

    articlePage = 0;
    articleTotalPage = 0;
    articleList: Article[] = [];

    constructor(props: Props) {
        super(props);
        this.state = {
            showFilterBox: false,
            filterQuery: null,
            spiderStatus: SpiderStatus.Stopped,
            logCount: 0,
            articleCount: 0,
            loadingArticles: false,
            loadingLogs: false,
            detailContent: null
        };
    }

    componentWillMount() {
        // bind CTRL + SHIFT + F
        acceleratorManager.register(
            () => this.setState({showFilterBox: true}),
            new CombineKey(KEYS.F, false, true, true)
        );
        // 刷新spider状态
        this.refreshSpiderStatus();

        resource.get('article.count').done((articleCount) => {
            this.articleTotalPage = Math.floor(articleCount / PAGE_SIZE)
                + (articleCount % PAGE_SIZE == 0 ? 0 : 1);
            this.loadMoreArticles();
            this.setState({articleCount: articleCount});
        });

        resource.get('log.count').done((logCount) => {
            this.logTotalPage = Math.floor(logCount / PAGE_SIZE)
                + (logCount % PAGE_SIZE == 0 ? 0 : 1);
            this.loadMoreLogs();
            this.setState({logCount: logCount});
        });
    }

    /**
     * 加载更多article数据，并设置loading动画
     */
    private loadMoreArticles() {
        if (this.articlePage < this.articleTotalPage) {
            this.setState({loadingArticles: true});
            window.onmousewheel = () => false; // 禁用滚动
            resource.get('article', {page: this.articlePage, size: PAGE_SIZE})
            .done((page: Page) => {
                this.articlePage++;
                // 将请求到的数据添加到list头部
                this.articleList = this.articleList.concat(page.content);

                this.setState({loadingArticles: false});
                window.onmousewheel = null;
                this.forceUpdate();
            });
        }
    }

    /**
     * 加载更多log数据，并设置loading动画
     */
    private loadMoreLogs() {
        if (this.logPage < this.logTotalPage) {
            this.setState({loadingLogs: true});
            window.onmousewheel = () => false; // 禁用滚动
            resource.get('log', {page: this.logPage, size: PAGE_SIZE})
                .done((page: Page) => {
                    this.logPage++;
                    // 将timestamp字段转换为number类型
                    let logList = <any[]> page.content;
                    logList.forEach((item) =>
                        item.timestamp = parseInt(item.timestamp));
                    // 将请求到的数据添加到list头部
                    this.logList = this.logList.concat(logList);
    
                    this.setState({loadingLogs: false});
                    window.onmousewheel = null;
                    this.forceUpdate();
                });
        }
    }

    /**
     * 处理即时推送来的消息
     * 消息类型有：Article, Log, Spider.Status
     * @param data 
     */
    private onWSData(data: any) {
        data = JSON.parse(data);
        const { type, content } = data;
        switch (type) {
            case 'Article': {
                this.articleList.unshift(content); // 头部插入
                this.state.articleCount++;
                this.forceUpdate();
                break;
            }
            case 'Log': {
                content.timestamp = parseInt(content.timestamp);
                this.logList.unshift(content); // 头部插入
                this.state.logCount++;
                this.forceUpdate();
                break;
            }
            case 'Spider.Status': {
                this.setState({spiderStatus: content});
                break;
            }
        }
    }

    /**
     * 获取Spider状态
     */
    private refreshSpiderStatus() {
        resource.get('spider.status')
            .done((rep) =>
                this.setState({spiderStatus: rep}));
    }

    /**
     * TODO: to complete
     */
    private filterQuery() {
        console.log(this.state.filterQuery);
    }

    /**
     * TODO: to complete
     * @param index 
     */
    private filterLogLevel(index: number) {
        console.log(LOG_LEVEL[index]);
    }

    private changeSpider() {
        const { spiderStatus } = this.state;
        if (spiderStatus == SpiderStatus.Running) {
            // 尝试停止spider
            resource.get('spider.stop');
        }
        else if (spiderStatus == SpiderStatus.Stopped) {
            // 尝试启动spider
            resource.get('spider.start');
        }
    }

    render() {
        const { showFilterBox, spiderStatus,
            articleCount, logCount,
            loadingArticles, loadingLogs,
            detailContent
        } = this.state;

        let key = 0;
        // 生成article列表
        let articles = [];
        if (this.articleList) {
            for (let article of this.articleList) {
                article = <Article> article;
                articles.push(
                    rc('div', key++, { onClick: () => {
                            let tmp = [
                                rc(Row, 0, null,
                                    rc(Label, 0, {fill: 'gray'}, 'Title'),
                                    rc(Label, 1, null, article.title)
                                    ),
                                rc(Row, 1, null,
                                    rc(Label, 0, {fill: 'gray'}, 'Author'),
                                    rc(Label, 1, null, article.author),
                                    rc(Label, 2, {fill: 'gray'}, 'Read Count'),
                                    rc(Label, 3, null, article.readCount),
                                    ),
                                rc(Row, 2, null,
                                    rc(Label, 0, {fill: 'gray'}, 'Create Time'),
                                    rc(Label, 1, null, article.createTime)
                                    ),
                                rc(Row, 3, null,
                                    rc(Label, 0, {fill: 'gray'}, 'Content'),
                                    rc('div', 1, {
                                        dangerouslySetInnerHTML: {__html: article.content},
                                        style: { position: 'relative', display: 'inline-block', border: '1px solid gray', borderRadius: '5px' } })
                                    ),
                            ];
                            this.setState({detailContent: tmp});
                        } },
                        rc(Label, 0, {size: 18, color: 'black', bold: true, href: null}, article.title),
                        rc(Label, 1, {noWrap: true}, article.summary),
                        rc(Avatar, 2, {src: article.avatar, size: 20}),
                        rc(Label, 3, {size: 12, color: 'rgb(10, 10, 10)'}, article.author),
                        rc(Label, 4, {size: 12}, article.createTime),
                        rc(Label, 5, {size: 12, style: {float: 'right'}}, '阅读量：' + article.readCount),
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
                    rc('div', key++, { onClick: () => {
                            let tmp = [
                                rc(Row, 0, null,
                                    rc(Label, 0, {fill: 'gray'}, 'Log Level'),
                                    rc(Label, 1, {fill: '', type: log.level}, log.level)
                                    ),
                                rc(Row, 1, null,
                                    rc(Label, 0, {fill: 'gray'}, 'Timestamp'),
                                    rc(Label, 1, null, formattedTime)
                                    ),
                                rc(Row, 2, null,
                                    rc(Label, 0, {fill: 'gray'}, 'Description'),
                                    rc(Label, 1, null, log.desc)
                                    ),
                                rc(Row, 3, null,
                                    rc(Label, 0, {fill: 'gray'}, 'Detail'),
                                    rc(RawText, 1, {value: log.detail})
                                    ),
                            ];
                            this.setState({detailContent: tmp});
                        } },
                        rc(Label, 0, {fill: true, type: log.level}, log.level),
                        rc(Label, 1, {color: 'black', size: 13}, formattedTime),
                        rc(Label, 2, {style: {display: 'block'}}, log.desc),
                        rc(Label, 3, {noWrap: true, style: {display: 'block'}}, log.detail),
                    )
                );
                logs.push(rc(Division, key++, {length: '100%'}));
            }
            if (logs.length > 0) logs.pop();
        }

        return r('div', {className: styles.root },
            detailContent &&
                rc(TopPanel, 'detailPanel', {onExit: () => this.setState({detailContent: null})}, detailContent),
            rc(Websocket, 'websocket', {
                url: 'ws://ai.uppfind.com:80/csdn2/websocket',
                onMessage: this.onWSData.bind(this) }),
            // left side
            rc('div', 'leftSide', { className: styles.listBox },
                rc('div', 'statistics', { className: styles.statistics },
                    rc(Capsule, 0, { name: '总数据量', value: articleCount, color: '#FA8235' }),
                    rc(Capsule, 1, { name: '已加载', value: this.articleList.length, color: '#BA82F5' }),
                    loadingArticles && rc(Loader, 2, { size: 12 }),
                ),
                rc(ItemList, 1, {
                    width: '100%', height: 'calc(100% - 30px)',
                    onScrollOverBottom: this.loadMoreArticles.bind(this) }, articles)
            ),
            // right side
            rc('div', 'rightSide', { className: styles.listBox },
                rc('div', 'statistics', { className: styles.statistics },
                    rc(Capsule, 0, { name: '总数据量', value: logCount, color: '#FA8235' }),
                    rc(Capsule, 1, { name: '已加载', value: this.logList.length, color: '#BA82F5' }),
                    loadingLogs && rc(Loader, 2, { size: 12 }),
                    rc(DropBox, 3, {
                        name: 'Level筛选', selected: 0,
                        items: LOG_LEVEL, style: { float: 'right' },
                        color: '#092F5F', onChange: this.filterLogLevel.bind(this) })
                ),
                rc(ItemList, 1, {
                    width: '100%', height: 'calc(100% - 30px)',
                    onScrollOverBottom: this.loadMoreLogs.bind(this) }, logs)
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
    
}