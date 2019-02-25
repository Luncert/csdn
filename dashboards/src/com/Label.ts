
import st from '../util/string-helper';
import { r } from '../util/react-helper';
import { Props, Component } from './Component';

const styles = <any >require('./Label.css');

interface CusProps extends Props {
    type: string;
    size: number;
    color: string;
    fill: boolean;
    selected: boolean;
    bold: boolean;
    href: string;
}

const LABEL_TYPE = { INFO: 'info', WARN: 'warn', ERROR: 'error' };

export {
    LABEL_TYPE,
}

/**
 * props:
 * * type: 标签类型，决定标签基本样式，默认LABEL_TYPE.INFO
 * * size: 设置字体大小，默认15px
 * * color: 字体颜色
 * * fill: 是否填充背景
 * * selected: 文字是否可以选中，默认true
 * * bold: 是否加粗，默认false
 * * href: 绑定链接，并改变光标样式
 */
export default class Label extends Component {

    props: CusProps;

    render() {
        const {
            type=LABEL_TYPE.INFO,
            size=15, color,
            fill=false,
            selected=true,
            bold=false,
            href,
            style={}, children } = this.props;

        style['fontSize'] = size + 'px';
        if (color) {
            style['color'] = color;
        }
        if (bold) {
            style['fontWeight'] = 'bold';
        }
        if (href) {
            style['cursor'] = 'pointer';
        }
        style['userSelect'] = selected ? 'auto' : 'none';

        return r('div', {
            className: st.join(styles.label, styles[type], fill ? styles.fill : ''),
            onClick: () => href && window.open(href),
            style: style}, children);
    }

}
