
import { Props, Component } from './Component';
import { r } from '../util/react-helper';

const styles = <any> require('./RawText.css');

interface CusProps extends Props {
    value: string;
}

/**
 * * props:
 *  * value:
 */
export default class RawText extends Component {

    props: CusProps;

    render() {
        const { value, style } = this.props;
        return r('pre', {className: styles.root, style: style}, value);
    }
    
}