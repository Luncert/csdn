
import { Props, Component } from './Component';
import { r } from '../util/react-helper';
const styles = require('./Input.css');

interface CusProps extends Props {
    value: string;
    placeholder: string;
    backgroundColor: string;
    focus: boolean;
    multiLine: boolean;
    onChange: Function;
}

/**
 * * props:
 *  * value: 文本内容
 *  * placeholder: 提示
 *  * backgroundColor: 背景颜色
 *  * focus: 是否自动聚焦，默认false
 *  * multiLine: 是否自动换行，默认false
 *  * onChange: 内容改变事件，同react原生的onChange相同
 */
export default class Input extends Component {

    props: CusProps;
    input: any;

    componentDidMount() {
        if (this.props['focus'] && this.input)
            this.input.focus();
    }

    render() {
        const { value, placeholder,
            backgroundColor, multiLine,
            onChange, style={} } = this.props;
        style['backgroundColor'] = backgroundColor;
        return r(multiLine ? 'span' : 'input', {
                contentEditable: multiLine,
                ref: (input: any) => this.input = input,
                spellCheck: false,
                key: 0,
                style: style,
                className: styles.input,
                placeholder: placeholder,
                value: value,
                onChange: (e: any) => { if (!multiLine) onChange(e.target.value)},
                onInput: (e: any) => { if (multiLine) onChange(e.target.innerHTML) }
            });
    }

}