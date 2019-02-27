
import st from '../util/string-helper';
import { r } from '../util/react-helper';
import { Props, Component } from './Component';

const styles = <any >require('./Label.css');

interface CusProps extends Props {
    type: string;
    size: number;
    color: string;
    fill: string;
    selected: boolean;
    bold: boolean;
    href: string;
    noWrap: boolean;
    border: string;
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
 * * fill: 背景颜色，不设置时无填充
 * * selected: 文字是否可以选中，默认true
 * * bold: 是否加粗，默认false
 * * href: 绑定链接，并改变光标样式
 * * noWrap: 不换行，默认false
 * * border: border颜色，不设置时无border
 */
export default class Label extends Component {

    props: CusProps;

    render() {
        const {
            type='',
            size=15, color,
            fill,
            selected=true,
            bold=false,
            href, noWrap=false, border,
            style={}, children } = this.props;

        let className = st.join(styles.label, styles[type]);
        if (fill != undefined) {
            className = st.join(className, styles.fill);
            if (fill.length > 0)
                style['backgroundColor'] = fill;
        }
        noWrap && (className = st.join(className, styles.noWrap));
        style['fontSize'] = size + 'px';
        color && (style['color'] = color);
        bold && (style['fontWeight'] = 'bold');
        href && (style['cursor'] = 'pointer');
        border && (style['border'] = '1px solid ' + border);
        style['userSelect'] = selected ? 'auto' : 'none';

        return r('div', {
            className: className,
            onClick: () => href && window.open(href),
            style: style}, children);
    }

}
