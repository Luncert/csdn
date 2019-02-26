
import { r, rc } from '../util/react-helper';
import { Component, Props } from '../com/Component';
import ItemList from '../com/ItemList';
import Avatar from '../com/Avatar';
import Label from '../com/Label';
import Division from '../com/Division';
import Input from '../com/Input';
import Button from '../com/Button';
import Capsule from '../com/Capsule';
import acceleratorManager, { KEYS, CombineKey } from '../core/AcceleratorManager';

const url = require('../res/img/author.jpg');
const styles = <any> require('./HomePage.css');

let testArticleData = [
    {
        title: '让人崩溃，程序员和工程师的对决！',
        summary: '项目要启动，经理要通过工程师定方案，定完以后就要交个程序员去做产品，但是工程师要改一个小细节，程序员可',
        avatar: url,
        author: '小可爱程序猿我我',
        createTime: '4天前',
        viewCount: 38
    },
    {
        title: '让人崩溃，程序员和工程师的对决！',
        summary: '项目要启动，经理要通过工程师定方案，定完以后就要交个程序员去做产品，但是工程师要改一个小细节，程序员可',
        avatar: url,
        author: '小可爱程序猿我我',
        createTime: '4天前',
        viewCount: 38
    },
    {
        title: '让人崩溃，程序员和工程师的对决！',
        summary: '项目要启动，经理要通过工程师定方案，定完以后就要交个程序员去做产品，但是工程师要改一个小细节，程序员可',
        avatar: url,
        author: '小可爱程序猿我我',
        createTime: '4天前',
        viewCount: 38
    },
    {
        title: '让人崩溃，程序员和工程师的对决！',
        summary: '项目要启动，经理要通过工程师定方案，定完以后就要交个程序员去做产品，但是工程师要改一个小细节，程序员可',
        avatar: url,
        author: '小可爱程序猿我我',
        createTime: '4天前',
        viewCount: 38
    },
    {
        title: '让人崩溃，程序员和工程师的对决！',
        summary: '项目要启动，经理要通过工程师定方案，定完以后就要交个程序员去做产品，但是工程师要改一个小细节，程序员可',
        avatar: url,
        author: '小可爱程序猿我我',
        createTime: '4天前',
        viewCount: 38
    },
]

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

interface Log {
    level: string;
    timestamp: number;
    desc: string;
    detail: string;
}

let testLogData = [
    {
        level: LogLevel.Info,
        timestamp: 1551108443953,
        desc: 'spider started',
        detail: 'no more details'
    },
    {
        level: LogLevel.Warn,
        timestamp: 1551108443953,
        desc: 'spider started',
        detail: 'no more details'
    },
    {
        level: LogLevel.Error,
        timestamp: 1551108443953,
        desc: 'spider started',
        detail: 'no more details'
    },
    {
        level: LogLevel.Info,
        timestamp: 1551108443953,
        desc: 'spider started',
        detail: 'no more details'
    },
    {
        level: LogLevel.Info,
        timestamp: 1551108443953,
        desc: 'spider started',
        detail: 'no more details'
    },
];

interface CusState {
    showFilterBox: boolean;
}

export default class HomePage extends Component {

    props: Props;
    state: CusState;

    constructor(props: Props) {
        super(props);
        this.state = {
            showFilterBox: false
        };
    }

    componentWillMount() {
        // bind CTRL + SHIFT + F
        acceleratorManager.register(
            () => this.setState({showFilterBox: true}),
            new CombineKey(KEYS.F, false, true, true)
        );
    }

    render() {
        const { showFilterBox } = this.state;

        let key = 0;
        // 生成article列表
        let articleList = [];
        for (let article of testArticleData) {
            article = <Article> article;
            articleList.push(
                rc('div', key++, null,
                    rc(Label, 0, {size: 18, color: 'black', bold: true, href: null}, article.title),
                    rc(Label, 1, null, article.summary),
                    rc(Avatar, 2, {src: article.avatar, size: 20}),
                    rc(Label, 3, {size: 12, color: 'rgb(10, 10, 10)'}, article.author),
                    rc(Label, 4, {size: 12}, article.createTime),
                    rc(Label, 5, {size: 12, style: {float: 'right'}}, '阅读量：' + article.viewCount),
                )
            );
            articleList.push(rc(Division, key++, {length: '100%'}));
        }
        if (articleList.length > 0) articleList.pop();

        key = 0;
        // 生成log列表
        let logList = [];
        for (let log of testLogData) {
            log = <Log> log;
            let formattedTime = this.parseTimestamp(log.timestamp);
            logList.push(
                rc('div', key++, null,
                    rc(Label, 0, {fill: true, type: log.level.toLowerCase()}, log.level),
                    rc(Label, 1, {color: 'black', size: 13}, formattedTime),
                    rc(Label, 2, {style: {display: 'block'}}, log.desc),
                    rc(Label, 3, {style: {display: 'block'}}, log.detail),
                )
            );
            logList.push(rc(Division, key++, {length: '100%'}));
        }
        if (logList.length > 0) logList.pop();

        return r('div', {className: styles.root},
            rc('div', 'leftSide', { style: {
                    height: 'calc(100% - 20px)',
                    width: 'calc(50% - 20px)',
                    margin: '10px',
                    display: 'inline-block',
                    verticalAlign: 'top'
                } },
                rc('div', 0, { className: styles.statistics },
                    rc(Capsule, 0, { name: '总数据量', value: testArticleData.length, color: this.randomColor() })
                ),
                rc(ItemList, 1, { width: '100%', height: 'calc(100% - 30px)' }, articleList)
            ),
            rc('div', 'rightSide', { style: {
                    height: 'calc(100% - 20px)',
                    width: 'calc(50% - 20px)',
                    margin: '10px',
                    display: 'inline-block',
                    verticalAlign: 'top'
                } },
                rc('div', 0, { className: styles.statistics },
                    rc(Capsule, 0, { name: '总数据量', value: testLogData.length, color: this.randomColor() })
                ),
                rc(ItemList, 1, { width: '100%', height: 'calc(100% - 30px)' }, logList)
            ),
            showFilterBox && rc('div', 'filterWrapper', {
                    className: styles.filterWrapper,
                    onClick: () => this.setState({ showFilterBox: false })
                },
                rc('div', 'filter', {
                        className: styles.filter,
                        onClick: (e: any) => e.stopPropagation()
                    },
                    rc(Input, 0, {
                        backgroundColor: 'rgba(0, 0, 0, 0)',
                        style: {
                            width: 'calc(100% - 25px)',
                            color: 'white',
                            borderBottom: '1px solid rgb(200, 200, 200)'
                        }
                    }),
                    rc(Button, 1, { type: 'search', style: { borderRadius: '5px', color: 'white' } })
                )
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