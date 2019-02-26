
import { Props, Component } from './Component';
import { r } from '../util/react-helper';
import st from '../util/string-helper';

const styles = require('./Button.css');

interface CusProps extends Props {
    type: string;
    fill: boolean;
}

/**
 * * props:
 *  * type: 按钮样式
 *  * fill: 有无填充，默认false
 */
export default class Button extends Component {

    props: CusProps;

    render() {
        const { type, fill=false, style={}, children } = this.props;

        style['backgroundColor'] = fill ? 'rgb(50, 60, 80)' : '';
        return r('div', {
            className: st.join(styles.button, type ? 'icon-' + type : null),
            onClick: this.props['onClick'],
            onDoubleClick: this.props['onDoubleClick'],
            onMouseDown: this.props['onMouseDown'],
            onMouseUp: this.props['onMouseUp'],
            style: style
        }, children);
    }
    
}