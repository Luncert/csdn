
import { r } from '../util/react-helper';
import { Props, Component } from './Component';

const styles = <any> require('./Avatar.css');

interface CusProps extends Props {
    size: number;
    src: string;
}
/**
 * * props:
 *  * src
 *  * size
 */
export default class Avatar extends Component {

    props: CusProps;

    render() {
        let { src, size, style, children } = this.props;
        if (!style) style = {};
        style['backgroundImage'] = 'url(' + src + ')';
        if (size > 0) {
            style['width'] = size + 'px';
            style['height'] = size + 'px';
        }
        return r('div', {className: styles.avatar, style: style}, children);
    }

}