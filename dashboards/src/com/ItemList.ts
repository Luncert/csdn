
import { r, rc } from '../util/react-helper';
import { Component, Props } from './Component';

const styles = <any> require('./ItemList.css');

interface CusProps extends Props {
    width: String;
    height: String;
    fixScroll: number;
    onScrollOverTop: () => void;
    onScrollOverBottom: () => void;
}

/**
 * * props
 *  * width
 *  * height
 *  * fixScroll: 设置是否锁定滚动条，即自动显示最新数据，0：false，1：锁定顶部，2：锁定底部
 *  * onScrollOverTop: 设定滚动到顶部时的动作
 *  * onScrollOverBottom: 设定滚动到底部时的动作
 */
export default class ItemList extends Component {

    props: CusProps;
    root: HTMLElement;

    componentWillUnmount() {
        this.root.onscroll = null;
    }

    componentDidMount() {
        const { onScrollOverTop, onScrollOverBottom } = this.props;
        let beforeScrollTop = this.root.scrollTop;
        this.root.onscroll = () => {
            let direction = this.root.scrollTop - beforeScrollTop;
            if (direction > 0 && Math.ceil(this.root.scrollTop) >=
                this.root.scrollHeight - this.root.clientHeight) {
                onScrollOverBottom && onScrollOverBottom();
            }
            else if (direction < 0 && Math.ceil(this.root.scrollTop) == 0) {
                onScrollOverTop && onScrollOverTop();
            }
            beforeScrollTop = this.root.scrollTop;
        };
        this.fixScroll();
    }

    componentDidUpdate() {
        this.fixScroll();
    }

    private fixScroll() {
        switch(this.props.fixScroll) {
            case 1:
                this.root.scrollTop = 0;
                break;
            case 2:
                this.root.scrollTop = this.root.scrollHeight;
                break;
        }
    }

    render() {
        const { width, height, style={}, children } = this.props;
        style['width'] = width;
        style['height'] = height;
        return r('div', {
                className: styles.root,
                style: style,
                ref: (node: HTMLElement) => this.root = node
            },
            children
        );
    }

}