
import st from '../util/string-helper';
import { r } from '../util/react-helper';
import { Props, Component } from './Component';
const styles = <any> require('./Division.css');

interface CusProps extends Props {
    vertical: boolean;
    length: string;
}

/**
 * props:
 * * vertical: 分割线是否在垂直方向，默认false
 * * length: 指定分割线长度，默认(100% - 10px)
 */
export default class Division extends Component {
    
    props: CusProps;

    render() {
        const { vertical=false, length, style={} } = this.props;
        if (length) {
            if (vertical) style['height'] = length;
            else style['width'] = length;
        }
        return r('div', {
            className: st.join(styles.division,
                vertical ? styles.vertical : styles.horizontal),
            style: style
        });
    }

}
