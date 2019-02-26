
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

        let className = st.join(styles.button, type ? 'icon-' + type : null);
        let disable = type == 'disable';
        if (disable) {
            className = st.join(className, styles.disable);
        }
        style['backgroundColor'] = fill ? 'rgb(50, 60, 80)' : '';
        return r('div', {
            className: className,
            onClick: !disable && this.props['onClick'],
            onDoubleClick: !disable && this.props['onDoubleClick'],
            onMouseDown: !disable && this.props['onMouseDown'],
            onMouseUp: !disable && this.props['onMouseUp'],
            style: style
        }, children);
    }
    
}