
import { Props, Component } from './Component';
import { r } from '../util/react-helper';
const styles = require('./Input.css');

interface CusProps extends Props {
    value: string;
    placeholder: string;
    backgroundColor: string;
    on_change: Function;
}

/**
 * * props:
 *  * value: 文本内容
 *  * placeholder: 提示
 *  * backgroundColor: 背景颜色
 *  * on_change: 内容改变事件，同react原生的onChange相同
 */
export default class Input extends Component {

    props: CusProps;

    render() {
        const { value, placeholder, backgroundColor, on_change, style } = this.props;
        style['backgroundColor'] = backgroundColor;
        return r('input', {
                spellCheck: false,
                key: 0,
                style: style,
                className: styles.input,
                placeholder: placeholder,
                value: value,
                onChange: on_change,
            });
    }

}